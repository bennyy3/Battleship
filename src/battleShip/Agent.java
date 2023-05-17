package battleShip;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Agent {
	
	/**
	 * A list of boats remaining used to determine longest boat left
	 */
	private int boatsRemaining[];
	
	/**
	 * reference to the model
	 */
	private Model model;
	
	/**
	 * a stack that stores the location of each offensive hit
	 * index 0: row
	 * index 1: column
	 */
	private Stack<int[]> hitStack;
	
	/**
	 * a reference to the previous board used to see what is different
	 */
	private String[][] previousBoard;
	
	/**
	 * reference to the previous attack used for tracking
	 */
	private String prevAttack;
	
	/**
	 * stores weights. Higher integers are more attractive to hit.
	 * The AI will load in random distributions for each game, this allows the AI
	 * the advantage of randomness
	 */
	private int[][] distribution;
	
	/**
	 * The message result of the model after model.input();
	 */
	private String message;
	
	/**
	 * Used to count the number of turns it took for the AI to win.
	 * This is only used when running the game many times to
	 * calculate the performance of the AI
	 */
	private int turns;
	
	/**
	 * constructor
	 * @param model
	 */
	public Agent(Model model)
	{
		this.boatsRemaining = new int[5];
		this.boatsRemaining[0] = 5;
		this.boatsRemaining[1] = 4;
		this.boatsRemaining[2] = 3;
		this.boatsRemaining[3] = 3;
		this.boatsRemaining[4] = 2;
		this.turns = 0;
		this.hitStack = new Stack<int[]>();
		this.model = model;
		this.prevAttack = new String();
		this.previousBoard = new String[10][10];
		this.message = new String();
		this.distribution = new int[10][10];
		fillDistribution();
	}

	/**
	 * This method has some basic distributions and will randomly generate numbers
	 * to create different distributions
	 * some distributions perform slightly better than others, but this makes the AI less predictable
	 */
	private void fillDistribution() {
		for(int i = 0; i < 10; i++) distribution[i] = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}; //fill 3
		
		Random rand = new Random();
		for(int i = 0; i < 10; i++)
		{
			distribution[0][i] = 1;
			distribution[9][i] = 1;
			distribution[i][0] = 1;
			distribution[i][9] = 1;
		}
		
		int layerWeight = rand.nextInt(3) + 1;
		for(int i = 1; i < 9; i++)
		{
			distribution[1][i] = layerWeight;
			distribution[8][i] = layerWeight;
			distribution[i][1] = layerWeight;
			distribution[i][8] = layerWeight;
		}
		

		for(int i = 2; i < 8; i++)
		{
			distribution[2][i] = 2;
			distribution[7][i] = 2;
			distribution[i][2] = 2;
			distribution[i][7] = 2;
		}
		
	}
	
	/**
	 * after a point, the distribution seems to hurt performance not help
	 */
	private void setEvenDistribution()
	{
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				distribution[i][j] = 3;
			}
		}
	}
	
	/**
	 * public method that tells the AI its time to take a turn
	 * @return the model that it has altered, sends back to controller to become the new model
	 */
	public Model action() {
		switch(model.getGameState())
		{
		case STARTP1:
			break;
		case STARTP2: //placing phase, random seemed to be just fine
			int row = (int)(Math.random() * 10);
			int col = (int)(Math.random() * 10);
			int rotate = (int) ((Math.random() * 10) % 2);
			message = model.input(rotate + "2def"+ row + "," + col);
			break;
		case P1:
			break;
		case P2: //attacking phase
			if(turns > 30) setEvenDistribution();
			previousBoard = model.getOffenseBoard(2);
			String attack = generateAttack(); //save reference of spot
			row = attack.charAt(0) - 48;
			col = attack.charAt(2) - 48;
			message = model.input("02off"+attack); //we attacked
			manageNextBestMove(row, col); //manage AI back end
			break;
		case P1WIN:
			break;
		case P2WIN:
			break;
		}
		return model;
	}
	
	/**
	 * This method keeps track of the hitStack and the prevAttack
	 * This method is crucial for the tracking down a boat after a hit
	 * @param row, row of the current attack
	 * @param col, column of the current attack
	 */
	private void manageNextBestMove(int row, int col) {
		if(model.getOffenseBoard(2)[row][col].equals("M") && previousBoard[row][col].equals(" ")) //prevents repeat bug
		{
			if(hitStack.size() > 1 && prevAttack.equals("H")) reverseHitStack();
			prevAttack = "M";
			turns++;
		}
		else if(model.getOffenseBoard(2)[row][col].equals("H") && previousBoard[row][col].equals(" "))
		{
			hitStack.push(new int[]{row, col});
			prevAttack = "H";
			turns++;
			
		}
		else if(model.getOffenseBoard(2)[row][col].equals("S") && previousBoard[row][col].equals(" "))
		{
			hitStack.push(new int[] {row, col});
			int len = model.getBoatLength(1, row, col); //YOU SUNK MY BATTLESHIP
			for(int i = 0; i < 5; i++)
			{
				if(boatsRemaining[i] == len)
				{
					boatsRemaining[i] = 0;
					break;
				}
			}
			popSunk();
			prevAttack = "S";
			turns++;
		}
	}
	
	/**
	 * This method starts the attack.
	 * The method will either hunt if there are no hits on the board
	 * otherwise it will track
	 * @return
	 */
	private String generateAttack()
	{

		if(hitStack.empty()) return getHunt();
		int row = hitStack.peek()[0];
		int col = hitStack.peek()[1];
		if(hitStack.size() > 1)
		{	
			int prevRow = hitStack.get(hitStack.size()-2)[0];
			int prevCol = hitStack.get(hitStack.size()-2)[1];
			
			if(row > 0 && prevRow - row > 0 && model.getOffenseBoard(2)[row-1][col].equals(" ")) return (row-1) + "," + col;
			if(row < 9 && prevRow - row < 0 && model.getOffenseBoard(2)[row+1][col].equals(" ")) return (row+1) + "," + col;
			if(col > 0 && prevCol - col > 0 && model.getOffenseBoard(2)[row][col-1].equals(" ")) return row + "," + (col-1);
			if(col < 9 && prevCol - col < 0 && model.getOffenseBoard(2)[row][col+1].equals(" ")) return row + "," + (col+1);
		}
		//else hit an adjacent
		ArrayList<String> availableAdj = new ArrayList<String>();
		if(row > 0 && model.getOffenseBoard(2)[row-1][col].equals(" ")) availableAdj.add((row-1) + "," + col);
		if(row < 9 && model.getOffenseBoard(2)[row+1][col].equals(" ")) availableAdj.add((row+1) + "," + col);
		if(col > 0 && model.getOffenseBoard(2)[row][col-1].equals(" ")) availableAdj.add(row + "," + (col-1));
		if(col < 9 && model.getOffenseBoard(2)[row][col+1].equals(" ")) availableAdj.add(row + "," + (col+1));
		if(availableAdj.size() == 0 && hitStack.size() > 1)
		{
			reverseHitStack();
			return getHunt(); //will 'waste' a turn executing one hunt, but then it will return to tracking.
			//I tried other ways, but they were complicated with very little performance gain.
		}
		else if(availableAdj.size() == 0) return getHunt();
		else if(availableAdj.size() == 1) return availableAdj.get(0); //attack the only available spot
		else
		{
			Random rand = new Random();
			return availableAdj.get(rand.nextInt(availableAdj.size()-1)); //randomly attack an available adjacent spot
		}
	}
	
	/**
	 * This method randomly picks a spot on the board to attack.
	 * Uses the probability distribution to favor higher weights.
	 * @return a location to attack
	 */
	private String getHunt()
	{
		Random rand = new Random();
		int length = getLongestBoat();
		
		//generate a minimal list of locations of a potential hit based on longest boat
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				if(((i + j) % length == 0) && model.getOffenseBoard(2)[i][j].equals(" ")) list.add(i + "," + j);
			}
		}
		
		//Sort the list so that we are only handling the largest weights
		int upperbound = list.size();
		if(upperbound == 0) return rand.nextInt(10) + "," + rand.nextInt(10);
		if(upperbound == 1) return list.get(0);
		else
			{
			Stack<String> probabilityStack = new Stack<String>();
			for(int k = 3; k >= 0; k--)
			{
				for(int i = 0; i < list.size(); i++)
				{
					String str = list.get(i);
					int row = str.charAt(0) - 48;
					int col = str.charAt(2) - 48;
					if(probabilityStack.isEmpty() && distribution[row][col] == k) probabilityStack.push(str);
					
					//I'm sorry, but it works. It can't be reduced because ordering of && prevents bad code from running
					else if(!probabilityStack.isEmpty() &&
							distribution[row][col] >= distribution[probabilityStack.get(0).charAt(0)-48][probabilityStack.get(0).charAt(2)-48])
						probabilityStack.push(str);
				}
			}
			int size = probabilityStack.size();
			return probabilityStack.get(rand.nextInt(size)); //randomly choose from the list of weighted best available moves
			}
		
	}
	
	/**
	 * @return the longest boat yet to be sunk
	 */
	private int getLongestBoat()
	{
		int longest = 0;
		for(int i = 0; i < 5; i++)
		{
			if(this.boatsRemaining[i] > longest) longest = this.boatsRemaining[i];
		}
		return longest;
	}
	
	public void updateModel(Model model)
	{
		this.model = model;
	}
	
	/**
	 * reverses the hit stack.
	 * Used to flip the direction of an attack
	 */
	private void reverseHitStack()
	{
		Stack<int[]> revStack = new Stack<int[]>();
		int len = hitStack.size(); //we are popping hitstack so we need this to stay constant
		for(int i = 0; i < len; i++)
			{
			revStack.push(hitStack.pop());
			}
		this.hitStack = revStack;
	}
	
	/**
	 * pops all of entries in the hitStack that correspond to the latest sunk boat
	 * It needs to manually check the previous grid against the new. otherwise the stack would get messed up.
	 */
	private void popSunk()
	{
		String[][] newBoard = model.getOffenseBoard(2);
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				if(newBoard[i][j].equals("S") && !previousBoard[i][j].equals("S"))
				{
					int[] sunkPosition = new int[] {i, j};
					for(int k = 0; k < hitStack.size(); k++)
					{
						if(hitStack.get(k)[0] == sunkPosition[0] && hitStack.get(k)[1] == sunkPosition[1])
						{
							hitStack.remove(k);
						}
					}
				}
			}
		}
	}
	
	/**
	 * getter for the message attribute
	 * @return the message from the model
	 */
	public String getMessage()
	{
		return this.message;
	}
	
	/**
	 * getter for the turns attribute
	 * @return the number of attacks an AI has made.
	 */
	public int getTurns()
	{
		return this.turns;
	}
}
