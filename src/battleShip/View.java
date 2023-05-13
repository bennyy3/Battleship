package battleShip;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class View extends BorderPane{
	
	/**
	 * Directions and messages from the model about the game
	 */
	private Text message;
	
	/**
	 * The upper grid of Battleship, filled with button objects
	 */
	GridPane offGrid; //TODO private
	
	/**
	 * The bottom grid of Battleship, filled with button objects
	 */
	GridPane defGrid;
	
	/**
	 * a grid of buttons assigned to the defGrid
	 */
	Button[][] defButton;
	
	/**
	 * a grid of buttons assigned to the offGrid
	 */
	Button[][] offButton;
	
	/**
	 * Button that will rotate the direction of a ship placement
	 */
	Button rotate;
	
	/**
	 * Keeps track of the rotation direction. 0 for horizontal and 1 for vertical
	 */
	private int rotateState;
	
	/**
	 * these strings are for style of the View and make the code easier to read and keep track of
	 */
	String emptyDefenseStyle = "-fx-border-color: black; -fx-background-color: #e591ff; ";
	String emptyOffenseStyle = "-fx-border-color: black; -fx-background-color: #ffd89e; ";
	String defenseBoatStyle = "-fx-border-color: black; -fx-background-color: #a3d3e3; ";
	String missStyle = "-fx-border-color: black; -fx-background-color: #c4c4c4; ";
	String hitStyle = "-fx-border-color: black; -fx-background-color: #ffef94; ";
	String sunkStyle = "-fx-border-color: black; -fx-background-color: #ed4e4e; ";
	String rotateStyle = "-fx-background-color: #eec4ff; ";
	
	
	/**
	 * Constructor for View
	 * @param event, the action event handler in the Controller. Handles button presses
	 * @param mouseEvent, the mouse event handler in the Controller. Used for hover highlighting
	 * @param id, used to track p1 and p2
	 * 
	 * NOTE: the button id is used to send its information as input to the model
	 * this is not the most ideal way to transfer information, but here we are
	 * example:
	 *	 defButton[row][col].setId(id+"def"+ row + "," + col);
	 * id 1 means player1, id 2 means player 2
	 * def means this is a defensive button
	 * row and col identify its location
	 * "1def0,0" means its player1 defensive grid button at location (0,0)
	*/
	public View(EventHandler<ActionEvent> event, EventHandler<MouseEvent> mouseEvent, int id)
	{
		super();
		this.rotateState = 0;
		offGrid = new GridPane();
		defGrid = new GridPane();
		defButton = new Button[10][10];
		offButton = new Button[10][10];
		
		for(int row = 0; row < 10; row++)
		{
			for(int col = 0; col < 10; col++)
			{
				defButton[row][col] = new Button();
				defButton[row][col].setId(id+"def"+ row + "," + col);
				defButton[row][col].setText(" ");
				defButton[row][col].setPrefHeight(40);
				defButton[row][col].setPrefWidth(40);
				defButton[row][col].setStyle(defenseBoatStyle);
				defButton[row][col].setOnAction(event);
				defButton[row][col].setOnMouseEntered(mouseEvent);
				defButton[row][col].setOnMouseExited(mouseEvent);
				defGrid.add(defButton[row][col], col, row);
				
				offButton[row][col] = new Button();
				offButton[row][col].setId(id+"off"+ row + "," + col);
				offButton[row][col].setText(" ");
				offButton[row][col].setPrefHeight(40);
				offButton[row][col].setPrefWidth(40);
				offButton[row][col].setStyle(emptyOffenseStyle);
				offButton[row][col].setOnAction(event);
				offGrid.add(offButton[row][col], col, row);
			}
		}
		
		VBox vbox = new VBox();
		vbox.getChildren().add(offGrid);
		vbox.getChildren().add(defGrid);
		message = new Text();
		rotate = new Button("rotate");
		rotate.setPrefHeight(80);
		rotate.setPrefWidth(100);
		rotate.setOnAction((evt) -> {
			toggleRotate();
		});
		rotate.setStyle(rotateStyle);
		rotate.setId("rotate");
		setCenter(vbox);
		setBottom(message);
		setRight(rotate);
	}
	
	/**
	 * setter for the message attribute
	 * @param message
	 */
	public void setMessage(String message)
	{
		this.message.setText(message);
	}
	
	/**
	 * updates the view with the new information from the model/controller
	 * @param defenseBoard, will either be empty or have a string identifying defense boats
	 * @param offenseBoard, will contain either " ", 'H', 'M', 'S'
	 */
	public void updateViewDefense(String[][] defenseBoard)
	{
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				defButton[i][j].setText(defenseBoard[i][j]);
				if(!defenseBoard[i][j].equals(" ")) defButton[i][j].setStyle(emptyDefenseStyle);
				else defButton[i][j].setStyle(defenseBoatStyle);
			}
		}
	}
	
	public void updateViewOffense(String[][] offenseBoard)
	{
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				offButton[i][j].setText(offenseBoard[i][j]);
				if(offenseBoard[i][j].equals("M")) offButton[i][j].setStyle(missStyle);
				if(offenseBoard[i][j].equals("H")) offButton[i][j].setStyle(hitStyle);
				if(offenseBoard[i][j].equals("S")) offButton[i][j].setStyle(sunkStyle);
			}
		}
	}
	
	/**
	 * This method toggles the rotateState attribute between 0 and 1
	 */
	private void toggleRotate()
	{
		if(this.rotateState == 1)
		{
			this.rotateState = 0;
		}
		else
		{
			this.rotateState = 1;
		}
	}
	
	/**
	 * 
	 * @return the rotate state. 0 for horizontal, 1 for vertical
	 */
	public int getRotateState()
	{
		return this.rotateState;
	}

	/**
	 * This method highlights the squares that new boat would be placed in
	 * @param currentBoatLength, used to know how many squares to highlight
	 * @param row, identifies the position of the boat
	 * @param col
	 */
	public void highlightPlacement(int currentBoatLength, int row, int col) {
		int rotate = this.getRotateState();
		for(int i = 0; i < currentBoatLength; i++)
		{
			if(rotate == 0)
			{
				if(i+col <= 9) defButton[row][col+i].setStyle(missStyle);
			}
			if(rotate == 1)
			{
				if(i+row <= 9) defButton[row+i][col].setStyle(missStyle);
			}
		}
	}

	/**
	 * This is a complimentary method of highlightPlacement that un-highlights after the mouse moves
	 * @param currentBoatLength, used to know how many squares to un-highlight
	 * @param row, identifies the position of the boat
	 * @param col
	 */
	public void offHighlight(int currentBoatLength, int row, int col) {
		int rotate = this.getRotateState();
		for(int i = 0; i < currentBoatLength; i++)
		{
			if(rotate == 0)
			{
				if(i+col <= 9)
				{
					if(!defButton[row][col+i].getText().equals(" "))
						defButton[row][col+i].setStyle(emptyDefenseStyle);
					else defButton[row][col+i].setStyle(defenseBoatStyle);
				}
			}
			if(rotate == 1)
			{
				if(i+row <= 9)
				{
					if(!defButton[row+i][col].getText().equals(" "))
						defButton[row+i][col].setStyle(emptyDefenseStyle);
					else defButton[row+i][col].setStyle(defenseBoatStyle);
				}
			}
		}
		
	}
}
