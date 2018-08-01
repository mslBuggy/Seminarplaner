package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Referent_Kurs database table.
 * 
 */
@Entity
@NamedQuery(name="Referent_Kurs.findAll", query="SELECT r FROM Referent_Kurs r")
public class Referent_Kurs implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="LfdNr")
	private int lfdNr;

	//bi-directional many-to-one association to Kurs
	@ManyToOne
	@JoinColumn(name="KursNr")
	private Kurs kur;

	//bi-directional many-to-one association to Referent
	@ManyToOne
	@JoinColumn(name="ReferentenNr")
	private Referent referent;

	public Referent_Kurs() {
	}

	public int getLfdNr() {
		return this.lfdNr;
	}

	public void setLfdNr(int lfdNr) {
		this.lfdNr = lfdNr;
	}

	public Kurs getKur() {
		return this.kur;
	}

	public void setKur(Kurs kur) {
		this.kur = kur;
	}

	public Referent getReferent() {
		return this.referent;
	}

	public void setReferent(Referent referent) {
		this.referent = referent;
	}

}