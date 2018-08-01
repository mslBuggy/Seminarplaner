package zeitplanung;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Zeitplanung;

public class ZeitplanungContextMenu {
	
	private ContextMenu menu = new ContextMenu();
	
	public ContextMenu contextMenu()
	{
		MenuItem mKurstermine = new MenuItem("Kurstermine");
		MenuItem mBerufsschule = new MenuItem("Berufsschule");
		MenuItem mUrlaub = new MenuItem("Urlaub");
		MenuItem mFeiertage = new MenuItem("Feiertage");
		
		menu.getItems().clear();
		menu.getItems().add(mKurstermine);
		menu.getItems().add(mBerufsschule);
		menu.getItems().add(mUrlaub);
		menu.getItems().add(mFeiertage);
		
		
		return menu;
		
	}
	
	public static class ContextMenuImpl implements EventHandler<ActionEvent>
	{
		private GridPane gridpane;
		private Zeitplanung z;
		public ContextMenuImpl() {
			// TODO Auto-generated constructor stub
		}
		
		public ContextMenuImpl(Zeitplanung z,GridPane gridpane)
		{
			this.gridpane = gridpane;
			this.z=z;
		}

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			MenuItem m = (MenuItem) event.getTarget();
			
			switch (m.getText().toLowerCase())
			{
			case "kurstermine" :
				this.aufrufKurstermine();
				break;
			}
		}
		
		public void aufrufKurstermine()
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/zeitplanung/TerminDetails.fxml"));
			try {
				Parent root = loader.load(); // Die Maske, die uns geliefert wird
				// Neues Fenster erzeugen
				Stage stage = new Stage();
				stage.setTitle("Termindetails");
				Scene sce = new Scene(root,382,422);
				String css = this.getClass().getResource("/layouts/Application.css").toExternalForm();
				// Stylesheets müssen als URL umgewandelt werden, daher toExternalForm()
				sce.getStylesheets().add(css);
				stage.setScene(sce);
				stage.initModality(Modality.WINDOW_MODAL);
				// Festlegen des Owners
				//Node node = (Node) event.getSource();
				//Scene sc = node.getScene();
				
				//Window w = sc.getWindow();
			    //stage.initOwner(w);
				// Zur Übergabe benötigen wir hier den Controller der abhängigen Maske
				// Beschaffung: deswegen haben wir den FXMLLoader instanziert
				Node n = getNodeByRowColumnIndex(1, 0, gridpane);
			
				Label l = (Label) n;
				
				LocalDate da = LocalDate.parse(l.getText(),DateTimeFormatter.ofPattern("dd.MM.yyyy"));
				
				n = getNodeByRowColumnIndex(1, 1, gridpane);
				l = (Label) n;
				
				LocalDate de = LocalDate.parse(l.getText(),DateTimeFormatter.ofPattern("dd.MM.yyyy"));

				
				TerminDetailsController ctl = loader.getController();
				
				// ctl.setZeitplanung(z,da,de);
				stage.showAndWait();
			}
			catch (Exception e)
			{
				
			}

		}
		
		public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
		    Node result = null;
		    ObservableList<Node> childrens = gridPane.getChildren();

		    for (Node node : childrens) {
		        if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
		            result = node;
		            break;
		        }
		    }

		    return result;
		}
		
	}

}
