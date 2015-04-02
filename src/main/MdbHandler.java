package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
				updateByteContents(msg);
				try {
					String filename = new String("chunks" + File.separator + new String(currentChunk.getFileId()) + File.separator + currentChunk.getChunkNumber());
					File tmp = new File(filename);
					tmp.getParentFile().mkdirs();
					tmp.createNewFile();

					FileOutputStream out = new FileOutputStream(filename);
					out.write(currentChunk.getContent());
					out.close();
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
					 e.printStackTrace();
				 }
			}
		}
	}

	private byte[] updateByteContents(String[] msg) {
		if (!msg[0].equals("PUTCHUNK") || !msg[1].equals("1.0")) return null;

		String stringBody = new String(msg[5]);
		for (int i = 6; i < msg.length; ++i)
			stringBody += " " + msg[i];

		currentChunk = new Chunk(msg[2].getBytes(), Integer.parseInt(msg[4]), Integer.parseInt(msg[3]), stringBody.getBytes());

		return stringBody.getBytes();
	}
}
