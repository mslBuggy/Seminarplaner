package ausbildung;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Alle App<package>, <package>Controller und <package>DetailsController 
 * 	stammen von @author kohz_julian
 */
public class AppAusbildung extends Application
{

	public static void main(String[] args)
	{
		launch(AppAusbildung.class);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Ausbildung.fxml"));
		String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
		// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
		Parent root = loader.load();
		primaryStage.setTitle("Ausbildung");
		AusbildungController ctl = loader.getController();
		ctl.lesen();
		Scene sce = (new Scene(root,600,400));
		sce.getStylesheets().add(css);
		primaryStage.setScene(sce);
		primaryStage.show();
		primaryStage.setResizable(false);	
	}
}