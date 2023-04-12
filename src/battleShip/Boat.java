package battleShip;

public class Boat {
	
	/*
	 * how many adjacent spaces the boat will take on the board
	 */
	private int length;
	
	/*
	 * how many spaces if its length have been hit
	 */
	private int hits;
	
	/*
	 * a quick indication if the boat has been hit a number equal to its length
	 */
	private boolean sunk;
	
	/*
	 * Constructor for the Boat Class
	 */
	public Boat(int length)
	{
		this.setLength(length);
		this.setSunk(false);
		this.hits = 0;
	}
	
	/**
	 * @param length
	 * sets the length of the boat
	 */
	private void setLength(int length)
	{
		this.length = length;
	}
	
	/**
	 * Setter for sunk
	 * @param sunk boolean
	 */
	private void setSunk(boolean sunk)
	{
		this.sunk = sunk;
	}
	
	/**
	 * increments the hit attribute
	 * This is where set sunk attribute to true
	 */
	private void hit()
	{
		this.hits++;
		if(this.getHits() == this.getLength()) this.setSunk(true);
	}
	
	/**
	 * Getter for length attribute
	 * @return integer length of boat
	 */
	public int getLength()
	{
		return this.length;
	}
	
	/**
	 * Getter for hit attribute
	 * @return integer number of hits
	 */
	public int getHits()
	{
		return this.hits;
	}
	
	/**
	 * Getter for sunk attribute
	 * @return the sunk attribute
	 */
	public boolean getSunk()
	{
		return this.sunk;
	}
	
}
