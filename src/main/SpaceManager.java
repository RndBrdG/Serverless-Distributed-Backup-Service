package main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import serviceInterfaces.Chunk;

public class SpaceManager extends Thread {
	private int totalSpace;
	private int usedSpace;
	private ArrayList<Chunk> storedChunks;

	public SpaceManager(int totalSpace) {
		this.totalSpace = totalSpace;
		this.storedChunks = new ArrayList<Chunk>();
	}

	public int getAvailableSpace() {
		return totalSpace;
	}

	public void setAvailableSpace(int totalSpace) {
		this.totalSpace = totalSpace;
	}

	public void addChunk(Chunk toAdd) {
		storedChunks.add(toAdd);
		usedSpace += toAdd.getContent().length;
	}

	@Override
	public void run() {
		while (usedSpace > totalSpace) {
			Chunk removed = storedChunks.remove(0); // Falta verificação para ver se desce do nível de replicação desejável
			String removedMsg = "REMOVED 1.0 " + removed.getFileId() + " " + removed.getChunkNumber() + " ";
			ByteArrayOutputStream msgStream = new ByteArrayOutputStream();
			try {
				msgStream.write(removedMsg.getBytes());
				msgStream.write((byte) 0x0d);
				msgStream.write((byte) 0x0a);
				msgStream.write((byte) 0x0d);
				msgStream.write((byte) 0x0a);
				msgStream.close();
				
				byte[] messageCompleted = msgStream.toByteArray();
				Main.mc.send(messageCompleted);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
