package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Ausbildung database table.
 * 
 */
@Entity
@NamedQuery(name="Ausbildung.findAll", query="SELECT a FROM Ausbildung a")
public class Ausbildung implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="AusbildungsNr")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ausbildungsNr;

	@Column(name="Abschlussart")
	private int abschlussart;

	@Column(name="AendBenutzer")
	private String aendBenutzer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AendDatum")
	private Date aendDatum;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AnfangsDatum")
	private Date anfangsDatum;

	@Column(name="AusbildungsBezeichnung")
	private String ausbildungsBezeichnung;

	@Column(name="Bemerkung")
	private String bemerkung;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EndDatum")
	private Date endDatum;

	//bi-directional many-to-one association to Seminarleiter
	@ManyToOne
	@JoinColumn(name="SeminarleiterNr")
	private Seminarleiter seminarleiter;

	//bi-directional many-to-one association to Ausbildung_Kurs
	@OneToMany(mappedBy="ausbildung")
	private List<Ausbildung_Kurs> ausbildungKurs;

	//bi-directional many-to-one association to Teilnehmer_Ausbildung
	@OneToMany(mappedBy="ausbildung")
	private List<Teilnehmer_Ausbildung> teilnehmerAusbildungs;

	public Ausbildung() {
	}

	public int getAusbildungsNr() {
		return this.ausbildungsNr;
	}

	public void setAusbildungsNr(int ausbildungsNr) {
		this.ausbildungsNr = ausbildungsNr;
	}

	public int getAbschlussart() {
		return this.abschlussart;
	}

	public void setAbschlussart(int abschlussart) {
		this.abschlussart = abschlussart;
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

	public Date getAnfangsDatum() {
		return this.anfangsDatum;
	}

	public void setAnfangsDatum(Date anfangsDatum) {
		this.anfangsDatum = anfangsDatum;
	}

	public String getAusbildungsBezeichnung() {
		return this.ausbildungsBezeichnung;
	}

	public void setAusbildungsBezeichnung(String ausbildungsBezeichnung) {
		this.ausbildungsBezeichnung = ausbildungsBezeichnung;
	}

	public String getBemerkung() {
		return this.bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public Date getEndDatum() {
		return this.endDatum;
	}

	public void setEndDatum(Date endDatum) {
		this.endDatum = endDatum;
	}

	public Seminarleiter getSeminarleiter() {
		return this.seminarleiter;
	}

	public void setSeminarleiter(Seminarleiter seminarleiter) {
		this.seminarleiter = seminarleiter;
	}

	public List<Ausbildung_Kurs> getAusbildungKurs() {
		return this.ausbildungKurs;
	}

	public void setAusbildungKurs(List<Ausbildung_Kurs> ausbildungKurs) {
		this.ausbildungKurs = ausbildungKurs;
	}

	public Ausbildung_Kurs addAusbildungKur(Ausbildung_Kurs ausbildungKur) {
		getAusbildungKurs().add(ausbildungKur);
		ausbildungKur.setAusbildung(this);

		return ausbildungKur;
	}

	public Ausbildung_Kurs removeAusbildungKur(Ausbildung_Kurs ausbildungKur) {
		getAusbildungKurs().remove(ausbildungKur);
		ausbildungKur.setAusbildung(null);

		return ausbildungKur;
	}

	public List<Teilnehmer_Ausbildung> getTeilnehmerAusbildungs() {
		return this.teilnehmerAusbildungs;
	}

	public void setTeilnehmerAusbildungs(List<Teilnehmer_Ausbildung> teilnehmerAusbildungs) {
		this.teilnehmerAusbildungs = teilnehmerAusbildungs;
	}

	public Teilnehmer_Ausbildung addTeilnehmerAusbildung(Teilnehmer_Ausbildung teilnehmerAusbildung) {
		getTeilnehmerAusbildungs().add(teilnehmerAusbildung);
		teilnehmerAusbildung.setAusbildung(this);

		return teilnehmerAusbildung;
	}

	public Teilnehmer_Ausbildung removeTeilnehmerAusbildung(Teilnehmer_Ausbildung teilnehmerAusbildung) {
		getTeilnehmerAusbildungs().remove(teilnehmerAusbildung);
		teilnehmerAusbildung.setAusbildung(null);

		return teilnehmerAusbildung;
	}

}