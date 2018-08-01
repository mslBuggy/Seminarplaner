package teilnehmer;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import drucken.TeilnehmerDrucken;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Ausbildung;
import model.Teilnehmer;
import model.Teilnehmer_Ausbildung;

public class TeilnehmerController 
{
	@FXML
	private TableView<Teilnehmer>tbTeilnehmer;
	private ObservableList<Teilnehmer>tbTeilnehmerData=FXCollections.observableArrayList();
	@FXML
	private TableColumn<Teilnehmer, String> tcTeilnehmerNr;
	@FXML
	private TableColumn<Teilnehmer, String> tcVorname;
	@FXML
	private TableColumn<Teilnehmer, String> tcNachname;
	@FXML
	private TableColumn<Teilnehmer, String> tcStrasse;
	@FXML
	private TableColumn<Teilnehmer, String> tcHausNr;
	@FXML
	private TableColumn<Teilnehmer, String> tcPLZ;
	@FXML
	private TableColumn<Teilnehmer, String> tcOrt;
	@FXML
	private TableColumn<Teilnehmer, String> tcGeschlecht;
	@FXML
	private TableColumn<Teilnehmer, String> tcTelFestnetz;
	@FXML
	private TableColumn<Teilnehmer, String> tcTelMobil;
	@FXML
	private TableColumn<Teilnehmer, String> tcEMail;
	@FXML
	private TableColumn<Teilnehmer, String> tcGebDatum;
	@FXML
	private TableColumn<Teilnehmer, String> tcAktiv;
	@FXML
	private TableColumn<Teilnehmer, String> tcAustrittsDatum;
	@FXML
	private TableColumn<Teilnehmer, String> tcBemerkung;
	@FXML
	private TableColumn<Teilnehmer, String> tcAendDatum;
	@FXML
	private TableColumn<Teilnehmer, String> tcAendBenutzer;
	@FXML
	private TableColumn<Teilnehmer, String> tcAusbildung;
	
	@FXML
	private Button btDetails;
	@FXML
	private Button btNeu;
	@FXML
	private Button btDrucken;
	@FXML
	public void initialize()
	//Initialisiert die Werte
	{
		tbTeilnehmer.setItems(tbTeilnehmerData);
		tcTeilnehmerNr.setCellValueFactory(new PropertyValueFactory<>("TeilnehmerNr"));
		tcVorname.setCellValueFactory(new PropertyValueFactory<>("Vorname"));
		tcNachname.setCellValueFactory(new PropertyValueFactory<>("Nachname"));
		
		tcTelFestnetz.setCellValueFactory(new PropertyValueFactory<>("TelFestnetz"));
		tcTelMobil.setCellValueFactory(new PropertyValueFactory<>("TelMobil"));
		tcAusbildung.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Teilnehmer,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Teilnehmer, String> param) {
				// TODO Auto-generated method stub
				Teilnehmer t = param.getValue();
				String ausb = "";
				for(Teilnehmer_Ausbildung a : t.getTeilnehmerAusbildungs())
				{
					ausb += a.getAusbildung().getAusbildungsBezeichnung();
				}
			    System.out.println(param.getValue());
				return new SimpleStringProperty(ausb);
			}
		});
		tcAktiv.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Teilnehmer,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Teilnehmer, String> param) {
				// TODO Auto-generated method stub
				if (param.getValue().getAktiv())
				    return new SimpleStringProperty("ja");
				else
					return new SimpleStringProperty("nein");
			}
		});		
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
	public void einlesen()
	{
		
		this.tbTeilnehmerData.clear();
		
		EntityManagerFactory emf = 
				Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		System.out.println(em.isOpen());
		if(!em.isOpen())
		{
			System.out.println("Kein Zugriff m�glich");
			System.exit(0);
		}
		Query query = em.createQuery("select t from Teilnehmer t");
		
		List<Teilnehmer> liTeilnehmer = query.getResultList();
		this.tbTeilnehmerData.addAll(liTeilnehmer);
		
	  
		em.close();
		emf.close();
		
	}
	public TeilnehmerController()
	{
			
	}
	public void btDeatils_Click(ActionEvent event)
	{
		//Erzeugt einen Button, um genauere Details anzuzeigen
		if (tbTeilnehmer.getSelectionModel().getSelectedIndex() <0 )
		{
			Alert a = new Alert (AlertType.ERROR);
			a.setHeaderText("");
			a.setContentText("Bitte wählen sie einen Teilnehmer aus");
			a.show();
			return;
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TeilnehmerDetails.fxml"));
		//Öffnet das Fenster Teilnehmer.fxml
		try
		{
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("TeilnehmerDetails");
			stage.setScene(new Scene (root));
			Node n1 = (Node) event.getSource();
			stage.initOwner(n1.getScene().getWindow());
			stage.initModality(Modality.WINDOW_MODAL);
			TeilnehmerDetailsController ctl = loader.getController();
			Teilnehmer t =tbTeilnehmer.getSelectionModel().getSelectedItem();
			
			ctl.einlesen(t.getTeilnehmerNr());
			stage.showAndWait();
			this.einlesen();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	public void btNeu_click(ActionEvent event)
	{
		Teilnehmer t = new Teilnehmer();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TeilnehmerDetails.fxml"));
		try
		{
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("TeilnehmerDetails");
			stage.setScene(new Scene (root));
			Node n1 = (Node) event.getSource();
			stage.initOwner(n1.getScene().getWindow());
			stage.initModality(Modality.WINDOW_MODAL);
			TeilnehmerDetailsController ctl = loader.getController();
			ctl.setTeilnehmer(t);
			stage.showAndWait();
			this.einlesen();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	public void btDrucken_Click(ActionEvent event)
	{
	    TeilnehmerDrucken drucken = new TeilnehmerDrucken();
	    drucken.alleTeilnehmer(tbTeilnehmerData);
	}
	
	
}
