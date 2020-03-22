package patientFile;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class represents a helper class for a Http User Session
 * 
 * @author Michael Kaleve, Eric Walter
 * @version 1.1, 2019-06-17
 */
public class SessionUtils {

	/**
	 * Getter method for http session
	 * @param session The session boolean
	 * @return Returns http session
	 */
	public static HttpSession getSession(Boolean session) {
		return (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
	}

	/**
	 * Getter method for http servlet request
	 * @return Returns http servlet request
	 */
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}

	/**
	 * Getter method for user name for the session
	 * @return Returns the user name for the session
	 */
	public static String getUserName() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		return session.getAttribute("username").toString();
	}

	/**
	 * Getter method for user ID for the session
	 * @return Returns the user ID for the session
	 */
	public static String getUserId() {
		HttpSession session = getSession(true);
		if (session != null)
			return (String) session.getAttribute("userid");
		else
			return null;
	}
}