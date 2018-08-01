package drucken;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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
import model.Zeitplanung;
/**
 * Diese Klasse gibt ein druckbares PDF-Dokument mit einer Tabelle aus, die Daten über
 * den Inhalt einer im Konstruktor übergebenen Liste von Zeitplanung-Objekten enthält.
 * Zudem werden Leerzeilen mit erzeugt, falls eine Kalenderwoche im Jahr
 * keinerlei Terminbuchungen enthält.
 * @author lara-bisch_manuel
 *
 */
public class TermineDrucken extends DruckenZentral {
	
	int jahr;

	public TermineDrucken() {
		super("Terminliste");
		this.Dokumentanzeigen();
	}
	
	public TermineDrucken(ObservableList<Zeitplanung> liZ){
		super("Terminliste");
		this.alleTermine(liZ, " ");
		this.Dokumentanzeigen();
	}
	
	public TermineDrucken(ObservableList<Zeitplanung> liZ, String s){
		super("Terminliste");
		this.jahr = Integer.parseInt(s.substring(s.length()-4));
		System.out.println(jahr);
		this.alleTermine(liZ, s);
		this.Dokumentanzeigen();
	}
	/**
	 * Druckt sämtliche Termine in der übergebenen Liste.
	 * @param liZ Die ObservableList von Zeitplanung-Objekten
	 * @param s Überschrift für die Tabelle
	 */
	public void alleTermine(ObservableList<Zeitplanung> liZ, String s){
		try {
			Paragraph p = new Paragraph(s, new Font(FontFamily.TIMES_ROMAN,14,Font.BOLD));
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			
			PdfPTable tTerm = new PdfPTable(7);
			float[] w = {1.1f,1.6f,1.6f,2.3f,1,2.1f,2.5f};
			tTerm.setWidths(w);
			// Überschriften
			Font fntUber = new Font(FontFamily.TIMES_ROMAN, 12);
			String[] ueberschriften = {"KW", "Beginn", "Ende", "Kurs", "Raum", "Referent", "Bemerkung"};
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
			for (Zeitplanung z : liZ) {
				LocalDate anf = z == null? LocalDate.now() : ZentraleDienste.dateToLocalDate(z.getAnfangsDatum());
				LocalDate end = z == null? LocalDate.now() : ZentraleDienste.dateToLocalDate(z.getEndDatum());
				int weeka = anf.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
				int weekb = end.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
				if (this.jahr != anf.getYear() && this.jahr != end.getYear()){
					System.out.println(anf.getYear() + " != " + this.jahr);
					continue;
				}
				if(weeka > startWoche && z != null){
					for(int i = startWoche; i < weeka; i++){
						// Leerzeile einfügen:
						
						zelle = new PdfPCell(new Paragraph(i + "",fntInhalt));
						tTerm.addCell(zelle);
						// Start der leeren Woche
						LocalDate woche = LocalDate.now();
						woche = woche.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, i-1);
						final TemporalAdjuster adjuster = TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY);
						LocalDate montag = woche.with(adjuster);
						zelle = new PdfPCell(new Paragraph(montag.format(DateTimeFormatter.ofPattern("dd.MM.uuuu")),fntInhalt));
						tTerm.addCell(zelle);
						zelle = new PdfPCell(new Paragraph(" ",fntInhalt));
						tTerm.addCell(zelle);
						tTerm.addCell(zelle);
						tTerm.addCell(zelle);
						tTerm.addCell(zelle);
						tTerm.addCell(zelle);
					}
				}
				startWoche = weekb+1;	
				String kw = ""+weeka;
				if (weeka != weekb || anf.getYear() != end.getYear())
					kw += " - "+weekb;
				
				zelle = new PdfPCell(new Paragraph(z == null? " " : kw,fntInhalt));
				tTerm.addCell(zelle);
				zelle = new PdfPCell(new Paragraph((z == null? " " : (ZentraleDienste.dateToString(z.getAnfangsDatum()))), fntInhalt));
				tTerm.addCell(zelle);
				zelle = new PdfPCell(new Paragraph(z == null? " " : (ZentraleDienste.dateToString(z.getEndDatum())), fntInhalt));
				tTerm.addCell(zelle);
				zelle = new PdfPCell(new Paragraph((z == null? " " : z.getKur().getKursBezeichnung()), fntInhalt));
				tTerm.addCell(zelle);
				zelle = new PdfPCell(new Paragraph(z == null? " " : z.getRaum().getRaumNr(), fntInhalt));
				tTerm.addCell(zelle);
				zelle = new PdfPCell(new Paragraph(z == null? " " : z.getReferent().getVorname() + " " +z.getReferent().getNachname(), fntInhalt));
				tTerm.addCell(zelle);
				zelle = new PdfPCell(new Paragraph(z == null? " " : nullPruefung(z.getBemerkung()), fntInhalt));
				tTerm.addCell(zelle);
			}
			// TODO: Leerzeilen für KWs ohne geplanten Termin
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

}
