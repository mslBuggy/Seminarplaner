package ausbildung;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import model.Ausbildung;
import referent.ReferentDetailsController;

/** AusbildungController dient zur Einbindung der Ausbildung.fxml
 * und beinhaltet eine Methode zur Bearbeitung der Räume
 */
public class AusbildungController {

	private Ausbildung a;
	
	@FXML
	private TableView<Ausbildung> tabAusbildung;
	private ObservableList<Ausbildung> tabAusbildungData = FXCollections.observableArrayList();
	
	@FXML
	private TableColumn<Ausbildung, String> tabcAusbildungNr;
		
	@FXML
	private TableColumn<Ausbildung, String> tabcBezeichnung;

	@FXML
	private TableColumn<Ausbildung, String> tabcAnfang;
	
	@FXML
	private TableColumn<Ausbildung, String> tabcEnde;
	
	@FXML
	private TableColumn<Ausbildung, String> tabcSeminarleiter;
	
	@FXML
	private TableColumn<Ausbildung, String> tabcAbschlussart;
	
	@FXML
	private TableColumn<Ausbildung, String> tabcBemerkung;
	
	@FXML
	private Button ende;
	
	@FXML
	private Button Details;
	
	@FXML
	private Button neu;
	
	public Ausbildung getAusbildung(){
		return a;
	}
	
	
	/**Methode zur Speicherung der Daten in einer Observable-arraylist
	 */
	public void initialize(){
		
		tabAusbildung.setItems(tabAusbildungData);
		tabcSeminarleiter.setCellValueFactory(new PropertyValueFactory<>("seminarleiterNr"));
		tabcBemerkung.setCellValueFactory(new PropertyValueFactory<>("bemerkung"));
		tabcAusbildungNr.setCellValueFactory(new PropertyValueFactory<>("AusbildungsNr"));
		tabcBezeichnung.setCellValueFactory(new PropertyValueFactory<>("AusbildungsBezeichnung"));
		tabcAbschlussart.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ausbildung,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Ausbildung, String> param) {
				Ausbildung a = param.getValue();
				int art = a.getAbschlussart();
				String s = "";
				if(art == 1){
					s += "IHK";
				}
				return new SimpleStringProperty(s);
			}
		
		});
		tabcAnfang.setCellValueFactory(new PropertyValueFactory<>("AnfangsDatum"));
		tabcEnde.setCellValueFactory(new PropertyValueFactory<>("EndDatum"));
		tabcSeminarleiter.setCellValueFactory(new PropertyValueFactory<>("Seminarleiter"));
		tabcBemerkung.setCellValueFactory(new PropertyValueFactory<>("Bemerkung"));
		tabAusbildung.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ausbildung>() {

			@Override
			public void changed(ObservableValue<? extends Ausbildung> observable, Ausbildung oldValue, Ausbildung newValue) {
				Ausbildung ausbildung = newValue;
				ausbildung = a;
			}
		});
		
	}
	/** Methode zum lesen der Daten aus Ausbildung
	 * 
	 */
	@SuppressWarnings(value = { "unchecked" })
	public void lesen(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("select a from Ausbildung a");
		List<Ausbildung> liAusbildung = query.getResultList();
		for(Ausbildung Ausbildung : liAusbildung) {
			tabAusbildungData.add(Ausbildung);
		}
		
	}
	
	public void btNeu_Click(ActionEvent event){
		// leere Details-Maske aufrufen
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AusbildungDetails.fxml"));
		try {
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Neue Ausbildung erzeugen");
			Scene sce = new Scene(root,400,600);
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets m�ssen als URL umgewandelt werden, daher toExternalForm()
			sce.getStylesheets().add(css);
			stage.setScene(sce);
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			// Festlegen des Owners
			Node node = (Node) event.getSource();
			Scene sc = node.getScene();
			Window w = sc.getWindow();
		    stage.initOwner(w);
		    AusbildungDetailsController ctl = loader.getController();

		    ctl.setNeu(true);
			stage.showAndWait();
			tabAusbildungData.clear();
			this.lesen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void btDetails_Click(ActionEvent event){
		if(tabAusbildung.getSelectionModel().getSelectedIndex() < 0)
		{
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("");
			a.setContentText("Bitte wählen Sie erst eine Ausbildung aus!");
			a.show();
			return;
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AusbildungDetails.fxml"));
		try 
		{
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			stage.setTitle("Ausbildungdetails");
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
			AusbildungDetailsController ctl = loader.getController();			
			ctl.setAusbildung(tabAusbildung.getSelectionModel().getSelectedItem());
			stage.showAndWait();
			tabAusbildungData.clear();
			this.lesen();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}		
	/** Methode zum Beenden
	*/
	public void btEnde_Click(ActionEvent event){
		Stage stage = (Stage) ende.getScene().getWindow();
		stage.close();
	}
	
}

