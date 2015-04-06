package main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.PriorityQueue;

import serviceInterfaces.Chunk;

public class SpaceManager extends Thread {
	private int totalSpace;
	private int usedSpace;
	private PriorityQueue<Chunk> storedChunks;

	public SpaceManager(int totalSpace) throws FileNotFoundException, UnsupportedEncodingException {
		this.totalSpace = totalSpace;
		this.storedChunks = new PriorityQueue<Chunk>();
		File chunkLog = new File("chunkLog");
		String logName = "chunkLog";
		if (!chunkLog.isFile()) {
			PrintWriter writer = new PrintWriter(logName, "UTF-8");
			writer.println("==================" + '\n' + "   " + logName + " FILE     " + '\n' + "==================");
			writer.close();
		}
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
				Chunk chunkToRemove = null;
				{
					Iterator<Chunk> chunkIterator = storedChunks.iterator();
					chunkToRemove = chunkIterator.next();
					int replicationDegreeDifference = chunkToRemove.getActualReplicationDegree() - chunkToRemove.getTargetReplicationDegree();

					while (chunkIterator.hasNext()) {
						Chunk nextChunk = chunkIterator.next();
						if (nextChunk.getActualReplicationDegree() - nextChunk.getTargetReplicationDegree() > replicationDegreeDifference)
							chunkToRemove = nextChunk;
					}
				}
				if (chunkToRemove == null || chunkToRemove.getActualReplicationDegree() <= 1) continue;

				System.out.println("Deleting chunk " + chunkToRemove.getFileId() + " " + chunkToRemove.getChunkNumber());

				File toRemove = new File("chunks" + File.separator + chunkToRemove.getFileId() + File.separator + chunkToRemove.getChunkNumber());
				toRemove.delete();
				String removedMsg = "REMOVED 1.0 " + chunkToRemove.getFileId() + " " + chunkToRemove.getChunkNumber() + " ";
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
