package ausbildung;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import kurs.KursDetailsController;

public class AusbildungstabelleController {
	
	@FXML
	private Button btDetails;
	
	@FXML
	void btDetails_Click(ActionEvent event)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ausbildung/Ausbildungdetails.fxml"));
		try {
			Parent root = loader.load(); // Die Maske, die uns geliefert wird
			// Neues Fenster erzeugen
			Stage stage = new Stage();
			
			stage.setTitle("Ausbildungsdetails");
			Scene sce = new Scene(root,600,600);
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
			AusbildungDetailsController ctl = loader.getController();
			
			
			stage.showAndWait();
			
		} catch (IOException e) {
			// TODO: handle exception^
			e.printStackTrace();
		}
	}
	

}
