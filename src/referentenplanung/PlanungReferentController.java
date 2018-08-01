package referentenplanung;



import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import drucken.ReferentenplanungDrucken;
import hauptsteuerung.Seminarverwaltung;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.TextFieldListCell;
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
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Kurs;
import model.Referent;
import model.Zeitplanung;
import zeitplanung.TerminDetailsController;
import zeitplanung.ZeitplanungContextMenu;

/**
 * Klasse für die Planung der Referenten
 * Es werden pro Kurs und pro Jahr die Kurse angezeigt
 * M�glich ist auch einen neuen Termin anzulegen allerdings nur Kurstermine
 * @author haug_heinrich
 *
 */
public class PlanungReferentController {

	private EntityManagerFactory emf;
	private EntityManager em;
	
	private int jahr = 2018;
	private HashMap<GridPane, Zeitplanung> hg = new HashMap<>();
	
	@FXML
	private ComboBox<Referent> cbReferent;
	private ObservableList<Referent> cbReferantData = FXCollections.observableArrayList();
	
	@FXML
	private ComboBox<String> cbJahr;
	@FXML
	private Button btDrucken;
	
	
	//@FXML 
	/**
	 * Box für die Monate
	 */
	private VBox vbMonate = new VBox();
	//@FXML 
	/**
	 * Hauptbox für die Aufnahme der Monatstabelle und der Planung
	 * Die HBox wird in einer ScrollPane verpackt
	 */
	private HBox hbGesamt = new HBox();
	
	@FXML 
	private BorderPane bpHaupt;
	
	public ContextMenu cm;
	
	private Window aktWindow;
	
	
	
	@FXML
	/**
	 * Für den Referenten werden Name und Vorname angezeigt
	 * Es wird das laufende Jahr und das nächste Jahr in der Combobox angezeigt
	 * Ebensfalls  aufgebaut werden die Monate eines Jahres
	 */
	private void initialize()
	{
		cm = new ContextMenu();
		cm.getItems().add(new MenuItem("Details"));
		aktWindow= cm.getScene().getWindow();
		
		cbReferent.setConverter(new StringConverter<Referent>() {

			@Override
			public String toString(Referent object) {
				// TODO Auto-generated method stub
				return object.getNachname()+" "+object.getVorname();
			}

			@Override
			public Referent fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
//			
		// Füllen der Jahres Box
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
			l.setPrefHeight(90);
			vbMonate.getChildren().set(i, l);
		}
		
		hbGesamt.getChildren().add(vbMonate);
				

		
		
		
	}
	/**
	 * Aufbau der Zeitplantabelle
	 * für jeden Zeitraum wird eine GridPane erstellt
	 * Diese enthält das BeginnDatum (immer Montag) und das Endedatum immer Freitag
	 * Anschließend wird der Kurs (falls vorhanden) gelesen
	 */
	public void aufbau()
	{
		
		
		
		
		hg.clear();
		hbGesamt.getChildren().clear();
		VBox vb = new VBox();
		vb.getStyleClass().add("VBox");
		vb.setPrefWidth(1600);
		
		
		
		for (int i=0;i<12;i++)
		{
			FlowPane fp = new FlowPane();
			fp.getStyleClass().add("fp");
			System.out.println(fp.getStyleClass().size());
			WochengridAufbauen(fp,i+1);
			vb.getChildren().add(fp);
		}
		hbGesamt.getChildren().add(vb);
		ScrollPane sp = new ScrollPane();
		sp.setContent(hbGesamt);
		bpHaupt.setCenter(sp);
		
		
		
		
	}
	/**
	 * Auswahl des Jahres
	 * @param event
	 */
	public void cbJahr_Click(ActionEvent event)
	{
		ComboBox<String> cb = (ComboBox<String>) event.getSource();
		jahr = Integer.parseInt(cb.getSelectionModel().getSelectedItem());
		if (cbReferent.getValue() == null)
		{
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("");
			a.setContentText("Bitte w�hlen Sie erst einen Referenten aus");
			a.showAndWait();
			return;
		}
		this.aufbau();
	}
	
	/**
	 * Auswahl des Referenten
	 * @param event
	 */
	public void cbReferent_Click(ActionEvent event)
	{
		ComboBox<String> cb = (ComboBox<String>) event.getSource();
		
		this.aufbau();
	}
	
	/**
	 * Aufbau der GridPane für einen Monat
	 * wird in die jeweilige FlowPane(Monat) gepackt
	 */
	private void WochengridAufbauen(FlowPane fp,int monat)
	{
		int d =  LocalDate.of(jahr, monat, 1).getDayOfWeek().getValue();
		LocalDate ld = LocalDate.of(jahr, monat, 1).minusDays(d-1);
		int letzerTag = LocalDate.of(jahr, monat, 1).lengthOfMonth();
		
		for(int i=0;i<6;i++)
		{
			
			
			
			GridPane g = new GridPane();
			g.getStyleClass().add("gp");
		   
			g.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {

				

				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
					
					
					GridPane g = (GridPane) event.getSource();
					
					
					cm.setX(event.getScreenX());
					cm.setY(event.getScreenY());
				
					cm.show(g.getScene().getWindow());
					cm.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							// TODO Auto-generated method stub
							Referent r = (Referent) cbReferent.getSelectionModel().getSelectedItem();
							
							aufbauWochenDetails(g,r);
							
						}
					});
					
					
				
					//new ZeitplanungContextMenu.ContextMenuImpl(hg.get(g),g).aufrufKurstermine();
					
					//cm.setOnAction(new ZeitplanungContextMenu.ContextMenuImpl(hg.get(g),g));
					int index = cbReferent.getSelectionModel().getSelectedIndex();
					
					referentenComboBox();
					
					
					cbReferent.getSelectionModel().select(index);

					
					
					
					aufbau();
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
			
			// Nächster Monat darf nicht mehr drin im drin sein
			if (ld.plusDays(7).getMonth().getValue() != monat)
			{
				
				return;
			}
			// datenbankAbfrage mit Kurs
			g.add(new Label("Kurs"), 0, 2);
			Zeitplanung k =pruefenReferentZeitraum(ld);
			
			if (k != null)
			{
			
				g.add(new Label(k.getKur().getKursBezeichnung()), 1, 2);
				hg.put(g, k);
				
			}
			else
			{
				Label l = new Label("kein Kurs");
				l.setStyle("-fx-background-color : blue; -fx-text-fill: white;");
				g.add(l,1,2);
				hg.put(g, null);
			}
			
			ld = ld.plusDays(7);
			
			//g.setStyle("-fx-border-color: red; -fx-background-color: yellow");
			fp.getChildren().add(g);
		}
	}
	
	/**
	 * Aufbau und Verarbeitung der Detailsansicht für eine Woche
	 */
	private void aufbauWochenDetails(GridPane g, Referent r)
	{
		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/referentenplanung/Wochendetails.fxml"));
		try
		{
			Parent root = loader.load();
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
			Scene sce = new Scene(root);
			sce.getStylesheets().add(css);
			Stage stage = new Stage();
			stage.initOwner(root.getScene().getWindow());
			stage.setTitle("Wochendetails f�r Referent "+r.getVorname()+" "+r.getNachname());
			stage.setScene(sce);
			
			
			// Aufbau der Eintr�ge aus den Childrens f�r die Gridpane
			WochendetailsController ctl = loader.getController();
			
			ctl.aufbau(g.getChildren(),r);
		
			stage.show();
				
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	
	}
	
	/**
	 * Prüfung ob für den Referenten in dem jeweiligen Zeitraum ein Kurs geplant ist
	 * @param ld
	 * @return
	 */
	public Zeitplanung pruefenReferentZeitraum(LocalDate ld)
	{
		if (cbReferent.getSelectionModel().getSelectedItem() != null)
		{
			Referent r = cbReferent.getSelectionModel().getSelectedItem();
//			EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
//			EntityManager em = emf.createEntityManager();
//			Query query = em.createQuery("Select z from Zeitplanung z");
//			List<Zeitplanung> liZeit =  query.getResultList();
			for(Zeitplanung z : r.getZeitplanungs1())
			{
              LocalDate anfang = z.getAnfangsDatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1);
              LocalDate ende = z.getEndDatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
             
              int ar = anfang.plusDays(1).getDayOfWeek().getValue();
              
              // Problem, wenn der Anfang nicht der Montag ist,
              // auf den Montag zurückgehen und das anfang entsprechend setzen
              
              if (ar > 1)
              {
            	  int diff = ar-1;
            	  anfang = anfang.minusDays(diff);
              }
              
              if( ld.isAfter(anfang) && ld.isBefore(ende))
              {
            	  
            	  return z;
              }
			}
			
			
			
		}
		return null;
	}
	
	/**
	 * Füllen der Combobox für den Referenten
	 */
	public void referentenComboBox()
	{
		
		emf = Persistence.createEntityManagerFactory("Seminarplaner");
		em = emf.createEntityManager();
		
		Query query = em.createQuery("Select r from Referent r" );
		
		this.cbReferantData.clear();
		
		
		cbReferantData.addAll(query.getResultList());
		cbReferent.setItems(cbReferantData);
		
		
		
	}
	/**
	 * Drucken der Referentenplanung
	 * @param event
	 */
	public void btDrucken_Click(ActionEvent event)
	{
		Referent r = cbReferent.getSelectionModel().getSelectedItem();
		Query query = em.createQuery("Select z from Zeitplanung z where z.referent.referentenNr = "
	               + r.getReferentenNr());
	            		           
	    List<Zeitplanung> lizeitplanung =  query.getResultList();
		ReferentenplanungDrucken d = new ReferentenplanungDrucken(cbJahr.getSelectionModel().getSelectedItem());
		d.EinzelnerReferent(this.cbReferent.getSelectionModel().getSelectedItem(),
				lizeitplanung);
				
	}
	
	
	
	
	
	

}
