package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
				String[] msg = msgQueue.poll().split("\\s",6);
				updateByteContents(msg);
				try {
					String filename = new String("chunks" + File.separator + new String(currentChunk.getFileId()) + File.separator + currentChunk.getChunkNumber());
					File tmp = new File(filename);
					tmp.getParentFile().mkdirs();
					tmp.createNewFile();
					
					FileOutputStream out = new FileOutputStream(filename);
					out.write(currentChunk.getContent());
					out.close();
				} catch (IOException e) {
					 e.printStackTrace();
				 }
			}
		}
	}

	private byte[] updateByteContents(String[] msg) {
		if (!msg[0].equals("PUTCHUNK") || !msg[1].equals("1.0")) return null;
		
		String stringBody = new String(msg[5].substring(3));

		currentChunk = new Chunk(msg[2].getBytes(), Integer.parseInt(msg[4]), Integer.parseInt(msg[3]), stringBody.getBytes(StandardCharsets.ISO_8859_1));
		return stringBody.getBytes();
	}
}
