package zeitplanung;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppZeitplanung extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Method created by lara-bisch_manuel on 25.04.2017 13:21:37
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/zeitplanung/ZeitplanTabelle.fxml"));
		Parent root = loader.load();
		primaryStage.setTitle("Zeitplanung");
		// Zuordnen der CSS-Datei
		// Achtung: CSS der FXML wird nicht aus SceneBuilder synchronisiert!!
		Scene sc = new Scene(root,800,480);
		String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
		// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
		sc.getStylesheets().add(css);
		ZeitplanTabelleController ctl = loader.getController();
		ctl.lesen();
		
		primaryStage.setScene(sc);
		primaryStage.show();

	}

	public static void main(String[] args) {
		// TODO Method created by lara-bisch_manuel on 25.04.2017 13:21:37
		//System.out.println("hallo");
		launch(AppZeitplanung.class);

	}

}
