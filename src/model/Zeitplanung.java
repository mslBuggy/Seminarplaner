package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the Zeitplanung database table.
 * 
 */
@Entity
@NamedQuery(name="Zeitplanung.findAll", query="SELECT z FROM Zeitplanung z")
public class Zeitplanung implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TerminNr")
	private int terminNr;

	@Column(name="AendBenutzer")
	private String aendBenutzer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AendDatum")
	private Date aendDatum;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AnfangsDatum")
	private Date anfangsDatum;

	@Column(name="Bemerkung")
	private String bemerkung;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EndDatum")
	private Date endDatum;

	@Column(name="Termintyp")
	private int termintyp;
	
	@Column(name="AusbildungsNr")
	private int ausbildungsNr;

	public int getAusbildungsNr() {
		return ausbildungsNr;
	}

	public void setAusbildungsNr(int ausbildungsnr) {
		this.ausbildungsNr = ausbildungsnr;
	}

	//bi-directional many-to-one association to Kurs
	@ManyToOne
	@JoinColumn(name="KursNr")
	private Kurs kur;

	//bi-directional many-to-one association to Raum
	@ManyToOne
	@JoinColumn(name="RaumNr")
	private Raum raum;

	

	//bi-directional many-to-one association to Referent
	@ManyToOne
	@JoinColumn(name="ReferentenNr")
	private Referent referent;

	public Zeitplanung() {
	}

	public int getTerminNr() {
		return this.terminNr;
	}

	public void setTerminNr(int terminNr) {
		this.terminNr = terminNr;
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

	public int getTermintyp() {
		return this.termintyp;
	}

	public void setTermintyp(int termintyp) {
		this.termintyp = termintyp;
	}

	public Kurs getKur() {
		return this.kur;
	}

	public void setKur(Kurs kur) {
		this.kur = kur;
	}

	public Raum getRaum() {
		return this.raum;
	}

	public void setRaum(Raum raum) {
		this.raum = raum;
	}

	

	public Referent getReferent() {
		return this.referent;
	}

	public void setReferent(Referent referent) {
		this.referent = referent;
	}

}