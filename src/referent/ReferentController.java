package referent;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import model.Referent;

/** ReferentController beinhaltet die Verbindung zu Referent.fxml in
 * der die grafische Oberfläche der Referenten eingebunden ist.
 * Des weiteren besitzt der Controller Methoden zum Aufruf der Bearbeitung
 * der Referenten und Neuanlegung eines Referenten (ToDo 1.0)
 */

public class ReferentController {

	private Referent ref;
	
	@FXML
	private TableView<Referent> tabReferent;
	private ObservableList<Referent> tabReferentData = FXCollections.observableArrayList();

	@FXML
	private TableColumn<Referent, String> tabcReferentNr;

	@FXML
	private TableColumn<Referent, String> tabcVorname;

	@FXML
	private TableColumn<Referent, String> tabcNachname;

	@FXML
	private TableColumn<Referent, String> tabcFestnetz;

	@FXML
	private TableColumn<Referent, String> tabcMobil;

	@FXML
	private TableColumn<Referent, String> tabcEmail;

	@FXML
	private TableColumn<Referent, String> tabcAktiv;

	@FXML
	private TableColumn<Referent, String> tabcBemerkung;
	
	@FXML
	private Button btDetails;
	
	@FXML
	private Button ende;
	
	/** Methode zum Aufruf der Referentenklasse aus dem Package "model"
	 * 
	 */
		public Referent getReferent(){
			return ref;
		}
		
	/** Methode für Speicherung der Daten vom Server 
	 * in einer Observable-arraylist
	 */
	public void initialize() {

		tabReferent.setItems(tabReferentData);
		tabcReferentNr.setCellValueFactory(new PropertyValueFactory<>("referentenNr"));
		tabcVorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
		tabcNachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
		tabcFestnetz.setCellValueFactory(new PropertyValueFactory<>("telfestNetz"));
		tabcMobil.setCellValueFactory(new PropertyValueFactory<>("telMobil"));
		tabcEmail.setCellValueFactory(new PropertyValueFactory<>("eMail"));
		tabcAktiv.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer,ObservableValue<Boolean>>() {

			@Override
			public ObservableValue<Boolean> call(Integer param) {
				// TODO Auto-generated method stub
			
				return new SimpleBooleanProperty( tabReferentData.get(param).getAktiv());
				
			}

			

			
		}));
		tabcBemerkung.setCellValueFactory(new PropertyValueFactory<>("bemerkung"));
		
		tabReferent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Referent>() {

			@Override
			public void changed(ObservableValue<? extends Referent> observable, Referent oldValue, Referent newValue) {
				
				Referent rf = newValue;
				ref = rf;
				
			}
		});
		
		}
	
	
	/** Methode zum lesen der Daten aus dem SQL-Server und der Speicherung
	 * dieser in einer Observable-arraylist
	 */
	@SuppressWarnings(value = { "unchecked" })
	public void lesen(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("select r from Referent r");
		List<Referent> liReferent = query.getResultList();
		for(Referent referent : liReferent) {
			tabReferentData.add(referent);
		}
		
	}
	
	/**Methode zur Bearbeitung eines Referenten und zur Überprüfung ob ein Referent ausgewählt wurde
	 * 
	 */
	public void btDetails_Click(ActionEvent event){
		if(tabReferent.getSelectionModel().getSelectedIndex() < 0){
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("");
			a.setContentText("Bitte wählen Sie erst einen Referenten aus!");
			a.show();
			return;
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ReferentDetails.fxml"));
		try {
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			
			stage.setTitle("Referentdetails");
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
			ReferentDetailsController ctl = loader.getController();
			
			ctl.setReferent(tabReferent.getSelectionModel().getSelectedItem());
			stage.showAndWait();
			tabReferentData.clear();
			this.lesen();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}	
	
	/** Methode zum schließen des Fensters
	 * 
	 */
	public void ende_Click(ActionEvent event){
		Stage stage = (Stage) ende.getScene().getWindow();
		stage.close();
	}
	
	/** ToDo 1.0 Methode zur Neuanlegung eines Referenten erstellen
	 * 
	 */

}
