package patientFile;

import java.sql.Date;

/**
 * This class represents an entity of a single patient
 * 
 * @author Eric Walter
 * @version 1.1, 2019-06-14
 */
public class Patient {

	private String preName, famName, eMail, telNr, healthIns, diagnose, station, therapy, med;
	private Date birthDate, inDate, outDate = null;
	private int pID, blobID;
	private boolean canEdit;

	/**
	 * The constructor for the patient object
	 * 
	 * @param preName The prename
	 * @param famName The family name
	 * @param eMail The email address
	 * @param birthDate The birthdate
	 * @param telNr The phone number
	 * @param healthIns The health insurance
	 * @param pID The patient ID
	 * @param inDate The arrival date
	 * @param outDate The departure date
	 * @param diagnose The patient diagnosis
	 * @param station The hospital station
	 * @param therapy The patient therapy
	 * @param med The patients medicine
	 * @param blobID The patients blob ID
	 */
	public Patient(String preName, String famName, String eMail, Date birthDate, String telNr, 
			String healthIns, int pID, Date inDate, Date outDate, String diagnose, String station, 
			String therapy, String med, int blobID) {
		this.setPreName(preName);
		this.setFamName(famName);
		this.seteMail(eMail);
		this.setBirthDate(birthDate);
		this.setTelNr(telNr);
		this.setHealthIns(healthIns);
		this.setpID(pID);
		this.setinDate(inDate);
		this.setoutDate(outDate);
		this.setDiagnose(diagnose);
		this.setStation(station);
		this.setTherapy(therapy);
		this.setMed(med);
		this.setBlobID(blobID);
		setCanEdit(false);
	}
	/**
	 * Empty patient constructor
	 */
	public Patient() {
	}

	/**
	 * Getter for patient ID
	 * @return Returns the patient ID
	 */
	public int getpID() {
		return pID;
	}

	/**
	 * Setter for patient ID
	 * @param pID The patient ID
	 */
	public void setpID(int pID) {
		this.pID = pID;
	}
	
	/**
	 * Getter for blob id
	 * @return Returns the blob ID
	 */
	public int getBlobID() {
		return blobID;
	}
	
	/**
	 * Setter for blob ID
	 * @param blobID The blob ID
	 */
	public void setBlobID(int blobID) {
		this.blobID = blobID;
	}

	/**
	 * Getter for patient prename
	 * @return Returns the prename
	 */
	public String getPreName() {
		return preName;
	}

	/**
	 * Setter for prename
	 * @param preName The prename
	 */
	public void setPreName(String preName) {
		this.preName = preName;
	}

	/**
	 * Getter for family name
	 * @return Returns the family name
	 */
	public String getFamName() {
		return famName;
	}

	/**
	 * Setter for family name
	 * @param famName The family name
	 */
	public void setFamName(String famName) {
		this.famName = famName;
	}

	/**
	 * Getter for email
	 * @return Returns the email address
	 */
	public String geteMail() {
		return eMail;
	}

	/**
	 * Setter for email
	 * @param eMail The email address
	 */
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	/**
	 * Getter for birthdate
	 * @return Returns the birthdate
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	
	/**
	 * Setter for birthdate
	 * @param birthDate The birthdate
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * Setter for arrival date
	 * @param inDate The arrival date
	 */
	public void setinDate(Date inDate) {
		this.inDate = inDate;
	}

	/**
	 * Getter for arrival date
	 * @return Returns the arrival date
	 */
	public Date getinDate() {
		return inDate;
	}
	
	/**
	 * Getter for departure date
	 * @return Returns the departure date
	 */
	public Date getoutDate() {
		return outDate;
	}

	/**
	 * Setter for departure date
	 * @param outDate The departure date
	 */
	public void setoutDate(Date outDate) {
		this.outDate = outDate;
	}

	/**
	 * Getter for phone number
	 * @return Returns the phone number
	 */
	public String getTelNr() {
		return telNr;
	}

	/**
	 * Setter for phone number
	 * @param telNr The phone number
	 */
	public void setTelNr(String telNr) {
		this.telNr = telNr;
	}

	/**
	 * Getter for health insurance
	 * @return Returns the health insurance
	 */
	public String getHealthIns() {
		return healthIns;
	}

	/**
	 * Setter for health insurance
	 * @param healthIns The health insurance
	 */
	public void setHealthIns(String healthIns) {
		this.healthIns = healthIns;
	}

	/**
	 * Getter for editable
	 * @return Returns boolean
	 */
	public boolean isCanEdit() {
		return canEdit;
	}

	/**
	 * Setter for editable
	 * @param canEdit The boolean
	 */
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	/**
	 * Getter for diagnose
	 * @return Returns the diagnose
	 */
	public String getDiagnose() {
		return diagnose;
	}

	/**
	 * Setter for diagnose
	 * @param diagnose The diagnose
	 */
	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}
	
	/**
	 * Getter for station
	 * @return Returns the station
	 */
	public String getStation() {
		return station;
	}

	/**
	 * Setter for station
	 * @param station The station
	 */
	public void setStation(String station) {
		this.station = station;
	}
	
	/**
	 * Getter for therapy
	 * @return Returns the therapy
	 */
	public String getTherapy() {
		return therapy;
	}

	/**
	 * Setter for therapy
	 * @param therapy
	 */
	public void setTherapy(String therapy) {
		this.therapy = therapy;
	}
	
	/**
	 * Getter for medicine
	 * @return Returns the medicine
	 */
	public String getMed() {
		return med;
	}

	/**
	 * Setter for medicine
	 * @param med The medicine
	 */
	public void setMed(String med) {
		this.med = med;
	}
	
	/**
	 * This method overrides the toString method to print out all values
	 */
	@Override
	public String toString() {
		return preName + " " + famName + " " + eMail + " " + birthDate + " " + telNr + " " + healthIns + " " + pID + " "
				+ inDate + " " + outDate+ " "+diagnose+" "+station+" "+therapy+" "+med+" "+blobID;
	}
}
