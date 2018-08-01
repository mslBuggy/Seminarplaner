package drucken;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;

import hauptsteuerung.ZentraleDienste;

import com.itextpdf.text.Font.FontFamily;

import javafx.collections.ObservableList;
import model.AusbildungsfestTermine;
/**
 * Diese Klasse gibt ein druckbares PDF-Dokument mit einer Tabelle aus, die Daten über
 * den Inhalt einer im Konstruktor übergebenen Liste von AusbildungsfestTermine-Objekten enthält.
 * Zudem werden Leerzeilen mit erzeugt, falls eine Kalenderwoche im Jahr
 * keinerlei Terminbuchungen enthält. Das PDF-Dokument wird unter Windows automatisch
 * mit dem Standardprogramm für diese Art von Datei geöffnet.
 * @author lara-bisch_manuel
 *
 */
public class FestTermineDrucken extends DruckenZentral {
	
	int jahr;

	public FestTermineDrucken() {
		super("Terminliste");
		this.Dokumentanzeigen();
	}
	
	public FestTermineDrucken(ObservableList<AusbildungsfestTermine> liA){
		super("Terminliste");
		this.alleTermine(liA, " ");
		this.Dokumentanzeigen();
	}
	
	public FestTermineDrucken(ObservableList<AusbildungsfestTermine> liA, String s){
		super("Terminliste");
		this.jahr = liA.get(0).getDatumbis().getYear() + 1900;
		System.out.println(jahr);
		this.alleTermine(liA, s);
		this.Dokumentanzeigen();
	}
	/**
	 * Druckt sämtliche Termine in der übergebenen Liste.
	 * @param liZ Die ObservableList von AusbildungsfestTermine-Objekten
	 * @param s Überschrift für die Tabelle
	 */
	public void alleTermine(ObservableList<AusbildungsfestTermine> liA, String s){
		try {
			Paragraph p = new Paragraph(s, new Font(FontFamily.TIMES_ROMAN,14,Font.BOLD));
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			
			PdfPTable tTerm = new PdfPTable(5);
			float[] w = {1.1f,1.6f,1.6f,2.3f,2.5f};
			tTerm.setWidths(w);
			// Überschriften
			Font fntUber = new Font(FontFamily.TIMES_ROMAN, 12);
			String[] ueberschriften = {"KW", "Beginn", "Ende", "Art Termin", "Bemerkung"};
			PdfPCell zelle = new PdfPCell(new Paragraph());
			for (String u : ueberschriften) {
				zelle = new PdfPCell(new Paragraph(u, fntUber));
				tTerm.addCell(zelle);
			}
			tTerm.setHeaderRows(1);
			
			// Inhalt
			Font fntInhalt = new Font(FontFamily.HELVETICA, 10);
			int startWoche = 1;
			// for(int i = 0; i < 20; i++) // Entkommentieren, um Seitenumbruch zu testen
			for (AusbildungsfestTermine aft : liA) {
				LocalDate anf = aft == null? LocalDate.now() : ZentraleDienste.dateToLocalDate(aft.getDatumvon());
				LocalDate end = aft == null? LocalDate.now() : ZentraleDienste.dateToLocalDate(aft.getDatumbis());
				int weeka = anf.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
				int weekb = end.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
				if (this.jahr != anf.getYear() && this.jahr != end.getYear()){
					System.out.println(anf.getYear() + " != " + this.jahr);
					continue;
				}
				if(weeka > startWoche && aft != null){
					for(int i = startWoche; i < weeka; i++){
						// Leerzeile einfügen:
						
						zelle = new PdfPCell(new Paragraph(i + "",fntInhalt));
						tTerm.addCell(zelle);
						// Start der leeren Woche
						LocalDate woche = LocalDate.of(jahr,1,1);
						woche = woche.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR,  i > 1? i-1: i);
						final TemporalAdjuster adjuster = TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY);
						LocalDate montag = woche.with(adjuster);
						zelle = new PdfPCell(new Paragraph(montag.format(DateTimeFormatter.ofPattern("dd.MM.uuuu")),fntInhalt));
						tTerm.addCell(zelle);
						zelle = new PdfPCell(new Paragraph(" ",fntInhalt));
						tTerm.addCell(zelle);
						tTerm.addCell(zelle);
						tTerm.addCell(zelle);
						
					}
				}
				startWoche = weekb+1;	
				String kw = ""+weeka;
				if (weeka != weekb || anf.getYear() != end.getYear())
					kw += " - "+weekb;
				
				zelle = new PdfPCell(new Paragraph(aft == null? " " : kw,fntInhalt));
				tTerm.addCell(zelle);
				zelle = new PdfPCell(new Paragraph((aft == null? " " : (ZentraleDienste.dateToString(aft.getDatumvon()))), fntInhalt));
				tTerm.addCell(zelle);
				zelle = new PdfPCell(new Paragraph(aft == null? " " : (ZentraleDienste.dateToString(aft.getDatumbis())), fntInhalt));
				tTerm.addCell(zelle);
				zelle = new PdfPCell(new Paragraph((aft == null? " " : artTermin(aft.getArttermin())), fntInhalt));
				tTerm.addCell(zelle);
				zelle = new PdfPCell(new Paragraph(aft == null? " " : nullPruefung(aft.getBezeichnung()), fntInhalt));
				tTerm.addCell(zelle);
				long wmax = LocalDate.now().range(IsoFields.WEEK_OF_WEEK_BASED_YEAR).getMaximum();
				if(liA.indexOf(aft) == liA.size() - 1 && startWoche <= wmax){
					for(int i = startWoche + 1; i <= wmax; i++){
						//Leerzeile einfügen:
						zelle = new PdfPCell(new Paragraph(i + "",fntInhalt));
						tTerm.addCell(zelle);
						// Start der leeren Woche
						LocalDate woche = LocalDate.of(jahr, 1, 1);
						woche = woche.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR,  i);
						final TemporalAdjuster adjuster = TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY);
						LocalDate montag = woche.with(adjuster);
						zelle = new PdfPCell(new Paragraph(montag.format(DateTimeFormatter.ofPattern("dd.MM.uuuu")),fntInhalt));
						tTerm.addCell(zelle);
						zelle = new PdfPCell(new Paragraph(" ",fntInhalt));
						tTerm.addCell(zelle);
						tTerm.addCell(zelle);
						tTerm.addCell(zelle);
					}
				}
			}
			for (PdfPRow row : tTerm.getRows()) {
				boolean bsch = false,
						bfer = false,
						bfei = false;
				PdfPCell[] zellen = row.getCells();
				for (PdfPCell cell : zellen) {
					if (cell.getPhrase().getContent().equals("Berufsschule"))
						bsch = true;
					else if (cell.getPhrase().getContent().equals("Betriebsferien"))
						bfer = true;
					else if (cell.getPhrase().getContent().equals("Feiertag"))
						bfei = true;
					else if (cell.getPhrase().getContent().equals("Sonstiger Termin"))
						bfei = true;
				}
				if(bsch)
					for (PdfPCell cell : zellen)
						cell.setBackgroundColor(BaseColor.CYAN);
				if(bfer)
					for (PdfPCell cell : zellen)
						cell.setBackgroundColor(BaseColor.ORANGE);
				if(bfei)
					for (PdfPCell cell : zellen)
						cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				
			}
			doc.add(new Paragraph(" "));
			doc.add(tTerm);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private String artTermin(int art){
		if (art == 1)
			return "Berufsschule";
		if (art == 2)
			return "Betriebsferien";
		if (art == 3)
			return "Feiertag";
		if (art == 4)
			return "Sonstiger Termin";
		return null;
	}

}
