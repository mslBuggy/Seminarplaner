package seminarleiter;

/** Imports  /seminarplaner/src/ **/
import model.Seminarleiter;

/** Imports  /java/ **/
import java.io.IOException;
import java.util.List;

/** Imports /javax/ **/
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/** Imports /javafx/ **/
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/** ReferentController beinhaltet die Verbindung zu Referent.fxml in
 * der die grafische Oberfläche der Referenten eingebunden ist.
 * Des weiteren besitzt der Controller Methoden zum Aufruf der Bearbeitung
 * der Referenten und Neuanlegung eines Referenten (ToDo 1.0)
 */

public class SeminarleiterController 
{
	private Seminarleiter s;
	
	@FXML
	private BorderPane BP;
	
	@FXML
	private Pane pane;
	
	@FXML
	private Pane viewpane;
	
	@FXML
	private TableView<Seminarleiter> tabSeminarleiter;
	private ObservableList<Seminarleiter> tabSeminarleiterData = FXCollections.observableArrayList();

	@FXML
	private TableColumn<Seminarleiter, String> tabcSeminarleiterNr;

	@FXML
	private TableColumn<Seminarleiter, String> tabcVorname;

	@FXML
	private TableColumn<Seminarleiter, String> tabcNachname;

	@FXML
	private TableColumn<Seminarleiter, String> tabcEmail;
	
	@FXML
	private TableColumn<Seminarleiter, String> tabcGeschlecht;
	
	@FXML
	private TableColumn<Seminarleiter, String>tabcPasswort;
	
	@FXML
	private TableColumn<Seminarleiter, String>tabcTelNr;

	@FXML
	private TableColumn<Seminarleiter, String> tabcBemerkung;
	
	@FXML
	private Button btNeu;
	
	@FXML
	private Button btDetails;
	
	@FXML
	private Button ende;
	
	/** Methode zum Aufruf der Seminarleiterklasse aus dem Package "model"
	 * 
	 */		
	public Seminarleiter getSeminarleiter()
		{
			return s;
		}
		
	/** Methode für Speicherung der Daten vom Server 
	 * in einer Observable-arraylist
	 */
	public void initialize() 
	{
		tabSeminarleiter.setItems(tabSeminarleiterData);
		tabcSeminarleiterNr.setCellValueFactory(new PropertyValueFactory<>("seminarleiterNr"));
		tabcVorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
		tabcNachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
		tabcGeschlecht.setCellValueFactory(new PropertyValueFactory<>("geschlecht"));
		tabcEmail.setCellValueFactory(new PropertyValueFactory<>("eMail"));
		tabcBemerkung.setCellValueFactory(new PropertyValueFactory<>("bemerkung"));
		tabSeminarleiter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Seminarleiter>() 
			{
			@Override
			public void changed(ObservableValue<? extends Seminarleiter> observable, Seminarleiter oldValue, Seminarleiter newValue) 
			{	
				Seminarleiter s = newValue;
				s = s;
			}
		});
	}
	/** Methode zum lesen der Daten aus dem SQL-Server und der Speicherung
	 * dieser in einer Observable-arraylist
	 */
		public void lesen()
		{
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
			EntityManager em = emf.createEntityManager();
			Query query = em.createQuery("select s from Seminarleiter s");
			List<Seminarleiter> liSeminarleiter = query.getResultList();
			for(Seminarleiter seminarleiter : liSeminarleiter) 
			{
				tabSeminarleiterData.add(seminarleiter);
			}
			
		}
	
	
	/**Methode zur Bearbeitung eines Seminarleiters und zur Überprüfung ob ein Seminarleiter ausgewählt wurde
	 * 
	 */
	public void btDetails_Click(ActionEvent event)
	{
		if(tabSeminarleiter.getSelectionModel().getSelectedIndex() < 0){
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("");
			a.setContentText("Bitte wählen Sie erst einen Seminarleiter aus!");
			a.show();
			return;
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SeminarleiterDetails.fxml"));
		try 
		{
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			
			stage.setTitle("Seminarleiterdetails");
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
			SeminarleiterDetailsController ctl = loader.getController();
			
			ctl.setSeminarleiter(tabSeminarleiter.getSelectionModel().getSelectedItem());
			stage.showAndWait();
			tabSeminarleiterData.clear();
			this.lesen();
		} 
		catch (IOException e) 
		{
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
	
	/** Methode zur Neuanlegung eines Seminarleiters
	 */
		public void btNeu_Click(ActionEvent event)
		{
			// leere Details-Maske aufrufen
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SeminarleiterDetails.fxml"));
			try 
			{
				Parent root = loader.load();
				Stage stage = new Stage();
				stage.setTitle("Neuen Seminarleiter erzeugen");
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
			    SeminarleiterDetailsController ctl = loader.getController();
			    ctl.setSeminarleiter(new Seminarleiter());
			    ctl.setNeu(true);
				stage.showAndWait();
				tabSeminarleiterData.clear();
				this.lesen();
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
	}
