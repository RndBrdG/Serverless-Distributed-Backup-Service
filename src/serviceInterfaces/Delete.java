package serviceInterfaces;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import main.Main;
import console.Console;

public class Delete {

	private String[] info;
	private String fileID;

	public Delete() throws IOException{
		this.info = Main.files.get(new String(Console.getInputFromUser("Please, insert the file name you want to delete!")).toLowerCase()).split(" ");
		this.fileID = info[0];

		byte[] message = createMessage();

		Main.mc.send(message);
	}

	private byte[] createMessage() throws IOException{
		String chunkInformation = "DELETE 1.0 " + this.fileID + " ";
		ByteArrayOutputStream msgStream = new ByteArrayOutputStream();
		msgStream.write(chunkInformation.getBytes());
		msgStream.write((byte) 0x0d);
		msgStream.write((byte) 0x0a);
		msgStream.write((byte) 0x0d);
		msgStream.write((byte) 0x0a);
		byte[] messageCompleted = msgStream.toByteArray();
		return messageCompleted;
	}
}
