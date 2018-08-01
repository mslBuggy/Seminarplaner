package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Raum database table.
 * 
 */
@Entity
@NamedQuery(name="Raum.findAll", query="SELECT r FROM Raum r")
public class Raum implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="RaumNr")
	private String raumNr;

	@Column(name="AendBenutzer")
	private String aendBenutzer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AendDatum")
	private Date aendDatum;

	@Column(name="Bemerkung")
	private String bemerkung;

	@Column(name="HatEDV")
	private boolean hatEDV;

	@Column(name="Kapazitaet")
	private int kapazitaet;

	//bi-directional many-to-one association to Zeitplanung
	@OneToMany(mappedBy="raum")
	private List<Zeitplanung> zeitplanungs;

	public Raum() {
	}

	public String getRaumNr() {
		return this.raumNr;
	}

	public void setRaumNr(String raumNr) {
		this.raumNr = raumNr;
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

	public boolean getHatEDV() {
		return this.hatEDV;
	}

	public void setHatEDV(boolean hatEDV) {
		this.hatEDV = hatEDV;
	}

	public int getKapazitaet() {
		return this.kapazitaet;
	}

	public void setKapazitaet(int kapazitaet) {
		this.kapazitaet = kapazitaet;
	}

	public List<Zeitplanung> getZeitplanungs() {
		return this.zeitplanungs;
	}

	public void setZeitplanungs(List<Zeitplanung> zeitplanungs) {
		this.zeitplanungs = zeitplanungs;
	}

	public Zeitplanung addZeitplanung(Zeitplanung zeitplanung) {
		getZeitplanungs().add(zeitplanung);
		zeitplanung.setRaum(this);

		return zeitplanung;
	}

	public Zeitplanung removeZeitplanung(Zeitplanung zeitplanung) {
		getZeitplanungs().remove(zeitplanung);
		zeitplanung.setRaum(null);

		return zeitplanung;
	}

}