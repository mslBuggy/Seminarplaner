package zeitplanung;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.ToLongFunction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import drucken.TermineDrucken;
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
import model.Ausbildung_Kurs;
import model.Kurs;
import model.Zeitplanung;

/**
 * Zuständiger Controller für ZeitplanTabelle.fxml
 * @author Manuel Lara Bisch
 */
public class ZeitplanTabelleController {
	@FXML
	private TableView<Zeitplanung> tbZeitplanung;
	private ObservableList<Zeitplanung> tbZeitplanungData = FXCollections.observableArrayList();
	private List<Ausbildung> liAusbildung;
	@FXML
	private ComboBox<String> cbAusbildungsgang;
	private ObservableList<String> cbAusbildungsgangData = FXCollections.observableArrayList();
	@FXML
	private ComboBox<Integer> cbLehrjahr;
	private ObservableList<Integer> cbLehrjahrData = FXCollections.observableArrayList();
	@FXML
	private Button btAnzeigen;
	private ObservableList<Kurs> olKursData = FXCollections.observableArrayList();
	
	@FXML
	private TableColumn<Zeitplanung, String> tcKursBezeichnung;
	@FXML
	private TableColumn<Zeitplanung, String> tcRaumNr;
	@FXML
	private TableColumn<Zeitplanung, String> tcAnfangsDatum;
	@FXML
	private TableColumn<Zeitplanung, String> tcEndDatum;
	@FXML
	private TableColumn<Zeitplanung, String> tcTermintyp;
	@FXML
	private TableColumn<Zeitplanung, String> tcReferent;
	@FXML
	private TableColumn<Zeitplanung, String> tcBemerkung;

	@FXML
	private Button btNeu;
	@FXML
	private Button btDetails;
	@FXML
	private Button btEnde;
	
	@FXML
	private Button btDrucken;
	
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
	EntityManager em = emf.createEntityManager();
	
	/**
	 * Weist den Comboboxen und der Tabelle Anfangswerte zu. 
	 */
	public void initialize(){
		tbZeitplanung.setPlaceholder(new Label("Bitte erst Ausbildungsgang und Lehrjahr auswählen!"));
		cbAusbildungsgang.setItems(cbAusbildungsgangData);
		cbLehrjahrData.addAll(1,2,3);
		cbLehrjahr.setItems(cbLehrjahrData);
		
		tbZeitplanung.setItems(tbZeitplanungData);
		
		tcKursBezeichnung.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Zeitplanung,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Zeitplanung, String> param) {
				return new SimpleStringProperty(param.getValue().getKur().getKursBezeichnung() + "");
			}
		});
		tcRaumNr.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Zeitplanung,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Zeitplanung, String> param) {
				return new SimpleStringProperty(param.getValue().getRaum().getRaumNr());
			}
		});
		tcAnfangsDatum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Zeitplanung,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Zeitplanung, String> param) {
				// TODO Method created by lara-bisch_manuel on 26.04.2017 14:43:24
				return new SimpleStringProperty(formatDatum(param.getValue().getAnfangsDatum()));
			}
		});
		tcEndDatum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Zeitplanung,String>, ObservableValue<String>>() {

			@Override 
			public ObservableValue<String> call(CellDataFeatures<Zeitplanung, String> param) {
				// TODO Method created by lara-bisch_manuel on 26.04.2017 14:43:57
				return new SimpleStringProperty(formatDatum(param.getValue().getEndDatum()));
			}
		});
		// TODO: Termintyp an die neue Tabelle AusbildungsfestTermine anpassen
		tcTermintyp.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Zeitplanung,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Zeitplanung, String> param) {
				// TODO Method created by lara-bisch_manuel on 26.04.2017 15:01:41
				Zeitplanung z = param.getValue();
				int typ = z.getTermintyp();
				String s = "";
				if(typ == 1)
					s = "Kurstermin";
				if(typ == 2)
					s = "Berufsschule";
				if(typ == 3)
					s = "Betriebsurlaub";
				if(typ == 4)
					s = "Feiertag";
				return new SimpleStringProperty(s);
			}
		});
		tcReferent.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Zeitplanung,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Zeitplanung, String> param) {
				return new SimpleStringProperty(param.getValue().getReferent().getNachname() + ", "
						+ param.getValue().getReferent().getVorname());
			}
		});
		tcBemerkung.setCellValueFactory(new PropertyValueFactory<>("bemerkung"));
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
		tbZeitplanungData.clear();
		olKursData.clear();
		if(cbAusbildungsgang.getSelectionModel().getSelectedItem() != null){
			int ausbNr = liAusbildung.get(cbAusbildungsgang.getSelectionModel().getSelectedIndex()).getAusbildungsNr();
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
			EntityManager em = emf.createEntityManager();
			Query query = em.createQuery("select a from Ausbildung_Kurs a where a.ausbildung.ausbildungsNr = " + ausbNr);
			List<Ausbildung_Kurs> liAus2 = query.getResultList();
			for (Ausbildung_Kurs auskurs: liAus2) {
				System.out.println(auskurs.getKur().getKursNr());
				olKursData.add(auskurs.getKur());
			}
			List<Zeitplanung> liTermin;
			if(!olKursData.isEmpty()){
				for (Kurs kurs : olKursData) {
					query = em.createQuery("select z from Zeitplanung z where z.kur.kursNr = " +  kurs.getKursNr());
					liTermin = query.getResultList();
					try{
						for (Zeitplanung zeitplanung : liTermin) {
						if(kurs.getLehrjahr() == cbLehrjahr.getSelectionModel().getSelectedItem().intValue())
							tbZeitplanungData.add(zeitplanung);
						}
					} catch (NullPointerException e) {
						Alert a = new Alert(AlertType.WARNING);
						a.setHeaderText("");
						a.setContentText("Sie müssen ein Lehrjahr auswählen, um die Termine anzuzeigen!");
						a.setTitle("Bitte Lehrjahr auswählen");
						a.showAndWait();
				}
					
					
				}
			}
			tbContentChange();
		}
			System.out.println(cbAusbildungsgang.getSelectionModel().getSelectedIndex()
								+ "\t\t" + cbLehrjahr.getSelectionModel().getSelectedItem());
	}
	
	/*public void tbContentChange(Zeitplanung z){
		// TODO: Sortiere Termine in Tabelle nach Anfangsdatum
		// Comparator<Zeitplanung> comparator = Comparator.comparingLong(Zeitplanung.getAnfangsDatum().toInstant().toEpochMilli()); // won't work
		// tbZeitplanungData.sort(comparator);
		tbZeitplanungData.set(tbZeitplanung.getSelectionModel().getSelectedIndex(), z);
	}*/
	public void tbContentChange(){
			
		SortedList<Zeitplanung> tbZeitplanungDataSorted = tbZeitplanungData.sorted();
		Comparator<Zeitplanung> cTermin = new Comparator<Zeitplanung>() {

			@Override
			public int compare(Zeitplanung o1, Zeitplanung o2) {
				// TODO Method created by lara-bisch_manuel on 04.10.2017 16:35:17
				return (int) (o2.getAnfangsDatum().toInstant().toEpochMilli()
								- o1.getAnfangsDatum().toInstant().toEpochMilli());
			}
		};
		tbZeitplanungDataSorted.setComparator(cTermin);
		tbZeitplanung.setItems(tbZeitplanungDataSorted);
	}
	
	public void btDrucken_Click(ActionEvent event){
		String s = cbAusbildungsgang.getSelectionModel().getSelectedItem();
		s += ", Lehrjahr " + cbLehrjahr.getSelectionModel().getSelectedItem();
		TermineDrucken t = new TermineDrucken(tbZeitplanungData, s);
	}
	
	/**
	 * Diese Methode wird bei Klick auf den Button btNeu ausgeführt.
	 * Sie öffnet die TerminDetails-Maske mit leeren Eingabefeldern.
	 */
	public void btNeu_Click(ActionEvent event){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TerminDetails.fxml"));
		try {
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			stage.setTitle("Neuer Termin");
			Scene sce = new Scene(root,382,422);
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
			//ctl.setZeitplanung(new Zeitplanung(), (Ausbildung) em.createQuery("select Ausbildung ").getSingleResult());
			//ctl.setZeitplanung(new Zeitplanung());
			stage.showAndWait();
			tbZeitplanungData.clear();
			this.lesen();
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * Diese Methode wird beim Klick auf btDetails ausgeführt.
	 * Sie öffnet die TerminDetails-Maske, sofern zuvor ein Termin in tbZeitplanung ausgewählt wurde.
	 * Ansonsten wird eine Fehlermeldung angezeigt.
	 * Nachdem das TerminDetails-Fenster geschlossen wurde, werden die Werte in tbZeitplanung 
	 * aus der Datenbank neu eingelesen, um Änderungen zu reflektieren.
	 */
	public void btDetails_Click(ActionEvent event){
		if(tbZeitplanung.getSelectionModel().getSelectedIndex() < 0){
	    	Alert a = new Alert(AlertType.ERROR);
	    	a.setHeaderText("");
	    	a.setContentText("Bitte wählen Sie erst einen Termin aus!");
	    	a.show();
	    	return;
    	}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TerminDetails.fxml"));
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
			// Festlegen des Owners
			Node node = (Node) event.getSource();
			Scene sc = node.getScene();
			Window w = sc.getWindow();
		    stage.initOwner(w);
			// Zur Übergabe benötigen wir hier den Controller der abhängigen Maske
			// Beschaffung: deswegen haben wir den FXMLLoader instanziert
			TerminDetailsController ctl = loader.getController();
			//ctl.setZeitplanung(tbZeitplanung.getSelectionModel().getSelectedItem());
			stage.showAndWait();
			tbZeitplanungData.clear();
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
	
	/*private Zeitplanung dummyZeitplanung(int terminNr,
										Kurs kurs,
										Raum raum,
										Date anfangsDatum,
										Date endDatum,
										int terminTyp,
										String aendBenutzer,
										Date aendDatum,
										String bemerkung){
		Zeitplanung z = new Zeitplanung();
		z.setTerminNr(terminNr);
		z.setKur(kurs);
		z.setRaum(raum);
		z.setAnfangsDatum(anfangsDatum);
		z.setEndDatum(endDatum);
		z.setTermintyp(terminTyp);
		z.setAendBenutzer(aendBenutzer);
		z.setAendDatum(aendDatum);
		z.setBemerkung(bemerkung);
		return z;
	}*/
	
	/**
	 * Diese Methode liest Werte für cbAusbildung aus der Datenbank ein
	 */
	@SuppressWarnings("unchecked")
	public void lesen(){
		Query query;
		/*query = em.createQuery("select z from Zeitplanung z");
		List<Zeitplanung> liZeitplanung = query.getResultList();
		for (Zeitplanung zeitplanung : liZeitplanung) {
			tbZeitplanungData.add(zeitplanung);
		}*/
		query = em.createQuery("select a from Ausbildung a");
		liAusbildung = query.getResultList();
		for(Ausbildung ausbildung: liAusbildung) {
			cbAusbildungsgangData.add(ausbildung.getAusbildungsBezeichnung());
		}
		
		/*List<Kurs> liKurs = query.getResultList();
		for(Kurs kurs: liKurs){
			System.out.println(kurs.getKursNr());*/
		
	}

}
