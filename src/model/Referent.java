package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Referent database table.
 * 
 */
@Entity
@NamedQuery(name="Referent.findAll", query="SELECT r FROM Referent r")
public class Referent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ReferentenNr")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int referentenNr;

	@Column(name="AendBenutzer")
	private String aendBenutzer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AendDatum")
	private Date aendDatum;

	@Column(name="Aktiv")
	private boolean aktiv;

	@Column(name="Bemerkung")
	private String bemerkung;

	@Column(name="EMail")
	private String EMail;

	@Column(name="Nachname")
	private String nachname;

	@Column(name="TelFestnetz")
	private String telFestnetz;

	@Column(name="TelMobil")
	private String telMobil;

	@Column(name="Vorname")
	private String vorname;

	//bi-directional many-to-one association to Referent_Kurs
	@OneToMany(mappedBy="referent")
	private List<Referent_Kurs> referentKurs;

	//bi-directional many-to-one association to Zeitplanung
	@OneToMany(mappedBy="referent")
	private List<Zeitplanung> zeitplanungs1;

	//bi-directional many-to-one association to Zeitplanung
	@OneToMany(mappedBy="referent")
	private List<Zeitplanung> zeitplanungs2;

	public Referent() {
	}

	public int getReferentenNr() {
		return this.referentenNr;
	}

	public void setReferentenNr(int referentenNr) {
		this.referentenNr = referentenNr;
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

	public String getNachname() {
		return this.nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
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

	public List<Referent_Kurs> getReferentKurs() {
		return this.referentKurs;
	}

	public void setReferentKurs(List<Referent_Kurs> referentKurs) {
		this.referentKurs = referentKurs;
	}

	public Referent_Kurs addReferentKur(Referent_Kurs referentKur) {
		getReferentKurs().add(referentKur);
		referentKur.setReferent(this);

		return referentKur;
	}

	public Referent_Kurs removeReferentKur(Referent_Kurs referentKur) {
		getReferentKurs().remove(referentKur);
		referentKur.setReferent(null);

		return referentKur;
	}

	public List<Zeitplanung> getZeitplanungs1() {
		return this.zeitplanungs1;
	}

	public void setZeitplanungs1(List<Zeitplanung> zeitplanungs1) {
		this.zeitplanungs1 = zeitplanungs1;
	}

	public Zeitplanung addZeitplanungs1(Zeitplanung zeitplanungs1) {
		getZeitplanungs1().add(zeitplanungs1);
		zeitplanungs1.setReferent(this);

		return zeitplanungs1;
	}

	public Zeitplanung removeZeitplanungs1(Zeitplanung zeitplanungs1) {
		getZeitplanungs1().remove(zeitplanungs1);
		zeitplanungs1.setReferent(null);

		return zeitplanungs1;
	}

	public List<Zeitplanung> getZeitplanungs2() {
		return this.zeitplanungs2;
	}

	public void setZeitplanungs2(List<Zeitplanung> zeitplanungs2) {
		this.zeitplanungs2 = zeitplanungs2;
	}

	public Zeitplanung addZeitplanungs2(Zeitplanung zeitplanungs2) {
		getZeitplanungs2().add(zeitplanungs2);
		zeitplanungs2.setReferent(this);

		return zeitplanungs2;
	}

	public Zeitplanung removeZeitplanungs2(Zeitplanung zeitplanungs2) {
		getZeitplanungs2().remove(zeitplanungs2);
		zeitplanungs2.setReferent(null);

		return zeitplanungs2;
	}

}