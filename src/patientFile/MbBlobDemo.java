package patientFile;

/*
 * MyBean.java
 * JSF 2.3 template
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
//import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.apache.catalina.core.ApplicationPart;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import patientFile.Util;

/**
 * Backing bean der JSF-Seite blobdemo.xhtml
 *
 * @author Wolfgang Lang, Michael Kaleve, Eric Walter
 * @version 1.0.5, 2019-07-11
 */
@Named( "mb" )
@SessionScoped
public class MbBlobDemo implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final long MAX_FILE_SIZE = 10_000_000; // 10 MB 

	final String CLASSNAME = getClass().getName();
	final static int RC_SUCCESS =  0;
	final static int RC_FAIL    = -1;
	final String PIC_TEXT_JA   = "And this is the picture:";
	final String PIC_TEXT_NEIN = "";

	private final Util util = new Util();
	private RunUpload runUpload = null;
	private Thread    thrUpload = null;

	private Part    part           = null;
	private long    partLen        = 0;
	private boolean inProgress     = false;
	private boolean pollActive     = false;
	private String  sBlobFileName  = null;
	private int     uploadTime     = 0;
	private String  uploadMessage  = "";
	private StreamedContent streamedContent = null;
	private StreamedContent streamedPic     = null;
	private boolean noPic =false;

	private EinBlob ebCurrent      = null;
	/**
	 * Getter method for current blob
	 * @return Returns the current blob object
	 */
	public EinBlob getEbCurrent() {	return ebCurrent;	}

	private String picText = PIC_TEXT_NEIN;		

	private int blobID;

	//HtmlSelectOneMenu cbxBlobs = new HtmlSelectOneMenu();

	public MbBlobDemo() {
		System.out.println( CLASSNAME + ".<init>..."  );
		System.out.println( (new Date()).toString() ); 
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * This method initializes the combo box
	 */
	void initBlob() {

		System.out.println( CLASSNAME + ".initBlob()..."  );

		Connection con = util.getCon();

		if( con != null ) {

			try {
				PreparedStatement ps = con.prepareStatement( "SELECT id, name, typ FROM patient_blob where id = ?" );
				ps.setInt( 1, blobID );
				ResultSet rs = ps.executeQuery();

				if( rs.next() ) {
					ebCurrent = new EinBlob( rs.getInt( 1 ), rs.getString( 2 ), rs.getString( 3 ) );
					System.out.println(ebCurrent.toString());
				} else {
					noPic=true;
					ebCurrent = null;
					System.err.println( "ResultSet ist leer!!" );
					}

				rs.close(); ps.close(); con.close();
			}
			catch( Exception ex ) { ex.printStackTrace(); }  
		}
		else System.err.println( "Connection ist null!!" );  	
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Method to get the picture from the database blob file
	 */
	void getPic() {

		System.out.println( CLASSNAME + ".getPic()..."  );
		String typ = "?";
		if( ebCurrent != null ) {
			typ = ebCurrent.getTyp();  	
			System.out.println( "ebCurrent: " + ebCurrent );
		}

		if( typ.equals( "image/jpeg" ) || typ.equals( "image/png" ) || typ.equals( "image/gif" )  ) {
			Connection con = util.getCon();  	
			if( con != null ) {

				try {
					PreparedStatement ps = con.prepareStatement( "SELECT pic FROM patient_blob where id = ?" );
					ps.setInt( 1, blobID );
					ResultSet rs = ps.executeQuery();

					if( rs.next() ) {	  				
						InputStream is = rs.getBinaryStream( 1 );	  				
						streamedPic = new DefaultStreamedContent( is, typ );		  			
						is.close(); 		  			
						picText = PIC_TEXT_JA;
					} else System.err.println( "ResultSet ist leer!!" );

					rs.close(); ps.close(); con.close();
				}
				catch( Exception ex ) { ex.printStackTrace(); }  
			}
			else System.err.println( "Connection ist null!!" );
		} else picText = PIC_TEXT_NEIN;
	}

	/**
	 * Getter method for noPic variable
	 * @return Returns noPic boolean
	 */
	public boolean isNoPic() {
		return noPic;
	}
	
	/*--------------------------------------------------------------------------*/

	@PostConstruct
	public void init() { 
		System.out.println( "@PostConstruct." + CLASSNAME ); 
	}
	/**
	 * Setter method for blob ID
	 * @param blobID The blob ID integer
	 */
	private void setBlobID(int blobID) {
		this.blobID = blobID;
	}

	public void preRenderAction()  { 
		System.out.println( CLASSNAME + ".preRenderAction"  ); 

		//-------------------- Parameter abfangen ----------------
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		setBlobID((int)sessionMap.get("blobID"));
		sessionMap.remove("blobID");
		//---------------------------------------------------------
		initBlob();
		getPic();

	}  
	/**
	 * Getter method for picture text
	 * @return Returns the picture text
	 */
	public String getPicText() { return picText;}

	public void postRenderAction() { System.out.println( CLASSNAME + ".postRenderAction" ); } 

	/**
	 * Setter method for part
	 * @param part
	 */
	public void setPart( Part part ) { 
		System.out.println( CLASSNAME + ".setPart. size: " + part.getSize() );

		this.part = part; 
	}
	/**
	 * Getter method for part
	 * @return Returns the part
	 */
	public Part getPart() { return part; }

	/**
	 * Getter method for upload time
	 * @return Returns the upload time
	 */
	public int getUploadTime() { return uploadTime; }

	/**
	 * Getter method for active poll boolean
	 * @return Returns the boolean variable
	 */
	public boolean getPollActive() { return pollActive; }

	/**
	 * Getter method for upload message
	 * @return Returns the upload message
	 */
	public String getUploadMessage() { return uploadMessage; }

	/**
	 * Getter method for streamed content
	 * @return Returns the streamed content
	 */
	public StreamedContent getStreamedContent() { 
		System.out.println( CLASSNAME + ".getStreamedContent()..."  );
		return streamedContent; 
	}

	/**
	 * Getter method for streamed picture
	 * @return Returns the streamed picture
	 */
	public StreamedContent getStreamedPic() { 
		System.out.println( CLASSNAME + ".getStreamedPic()..."  );  	 
		return streamedPic; 
	}

	/**
	 * Getter method for progress
	 * @return Returns progress boolean
	 */
	public boolean getInProgress() { return inProgress; }

	/**
	 * Setter method for progress
	 * @param b Progress boolean
	 */
	public void    setInProgress( boolean b ) { 
		inProgress = b;
		System.out.println( "setInProgress: " + inProgress );
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Validator für input file
	 * 
	 * @param ctx The faces context
	 * @param uic The UI component
	 * @param val The object
	 */
	public void validateFile( FacesContext ctx, UIComponent uic, Object val ) {

		final String MNAME = CLASSNAME + ".validateFile()";  
		util.log( MNAME );

		if( val == null ) util.log( "val is null.");    	
		else if( ((Part) val ).getSize() > MAX_FILE_SIZE ) {
			String msg = "File too big. Max size: " + MAX_FILE_SIZE;
			System.err.println( msg );
			throw new ValidatorException(
					new FacesMessage( FacesMessage.SEVERITY_ERROR, "Error", msg ));      
		}    
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Value change listener des UI-Objekts h:inputFile. 
	 * 
	 * @param vce ValueChangeEvent
	 */
	public void vclUploadFile( ValueChangeEvent vce ) {

		final String MNAME = CLASSNAME + ".vclUploadFile()";  
		util.log( MNAME );

		if( vce == null ) util.log( "vce is null." );    
		else {
			if( vce.getNewValue() == null ) util.log( "vce.getNewValue() is null." ); 
			else if( vce.getNewValue() instanceof ApplicationPart ) {
				sBlobFileName = ((ApplicationPart) vce.getNewValue()).getSubmittedFileName();
				util.log( "instanceof ApplicationPart. New file name for upload: " + sBlobFileName );        
			}
		}              
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Method to download a file
	 * @param ae An action event
	 */
	public void aclFileDownload( ActionEvent ae ) {

		System.out.println( CLASSNAME + ".aclFileDownload..."  );

		Connection con = util.getCon();

		if( con != null ) {

			try {
				PreparedStatement ps = con.prepareStatement( "SELECT pic FROM patient_blob where id = ?" );
				ps.setInt( 1, ebCurrent.getId() );
				ResultSet rs = ps.executeQuery();

				if( rs.next() ) {

					InputStream is = rs.getBinaryStream( 1 );

					/* Alternative für File: is =
  				FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream( "/resources/img/java.png" ); */

					streamedContent = new DefaultStreamedContent( is, ebCurrent.getTyp(), ebCurrent.getName() );

					is.close(); 	  			
				} else System.err.println( "ResultSet ist leer!!" );

				rs.close(); ps.close(); con.close();
			}
			catch( Exception ex ) { ex.printStackTrace(); }  
		}
		else System.err.println( "Connection ist null!!" );
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * Method to delete a file
	 * @param ae An action event
	 */
	public void aclFileDelete( ActionEvent ae ) {

		System.out.println( CLASSNAME + ".aclFileDelete. ebCurrent is " + ebCurrent );

		Connection con = util.getCon();

		if( con != null ) {

			FacesContext ctx = FacesContext.getCurrentInstance();

			try {
				PreparedStatement ps = con.prepareStatement( "DELETE FROM patient_blob where id = ?" );
				ps.setInt( 1, blobID );
				ps.execute();  			
				ps.close(); con.close();
				ctx.addMessage( null, new FacesMessage(
						FacesMessage.SEVERITY_INFO, "O. K.", ebCurrent + " ist Geschichte." ));
				ebCurrent = null;
			}
			catch( Exception ex ) { 
				ex.printStackTrace();

				ctx.addMessage( null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "SQL Error!", ex.getLocalizedMessage() ));
			}  		
		}
		else System.err.println( "Connection ist null!!" );
	}

	/*--------------------------------------------------------------------------*/
	/**
	 * Method to upload a file
	 * @param ae An action event
	 */
	public void aclFileUpload( ActionEvent ae ) {

		final String MNAME = CLASSNAME + ".aclFileUpload()";  
		util.log( MNAME );

		FacesContext ctx = FacesContext.getCurrentInstance();

		if( inProgress ) {
			String msg = "Upload in progress - please wait!";
			System.out.println( msg );
			ctx.addMessage( null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Wait!", msg ));
		}
		else {
			try { 
				InputStream is = null;
				if( part != null ) is = part.getInputStream();
				else System.err.println( "part is null." );

				if( is != null ){

					partLen = part.getSize();
					runUpload = new RunUpload( this, util.getCon(), is, part, sBlobFileName );

					thrUpload = new Thread( runUpload );
					thrUpload.start();
					inProgress = true; pollActive = true; 
					uploadTime = 0;
					uploadMessage = "";
					System.out.println( "inProgress: " + inProgress );          
				}
				else {
					System.out.println( "Input Stream is null." );
					ctx.addMessage( null, new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Error!", "Missing file." ));
				}
			}
			catch( IOException ex ) {
				ctx.addMessage( null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "IOException!", 
						ex.getLocalizedMessage() ));
				ex.printStackTrace();
			}
			catch( Exception ex ){
				ctx.addMessage( null, new FacesMessage( 
						FacesMessage.SEVERITY_ERROR, "Exception!", ex.getLocalizedMessage() ));
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Diese Methode wird von <p:poll> im in Abständen aufgerufen, solange der upload läuft
	 */
	public void polProgress() {

		final String MNAME = CLASSNAME + ".polProgress()";  
		util.log( MNAME + ". " + new Date() );
		System.out.println( MNAME + ". " + new Date() );

		uploadTime++;

		if( ! inProgress & runUpload != null && util != null ) { 
			pollActive = false;
			int rc = runUpload.getRc();
			util.log( "runUpload.rc: " + rc );
			try {
				if( rc == RC_SUCCESS ) {
					uploadMessage = "File " + sBlobFileName + " mit " + partLen + " Bytes erfolgreich hochgeladen.";
				}
				else uploadMessage = "Exception! " + runUpload.getEx().getMessage();  				  	   
			} catch( Exception ex ) {
				System.err.println( ex.getMessage());
			}  		
		} 
	}

	/**
	 * Initialisierung der Combobox Blobs
	 * 
	 * @return cbxBlobs
	 * /
  public HtmlSelectOneMenu getCbxBlobs() {

  	final String MNAME = CLASSNAME + ".getCbxBlobs()";       
    return cbxBlobs;
  }*/
}

