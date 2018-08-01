package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the AusbildungsfestTermine database table.
 * 
 */
@Entity
@NamedQuery(name="AusbildungsfestTermine.findAll", query="SELECT a FROM AusbildungsfestTermine a")
public class AusbildungsfestTermine implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="Lfdnr")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int lfdnr;

	@Column(name="Arttermin")
	private int arttermin;

	@Column(name="Bezeichnung")
	private String bezeichnung;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Datumbis")
	private Date datumbis;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Datumvon")
	private Date datumvon;

	@Column(name="Jahr")
	private int jahr;

	@Column(name="Lehrjahr")
	private int lehrjahr;

	//bi-directional many-to-one association to Ausbildung
	@ManyToOne
	@JoinColumn(name="Ausbildungsnr")
	private Ausbildung ausbildung;

	public AusbildungsfestTermine() {
	}

	public int getLfdnr() {
		return this.lfdnr;
	}

	public void setLfdnr(int lfdnr) {
		this.lfdnr = lfdnr;
	}

	public int getArttermin() {
		return this.arttermin;
	}

	public void setArttermin(int arttermin) {
		this.arttermin = arttermin;
	}

	public String getBezeichnung() {
		return this.bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public Date getDatumbis() {
		return this.datumbis;
	}

	public void setDatumbis(Date datumbis) {
		this.datumbis = datumbis;
	}

	public Date getDatumvon() {
		return this.datumvon;
	}

	public void setDatumvon(Date datumvon) {
		this.datumvon = datumvon;
	}

	public int getJahr() {
		return this.jahr;
	}

	public void setJahr(int jahr) {
		this.jahr = jahr;
	}

	public int getLehrjahr() {
		return this.lehrjahr;
	}

	public void setLehrjahr(int lehrjahr) {
		this.lehrjahr = lehrjahr;
	}

	public Ausbildung getAusbildung() {
		return this.ausbildung;
	}

	public void setAusbildung(Ausbildung ausbildung) {
		this.ausbildung = ausbildung;
	}

}