package referentenplanung;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Referent;
import model.Zeitplanung;

public class WochendetailsController {
	
	
	@FXML
	private Label kw;
	
	@FXML
	private Label von;
	
	@FXML
	private Label bis;
	
	@FXML
	private TableView<TagEintrag> tbWoche;
	private ObservableList<TagEintrag> tbWocheData = FXCollections.observableArrayList(); 
	
	@FXML 
	private TableColumn<TagEintrag, String> tcTag;
	
	@FXML 
	private TableColumn<TagEintrag, String> tcDatum;
	
	@FXML 
	private TableColumn<TagEintrag, String> tcKurs;
	
	
	private ObservableList<Node> gridDaten;
	private Referent referent;
	private LocalDate beginn;
	private LocalDate ende;
	private LocalDate wochentag;
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	@FXML
	public void initialize()
	{
		emf = Persistence.createEntityManagerFactory("Seminarplaner");
		em = emf.createEntityManager();
		
		tcTag.setCellValueFactory(new PropertyValueFactory<>("tag"));
		tcDatum.setCellValueFactory(new PropertyValueFactory<>("datum"));
		tcKurs.setCellValueFactory(new PropertyValueFactory<>("kurs"));
		tbWoche.setItems(tbWocheData);
	}
	
	
	public void aufbau(ObservableList<Node> gridDaten, Referent referent)
	{
		this.gridDaten = gridDaten;
		this.kw.setText(((Label)gridDaten.get(1)).getText());
		this.von.setText(((Label)gridDaten.get(2)).getText());
		this.bis.setText(((Label)gridDaten.get(3)).getText());
		
		
		
		
		
		beginn = LocalDate.parse(this.von.getText(),DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		ende = LocalDate.parse(this.bis.getText(),DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		wochentag = beginn;
		
			

		// ToDo lesen alle Kurse des Referenten
		// und dann Abfrage ob für den Wochentag ein Kurs vorhanden ist
		Query query = em.createQuery("Select z from Zeitplanung z where z.referent.referentenNr = "
		               + referent.getReferentenNr());
		            		           
		List<Zeitplanung> lizeitplanung =  query.getResultList();
		
		
		
		
		
		
		
		
		for (int i=0;i<5;i++)
		{
		   boolean gefunden = false;
		   for(Zeitplanung zeitplanung : lizeitplanung)
		   {
			 LocalDate danfang = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(zeitplanung.getAnfangsDatum()));
			 LocalDate dende = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(zeitplanung.getEndDatum()));  
		     if (wochentag.isAfter(danfang.minusDays(1)) && wochentag.isBefore(dende.plusDays(1)))
		     {
		    	 tbWocheData.add(new TagEintrag(wochentag.format(DateTimeFormatter.ofPattern("EEEE")), wochentag.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),zeitplanung.getKur().getKursBezeichnung()));
		    	 gefunden = true;
		    	 break;
		     }
		      
		   
		   }
		   if (!gefunden)
			   tbWocheData.add(new TagEintrag(wochentag.format(DateTimeFormatter.ofPattern("EEEE")), wochentag.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),""));
		  
		   wochentag = wochentag.plusDays(1);
		}
		
		
		
		
		
		
		
	}
	
	public class TagEintrag
	{
		private String tag;
		private String datum;
		private String kurs;
		public String getTag() {
			return tag;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
		public String getDatum() {
			return datum;
		}
		public void setDatum(String datum) {
			this.datum = datum;
		}
		public String getKurs() {
			return kurs;
		}
		public void setKurs(String kurs) {
			this.kurs = kurs;
		}
		
		public TagEintrag(String tag,String datum,String kurs)
		{
			this.tag = tag;
			this.datum = datum;
			this.kurs = kurs;
		}
	}

}
