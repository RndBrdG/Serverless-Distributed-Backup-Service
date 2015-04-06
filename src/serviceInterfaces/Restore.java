package serviceInterfaces;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import main.Main;
import console.Console;

public class Restore {
	private String fileID;
	private String[] FILE;
	private String size;
	
	public Restore(){;
		this.FILE = Main.files.get(new String(Console.getInputFromUser("Please, insert the file name!")).toLowerCase()).split(" ");
		if (this.FILE == null) return;
		this.size = FILE[1];
		this.fileID = FILE[0];
	}
	
	public void start() throws IOException{
			int size = Integer.parseInt(this.size) / 64000;
			for(int i = 0; i <= size; i++){
				byte[] byteMessage = buildMessage(i+1);
				long Initial_t, current_t;
				Initial_t = System.currentTimeMillis();
				current_t = System.currentTimeMillis();
				Random rand = new Random();
				int randomNum = rand.nextInt(401);
				while( current_t < Initial_t + randomNum)
					current_t = System.currentTimeMillis();
				Date dNow = new Date();
				SimpleDateFormat time = new SimpleDateFormat ("hh:mm:ss");
				Main.logfile.appendLog("[SENDING RESTORE REQUEST][" + time.format(dNow) + "]");
				Main.mc.send(byteMessage);
			}
	}
	
	private byte[] buildMessage(int chunkNO) throws IOException {
		String msg = "GETCHUNK 1.0 " + this.fileID + " " + chunkNO + " ";
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
