package drucken;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import hauptsteuerung.ZentraleDienste;

import com.itextpdf.text.Font.FontFamily;

import model.Ausbildung;
import model.Ausbildung_Kurs;
import model.Referent;
import model.Zeitplanung;

public class ReferentenplanungDrucken extends DruckenZentral{
	
	int jahr;
	public ReferentenplanungDrucken(String jahr)
	{
		super("Referentenplanung für "+jahr);
		this.jahr = Integer.parseInt(jahr);
		
	}
	
	public void EinzelnerReferent(Referent r,List<Zeitplanung> liZeitplanung)
	{
		try
		{
			
			Paragraph p = new Paragraph("Referent "+r.getNachname()+" "+r.getVorname(),new Font(FontFamily.TIMES_ROMAN,14,Font.BOLD));
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			
			for (int i=0;i<3;i++)
				doc.add(new Paragraph(" "));
			
			// Überschrift
			PdfPTable zeit = new PdfPTable(7);
			zeit.setHorizontalAlignment(Element.ALIGN_LEFT);
			zeit.setWidthPercentage(100);
			float[] w = {1,2,2,3,3,1,2};
			zeit.setWidths(w);

			PdfPCell cell = new PdfPCell(new Paragraph("KW",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
			zeit.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("Von",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
			zeit.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("Bis",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
			zeit.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("Kurs",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
			zeit.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("Ausbildung",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
			zeit.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("LJ",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
			zeit.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("Raum",new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
			zeit.addCell(cell);
			
			DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE dd.MM.yyyy");
			//Daten
			for(Zeitplanung z : liZeitplanung)
			{
				LocalDate anf = ZentraleDienste.dateToLocalDate(z.getAnfangsDatum());
				LocalDate end = ZentraleDienste.dateToLocalDate(z.getEndDatum());
				int weeka = anf.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
				int weekb = end.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
				
				
				if (this.jahr != anf.getYear())
					continue;
				String kw = ""+weeka;
				if (weeka != weekb)
					kw += " - "+weekb;
				

				cell = new PdfPCell(new Paragraph(kw,new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
				zeit.addCell(cell);
				
				cell = new PdfPCell(new Paragraph(anf.format(format),new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
				zeit.addCell(cell);
				
				cell = new PdfPCell(new Paragraph(end.format(format),new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
				zeit.addCell(cell);
				
				cell = new PdfPCell(new Paragraph(z.getKur().getKursBezeichnung(),new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
				zeit.addCell(cell);
				
				String ausbildungsg = "";
				
				for (Ausbildung_Kurs a : z.getKur().getAusbildungKurs())
					ausbildungsg += a.getAusbildung().getAusbildungsBezeichnung()+"\n";
				
				cell = new PdfPCell(new Paragraph(ausbildungsg,new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
				zeit.addCell(cell);
				
				cell = new PdfPCell(new Paragraph(""+z.getKur().getLehrjahr(),new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
				zeit.addCell(cell);
				
				cell = new PdfPCell(new Paragraph(""+z.getRaum().getRaumNr(),new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD)));
				zeit.addCell(cell);
			}
			
			doc.add(zeit);
			this.Dokumentanzeigen();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	
	}
	
	

}
