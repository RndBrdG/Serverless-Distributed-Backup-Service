package serviceInterfaces;

import java.io.IOException;

import main.Main;

public class BackupChunk {

	public BackupChunk() throws IOException{
		byte[] received = new byte[8192];
		Main.mc.receive(received);
		String ex = new String(received);
		System.out.println(ex);
	}
}
