/*
 * MbBrowse
 * JSF 2.3 DB-Anwendung
 */

package patientFile;

import static java.lang.System.*;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.sql.Date;
import java.sql.PreparedStatement;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.inject.Named;


/**
 * Backing bean der JSF-Seite browse_user.xhtml und browse_admin.xhtml
 *
 * @author Michael Kaleve, Eric Walter, auf der Basis der Vorlage von Prof. Dr.
 *         Wolfgang Lang
 * @version 1.5, 2019-06-22
 */
@Named("MbBrowse")
@SessionScoped
public class MbBrowse implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean connected = false;
	private static HtmlDataTable datatablePatients;
	private String SQL_select = "select * from patient_stamm;";

	/**
	 * Util ist eine Hilfsklasse, die u. a. den Verbindungsaufbau zur Datenbank
	 * vereinfacht:
	 */
	private Util util = new Util();
	private Connection con = null;
	private Statement stm = null;
	private ResultSet rs = null;
	/*--------------------------------------------------------------------------*/
	/*
	 * Variablen für Patienteninformationen
	 */
	private int pID, blobID = 0;
	private String preName, famName, eMail, telNr, healthIns, diagnose, station, therapy, med = "";
	private Date birthDate, inDate, outDate = null;
	private boolean show = false;

	/*--------------------------------------------------------------------------*/
	/**
	 * The constructor
	 */
	public MbBrowse() {
		System.out.println("MbBrowse.<init>...");
	}
	
	/**
	 * Initialization
	 */
	@PostConstruct
	public void init() {
		System.out.println("@PostConstruct.MbBrowse");
		connect();
	}
	/**
	 * The pre render action
	 */
	public void preRenderAction() {
		System.out.println("MbBrowse.preRenderAction");
	}
	
	/**
	 * The post render action
	 */
	public void postRenderAction() {
		System.out.println("MbBrowse.postRenderAction");
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Getter method for data table
	 * @return Returns data table object
	 */
	public HtmlDataTable getDatatablePatients() {
		return MbBrowse.datatablePatients;
	}

	/**
	 * Setter method for data table
	 * @param datatablePatients
	 */
	public void setDatatablePatients(HtmlDataTable datatablePatients) {
		MbBrowse.datatablePatients = datatablePatients;
	}
	/**
	 * Getter method for SQL query string
	 * @return Returns string
	 */
	public String getSQL_select() {
		return SQL_select;
	}
	/**
	 * Setter method for SQL query string
	 * @param s SQL query string
	 */
	public void setSQL_select(String s) {
		SQL_select = s;
	}

	/**
	 * Getter method fpr patient ID
	 * @return Returns patient ID
	 */
	public int getpID() {
		return pID;
	}
	
	/**
	 * Setter method for patient ID
	 * @param n The patient ID integer
	 */
	public void setpID(int n) {
		pID = n;
	}

	/**
	 * Getter for blob ID
	 * @return Returns the blob ID
	 */
	public int getBlobID() {
		return blobID;
	}

	/**
	 * Setter for blobID
	 * @param blobID The blob ID
	 */
	public void setBlobID(int blobID) {
		this.blobID = blobID;
	}
	
	/**
	 * Getter method for prename
	 * @return Returns prename string
	 */
	public String getPreName() {
		return preName;
	}
	
	/**
	 * Setter method for prename
	 * @param s Prename string
	 */
	public void setPreName(String s) {
		preName = s;
	}
	
	/**
	 * Getter method for family name
	 * @return Returns family name string
	 */
	public String getFamName() {
		return famName;
	}
	
	/**
	 * Setter method for family name
	 * @param s Family name string
	 */
	public void setFamName(String s) {
		famName = s;
	}
	
	/**
	 * Getter method for e-Mail
	 * @return Returns e-Mail string
	 */
	public String geteMail() {
		return eMail;
	}
	
	/**
	 * Setter method for e-Mail
	 * @param s E-Mail string
	 */
	public void seteMail(String s) {
		eMail = s;
	}
	
	/**
	 * Getter method for health insurance
	 * @return Returns health insurance string
	 */
	public String getHealthIns() {
		return healthIns;
	}
	
	/**
	 * Setter method for health insurance
	 * @param s Health insurance string
	 */
	public void setHealthIns(String s) {
		healthIns = s;
	}
	
	/**
	 * Getter method for phone number
	 * @return Returns the phone number string
	 */
	public String getTelNr() {
		return telNr;
	}
	
	/**
	 * Setter method for phone number
	 * @param s The phone number string
	 */
	public void setTelNr(String s) {
		telNr = s;
	}
	
	/**
	 * Getter method for birth date
	 * @return Returns birth date object
	 */
	public java.util.Date getBirthDate() {
		return birthDate;
	}
	/**
	 * Setter method for birth date
	 * @param dt Date object
	 */
	public void setBirthDate(java.util.Date dt) {
		if (dt != null)
			birthDate = new Date(dt.getTime());
		else
			birthDate = null;
	}
	
	/**
	 * Getter method for arrival date
	 * @return Returns arrival date object
	 */
	public java.util.Date getInDate() {
		return inDate;
	}
	
	/**
	 * Setter method for arrival date
	 * @param dt Arrival date object
	 */
	public void setInDate(java.util.Date dt) {
		if (dt != null)
			inDate = new Date(dt.getTime());
		else
			inDate = null; // new Date(0L);
	}
	
	/**
	 * Getter method for departure date
	 * @return Returns departure date object
	 */
	public java.util.Date getOutDate() {
		return outDate;
	}
	
	/**
	 * Setter method for departure date
	 * @param dt Departure date object
	 */
	public void setOutDate(java.util.Date dt) {
		if (dt != null)
			outDate = new Date(dt.getTime());
		else
			outDate = null;
	}
	
	/**
	 * Getter method for empty date
	 * @return Returns empty string
	 */
	public String getNoDate() {
		return "";
	}
	
	/**
	 * Getter method for diagnose
	 * @return Returns diagnose string
	 */
	public String getDiagnose() {
		return diagnose;
	}

	/**
	 * Setter method for diagnose
	 * @param diagnose Diagnose string
	 */
	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}
	
	/**
	 * Getter method for therapy
	 * @return Returns therapy string
	 */
	public String getTherapy() {
		return therapy;
	}
	
	/**
	 * Setter method for therapy
	 * @param therapy Therapy string
	 */
	public void setTherapy(String therapy) {
		this.therapy = therapy;
	}
	
	/**
	 * Getter method for medicine
	 * @return Returns medicine string
	 */
	public String getMed() {
		return med;
	}
	
	/**
	 * Setter method for medicine
	 * @param med Medicine string
	 */
	public void setMed(String med) {
		this.med = med;
	}
	
	/**
	 * Getter method for station
	 * @return Returns station string
	 */
	public String getStation() {
		return station;
	}
	
	/**
	 * Setter method for station
	 * @param station Station string
	 */
	public void setStation(String station) {
		this.station = station;
	}
	
	/**
	 * Getter method for show variable
	 * @return Returns show boolean
	 */
	public boolean isShow() {
		return show;
	}

	/**
	 * Getter method for connection
	 * @return Returns the connection
	 */
	public boolean getConnected() {
		return connected;
	}

	/**
	 * Setter method for connection
	 * @param b
	 */
	public void setConnected(boolean b) {
		connected = b;
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Method to establish a connection to the database
	 *
	 * @param ae ActionEvent
	 */
	public void connect() {

		System.out.println( "connect()..." );

		if (util != null)
			con = util.getCon();
		if (con != null) {
			try {
				stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stm.executeQuery(SQL_select);
				if (rs.first())
					
					connected = true;

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
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Verbindung zur Datenbank beenden
	 *
	 * @param ae ActionEvent
	 */
	public void disconnect(ActionEvent ae) {

		if (con != null) {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();

				util.closeConnection(con);

				connected = false;

				/*
				 * reset variables
				 */
				setpID(0);
				setPreName("");
				setFamName("");
				seteMail("");
				setTelNr("");
				setBirthDate(null);
				setHealthIns("");
				setInDate(null);
				setOutDate(null);
				setDiagnose("");
				setStation("");
				setTherapy("");
				setMed("");
				setBlobID(0);

			} catch (Exception ex) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exception", ex.getLocalizedMessage()));
				out.println("Error: " + ex);
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Datensatz suchen für Admin
	 * 
	 * @param ae Action event
	 */
	public void search(ActionEvent ae) {

		String prename = getPreName();
		String famName = getFamName();
		String eMail = geteMail();
		String telNr = getTelNr();
		String healthIns = getHealthIns();
		String diagnose = getDiagnose();
		String station = getStation();
		String therapy = getTherapy();
		String med = getMed();
		Date birthDate = (Date) getBirthDate();
		Date inDate = (Date) getInDate();
		Date outDate = (Date) getOutDate();
		MbArray.fillArray(MbArray.build_SQLstring(prename, famName, eMail, telNr, healthIns, diagnose, station, therapy,
				med, birthDate, inDate, outDate));
	}

	/**
	 * Datensatz suchen für User
	 * 
	 * @param ae Action event
	 */
	public void searchU(ActionEvent ae) {

		String prename = getPreName();
		String famName = getFamName();
		String eMail = geteMail();
		String telNr = getTelNr();
		String healthIns = getHealthIns();
		Date birthDate = (Date) getBirthDate();
		Date inDate = (Date) getInDate();
		Date outDate = (Date) getOutDate();

		// String SQL_select = MbArray.build_SQLstring(prename);
		MbArray.fillArray(MbArray.build_SQLstringU(prename, famName, eMail, telNr, healthIns,
				birthDate, inDate, outDate));

		// System.out.println("Suche nach " + prename + " starten ...");

	}

	/**
	 * Datensatz einfuegen für Admin
	 *
	 * @param ae ActionEvent
	 */
	public void insert(ActionEvent ae) {

		try {
			// if( ps == null ){

			String sQl_stammdaten = "INSERT INTO patient_stamm( "
					+ "vorName, nachName, geburtsdatum, email, telefon, krankenkasse ) "
					+ "VALUES ( ?, ?, ?, ?, ?, ? )";

			String sQL_behandlung = "INSERT INTO patient_behandlung( "
					+ "einlieferungsdatum, entlassungsdatum, diagnose, patientID, stationID) "
					+ "VALUES (?,?,?,?,14)";

			String sQL_patientTherapie = "INSERT INTO patient_therapie( "
					+ "behandlungID, therapieID) "
					+ "VALUES (?,8)";

			int patient_id = 0;
			int behandl_id = 0;

			// Create dataset Patient Stamm
			PreparedStatement ps = con.prepareStatement(sQl_stammdaten, Statement.RETURN_GENERATED_KEYS);

			// Das wird in die "?" bei VALUES eingesetzt
			ps.setString(1, preName);
			ps.setString(2, famName);
			ps.setDate(3, birthDate);
			ps.setString(4, eMail);
			ps.setString(5, telNr);
			ps.setString(6, healthIns);

			int n = ps.executeUpdate();

			// get primary key
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				patient_id = rs.getInt(1);
			}

			// Create dataset Patient Behandlung
			ps = con.prepareStatement(sQL_behandlung, Statement.RETURN_GENERATED_KEYS);

			ps.setDate(1, inDate); 
			ps.setDate(2, outDate); 
			ps.setString(3, diagnose);
			ps.setInt(4, patient_id);

			int n1 = ps.executeUpdate();

			// get primary key
			rs = ps.getGeneratedKeys();
			while(rs.next()) {
				behandl_id = rs.getInt(1);
			}

			// Create dataset Patient Therapie
			ps = con.prepareStatement(sQL_patientTherapie);
			ps.setInt(1, behandl_id);

			int n2 = ps.executeUpdate();

			ps.close();

			if (n == 1 && n1 == 1 && n2 == 1) {
				out.println("O.K., Datensatz eingefuegt.");
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "O. K.", "Ein Datensatz erfolgreich eingefuegt."));
			}

			// Result set neu aufbauen:
			rs = stm.executeQuery(SQL_select);
		} catch (SQLException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
			out.println("Error: " + ex);
			ex.printStackTrace();
		}
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Datensatz löschen
	 *
	 * @param ae ActionEvent
	 * @throws SQLException 
	 */
	public void delete(ActionEvent ae) throws SQLException {

		if (util != null)
			util.log("delete()...");

		String sql_deletePatientTherapie = "DELETE FROM patient_therapie WHERE behandlungID = ?";
		String sql_deletePatientBehandlung = "DELETE FROM patient_behandlung WHERE patientID = ?";
		String sql_deletePatientStamm = "DELETE FROM patient_stamm WHERE patientID = ?";
		String sql_getBehandlungID = "SELECT behandlungID FROM patient_behandlung WHERE patientID = ?";
		ResultSet rs;
		int behandlung_id = 0;

		try {
			PreparedStatement ps = con.prepareStatement(sql_getBehandlungID);
			ps.setInt(1, pID);
			rs = ps.executeQuery();
			if (rs.next()) {
				behandlung_id = rs.getInt("behandlungID");
			}

			ps = con.prepareStatement(sql_deletePatientTherapie);			
			ps.setInt(1, behandlung_id);
			int n1 = ps.executeUpdate();

			ps = con.prepareStatement(sql_deletePatientBehandlung);			
			ps.setInt(1, pID);
			int n2 = ps.executeUpdate();

			ps = con.prepareStatement(sql_deletePatientStamm);			
			ps.setInt(1, pID);
			int n3 = ps.executeUpdate();

			ps.close();

			if (n1 == 1 && n2 == 1 && n3 == 1) {
				out.println("O.K., Datensatz gel�scht.");
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Datensatz wurde erfolgreich gel�scht",
								""));
			} else {
				out.println("Keine Aenderung der Patientendaten!!");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Daten wurden nicht gel�scht", ""));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Datensatz aktualisieren für Admin
	 *
	 * @param ae ActionEvent
	 */
	@SuppressWarnings("resource")
	public void update(ActionEvent ae) {

		// out.println( "update()..." );
		if (util != null)
			util.log("update()...");

		try {

			String sql_update_patient = "UPDATE patient_stamm SET " + "vorName = ?, nachName = ?, geburtsdatum = ?,"
					+ " email = ?, telefon = ?, krankenkasse = ? " + "WHERE patientID = ?";
			String sql_update_behandlung = "UPDATE patient_behandlung SET " + "einlieferungsdatum = ?, "
					+ "entlassungsdatum = ?, diagnose = ? " + "WHERE patientID = ?";

			String sql_checkStation = "SELECT * FROM station WHERE stationsName LIKE ?";
			String sql_addStation = "INSERT INTO station (stationsName) VALUES (?)";
			String sql_updateStation_behandlung = "UPDATE patient_behandlung SET stationID = ? WHERE patientID = ?";
			String sql_getStationID = "SELECT stationID FROM station WHERE stationsName = ?";

			String sql_checkTherapie = "SELECT * FROM therapie WHERE therapieName LIKE ?";
			String sql_addTherapie = "INSERT INTO therapie (therapieName) VALUES (?)";
			String sql_updatePatient_therapie = "UPDATE patient_therapie SET therapieID = ? WHERE behandlungID = ?";
			String sql_getTherapieID = "SELECT therapieID FROM therapie WHERE therapieName = ?";

			String sql_updateMedikament = "UPDATE patient_therapie SET medikament = ? WHERE behandlungID= ?";
			String sql_getBehandlungID = "SELECT behandlungID FROM patient_behandlung WHERE patientID = ?";

			ResultSet rs;
			String value = "";
			int id = 0;
			int id1 = 0;

			// update Patient
			PreparedStatement ps = con.prepareStatement(sql_update_patient);
			ps.setString(1, preName);
			ps.setString(2, famName);
			ps.setDate(3, birthDate);
			ps.setString(4, eMail);
			ps.setString(5, telNr);
			ps.setString(6, healthIns);
			ps.setInt(7, pID);
			int upStamm = ps.executeUpdate();

			// update Behandlung
			ps = con.prepareStatement(sql_update_behandlung);
			ps.setDate(1, inDate);
			ps.setDate(2, outDate);
			ps.setString(3, diagnose);
			ps.setInt(4, pID);
			int upBeh = ps.executeUpdate();

			if (!station.isEmpty()) {
				// update StationsName
				// check if Station already exists
				ps = con.prepareStatement(sql_checkStation);
				ps.setString(1, station);
				rs = ps.executeQuery();
				if (rs.next()) {
					value = rs.getString("stationsName");
				}

				// if Station doesn't exist create new Dataset and update Behandlung
				if (value.isEmpty()) {
					ps = con.prepareStatement(sql_addStation);
					ps.setString(1, station);
					int safeStat = ps.executeUpdate();
					ps = con.prepareStatement(sql_getStationID);
					ps.setString(1, station);
					rs = ps.executeQuery();
					if (rs.next()) {
						id = rs.getInt("stationID");
					}
					ps = con.prepareStatement(sql_updateStation_behandlung);
					ps.setInt(1, id);
					ps.setInt(2, pID);
					int setBeh = ps.executeUpdate();

					if (safeStat == 1 && setBeh == 1) {
						out.println("O.K., Datensatz geaendert.");
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_INFO,
										"Neue Station wurde erfolgreich der Datenbank hinzugef�gt",
										"Behandlung wurde aktualiiert."));
					} else {
						out.println("Keine Aenderung der Stationsdaten!!");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Station wurde nicht hinzugef�gt!", "Behandlung wurde nicht gespeichert."));
					}
				}
				// if Station already exists update Behandlung
				else {
					ps = con.prepareStatement(sql_getStationID);
					ps.setString(1, station);
					rs = ps.executeQuery();
					if (rs.next()) {
						id = rs.getInt("stationID");
					}
					ps = con.prepareStatement(sql_updateStation_behandlung);
					ps.setInt(1, id);
					ps.setInt(2, pID);
					int setBeh = ps.executeUpdate();

					if (setBeh == 1) {
						out.println("O.K., Datensatz geaendert.");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"O. K.", "Die Station wurde erfolgreich gespeichert."));
					} else {
						out.println("Keine Aenderung der Stationsdaten!!");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Station wurde nicht gespeichert!", "�nderung des Datensatzes nicht m�glich."));
					}
				}
			}

			// update Therapie
			// check if Therapie already exists
			if (!therapy.isEmpty()) {
				value = "";
				ps = con.prepareStatement(sql_checkTherapie);
				ps.setString(1, therapy);
				rs = ps.executeQuery();
				if (rs.next()) {
					value = rs.getString("therapieName");
				}

				// if Therapie doesn't exist create new Dataset and update patient_therapie
				if (value.isEmpty()) {
					ps = con.prepareStatement(sql_addTherapie);
					ps.setString(1, therapy);
					int safeTher = ps.executeUpdate();
					ps = con.prepareStatement(sql_getTherapieID);
					ps.setString(1, therapy);
					rs = ps.executeQuery();
					if (rs.next()) {
						id = rs.getInt("therapieID");
					}
					ps = con.prepareStatement(sql_getBehandlungID);
					ps.setInt(1, pID);
					rs = ps.executeQuery();
					if (rs.next()) {
						id1 = rs.getInt("behandlungID");
					}
					ps = con.prepareStatement(sql_updatePatient_therapie);
					ps.setInt(1, id);
					ps.setInt(2, id1);
					int setBeha = ps.executeUpdate();

					if (safeTher == 1 && setBeha == 1) {
						out.println("O.K., Therapie-Datensatz geaendert.");
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_INFO,
										"Neue Therapie wurde erfolgreich der Datenbank hinzugef�gt",
										"Patient Therapie wurde aktualiiert."));
					} else {
						out.println("Keine Aenderung der Stationsdaten!!");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Therapie wurde nicht hinzugef�gt!", "Patient Therapie wurde nicht gespeichert."));
					} 
				}
				// if Therapie already exists update Behandlung
				else {
					ps = con.prepareStatement(sql_getTherapieID);
					ps.setString(1, therapy);
					rs = ps.executeQuery();
					if (rs.next()) {
						id = rs.getInt("therapieID");
					}
					ps = con.prepareStatement(sql_getBehandlungID);
					ps.setInt(1, pID);
					rs = ps.executeQuery();
					if (rs.next()) {
						id1 = rs.getInt("behandlungID");
					}
					ps = con.prepareStatement(sql_updatePatient_therapie);
					ps.setInt(1, id);
					ps.setInt(2, id1);
					int setBeha = ps.executeUpdate();

					if (setBeha == 1) {
						out.println("O.K., Therapie-Datensatz geaendert.");
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_INFO,
										"Neue Therapie wurde erfolgreich der Datenbank hinzugef�gt",
										"Patient Therapie wurde aktualiiert."));
					} else {
						out.println("Keine Aenderung der Stationsdaten!!");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Therapie wurde nicht hinzugef�gt!", "Patient Therapie wurde nicht gespeichert."));
					}
				}
			}

			if (!med.isEmpty()) {
				// update Medikament
				ps = con.prepareStatement(sql_getBehandlungID);
				ps.setInt(1, pID);
				rs = ps.executeQuery();
				if (rs.next()) {
					id = rs.getInt("behandlungID");
				}
				ps = con.prepareStatement(sql_updateMedikament);
				ps.setString(1, med);
				ps.setInt(2, id);
				int setMed = ps.executeUpdate();

				if (setMed == 1) {
					out.println("O.K., Datensatz geaendert.");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"O. K.", "Das Medikament wurde erfolgreich gespeichert."));
				} else {
					out.println("Keine Aenderung der Medikament-Daten!!");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Medikament wurde nicht gespeichert!", "�nderung des Datensatzes nicht m�glich."));
				}
			}

			if (upStamm == 1 && upBeh == 1) {
				out.println("O.K., Datensatz geaendert.");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "O. K.",
						"Datensatz wurde erfolgreich geaendert."));
			} else if (upStamm == 0) {
				out.println("Keine Aenderung der Stammdaten!!");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Stammdaten nicht geaendert!", "PK-Aenderung nicht erlaubt."));
			} else if (upBeh == 0) {
				out.println("Keine Aenderung der Behandlungsdaten!!");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Behandlungsdaten nicht geaendert!", "PK-Aenderung nicht erlaubt."));
			}

			ps.close();

			// Result set neu aufbauen:
			rs = stm.executeQuery(SQL_select);
		} catch (SQLException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
			out.println("Error: " + ex);
			ex.printStackTrace();
		}
	}

	/**
	 * Datensatz aktualisieren für User
	 *
	 * @param ae ActionEvent
	 */
	@SuppressWarnings("resource")
	public void updateU(ActionEvent ae) {

		// out.println( "update()..." );
		if (util != null)
			util.log("update()...");

		try {

			String sql_update_patient = "UPDATE patient_stamm SET " + "vorName = ?, nachName = ?, geburtsdatum = ?,"
					+ " email = ?, telefon = ?, krankenkasse = ? " + "WHERE patientID = ?";
			String sql_update_behandlung = "UPDATE patient_behandlung SET " + "einlieferungsdatum = ?, "
					+ "entlassungsdatum = ? " + "WHERE patientID = ?";

			ResultSet rs;
			String value = "";
			int id = 0;
			int id1 = 0;

			// update Patient
			PreparedStatement ps = con.prepareStatement(sql_update_patient);
			ps.setString(1, preName);
			ps.setString(2, famName);
			ps.setDate(3, birthDate);
			ps.setString(4, eMail);
			ps.setString(5, telNr);
			ps.setString(6, healthIns);
			ps.setInt(7, pID);
			int upStamm = ps.executeUpdate();

			// update Behandlung
			ps = con.prepareStatement(sql_update_behandlung);
			ps.setDate(1, inDate);
			ps.setDate(2, outDate);
			ps.setInt(3, pID);
			int upBeh = ps.executeUpdate();

			if (upStamm == 1 && upBeh == 1) {
				out.println("O.K., Datensatz geaendert.");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "O. K.",
						"Datensatz wurde erfolgreich geaendert."));
			} else if (upStamm == 0) {
				out.println("Keine Aenderung der Stammdaten!!");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Stammdaten nicht geaendert!", "PK-Aenderung nicht erlaubt."));
			} else if (upBeh == 0) {
				out.println("Keine Aenderung der Behandlungsdaten!!");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Behandlungsdaten nicht geaendert!", "PK-Aenderung nicht erlaubt."));
			}
			ps.close();

			// Result set neu aufbauen:
			rs = stm.executeQuery(SQL_select);
		} catch (SQLException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
			out.println("Error: " + ex);
			ex.printStackTrace();
		}
	}

	/**
	 * Method to reset the values of the input boxes for Admin
	 * 
	 * @param ae The action event
	 */
	public void reset(ActionEvent ae) {
		setPreName(null);
		setFamName(null);
		setBirthDate(null);
		seteMail(null);
		setTelNr(null);
		setHealthIns(null);
		setInDate(null);
		setOutDate(null);
		setDiagnose(null);
		setStation(null);
		setTherapy(null);
		setMed(null);
		MbArray.clear();
	}

	/**
	 * Method to reset the values of the input boxes for User
	 * 
	 * @param ae The action event
	 */
	public void resetU(ActionEvent ae) {
		setPreName(null);
		setFamName(null);
		setBirthDate(null);
		seteMail(null);
		setTelNr(null);
		setHealthIns(null);
		setInDate(null);
		setOutDate(null);
		MbArray.clear();
	}

	/**
	 * Method to transfer the data from the data table to the input boxes for Admin
	 * 
	 * @param ae The action event
	 */
	public void transfer(ActionEvent ae) {

		Patient patient = (Patient) datatablePatients.getRowData();

		setpID(patient.getpID());
		setPreName(patient.getPreName());
		setFamName(patient.getFamName());
		setBirthDate(patient.getBirthDate());
		seteMail(patient.geteMail());
		setTelNr(patient.getTelNr());
		setHealthIns(patient.getHealthIns());
		setInDate(patient.getinDate());
		setOutDate(patient.getoutDate());
		setDiagnose(patient.getDiagnose());
		setStation(patient.getStation());
		setTherapy(patient.getTherapy());
		setMed(patient.getMed());
	}

	/**
	 * Method to transfer the data from the data table to the input boxes for User
	 * 
	 * @param ae The action event
	 */
	public void transferU(ActionEvent ae) {

		Patient patient = (Patient) datatablePatients.getRowData();

		setpID(patient.getpID());
		setPreName(patient.getPreName());
		setFamName(patient.getFamName());
		setBirthDate(patient.getBirthDate());
		seteMail(patient.geteMail());
		setTelNr(patient.getTelNr());
		setHealthIns(patient.getHealthIns());
		setInDate(patient.getinDate());
		setOutDate(patient.getoutDate());
		setDiagnose(patient.getDiagnose());
		setStation(patient.getStation());
		setTherapy(patient.getTherapy());
		setMed(patient.getMed());
	}

	/**
	 * Getter for patient ID from the data table
	 * 
	 * @return Integer
	 */
	public static int getPatientID() {
		Patient patient = (Patient) datatablePatients.getRowData();
		return patient.getpID();
	}

	//--------------------- Parameter übertragen -------------------------	
	/**
	 * Action listener event für show button
	 * @param event
	 */
	public void attrListener(ActionEvent event){

		System.out.println("MbBrowse ... Action event listener opened.");

		// trying to get the parameter from the browse_admin.xhtml when button is clicked
		//blobID = Integer.parseInt((String)event.getComponent().getAttributes().get("blobID"));
		blobID = (int) event.getComponent().getAttributes().get("blobID");

		System.out.println(blobID);

		// blobID soll in session map über context gespeichert werden
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("blobID",blobID);
	}
}
