package zeitplanung;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppPlanungAusbildung extends Application {

	public static void main(String[] args) {

		launch();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/zeitplanung/PlanungAusbildung.fxml"));
		Parent root = loader.load();
		
		primaryStage.setTitle("Termine für Ausbildung");
		Scene sc = new Scene(root, 800, 400);
		primaryStage.setScene(sc);
		String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
		// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
		sc.getStylesheets().add(css);
		PlanungAusbildungController ctl = loader.getController();
		ctl.ausbildungenComboBox();
		primaryStage.show();
		
	}

}
