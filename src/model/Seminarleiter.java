package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Seminarleiter database table.
 * 
 */
@Entity
@NamedQuery(name="Seminarleiter.findAll", query="SELECT s FROM Seminarleiter s")
public class Seminarleiter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SeminarleiterNr")
	private int seminarleiterNr;

	@Column(name="AendBenutzer")
	private String aendBenutzer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AendDatum")
	private Date aendDatum;

	@Column(name="Bemerkung")
	private String bemerkung;

	private String EMail;

	@Column(name="Geschlecht")
	private char geschlecht;

	@Column(name="Nachname")
	private String nachname;

	@Column(name="Passwort")
	private String passwort;

	@Column(name="TelefonNr")
	private String telefonNr;

	@Column(name="Vorname")
	private String vorname;

	//bi-directional many-to-one association to Ausbildung
	@OneToMany(mappedBy="seminarleiter")
	private List<Ausbildung> ausbildungs;

	public Seminarleiter() {
	}

	public int getSeminarleiterNr() {
		return this.seminarleiterNr;
	}

	public void setSeminarleiterNr(int seminarleiterNr) {
		this.seminarleiterNr = seminarleiterNr;
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

	public char getGeschlecht() {
		return this.geschlecht;
	}

	public void setGeschlecht(char geschlecht) {
		this.geschlecht = geschlecht;
	}

	public String getNachname() {
		return this.nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getPasswort() {
		return this.passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public String getTelefonNr() {
		return this.telefonNr;
	}

	public void setTelefonNr(String telefonNr) {
		this.telefonNr = telefonNr;
	}

	public String getVorname() {
		return this.vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public List<Ausbildung> getAusbildungs() {
		return this.ausbildungs;
	}

	public void setAusbildungs(List<Ausbildung> ausbildungs) {
		this.ausbildungs = ausbildungs;
	}

	public Ausbildung addAusbildung(Ausbildung ausbildung) {
		getAusbildungs().add(ausbildung);
		ausbildung.setSeminarleiter(this);

		return ausbildung;
	}

	public Ausbildung removeAusbildung(Ausbildung ausbildung) {
		getAusbildungs().remove(ausbildung);
		ausbildung.setSeminarleiter(null);

		return ausbildung;
	}

}