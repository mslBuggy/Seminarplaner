package referentenplanung;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppReferent extends Application {

	public static void main(String[] args) {

		launch();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/referentenplanung/PlanungReferent.fxml"));
		Parent root = loader.load();
		String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
		// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
		Scene sce = new Scene(root, 800, 400);
		sce.getStylesheets().add(css);
		primaryStage.setTitle("Referentenplanung");
		primaryStage.setScene(sce);
		PlanungReferentController ctl = loader.getController();
		ctl.referentenComboBox();
		primaryStage.show();
		
	}

}
