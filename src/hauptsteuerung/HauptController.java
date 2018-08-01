package hauptsteuerung;



import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import ausbildung.AusbildungstabelleController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import kurs.KursTabelleController;
import model.Seminarleiter;
import raumplanung.RaumController;
import referent.ReferentController;
import referentenplanung.PlanungReferentController;
import seminarleiter.SeminarleiterController;
import teilnehmer.TeilnehmerController;
import zeitplanung.FestTerminTabelleController;
import zeitplanung.PlanungAusbildungController;
import zeitplanung.TerminDetailsController;
import zeitplanung.ZeitplanTabelleController;
/**
 * Steuerung f�r das ganze Projekt
 * 
 * Steuerung des gesamten Projektes
 * Anmeldung des Benutzers (Seminarleiter)
 * Auswertung der Men�s
 * 
 * FXML : Hauptfenster.fxml
 * ToDO: Anmeldung derzeit dekativiert, erst bei Produktions�bernahme aktivieren
 * 
 * @author haug_heinrich
 * 
 * 
 *
 */
public class HauptController {
	// aktueller Seminarleiter f�r alle Anwendungen (AendBenutzer !)
	public static Seminarleiter aktBenutzer;

	 
	 @FXML
	 private MenuBar menue;
	 @FXML
	 private MenuItem mnuZeitplanung;
	 
	 @FXML
	 private MenuItem mnuTeilnehmer;
	 
	 @FXML
	 private MenuItem mnuAusbildung;
	 @FXML
	 private MenuItem mnuRaueme;
	 @FXML
	 private MenuItem mnuReferent;
	 @FXML
	 private MenuItem mnuReferentenplanung;
	 @FXML
	 private MenuItem mnuEnde;
	 @FXML
	 private MenuItem mnuKurse;
	 @FXML
	 private MenuItem mnuAusbildungsgang;
	 @FXML
	 private MenuItem mnuSeminarleiter;
	 @FXML
	 private MenuItem mnuFesteTermine;
	 
	 // Toolbar Eintr�ge
	 @FXML
	 private Button tbEnde;
	 
	 @FXML
	 private ImageView imgTitel;
	 
	 @FXML
	 private ImageView imgBBW;
	 
	 @FXML
	 private ImageView imgBFZ;
	 
	 @FXML
	 private ImageView imgGFI;
	 
	 @FXML
	 private ImageView imgGPS;
	 
	 @FXML
	 private Pane login;
	 
	 @FXML
	 private TextField txtAnmeldungEmail;
	 
	 @FXML
	 private PasswordField txtAnmeldungPasswort;
	 
	 @FXML
	 private Hyperlink hyAnmeldungPasswortVergessen;
	 
	 @FXML
	 private Button btAnmelden;
	 @FXML
	 private PasswordField txtGesendetesPasswort;
	 
	 @FXML
	 private PasswordField txtNeuesPasswort;
	 
	 @FXML
	 private PasswordField txtNeuesPasswortWiederholen;
	 
	 @FXML
	 private Button btNeuesPasswort;
	 
	 @FXML
	 private Pane pnNeuesPasswort;
	 @FXML
	 private Button tbReferent;
	 @FXML
	 private Button tbAusbildung;
	 
	 @FXML
	 private Button tbZeitplanung;
	 
	 private EntityManagerFactory emf;
	 private EntityManager em;
	 
	 private  String neuesGesendetesPasswort = "";
	 /**
	  * Konstuktor derzeit keine Funktion
	  */
	 public HauptController()
	 {
		 emf = 
				 Persistence.createEntityManagerFactory("Seminarplaner");
		 em = emf.createEntityManager();
		 ZentraleDienste.feiertage(LocalDate.now().getYear());
		 ZentraleDienste.feiertage(LocalDate.now().getYear()+1);
		 
		 
		
	 }
	 
	 /**
	  * Panel f�r neue Passwortvergabe ausgebelendet
	  * wird aktiviert wenn ein neues Passwort erforderlich ist
	  */
	 public void initialize()
	 {
		
		
		 
		
		 if (pnNeuesPasswort != null)
			 pnNeuesPasswort.setVisible(false);
		 
		 

	 }
	 /**
	  * Anmeldung des Benutzers 
	  * M�gliche Benutzer sind die Seminarleiter
	  * Anmeldung mit EMail und dem Passwort
	  * beim ersten Mal wird kein Passwort eingeben
	  * es wird die Methode Neues Passwort aufgerufen
	  * Alle Passw�rter werden verschl�sselt gespeichert
	  * Methode : ZentraleDienste.verschluesselung
	  * Der angemeldete Benutzer wird im statischen Attribut aktBenutzer festgehalten
	  * @param event
	  */
	 public void btAnmelden_Click(ActionEvent event)
	 {
		 // Disable setzen
		// this.login.setDisable(true);
		
		
		 Query query = em.createQuery("Select s from Seminarleiter s "+
		 "where s.EMail = '"+this.txtAnmeldungEmail.getText().trim()+"'");
		 try
		 {
		    Seminarleiter s = (Seminarleiter) query.getSingleResult();
		    HauptController.aktBenutzer = s;
		    if (s.getPasswort() == null)
		    	this.NeuesPasswort(s);
		    else
		    {
		    	String pw = ZentraleDienste.verschluesselung(this.txtAnmeldungPasswort.getText().trim());
		    	if (pw.equals(s.getPasswort().trim()))
		    	{
			    	Alert message = new Alert(AlertType.INFORMATION);
			    	message.setHeaderText("");
			    	message.setContentText("Willkommen "+ZentraleDienste.anredefeststellen(s.getNachname(),s.getVorname(),s.getGeschlecht()));
				
			    	message.showAndWait();
		    	}
		    	else
		    	{
		    		Alert message = new Alert(AlertType.ERROR);
			    	message.setHeaderText("Sie konnten nicht angemeldet werden");
			    	message.setContentText("Email oder Passwort falsch");
				
			    	message.showAndWait();
			        this.login.setDisable(false);
		    	}
		    		
		    }
		    
		    		
		    
		 }
		 catch (Exception e)
		 {
			 Alert message = new Alert(AlertType.ERROR);
			 message.setTitle("Anmeldung");
			 message.setHeaderText("Leider konnten Sie nicht angemeldet werden");
			
			 message.showAndWait();
			 this.login.setDisable(false);
		 }
	 }
	 /**
	  * 
	  * Es wird ein neues Passwort generiert �ber die Methode
	  * ZentraleDienste passwortGenerierung
	  * Dieses wird via SMPTMail an die Emailadresse des Seminarleiters geschickt
	  * @param s
	  * @throws NoSuchAlgorithmException
	  * @throws AddressException
	  * @throws MessagingException
	  */
	 private void NeuesPasswort(Seminarleiter s) throws NoSuchAlgorithmException, AddressException, MessagingException
	 {
		 Alert message = new Alert(AlertType.ERROR);
	     message.setHeaderText("Neues Passwort");
	     message.setContentText("Sie bekommen ein Passwort an Ihre EMail Adresse gesendet\n"+ 
	     "Bitte geben Sie diese ein und vergeben ein eigenes Passwort");
		
	     message.showAndWait();
	     
	  // Erstellen des Generierten Passwortes 
	     // und senden des Emails
	     neuesGesendetesPasswort = ZentraleDienste.passwortGenerierung();
	     SmtpMail mail = new SmtpMail(this.txtAnmeldungEmail.getText().trim(),
	    		 "Ihr Eingabepasswort ", "Geben Sie bitte dieses Passwort\n\n\n"+
	    				 neuesGesendetesPasswort+"\n\n ein und vergeben Sie dann ihr eigenes Passwort");
	     
		 this.pnNeuesPasswort.setVisible(true);
	 }
	 /**
	  * Der benutzer bekommt das generierte Passwort per Email und muss dieses eingeben
	  * Anschliessend kann er sein eigenes Passwort vergeben
	  *  
	  * @param event
	  */
	 public void btNeuesPasswort_click(ActionEvent event)
	 {
		 if (!this.txtGesendetesPasswort.getText().equals(this.neuesGesendetesPasswort))
		 {
			 Alert message = new Alert(AlertType.ERROR);
		     message.setHeaderText("Das Passwort ist nicht korrekt");
			 	     
		     message.showAndWait(); 
		     return;
		 }
			 
		
		 if (!this.txtNeuesPasswort.getText().trim().equals(this.txtNeuesPasswortWiederholen.getText().trim()))
		 {
			 Alert message = new Alert(AlertType.ERROR);
		     message.setHeaderText("Die Passw�rter stimmen nicht �berein");
			  
		     
		     
		     
		     message.showAndWait(); 
		     return;
		 }
		 
		 // Verschluesseln des Passwortes
		 try {
			System.out.println(ZentraleDienste.verschluesselung(this.txtNeuesPasswort.getText().trim()));
			
			
			 EntityManagerFactory emf = 
					 Persistence.createEntityManagerFactory("Seminarplaner");
			 EntityManager em = emf.createEntityManager();
			
			 Query query = em.createQuery("Select s from Seminarleiter s "+
			 "where s.seminarleiterNr = "+aktBenutzer.getSeminarleiterNr());
			 
			 Seminarleiter s=(Seminarleiter) query.getSingleResult();
			 s.setPasswort(ZentraleDienste.verschluesselung(this.txtNeuesPasswort.getText().trim()));
			 em.getTransaction().begin();
			 em.persist(s);
			 em.getTransaction().commit();
			 
			 Alert message = new Alert(AlertType.INFORMATION);
		     message.setHeaderText("Willkommen "+ZentraleDienste.anredefeststellen(s.getNachname(),s.getVorname(),s.getGeschlecht()));
			 message.setContentText("Ihr neues Passwort wurde gespeichert");
		     message.showAndWait();
		     
		     this.login.setDisable(false);
		     this.pnNeuesPasswort.setVisible(false);
		     em.close();
		     emf.close();
			 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 /**
	  * Wenn der Benutzer sein Passwort vergessen hat
	  * kann er ein neues Passwort anfordern
	  * Dann wird wie bei einem neuenBenutzer verfahren
	  * @param event
	  */
	 public void passwortvergessen(ActionEvent event)
	 {
		 EntityManagerFactory emf = 
				 Persistence.createEntityManagerFactory("Seminarplaner");
		 EntityManager em = emf.createEntityManager();
		
		 Query query = em.createQuery("Select s from Seminarleiter s "+
		 "where s.seminarleiterNr = "+aktBenutzer.getSeminarleiterNr());
		 
		 Seminarleiter s=(Seminarleiter) query.getSingleResult();
		 s.setPasswort(null);
		 em.getTransaction().begin();
		 em.persist(s);
		 em.getTransaction().commit();
		 
		 em.close();
		 emf.close();
		 this.btAnmelden_Click(event);
	 }
	 
	 /**
	  * Beim Schliessen des Fenster (�ber das Kreuz und ende Button)
	  * wird die Abfrage, ob er wirklich beenden will durchgef�hrt
	  * @return
	  */
	 public boolean endeAbfrage()
	 {
		 Alert a = new Alert(AlertType.CONFIRMATION);
		 a.setHeaderText("");
		 a.setContentText("Wollen Sie wirklich beenden ? ");
		 Optional<ButtonType> result = a.showAndWait();
		 if (result.get() == ButtonType.OK)
			 return true;
		 else
			 return false;
	 }
	 
	 /**
	  * Men�steuerung
	  * Es werden die entsprechenden Methoden f�r den Aufruf
	  * der weiteren Fenster durchgef�hrt.
	  * Die Auswahl ist �ber das Men� (mnu..) und bei einigen
	  * �ber die Toolbar erm�glicht (tb...) 
	  * @param event
	  */
	 public void menu_Click(ActionEvent event)
	 {
		String auswahl = "";
		if (event.getSource() instanceof MenuItem)
		{
			auswahl = ((MenuItem) event.getSource()).getId();
		}
		if (event.getSource() instanceof Control)
		{
			auswahl = ((Control) event.getSource()).getId();
		}
		
		 switch (auswahl.toLowerCase())
		 {
		 case "mnuende" :
		 case "tbende" :
			 if (endeAbfrage())
				 System.exit(0);
			 break;
		 case "mnuteilnehmer" :
			 this.aufrufTeilnehmer(event);
			 break;
		 case "tbausbildung" :
		 case "mnuausbildung":
			 this.aufrufAusbildungen(event);
			 break;
		
		 case "tbzeitplanung" :
		 case "mnuzeitplanung" :
			 this.aufrufZeitplanung(event);
			 break;
		 case "mnureferentenplanung" :
			 this.aufrufReferentenplanung(event);
			 break;
		 case "mnuraueme" :
			 this.aufrufRaeume(event);
			 break;
		 case "tbreferent" :
		 case "mnureferent" :
			 this.aufrufReferenten(event);
			 break;
		 case "mnuseminarleiter" :
            
			 this.aufrufSeminarleiter(event);
			 break;
		 case "mnukurse" :
			
			 this.aufrufKurse(event);	
			 break;
		 case "mnufestetermine" :
				
			 this.aufrufFesteTermine(event);	
			 break;
		 }
	 }
	 
	 /**
	  * Aufruf der festen Termine
	  * @param event
	  */
	 private void aufrufFesteTermine(ActionEvent event) 
	 {
		 try
		 {
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("/zeitplanung/FestTerminTabelle.fxml"));
				Parent root = loader.load();
				Stage primaryStage = new Stage();
				primaryStage.setTitle("Festtermine");
				// Zuordnen der CSS-Datei
				// Achtung: CSS der FXML wird nicht aus SceneBuilder synchronisiert!!
				Scene sc = new Scene(root,800,480);
				String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
				// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
				sc.getStylesheets().add(css);
				FestTerminTabelleController ctl = loader.getController();
				ctl.lesen();
				
				primaryStage.setScene(sc);
				primaryStage.show();


		 }
		 catch (Exception e)
		 {
			 e.printStackTrace();
		 }
	 }
	
	 /**
	  * Aufruf f�r die Stammdaten Referent
	  * @param event
	  */
	 private void aufrufReferenten(ActionEvent event) 
	 {
		 try
		 {
			
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("/referent/Referent.fxml"));
			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setTitle("Referent");
			ReferentController ctl = loader.getController();
			ctl.lesen();
			primaryStage.setScene(new Scene(root, 800, 500));
			primaryStage.show();
		 }
		 catch (Exception e)
		 {
			 
		 }
	 }
	 /**
	  * Aufruf f�r die Stammdaten Referent
	  * @param event
	  */
	 private void aufrufRaeume(ActionEvent event) 
	 {
		
		 try
		 {
			
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("/raumplanung/Raum.fxml"));
			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setTitle("Raumplanung");
			RaumController ctl = loader.getController();
			ctl.lesen();
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			Scene sc = new Scene(root);
			sc.getStylesheets().add(css);
			primaryStage.setScene(sc);
			primaryStage.show();
		 }
		 catch (Exception e)
		 {
			 e.printStackTrace();
		 }
	 }
	 /**
	  * Aufruf Stammdaten Ausbildungen
	  * @param event
	  */
	 private void aufrufAusbildungen(ActionEvent event)
	 {
		 try
		 {
		 Stage stage = new Stage();
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("/ausbildung/Ausbildungstabelle.fxml"));
			Parent root = loader.load();
			stage.setTitle("Ausbildungen");
			// Zuordnen der CSS-Datei
			// Achtung: CSS der FXML wird nicht aus SceneBuilder synchronisiert!!
			Scene sc = new Scene(root,800,480);
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
			sc.getStylesheets().add(css);
			AusbildungstabelleController ctl = loader.getController();
			
			stage.setScene(sc);
			stage.show();
		 }
		 catch (Exception e)
		 {
			 
		 }
	 }
	 
	

	 /**
	  * Aufruf f�r die Stammdaten Kurse
	  * @param event
	  */
	 private void aufrufKurse(ActionEvent event) 
	 {
		 try
		 {
		    Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/kurs/KursTabelle.fxml"));
			Parent root = loader.load();
			primaryStage.setTitle("Tabelle Kurse");
			// Zuordnen der CSS-Datei
			// Achtung: CSS der FXML wird nicht aus SceneBuilder synchronisiert!!
			Scene sc = new Scene(root,800,480);
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets m�ssen als URL umgewandelt werden, daher toExternalForm()
			sc.getStylesheets().add(css);
			KursTabelleController ctl = loader.getController();
			ctl.lesen();
			primaryStage.setScene(sc);
			primaryStage.show();
		 }
		 catch(Exception e)
		 {
			 
		 }
	 }
	 
	 /**
	  * Aufruf f�r die Stammdaten Seminarleiter
	  * @param event
	  */
	 private void aufrufSeminarleiter(ActionEvent event) 
	 {
		 try
		 {
			
		    Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/seminarleiter/Seminarleiter.fxml"));
			Parent root = loader.load();
			primaryStage.setTitle("Seminarleiter");
			// Zuordnen der CSS-Datei
			// Achtung: CSS der FXML wird nicht aus SceneBuilder synchronisiert!!
			Scene sc = new Scene(root,800,480);
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets m�ssen als URL umgewandelt werden, daher toExternalForm()
			sc.getStylesheets().add(css);
			SeminarleiterController ctl = loader.getController();
			ctl.lesen();
			primaryStage.setScene(sc);
			
			primaryStage.show();
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
	 }
	 
	 private void aufrufTeilnehmer(ActionEvent event) 
	 {
		 try
		 {
			
		    Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/teilnehmer/Teilnehmer.fxml"));
			Parent root = loader.load();
			primaryStage.setTitle("Teilnehmer");
			// Zuordnen der CSS-Datei
			// Achtung: CSS der FXML wird nicht aus SceneBuilder synchronisiert!!
			Scene sc = new Scene(root);
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets m�ssen als URL umgewandelt werden, daher toExternalForm()
			sc.getStylesheets().add(css);
			TeilnehmerController ctl = loader.getController();
			ctl.einlesen();
			primaryStage.setScene(sc);
			
			primaryStage.show();
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
	 }
	 

	 /**
	  * Aufruf f�r die Zeitplanung
	  * @param event
	  */
	 private void aufrufZeitplanung(ActionEvent event)
	 {
		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/zeitplanung/PlanungAusbildung.fxml"));
		try {
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			stage.setTitle("Zeitplanung");
			Scene sce = new Scene(root,782,422);
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets m�ssen als URL umgewandelt werden, daher toExternalForm()
			sce.getStylesheets().add(css);
			stage.setScene(sce);
			stage.setMaximized(true);
			stage.initOwner(Seminarverwaltung.mdi);
			PlanungAusbildungController ctl = loader.getController();
			ctl.ausbildungenComboBox();
			
			
			stage.show();
			
		} catch (IOException e) {
			// TODO: handle exception
		}
	 }
	 private void aufrufReferentenplanung(ActionEvent event)
	 {
		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/referentenplanung/PlanungReferent.fxml"));
		try {
			Stage stage = new Stage();
			Parent root = loader.load();
			String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
			// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
			Scene sce = new Scene(root, 800, 400);
			sce.getStylesheets().add(css);
			stage.setTitle("Referentenplanung");
			stage.setScene(sce);
			stage.setMaximized(true);
			stage.initOwner(Seminarverwaltung.mdi);
			PlanungReferentController ctl = loader.getController();
			ctl.referentenComboBox();
			stage.show();
			
			
			
			
		} catch (IOException e) {
			// TODO: handle exception
		}
	 }
	
	 
	 

}
