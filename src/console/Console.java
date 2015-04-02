package console;

import java.util.Scanner;

public class Console {

	private static Scanner input = new Scanner(System.in);
	private String userOption;
	
	public Console() {
		//start();
	}
	
	public void start() { 
		loop: do {
			startingMenu();

			switch (input.nextLine()) {
			case "1":
				userOption = "BACKUP";
				clearScreen();
				break loop;
			case "2":
				userOption = "RESTORE";
				clearScreen();
				break loop;
			case "3":
				userOption = "DELETE";
				clearScreen();
				break loop;
			case "4":
				userOption = "FREE";
				clearScreen();
				break loop;
			case "5":
				userOption = "BYE";
				clearScreen();
				System.out.print("Bye!");
				break loop;
			}
			clearScreen();
		} while (true);
	}
	
	private void startingMenu(){
		System.out.println("What do you want to do?");
		System.out.println("[1]Backup File");
		System.out.println("[2]Restore File");
		System.out.println("[3]Delete File");
		System.out.println("[4]Free Space");
		System.out.println("[5]Exit");
	}
	
	public void clearScreen(){
		for(int i = 0; i < 40; i++)
			System.out.println();
	}

	public static String getInputFromUser(String message){
		System.out.println(message);
		message = new String(input.nextLine());
		return message;
	}
	
	public String getUserOption(){
		return this.userOption;
	}
	
	public void endInput(){
		input.close();
	}
}
