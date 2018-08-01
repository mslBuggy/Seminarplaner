package kurs;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import drucken.KurseDrucken;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import model.Ausbildung;
import model.Ausbildung_Kurs;
import model.Kurs;

/**
 * Zuständiger Controller für KursTabelle.fxml
 * @author Manuel Lara Bisch
 */
public class KursTabelleController {
	private Kurs kurs;
	public Kurs getKurs(){
		return kurs;
	}
	
	@FXML
	private TableView<Kurs> tbKurs;
	private ObservableList<Kurs> tbKursData = FXCollections.observableArrayList();
	
	@FXML
	private TableColumn<Kurs, String> tcKursNr;
	@FXML
	private TableColumn<Kurs, String> tcKursBezeichnung;
	@FXML
	private TableColumn<Kurs, String> tcKursDauerTage;
	@FXML
	private TableColumn<Kurs, String> tcBrauchtEDV;
	@FXML
	private TableColumn<Kurs, String> tcLehrjahr;
	@FXML
	private TableColumn<Kurs, String> tcAusbildung;
	@FXML
	private TableColumn<Kurs, String> tcBemerkung;
	
	@FXML
	private Button btDetails;
	@FXML
	private Button btNeu;
	
	@FXML
	private Button btDrucken;
	
	@FXML
	private Button btEnde;
	
	/*public KursTabelleController() {
		// TODO Auto-generated constructor stub
		Kurs k1 = new Kurs();
		k1.setKursNr(1);
		k1.setKursBezeichnung("Datenbanken");
		k1.setKursDauerTage(50);
		k1.setBrauchtEDV(true);
		k1.setLehrjahr(2);
		k1.setAendBenutzer("root");
		k1.setAendDatum(new Date());
		tbKursData.add(k1);
		
		
	}*/
	/**
	 * Weist der Tabelle und den Spalten Werte zu.
	 * btEnde bekommt einen EventHandler, um das aktuelle Fenster zu schließen.
	 */
	public void initialize() {
		tbKurs.setItems(tbKursData);
		tcKursNr.setCellValueFactory(new PropertyValueFactory<>("kursNr"));
		tcKursBezeichnung.setCellValueFactory(new PropertyValueFactory<>("kursBezeichnung"));
		tcKursDauerTage.setCellValueFactory(new PropertyValueFactory<>("kursDauerTage"));
		tcBrauchtEDV.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer,ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(Integer param) {
				// TODO Auto-generated method stub
				// System.out.println(tbKursData.get(param).getBrauchtEDV());
				return new SimpleBooleanProperty(tbKursData.get(param).getBrauchtEDV());
			}
		}));
		tcLehrjahr.setCellValueFactory(new PropertyValueFactory<>("lehrjahr"));
		tcAusbildung.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kurs,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Kurs, String> param) {
				// TODO Method created by lara-bisch_manuel on 30.01.2018 14:39:32
				Kurs k = param.getValue();
				String s = "";
				for (Ausbildung_Kurs a : k.getAusbildungKurs()) {
					if (!s.equals(""))
						s += "\n";
					s+= a.getAusbildung().getAusbildungsBezeichnung();
				}
				return new SimpleStringProperty(s);
			}
		});
		tcBemerkung.setCellValueFactory(new PropertyValueFactory<>("bemerkung"));
		btEnde.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// Schließt das Fenster
				((Stage) (btEnde.getScene().getWindow())).close();
			}
		});
		tbKurs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Kurs>() {

			@Override
			public void changed(ObservableValue<? extends Kurs> observable, Kurs oldValue, Kurs newValue) {
				// TODO Method created by lara-bisch_manuel on 11.12.2017 15:51:35
				kurs = newValue;
			}
		});
	}
	
	/**
	 * Methode, die bei Klick auf den btDetails Knopf ausgeführt wird.
	 * Überprüft, ob ein Kurs in der Tabelle ausgewählt wurde, und ruft die KursDetails-Maske mit den Werten dieses Kurses auf.
	 * Falls kein Kurs ausgewählt war, wird eine Fehlermeldung angezeigt.
	 */
	public void btDetails_Click(ActionEvent event){
		if(tbKurs.getSelectionModel().getSelectedIndex() < 0){
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("");
			a.setContentText("Bitte wählen Sie erst einen Kurs aus!");
			a.show();
			return;
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("KursDetails.fxml"));
		try {
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			
			stage.setTitle("Kursdetails");
			Scene sce = new Scene(root,400,600);
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
			KursDetailsController ctl = loader.getController();
			
			ctl.setKurs(tbKurs.getSelectionModel().getSelectedItem());
			stage.showAndWait();
			tbKursData.clear();
			this.lesen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode, die bei Klick auf den btNeu Knopf ausgeführt wird.
	 * Ruft ähnlich wie btDetails_Click die Maske für Details auf, diese bekommt aber keine Kurswerte übergeben
	 * (der Benutzer soll diese eingeben)
	 */
	public void btNeu_Click(ActionEvent event){
		// DONE: leere Details-Maske aufrufen
		FXMLLoader loader = new FXMLLoader(getClass().getResource("KursDetails.fxml"));
		try {
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Neuen Kurs erzeugen");
			Scene sce = new Scene(root,400,600);
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
		    KursDetailsController ctl = loader.getController();
		    ctl.setKurs(new Kurs());
		    ctl.setNeu(true);
			stage.showAndWait();
			tbKursData.clear();
			this.lesen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Drucken der Kurstabelle
	 * @param event
	 */
	public void btDrucken_Click(ActionEvent event){
		KurseDrucken k = new KurseDrucken(tbKursData);
	}
	
	

	/**
	 * Liest alle Datensätze in der Tabelle Kurs ein
	 */
	@SuppressWarnings("unchecked")
	public void lesen(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("select k from Kurs k");
		List<Kurs> liKurs = query.getResultList();
		for (Kurs kurs : liKurs) {
			tbKursData.add(kurs);
		}
	}

}
