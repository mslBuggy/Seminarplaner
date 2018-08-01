package raumplanung;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Raum;

/** RaumController dient zur Einbindung der Raum.fxml
 * und beinhaltet eine Methode zur Bearbeitung der Räume
 */
public class RaumController {

	private Raum raum;
	
	@FXML
	private TableView<Raum> tabRaum;
	private ObservableList<Raum> tabRaumData = FXCollections.observableArrayList();
	
	@FXML
	private TableColumn<Raum, String> tabcRaumNr;
	
	@FXML
	private TableColumn<Raum, String> tabcKapazitaet;
	
	@FXML
	private TableColumn<Raum, String> tabchatEDV;
	
	@FXML
	private TableColumn<Raum, String> tabcBemerkung;
	
	@FXML
	private Button ende;
	
	@FXML
	private Button Details;
	
	@FXML
	private Button neu;
	
	public Raum getRaum(){
		return raum;
	}
	
	/**Methode zur Speicherung der Daten in einer Observable-arraylist
	 */
	public void initialize(){
		
		tabRaum.setItems(tabRaumData);
		tabcKapazitaet.setCellValueFactory(new PropertyValueFactory<>("kapazitaet"));
		tabchatEDV.setCellValueFactory(new PropertyValueFactory<>("hatEDV"));
		tabcBemerkung.setCellValueFactory(new PropertyValueFactory<>("bemerkung"));
		tabcRaumNr.setCellValueFactory(new PropertyValueFactory<>("raumNr"));
		tabRaum.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Raum>() {

			@Override
			public void changed(ObservableValue<? extends Raum> observable, Raum oldValue, Raum newValue) {
				Raum r = newValue;
				raum = r;
			}
		});
		
	}
	
	/** Methode zum lesen der Daten aus Raum
	 * 
	 */
	@SuppressWarnings(value = { "unchecked" })
	public void lesen(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("select r from Raum r");
		List<Raum> liRaum = query.getResultList();
		for(Raum raum : liRaum) {
			tabRaumData.add(raum);
		}
		
	}
	
	public void Neu_Click(ActionEvent event){
		// leere Details-Maske aufrufen
		FXMLLoader loader = new FXMLLoader(getClass().getResource("RaumDetails.fxml"));
		try {
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Neuen Raum erzeugen");
			Scene sce = new Scene(root,400,600);
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets m�ssen als URL umgewandelt werden, daher toExternalForm()
			sce.getStylesheets().add(css);
			stage.setScene(sce);
			stage.initModality(Modality.WINDOW_MODAL);
			// Festlegen des Owners
			Node node = (Node) event.getSource();
			Scene sc = node.getScene();
			Window w = sc.getWindow();
		    stage.initOwner(w);
		    RaumDetailsController ctl = loader.getController();

		    ctl.setNeu(true);
			stage.showAndWait();
			tabRaumData.clear();
			this.lesen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Details_Click(ActionEvent event){
		if(tabRaum.getSelectionModel().getSelectedIndex() < 0){
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("");
			a.setContentText("Bitte w�hlen Sie erst einen Raum aus!");
			a.show();
			return;
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("RaumDetails.fxml"));
		try {
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			
			stage.setTitle("Raumdetails");
			Scene scene = new Scene(root,400,600);
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets m�ssen als URL umgewandelt werden, daher toExternalForm()
			scene.getStylesheets().add(css);
			stage.setScene(scene);
			stage.initModality(Modality.WINDOW_MODAL);
			// Festlegen des Owners
			Node node = (Node) event.getSource();
			Scene sc = node.getScene();
			Window w = sc.getWindow();
		    stage.initOwner(w);
			RaumDetailsController ctl = loader.getController();
			
			ctl.setRaum(tabRaum.getSelectionModel().getSelectedItem());
			stage.showAndWait();
			tabRaumData.clear();
			this.lesen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	/** Methode zum Beenden
	*/
	public void ende_Click(ActionEvent event){
		Stage stage = (Stage) ende.getScene().getWindow();
		stage.close();
	}
	
}

