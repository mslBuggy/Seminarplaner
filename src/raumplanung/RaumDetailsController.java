package raumplanung;

import java.time.Instant;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Raum;

public class RaumDetailsController {
	
	@FXML
	private Label labRaumNr;
	
	@FXML
	private TextField textRaumNr;
	
	@FXML
	private Button speichern;
	
	@FXML
	private Button ende;
	
	@FXML
	private Label labKapazitaet;
	
	@FXML
	private Label labhatEDV;
	
	@FXML
	private Label labBemerkung;
	
	@FXML
	private TextField textKapazitaet;
	
	@FXML
	private CheckBox cbhatEDV;
	
	@FXML
	private TextField textBemerkung;
	
	@FXML
	public void initialize(){
		
	}
	
	private Raum raum;
	private boolean neu;
	
	public void setRaum (Raum raum) {
		
		this.raum = raum;
		textRaumNr.setText(this.raum.getRaumNr()+"");
		textKapazitaet.setText(this.raum.getKapazitaet()+"");
		cbhatEDV.setText(booleanToString(this.raum.getHatEDV()));
		textBemerkung.setText(this.raum.getBemerkung());
		
	}
	
	public String booleanToString (boolean b1){
	
	if(b1)
		return "ja";
	
	return "nein";
}
	
	public void setNeu(boolean neu) {
		this.neu = neu;
	}
	
	public void speichern_Click(ActionEvent event){

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		if(!neu) { // Falls bestehender Raum geändert werden soll
			Query query = em.createQuery("select r from Raum r where r.raumNr = "+ raum.getRaumNr());
			raum = (Raum) query.getSingleResult();
			
		}
		else{
			raum = new Raum();
		}
		try{
		raum.setRaumNr(textRaumNr.getText()+"");
		raum.setKapazitaet(Integer.parseInt(textKapazitaet.getText()));
		raum.setHatEDV(cbhatEDV.isSelected());
		raum.setBemerkung(textBemerkung.getText());
		raum.setAendDatum(Date.from(Instant.now()));
		raum.setAendBenutzer(System.getenv("username"));
		if((raum.getKapazitaet() > 0) && (raum.getRaumNr() != null)){
		em.getTransaction().begin();
		em.persist(raum);
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
		}
		catch (Exception e){

			Alert a = new Alert(AlertType.ERROR);
			String fehlerDetail = new String();
			
			System.out.println(raum.getRaumNr().length());
			if(raum.getRaumNr().length() == 0){
				textRaumNr.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
				labRaumNr.setStyle("-fx-text-background-color:red;-fx-font-weight:bold;");
				fehlerDetail +="\n\tRaum-Nr darf nicht leer sein.";
			}
			else
			{
				textRaumNr.setStyle(null);
				labRaumNr.setStyle(null);
			}
			
			if(raum.getKapazitaet() == 0)
			{
				textKapazitaet.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
				labKapazitaet.setStyle("-fx-text-background-color:red;-fx-font-weight:bold;");
				fehlerDetail += "\n\tKapazität darf nicht 0 sein.";
			}
			else
			{
			textKapazitaet.setStyle(null);
			labKapazitaet.setStyle(null);
			}
			
			a.setHeaderText("");
			a.setContentText("Bitte korrigieren Sie die rot markierten Einträge. Details:"
					+ fehlerDetail);
			a.show();
			return;
		}
		
	}
	public void ende_Click(ActionEvent event){
		Stage stage = (Stage) ende.getScene().getWindow();
		stage.close();
	}
	
}