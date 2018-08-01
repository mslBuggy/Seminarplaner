package teilnehmer;

import javafx.fxml.FXML;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Ausbildung;
import model.Ausbildung_Kurs;
import model.Teilnehmer;
import model.Teilnehmer_Ausbildung;

public class TeilnehmerDetailsController 
{
	@FXML
	private TextField txtTeilnehmerNr;
	@FXML
	private TextField txtVorname;
	@FXML
	private TextField txtNachname;
	@FXML
	private TextField txtStrasse;
	@FXML
	private TextField txtHausNr;
	@FXML
	private TextField txtPLZ;
	@FXML
	private TextField txtOrt;
	@FXML
	private ComboBox<String> txtGeschlecht;
	@FXML
	private TextField txtTelFestnetz;
	@FXML
	private TextField txtTelMobil;
	@FXML
	private TextField txtEMail;
	@FXML
	private TextField txtGebDatum;
	@FXML
	private CheckBox txtAktiv;
	@FXML
	private TextField txtAustrittsDatum;
	@FXML
	private TextField txtBemerkung;
	@FXML
	private TextField txtAendDatum;
	@FXML
	private TextField txtAendBenutzer;
	@FXML
	private ListView lvAusbildung;
	@FXML
	private Button btSpeichern;
	@FXML
	private Button btEnde;
	private Teilnehmer teilnehmer = null;
	private EntityManagerFactory emf;
	private EntityManager em;
	@FXML
	private ComboBox<Ausbildung> cbAusbildung;
	private ObservableList<Ausbildung> cbAusbildungData = FXCollections.observableArrayList();
	
	public void initialize()
	//Basis-Konstruktor
	{
		emf = 
				Persistence.createEntityManagerFactory("Seminarplaner");
		em = emf.createEntityManager();
		System.out.println(em.isOpen());
		if(!em.isOpen())
		{
			System.out.println("Kein Zugriff m�glich");
			System.exit(0);
		}
		this.txtGeschlecht.getItems().add("m");
		this.txtGeschlecht.getItems().add("w");
		
		this.cbAusbildung.setConverter(new StringConverter<Ausbildung>() {
			
			@Override
			public String toString(Ausbildung object) {
				// TODO Auto-generated method stub
				return object.getAusbildungsBezeichnung();
			}
			
			@Override
			public Ausbildung fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		
		
		
	}
	public void einlesen(int teilnehmernr)
	{
		Query querya = em.createQuery("Select a from Ausbildung a");
		
		this.cbAusbildungData.addAll(querya.getResultList());
		
		this.cbAusbildung.setItems(cbAusbildungData);
		
		Query query = em.createQuery("select t from Teilnehmer t where t.teilnehmerNr = "+teilnehmernr);
		
		this.setTeilnehmer( (Teilnehmer) query.getSingleResult());
	    
		
	}
	
	public void btEnde_Click(ActionEvent event)
	{
		//Erstellt den Ende-Button, um das Teilnehmer-Details-Fenster zu schlie�enm
		Stage stage = (Stage) btEnde.getScene().getWindow();
		stage.close();
	}
	public void setTeilnehmer(Teilnehmer teilnehmer) 
	{
		//�bernimmt die Werte von Teilnehmer
		this.teilnehmer=teilnehmer;
		this.txtTeilnehmerNr.setText(this.teilnehmer.getTeilnehmerNr()+"");
		this.txtVorname.setText(this.teilnehmer.getVorname());
		this.txtNachname.setText(this.teilnehmer.getNachname());
		this.txtStrasse.setText(this.teilnehmer.getStrasse());
		this.txtHausNr.setText(this.teilnehmer.getHausNr());
		this.txtPLZ.setText(this.teilnehmer.getPlz());
		this.txtOrt.setText(this.teilnehmer.getPlz());
		this.txtGeschlecht.getSelectionModel().select(String.valueOf(this.teilnehmer.getGeschlecht()));	
		
		this.txtTelFestnetz.setText(this.teilnehmer.getTelFestnetz());
		this.txtTelMobil.setText(this.teilnehmer.getTelMobil());
		this.txtEMail.setText(this.teilnehmer.getEMail());
		if (this.teilnehmer.getGebDatum() != null)
			this.txtGebDatum.setText(dateToString(this.teilnehmer.getGebDatum()));
		this.txtAktiv.setSelected(this.teilnehmer.getAktiv());
		if (this.teilnehmer.getAustrittsDatum() != null)
			this.txtAustrittsDatum.setText(dateToString(this.teilnehmer.getAustrittsDatum()));
		if (this.teilnehmer.getTeilnehmerAusbildungs().size() > 0)
		{
			Teilnehmer_Ausbildung ak = this.teilnehmer.getTeilnehmerAusbildungs().get(0);
			this.cbAusbildung.getSelectionModel().select(ak.getAusbildung());
		}
		
		this.txtBemerkung.setText(this.teilnehmer.getBemerkung());
		
	}
	public void btSpeichern_Click(ActionEvent event)
	{
		//Erstellt einen Button mit Speicher-Funktion
		System.out.println(this.teilnehmer);
		this.teilnehmer.setTeilnehmerNr(Integer.parseInt(this.txtTeilnehmerNr.getText()));
		this.teilnehmer.setVorname(this.txtVorname.getText());
		this.teilnehmer.setNachname(this.txtNachname.getText());
		this.teilnehmer.setStrasse(this.txtStrasse.getText());
		this.teilnehmer.setHausNr(this.txtHausNr.getText());
		this.teilnehmer.setPlz(this.txtPLZ.getText());
		this.teilnehmer.setOrt(this.txtOrt.getText());
		this.teilnehmer.setGeschlecht(this.txtGeschlecht.getSelectionModel().getSelectedItem().charAt(0));
		this.teilnehmer.setTelFestnetz(this.txtTelFestnetz.getText());
		this.teilnehmer.setTelMobil(this.txtTelMobil.getText());
		this.teilnehmer.setEMail(this.txtEMail.getText());
		this.teilnehmer.setGebDatum(stringToDate(this.txtGebDatum.getText()));
		this.teilnehmer.setAktiv(this.txtAktiv.isSelected());
		this.teilnehmer.setAustrittsDatum(stringToDate(this.txtAustrittsDatum.getText()));
		this.teilnehmer.setBemerkung(this.txtBemerkung.getText());
	
		this.teilnehmer.setAendDatum(Date.from(Instant.now()));
		this.teilnehmer.setAendBenutzer(System.getenv("username"));
		
		
		
		em.getTransaction().begin();
		em.persist(this.teilnehmer);
		em.getTransaction().commit();
		
	
		
		
	}
	public void cbAusbildung_Click(ActionEvent event)
	{
		System.out.println(this.cbAusbildung.getSelectionModel().getSelectedItem());
		Teilnehmer_Ausbildung tna = new Teilnehmer_Ausbildung();
		tna.setAusbildung(this.cbAusbildung.getSelectionModel().getSelectedItem());
		tna.setTeilnehmer(this.teilnehmer);
		
		// Löschen aller vorhandenen Verbindungen für diesen Teilnehmer
	  
		
		
		em.getTransaction().begin();
		
		//em.createQuery("Delete from Teilnehmer_Ausbildung a where a.teilnehmer.teilnehmerNr = "+this.teilnehmer.getTeilnehmerNr());
	    em.remove(this.teilnehmer.getTeilnehmerAusbildungs().get(0));
		em.persist(tna);
		em.getTransaction().commit();
			
			
			
			
	    
	}
	private String dateToString(Date datum)
	{
		//Wandelt ein Datum in einen String um
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String dat = df.format(datum);
		return dat;
	}
	private Date stringToDate(String datum)
	{
		//Wandelt einen String in ein Datum um
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		try
		{
			Date d = df.parse(datum);
			return d;
		}
		catch (ParseException e)
		{
			return null;
		}
	}
}
