package zeitplanung;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Ausbildung;
import model.AusbildungsfestTermine;
import model.Kurs;
import model.Raum;
import model.Zeitplanung;

/**
 * 
 * @author lara-bisch_manuel
 * Controllerklasse zum Bearbeiten der ausgegliederten Festtermine
 * (d.h. Berufsschule und Betriebsurlaub, evtl auch Feiertage -- diese sollen allerdings
 *  größtenteils vorberechnet werden)
 * 
 */
public class FestTerminDetailsController {
	
	@FXML
	private TextField txtBezeichnung;
	
	@FXML
	private DatePicker dpAnfang;
	@FXML
	private DatePicker dpEnde;
	
	/**
	 * Die ComboBox cbTerminArt ist zur Bearbeitung gesperrt,
	 * da Terminart beim Zugriff auf dieses Fenster bereits mit übergeben werden soll.
	 * Eventuell wäre hier ein anderes Steuerelement besser,
	 * um Benutzer nicht zu verwirren.
	 */
	@FXML
	private ComboBox<String> cbTerminArt;
	private ObservableList<String> cbTerminArtData = FXCollections.observableArrayList();
	@FXML
	private ComboBox<String> cbAusbildung;
	private ObservableList<String> cbAusbildungData = FXCollections.observableArrayList();
	@FXML
	private ComboBox<String> cbLehrjahr;
	private ObservableList<String> cbLehrjahrData = FXCollections.observableArrayList();
	

	// TODO: Entsprechende onAction-Methoden für Buttons schreiben
	// 		 (können vermutlich aus Klassen wie TerminDetailsController übernommen
	//		 und angepasst werden)
	
	// TODO: Funktion zum Übernehmen Festtermine einer anderen Ausbildung
	
	@FXML
	private Button btLoeschen;
	@FXML
	private Button btSpeichern;
	@FXML
	private Button btEnde;
	
	private Ausbildung a;
	private AusbildungsfestTermine ft;
	
	@FXML
	public void initialize() {
		cbTerminArtData.addAll("Berufsschule","Betriebsurlaub","Feiertag", "Sonstiger Termin");
		cbTerminArt.setItems(cbTerminArtData);
		
		cbLehrjahrData.addAll("1","2","3");
		cbLehrjahr.setItems(cbLehrjahrData);
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a FROM Ausbildung a");
		List<Ausbildung> liAusbildung = query.getResultList();
		for (Ausbildung ausbildung : liAusbildung) {
			cbAusbildungData.add(ausbildung.getAusbildungsBezeichnung());
		}
		cbAusbildung.setItems(cbAusbildungData);
	}
	
	/**
	 * Wird bei Auswahl eines Anfangsdatums ausgeführt.
	 * Setzt Enddatum auf Anfangsdatum.
	 */
	public void dpAnfang_onSelect(ActionEvent event){
		LocalDate ldEnd = dpAnfang.getValue();
		dpEnde.setValue(ldEnd);
	}
	
	/**
	 * Wird beim Drücken auf den Knopf btSpeichern ausgeführt.
	 * Überprüft, ob Wert in dpEnde >= dpAnfang ist, und speichert in diese Fall den Termin.
	 * Ansonsten wird eine Fehlermeldung ausgegeben.
	 * @param event
	 */
	public void btSpeichern_Click(ActionEvent event){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		if (ft != null){
			try {
				ft = em.find(AusbildungsfestTermine.class, ft.getLfdnr());
				
			} catch (NoResultException e) {
				System.out.println("Kein Objekt in Datenbank");
			}
		} else
			ft = new AusbildungsfestTermine();
		ft.setDatumvon(Date.from(dpAnfang.getValue().atStartOfDay().atZone(ZoneId.of("Europe/Berlin")).toInstant()));
		ft.setDatumbis(Date.from(dpEnde.getValue().atStartOfDay().atZone(ZoneId.of("Europe/Berlin")).toInstant()));
		ft.setJahr(dpEnde.getValue().getYear());
		ft.setArttermin(cbTerminArt.getSelectionModel().getSelectedIndex() + 1);
		Query query = em.createQuery("SELECT a FROM Ausbildung a");
		List<Ausbildung> liAusbildung = query.getResultList();
		if(ft.getArttermin() == 1){ // Ausbildung + Lehrjahr nur für Berufsschule relevant
			try {
				ft.setAusbildung(liAusbildung.get(cbAusbildung.getSelectionModel().getSelectedIndex()));
				ft.setLehrjahr(cbLehrjahr.getSelectionModel().getSelectedIndex() +1);
			} catch (ArrayIndexOutOfBoundsException e) {
				Alert a = new Alert(AlertType.WARNING);
				a.setTitle("Kein Ausbildungsgang/Lehrjahr ausgewählt");
				a.setHeaderText("");
				a.setContentText("Bitte wählen Sie einen Ausbildungsgang und ein Lehrjahr aus, bevor Sie diesen Termin speichern.");
				a.showAndWait();
			}
			
		}
		ft.setBezeichnung(txtBezeichnung.getText());
			
		
		// TODO: Fehlerprüfung:
		// TODO: 	Anfangs, Enddatum in richtiger Reihenfolge?
		
		if((dpEnde.getValue().isAfter(dpAnfang.getValue())) || dpEnde.getValue().isEqual(dpAnfang.getValue())){
			if(!em.getTransaction().isActive())
				em.getTransaction().begin();
			em.persist(ft);
			em.getTransaction().commit();

			// DONE: Nach erfolgreichem Speichern, Maske schließen
			((Stage) (btEnde.getScene().getWindow())).close();
		}
		else
		{
			if(!em.getTransaction().isActive())
				em.getTransaction().begin();
			em.getTransaction().rollback();
			Alert a = new Alert(AlertType.ERROR);
			String fehlerDetail = "\nEin Fehler in Datum Anfang, Ende.";
			a.setHeaderText("");
			a.setContentText("Bitte korrigieren Sie die rot markierten Einträge. Details:" + fehlerDetail);
			a.show();
			return;
		}
	}
	
	public void btEnde_Click(ActionEvent event){
		if (ft == null){
			Alert a = new Alert(AlertType.CONFIRMATION);
			a.setHeaderText("");
			a.setContentText("Dieser neu angelegte Termin wurde noch nicht gespeichert.\n"
					+ "Wollen Sie ihn verwerfen?");
			if(a.showAndWait().get() == ButtonType.CANCEL)
				return;
		}
		((Stage) (btEnde.getScene().getWindow())).close();
	}
	
	public void setAusbildungsfestTermine(AusbildungsfestTermine ft){
		this.ft = ft;
		txtBezeichnung.setText(ft.getBezeichnung());
		cbTerminArt.getSelectionModel().select(ft.getArttermin());
		if(ft.getArttermin() == 0){
			cbAusbildung.getSelectionModel().select(ft.getAusbildung().getAusbildungsBezeichnung());
			cbLehrjahr.getSelectionModel().select(ft.getLehrjahr() - 1);
		}
		dpAnfang.setValue(date2LocalDate(ft.getDatumvon()));
		dpEnde.setValue(date2LocalDate(ft.getDatumbis()));
		
	}
	
	public LocalDate date2LocalDate(Date date){
		return date.toInstant().atZone(ZoneId.of("Europe/Berlin")).toLocalDate();
	}

	public void setAusbildungsfestTermine(AusbildungsfestTermine aft, LocalDate da, LocalDate de, int typ) {
		// TODO Method created by lara-bisch_manuel on 10.01.2018 11:45:30
		this.ft = aft;
		dpAnfang.setValue(da);
		dpEnde.setValue(de);
		cbTerminArt.getSelectionModel().select(typ);
	}

}
