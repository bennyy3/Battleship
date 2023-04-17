package battleShip;

public class Model {
	private Player p1;
	private Player p2;
	private GameState gameState;
	
	/**
	 * Default constructor for Model class
	 */
	public Model()
	{
		this.p1 = new Player();
		this.p2 = new Player();
		this.gameState = GameState.START;
	}
	
	/**
	 * setter for gameState
	 * @param state ENUM GameState
	 */
	private void setGameState(GameState state)
	{
		this.gameState = state;
	}
	
	/**
	 * Getter for the gameState
	 * @return gameState
	 */
	private GameState getGameState()
	{
		return this.gameState;
	}
	
	/**
	 * Get a string visual of the defense board of a particular player object
	 * . means empty, otherwise will display the boatNumber to show location of boats
	 * @param player
	 * @return a string representation of the defensive board
	 */
	private String getDefenseBoard(Player player)
	{
		String row = "abcdefghij";
		String result = "  0 1 2 3 4 5 6 7 8 9\n";
		for(int i = 0; i < 10; i++)
		{
			result += row.charAt(i) + " ";
			for(int j = 0; j < 10; j++)
			{
				if(player.getDeffensiveGrid(i, j) == null)
				{
					result += ". ";
				} else
				{
					Boat tempBoat = player.getDeffensiveGrid(i, j);
					result += tempBoat.getBoatNumber() + " ";
				}
			}
			result += "\n";
		}
		return result;
	}
	
	/**
	 * Get a string visual of the Offense Board
	 * . means empty
	 * H means HIT
	 * M means MISS
	 * S means SUNK
	 * @param player
	 * @return a string that visualized the Offensive Board
	 */
	private String getOffenseBoard(Player player)
	{
		String result = "  0 1 2 3 4 5 6 7 8 9\n";
		String row = "abcdefghij";
		for(int i = 0; i < 10; i++)
		{
			result += row.charAt(i) + " ";
			for(int j = 0; j < 10; j++)
			{
				if(player.getOffensiveGrid(i, j) == GridState.EMPTY) result += ". ";
				if(player.getOffensiveGrid(i, j) == GridState.HIT) result += "H ";
				if(player.getOffensiveGrid(i, j) == GridState.MISS) result += "M ";
				if(player.getOffensiveGrid(i, j) == GridState.SUNK) result += "S "; 
			}
			result += "\n";
		}
		return result;
	}
	
	/**
	 * This sets the grid state of any sunk boat to sunk on the offensive map
	 * @param boat, The boat that has just been attacked, so it needs to check if its sunk to update offensive map
	 * @param defense, The defender so that we can check locations on their map
	 * @param attacker, the player who just took an attack
	 */
	private void setSunk(Boat boat, Player defense, Player attacker)
	{
		if(boat.getSunk())
		{
			for(int i = 0; i < 10; i++)
			{
				for(int j = 0; j < 10; j++)
				{
					//Loop through the map until we get a match on the boat, we need to turn those spots to sunk
					if(boat.equals(defense.getDeffensiveGrid(i, j))) attacker.setOffensiveGrid(i, j, GridState.SUNK);
				}
			}
		}
	}
	
	/**
	 * This is an input constraint method used to make sure boats are not placed on top of each other
	 * @param row
	 * @param col
	 * @param dir, 0 means horizontal and 1 means vertical
	 * @param player, the player who is attempting to place a boat
	 * @param boat, the boat being placed is used to find length
	 * @return true if there is no collision and all spots are open to place a new boat
	 */
	private boolean checkEmpty(int row, int col, int dir, Player player, Boat boat)
	{
		for(int i = 0; i < boat.getLength(); i++)
		{
			if(dir == 0)
			{
				if(player.getDeffensiveGrid(row, col+i) != null) return false;
			}
			if(dir == 1)
			{
				if(player.getDeffensiveGrid(row+i, col) != null) return false;
			}
		}
		return true;
	}
	
	/**
	 * given a row and column, we see if the attacker, hit, missed, or sunk an enemy boat.
	 * @param row
	 * @param col
	 * @param defense, player who is being attacked
	 * @param attacker, the player actively attacking
	 */
	private void attackAgainst(int row, int col, Player defense, Player attacker)
	{
		if(defense.getDeffensiveGrid(row, col) == null)
		{
			attacker.setOffensiveGrid(row, col, GridState.MISS);
		} else {
			defense.getDeffensiveGrid(row, col).hit();
			attacker.setOffensiveGrid(row, col, GridState.HIT);
			setSunk(defense.getDeffensiveGrid(row, col), defense, attacker);
			
		}
	}
	
	/**
	 * Check to see if the defensive player's boats are all sunk
	 * @param defense, the player who was just attacked
	 * @return if all the defensive player's boats are sunk
	 */
	private boolean checkLoss(Player defense)
	{
		boolean result = true;
		for(int i = 0; i < 5; i++)
		{
			if(!defense.getBoat(i).getSunk()) result = false; //if any boat is not sunk return false
		}
		return result;
	}
	
	/**
	 * This method interprets input called by the controller class
	 * @param input
	 * @return a string prompting the next action
	 */
	public String input(String input)
	{	
		String result = "";
		switch(getGameState())
		{
		case START:
			result += "P1 Defensive Board:\n" + getDefenseBoard(p1) +"\n\n";
			result += "Enter; 'Row,Col,Direction' to place Boat of length "
					+ p1.getBoat(0).getLength() + " Note 0 Horizontal, 1 Vertical";
			this.setGameState(GameState.STARTP1);
			break;
		case STARTP1:
				if(input.length() != 5) return "invalid input";
				int row = input.charAt(0) - 97; //converting from ASCII expecting a-j
				int col = input.charAt(2) - 48; //converting from ASCII expecting 0-9
				int dir = input.charAt(4) - 48;
				if(dir != 0 && dir != 1) return "invalid direction";
				if(dir == 0)
				{
					if(row < 0 || row >=10) return "invalid row";
					if(col < 0 || col + p1.getBoat(p1.getBoatsPlaced()).getLength() -1 >= 10) return "invalid column"; //out of bounds with boat length
				}
				if(dir == 1)
				{
					if(row < 0 || row + p1.getBoat(p1.getBoatsPlaced()).getLength() -1 >=10) return "invalid row";
					if(col < 0 || col >= 10) return "invalid column"; //out of bounds with boat length
				}
				if(!checkEmpty(row, col, dir, p1, p1.getBoat(p1.getBoatsPlaced()))) return "a space is already occupied";
				
				p1.placeBoat(row, col, dir, p1.getBoat(p1.getBoatsPlaced()));
				
				result += "P1 Defensive Board:\n" + getDefenseBoard(p1) +"\n\n";
				
			if(p1.getBoatsPlaced() < 5)
			{
				result += "P1 Enter 'Row,Col,Direction' to place Boat of length "
						+ p1.getBoat(p1.getBoatsPlaced()).getLength() + " NOTE 0 Horizontal, 1 Vertical";
				
			} else {
				this.setGameState(GameState.STARTP2);
				result += "P2 Defensive Board:\n" + getDefenseBoard(p2) + "\n\n";
				result += "P2 Enter 'Row,Col,Direction' to place Boat of length "
						+ p2.getBoat(0).getLength() + " "
								+ "NOTE 0 Horizontal, 1 Vertical";
			}
			break;
			
		case STARTP2:
				if(input.length() != 5) return "invalid input";
				int row2 = input.charAt(0) - 97; //converting from ASCII
				int col2 = input.charAt(2) - 48; //converting from ASCII
				int dir2 = input.charAt(4) - 48;
				if(dir2 != 0 && dir2 != 1) return "invalid direction";
				if(dir2 == 0)
				{
					if(row2 < 0 || row2 >=10) return "invalid row";
					if(col2 < 0 || col2 + p2.getBoat(p2.getBoatsPlaced()).getLength() -1 >= 10) return "invalid column"; //out of bounds with boat length
				}
				if(dir2 == 1)
				{
					if(row2 < 0 || row2 + p2.getBoat(p2.getBoatsPlaced()).getLength() -1 >=10) return "invalid row";
					if(col2 < 0 || col2 >= 10) return "invalid column"; //out of bounds with boat length
				}
				
				if(!checkEmpty(row2, col2, dir2, p2, p2.getBoat(p2.getBoatsPlaced()))) return "a space is already occupied";
				p2.placeBoat(row2, col2, dir2, p2.getBoat(p2.getBoatsPlaced()));
				
				result += "P2 Defensive Board:\n" + getDefenseBoard(p2) + "\n\n";
				
			if(p2.getBoatsPlaced() < 5)
			{
				result += "P2 Enter 'Row,Col,Direction' to place Boat of length "
						+ p2.getBoat(p2.getBoatsPlaced()).getLength() + "NOTE 0 Horizontal, 1 Vertical";
			} else {
				this.setGameState(GameState.P1);
				result += "P1 Defensive Board:\n" + getDefenseBoard(p1) +"\n\n";
				result += "P1 Offesive Board:\n" + getOffenseBoard(p1)+"\n\n";
				result += "P1 enter 'Row,Col' to attack";
			}
			break;
		case P1:
			if(input.length() != 3) return "invalid input";
			int row3 = input.charAt(0) - 97; //converting from ASCII
			int col3 = input.charAt(2) - 48; //converting from ASCII
			if(row3 < 0 || row3 >= 10) return "invalid row";
			if(col3 < 0 || col3 >= 10) return "invalid column";
			
			attackAgainst(row3, col3, p2, p1);
			if(checkLoss(p2))
			{
				result = "Player 1 Wins!";
				this.setGameState(GameState.END);
			} else {
				result += "P2 Defensive Board:\n" + getDefenseBoard(p2) + "\n\n";
				result += "P2 Offesive Board:\n" + getOffenseBoard(p2)+"\n\n";
				result += "P2 enter 'Row,Col' to attack";
				this.setGameState(GameState.P2);
			}
			break;
		case P2:
			if(input.length() != 3) return "invalid input";
			int row4 = input.charAt(0) - 97; //converting from ASCII
			int col4 = input.charAt(2) - 48; //converting from ASCII
			if(row4 < 0 || row4 >= 10) return "invalid row";
			if(col4 < 0 || col4 >= 10) return "invalid column";
			attackAgainst(row4, col4, p1, p2);
			
			if(checkLoss(p1))
			{
				result = "Player 2 Wins!";
				this.setGameState(GameState.END);
			} else {
				result += "P1 Defensive Board:\n" + getDefenseBoard(p1) +"\n\n";
				result += "P1 Offesive Board:\n" + getOffenseBoard(p1)+"\n\n";
				result += "P1 enter 'Row,Col' to attack";
				this.setGameState(GameState.P1);
			}
			break;
		case END:
			result += "END";
			break;
		}
		return result;
	}
	
	
}
