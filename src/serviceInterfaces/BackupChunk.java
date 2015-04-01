package serviceInterfaces;

import java.io.IOException;

import main.Main;

public class BackupChunk {

	public BackupChunk() throws IOException{
		String ex = Main.mc.receive();
		String[] splitMessage = ex.split("\\s+");
		System.out.println(splitMessage);
	}
}
