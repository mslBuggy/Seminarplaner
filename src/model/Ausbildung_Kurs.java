package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Ausbildung_Kurs database table.
 * 
 */
@Entity
@NamedQuery(name="Ausbildung_Kurs.findAll", query="SELECT a FROM Ausbildung_Kurs a")
public class Ausbildung_Kurs implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="LfdNr")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int lfdNr;

	//bi-directional many-to-one association to Ausbildung
	@ManyToOne
	@JoinColumn(name="AusbildungsNr")
	private Ausbildung ausbildung;

	//bi-directional many-to-one association to Kurs
	@ManyToOne
	@JoinColumn(name="KursNr")
	private Kurs kur;

	public Ausbildung_Kurs() {
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

	public Kurs getKur() {
		return this.kur;
	}

	public void setKur(Kurs kur) {
		this.kur = kur;
	}

}