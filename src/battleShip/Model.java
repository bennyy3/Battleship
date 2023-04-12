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
	
	
}
