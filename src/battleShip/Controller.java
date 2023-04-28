package battleShip;

import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller extends Application implements EventHandler<ActionEvent> {

	private Model model;
	private View view;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			model = new Model();
			view = new View(this);
			Scene scene = new Scene(view, 1000, 1000);
			primaryStage.setScene(scene); //stage is a window
			primaryStage.show(); //puts it on the screen
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("You done messed up");
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void handle(ActionEvent arg0) {
		Button button = (Button) arg0.getSource();
		String message = model.input(button.getId());
		view.setMessage(message);
		updateView();
	}
	
	private void updateView()
	{
		view.updateView(model.getDefenseBoard(new Player()));
	}
	
}
