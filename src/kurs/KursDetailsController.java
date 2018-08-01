package kurs;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Ausbildung;
import model.Ausbildung_Kurs;
import model.Kurs;

/**
 * Zuständiger Controller für KursDetails.fxml
 * @author Manuel Lara Bisch
 */
public class KursDetailsController {
	// Diese vier Labels müssen konditionell Stiländerungen bekommen können (in btSpeichern_Click), und bekommen daher eigene fxids.
	@FXML
	private Label lbKursBezeichnung;
	@FXML
	private Label lbKursDauerTage;
	@FXML
	private Label lbLehrjahr;
	@FXML
	private Label lbAusbildung;
	
	@FXML
	private TextField txtKursNr;
	@FXML
	private TextField txtKursBezeichnung;
	@FXML
	private TextField txtKursDauerTage;
	@FXML
	private CheckBox cbBrauchtEDV;
	
	@FXML
	private ListView<Ausbildung> lvAusbildungen;
	private ObservableList<Ausbildung> lvAusbildungenData = FXCollections.observableArrayList();
	
	@FXML
	private TextField txtLehrjahr;
	
	@FXML
	private TextArea taBemerkung;
	
	@FXML
	private Button btLoeschen;
	@FXML
	private Button btSpeichern;
	@FXML
	private Button btAbbrechen;
	
	
	private Kurs kurs;
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
	private EntityManager em = emf.createEntityManager();
	
	/**
	 * boolean neu dient dazu, von KursTabelle die Information übergeben zu bekommen,
	 * ob es sich um einen neuen oder bereits bestehenden Kurs handelt.
	 * Dies ist für btSpeichern_Click(event) relevant.
	 */
	private boolean neu;
	
	public void setNeu(boolean neu) {
		this.neu = neu;
	}

	/**
	 * von FXML benötigte Methode, aber hier finden keine Wertzuweisungen statt.
	 * Diese sind stattdessen in setKurs(Kurs) zu finden.
	 */
	@FXML
	public void initialize(){
		lvAusbildungen.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		lvAusbildungen.setCellFactory(new Callback<ListView<Ausbildung>, ListCell<Ausbildung>>() {
			
			@Override
			public ListCell<Ausbildung> call(ListView<Ausbildung> param) {
				// TODO Method created by lara-bisch_manuel on 11.12.2017 13:18:20
				TextFieldListCell<Ausbildung> cell = new TextFieldListCell<>(new StringConverter<Ausbildung>() {

					@Override
					public String toString(Ausbildung object) {
						// TODO Method created by lara-bisch_manuel on 11.12.2017 13:20:10
						return object.getAusbildungsBezeichnung();
					}

					@Override
					public Ausbildung fromString(String string) {
						// TODO Method created by lara-bisch_manuel on 11.12.2017 13:20:10
						return null;
					}
				});
				return cell;
			}
		});
		Query query = em.createQuery("select a from Ausbildung a");
		List<Ausbildung> listAusb = query.getResultList();
		for (Ausbildung ausbildung : listAusb) {
			
			if(ausbildung.getAusbildungsBezeichnung() != null)
				lvAusbildungenData.add(ausbildung);
		}
		lvAusbildungen.setItems(lvAusbildungenData);
		System.out.println(kurs);
		if (kurs != null){
			query = em.createQuery("select a from Ausbildung a, Ausbildung_Kurs ak"
					+ " where a.AusbildungsNr = ak.AusbildungsNr and ak.KursNr = " + kurs.getKursNr());
			listAusb = query.getResultList();
			lvAusbildungen.getSelectionModel().select(0);
			System.out.println(lvAusbildungen.getSelectionModel().getSelectedIndex());
			//for (Ausbildung ausbildung : listAusb) {
			//	lvAusbildungen.getSelectionModel().select(ausbildung);
			//}
		}
	}
	
	/**
	 * Wird bei Klick auf btLoeschen ausgeführt.
	 * Löscht den aktuellen Datensatz nach Bestätigung und schließt das Detailfenster.
	 * Falls Datensatz nicht in Datenbank enthalten, wird das Detailfenster
	 * einfach nur geschlossen.
	 */
	public void btLoeschen_Click(ActionEvent event){
		// Rückfragedialog
		if (!neu){
			Alert a = new Alert(AlertType.CONFIRMATION);
			a.setHeaderText("");
			a.setContentText("Sie sind im Begriff, diesen Datensatz unwiederbringlich zu löschen.\n"
					+ "Wollen Sie diesen Kurs wirklich löschen?");
			if (a.showAndWait().get() == ButtonType.OK){
				// DONE: Löschen
				try {
					em.getTransaction().begin();
					em.remove(em.find(Kurs.class, kurs.getKursNr()));
					em.getTransaction().commit();
				} catch (RollbackException e) {
					// Warnung, Hinweis auf verknüpfte Tabelleneinträge
					Alert a2 = new Alert(AlertType.WARNING);
					a2.setHeaderText("");
					a2.setContentText("Der Kurs konnte nicht gelöscht werden, da sich noch mindestens ein "
							+ "Termin darauf bezieht. Bitte löschen Sie zuerst alle Termine dieses Kurses.");
					a2.show();
				}
				
			} else return;
		}
		btAbbrechen_Click(event);
	}
	
	/**
	 * Die Methode, die von btSpeichern aufgerufen wird.
	 * Wie der Name schon sagt, wird der Kurs in die Datenbank gespeichert;
	 * jedoch nicht, ohne vorher auf offensichtliche Fehler überprüft zu werden.
	 * Falls Eingabefehler erkannt wurden (Kursbeschreibung nicht ausgefüllt, Lehrjahr nicht Zahl zw. 1 und 3, oder DauerTage <1),
	 * wird eine entsprechende Fehlermeldung angezeigt, und die betreffenden Felder und ihre Labels werden farblich hervorgehoben.
	 * Nach erfolgreichem Speichern wird die Maske geschlossen.
	 */
	public void btSpeichern_Click(ActionEvent event){
		// DONE: Kurs speichern

		if(!neu) { // Falls bestehender Kurs geändert werden soll
			kurs = em.find(Kurs.class, kurs.getKursNr()); // überschreibt bisheriges Objekt
		}
		em.getTransaction().begin();
		kurs.setBrauchtEDV(cbBrauchtEDV.isSelected());
		kurs.setBemerkung(taBemerkung.getText());
		kurs.setKursBezeichnung(txtKursBezeichnung.getText());
		String fehlerDetail = new String();
		try {
			kurs.setLehrjahr(Integer.parseInt(txtLehrjahr.getText()));
			kurs.setKursDauerTage(Integer.parseInt(txtKursDauerTage.getText()));
		} catch (NumberFormatException e) {
			fehlerDetail += "\n\t" + (e.getMessage().substring(18) + " ist kein gültiger Eingabewert für dieses Feld.");
		}
		
		// AendBenutzer mit aktuellem Benutzer belegen
		kurs.setAendDatum(Date.from(Instant.now()));
		kurs.setAendBenutzer(System.getenv("username"));
		
		// Die ausgewählten Ausbildungsgänge zuweisen
		List<Ausbildung_Kurs> liAK = FXCollections.observableArrayList();
		List<Ausbildung> liA = lvAusbildungen.getSelectionModel().getSelectedItems();
		
		
		// Löschen der alten Relationen
		
		if (kurs.getAusbildungKurs() != null)
		{
			if(!em.getTransaction().isActive())
				em.getTransaction().begin();
		
			for (Ausbildung_Kurs ak : kurs.getAusbildungKurs())
			{
				
				
			    em.createQuery("Delete from Ausbildung_Kurs k where k.lfdNr = "+ ak.getLfdNr()).executeUpdate();
			
			}
			em.getTransaction().commit();
		}
		if(!em.getTransaction().isActive())
			em.getTransaction().begin();
	    for (Ausbildung a : liA)
	    {

	    	Ausbildung_Kurs ak = new Ausbildung_Kurs();
	    	ak.setAusbildung(a);
	    	ak.setKur(kurs);
	    	liAK.add(ak);

	    }

	    
	    kurs.setAusbildungKurs(liAK);

		// DONE: Auf Fehler überprüfen:
		//			Lehrjahr > 0, Lehrjahr < 4
		//			DauerTage > 0
		//			kursBezeichnung != null
		if (
				(kurs.getLehrjahr() > 0 && kurs.getLehrjahr() < 4)
				&& kurs.getKursDauerTage() > 0
				&& kurs.getKursBezeichnung().length() > 0
				&& !liAK.isEmpty())
		{
			if(!em.getTransaction().isActive())
				em.getTransaction().begin();
			em.persist(kurs);
			

			em.getTransaction().commit();
			// DONE: darf neuem Kurs nicht explizit Wert für kursNr zuweisen
			// Lösung: Einfügen bei kursNr in Kurs.java: @GeneratedValue(strategy=GenerationType.IDENTITY)
			
			((Stage) (btSpeichern.getScene().getWindow())).	close();
		}
		else
		{
			if(!em.getTransaction().isActive())
				em.getTransaction().begin();
			em.getTransaction().rollback();
			
			Alert a = new Alert(AlertType.ERROR);
			
			if(kurs.getKursBezeichnung() == null || kurs.getKursBezeichnung().length() < 1){
				txtKursBezeichnung.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
				lbKursBezeichnung.setStyle("-fx-text-background-color:red;-fx-font-weight:bold;");
				fehlerDetail += "\n\tKursbezeichnung darf nicht leer sein.";
			}else{
				txtKursBezeichnung.setStyle(null);
				lbKursBezeichnung.setStyle(null);
			}
			if(kurs.getLehrjahr() < 1 || kurs.getLehrjahr() > 3){
				txtLehrjahr.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
				lbLehrjahr.setStyle("-fx-text-background-color:red;-fx-font-weight:bold;");
				fehlerDetail += "\n\tLehrjahr darf nur 1, 2, oder 3 betragen.";
			}else{
				txtLehrjahr.setStyle(null);
				lbLehrjahr.setStyle(null);
			}
			if(kurs.getKursDauerTage() < 1){
				txtKursDauerTage.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
				lbKursDauerTage.setStyle("-fx-text-background-color:red;-fx-font-weight:bold;");
				fehlerDetail += "\n\tDauer muss mehr als 0 Tage betragen.";
			}else{
				txtKursDauerTage.setStyle(null);
				lbKursDauerTage.setStyle(null);
			}
			if(kurs.getAusbildungKurs().isEmpty()){
				lvAusbildungen.setStyle("-fx-border-color:red;-fx-control-inner-background:bisque");
				lbAusbildung.setStyle("-fx-text-background-color:red;-fx-font-weight:bold;");
				fehlerDetail += "\n\tEs muss mindestens ein Ausbildungsgang ausgewählt sein.";
			}else{
				lvAusbildungen.setStyle(null);
				lbAusbildung.setStyle(null);
			}
			a.setHeaderText("");
			a.setContentText("Bitte korrigieren Sie die rot markierten Einträge. Details:"
					+ fehlerDetail);
			a.show();
			return;
		}
	}
	
	/**
	 * Die Methode für den Knopf btAbbrechen. Schließt die Maske.
	 */
	public void btAbbrechen_Click(ActionEvent event){
		((Stage) (btAbbrechen.getScene().getWindow())).close();
	}

	/**
	 * Initialisiert die Eingabefelder mit Werten aus einem übergebenen Kurs.
	 * 
	 * @param kurs
	 * Der ausgewählte Kurs. Wert wird aus KursTabelleController übergeben.
	 */
	public void setKurs(Kurs kurs) {
		this.kurs = kurs;
		
		txtKursNr.setText(this.kurs.getKursNr() + "");
		txtKursBezeichnung.setText(this.kurs.getKursBezeichnung());
		txtKursDauerTage.setText(this.kurs.getKursDauerTage() + "");
		cbBrauchtEDV.setSelected(this.kurs.getBrauchtEDV());
		txtLehrjahr.setText(this.kurs.getLehrjahr() + "");
		taBemerkung.setText(this.kurs.getBemerkung());
		
		// Select für die Ausgaben
		if (this.kurs.getAusbildungKurs() != null)
		{
			for(Ausbildung_Kurs k : kurs.getAusbildungKurs())
			{
				for (Ausbildung a : lvAusbildungenData)
				{
					if (a.getAusbildungsBezeichnung().equals(k.getAusbildung().getAusbildungsBezeichnung()))
					{
						lvAusbildungen.getSelectionModel().select(a);
						break;
					}
				}
			}
		}
	}
	
}