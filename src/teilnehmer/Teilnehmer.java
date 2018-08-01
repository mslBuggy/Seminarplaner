package teilnehmer;

import java.util.Date;

/**
 * @author Admin
 *
 */
public class Teilnehmer 
{
	//Definiert die Variablen der Klasse und deren Getter und Setter
	private int teilnehmerNr;
	private String vorname;
	private String nachname;
	private String strasse;
	private String hausNr;
	private String plz;
	private String ort;
	private String geschlecht;
	private String telFestnetz;
	private String telMobil;
	private String eMail;
	private Date gebDatum;
	private String aktiv;
	private Date austrittsDatum;
	private String bemerkung;
	private Date aendDatum;
	private String aendBenutzer;
	public int getTeilnehmerNr() {
		return teilnehmerNr;
	}
	public void setTeilnehmerNr(int teilnehmerNr) {
		this.teilnehmerNr = teilnehmerNr;
	}
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getStrasse() {
		return strasse;
	}
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}
	public String getHausNr() {
		return hausNr;
	}
	public void setHausNr(String hausNr) {
		this.hausNr = hausNr;
	}
	public String getPlz() {
		return plz;
	}
	public void setPlz(String plz) {
		this.plz = plz;
	}
	public String getOrt() {
		return ort;
	}
	public void setOrt(String ort) {
		this.ort = ort;
	}
	public String getGeschlecht() {
		return geschlecht;
	}
	public void setGeschlecht(String geschlecht) {
		this.geschlecht = geschlecht;
	}
	public String getTelFestnetz() {
		return telFestnetz;
	}
	public void setTelFestnetz(String telFestnetz) {
		this.telFestnetz = telFestnetz;
	}
	public String getTelMobil() {
		return telMobil;
	}
	public void setTelMobil(String telMobil) {
		this.telMobil = telMobil;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public Date getGebDatum() {
		return gebDatum;
	}
	public void setGebDatum(Date gebDatum) {
		this.gebDatum = gebDatum;
	}
	public String getAktiv() {
		return aktiv;
	}
	public void setAktiv(String aktiv) {
		this.aktiv = aktiv;
	}
	public Date getAustrittsDatum() {
		return austrittsDatum;
	}
	public void setAustrittsDatum(Date austrittsDatum) {
		this.austrittsDatum = austrittsDatum;
	}
	public String getBemerkung() {
		return bemerkung;
	}
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	public Date getAendDatum() {
		return aendDatum;
	}
	public void setAendDatum(Date aendDatum) {
		this.aendDatum = aendDatum;
	}
	public String getAendBenutzer() {
		return aendBenutzer;
	}
	public void setAendBenutzer(String aendBenutzer) {
		this.aendBenutzer = aendBenutzer;
	}
	public Teilnehmer()
	{
		
	}
	public Teilnehmer(int TeilnehmerNr, String Vorname, String Nachname, String Strasse, String HausNr, String PLZ, String Ort, String Geschlecht, String TelFestnetz, String TelMobil, String EMail, Date GebDatum, String Aktiv, Date AustrittsDatum, String Bemerkung, Date AendDatum, String AendBenutzer)
	{
		//Wei�t den Eigenschaften die Werte zu
		this.teilnehmerNr = TeilnehmerNr;
		this.vorname=Vorname;
		this.nachname=Nachname;
		this.strasse=Strasse;
		this.hausNr=HausNr;
		this.plz=PLZ;
		this.ort=Ort;
		this.geschlecht=Geschlecht;
		this.telFestnetz=TelFestnetz;
		this.telMobil=TelMobil;
		this.eMail=EMail;
		this.gebDatum=GebDatum;
		this.aktiv=Aktiv;
		this.austrittsDatum=AustrittsDatum;
		this.bemerkung=Bemerkung;
		this.aendDatum=AendDatum;
		this.aendBenutzer=AendBenutzer;
	}
	
}

