package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;

import serviceInterfaces.Chunk;

public class MdbHandler extends Thread {
	private Queue<String> msgQueue;
	private Chunk currentChunk;
	public MdbHandler(Queue<String> msgQueue) {
		this.msgQueue = msgQueue;
	}
	
	@Override
	public void run() {
		while (!isInterrupted()) {
			if (!msgQueue.isEmpty()) {
				String[] msg = msgQueue.poll().split("\\s+");
				byteContents(msg);
				try {
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Write your data
				/*FileOutputStream out = new FileOutputStream("chunks"+ File.separator"the-file-name");
				out.write(currentChunk.content);
				out.close();*/
				/*fileOut = new FileOutputStream(new File("chunks"+ File.separator + new String(currentChunk.fileID) + File.separator + currentChunk.chuckNumber + ".bin"));
				fileOut.write(currentChunk.content);
				fileOut.flush();
				fileOut.close();
				*/ catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private byte[] byteContents(String[] msg) {
		if (!msg[0].equals("PUTCHUNK") || !msg[1].equals("1.0")) return null;
		currentChunk = new Chunk();
		currentChunk.chuckNumber = Integer.parseInt(msg[3]);
		currentChunk.fileID = msg[2].getBytes();
		currentChunk.replicationDegree = Integer.parseInt(msg[4]);
		String stringBody = new String(msg[5]);
		for (int i = 6; i < msg.length; ++i)
			stringBody += " " + msg[i];
		currentChunk.content = stringBody.getBytes();
		return stringBody.getBytes();
	}
}
