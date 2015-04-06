package main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.Random;

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
				String[] msg = msgQueue.poll().split("\\s",6);
				boolean isValid = updateByteContents(msg);
				if (isValid){
					try {
						String filename = new String("chunks" + File.separator + new String(currentChunk.getFileId()) + File.separator + currentChunk.getChunkNumber());
						File tmp = new File(filename);
						if (!tmp.getParentFile().exists())
							tmp.getParentFile().mkdirs();
						tmp.createNewFile();
						
						FileOutputStream out = new FileOutputStream(filename);
						out.write(currentChunk.getContent());
						out.close();
						sendConfirmationMessage(msg);
						Main.spaceManager.addChunk(currentChunk);
					} catch (IOException e) {
						 e.printStackTrace();
					 }
				}
			}
		}
	}

	private boolean updateByteContents(String[] msg) {
		if (!msg[0].equals("PUTCHUNK") || !msg[1].equals("1.0")) return false;
		
		String stringBody = new String(msg[5].substring(3));

		currentChunk = new Chunk(msg[2].getBytes(), Integer.parseInt(msg[4]), Integer.parseInt(msg[3]), stringBody.getBytes(StandardCharsets.ISO_8859_1));
		currentChunk.incrementReplication();
		
		if (!Main.spaceManager.getStoredChunks().contains(currentChunk))
			Main.spaceManager.addChunk(currentChunk);
		else {
			for(Chunk tmp : Main.spaceManager.getStoredChunks()){
				if (tmp.compareTo(currentChunk) == 0)
					tmp.incrementReplication();
			}
		}
		return true;
	}
	
	private void sendConfirmationMessage(String[] received) throws IOException{
		String confirmationMsg = new String();
		confirmationMsg += "STORED " + received[1] + " " + received[2] + " " + received[3] + " ";
		ByteArrayOutputStream msgStream = new ByteArrayOutputStream();
		msgStream.write(confirmationMsg.getBytes());
		msgStream.write((byte) 0x0d);
		msgStream.write((byte) 0x0a);
		msgStream.write((byte) 0x0d);
		msgStream.write((byte) 0x0a);
		msgStream.close();
		byte[] messageCompleted = msgStream.toByteArray();
		
		// GET TIME IN MILISECONDS
		long initial_t, current_t;
		Random rand = new Random();
		int randomNum = rand.nextInt(401);
		
		initial_t = System.currentTimeMillis();
		current_t = System.currentTimeMillis();
		while(current_t < initial_t + randomNum){
			current_t = System.currentTimeMillis();
		}
		Main.logfile.appendLog("[CONFIRMATION MESSAGE] > " + new String(messageCompleted).substring(0, messageCompleted.length-4) + "<CRLF><CRLF>");
		Main.mc.send(messageCompleted);
	}
}
