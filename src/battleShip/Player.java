package battleShip;

public class Player {
	private Boat deffensiveGrid[][];
	private GridState offensiveGrid[][];
	private Boat boats[];
	private int boatsPlaced;
	
	public Player()
	{
		this.deffensiveGrid = new Boat[10][10];
		this.offensiveGrid = new GridState[10][10];
		this.initOffensiveGrid();
		this.boats = new Boat[5];
		initBoats();
		this.boatsPlaced = 0;
	}
	
	/**
	 * Setting a boat on the defensive grid
	 * @param row integer 1-10
	 * @param column integer 1-10
	 * @param boat, the Boat being placed
	 */
	private void setDeffensiveGrid(int row, int column, Boat boat)
	{
		this.deffensiveGrid[row][column] = boat;
	}
	
	public void setOffensiveGrid(int row, int column, GridState gridState)
	{
		this.offensiveGrid[row][column] = gridState;
	}
	
	public GridState getOffensiveGrid(int row, int col)
	{
		return this.offensiveGrid[row][col];
	}
	
	public Boat getDeffensiveGrid(int row, int column)
	{
		return this.deffensiveGrid[row][column];
	}
	
	/**
	 * initializes the 10x10 array to all EMPTY
	 */
	private void initOffensiveGrid()
	{	
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				this.offensiveGrid[i][j] = GridState.EMPTY;
			}
		}
	}
	
	public int boatsRemaining() {
		int result = 0;
		for(int i = 0; i < 5; i++)
		{
			if(!this.boats[i].getSunk()) result++;
		}
		
		return result;
	}
	
	private void initBoats()
	{
		this.boats[0] = new Boat(2);
		this.boats[1] = new Boat(3);
		this.boats[2] = new Boat(3);
		this.boats[3] = new Boat(4);
		this.boats[4] = new Boat(5);
	}
	
	public void placeBoat(int row, int col, int direction, Boat boat)
	{
		for(int i = 0; i < boat.getLength(); i++)
		{
			if(direction == 0) this.setDeffensiveGrid(row, col+i, boat); //horizontal
			if(direction == 1) this.setDeffensiveGrid(row+i, col, boat); //vertical
		}
		this.boatsPlaced++;
	}
	
	public int getBoatsPlaced()
	{
		return this.boatsPlaced;
	}
	
	public Boat getBoat(int index)
	{
		return this.boats[index];
	}
	
	
}
