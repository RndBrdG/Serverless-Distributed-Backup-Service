package main;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

	public void decrementChunkReplication(byte[] fileId, int chunkNo) {
		for (Chunk targetChunk : storedChunks) {
			if (targetChunk.getFileId().equals(fileId) && targetChunk.getChunkNumber() == chunkNo) {
				targetChunk.decrementReplication();
				break;
			}
		}
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			if (usedSpace > totalSpace) {
				Chunk removed = storedChunks.remove(0); // Falta verificação para ver se desce do nível de replicação desejável
				File toRemove = new File("chunks" + File.separator + removed.getFileId() + File.separator + removed.getChunkNumber());
				toRemove.delete();
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
}
