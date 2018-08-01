package zeitplanung;

import java.util.Date;

public class Zeitplanung {
	private int terminNr,
				termintyp;
	private String kursNr,
					raumNr,
					referentNr,
					bemerkung,
					aendBenutzer;
	private Date anfangsDatum,
					endDatum,
					aendDatum;
	
	public Zeitplanung() {
		//  
	}
	public Zeitplanung(int terminNr,
						String kursNr,
						String raumNr,
						Date anfangsDatum,
						Date endDatum,
						int termintyp,
						String referentNr,
						Date aendDatum,
						String aendBenutzer) {
		// TODO Auto-generated constructor stub
		setTerminNr(terminNr);
		setKursNr(kursNr);
		setRaumNr(raumNr);
		setAnfangsDatum(anfangsDatum);
		setEndDatum(endDatum);
		setTermintyp(termintyp);
		setReferentNr(referentNr);
		setBemerkung("");
		setAendDatum(aendDatum);
		setAendBenutzer(aendBenutzer);
	}
	
	public int getTerminNr() {
		return terminNr;
	}
	public void setTerminNr(int terminNr) {
		this.terminNr = terminNr;
	}
	public int getTermintyp() {
		return termintyp;
	}
	public void setTermintyp(int termintyp) {
		this.termintyp = termintyp;
	}
	public String getKursNr() {
		return kursNr;
	}
	public void setKursNr(String kursNr) {
		this.kursNr = kursNr;
	}
	public String getRaumNr() {
		return raumNr;
	}
	public void setRaumNr(String raumNr) {
		this.raumNr = raumNr;
	}
	public String getReferentNr() {
		return referentNr;
	}
	public void setReferentNr(String referentNr) {
		this.referentNr = referentNr;
	}
	public String getBemerkung() {
		return bemerkung;
	}
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	public String getAendBenutzer() {
		return aendBenutzer;
	}
	public void setAendBenutzer(String aendBenutzer) {
		this.aendBenutzer = aendBenutzer;
	}
	public Date getAnfangsDatum() {
		return anfangsDatum;
	}
	public void setAnfangsDatum(Date anfangsDatum) {
		this.anfangsDatum = anfangsDatum;
	}
	public Date getEndDatum() {
		return endDatum;
	}
	public void setEndDatum(Date endDatum) {
		this.endDatum = endDatum;
	}
	public Date getAendDatum() {
		return aendDatum;
	}
	public void setAendDatum(Date aendDatum) {
		this.aendDatum = aendDatum;
	}

}
