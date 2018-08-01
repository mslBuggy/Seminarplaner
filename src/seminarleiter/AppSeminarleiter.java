package seminarleiter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppSeminarleiter extends Application {

	public static void main(String[] args) {

		launch(AppSeminarleiter.class);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Seminarleiter.fxml"));
		String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
		// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
		Parent root = loader.load();
		primaryStage.setTitle("Seminarleiter");
		SeminarleiterController ctl = loader.getController();
		ctl.lesen();
		Scene sce = (new Scene(root));
		sce.getStylesheets().add(css);
		primaryStage.setScene(sce);
		primaryStage.show();
		primaryStage.setResizable(false);
	}

}
