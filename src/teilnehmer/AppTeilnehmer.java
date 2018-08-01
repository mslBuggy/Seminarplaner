package teilnehmer;

import javafx.application.Application;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


public class AppTeilnehmer extends Application{
	

	public static void main(String[] args) 
	{
		launch(AppTeilnehmer.class);
		

	}
	public void start(Stage primaryStage) throws Exception 
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Teilnehmer.fxml"));
		Parent root = loader.load();
		primaryStage.setTitle("Teilnehmerübersicht");
		Scene sc =new Scene(root);
		String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
		sc.getStylesheets().add(css);
		primaryStage.setScene(sc);
		TeilnehmerController ctl = loader.getController();
		ctl.einlesen();
		primaryStage.show();
		
	}

}
