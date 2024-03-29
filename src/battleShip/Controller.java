package battleShip;

import java.util.Random;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller extends Application implements EventHandler<ActionEvent> {

	/**
	 * reference of the battleship model
	 */
	private Model model;
	
	/*
	 * player 1's game board
	 */
	private View viewP1;
	
	/*
	 * player 2's game board
	 */
	private View viewP2;
	
	private Agent agent;
	
	/**
	 * false during placement phase, true for the rest of the game, used for dark theme
	 */
	private boolean transition;
	
	@Override
	public void start(Stage arg0) throws Exception {
		try {
			transition = false;
			model = new Model();
			agent = new Agent(model);
			viewP1 = new View(this, mouseEvent, 1);
			viewP2 = new View(this, mouseEvent, 2);
			Scene sceneP1 = new Scene(viewP1, 500, 1000);
			Scene sceneP2 = new Scene(viewP2, 500, 1000);
			viewP1.setMessage(model.startInstructions());
			viewP2.setMessage(model.startInstructions());
			viewP1.darkTop();
			viewP2.darkAll();
			Stage stageP1 = new Stage();
			Stage stageP2 = new Stage();
			stageP1.setTitle("Player 1");
			stageP2.setTitle("AI Agent");
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

	/*
	 * activated after a button press
	 */
	@Override
	public void handle(ActionEvent arg0) {
		Button button = (Button) arg0.getSource();
		if(button.getId().equals("r")) restartGame();
		else {
		int rotation = 0;
		if(model.getGameState() == GameState.STARTP1) rotation = viewP1.getRotateState();
		else rotation = viewP2.getRotateState();
		
		String message = model.input(rotation + button.getId()); //This is where interaction with the model takes place
		viewP1.setMessage(message); //resulting message from the model
		viewP2.setMessage(message);
		
		agent.updateModel(model); //Giving a fresh copy of the recent turn/s
		while(model.getGameState() == GameState.STARTP2 || model.getGameState() == GameState.P2)
		{
			this.model = agent.action();
			viewP1.setMessage(agent.getMessage());
			viewP2.setMessage(agent.getMessage());
			
		}
		}
		updateView();
		
	}
	
	/**
	 * calls to update both of player's views
	 */
	private void updateView()
	{
		if(model.getGameState() == GameState.STARTP1) viewP1.updateViewDefense(model.getDefenseBoard(1));
		else {	
			viewP1.updateViewDefense(model.getDefenseBoard(1));
			viewP1.updateViewOffense(model.getOffenseBoard(1));
			viewP2.updateViewOffense(model.getOffenseBoard(2));
		}
		
		if(model.getGameState() != GameState.STARTP1 && !transition) transitionTheme();
		
		//show the AI game defense board at the end of the game
		if(model.getGameState() == GameState.P1WIN
				//|| model.getGameState() == GameState.STARTP1 //Helps Reset
				|| model.getGameState() == GameState.P2WIN)
			viewP2.updateViewDefense(model.getDefenseBoard(2));
	}
	
	/**
	 * restarts the colors of the boards
	 */
	private void restartGame()
	{
		model = new Model();
		agent = new Agent(model);
		viewP1.resetView();
		viewP2.resetView();
		viewP1.darkTop();
		viewP2.darkAll();
		transition = false;
		updateView();
		viewP1.setMessage(model.startInstructions());
		viewP2.setMessage(model.startInstructions());
	}
	
	private void transitionTheme()
	{
		transition = true;
		viewP1.resetTop();
		viewP2.resetView();
	}
	
	/**
	 * This method helps highlight the defensive board during the placement phase.
	 * it will gather information about what boat is about to be placed and give that to the view
	 */
	EventHandler<MouseEvent> mouseEvent = new EventHandler<MouseEvent>() { 
		   @Override 
		   public void handle(MouseEvent evt) {
			   Button button = (Button) evt.getSource();
				int row = button.getId().charAt(4) - 48;
				int col = button.getId().charAt(6) - 48;
				if(model.getGameState() == GameState.STARTP1)
				{
					if(button.getId().charAt(0) != 49) return;
					if(evt.getEventType() == MouseEvent.MOUSE_ENTERED)
						viewP1.highlightPlacement(model.getCurrentBoatLength(), row, col);
					else
						viewP1.offHighlight(model.getCurrentBoatLength(), row, col);
				}
				if(model.getGameState() == GameState.STARTP2)
				{
					
					if(button.getId().charAt(0) != 50) return;
					if(evt.getEventType() == MouseEvent.MOUSE_ENTERED)
						viewP2.highlightPlacement(model.getCurrentBoatLength(), row, col);
					else
						viewP2.offHighlight(model.getCurrentBoatLength(), row, col);
				}
		   }
		};
	
}
