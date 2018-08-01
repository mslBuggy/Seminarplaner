package zeitplanung;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import drucken.FestTermineDrucken;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import model.Ausbildung;
import model.AusbildungsfestTermine;
import model.Kurs;

/**
 * Zuständiger Controller für ZeitplanTabelle.fxml
 * @author Manuel Lara Bisch
 */
public class FestTerminTabelleController {
	@FXML
	private TableView<AusbildungsfestTermine> tbFestTermin;
	private ObservableList<AusbildungsfestTermine> tbFestTerminData = FXCollections.observableArrayList();
	private List<Ausbildung> liAusbildung;
	@FXML
	private ComboBox<String> cbAusbildungsgang;
	private ObservableList<String> cbAusbildungsgangData = FXCollections.observableArrayList();
	@FXML
	private ComboBox<Integer> cbLehrjahr;
	private ObservableList<Integer> cbLehrjahrData = FXCollections.observableArrayList();
	@FXML
	private ComboBox<String> cbTerminArt;
	private ObservableList<String> cbTerminArtData = FXCollections.observableArrayList();
	@FXML
	private Button btAnzeigen;
	private ObservableList<Kurs> olKursData = FXCollections.observableArrayList();
	
	@FXML
	private TableColumn<AusbildungsfestTermine, String> tcArtTermin;
	@FXML
	private TableColumn<AusbildungsfestTermine, String> tcLehrjahr;
	@FXML
	private TableColumn<AusbildungsfestTermine, String> tcAnfangsDatum;
	@FXML
	private TableColumn<AusbildungsfestTermine, String> tcEndDatum;
	@FXML
	private TableColumn<AusbildungsfestTermine, String> tcAusbildung;
	@FXML
	private TableColumn<AusbildungsfestTermine, String> tcJahr;
	@FXML
	private TableColumn<AusbildungsfestTermine, String> tcBezeichnung;

	@FXML
	private Button btNeu;
	@FXML
	private Button btDetails;
	@FXML
	private Button btEnde;
	
	@FXML
	private Button btDrucken;
	
	/**
	 * Weist den Comboboxen und der Tabelle Anfangswerte zu. 
	 */
	public void initialize(){
		tbFestTermin.setPlaceholder(new Label("Bitte erst Ausbildungsgang und Lehrjahr auswählen!"));
		cbAusbildungsgang.setItems(cbAusbildungsgangData);
		cbLehrjahrData.addAll(1,2,3);
		cbLehrjahr.setItems(cbLehrjahrData);
		cbTerminArtData.addAll("Berufsschule","Betriebsurlaub","Feiertag", "Sonstiger Termin");
		cbTerminArt.setItems(cbTerminArtData);
		tbFestTermin.setItems(tbFestTerminData);
		
		tcArtTermin.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AusbildungsfestTermine,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<AusbildungsfestTermine, String> param) {
				AusbildungsfestTermine z = param.getValue();
				int typ = z.getArttermin() + 1;
				String s = "";
				if(typ == 2)
					s = "Berufsschule";
				if(typ == 3)
					s = "Betriebsurlaub";
				if(typ == 4)
					s = "Feiertag";
				if(typ == 5)
					s = "Sonstiger Termin";
				return new SimpleStringProperty(s);
				
			}
		});
		tcLehrjahr.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AusbildungsfestTermine,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<AusbildungsfestTermine, String> param) {
				return new SimpleStringProperty(param.getValue().getLehrjahr() + "");
			}
		});
		tcAnfangsDatum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AusbildungsfestTermine,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<AusbildungsfestTermine, String> param) {
				// TODO Method created by lara-bisch_manuel on 26.04.2017 14:43:24
				return new SimpleStringProperty(formatDatum(param.getValue().getDatumvon()));
			}
		});
		tcEndDatum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AusbildungsfestTermine,String>, ObservableValue<String>>() {

			@Override 
			public ObservableValue<String> call(CellDataFeatures<AusbildungsfestTermine, String> param) {
				// TODO Method created by lara-bisch_manuel on 26.04.2017 14:43:57
				return new SimpleStringProperty(formatDatum(param.getValue().getDatumbis()));
			}
		});
		
		tcAusbildung.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AusbildungsfestTermine,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<AusbildungsfestTermine, String> param) {
				// TODO Method created by lara-bisch_manuel on 26.04.2017 15:01:417
				if (param.getValue().getAusbildung() != null)
				 return new SimpleStringProperty(param.getValue().getAusbildung().getAusbildungsBezeichnung());
				else
					return new SimpleStringProperty("");
			}
		});
		tcJahr.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AusbildungsfestTermine,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<AusbildungsfestTermine, String> param) {
				return new SimpleStringProperty(param.getValue().getJahr() + "");
			}
		});
		tcBezeichnung.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));
		btEnde.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Method created by lara-bisch_manuel on 26.04.2017 15:00:30
				((Stage) (btEnde.getScene().getWindow())).close();
			}
		});
	
	}
	/**
	 * Diese Methode wird bei Klick auf btAnzeigen ausgeführt.
	 * Die Tabelle aller Termine wird nach dem ausgewählten Ausbildungsgang und Lehrjahr gefiltert.
	 */
	@SuppressWarnings("unchecked")
	public void btAnzeigen_Click(ActionEvent event){
		tbFestTerminData.clear();
		olKursData.clear();
		if(cbAusbildungsgang.getSelectionModel().getSelectedItem() != null){
			int ausbNr = liAusbildung.get(cbAusbildungsgang.getSelectionModel().getSelectedIndex()).getAusbildungsNr();
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
			EntityManager em = emf.createEntityManager();
			String queryS = "";
			if(cbTerminArt.getSelectionModel().getSelectedItem() != null)
				queryS = "aft.arttermin = " + (cbTerminArt.getSelectionModel().getSelectedIndex() + 1) + " and ";
			Query query = em.createQuery("select aft from AusbildungsfestTermine aft where " + queryS + "(aft.ausbildung.ausbildungsNr = " + ausbNr + " or aft.ausbildung IS NULL)");
			List<AusbildungsfestTermine> liTermin = query.getResultList();
			try {
				for (AusbildungsfestTermine aft : liTermin) {
					if(aft.getLehrjahr() == cbLehrjahr.getSelectionModel().getSelectedItem().intValue() || aft.getLehrjahr() == 0)
						tbFestTerminData.add(aft);
				}
			} catch (NullPointerException e) {
				Alert a = new Alert(AlertType.WARNING);
				a.setHeaderText("");
				a.setContentText("Sie müssen ein Lehrjahr auswählen, um die Termine anzuzeigen!");
				a.setTitle("Bitte Lehrjahr auswählen");
				a.showAndWait();
			}
			
			
			tbFestTerminData.sort(new Comparator<AusbildungsfestTermine>() {

				@Override
				public int compare(AusbildungsfestTermine o1, AusbildungsfestTermine o2) {
					return o1.getDatumvon().compareTo(o2.getDatumvon());
				}
			});
		}
	}

	

	/**
	 * Die Methode, die ausgeführt wird, wenn der Button btDrucken gedrückt wird.
	 * Erstellt mit Hilfe der Klasse FestTermineDrucken ein PDF-Dokument mit den Inhalten von
	 * tbFestTerminData und zeigt dieses im System-Standardprogramm zum Öffnen von PDF-Dateien an.
	 * @param event
	 */
	public void btDrucken_Click(ActionEvent event){
		String s = "Externe Termine " + cbAusbildungsgang.getSelectionModel().getSelectedItem();
		s += ", Lehrjahr " + cbLehrjahr.getSelectionModel().getSelectedItem();
		@SuppressWarnings("unused")
		FestTermineDrucken t = new FestTermineDrucken(tbFestTerminData, s);
	}
	
	/**
	 * Diese Methode wird bei Klick auf den Button btNeu ausgeführt.
	 * Sie öffnet die TerminDetails-Maske mit leeren Eingabefeldern.
	 */
	public void btNeu_Click(ActionEvent event){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FestTerminDetails.fxml"));
		try {
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			stage.setTitle("Neuer Termin");
			Scene sce = new Scene(root,382,500);
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
			sce.getStylesheets().add(css);
			stage.setScene(sce);
			stage.initModality(Modality.WINDOW_MODAL);
			// Festlegen des Owners
			Node node = (Node) event.getSource();
			Scene sc = node.getScene();
			Window w = sc.getWindow();
		    stage.initOwner(w);
			// Zur Übergabe benötigen wir hier den Controller der abhängigen Maske
			// Beschaffung: deswegen haben wir den FXMLLoader instanziert
			//TerminDetailsController ctl = loader.getController();
			//ctl.setZeitplanung(new Zeitplanung());
			stage.showAndWait();
			tbFestTerminData.clear();
			this.lesen();
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * Diese Methode wird beim Klick auf btDetails ausgeführt.
	 * Sie öffnet die FestTerminDetails-Maske, sofern zuvor ein Termin in tbFestTermin
	 * ausgewählt wurde.
	 * Ansonsten wird eine Fehlermeldung angezeigt.
	 * Nachdem das TerminDetails-Fenster geschlossen wurde, werden die Werte in tbFestTermin 
	 * aus der Datenbank neu eingelesen, um Änderungen zu reflektieren.
	 */
	public void btDetails_Click(ActionEvent event){
		if(tbFestTermin.getSelectionModel().getSelectedIndex() < 0){
	    	Alert a = new Alert(AlertType.ERROR);
	    	a.setHeaderText("");
	    	a.setContentText("Bitte wählen Sie erst einen Termin aus!");
	    	a.show();
	    	return;
    	}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FestTerminDetails.fxml"));
		try {
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			stage.setTitle("Termindetails");
			Scene sce = new Scene(root,382,500);
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
			sce.getStylesheets().add(css);
			stage.setScene(sce);
			stage.initModality(Modality.WINDOW_MODAL);
			// Festlegen des Owners
			Node node = (Node) event.getSource();
			Scene sc = node.getScene();
			Window w = sc.getWindow();
		    stage.initOwner(w);
			// Zur Übergabe benötigen wir hier den Controller der abhängigen Maske
			// Beschaffung: deswegen haben wir den FXMLLoader instanziert
			FestTerminDetailsController ctl = loader.getController();
			ctl.setAusbildungsfestTermine(tbFestTermin.getSelectionModel().getSelectedItem());
			stage.showAndWait();
			tbFestTerminData.clear();
			this.lesen();
			
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * Formatiert ein Datum in das in Deutschland übliche Format, und gibt dies als String zurück
	 * @param d
	 * (ein java.util.Date Objekt, das aus der Datenbank eingelesen wurde)
	 * @return
	 * String: formatiertes Datum (TT.MM.JJJJ)
	 */
	private String formatDatum(Date d){
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(d);
	}
	
	/**
	 * Diese Methode liest Werte für cbAusbildung aus der Datenbank ein
	 */
	@SuppressWarnings("unchecked")
	public void lesen(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		Query query;
		
		query = em.createQuery("select a from Ausbildung a");
		liAusbildung = query.getResultList();
		cbAusbildungsgangData.clear();
		for(Ausbildung ausbildung: liAusbildung) {
			cbAusbildungsgangData.add(ausbildung.getAusbildungsBezeichnung());
		}

	}

}
