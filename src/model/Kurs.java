package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Kurs database table.
 * 
 */
@Entity
@NamedQuery(name="Kurs.findAll", query="SELECT k FROM Kurs k")
public class Kurs implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="KursNr")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int kursNr;

	@Column(name="AendBenutzer")
	private String aendBenutzer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AendDatum")
	private Date aendDatum;

	@Column(name="Bemerkung")
	private String bemerkung;

	@Column(name="BrauchtEDV")
	private boolean brauchtEDV;

	@Column(name="KursBezeichnung")
	private String kursBezeichnung;

	@Column(name="KursDauerTage")
	private int kursDauerTage;

	@Column(name="Lehrjahr")
	private int lehrjahr;

	//bi-directional many-to-one association to Ausbildung_Kurs
	@OneToMany(mappedBy="kur",cascade=CascadeType.PERSIST)
	private List<Ausbildung_Kurs> ausbildungKurs;

	//bi-directional many-to-one association to Referent_Kurs
	@OneToMany(mappedBy="kur")
	private List<Referent_Kurs> referentKurs;

	//bi-directional many-to-one association to Zeitplanung
	@OneToMany(mappedBy="kur")
	private List<Zeitplanung> zeitplanungs;

	public Kurs() {
	}

	public int getKursNr() {
		return this.kursNr;
	}

	public void setKursNr(int kursNr) {
		this.kursNr = kursNr;
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

	public boolean getBrauchtEDV() {
		return this.brauchtEDV;
	}

	public void setBrauchtEDV(boolean brauchtEDV) {
		this.brauchtEDV = brauchtEDV;
	}

	public String getKursBezeichnung() {
		return this.kursBezeichnung;
	}

	public void setKursBezeichnung(String kursBezeichnung) {
		this.kursBezeichnung = kursBezeichnung;
	}

	public int getKursDauerTage() {
		return this.kursDauerTage;
	}

	public void setKursDauerTage(int kursDauerTage) {
		this.kursDauerTage = kursDauerTage;
	}

	public int getLehrjahr() {
		return this.lehrjahr;
	}

	public void setLehrjahr(int lehrjahr) {
		this.lehrjahr = lehrjahr;
	}

	public List<Ausbildung_Kurs> getAusbildungKurs() {
		return this.ausbildungKurs;
	}

	public void setAusbildungKurs(List<Ausbildung_Kurs> ausbildungKurs) {
		this.ausbildungKurs = ausbildungKurs;
	}

	public Ausbildung_Kurs addAusbildungKur(Ausbildung_Kurs ausbildungKur) {
		getAusbildungKurs().add(ausbildungKur);
		ausbildungKur.setKur(this);

		return ausbildungKur;
	}

	public Ausbildung_Kurs removeAusbildungKur(Ausbildung_Kurs ausbildungKur) {
		getAusbildungKurs().remove(ausbildungKur);
		ausbildungKur.setKur(null);

		return ausbildungKur;
	}

	public List<Referent_Kurs> getReferentKurs() {
		return this.referentKurs;
	}

	public void setReferentKurs(List<Referent_Kurs> referentKurs) {
		this.referentKurs = referentKurs;
	}

	public Referent_Kurs addReferentKur(Referent_Kurs referentKur) {
		getReferentKurs().add(referentKur);
		referentKur.setKur(this);

		return referentKur;
	}

	public Referent_Kurs removeReferentKur(Referent_Kurs referentKur) {
		getReferentKurs().remove(referentKur);
		referentKur.setKur(null);

		return referentKur;
	}

	public List<Zeitplanung> getZeitplanungs() {
		return this.zeitplanungs;
	}

	public void setZeitplanungs(List<Zeitplanung> zeitplanungs) {
		this.zeitplanungs = zeitplanungs;
	}

	public Zeitplanung addZeitplanung(Zeitplanung zeitplanung) {
		getZeitplanungs().add(zeitplanung);
		zeitplanung.setKur(this);

		return zeitplanung;
	}

	public Zeitplanung removeZeitplanung(Zeitplanung zeitplanung) {
		getZeitplanungs().remove(zeitplanung);
		zeitplanung.setKur(null);

		return zeitplanung;
	}

}