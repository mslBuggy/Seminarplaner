package ausbildung;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Ausbildung;
import seminarleiter.SeminarleiterController;

public class AusbildungDetailsController {
	
	@FXML
	private Label labAusbildungNr;
	
	@FXML
	private TextField textAusbildungNr;
	
	@FXML
	private Label labAbschlussart;
	
	@FXML
	private ComboBox<String> cbAbschlussart = new ComboBox<String>();
	private ObservableList<String> cbAbschlussartData = FXCollections.observableArrayList();
	
	@FXML
	private Label labSeminarleiterNr;
	
	@FXML
	private TextField textSeminarleiterNr;
	
	@FXML
	private Label labBezeichnung;
	
	@FXML
	private TextField textBezeichnung;
	
	@FXML
	private Label labAnfang;
	
	@FXML
	private DatePicker dpAnfang;
	
	@FXML
	private Label labEnde;
	
	@FXML 
	private DatePicker dpEnde;
	
	@FXML
	private Label labBemerkung;
	
	@FXML
	private TextField textBemerkung;
	
	@FXML
	private Button speichern;
	
	@FXML
	private Button sucheSemleiter;
	
	@FXML
	private Button ende;
	
	@FXML
	public void initialize(){
		cbAbschlussartData.add("IHK");
		cbAbschlussart.setItems(cbAbschlussartData);
	}
	
	private Ausbildung ab;
	private boolean neu;
		
	public String booleanToString (boolean b1){
	
	if(b1)
		return "ja";
	
	return "nein";
}
	
	public void setNeu(boolean neu) {
		this.neu = neu;
	}
	
	public LocalDate date2LocalDate(Date date){
		return date.toInstant().atZone(ZoneId.of("Europe/Berlin")).toLocalDate();
	}
	
	public void speichern_Click(ActionEvent event){

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		if(!neu) { // Falls bestehender Raum geändert werden soll
			Query query = em.createQuery("select a from Ausbildung a where a.AusbildungsNr = "+ ab.getAusbildungsNr());
			ab = (Ausbildung) query.getSingleResult();
			
		}
		else{
			ab = new Ausbildung();
	}
		ab.setAbschlussart(Integer.parseInt(cbAbschlussart.getValue()));
		dpAnfang.setValue(date2LocalDate(ab.getAnfangsDatum()));
		dpEnde.setValue(date2LocalDate(ab.getEndDatum()));
		ab.setBemerkung(textBemerkung.getText());
		ab.setAendDatum(Date.from(Instant.now()));
		ab.setAendBenutzer(System.getenv("username"));
		if(textAusbildungNr != null ){
		em.getTransaction().begin();
		em.persist(ab);
		em.getTransaction().commit();
		(
				(Stage) (
						speichern.
							getScene().
								getWindow()
						)
				).
		close();
		}
		
		else{

			Alert a = new Alert(AlertType.ERROR);
			String fehlerDetail = new String();
			
			
			a.setHeaderText("");
			a.setContentText("Bitte korrigieren Sie die rot markierten Einträge. Details:"
					+fehlerDetail);
			a.show();
			return;
		}
		
		
		
	}
	
	public void btSuche_Seminarleiter_Click(ActionEvent event){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/seminarleiter/Seminarleiter.fxml"));
		try{
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Seminarleiter");
			Scene sce = new Scene(root,600,400);
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

			SeminarleiterController ctl = loader.getController();
			ctl.lesen();

			stage.showAndWait();
			if(ctl.getSeminarleiter() != null){
				textSeminarleiterNr.setText(""+ctl.getSeminarleiter().getSeminarleiterNr());
			}

		}
		catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void ende_Click(ActionEvent event){
		Stage stage = (Stage) ende.getScene().getWindow();
		stage.close();
	}
	
	public void setAusbildung (Ausbildung ab) {
		
		this.ab = ab;
		textAusbildungNr.setText(this.ab.getAusbildungsNr()+"");
		textBezeichnung.setText(this.ab.getAusbildungsBezeichnung());
		textSeminarleiterNr.setText(this.ab.getSeminarleiter()+"");
		dpAnfang.setValue(date2LocalDate(this.ab.getAnfangsDatum()));
		dpEnde.setValue(date2LocalDate(this.ab.getEndDatum()));
		cbAbschlussart.setValue(cbAbschlussartData.get(this.ab.getAbschlussart() - 1));;
		System.out.println(cbAbschlussart.getSelectionModel().getSelectedItem());
	}
}
