package referent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class AppReferent extends Application {

	public static void main(String[] args) {

		launch(AppReferent.class);

	}

	//getClass().getResource("Referent.fxml"));
	
	public static Stage stage;
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Referent.fxml"));
		String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
		// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
		Parent root = loader.load();
		primaryStage.setTitle("Referent");
		ReferentController ctl = loader.getController();
		ctl.lesen();
		Scene sce = (new Scene(root, 610, 440));
		sce.getStylesheets().add(css);
		primaryStage.setScene(sce);
		primaryStage.show();
		primaryStage.setResizable(false);
		
	}

}
