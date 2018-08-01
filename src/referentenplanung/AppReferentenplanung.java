package referentenplanung;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppReferentenplanung extends Application{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/referentenplanung/PlanungReferent.fxml"));
		Parent root = loader.load();
		primaryStage.setTitle("Referentenplanung");
		// Zuordnen der CSS-Datei
		// Achtung: CSS der FXML wird nicht aus SceneBuilder synchronisiert!!
		Scene sc = new Scene(root,800,480);
		//String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
		// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
		//sc.getStylesheets().add(css);
		//ZeitplanTabelleController ctl = loader.getController();
		//ctl.lesen();
		
		primaryStage.setScene(sc);
		primaryStage.show();
		
	}

}
