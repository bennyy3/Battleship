package battleShip;

public class Model {
	private Player p1;
	private Player p2;
	private GameState gameState;
	
	public Model()
	{
		this.p1 = new Player();
		this.p2 = new Player();
		this.gameState = GameState.START;
	}
	
	private void setGameState(GameState state)
	{
		this.gameState = state;
	}
	
	private GameState getGameState()
	{
		return this.gameState;
	}
	
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
					result += tempBoat.getLength() + " ";
				}
			}
			result += "\n";
		}
		return result;
	}
	
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
	
	private void setSunk(Boat boat, Player defense, Player attacker)
	{
		if(boat.getSunk())
		{
			for(int i = 0; i < 10; i++)
			{
				for(int j = 0; j < 10; j++)
				{
					if(boat.equals(defense.getDeffensiveGrid(i, j))) attacker.setOffensiveGrid(i, j, GridState.SUNK);
				}
			}
		}
	}
	
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
	
	private boolean checkLoss(Player defense)
	{
		boolean result = true;
		for(int i = 0; i < 5; i++)
		{
			if(!defense.getBoat(i).getSunk()) result = false; //if any boat is not sunk return false
		}
		return result;
	}
	
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
				int row = input.charAt(0) - 97; //converting from ASCII expecting a-j
				int col = input.charAt(2) - 48; //converting from ASCII expecting 0-9
				int dir = input.charAt(4) - 48;
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
				int row2 = input.charAt(0) - 97; //converting from ASCII
				int col2 = input.charAt(2) - 48; //converting from ASCII
				int dir2 = input.charAt(4) - 48;
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
			int row3 = input.charAt(0) - 97; //converting from ASCII
			int col3 = input.charAt(2) - 48; //converting from ASCII
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
			int row4 = input.charAt(0) - 97; //converting from ASCII
			int col4 = input.charAt(2) - 48; //converting from ASCII
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
