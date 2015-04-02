package serviceInterfaces;

import java.io.IOException;

import main.Main;

public class BackupChunk {

	public BackupChunk() throws IOException{
		byte[] received = new byte[8192];
		System.out.println("e");
		Main.mc.receive(received);
		String ex = new String(received);
		System.out.println("out of bkpchunk");
	}
}
