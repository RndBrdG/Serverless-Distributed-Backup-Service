package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Queue;

public class MdbHandler extends Thread {
	private Queue<String> msgQueue;
	
	public MdbHandler(Queue<String> msgQueue) {
		this.msgQueue = msgQueue;
	}
	
	@Override
	public void run() {
		while (!isInterrupted()) {
			if (!msgQueue.isEmpty()) {
				String[] msg = msgQueue.poll().split("\\s");
				
				String fileId = null;
				int chunkNo = 0, replication = 0;
				byte[] chunkBody = byteContents(msg, fileId, chunkNo, replication);
				
				FileOutputStream fileOut = null;
				try {
					File theDir = new File("chunks");
					if (!theDir.exists())
					        theDir.mkdir();
					fileOut = new FileOutputStream("chunks/" + fileId + "/" + chunkNo);
					fileOut.write(chunkBody);
					fileOut.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private byte[] byteContents(String[] msg, String fileId, int chunkNo, int replication) {
		for(int i = 0; i < msg.length; i++)
			System.out.println(msg[i]);
		
		if (!msg[0].equals("PUTCHUNK") || !msg[1].equals("1.0")) return null;
		fileId = msg[2];
		chunkNo = Integer.parseInt(msg[3]);
		replication = Integer.parseInt(msg[4]);
		
		String stringBody = new String(msg[5].substring(4));
		for (int i = 6; i < msg.length; ++i)
			stringBody += " " + msg[i];
			
		return stringBody.getBytes();
	}
}
