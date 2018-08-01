package drucken;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class DruckenZentral {
	File file;
	PdfWriter writer;
	Document doc;
	Paragraph hauptPage;
	
	public DruckenZentral(String ueberschrift)
	{
		try {
			file = File.createTempFile("Seminarplaner", ".pdf");
			doc = new Document(PageSize.A4);
			
		    
			
			writer = PdfWriter.getInstance(doc, new FileOutputStream(file));
			writer.setPageEvent(new HeaderFooter() );
			
			doc.open();
			doc.addTitle(this.getClass().getSimpleName());
			
			hauptPage = new Paragraph();
			
			
			
			hauptPage.add(new Paragraph(" "));
			Paragraph titel = new Paragraph(ueberschrift,new Font(FontFamily.TIMES_ROMAN,30,Font.BOLD));
			titel.setAlignment(Element.ALIGN_CENTER);
			hauptPage.add(titel);
			
			this.doc.add(hauptPage);
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public void Dokumentanzeigen()
	{
		// Aufruf der PDF Datei
		doc.close();
		writer.close();
		try {
			Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String nullPruefung(Object p)
	{
		if (p== null)
			return "";
		else
			return p.toString();
	}
	
	class HeaderFooter extends PdfPageEventHelper
	{
        private int seite=1;
		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			// TODO Auto-generated method stub
			super.onEndPage(writer, document);
			
			try
			{
				 /*ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT,
							new Phrase("Seite "+seite++),document.right(), document.bottom(),0);
					ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT,
								new Phrase("Erstellt am : "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))),document.left(), document.bottom(),0);
				*/
			
		
			
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		@Override
		public void onStartPage(PdfWriter writer, Document document) {
			// TODO Auto-generated method stub
			super.onStartPage(writer, document);
			try
			{
				
				Image img = Image.getInstance("src/hauptsteuerung/gfi_RGB.jpg");
				Chunk c = new Chunk(img, 10, 50);
				ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT,
						new Phrase(c),document.left()+50,document.top()+30,180);
			    ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT,
						new Phrase("GFI München"),document.right()-50,document.top(),0);
			    ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT,
						new Phrase("Seminarplaner Seite "+ seite++),document.right()-50,document.top()-20,0);
			   	ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT,
							new Phrase("Erstellt am : "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))),document.right()-50, document.top()-40,0);
			
		
	
			    
			   
				for(int i=0;i<3;i++)
					document.add(new Paragraph(" "));
			
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		
		
	}
	
}
