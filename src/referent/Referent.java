package referent;

import java.util.Date;

public class Referent {

	private int referentenNr;
	private String vorname,nachname,telfestNetz,telMobil,eMail,aendBenutzer, bemerkung;
	
	private boolean aktiv;
	private Date aendDatum;
	
	public String getBemerkung() {
		return bemerkung;
	}
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	public int getReferentenNr() {
		return referentenNr;
	}
	public void setReferentenNr(int referentenNr) {
		this.referentenNr = referentenNr;
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
	public String getTelfestNetz() {
		return telfestNetz;
	}
	public void setTelfestNetz(String telfestNetz) {
		this.telfestNetz = telfestNetz;
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
	public String getAendBenutzer() {
		return aendBenutzer;
	}
	public void setAendBenutzer(String aendBenutzer) {
		this.aendBenutzer = aendBenutzer;
	}
	public boolean isAktiv() {
		return aktiv;
	}
	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
	}
	public Date getAendDatum() {
		return aendDatum;
	}
	public void setAendDatum(Date aendDatum) {
		this.aendDatum = aendDatum;
	}
	public Referent(int referentnr, String vorname,String nachname,String string2,String string3,
			String eMail,String aendBenutzer, String string,String bemerkung, boolean aktiv){
		this.vorname = vorname;
		this.nachname = nachname;
		this.referentenNr = referentnr;
		this.aendBenutzer = aendBenutzer;
		this.eMail = eMail;
		this.telfestNetz = string2;
		this.telMobil = string3;
		this.aktiv = aktiv;
		this.bemerkung = bemerkung;
	}
	
	
}
