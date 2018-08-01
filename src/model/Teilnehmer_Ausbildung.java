package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Teilnehmer_Ausbildung database table.
 * 
 */
@Entity
@NamedQuery(name="Teilnehmer_Ausbildung.findAll", query="SELECT t FROM Teilnehmer_Ausbildung t")
public class Teilnehmer_Ausbildung implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="LfdNr")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int lfdNr;

	//bi-directional many-to-one association to Ausbildung
	@ManyToOne
	@JoinColumn(name="AusbildungsNr")
	private Ausbildung ausbildung;

	//bi-directional many-to-one association to Teilnehmer
	@ManyToOne
	@JoinColumn(name="TeilnehmerNr")
	private Teilnehmer teilnehmer;

	public Teilnehmer_Ausbildung() {
	}

	public int getLfdNr() {
		return this.lfdNr;
	}

	public void setLfdNr(int lfdNr) {
		this.lfdNr = lfdNr;
	}

	public Ausbildung getAusbildung() {
		return this.ausbildung;
	}

	public void setAusbildung(Ausbildung ausbildung) {
		this.ausbildung = ausbildung;
	}

	public Teilnehmer getTeilnehmer() {
		return this.teilnehmer;
	}

	public void setTeilnehmer(Teilnehmer teilnehmer) {
		this.teilnehmer = teilnehmer;
	}

}