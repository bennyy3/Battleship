package battleShip;

import java.util.Scanner;

public class Controller {

	public static void main(String[] args) {
		Model model = new Model();
		while(true)
		{
			System.out.println(">");
			Scanner s = new Scanner(System.in);
			String input = s.nextLine();
			System.out.println(model.input(input));
		}

	}

}
