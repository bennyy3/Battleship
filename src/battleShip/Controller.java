package battleShip;

import java.util.Scanner;

public class Controller {

	public static void main(String[] args) {
		Model model = new Model();
		Scanner s = new Scanner(System.in);
		while(true)
		{
			System.out.println(">");
			String input = s.nextLine();
			System.out.println(model.input(input));
		}

	}

}
