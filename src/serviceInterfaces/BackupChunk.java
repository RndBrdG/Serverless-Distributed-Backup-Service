package serviceInterfaces;

import java.io.IOException;

import main.Main;

public class BackupChunk {

	public BackupChunk() throws IOException{
		byte[] received = null;
		Main.mc.receive(received);
		String ex = new String(received);
		String[] splitMessage = ex.split("\\s+");
		System.out.println(splitMessage);
	}
}
