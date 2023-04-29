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
	private Text message;
	GridPane offGrid;
	GridPane defGrid;
	Button[][] defButton;
	Button[][] offButton;
	Button rotate;
	private int rotateState;
	
	public View(EventHandler<ActionEvent> event, EventHandler<MouseEvent> mouseEvent, int id)
	{
		super();
		this.rotateState = 1;
		offGrid = new GridPane();
		defGrid = new GridPane();
		defButton = new Button[10][10];
		offButton = new Button[10][10];
		
		for(int row = 0; row < 10; row++)
		{
			for(int col = 0; col < 10; col++)
			{
				defButton[row][col] = new Button();
				defButton[row][col].setId(id+"def"+ row + "," + col); //TODO needs rotate state to update
				defButton[row][col].setPrefHeight(40);
				defButton[row][col].setPrefWidth(40);
				defButton[row][col].setStyle("-fx-border-color: black; -fx-background-color: #a3d3e3; ");
				defButton[row][col].setOnAction(event);
				defButton[row][col].setOnMouseEntered(mouseEvent);
				defButton[row][col].setOnMouseExited(mouseEvent);
				defGrid.add(defButton[row][col], col, row);
				
				offButton[row][col] = new Button();
				offButton[row][col].setId(id+"off"+ row + "," + col);
				offButton[row][col].setPrefHeight(40);
				offButton[row][col].setPrefWidth(40);
				offButton[row][col].setStyle("-fx-border-color: black; -fx-background-color: #ffd89e; ");
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
		rotate.setStyle("-fx-background-color: #eec4ff; ");
		rotate.setId("rotate");
		setCenter(vbox);
		setBottom(message);
		setRight(rotate);
	}
	
	public void setMessage(String message)
	{
		this.message.setText(message);
	}
	
	public void updateView(String[][] defenseBoard, String[][] offenseBoard)
	{
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				defButton[i][j].setText(defenseBoard[i][j]);
				offButton[i][j].setText(offenseBoard[i][j]);
			}
		}
	}
	
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
	
	public int getRotateState()
	{
		return this.rotateState;
	}

	public void highlightPlacement(int currentBoatLength, int row, int col) {
		int rotate = this.getRotateState();
		for(int i = 0; i < currentBoatLength; i++)
		{
			if(rotate == 0)
			{
				if(i+col <= 9) defButton[row][col+i].setStyle("-fx-border-color: black; -fx-background-color: #c4c4c4; ");
			}
			if(rotate == 1)
			{
				if(i+row <= 9) defButton[row+i][col].setStyle("-fx-border-color: black; -fx-background-color: #c4c4c4; ");
			}
		}
	}

	public void offHighlight(int currentBoatLength, int row, int col) {
		int rotate = this.getRotateState();
		for(int i = 0; i < currentBoatLength; i++)
		{
			if(rotate == 0)
			{
				if(i+col <= 9) defButton[row][col+i].setStyle("-fx-border-color: black; -fx-background-color: #a3d3e3; ");
			}
			if(rotate == 1)
			{
				if(i+row <= 9) defButton[row+i][col].setStyle("-fx-border-color: black; -fx-background-color: #a3d3e3; ");
			}
		}
		
	}
	
	public void resetHighlight()
	{
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				defButton[i][j].setStyle("-fx-border-color: black; -fx-background-color: #a3d3e3; ");
			}
		}
	}
	
	
}
