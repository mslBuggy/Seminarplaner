package drucken;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import javafx.collections.ObservableList;
import model.Kurs;
/**
 * Diese Klasse gibt ein druckbares PDF-Dokument mit einer Tabelle aus, die Daten über
 * den Inhalt einer im Konstruktor übergebenen Liste von Kurs-Objekten enthält.
 * Das PDF-Dokument wird unter Windows automatisch mit dem Standardprogramm
 * für diese Art von Datei geöffnet.
 * @author lara-bisch_manuel
 *
 */
public class KurseDrucken extends DruckenZentral {

	public KurseDrucken() {
		super("Kursliste");
		this.Dokumentanzeigen();
	}
	
	public KurseDrucken(ObservableList<Kurs> liK){
		super ("Kursliste");
		this.alleKurse(liK);
		this.Dokumentanzeigen();
	}
	
	public void alleKurse(ObservableList<Kurs> liKurs){
		try {
			Paragraph p = new Paragraph("Alle Kurse", new Font(FontFamily.TIMES_ROMAN,14,Font.BOLD));
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			PdfPTable tKurs = new PdfPTable(6);
			
			float[] w = {0.5f,3,1.7f,1,1.4f,3};
			tKurs.setWidths(w);
			
			// Überschriften
			Font fntUber = new Font(FontFamily.TIMES_ROMAN, 12);
			String[] ueberschriften = {"Nr.", "Bezeichnung", "Dauer Tage", "EDV", "Lehrjahr", "Bemerkung"};
			PdfPCell zelle = new PdfPCell(new Paragraph());
			for (String u : ueberschriften) {
				zelle = new PdfPCell(new Paragraph(u, fntUber));
				tKurs.addCell(zelle);
			}
			tKurs.setHeaderRows(1);
			
			// Inhalt
			Font fntInhalt = new Font(FontFamily.HELVETICA, 10);
			for (Kurs kurs : liKurs) {
				zelle = new PdfPCell(new Paragraph(((Integer)kurs.getKursNr()).toString(), fntInhalt));
				tKurs.addCell(zelle);
				zelle = new PdfPCell(new Paragraph(kurs.getKursBezeichnung(), fntInhalt));
				tKurs.addCell(zelle);
				zelle = new PdfPCell(new Paragraph(((Integer)kurs.getKursDauerTage()).toString(), fntInhalt));
				tKurs.addCell(zelle);
				zelle = new PdfPCell(new Paragraph((kurs.getBrauchtEDV()? "ja" : "nein"), fntInhalt));
				tKurs.addCell(zelle);
				zelle = new PdfPCell(new Paragraph(((Integer)kurs.getLehrjahr()).toString(), fntInhalt));
				tKurs.addCell(zelle);
				zelle = new PdfPCell(new Paragraph(this.nullPruefung(kurs.getBemerkung()), fntInhalt));
				tKurs.addCell(zelle);
			}
			this.doc.add(new Paragraph(" "));
			this.doc.add(tKurs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
