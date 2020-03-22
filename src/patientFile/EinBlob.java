package patientFile;

/*
 * EinBlob.java
 * JSF Blob-Demo
 */

/**
 * Die Objekte der Klasse EinBlob repräsentieren jeweils ein Tupel der Tabelle
 * blobdemo (ohne Spalte "pic").
 * 
 * @author Wolfgang Lang
 * @version 1.0.0, 2017-08-25
 * @see     "Foliensatz zur Vorlesung"
 */
public class EinBlob {
 
  private int id = -1;
  private String name, typ;
  
  /**
   * 
   * @param id The blob id
   * @param name The file name
   * @param typ The picture typ
   */
  public EinBlob( int id, String name, String typ ) {
      this.id    = id;
      this.name  = name;
      this.typ   = typ;
    }
  /**
   * Setter method for blob id  
   * @param n Integer id value
   */
  public void setId( int n ){ id = n; }
  /**
   * Getter mehtod for blob id
   * @return Returns the blob id
   */
  public int getId(){ return id; }
  /**
   * Setter method for the name  
   * @param s Name string
   */
  public void setName( String s ){ name = s; }
  /**
   * Getter method for name
   * @return Returns the name
   */
  public String getName(){ return name; }
  /**
   * Setter method for type  
   * @param s Typ string
   */
  public void setTyp( String s ){ typ = s; }
  /**
   * Getter method for typ
   * @return Returns the typ
   */
  public String getTyp(){ return typ; }
  
  /**
   * This method overrides the toString method to print out the values
   */
  @Override
  public String toString() {
    return id + " " + name + " " + typ;
  }
}
