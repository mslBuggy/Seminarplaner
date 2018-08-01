package hauptsteuerung;

import java.time.LocalDate;

/**
 * Klasse für die  Feiertage
 * @author haug_heinrich
 *
 */
public class Feiertage {
	private LocalDate datum;
	private String bezeichnung;
	
	
	public LocalDate getDatum() {
		return datum;
	}
	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	public Feiertage(LocalDate datum, String bezeichnung) {
		super();
		this.datum = datum;
		this.bezeichnung = bezeichnung;
	}
	
	

}
