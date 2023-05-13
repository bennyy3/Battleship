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
			model.input("02off"+attack);
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
			//else if(model.getOffenseBoard(2)[row][col].equals("S"))
			else //sunk
			{
				int len = model.getBoatLength(1, row, col);
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
			
			break;
			
		case P1WIN:
			break;
		case P2WIN:
			break;
		}
		System.out.println(hitStack.toString());
		return model;
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
		if(row > 0 && model.getOffenseBoard(2)[row-1][col].equals(" ")) return (row-1) + "," + col;
		if(row < 9 && model.getOffenseBoard(2)[row+1][col].equals(" ")) return (row+1) + "," + col;
		if(col > 0 && model.getOffenseBoard(2)[row][col-1].equals(" ")) return row + "," + (col-1);
		if(col < 9 && model.getOffenseBoard(2)[row][col+1].equals(" ")) return row + "," + (col+1);
		
		
		return getHunt(); //THIS IS A RANDOM shouldn't get here? Also makes compiler happy
		
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
		if(upperbound == 0) return list.get(0); // The last potential spot of the ship, we should get a hit here
		if(upperbound < 0) return rand.nextInt(9) + "," + rand.nextInt(9); //This shouldn't happen unless targeting phase fails
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
