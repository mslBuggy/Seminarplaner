package seminarleiter;

import java.time.Instant;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Seminarleiter;

/** SeminarleiterDetailsController enthält Methoden 
 * zum speichern von geänderten an einem bestimmten
 * Seminarleiter und zum Abbruch der Bearbeitung
 */
public class SeminarleiterDetailsController 
{

	private Seminarleiter s;
	private boolean neu;
	
	/** FXML Notationen
	 *   
	 */
	@FXML
	private Pane pane;
	
	@FXML
	private Label labSeminarleiterNr;
	
	@FXML
	private Label labVorname;
	
	@FXML
	private Label labNachname;
	
	@FXML
	private Label labGeschlecht;
	
	@FXML
	private Label labTelNr;
	
	@FXML
	private Label labEmail;
	
	@FXML
	private Label labBemerkung;
	
	@FXML
	private TextField textSeminarleiterNr;
	
	@FXML
	private TextField textVorname;
	
	@FXML
	private TextField textNachname;
	
	@FXML
	private ComboBox<String> cbGeschlecht;
	
	@FXML
	private TextField textTelnr;
	
	@FXML
	private TextField textEmail;
	
	@FXML
	private TextField textBemerkung;
	
	@FXML
	private Button ende;
	
	@FXML
	private Button speichern;
	
	@FXML
	public void initialize()
	{
		cbGeschlecht.getItems().addAll(
				"m",
				"w"
			);
	}
	/** Methode zum Anlegen neuer Seminarleiter
	 * 
	 */
	public void setNeu(boolean neu) 
	{
		this.neu = neu;
	}
	
	
	/** Methode zum Speichern von geänderten Daten an einem Seminarleiter
	 * ToDo 1.0 Methode erweitern: Inkonsistene Dateneingabe durch Überprüfung verhindern
	 */
	public void btSpeichern_Click(ActionEvent event)
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		if(!neu) 
		/** Falls bestehender Referent geändert werden soll
		  */
		{ 
			Query query = em.createQuery("select s from Seminarleiter s where s.SeminarleiterNr = " +s.getSeminarleiterNr());
			s = (Seminarleiter) query.getSingleResult(); /** überschreibt bisheriges Objekt
																							*/
		}
		s.setSeminarleiterNr(Integer.parseInt(textSeminarleiterNr.getText()+""));
		s.setVorname(textVorname.getText());
		s.setNachname(textNachname.getText());
		s.setGeschlecht(cbGeschlecht.getValue().charAt(0));
		s.setBemerkung(textBemerkung.getText());
		s.setEMail(textEmail.getText());
		s.setAendDatum(Date.from(Instant.now()));
		s.setAendBenutzer(System.getenv("username"));
		
		if(s.getSeminarleiterNr() != 0 && s.getVorname() != null && s.getNachname() != null && s.getEMail() != null 
				&& s.getGeschlecht() == 'm' || s.getGeschlecht() == 'w')
		{
			em.getTransaction().begin();
			em.persist(s);
			em.getTransaction().commit();
			(
					(Stage) 
					(
							speichern.
								getScene().
									getWindow()
					)
			).
			close();	
			}
		else
		{
			Alert a = new Alert(AlertType.ERROR);
			String fehlerDetail = new String();
			
			if(s.getSeminarleiterNr() == 0)
			{
				textSeminarleiterNr.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
				labSeminarleiterNr.setStyle("-fx-text-background-color:red;-fx-font-weight:bold;");
				fehlerDetail +="\n\tSeminarleiter-Nr darf nicht 0 sein.";
			}
			
			else
			{
				textSeminarleiterNr.setStyle(null);
				labSeminarleiterNr.setStyle(null);
			}
			
			if(cbGeschlecht.getValue() == null)
			{
						fehlerDetail +="\n\tBitte Geschlecht auswählen";
			}
			
			else
			{
				cbGeschlecht.setStyle(null);
				labGeschlecht.setStyle(null);
			}
			
			if(s.getVorname() == null)
			{
				textVorname.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");	
				labVorname.setStyle("-fx-text-background-color:red;-fx-font-weight:bold;");
				fehlerDetail += "\n\tVorname darf nicht leer sein.";
			}

			else
			{
				textVorname.setStyle(null);
				labVorname.setStyle(null);
			}
			
			if(s.getNachname() == null)
			{
				textNachname.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
				labNachname.setStyle("-fx-text-background-color:red;-fx-font-weight:bold;");
				fehlerDetail +="\n\tNachname darf nicht leer sein.";
			}
			
			else
			{
				textNachname.setStyle(null);
				labNachname.setStyle(null);
			}
			
			if(s.getEMail() == null)
			{
				textEmail.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
				labEmail.setStyle("-fx-text-background-color:red;-fx-font-weight:bold;");
				fehlerDetail +="\n\tEmail darf nicht leer sein.";
			}
			
			else
			{
				textNachname.setStyle(null);
				labEmail.setStyle(null);
			}
			
			a.setHeaderText("");
			a.setContentText("Bitte korrigieren Sie die rot markierten Einträge. Details:"
					+ fehlerDetail);
			a.show();
			return;
		}
	}
		
	
	
	/** Methode zum Abbruch der Bearbeitung
	 * 
	 */
	public void ende_Click(ActionEvent event)
	{
		Stage stage = (Stage) ende.getScene().getWindow();
		stage.close();
	}
	
	/** Methode zur Übergabe der Daten vom SeminarleiterDetailsController
	 * zum Controller
	 */
	public void setSeminarleiter(Seminarleiter s) 
	{
			this.s = s;
			textSeminarleiterNr.setText(this.s.getSeminarleiterNr() +"");
			textVorname.setText(this.s.getVorname());
			textNachname.setText(this.s.getNachname());
			textTelnr.setText(this.s.getTelefonNr());
			textEmail.setText(this.s.getEMail());
			textBemerkung.setText(this.s.getBemerkung());
			cbGeschlecht.setValue(this.s.getGeschlecht()+"");
	}

}