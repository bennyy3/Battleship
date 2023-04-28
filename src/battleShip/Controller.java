package battleShip;

import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller extends Application implements EventHandler<ActionEvent> {

	private Model model;
	private View viewP1;
	private View viewP2;
	
	@Override
	public void start(Stage arg0) throws Exception {
		try {
			model = new Model();
			viewP1 = new View(this, 1);
			viewP2 = new View(this, 2);
			Scene sceneP1 = new Scene(viewP1, 500, 1000);
			Scene sceneP2 = new Scene(viewP2, 500, 1000);
			viewP1.setMessage(model.startInstructions());
			viewP2.setMessage(model.startInstructions());
			Stage stageP1 = new Stage();
			Stage stageP2 = new Stage();
			stageP1.setTitle("Player 1");
			stageP2.setTitle("Player 2");
			stageP1.setScene(sceneP1); //stage is a window
			stageP2.setScene(sceneP2);
			stageP1.show(); //puts it on the screen
			stageP2.show();
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Battle Ship");
			alert.setContentText("Press Enter To Start");
			alert.showAndWait();
			
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
		viewP1.setMessage(message);
		viewP2.setMessage(message);
		updateView();
	}
	
	private void updateView()
	{
		viewP1.updateView(model.getDefenseBoard(1), model.getOffenseBoard(1));
		viewP2.updateView(model.getDefenseBoard(2), model.getOffenseBoard(2));
	}
	
}
