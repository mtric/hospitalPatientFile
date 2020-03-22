package patientFile;

/*
 * RunUpload.java
 * JSF 2.3 file upload mit progress bar
 */


import java.io.IOException;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.http.Part;

/**
 * File upload. Aufbau der Tabelle:
 * CREATE TABLE blobdemo (
 *   id   INT NOT NULL AUTO_INCREMENT,
 *   name VARCHAR( 255 ),
 *   typ  VARCHAR( 128 ),
 *   pic  MEDIUMBLOB,
 *   PRIMARY KEY( id )
 * );          
 *
 * @author Wolfgang Lang
 * @version 1.1.4, 2017-08-23
 */
public class RunUpload implements Runnable {
  
  private final String CLASSNAME = getClass().getName();
  
  private MbBlobDemo mbBlobDemo = null;
  //private MbBrowse mbBrowse = null;
  private int rc = MbBlobDemo.RC_SUCCESS;
  private InputStream is = null;
  private String fileName = "filename";
  private String contentType = "contentType";
  private Exception ex = null;
	private long fileLen = 0;	
  private Connection con = null;
  
  /**                             
   * @param util
   * @param is
   * @param sBlobFileName
   * @param contentType
   */
  public RunUpload( MbBlobDemo mbBlobDemo, /*MbBrowse mbBrowse , */Connection con, InputStream is, Part part, String sBlobFileName ) {
    super();
    
    final String MNAME = CLASSNAME + ".RunUpload()";  
    System.out.println( MNAME ); 
    
    this.mbBlobDemo  = mbBlobDemo;
    //this.mbBrowse	 = mbBrowse;
    this.is          = is;
    this.fileName    = sBlobFileName;    
    this.contentType = part.getContentType(); 
    this.fileLen     = part.getSize();
    this.con         = con;
  }
  
  /*--------------------------------------------------------------------------*/
  @Override
  public void run(){

    final String MNAME = "run()"; 
    final String TAG = CLASSNAME + "." + MNAME;
    System.out.println( TAG + ": entering..." );
    
    System.out.println( "fileName: "    + fileName    );
    System.out.println( "contentType: " + contentType );
    System.out.println( "fileLen: "     + fileLen     );
    
    try{    	
    	if( con != null ) {
    		long ts = System.currentTimeMillis();
    		PreparedStatement ps = con.prepareStatement( "insert into patient_blob( name, typ, pic ) values ( ?, ?, ? )" );
	    	
	      ps.setString      ( 1, fileName                );
	      ps.setString      ( 2, contentType             );
	      ps.setBinaryStream( 3, is, (int) fileLen );      
	      /* Das Löschen eines Blobs kann so erfolgen:
	       * ps.setNull( 3, Types.BINARY );  */
	      
	      //Thread.sleep( 5_000 );
	      
	      int n =  ps.executeUpdate();
	      if( n == 1) System.out.println( "O.K.,  " + fileName + " inserted." ); 
	            	      	     
	      System.out.println( "Upload time: " + 
	                         ((System.currentTimeMillis() - ts) / 1000) + " sec." );
	              
	      if( ps  != null ) ps.close();
	      if( con != null ) con.close();
	      if( is  != null ) is.close();
    	}
    	else System.err.println( "Connection ist null!!" );
    }
    catch( IOException ex ){
      rc = MbBlobDemo.RC_FAIL;
      this.ex = ex;
      ex.printStackTrace();
    }
    catch( Exception ex ){
      rc = MbBlobDemo.RC_FAIL;
      this.ex = ex;
      ex.printStackTrace();
    }
    
    mbBlobDemo.setInProgress( false );
    // mbBrowse.setInProgress(false);
  }
  
  public Exception getEx() { return ex; }
  public int       getRc() { return rc; }
}