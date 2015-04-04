package serviceInterfaces;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import main.Main;
import console.Console;

public class Restore {

	private String fileID;
	private int chunkNO;
	
	public Restore(){
		this.fileID = Main.files.get(new String(Console.getInputFromUser("Please, insert the file name!")));
		this.chunkNO = Integer.parseInt(new String(Console.getInputFromUser("Please, insert the chunk number!")));
	}
	
	public void start() throws IOException{
		byte[] byteMessage = buildMessage();
		Main.errorsLog.appendLog(new String(byteMessage));
		Main.mc.send(byteMessage);
	}
	
	private byte[] buildMessage() throws IOException{
		String msg = "GETCHUNK 1.0 " + this.fileID + " " + this.chunkNO + " ";
		ByteArrayOutputStream msgStream = new ByteArrayOutputStream();
		msgStream.write(msg.getBytes());
		msgStream.write((byte) 0x0d);
		msgStream.write((byte) 0x0a);
		msgStream.write((byte) 0x0d);
		msgStream.write((byte) 0x0a);
		msgStream.close();
		return msgStream.toByteArray();
	}
}
