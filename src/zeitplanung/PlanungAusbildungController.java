package zeitplanung;


import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import drucken.TermineDrucken;
import hauptsteuerung.ZentraleDienste;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ContextMenuBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Ausbildung;
import model.AusbildungsfestTermine;
import model.Kurs;
import model.Raum;
import model.Referent;
import model.Zeitplanung;
import zeitplanung.TerminDetailsController;

/**
 * Controllerklasse für PlanungAusbildung, eine grafische Oberfläche,
 *  um Termine einer bestimmten  Ausbildung in den Kalenderwochen eines Jahres
 *  anzuzeigen.
 *  Bei Klick auf eine KW (als GridPane realisiert) kann man Ausbildungstermine
 *  erstellen und/oder bearbeiten; allerdings nur, wenn diese Woche nicht bereits
 *  mit einem von extern festgelegten Termin (farblich gekennzeichnet) belegt ist. 
 * @author lara-bisch_manuel
 *
 */
public class PlanungAusbildungController {
	
	private int jahr = 2018;
	private HashMap<GridPane, Zeitplanung> hg = new HashMap<>();
	private List<Zeitplanung> liZeit;
	private MenuItem miWoche = new MenuItem("Wochendetails");
	private MenuItem miTerm = new MenuItem("Termin erstellen/bearbeiten");
	private ContextMenu cm = new ContextMenu(miWoche, miTerm);
	
	@FXML
	private ComboBox<Ausbildung> cbAusbildung;
	private ObservableList<Ausbildung> cbAusbildungData = FXCollections.observableArrayList();
	
	@FXML
	private ComboBox<String> cbJahr;
	
	@FXML
	private Button btDrucken;
	
	private VBox vbMonate = new VBox();
	private HBox hbGesamt = new HBox();
	
	@FXML 
	private BorderPane bpHaupt;
	
	private GridPane gpKlick;
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	
	
	@FXML
	private void initialize()
	{
		cbAusbildung.setConverter(new StringConverter<Ausbildung>() {

			@Override
			public String toString(Ausbildung object) {
				return object.getAusbildungsBezeichnung();
			}

			@Override
			public Ausbildung fromString(String string) {
				return null;
			}
		});
		
		// Füllen der Jahres-Box
		cbJahr.getItems().add(((Integer)LocalDate.now().getYear()).toString());
		cbJahr.getItems().add(((Integer)LocalDate.now().plusYears(1).getYear()).toString());
		cbJahr.getSelectionModel().selectFirst();
		jahr = LocalDate.now().getYear();
		
		for(int i =0;i<12;i++)
		{   String s = LocalDate.of(jahr,i+1,1).format(DateTimeFormatter.ofPattern("MMMM"));
		   	vbMonate.getChildren().add(new Label(s));
		}
		for (int i = 0; i < 12; i++){
			Label l = (Label) vbMonate.getChildren().get(i);
			l.setPrefHeight(111);
			vbMonate.getChildren().set(i, l);
		}
		vbMonate.setPadding(new Insets(0, 0, 0, 4));
		hbGesamt.getChildren().add(vbMonate);	
	}
	
	/**
	 * Diese Methode wird beim Aktualisieren der Auswahl Ausbildung bzw. Jahr ausgeführt.
	 * Sie sorgt dafür, dass sich das Fenster unter der Kopfzeile
	 * mit einer Wochentabelle mit passendem Inhalt füllt.
	 */
	public void aufbau()
	{
		emf = Persistence.createEntityManagerFactory("Seminarplaner");
		em = emf.createEntityManager();
		hbGesamt.getChildren().clear();
		hg.clear();
		VBox vb = new VBox();
		vb.setPrefWidth(1600);
		vb.getStyleClass().add("VBox");
		//vb.setStyle("-fx-border-color: yellow");
		for (int i=0;i<12;i++)
		{
			FlowPane fp = new FlowPane();
			fp.getStyleClass().add("fp");
			WochengridAufbauen(fp,i+1);
			vb.getChildren().add(fp);
		}
		hbGesamt.getChildren().addAll(vbMonate,vb);
		ScrollPane sp = new ScrollPane(); // Scrollpane ist für Scrollbalken am rechten und unteren Fensterrand zuständig
		sp.setContent(hbGesamt);
		bpHaupt.setCenter(sp);
		
	}
	
	public void cbJahr_Click(ActionEvent event)
	{
		ComboBox<String> cb = (ComboBox<String>) event.getSource();
		jahr = Integer.parseInt(cb.getSelectionModel().getSelectedItem());
		this.aufbau();
	}
	
	@FXML
	public void cbAusbildung_Click(ActionEvent event)
	{
		ComboBox<String> cb = (ComboBox<String>) event.getSource();
		this.aufbau();
		
	}
	
	public void btDrucken_Click(ActionEvent event){
		ObservableList<Zeitplanung> liZ = FXCollections.observableArrayList();
		try {
			liZ.addAll(liZeit);
			String s = cbAusbildung.getSelectionModel().getSelectedItem().getAusbildungsBezeichnung();
			s += " " + cbJahr.getSelectionModel().getSelectedItem();
			@SuppressWarnings("unused")
			TermineDrucken t = new TermineDrucken(liZ, s);
		} catch (NullPointerException e) {
			Alert a = new Alert(AlertType.WARNING);
			a.setTitle("Bitte Ausbildungsgang auswählen");
			a.setHeaderText("");
			a.setContentText("Bitte wählen Sie erst einen Ausbildungsgang aus.");
			a.showAndWait();
		}
		
	}
	
	/**
	 * Setzt GridPanes für die Kalenderwochen eines gegebenen Monats
	 * in ein gegebenes FlowPane ein.
	 * @param fp Das FlowPane, welches die GridPanes des gewünschten Monats enthalten soll.
	 * @param monat Der Monat als Ganzzahl 1-12
	 */
	private void WochengridAufbauen(FlowPane fp,int monat)
	{
		int d =  LocalDate.of(jahr, monat, 1).getDayOfWeek().getValue();
		LocalDate ld = LocalDate.of(jahr, monat, 1).minusDays(d-1);
		int letzterTag = LocalDate.of(jahr, monat, 1).lengthOfMonth();
		
		for(int i=0;i<6;i++)
		{
			GridPane g = new GridPane();
			g.getStyleClass().add("gp");
			// (Rechtsklick soll nun Kontextmenü zur Auswahl gewünschter Terminart öffnen)
			g.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					GridPane g = (GridPane) event.getSource();
					Label l = (Label) getNodeByRowColumnIndex(2, 1, g);
					if(l != null && l.getText().matches("Berufsschule|Betriebsferien")){
						miTerm.setDisable(true); // bei Festterminen (außer Feiertagen) wird keine Bearbeitung möglich
					} else
						miTerm.setDisable(false);
					miTerm.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							// TODO Method created by lara-bisch_manuel on 29.01.2018 16:05:15
							zeitplanungAufrufen(hg.get(g));
						}
					});
					miWoche.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							// TODO Method created by lara-bisch_manuel on 29.01.2018 16:06:23
							aufbauWochenDetails(g, cbAusbildung.getSelectionModel().getSelectedItem());
						}
					});
					System.out.println(hg.get(g));
					gpKlick = g;
					cm.setAnchorX(event.getScreenX());
					cm.setAnchorY(event.getScreenY());
					cm.show(g.getScene().getWindow());
					
					
					
					
				}
			});
			
			g.getColumnConstraints().add(new ColumnConstraints(100,100,100));
			g.getColumnConstraints().add(new ColumnConstraints(100,100,100));
			
			g.getRowConstraints().add(new RowConstraints(30,30,30));
			g.getRowConstraints().add(new RowConstraints(30,30,30));
			g.getRowConstraints().add(new RowConstraints(30,30,30));
		
			g.add(new Label("KW"), 0, 0);
			g.add(new Label(ld.format(DateTimeFormatter.ofPattern("w"))), 1, 0);
			g.add(new Label(ld.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))), 0, 1);
		
			g.add(new Label(ld.plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))),1,1);
			
			// Nächster Monat darf nicht mehr drin sein
			if (ld.plusDays(7).getMonth().getValue() != monat)
			{
				return;
			}
			// datenbankAbfrage mit Kurs#
			// DONE: Neue Zeile "Referenten" mit einfügen
			g.add(new Label("Kurs"), 0, 2);
			g.add(new Label("Referent"), 0, 3);
			
			Zeitplanung k =pruefenAusbildungZeitraum(ld);
			
			if (k != null)
			{
				g.add(new Label(k.getKur().getKursBezeichnung()), 1, 2);
				g.add(new Label(k.getReferent().getNachname()), 1, 3);
				if (k.getRaum().getRaumNr() == null){
					// Fester Termin
					if (k.getKur().getKursBezeichnung().equals("Berufsschule")){
						g.getStyleClass().add("bstermine");
						
					} else if(k.getKur().getKursBezeichnung().equals("Feiertag")){
						g.setStyle("-fx-background-color: #89ef2a; -fx-font-style: oblique;");
					} else {
						g.getStyleClass().add("feiertage");
					}
					
				}
				hg.put(g, k);
				
			}
			else
				hg.put(g, null);
			ld = ld.plusDays(7);
			fp.getChildren().add(g);
		}
	}
	
	
	/**
	 * Prüft, ob sich ein bestimmtes Datum innerhalb der Dauer
	 * eines geplanten Termins (Zeitplanung oder Ausbildungsfesttermin)
	 * befindet.
	 * Wenn ja, gibt es das Zeitplanung-Objekt für den Termin zurück.
	 * @param ld Das zu prüfende Datum
	 * @return entweder null, oder der Termin (falls vorhanden)
	 */
	public Zeitplanung pruefenAusbildungZeitraum(LocalDate ld)
	{
		if (cbAusbildung.getSelectionModel().getSelectedItem() != null)
		{
			Ausbildung a = cbAusbildung.getSelectionModel().getSelectedItem();
			Query query = em.createQuery("select z from Zeitplanung z"
					+ "	where z.ausbildungsNr = :anr", Zeitplanung.class);
			query.setParameter("anr", a.getAusbildungsNr());
			liZeit =  query.getResultList();
			query = em.createQuery("Select a from AusbildungsfestTermine a "+
					 " where a.jahr = "+jahr);
			List<AusbildungsfestTermine> liA = query.getResultList();
			//System.out.println(liA.size());
			for (AusbildungsfestTermine aft : liA) {
				Kurs k = new Kurs();
				switch (aft.getArttermin()) {
				case 1:
					if(aft.getAusbildung() != null && aft.getAusbildung().getAusbildungsNr() != cbAusbildung.getSelectionModel().getSelectedItem().getAusbildungsNr())
						continue;
					k.setKursBezeichnung("Berufsschule");
					break;
				case 2:
					k.setKursBezeichnung("Betriebsferien");
					break;
				case 3:
					k.setKursBezeichnung("Feiertag");
					break;
				case 4:
					k.setKursBezeichnung("Sonstiger Termin");
					break;
				}
				Zeitplanung z = new Zeitplanung();
				z.setAnfangsDatum(aft.getDatumvon());
				z.setEndDatum(aft.getDatumbis());
				z.setBemerkung(aft.getBezeichnung());
				Referent r = new Referent();
				r.setVorname("");
				r.setNachname("");
				z.setReferent(r);
				z.setKur(k);
				z.setRaum(new Raum());
				liZeit.add(z);
			}
			 liZeit.sort(new Comparator<Zeitplanung>() {

					@Override
					public int compare(Zeitplanung o1, Zeitplanung o2) {
						return o1.getAnfangsDatum().compareTo(o2.getAnfangsDatum());
					}
				});
			
			for(Zeitplanung z : liZeit)
			{
				//System.out.println(z.getAnfangsDatum()+" "+z.getEndDatum());
				LocalDate anfang = z.getAnfangsDatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1);
				LocalDate ende = z.getEndDatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
				// Wenn Beginn nicht Montag, Variable anfang zurückjustieren
				int anfangWT = anfang.plusDays(1).getDayOfWeek().getValue();
				if (anfangWT > 1){
					anfang = anfang.minusDays(anfangWT - 1);
				}
				
				if( ld.isAfter(anfang) && ld.isBefore(ende))
				{
					//System.out.println("ld: "+ ld);
					return z;
				}
			}
		}
		return null;
	}
	
	// AusbildungenCombobox füllen
	public void ausbildungenComboBox()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("Select a from Ausbildung a" );
		cbAusbildungData.addAll(query.getResultList());
		cbAusbildung.setItems(cbAusbildungData);
		System.out.println(query.getResultList().size());
		
	}
	
	/**
	 * Ruft die Maske zum Erstellen bzw. Bearbeiten des übergebenen Termins
	 * (bzw. eines neuen Termins in der angeklickten Kalenderwoche) auf
	 * @param z Übergebener Termin
	 */
	public void zeitplanungAufrufen(Zeitplanung z)
	{
		Ausbildung a = cbAusbildung.getSelectionModel().getSelectedItem();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/zeitplanung/TerminDetails.fxml"));
		try {
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			stage.setTitle("Termindetails");
			Scene sce = new Scene(root,382,422);
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
			sce.getStylesheets().add(css);
			stage.setScene(sce);
			stage.initModality(Modality.WINDOW_MODAL);
			
			// Zur Übergabe benötigen wir hier den Controller der abhängigen Maske
			// Beschaffung: deswegen haben wir den FXMLLoader instanziert
			TerminDetailsController ctl = loader.getController();
			if(z == null || z.getKur().getKursBezeichnung().matches("Berufsschule|Betriebsurlaub|Feiertag")){
				z = new Zeitplanung();
				int year;
				int month;
				int dayOfMonth;
				// Die folgenden Zeilen holen Anfangs-, Enddatum aus dem Text im angeklickten GridPane.
				Label l = (Label) getNodeByRowColumnIndex(1, 0, gpKlick);
				year = Integer.parseInt(l.getText().substring(6));
				month = Integer.parseInt(l.getText().substring(3, 5));
				dayOfMonth = Integer.parseInt(l.getText().substring(0, 2));
				LocalDate da = LocalDate.of(year, month, dayOfMonth);
				l = (Label) getNodeByRowColumnIndex(1, 1, gpKlick);
				year = Integer.parseInt(l.getText().substring(6));
				month = Integer.parseInt(l.getText().substring(3, 5));
				dayOfMonth = Integer.parseInt(l.getText().substring(0, 2));
				LocalDate de = LocalDate.of(year, month, dayOfMonth);
				ctl.setZeitplanung(z, a, da, de);
			}
			else
				ctl.setZeitplanung(z, a);
			stage.showAndWait();
			// DONE: nach Änderung Grid neu aufbauen
			this.aufbau();
			//tbZeitplanungData.clear();
			//this.lesen();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Aufbau und Verarbeitung der Detailsansicht f�r eine Woche
	 */
	private void aufbauWochenDetails(GridPane g, Ausbildung a)
	{
		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/zeitplanung/Wochendetails.fxml"));
		try
		{
			Parent root = loader.load();
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
			Scene sce = new Scene(root);
			sce.getStylesheets().add(css);
			Stage stage = new Stage();
			stage.initOwner(root.getScene().getWindow());
			stage.setTitle("Wochendetails für Ausbildung " + a.getAusbildungsBezeichnung());
			stage.setScene(sce);
			
			
			// Aufbau der Einträge aus den Childrens für die Gridpane
			WochendetailsController ctl = loader.getController();
			
			ctl.aufbau(g.getChildren(),a, liZeit);
		
			stage.show();
				
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	
	}
	
	/**
	 * Methode, um aus gegebener Zeile und Spalte eines GridPanes
	 * das entsprechende Element zu holen.
	 * Quelle: https://stackoverflow.com/a/20828724
	 * @param row Gegebene Zeile
	 * @param column Gegebene Spalte
	 * @param gridPane Gegebenes GridPane-Objekt
	 * @return Node-Objekt an gegebener Position
	 */
	public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
	    Node result = null;
	    ObservableList<Node> childrens = gridPane.getChildren();

	    for (Node node : childrens) {
	        if(gridPane.getRowIndex(node) == row
	        		&& gridPane.getColumnIndex(node) == column) {
	            result = node;
	            break;
	        }
	    }

	    return result;
	}

}
