package zeitplanung;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.sun.mail.iap.Response;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import kurs.KursTabelleController;
import model.Ausbildung;
import model.AusbildungsfestTermine;
import model.Kurs;
import model.Raum;
import model.Referent;
import model.Zeitplanung;
import raumplanung.RaumController;
import referent.ReferentController;

/**
 * Zuständiger Controller für TerminDetails.fxml
 * @author Manuel Lara Bisch
 */
public class TerminDetailsController {
	@FXML
	private Label lbTerminNr;
	@FXML
	private TextField txtTerminNr;
	@FXML
	private TextField txtKursNr;
	@FXML
	private Label lbKurs;
	@FXML
	private Button btSucheKurs;
	@FXML
	private TextField txtRaumNr;
	@FXML
	private Button btSucheRaum;
	@FXML
	private TextField txtReferentName;
	private int referentenNr;
	@FXML
	private Button btSucheReferent;
	
	@FXML
	private TextArea taBemerkung;
	
	@FXML
	private DatePicker dpBeginn;
	@FXML
	private DatePicker dpEnde;
	
	@FXML
	private Label lbReferentName;
	@FXML
	private Label lbBeginn;
	@FXML
	private Label lbEnde;
	
	
	@FXML
	private Button btLoeschen;
	@FXML
	private Button btSpeichern;
	@FXML
	private Button btEnde;
	
	
	private Zeitplanung zeitplanung;
	private Referent referent;
	private Ausbildung ausbildung;
	
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
	private EntityManager em = emf.createEntityManager();
	
	/**
	 * Initialisiert cbTerminArt mit Standardwerten.
	 */
	@FXML
	public void initialize(){
		
		txtTerminNr.setVisible(false);
		lbTerminNr.setVisible(false);
		
	}
	
	/**
	 * Wird bei Klick auf btSucheReferent ausgeführt.
	 * Öffnet eine Referent-Maske zur Auswahl eines Referenten für den Termin.
	 */
	public void btSucheReferent_Click(ActionEvent event){
		// TODO: evtl besser als Dropdown Liste bestehender Referenten
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/referent/Referent.fxml"));
		try {
			// TODO: Nur Referenten anzeigen, die für den Kurs relevant sind
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			stage.setTitle("Referenten");
			Scene sce = new Scene(root,815,533);
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
			ReferentController ctl = loader.getController();
			ctl.lesen();
						
			stage.showAndWait();
			if (ctl.getReferent() != null){
				referentenNr = ctl.getReferent().getReferentenNr();
				txtReferentName.setText(ctl.getReferent().getVorname() + " " + ctl.getReferent().getNachname());
				referent = ctl.getReferent();
			}
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * Wird bei Klick auf btSucheKurs ausgeführt. Öffnet Kursmaske zur Auswahl eines Kurs-Objekts.
	 */
	public void btSucheKurs_Click(ActionEvent event){
		// TODO: Nur Kurse anzeigen, die für Ausbildungsgang und Lehrjahr relevant sind
		//		 -- mit Option, einen Kurs neu anzulegen
		// TODO: evtl besser als Dropdown-Liste bestehender Kurse
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/kurs/KursTabelle.fxml"));
		
		try {
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			stage.setTitle("Kurse");
			Scene sce = new Scene(root,800,480);
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
			KursTabelleController ctl = loader.getController();
			ctl.lesen();
			stage.showAndWait();
			txtKursNr.setText("" + ctl.getKurs().getKursNr());
			lbKurs.setText(ctl.getKurs().getKursBezeichnung());
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	/**
	 * Wird bei Klick auf btSucheRaum ausgeführt. Öffnet Maske zur Auswahl eines Raum-Objekts.
	 */
	public void btSucheRaum_Click(ActionEvent event){
		// TODO: Nur Räume anzeigen, die zu gegebenem Zeitpunkt nicht belegt sind
		// TODO: evtl besser als Dropdown liste
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/raumplanung/Raum.fxml"));
		try {
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Kurse");
			Scene sce = new Scene(root,800,480);
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
			RaumController ctl = loader.getController();
			ctl.lesen();
			stage.showAndWait();
			txtRaumNr.setText("" + ctl.getRaum().getRaumNr());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Wird bei Klick auf btLoeschen ausgeführt.
	 * Löscht den aktuellen Datensatz nach Bestätigung und schließt das Detailfenster.
	 */
	public void btLoeschen_Click(ActionEvent event){
		// Rückfragedialog
		if (zeitplanung != null){
			Alert a = new Alert(AlertType.CONFIRMATION);
			a.setHeaderText("");
			a.setContentText("Sie sind im Begriff, diesen Datensatz unwiederbringlich zu löschen.\n"
					+ "Wollen Sie diesen Termin wirklich löschen?");
			if (a.showAndWait().get() == ButtonType.OK){
				// TODO: Löschen
				em.getTransaction().begin();
				em.remove(em.find(Zeitplanung.class, zeitplanung.getTerminNr()));
				em.getTransaction().commit();
			} else return;
		}
		btEnde_Click(event);
	}
	
	/**
	 * Wird bei Klick of btSpeichern ausgeführt
	 */
	public void btSpeichern_Click(ActionEvent event){
		/* DONE: falls neuer Termin: neue Zeitplanung generieren und terminNr setzen
		 *  (ansonsten hier NullPo)
		 */
		if (zeitplanung != null){
			try {
				zeitplanung = em.find(Zeitplanung.class, zeitplanung.getTerminNr());
			} catch (NoResultException e) {
				System.out.println("Kein Objekt in Datenbank");
				txtTerminNr.setVisible(true);
				lbTerminNr.setVisible(true);
			}
		} else
			zeitplanung = new Zeitplanung();
		zeitplanung.setAendDatum(Date.from(Instant.now()));
		zeitplanung.setAnfangsDatum(Date.from(dpBeginn.getValue().atStartOfDay().atZone(ZoneId.of("Europe/Berlin")).toInstant()));
		zeitplanung.setEndDatum(Date.from(dpEnde.getValue().atStartOfDay().atZone(ZoneId.of("Europe/Berlin")).toInstant()));
		zeitplanung.setBemerkung(taBemerkung.getText());
		zeitplanung.setTermintyp(1);
		zeitplanung.setAusbildungsNr(ausbildung.getAusbildungsNr());
		// Objekte Referent, Raum, Kurs setzen
		
		String fehlerDetail = new String();
		// TODO: Fehlerprüfungen:
		// TODO: 	Anfangs, Enddatum in richtiger Reihenfolge?
		// Referent existiert, hat zwei Termine gleichzeitig? PRIORITÄT1
		// Prüfen referent existiert
		if (referent != null)
			zeitplanung.setReferent(referent);
		// Prüfen Referent nicht anderweitig belegt
		LocalDate refPruefAnfang = dpBeginn.getValue(),
				refPruefEnde = dpEnde.getValue();
		Zeitplanung refKonfliktTermin = null;
		TypedQuery<Zeitplanung> q = em.createQuery("select z from Zeitplanung z"
				+ " where z.referent.referentenNr = :ref"  +
				" and z.anfangsDatum between :anf and :end", Zeitplanung.class);
		q.setParameter("ref", (referent == null ? -1 : referent.getReferentenNr()));
		q.setParameter("anf", Date.from(refPruefAnfang.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		q.setParameter("end", Date.from(refPruefEnde.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		List<Zeitplanung> lirefPruef1 =	q.getResultList();
		for (Zeitplanung z : lirefPruef1) {
			if (z.getTerminNr() != zeitplanung.getTerminNr()){
				// Referent anderweitig belegt
				// Fehlermeldung, Speichern abbrechen
				refKonfliktTermin = z;
			}
		}
		q =	em.createQuery("select z from Zeitplanung z"
				+ " where z.referent.referentenNr = :ref" +
				" and z.endDatum between :anf and :end", Zeitplanung.class);
		q.setParameter("ref", (referent == null ? -1 : referent.getReferentenNr()));
		q.setParameter("anf", Date.from(refPruefAnfang.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		q.setParameter("end", Date.from(refPruefEnde.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		List<Zeitplanung> lirefPruef2 =	q.getResultList();
		for (Zeitplanung z : lirefPruef2) {
			if (z.getTerminNr() != zeitplanung.getTerminNr()){
				// Referent anderweitig belegt
				// Fehlermeldung, Speichern abbrechen
				refKonfliktTermin = z;
			}
		}
		
		Zeitplanung raumKonfliktTermin = null;
		q =	em.createQuery("select z from Zeitplanung z"
				+ " where z.raum.raumNr = :rnr" +
				" and z.anfangsDatum between :anf and :end", Zeitplanung.class);
		q.setParameter("rnr", (txtRaumNr.getText() == null ? "-1" : txtRaumNr.getText()));
		q.setParameter("anf", Date.from(refPruefAnfang.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		q.setParameter("end", Date.from(refPruefEnde.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		List<Zeitplanung> liraumPruef1 = q.getResultList();
		for (Zeitplanung z : liraumPruef1) {
			if (z.getTerminNr() != zeitplanung.getTerminNr()){
				// Raum anderweitig belegt
				// Fehlermeldung, Speichern abbrechen
				raumKonfliktTermin = z;
			}
		}
		q = em.createQuery("select z from Zeitplanung z"
				+ " where z.raum.raumNr = :rnr" +
				" and z.endDatum between :anf and :end", Zeitplanung.class);
		q.setParameter("rnr", (txtRaumNr.getText() == null ? "-1" : txtRaumNr.getText()));
		q.setParameter("anf", Date.from(refPruefAnfang.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		q.setParameter("end", Date.from(refPruefEnde.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		List<Zeitplanung> liraumPruef2 = q.getResultList();
		for (Zeitplanung z : liraumPruef2) {
			if (z.getTerminNr() != zeitplanung.getTerminNr()){
				// Raum anderweitig belegt
				// Fehlermeldung, Speichern abbrechen
				raumKonfliktTermin = z;
			}
		}
		// TODO:	Raum existiert, zwischenzeitlich anderweitig belegt?
		zeitplanung.setRaum(em.find(Raum.class, txtRaumNr.getText()));
		try {
			zeitplanung.setKur(em.find(Kurs.class, Integer.parseInt(txtKursNr.getText())));
		} catch (NumberFormatException e) {
			fehlerDetail += "\nBitte wählen Sie einen gültigen Kurs aus.";
		}
		
		// TODO: 	Kurs existiert, hat zwei Termine gleichzeitig?
		// TODO:    Bereits anderer Termin für Ausbildungsgang  + Lehrjahr in diesem Zeitraum?
//		q = em.createQuery("select z from Zeitplanung z"
//				+ " where z.terminNr = ", Zeitplanung.class);
		// TODO:	Daten nur auf Arbeitstage setzen (Wochenenden, Feiertage auslassen)
		// TODO: weitere Fehlerbedingungen (negiert) in diese Überprüfung
		if(refKonfliktTermin == null
				&& raumKonfliktTermin == null
				&& zeitplanung.getReferent() != null
				&& (dpEnde.getValue().isAfter(dpBeginn.getValue()) 
						|| dpEnde.getValue().isEqual(dpBeginn.getValue()))
				&& zeitplanung.getKur() != null)
		{
			if(!em.getTransaction().isActive())
			em.getTransaction().begin();
			em.persist(zeitplanung);
			em.getTransaction().commit();

			/*
			 * Standard Enddatum auf ersten Donnerstag nach Anfangsdatum
			 */
			// DONE: Nach erfolgreichem Speichern, Maske schließen
			((Stage) (btSpeichern.getScene().getWindow())).close();
		}
		else
		{
			ButtonType ja = new ButtonType("Ja", ButtonBar.ButtonData.OK_DONE);
			ButtonType nein = new ButtonType("Nein", ButtonBar.ButtonData.CANCEL_CLOSE);
			Alert a = new Alert(AlertType.WARNING, "", ja, nein);
			if(referent == null){
				fehlerDetail += "\nKein gültiger Referent ausgewählt.";
				txtReferentName.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
			}
			if(zeitplanung.getKur() == null){
				txtKursNr.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
			}
			styleDateError(refKonfliktTermin == null && raumKonfliktTermin == null);
			if(refKonfliktTermin != null){
				fehlerDetail += "\nDer ausgewählte Referent ist innerhalb des angegebenen Zeitraums anderweitig belegt:"
						+ "\n\tKurs: " + refKonfliktTermin.getKur().getKursBezeichnung() +
						"\n\tvon " + refKonfliktTermin.getAnfangsDatum().toLocaleString() +
						"\t bis " + refKonfliktTermin.getEndDatum().toLocaleString();
			}
			if(raumKonfliktTermin != null){
				fehlerDetail += "\nDer ausgewählte Raum ist innerhalb des angegebenen Zeitraums anderweitig belegt:"
						+ "\n\tKurs: " + raumKonfliktTermin.getKur().getKursBezeichnung() +
						"\n\tvon " + raumKonfliktTermin.getAnfangsDatum().toLocaleString() +
						"\t bis " + raumKonfliktTermin.getEndDatum().toLocaleString();
			}
			if(zeitplanung.getEndDatum().before(zeitplanung.getAnfangsDatum())){
				fehlerDetail += "\nDas Datum Ende darf nicht vor dem Datum Beginn liegen.";
			}
			a.setTitle("Fehlerhafte Eingabe");
			a.setHeaderText("");
			a.setContentText("Bitte korrigieren Sie die rot markierten Einträge. Details:" + fehlerDetail + "\n\nOhne Korrektur speichern?");
			a.showAndWait().ifPresent(response -> {
				if(response == ja) {
					if(!em.getTransaction().isActive())
						em.getTransaction().begin();
					em.persist(zeitplanung);
					em.getTransaction().commit();
					((Stage) (btSpeichern.getScene().getWindow())).close();
				}
				else
				{
					if(!em.getTransaction().isActive())
						em.getTransaction().begin();
					em.getTransaction().rollback();
				}
			});
			return;
		}
	}
	/**
	 * Wird beim Klick auf btEnde ausgeführt. Schließt das Fenster.
	 */
	public void btEnde_Click(ActionEvent event){
		if (zeitplanung == null){
			Alert a = new Alert(AlertType.CONFIRMATION);
			a.setHeaderText("");
			a.setContentText("Dieser neu angelegte Termin wurde noch nicht gespeichert.\n"
					+ "Wollen Sie ihn verwerfen?");
			if(a.showAndWait().get() == ButtonType.CANCEL)
				return;
		}
		((Stage) (btEnde.getScene().getWindow())).close();
	}
	
	/**
	 * Wird bei Auswahl eines Anfangsdatums ausgeführt.
	 * Sollte Anfangsdatum Montag bis Donnerstag sein,
	 * wird Enddatum auf Donnerstag derselben Woche gesetzt.
	 * Ansonsten wird Enddatum auf Anfangsdatum gesetzt. 
	 */
	public void dpBeginn_onSelect(ActionEvent event){
		LocalDate ldEnd = dpBeginn.getValue();
		while(ldEnd.getDayOfWeek().getValue() < 4){
			ldEnd = ldEnd.plusDays(1);
		}
		dpEnde.setValue(ldEnd);
	}

	/**
	 * Standard-Getter für referentenNr.
	 */
	public int getReferentNr() {
		return referentenNr;
	}

	/**
	 * Standard-Getter für zeitplanung.
	 */
	public Zeitplanung getZeitplanung() {
		return zeitplanung;
	}

	/**
	 * Setzt die Eingabefelder auf die Attributswerte, die beim Aufruf der Maske übergeben wurden
	 * @param z das übergebene Zeitplanung-Objekt
	 */
	public void setZeitplanung(Zeitplanung z, Ausbildung a) {
		if (z != null){
			this.zeitplanung = z;
			this.referent = z.getReferent();
			txtKursNr.setText(z.getKur().getKursNr() + "");
			lbKurs.setText(z.getKur().getKursBezeichnung());
			txtRaumNr.setText(z.getRaum().getRaumNr());
			txtReferentName.setText(z.getReferent().getNachname());
			txtTerminNr.setText(z.getTerminNr() + "");
			taBemerkung.setText(z.getBemerkung());
			dpBeginn.setValue(date2LocalDate(z.getAnfangsDatum()));
			dpEnde.setValue(date2LocalDate(z.getEndDatum()));
		}
		this.ausbildung = a;
	}
	
	public void setZeitplanung(Zeitplanung z, Ausbildung a, LocalDate da, LocalDate de) {
		this.ausbildung = a;
		if (z == null){
			this.zeitplanung = new Zeitplanung();
		}
		dpBeginn.setValue(da);
		dpEnde.setValue(de);
		
	}
	
	public void setBeginn(Date date){
		dpBeginn.setValue(date2LocalDate(date));
	}
	
	public void setEnde(Date date){
		dpEnde.setValue(date2LocalDate(date));
	}
	
	public LocalDate date2LocalDate(Date date){
		return date.toInstant().atZone(ZoneId.of("Europe/Berlin")).toLocalDate();
	}
	public void styleDateError(boolean fehler){
		if(fehler){
			dpBeginn.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
			dpEnde.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
			lbBeginn.setStyle("-fx-text-background-color:red;-fx-font-weight:bold;");
			lbEnde.setStyle("-fx-text-background-color:red;-fx-font-weight:bold;");
		}else{
			dpBeginn.setStyle(null);
			dpEnde.setStyle(null);
			lbBeginn.setStyle(null);
			lbEnde.setStyle(null);
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public boolean isArbeitstag(Date d){
		if(d.getDay() > 0 && d.getDay() < 6) // Mo-Fr
			return true;
		return false;
	}

}
