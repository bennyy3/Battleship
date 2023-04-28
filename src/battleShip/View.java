package battleShip;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
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
	
	public View(EventHandler<ActionEvent> event, int id)
	{
		super();
		offGrid = new GridPane();
		defGrid = new GridPane();
		defButton = new Button[10][10];
		offButton = new Button[10][10];
		
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				defButton[i][j] = new Button();
				defButton[i][j].setId(id+"def"+ i + "," + j);
				//defButton[i][j].setText(counter.toString());
				defButton[i][j].setPrefHeight(40);
				defButton[i][j].setPrefWidth(40);
				defButton[i][j].setStyle("-fx-border-color: black; -fx-background-color: #a3d3e3; ");
				defButton[i][j].setOnAction(event);
				defGrid.add(defButton[i][j], j, i);
				
				offButton[i][j] = new Button();
				offButton[i][j].setId(id+"off"+ i + "," + j);
				//offButton[i][j].setText(counter.toString());
				offButton[i][j].setPrefHeight(40);
				offButton[i][j].setPrefWidth(40);
				offButton[i][j].setStyle("-fx-border-color: black; -fx-background-color: #ffd89e; ");
				offButton[i][j].setOnAction(event);
				offGrid.add(offButton[i][j], j, i);
			}
		}
		
		VBox vbox = new VBox();
		vbox.getChildren().add(offGrid);
		vbox.getChildren().add(defGrid);
		message = new Text();
		//model.input("start");
		message.setText("hello");
		setCenter(vbox);
		setBottom(message);
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
	
}
