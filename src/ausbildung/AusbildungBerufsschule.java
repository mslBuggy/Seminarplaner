package ausbildung;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import hauptsteuerung.ZentraleDienste;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Ausbildung;
import model.AusbildungsfestTermine;
import model.Kurs;

public class AusbildungBerufsschule {
	
	public AusbildungBerufsschule() {
		// TODO Auto-generated constructor stub
		cbLehrjahrData.addAll(1,2,3);
	}
	
	private List<Ausbildung> liAusbildung;
	private int aktAusbildungsnr=2;
	
	@FXML
	private ComboBox<Integer> cbLehrjahr;
	private ObservableList<Integer> cbLehrjahrData = FXCollections.observableArrayList();
	
	@FXML
	private ComboBox<String> cbUebernahme;
	private ObservableList<String> cbUebernahmeData = FXCollections.observableArrayList();
	
	@FXML
	private Button btNeu;
	
	
	
	@FXML
	private Button btSpeichern;
	
	@FXML
	private Button btCancel;
	
	 @FXML
	 private TableView<AusbildungsfestTermine> tbTermine;
	 private ObservableList<AusbildungsfestTermine> tbTermineData =  
			 FXCollections.observableArrayList();

	 

	 @FXML
	 private TableColumn<AusbildungsfestTermine, String> tbVon;
	    
	 @FXML
	 private TableColumn<AusbildungsfestTermine, String> tbBezeichnung;

	  @FXML
	  private TableColumn<AusbildungsfestTermine, String> tbBis;
	
	
	@FXML
	void initialize()
	{
	   btNeu.setDisable(true);
	   cbUebernahme.setDisable(true);
	   btSpeichern.setDisable(true);
	   
	  cbLehrjahr.setItems(cbLehrjahrData);
	  cbUebernahme.setItems(cbUebernahmeData);
	  
	  tbTermine.setItems(tbTermineData);
	  tbTermine.setPlaceholder(new Label("Bitte wählen Sie erst das Lehrjahr aus"));
	  tbBezeichnung.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));
	  
	  
	  tbVon.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AusbildungsfestTermine,String>, ObservableValue<String>>() {

		@Override
		public ObservableValue<String> call(CellDataFeatures<AusbildungsfestTermine, String> param) {
			// TODO Auto-generated method stub
			return new SimpleStringProperty(ZentraleDienste.dateToString(param.getValue().getDatumvon()));
		}
	});
	  tbBis.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AusbildungsfestTermine,String>, ObservableValue<String>>() {

		@Override
		public ObservableValue<String> call(CellDataFeatures<AusbildungsfestTermine, String> param) {
			// TODO Auto-generated method stub
			return new SimpleStringProperty(ZentraleDienste.dateToString(param.getValue().getDatumbis()));
		}
	});
	  
	}
	
	@FXML
	void cbLehrjahr_Click(ActionEvent event)
	{
		btNeu.setDisable(false);
		btSpeichern.setDisable(false);
		cbUebernahme.setDisable(false);
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		Query query = 
				em.createQuery("select at from AusbildungsfestTermine at where at.ausbildung.ausbildungsNr = "+aktAusbildungsnr+
						" and at.lehrjahr = "+cbLehrjahr.getSelectionModel().getSelectedItem()+
						" and at.arttermin = 1");
		List<AusbildungsfestTermine> lifestTermine = query.getResultList();
		if (lifestTermine.size() == 0)
			tbTermine.setPlaceholder(new Label("Es liegen keine Termine vor"));
		else
		{
			tbTermineData.clear();
			tbTermineData.addAll(lifestTermine);
		}
			
		
	}
	
	@FXML
	void cbUebernahme_Click(ActionEvent event)
	{
		
		Ausbildung a = liAusbildung.get(cbUebernahme.getSelectionModel().getSelectedIndex());
		if (a.getAusbildungsNr() == aktAusbildungsnr)
		{
			Alert message = new Alert(AlertType.INFORMATION);
		     message.setHeaderText("Der ausgewählte Ausbildungsgang entspricht dem bearbeiteten Ausbildungsgang");
			 message.setContentText("Eine Übernahme ist daher nicht möglich");
		     message.showAndWait();
			return;
		}
		
		int uebernahme = a.getAusbildungsNr();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		Query query = 
				em.createQuery("select at from AusbildungsfestTermine at where at.ausbildung.ausbildungsNr = "+uebernahme+
						" and at.lehrjahr = "+cbLehrjahr.getSelectionModel().getSelectedItem()+
						" and at.arttermin = 1");
		List<AusbildungsfestTermine> lifestTermine = query.getResultList();
		
		if (lifestTermine.size()== 0)
		{
			Alert message = new Alert(AlertType.INFORMATION);
		     message.setHeaderText("Der ausgewählte Ausbildungsgang enthält keine Termine");
			 message.setContentText("Eine Übernahme ist daher nicht möglich");
		     message.showAndWait();
		}
		else
		{
			tbTermineData.clear();
			tbTermineData.addAll(lifestTermine);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void lesen(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Seminarplaner");
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("select a from Ausbildung a");
		liAusbildung = query.getResultList();
		for(Ausbildung a : liAusbildung)
		{
			
			cbUebernahmeData.add(a.getAusbildungsBezeichnung());
		}
		
	}
	

}
