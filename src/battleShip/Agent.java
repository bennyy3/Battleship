package battleShip;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Agent {
	
	private int boatsRemaining[];
	private Model model;
	private Stack<int[]> hitStack;
	
	private String prevAttack;
	
	public Agent(Model model)
	{
		this.boatsRemaining = new int[5];
		this.boatsRemaining[0] = 5;
		this.boatsRemaining[1] = 4;
		this.boatsRemaining[2] = 3;
		this.boatsRemaining[3] = 3;
		this.boatsRemaining[4] = 2;
		this.hitStack = new Stack<int[]>();
		this.model = model;
		this.prevAttack = new String();
		
	}
	
	public Model action()
	{
		switch(model.getGameState())
		{
		case STARTP1:
			break;
		case STARTP2:
			int row = (int)(Math.random() * 10);
			int col = (int)(Math.random() * 10);
			int rotate = (int) ((Math.random() * 10) % 2);
			model.input(rotate + "2def"+ row + "," + col);
			break;
		case P1:
			break;
		case P2:
			String attack = generateAttack(); //save reference of spot
			row = attack.charAt(0) - 48;
			col = attack.charAt(2) - 48;
			model.input("02off"+attack); //we attacked
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
		if(model.getOffenseBoard(2)[row][col].equals("M"))
		{
			if(hitStack.size() > 1 && prevAttack.equals("H")) reverseHitStack();
			prevAttack = "M";
		}
		else if(model.getOffenseBoard(2)[row][col].equals("H"))
		{
			hitStack.push(new int[]{row, col});
			prevAttack = "H";
		}
		else if(model.getOffenseBoard(2)[row][col].equals("S"))
		{
			int len = model.getBoatLength(1, row, col); //YOU SUNK MY BATTLESHIP
			for(int i = 0; i < 5; i++)
			{
				if(boatsRemaining[i] == len)
				{
					boatsRemaining[i] = 0;
					break;
				}
			}
			for(int i = 0; i < len - 1 ; i++) hitStack.pop();
			prevAttack = "S";
		}
		else prevAttack = "";
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
		if(availableAdj.size() == 0) return getHunt(); //Should hopefully never run
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
		int upperbound = list.size() - 1;
		int salt = rand.nextInt(5); //We don't want to be completely predictable
		if(upperbound < 0 || salt == 0) return rand.nextInt(9) + "," + rand.nextInt(9);
		if(upperbound == 0) return list.get(0);
		else return list.get(rand.nextInt(upperbound));
		
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
}
