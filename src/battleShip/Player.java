package battleShip;

public class Player {
	
	/**
	 * The bottom grid showing a players boats
	 */
	private Boat deffensiveGrid[][];
	
	/**
	 * The grid showing where a player has attacked
	 * HIT indicates we have attacked and hit an enemy boat on
	 * MISS indicates we attacked an empty location
	 * SUNK indicates a boat has been completely hit and sunk
	 * EMPTY indicates the attacker has not attacked a location yet
	 */
	private GridState offensiveGrid[][];
	
	/**
	 * The list of a players defensive boats
	 */
	private Boat boats[];
	
	/**
	 * The number of boats placed is used in the placing phase to check when to move to next player
	 */
	private int boatsPlaced;
	
	/**
	 * Default constructor for a player
	 */
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
	
	/**
	 * Setter for the offensiveGrid
	 * @param row
	 * @param column
	 * @param gridState
	 */
	public void setOffensiveGrid(int row, int column, GridState gridState)
	{
		this.offensiveGrid[row][column] = gridState;
	}
	
	/**
	 * getter for the offensiveGrid
	 * @param row
	 * @param col
	 * @return the state of the grid at that location
	 */
	public GridState getOffensiveGrid(int row, int col)
	{
		return this.offensiveGrid[row][col];
	}
	
	/**
	 * getter for the deffensiveGrid
	 * @param row
	 * @param column
	 * @return a reference to the boat that is in the location row, column
	 */
	public Boat getDeffensiveGrid(int row, int column)
	{
		return this.deffensiveGrid[row][column];
	}
	
	/**
	 * initializes the 10x10 array to all EMPTY state
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
	
	/**
	 * @return how many boats that have not been sunk. 0 indicates a loss.
	 */
	public int boatsRemaining() {
		int result = 0;
		for(int i = 0; i < 5; i++)
		{
			if(!this.boats[i].getSunk()) result++;
		}
		
		return result;
	}
	
	/**
	 * Initialize the size of the boats
	 * These are hard coded to the official rules, but could be changed
	 */
	private void initBoats()
	{
		this.boats[0] = new Boat(2, 1);
		this.boats[1] = new Boat(3, 2);
		this.boats[2] = new Boat(3, 3);
		this.boats[3] = new Boat(4, 4);
		this.boats[4] = new Boat(5, 5);
	}
	
	/**
	 * This method inserts a boat into the deffensiveGrid
	 * for example a boat of length 2 horizontally placed at 0,0 would put a pointer to a boat at location 0,0 and at 0,1
	 * @param row
	 * @param col
	 * @param direction
	 * @param boat
	 */
	public void placeBoat(int row, int col, int direction, Boat boat)
	{
		for(int i = 0; i < boat.getLength(); i++)
		{
			if(direction == 0) this.setDeffensiveGrid(row, col+i, boat); //horizontal
			if(direction == 1) this.setDeffensiveGrid(row+i, col, boat); //vertical
		}
		this.boatsPlaced++;
	}
	
	/**
	 * @return getter for boatsPlaced
	 */
	public int getBoatsPlaced()
	{
		return this.boatsPlaced;
	}
	
	/**
	 * @param index
	 * @return returns a boat at the index between 0 and 4
	 */
	public Boat getBoat(int index)
	{
		return this.boats[index];
	}
	
	
}
