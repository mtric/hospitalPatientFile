package patientFile;
/*
 * MbLogin.java
 * JSF 2.3 template
 */

import java.io.Serializable;

import java.util.Date;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

/**
 * Backing Bean der JSF-Seite login.xhtml
 *
 * @author Wolfgang Lang, Michael Kaleve, Eric Walter
 * @version 1.1, 2019-06-13
 */
@Named("login")
@SessionScoped
public class MbLogin extends HttpServlet implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String CLASSNAME = getClass().getName();
	final static Locale DEFAULT_LOCALE = Locale.GERMAN;
	private Locale locale = DEFAULT_LOCALE;
	private String kennung = "";
	private String pw = "";
	private boolean loggedIn = false;
	private boolean english = false;

	/**
	 * The constructor
	 */
	public MbLogin() {
		System.out.println("Constructor " + CLASSNAME + " at " + new Date() + "...");
	}

	/**
	 * Setter method login name
	 * @param s The login name
	 */
	public void setKennung(String s) {
		kennung = s;
	}

	/**
	 * Getter method for login name
	 * @return Returns the login name
	 */
	public String getKennung() {
		return kennung;
	}

	/**
	 * Setter method for password
	 * @param s The password string
	 */
	public void setPw(String s) {
		pw = s;
	}

	/**
	 * Getter method for password
	 * @return Returns the password string
	 */
	public String getPw() {
		return pw;
	}

	/**
	 * Getter method for system date
	 * @return Returns the system date
	 */
	public Date getDate() {
		return new Date();
	}

	/**
	 * Getter method for logged in boolean
	 * @return Returns the boolean variable
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * Getter method for language boolean
	 * @return Returns the boolean variable
	 */
	public boolean isEnglish() {
		return english;
	}

	/**
	 * This method changes the language boolean, when the user clicked
	 * on the menu item
	 * @param i Integer value
	 */
	public void Language(int i) {
		if (i == 1) {
			english = false;
			System.out.println("Language changed to german");
		} else if (i == 2) {
			english = true;
			System.out.println("Language changed to english");
		}
	}

	/**
	 * This method establishes a connection a database to check for user name and password.
	 * When a database entry exists the "role" will be returned.
	 * @return Returns the outcome String with value = "admin" or "user"
	 */
	public String loginCheck(){
		String sOutcome = null;
		System.out.println("loginCheck()...");
		kennung = kennung.trim();
		pw = pw.trim();		

		sOutcome = LoginDAO.validate(kennung, pw);
		if(!sOutcome.isEmpty()) {
			HttpSession session = SessionUtils.getSession(true);
			session.setAttribute("username", kennung);
			loggedIn = true;
			return sOutcome;
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username or Password",
							"Please enter correct Username and Password"));
			return "login.xhtml";
		}		
	}

	/**
	 * Method to end the session and set variable loggedIn = false
	 * @param ae
	 */
	public void aclLogout(ActionEvent ae) {
		HttpSession session = SessionUtils.getSession(true);
		session.invalidate();
		loggedIn = false;
	}

	/**
	 * Initialization
	 */
	@PostConstruct
	public void init() {
		System.out.println("@PostConstruct." + CLASSNAME);
		locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale == null)
			locale = DEFAULT_LOCALE;
		System.out.println("locale: " + locale);
	}
	
	/**
	 * The pre render action
	 */
	public void preRenderAction() {
		System.out.println("MbLogin.preRenderAction");
	}
	
	/**
	 * The post render action
	 */
	public void postRenderAction() {
		System.out.println("MbLogin.postRenderAction");
	}

}
