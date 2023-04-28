package battleShip;

public class Model {
	
	/**
	 * player 1 object
	 */
	private Player p1;
	
	/**
	 * player 2 object
	 */
	private Player p2;
	
	/**
	 * Keep track of what phase of the game we're in
	 */
	private GameState gameState;
	
	/**
	 * Default constructor for Model class
	 */
	public Model()
	{
		this.p1 = new Player();
		this.p2 = new Player();
		this.gameState = GameState.STARTP1;
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
	 * @param player, the player who's defensive board to get
	 * @return a string representation of the defensive board
	 */
	public String[][] getDefenseBoard(int playerID)
	{
		Player player = null;
		if(playerID == 1) player = p1;
		if(playerID == 2) player = p2;
		String[][] result = new String[10][10];
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				if(player.getDeffensiveGrid(i, j) == null)
				{
					result[i][j] = " ";
				} else
				{
					Boat tempBoat = player.getDeffensiveGrid(i, j);
					result[i][j] = "" + tempBoat.getBoatNumber();
				}
			}
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
	private String getOffenseBoard(int playerID)
	{
		Player player = null;
		if(playerID == 1) player = p1;
		if(playerID == 2) player = p2;
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
	 * This is an input constraint method check if a location on the defensive board is empty or not
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
	 * Included is input constraints
	 * @param row
	 * @param col
	 * @param defense, player who is being attacked
	 * @param attacker, the player actively attacking
	 */
	private String attack(String input, Player attacker, Player defense)
	{
		if(input.length() != 3) return "invalid input";
		int row = input.charAt(0) - 48; //converting from ASCII expecting a-j
		int col = input.charAt(2) - 48; //converting from ASCII expecting 0-9
		if(row < 0 || row >= 10) return "invalid row";
		if(col < 0 || col >= 10) return "invalid column";
		if(!(attacker.getOffensiveGrid(row, col) == GridState.EMPTY)) return "space has already been attacked";
		//end constraints
		
		String result = "";
		if(defense.getDeffensiveGrid(row, col) == null)
		{
			attacker.setOffensiveGrid(row, col, GridState.MISS);
			result = "MISS ";
		} else {
			defense.getDeffensiveGrid(row, col).hit();
			attacker.setOffensiveGrid(row, col, GridState.HIT);
			setSunk(defense.getDeffensiveGrid(row, col), defense, attacker);
			result = "HIT ";
			
		}
		
		return result + "attack"; //This method might change to boolean for GUI
	}
	
	/**
	 * Check to see if the defensive player's boats are all sunk
	 * @param defense, the player who was just attacked
	 * @return if all the defensive player's boats are sunk
	 */
	private boolean checkLoss(Player defense)
	{
		if(defense.boatsRemaining() == 0) return true;
		return false;
	}
	
	/**
	 * This is to make sure the placement of a boat is in a valid location
	 * @param input
	 * @param player
	 * @return
	 */
	private String checkValidPlacement(String input, Player player)
	{
		if(player == p1 && input.charAt(0) != 49) return "invalid defensive position for player 1";
		if(player == p2 && input.charAt(0) != 50) return "invalid defensive position for player 2";
		int row = input.charAt(4) - 48; //converting from ASCII expecting a-j
		int col = input.charAt(6) - 48; //converting from ASCII expecting 0-9
		int dir = 0;
		if(dir != 0 && dir != 1) return "invalid direction";
		if(dir == 0)
		{
			if(row < 0 || row >=10) return "invalid row";
			if(col < 0 || col + player.getBoat(player.getBoatsPlaced()).getLength() -1 >= 10) return "invalid column"; //out of bounds with boat length
		}
		if(dir == 1)
		{
			if(row < 0 || row + player.getBoat(player.getBoatsPlaced()).getLength() -1 >=10) return "invalid row";
			if(col < 0 || col >= 10) return "invalid column"; //out of bounds with boat length
		}
		if(!checkEmpty(row, col, dir, player, player.getBoat(player.getBoatsPlaced()))) return "a space is already occupied";
		
		player.placeBoat(row, col, dir, player.getBoat(player.getBoatsPlaced()));
		return "Placed Boat";
	}
	
	/**
	 * The message to display at the start
	 * @return
	 */
	public String startInstructions()
	{
		return "P1 Place your ships on the bottom grid";
	}
	
	/**
	 * This method interprets input called by the controller class, controls logic of the game
	 * This method looks pretty gross now, and will look better without having to create text visual output.
	 * @param input
	 * @return a string prompting the next action
	 */
	public String input(String input)
	{	
		String result = "";
		switch(getGameState())
		{
		
		case STARTP1:
			String placementCheckP1 = checkValidPlacement(input, p1);
			if(!placementCheckP1.equals("Placed Boat")) return placementCheckP1; //invalid argument
			if(p1.getBoatsPlaced() < 5)
			{
				result = "P1 Place your ships on the bottom grid";
				
			} else { //begin the print statement for the next gameState, a little confusing I know, but it must go right here, this is a pattern
				this.setGameState(GameState.STARTP2);
				result = "P2 Place your ships on the bottom grid";
			}
			break;
			
		case STARTP2:
			String placementCheckP2 = checkValidPlacement(input, p2);
			if(!placementCheckP2.equals("Placed Boat")) return placementCheckP2; //invalid argument
			
			if(input.length() != 5) return "invalid input";
				
			if(p2.getBoatsPlaced() < 5)
			{
				result = "P2 Place your ships on the bottom grid";
				
			} else { //begin the print statement for the next gameState
				this.setGameState(GameState.P1);
				result = "P1 choose a square on the top grid to attack";
			}
			break;
		case P1:
			String attackResult = attack(input, p1, p2);
			if(!attackResult.contains("attack")) return attackResult; //This will change with GUI
			
			if(checkLoss(p2))
			{
				result = "Player 1 Wins!";
				this.setGameState(GameState.END);
			} else {
				
				result += "P2 choose a square on the top grid to attack";
				this.setGameState(GameState.P2);
			}
			break;
		case P2:
			String attackResult2 = attack(input, p2, p1);
			if(!attackResult2.contains("attack")) return attackResult2; //This will change with GUI
			
			if(checkLoss(p1))
			{
				result = "Player 2 Wins!";
				this.setGameState(GameState.END);
			} else {
				result += "P1 choose a square on the top grid to attack";
				this.setGameState(GameState.P1);
			}
			break;
		case END:
			result = "END";
			break;
		}
		return result;
	}
	
	
}
