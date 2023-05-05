package battleShip;

public class Agent {
	
	public Agent()
	{

	}
	
	public Model action(Model model)
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
			int rowAttack = (int)(Math.random() * 10);
			int colAttack = (int)(Math.random() * 10);
			model.input("02off"+rowAttack+","+colAttack);
		case P1WIN:
			break;
		case P2WIN:
			break;
		}
		
		return model;
	}
	
}
