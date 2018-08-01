package drucken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.ObservableList;
import model.Teilnehmer;

public class TeilnehmerDrucken extends DruckenZentral {

	
	
	public TeilnehmerDrucken()
	{
		super("Teilnehmerliste");	
			
		
	}
	
	public void alleTeilnehmer(ObservableList<Teilnehmer> liTeilnehmer)
	{
		try
		{
		Paragraph p = new Paragraph("Alle Teilnehmer",new Font(FontFamily.TIMES_ROMAN,14,Font.BOLD));
		p.setAlignment(Element.ALIGN_CENTER);
		doc.add(p);
		
		PdfPTable tn = new PdfPTable(8);
		tn.setHorizontalAlignment(Element.ALIGN_LEFT);
		tn.setWidthPercentage(100);
		float[] w = {1,3,3,1,2,3,3,2};
		
			tn.setWidths(w);
		
		
		// �berschrift
		PdfPCell cell = new PdfPCell(new Paragraph("TNR",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
		tn.addCell(cell);
		cell = new PdfPCell(new Paragraph("Nachname",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
		tn.addCell(cell);
		cell = new PdfPCell(new Paragraph("Vorname",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
		tn.addCell(cell);
		cell = new PdfPCell(new Paragraph("G",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
		tn.addCell(cell);
		cell = new PdfPCell(new Paragraph("GebDatum",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
		tn.addCell(cell);
		cell = new PdfPCell(new Paragraph("Adresse",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
		tn.addCell(cell);
		cell = new PdfPCell(new Paragraph("Telefon",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
		tn.addCell(cell);
		cell = new PdfPCell(new Paragraph("Aktiv",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
		tn.addCell(cell);
		
		for(Teilnehmer t : liTeilnehmer)
		{
			cell = new PdfPCell(new Paragraph(((Integer)t.getTeilnehmerNr()).toString(),new Font(FontFamily.TIMES_ROMAN,8)));
			tn.addCell(cell);
			cell = new PdfPCell(new Paragraph(t.getNachname(),new Font(FontFamily.TIMES_ROMAN,8)));
			tn.addCell(cell);
			cell = new PdfPCell(new Paragraph(t.getVorname(),new Font(FontFamily.TIMES_ROMAN,8)));
			tn.addCell(cell);
			cell = new PdfPCell(new Paragraph(String.valueOf(t.getGeschlecht()),new Font(FontFamily.TIMES_ROMAN,8)));
			tn.addCell(cell);
			cell = new PdfPCell(new Paragraph(new SimpleDateFormat("dd.MM.yyyy").format(t.getGebDatum()),new Font(FontFamily.TIMES_ROMAN,8)));
			tn.addCell(cell);
			String adr =this.nullPruefung(t.getStrasse())+" "+
			            this.nullPruefung(t.getHausNr())+"\n"+
			            this.nullPruefung(t.getPlz())+" "+
			            this.nullPruefung(t.getOrt());
			cell = new PdfPCell(new Paragraph(adr,new Font(FontFamily.TIMES_ROMAN,8)));
			tn.addCell(cell);
			String tel = this.nullPruefung(t.getTelFestnetz()+"\n"+
			             this.nullPruefung(t.getTelMobil()));
			cell = new PdfPCell(new Paragraph(tel,new Font(FontFamily.TIMES_ROMAN,8)));
			tn.addCell(cell);
			if (t.getAktiv())
				cell = new PdfPCell(new Paragraph("ja",new Font(FontFamily.TIMES_ROMAN,8)));
			else
				cell = new PdfPCell(new Paragraph("nein",new Font(FontFamily.TIMES_ROMAN,8)));
			tn.addCell(cell);
		}
		
			tn.setHeaderRows(1);
		
			
			this.doc.add(new Paragraph(" "));
			this.doc.add(tn);
	
	
			this.Dokumentanzeigen();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
}
