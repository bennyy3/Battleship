package battleShip;

public class Player {
	private Boat deffensiveGrid[][];
	private GridState offensiveGrid[][];
	
	public Player()
	{
		this.deffensiveGrid = new Boat[10][10];
		this.offensiveGrid = new GridState[10][10];
		this.initOffensiveGrid();
	}
	
	/**
	 * Setting a boat on the defensive grid
	 * @param row integer 1-10
	 * @param column integer 1-10
	 * @param boat, the Boat being placed
	 */
	public void setDeffensiveGrid(int row, int column, Boat boat)
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
	
	
}
