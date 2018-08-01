package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Teilnehmer database table.
 * 
 */
@Entity
@NamedQuery(name="Teilnehmer.findAll", query="SELECT t FROM Teilnehmer t")
public class Teilnehmer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TeilnehmerNr")
	private int teilnehmerNr;

	@Column(name="AendBenutzer")
	private String aendBenutzer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AendDatum")
	private Date aendDatum;

	@Column(name="Aktiv")
	private boolean aktiv;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AustrittsDatum")
	private Date austrittsDatum;

	@Column(name="Bemerkung")
	private String bemerkung;

	private String EMail;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="GebDatum")
	private Date gebDatum;

	@Column(name="Geschlecht")
	private char geschlecht;

	@Column(name="HausNr")
	private String hausNr;

	@Column(name="Nachname")
	private String nachname;

	@Column(name="Ort")
	private String ort;

	@Column(name="PLZ")
	private String plz;

	@Column(name="Strasse")
	private String strasse;

	@Column(name="TelFestnetz")
	private String telFestnetz;

	@Column(name="TelMobil")
	private String telMobil;

	@Column(name="Vorname")
	private String vorname;

	//bi-directional many-to-one association to Teilnehmer_Ausbildung
	@OneToMany(mappedBy="teilnehmer")
	private List<Teilnehmer_Ausbildung> teilnehmerAusbildungs;

	public Teilnehmer() {
	}

	public int getTeilnehmerNr() {
		return this.teilnehmerNr;
	}

	public void setTeilnehmerNr(int teilnehmerNr) {
		this.teilnehmerNr = teilnehmerNr;
	}

	public String getAendBenutzer() {
		return this.aendBenutzer;
	}

	public void setAendBenutzer(String aendBenutzer) {
		this.aendBenutzer = aendBenutzer;
	}

	public Date getAendDatum() {
		return this.aendDatum;
	}

	public void setAendDatum(Date aendDatum) {
		this.aendDatum = aendDatum;
	}

	public boolean getAktiv() {
		return this.aktiv;
	}

	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
	}

	public Date getAustrittsDatum() {
		return this.austrittsDatum;
	}

	public void setAustrittsDatum(Date austrittsDatum) {
		this.austrittsDatum = austrittsDatum;
	}

	public String getBemerkung() {
		return this.bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public String getEMail() {
		return this.EMail;
	}

	public void setEMail(String EMail) {
		this.EMail = EMail;
	}

	public Date getGebDatum() {
		return this.gebDatum;
	}

	public void setGebDatum(Date gebDatum) {
		this.gebDatum = gebDatum;
	}

	public char getGeschlecht() {
		return this.geschlecht;
	}

	public void setGeschlecht(char geschlecht) {
		this.geschlecht = geschlecht;
	}

	public String getHausNr() {
		return this.hausNr;
	}

	public void setHausNr(String hausNr) {
		this.hausNr = hausNr;
	}

	public String getNachname() {
		return this.nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getOrt() {
		return this.ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getPlz() {
		return this.plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getStrasse() {
		return this.strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getTelFestnetz() {
		return this.telFestnetz;
	}

	public void setTelFestnetz(String telFestnetz) {
		this.telFestnetz = telFestnetz;
	}

	public String getTelMobil() {
		return this.telMobil;
	}

	public void setTelMobil(String telMobil) {
		this.telMobil = telMobil;
	}

	public String getVorname() {
		return this.vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public List<Teilnehmer_Ausbildung> getTeilnehmerAusbildungs() {
		return this.teilnehmerAusbildungs;
	}

	public void setTeilnehmerAusbildungs(List<Teilnehmer_Ausbildung> teilnehmerAusbildungs) {
		this.teilnehmerAusbildungs = teilnehmerAusbildungs;
	}

	public Teilnehmer_Ausbildung addTeilnehmerAusbildung(Teilnehmer_Ausbildung teilnehmerAusbildung) {
		getTeilnehmerAusbildungs().add(teilnehmerAusbildung);
		teilnehmerAusbildung.setTeilnehmer(this);

		return teilnehmerAusbildung;
	}

	public Teilnehmer_Ausbildung removeTeilnehmerAusbildung(Teilnehmer_Ausbildung teilnehmerAusbildung) {
		getTeilnehmerAusbildungs().remove(teilnehmerAusbildung);
		teilnehmerAusbildung.setTeilnehmer(null);

		return teilnehmerAusbildung;
	}

}