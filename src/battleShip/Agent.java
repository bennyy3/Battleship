package battleShip;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Agent {
	
	private int boatsRemaining[];
	private Model model;
	private Stack<int[]> hitStack;
	private String[][] previousBoard;
	private String prevAttack;
	private int[][] distribution;
	private String message;
	private int turns;
	
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

	private void fillDistribution() {
		for(int i = 0; i < 10; i++) distribution[i] = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}; //fill 3
		
		Random rand = new Random();
		if(rand.nextInt(5) != 0) //Most of the time edges will be weighted as 2
		{
			for(int i = 0; i < 10; i++)
			{
				distribution[0][i] = 2;
				distribution[9][i] = 2;
				distribution[i][0] = 2;
				distribution[i][9] = 2;
			}
		}
		if(rand.nextInt(3) != 0) //Most of the time this layer will be weighted as 1
		{
			for(int i = 1; i < 9; i++)
			{
				distribution[1][i] = 1;
				distribution[8][i] = 1;
				distribution[i][1] = 1;
				distribution[i][8] = 1;
			}
		}

		for(int i = 2; i < 8; i++)
		{
			distribution[2][i] = 2;
			distribution[7][i] = 2;
			distribution[i][2] = 2;
			distribution[i][7] = 2;
		}
		
		
		if(rand.nextInt(5) == 0)
		{
		//THIS IS EQUAL DISTRIBUTION
			for(int i = 0; i < 10; i++)
			{
				for(int j = 0; j < 10; j++)
				{
					distribution[i][j] = 3;
				}
			}
		}
	}
	
	public Model action() {
		switch(model.getGameState())
		{
		case STARTP1:
			break;
		case STARTP2:
			int row = (int)(Math.random() * 10);
			int col = (int)(Math.random() * 10);
			int rotate = (int) ((Math.random() * 10) % 2);
			message = model.input(rotate + "2def"+ row + "," + col);
			break;
		case P1:
			break;
		case P2:
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
		//printStack();
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
			return getHunt();
		}
		else if(availableAdj.size() == 0) return getHunt();
		else if(availableAdj.size() == 1) return availableAdj.get(0);
		else
		{
			Random rand = new Random();
			return availableAdj.get(rand.nextInt(availableAdj.size()-1));
		}
	}
	
	private String getHunt()
	{
		int length = getLongestBoat();
		
		ArrayList<String> list = new ArrayList<String>();
		
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				if(((i + j) % length == 0) && model.getOffenseBoard(2)[i][j].equals(" ")) list.add(i + "," + j);
			}
		}
		
		Random rand = new Random();
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
					else if(!probabilityStack.isEmpty() &&
							distribution[row][col] >= distribution[probabilityStack.get(0).charAt(0)-48][probabilityStack.get(0).charAt(2)-48])
						probabilityStack.push(str);
				}
			}
			int size = probabilityStack.size();
			return probabilityStack.get(rand.nextInt(size));
			}
		
	}
	
	/**
	 * Returns the longest boat yet to be sunk
	 * @return
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
	
	private void popSunk()
	{
		String[][] newBoard = model.getOffenseBoard(2);
		int testCount = 0;
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
							//System.out.println("{" + i + ", " + j + "}");
							testCount++;
							}
					}
				}
			}
		}
		//System.out.println(testCount);
	}
	
	private void printStack() {
		for(int i = 0; i < hitStack.size(); i++)
		{
			System.out.println("{"+hitStack.get(i)[0]+", " +hitStack.get(i)[1]+"}");
		}
		System.out.println(".");
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	public int getTurns()
	{
		return this.turns;
	}
}
