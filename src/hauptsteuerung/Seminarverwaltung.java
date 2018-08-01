package hauptsteuerung;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Seminarverwaltung extends Application {
	
	public static Stage mdi;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(Seminarverwaltung.class);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		// liefern des aktuelles Benutzers von Windows
		 //System.out.println(System.getenv("userName"));
		
		  FXMLLoader loader = new  FXMLLoader(getClass().getResource("Hauptfenster.fxml"));
	      
		  Parent root = loader.load();
		  
		  primaryStage.setTitle("Seminarverwaltung");
		  mdi = primaryStage;
		  
		  Scene sc = new Scene(root);
		  String s = this.getClass().getResource("Application.css").toExternalForm();
		  sc.getStylesheets().add(s);
		  primaryStage.setMaximized(true);
		  primaryStage.setResizable(true);
		 
		  primaryStage.setScene(sc);
		  
		  // liefern des Controllers
		  HauptController h = loader.getController();
		  
		  // Ende Abfrage das kreuz
		  primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
			    if (h.endeAbfrage())
			    	System.exit(0);
			    else
			    	event.consume(); // Abbruch des Schliessen vorganges
				
			}
		});
		  
		  primaryStage.show();
		  
		  
		
	}

}
