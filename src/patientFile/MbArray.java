package patientFile;

import static java.lang.System.out;

/*
 * MbArray.java
 * JSF Tabellen Demo
 */


import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Backing bean der JSF-Seite browse_admin.xhtml und browse_user.xhtml für
 * das Handling der Array Liste, auf welche die Datatable zugreift
 *
 * @author Michael Kaleve, Eric Walter
 * @version 1.1., 2019-06-13
 */
@Named( "arr" )
@SessionScoped
public class MbArray implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Util ist eine Hilfsklasse, die u. a. den Verbindungsaufbau zur Datenbank
	 * vereinfacht:
	 */
	private static Util util = new Util();
	private static Connection con = null;
	private static Statement stm = null;
	private static ResultSet rs = null;
	private static int arraySize = 0;
	/**
	 * Query to pull data from database
	 */	
	private static String SQL_select = "";
	
	/**
	 * ArrayList to save the patient objects from the database
	 */
	private static ArrayList<Patient> patientList = new ArrayList<Patient>();

	public MbArray() {
		System.out.println( "MbArray.<init>..."  );
		System.out.println( (new Date()).toString() );
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Post construction initialization
	 */
	@PostConstruct
	public void init() { 
		fillArray("");
		System.out.println( "@PostConstruct.MbArray" ); }

	public void preRenderAction()  { System.out.println( "MbArray.preRenderAction"  ); }  
	public void postRenderAction() { System.out.println( "MbArray.postRenderAction" ); } 

	/*--------------------------------------------------------------------------*/

	/**
	 * Method to return the ArrayList filled with Objects<Patient>
	 * @return Returns an ArrayList with Objects with the datatype Patient
	 */
	public ArrayList<Patient> getPatient(){
		return patientList;
	}
	
	/**
	 * Method to return size of the array list
	 * @return Returns size of the array list
	 */
	public int getArraySize() {
		System.out.println("Länge des Arrays: " + arraySize);
		return patientList.size();
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Method to fill the patient ArrayList with Objects, which are filled with data from the database
	 */
	public static void fillArray(String SQL_select) {
		
		patientList.clear();

		if (util != null)
			con = util.getCon();
		if (con != null) {
			try { if(!SQL_select.isEmpty()) {
				stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stm.executeQuery(SQL_select);
				while (rs.next()) {
					Arrays.asList(patientList.add(new Patient(rs.getString("vorName"),
							rs.getString("nachName"), rs.getString("email"), rs.getDate("geburtsdatum"), 
							rs.getString("telefon"), rs.getString("krankenkasse"), rs.getInt("patientID"),
							rs.getDate("einlieferungsdatum"),rs.getDate("entlassungsdatum"),
							rs.getString("diagnose"), rs.getString("stationsName"), rs.getString("therapieName"),
							rs.getString("medikament"),rs.getInt("blobID"))));

				}
				arraySize = patientList.size();
				System.out.println("Länge der Liste: "+arraySize);
				rs.close(); con.close();}				

			} catch (Exception ex) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
				out.println("Error: " + ex);
				ex.printStackTrace();
			}
		} else {
			arraySize = 0;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Exception", "Keine Verbindung zur Datenbank (Treiber nicht gefunden?)"));
			out.println("Keine Verbingung zur Datenbank");
		}
	}
	
	/**
	 * Method to save different data in the patient array list based on the search query
	 */
	public void load() {
		MbArray.patientList = loadResult(SQL_select);
	}
	
	/**
	 * Method to delete all entries of the array list
	 */
	public static void clear() {
		MbArray.patientList.clear();
	}
	
	/**
	 * Method to refill the list with some different data from the database
	 * @return Returns the updated ArrayList
	 */
	static ArrayList<Patient> loadResult(String SQL_select) {
		
		ArrayList<Patient> result = new ArrayList<Patient>();
		
		if (util != null)
			con = util.getCon();
		if (con != null) {
			try {
				stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stm.executeQuery(SQL_select);
				while (rs.next()) {
					result.add(new Patient(rs.getString("vorName"),
							rs.getString("nachName"), rs.getString("email"), rs.getDate("geburtsdatum"), 
							rs.getString("telefon"), rs.getString("krankenkasse"), rs.getInt("patientID"),
							rs.getDate("einlieferungsdatum"),rs.getDate("entlassungsdatum"),
							rs.getString("diagnose"), rs.getString("stationsName"), rs.getString("therapieName"),
							rs.getString("medikament"), rs.getInt("blobID")));

				}
				rs.close(); con.close();

			} catch (Exception ex) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
				out.println("Error: " + ex);
				ex.printStackTrace();
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Exception", "Keine Verbindung zur Datenbank (Treiber nicht gefunden?)"));
			out.println("Keine Verbingung zur Datenbank");
		}
		
		return result;
	}
	
	/**
	 * Method to build the SQL String based on the data from the InputText-fields for Admin
	 * @param vorName The prename
	 * @param nachName The family name
	 * @param email The eMail address
	 * @param telefon The phone number 
	 * @param krankenkasse The health insurance
	 * @param geburtsdatum The birth date
	 * @param einlieferungsdatum The arrival date
	 * @param entlassungsdatum The departure date
	 * @param diagnose The diagnosis
	 * @param stationsName The station name
	 * @param therapieName The therapy name
	 * @param medikament The drug name
	 * @return Returns the SQL String based on the search parameters
	 */
	public static String build_SQLstring(String vorName, String nachName, String email, 
			String telefon, String krankenkasse, String diagnose, String stationsName, 
			String therapieName, String medikament, 
			Date geburtsdatum, Date einlieferungsdatum, Date entlassungsdatum) {
		boolean searchCondition=false;
		SQL_select = "select patientID, vorName, nachName, "
				+ "geburtsdatum, email, telefon, krankenkasse, einlieferungsdatum,"
				+ "entlassungsdatum, diagnose, stationsName, therapieName, medikament, blobID "
				+ "from patient_stamm "
				+ "join patient_behandlung using(patientID) "
				+ "join patient_therapie using(behandlungID) "
				+ "join therapie using(therapieID) "
				+ "join station using(stationID)";
		
		if (!vorName.isEmpty()) {
			searchCondition = true;
			SQL_select = SQL_select + " where vorName like '%" + vorName + "%'";
		}
		
		if (!nachName.isEmpty()) {
			if(searchCondition) {SQL_select = SQL_select + " AND nachName like '%" + nachName + "%'";} 
			else {searchCondition = true;
			SQL_select = SQL_select + " where nachName like '%" + nachName + "%'";}
		}
		
		if (!email.isEmpty()) {
			if(searchCondition) {SQL_select = SQL_select + " AND email like '%" + email + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where email like '%" + email + "%'";}
		}
		
		if (!telefon.isEmpty()) {
			if(searchCondition) {SQL_select = SQL_select + " AND telefon like '%" + telefon + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where telefon like '%" + telefon + "%'";}
		}
		
		if (!krankenkasse.isEmpty()) {
			if(searchCondition) {SQL_select = SQL_select + " AND krankenkasse like '%" + krankenkasse + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where krankenkasse like '%" + krankenkasse + "%'";}
		}
		
		if (!diagnose.isEmpty()) {
			if(searchCondition) {SQL_select = SQL_select + " AND diagnose like '%" + diagnose + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where diagnose like '%" + diagnose + "%'";}
		}
		
		if (!stationsName.isEmpty()) {
			if(searchCondition) {SQL_select = SQL_select + " AND stationsName like '%" + stationsName + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where stationsName like '%" + stationsName + "%'";}
		}
		
		if (!therapieName.isEmpty()) {
			if(searchCondition) {SQL_select = SQL_select + " AND therapieName like '%" + therapieName + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where therapieName like '%" + therapieName + "%'";}
		}
		
		if (!medikament.isEmpty()) {
			if(searchCondition) {SQL_select = SQL_select + " AND medikament like '%" + medikament + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where medikament like '%" + medikament + "%'";}
		}
		
		if (geburtsdatum != null) {
			if(searchCondition) {SQL_select = SQL_select + " AND geburtsdatum like '%" + geburtsdatum + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where geburtsdatum like '%" + geburtsdatum + "%'";}
		}
		
		if (einlieferungsdatum != null) {
			if(searchCondition) {SQL_select = SQL_select + " AND einlieferungsdatum like '%" + einlieferungsdatum + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where einlieferungsdatum like '%" + einlieferungsdatum + "%'";}
		}
		
		if (entlassungsdatum != null) {
			if(searchCondition) {SQL_select = SQL_select + " AND entlassungsdatum like '%" + entlassungsdatum + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where entlassungsdatum like '%" + entlassungsdatum + "%'";}
		}
				
		if (searchCondition) {return SQL_select;}
		else {return "";}
	}	
	
	/**
	 * Method to build the SQL String based on the data from the InputText-fields for User
	 * 
	 * @param vorName The prename
	 * @param nachName The family name
	 * @param email The eMail address
	 * @param telefon The phone number 
	 * @param krankenkasse The health insurance
	 * @param geburtsdatum The birth date
	 * @param einlieferungsdatum The arrival date
	 * @param entlassungsdatum The departure date
	 * @return Returns a string
	 */
	public static String build_SQLstringU(String vorName, String nachName, String email, 
			String telefon, String krankenkasse, Date geburtsdatum, Date einlieferungsdatum, Date entlassungsdatum) {
		boolean searchCondition=false;
		SQL_select = "select patientID, vorName, nachName, "
				+ "geburtsdatum, email, telefon, krankenkasse, einlieferungsdatum,"
				+ "entlassungsdatum, diagnose, stationsName, therapieName, medikament, blobID "
				+ "from patient_stamm "
				+ "join patient_behandlung using(patientID) "
				+ "join patient_therapie using(behandlungID) "
				+ "join therapie using(therapieID) "
				+ "join station using(stationID)";
		
		if (!vorName.isEmpty()) {
			searchCondition = true;
			SQL_select = SQL_select + " where vorName like '%" + vorName + "%'";
		}
		
		if (!nachName.isEmpty()) {
			if(searchCondition) {SQL_select = SQL_select + " AND nachName like '%" + nachName + "%'";} 
			else {searchCondition = true;
			SQL_select = SQL_select + " where nachName like '%" + nachName + "%'";}
		}
		
		if (!email.isEmpty()) {
			if(searchCondition) {SQL_select = SQL_select + " AND email like '%" + email + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where email like '%" + email + "%'";}
		}
		
		if (!telefon.isEmpty()) {
			if(searchCondition) {SQL_select = SQL_select + " AND telefon like '%" + telefon + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where telefon like '%" + telefon + "%'";}
		}
		
		if (!krankenkasse.isEmpty()) {
			if(searchCondition) {SQL_select = SQL_select + " AND krankenkasse like '%" + krankenkasse + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where krankenkasse like '%" + krankenkasse + "%'";}
		}
		
		if (geburtsdatum != null) {
			if(searchCondition) {SQL_select = SQL_select + " AND geburtsdatum like '%" + geburtsdatum + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where geburtsdatum like '%" + geburtsdatum + "%'";}
		}
		
		if (einlieferungsdatum != null) {
			if(searchCondition) {SQL_select = SQL_select + " AND einlieferungsdatum like '%" + einlieferungsdatum + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where einlieferungsdatum like '%" + einlieferungsdatum + "%'";}
		}
		
		if (entlassungsdatum != null) {
			if(searchCondition) {SQL_select = SQL_select + " AND entlassungsdatum like '%" + entlassungsdatum + "%'";}
			else{searchCondition= true;
			SQL_select = SQL_select + " where entlassungsdatum like '%" + entlassungsdatum + "%'";}
		}
				
		if (searchCondition) {return SQL_select;}
		else {return "";}
	}	
}