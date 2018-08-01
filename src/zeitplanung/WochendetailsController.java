package zeitplanung;

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
import model.Ausbildung;
import model.Ausbildung_Kurs;
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
	
	private List<Zeitplanung> liZeit;
	private ObservableList<Node> gridDaten;
	private Ausbildung ausbildung;
	private LocalDate beginn;
	private LocalDate ende;
	private LocalDate wochentag;
	
	private EntityManagerFactory emf;
	private EntityManager em;
	/**
	 * Erstellt einen EntityManager und weist den Tabellenspalten Attribute zu
	 */
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

	/**
	 * Baut aus den übergebenen Daten die Überschrift auf, und erstellt Tabelleninhalte
	 * aus der übergebenen Liste von Terminen.
	 * @param gridDaten Liste von Nodes im ausgewählten GridPane aus PlanungAusbildungController
	 * @param ausbildung Ausbildungsgang, für den Termine angezeigt werden sollen
	 * @param liZeit 
	 */
	public void aufbau(ObservableList<Node> gridDaten, Ausbildung ausbildung, List<Zeitplanung> liZeit)
	{
		this.gridDaten = gridDaten;
		this.kw.setText(((Label)gridDaten.get(1)).getText());
		this.von.setText(((Label)gridDaten.get(2)).getText());
		this.bis.setText(((Label)gridDaten.get(3)).getText());
		this.ausbildung = ausbildung;
		this.liZeit = liZeit;

		beginn = LocalDate.parse(this.von.getText(),DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		ende = LocalDate.parse(this.bis.getText(),DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		wochentag = beginn;

		for (int i=0;i<5;i++)
		{
			boolean gefunden = false;
			for(Zeitplanung zeitplanung : liZeit)
			{

				LocalDate danfang = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(zeitplanung.getAnfangsDatum()));
				LocalDate dende = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(zeitplanung.getEndDatum()));  
				if (wochentag.isAfter(danfang.minusDays(1)) && wochentag.isBefore(dende.plusDays(1)))
				{
					//System.out.println(zeitplanung.getKur().getKursBezeichnung());
					System.out.println(liZeit.size());
					if((zeitplanung.getKur().getAusbildungKurs() != null && !zeitplanung.getKur().getAusbildungKurs().isEmpty())){

						for (Ausbildung_Kurs ak : zeitplanung.getKur().getAusbildungKurs()) {
							if(ak.getAusbildung().getAusbildungsNr() == ausbildung.getAusbildungsNr()){
								System.out.println("drin");
								tbWocheData.add(new TagEintrag(wochentag.format(DateTimeFormatter.ofPattern("EEEE")), wochentag.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),zeitplanung.getKur().getKursBezeichnung()));
								gefunden = true;
								break;
							}
						}

					} else if (zeitplanung.getKur().getKursBezeichnung().matches("Feiertag|Berufsschule|Betriebsferien")){

						tbWocheData.add(new TagEintrag(wochentag.format(DateTimeFormatter.ofPattern("EEEE")), wochentag.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),zeitplanung.getKur().getKursBezeichnung()));
						gefunden = true;
						break;
					}
				}
			}
			if (!gefunden)
				tbWocheData.add(new TagEintrag(wochentag.format(DateTimeFormatter.ofPattern("EEEE")), wochentag.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),""));

			wochentag = wochentag.plusDays(1);
		}

	}
	/*
	 * Hilfsklasse für die Einträge in die Tabelle.
	 */
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
