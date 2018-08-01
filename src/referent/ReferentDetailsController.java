package referent;

import java.time.Instant;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Referent;

/** ReferentDetailsController enthält Methoden 
 * zum speichern von geänderten an einem bestimmten
 * Referenten und zum Abbruch der Bearbeitung
 */
public class ReferentDetailsController {

	private Referent rf;
	private boolean neu;
	
	/** FXML Notationen
	 *   
	 */
	@FXML
	private Label labReferentNr;
	
	@FXML
	private Label labVorname;
	
	@FXML
	private Label labNachname;
	
	@FXML
	private Label labFestnetz;
	
	@FXML
	private Label labMobil;
	
	@FXML
	private Label labEmail;
	
	@FXML
	private Label labBemerkung;
	
	@FXML
	private Label labAktiv;
	
	@FXML
	private TextField textReferentNr;
	
	@FXML
	private TextField textVorname;
	
	@FXML
	private TextField textNachname;
	
	@FXML
	private TextField textFestnetz;
	
	@FXML
	private TextField textMobil;
	
	@FXML
	private TextField textEmail;
	
	@FXML
	private TextField textBemerkung;
	
	@FXML
	private CheckBox cbAktiv;
	
	@FXML
	private Button ende;
	
	@FXML
	private Button speichern;
	
	@FXML
	public void initialize(){
		
	}

	/** Methode zum Anlegen neuer Referenten
	 * 
	 */
	public void setNeu(boolean neu) {
		this.neu = neu;
	}
	
	
	/** Methode zum Speichern von geänderten Daten an einem Referenten
	 * ToDo 1.0 Methode erweitern: Inkonsistene Dateneingabe durch Überprüfung verhindern
	 */
	public void speichern_Click(ActionEvent event){

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		if(!neu) { 
			Query query = em.createQuery("select r from Referent r where r.referentenNr = "+ rf.getReferentenNr());
			rf = (Referent) query.getSingleResult(); 
		}
		rf.setReferentenNr(Integer.parseInt(textReferentNr.getText()));
		rf.setNachname(textNachname.getText());
		rf.setVorname(textVorname.getText());
		rf.setEMail(textEmail.getText());
		rf.setTelMobil(textMobil.getText());
		rf.setTelFestnetz(textFestnetz.getText());
		rf.setAktiv(cbAktiv.isSelected());
		rf.setBemerkung(textBemerkung.getText());
		rf.setAendDatum(Date.from(Instant.now()));
		rf.setAendBenutzer(System.getenv("username"));
		em.getTransaction().begin();
		em.persist(rf);
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
	
	/** Methode zum Abbruch der Bearbeitung
	 * 
	 */
	public void ende_Click(ActionEvent event){
		Stage stage = (Stage) ende.getScene().getWindow();
		stage.close();
	}
	
	/** Methode zur Übergabe der Daten vom ReferentDetailsController
	 * zum ReferentController
	 */
	public void setReferent(Referent rf) {
		
			this.rf = rf;
			textReferentNr.setText(this.rf.getReferentenNr() + "");
			textVorname.setText(this.rf.getVorname());
			textNachname.setText(this.rf.getNachname());
			textFestnetz.setText(this.rf.getTelFestnetz());
			textMobil.setText(this.rf.getTelMobil());
			textEmail.setText(this.rf.getEMail());
			cbAktiv.setSelected(this.rf.getAktiv());
	}

}
