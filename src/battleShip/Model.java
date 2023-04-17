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
		String result = "";
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				if(player.getDeffensiveGrid(i, j) == null)
				{
					result += "0 ";
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
	
	public String input(String input)
	{	
		String result = "";
		switch(getGameState())
		{
		case START:
			result += "P1 Defensive Board:\n" + getDefenseBoard(p1) +"\n\n";
			result += "P2 Defensive Board:\n" + getDefenseBoard(p2) + "\n\n";
			result += "Enter; 'Row,Col,Direction' to place Boat of length "
					+ p1.getBoat(0).getLength() + " Note 0 Horizontal, 1 Vertical";
			this.setGameState(GameState.STARTP1);
			break;
		case STARTP1:
			
			
				int row = input.charAt(0) - 48; //converting from ASCII
				int col = input.charAt(2) - 48; //converting from ASCII
				int dir = input.charAt(4) - 48;
				p1.placeBoat(row, col, dir, p1.getBoat(p1.getBoatsPlaced()));
				
				result += "P1 Defensive Board:\n" + getDefenseBoard(p1) +"\n\n";
				result += "P2 Defensive Board:\n" + getDefenseBoard(p2) + "\n\n";
				
			if(p1.getBoatsPlaced() < 5)
			{
				result += "P1 Enter 'Row,Col,Direction' to place Boat of length "
						+ p1.getBoat(p1.getBoatsPlaced()).getLength() + " NOTE 0 Horizontal, 1 Vertical";
				
			} else {
				this.setGameState(GameState.STARTP2);
				result += "P1 Defensive Board:\n" + getDefenseBoard(p1) +"\n\n";
				result += "P2 Defensive Board:\n" + getDefenseBoard(p2) + "\n\n";
				result += "P2 Enter 'Row,Col,Direction' to place Boat of length "
						+ p2.getBoat(0).getLength() + " "
								+ "NOTE 0 Horizontal, 1 Vertical";
			}
			break;
			
		case STARTP2:
				int row2 = input.charAt(0) - 48; //converting from ASCII
				int col2 = input.charAt(2) - 48; //converting from ASCII
				int dir2 = input.charAt(4) - 48;
				p2.placeBoat(row2, col2, dir2, p2.getBoat(p2.getBoatsPlaced()));
				
				result += "P1 Defensive Board:\n" + getDefenseBoard(p1) +"\n\n";
				result += "P2 Defensive Board:\n" + getDefenseBoard(p2) + "\n\n";
				
			if(p2.getBoatsPlaced() < 5)
			{
				result += "P2 Enter 'Row,Col,Direction' to place Boat of length "
						+ p2.getBoat(p2.getBoatsPlaced()).getLength() + "NOTE 0 Horizontal, 1 Vertical";
			} else {
				this.setGameState(GameState.P1);
				result += "P1 Defensive Board:\n" + getDefenseBoard(p1) +"\n\n";
				result += "P2 Defensive Board:\n" + getDefenseBoard(p2) + "\n\n";
				result += "P1";
			}
			break;
		case P1:
			result += "P1";
			break;
		case P2:
			result += "P2";
			break;
		case END:
			result += "END";
			break;
		}
		return result;
	}
	
	
}
