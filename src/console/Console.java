package console;

import java.util.Scanner;

public class Console {

	private Scanner input = new Scanner(System.in);
	
	public Console(){
		start();
	}
	
	public void start() { 
		loop: do {
			startingMenu();

			switch (input.nextLine()) {
			case "1":
				//backup();
				break loop;
			case "2":
				//restore();
				continue loop;
			case "3":
				//deleteFile();
				break loop;
			case "4":
				//freeSpace();
				break loop;
			case "5":
				clearScreen();
				System.out.print("Bye!");
				break loop;
			}
		} while (true);

		input.close();
	}
	
	private void startingMenu(){
		System.out.println("What do you want to do?");
		System.out.println("[1]Backup File");
		System.out.println("[2]Restore File");
		System.out.println("[3]Delete File");
		System.out.println("[4]Free Space");
		System.out.println("[5]Exit");
	}
	
	private void clearScreen(){
		for(int i = 0; i < 40; i++)
			System.out.println();
	}
}
