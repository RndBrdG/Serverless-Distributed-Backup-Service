package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import serviceInterfaces.Chunk;

public class SpaceManager {
	private int totalSpace;
	private int usedSpace;
	private ArrayList<Chunk> storedChunks;

	public SpaceManager(int totalSpace) throws FileNotFoundException, UnsupportedEncodingException {
		this.totalSpace = totalSpace;
		this.storedChunks = new ArrayList<Chunk>();
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

	public ArrayList<Chunk> getStoredChunks(){
		return this.storedChunks;
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

	public void makeSpace() {
		while (usedSpace >= totalSpace) {
			Iterator<Chunk> chunkIterator = storedChunks.iterator();
			if (!chunkIterator.hasNext()) return;

			Chunk chunkToRemove = chunkIterator.next();

			while (chunkIterator.hasNext()) {
				int replicationDegreeDifference = chunkToRemove.getActualReplicationDegree() - chunkToRemove.getTargetReplicationDegree();
				Chunk nextChunk = chunkIterator.next();
				if (nextChunk.getActualReplicationDegree() - nextChunk.getTargetReplicationDegree() > replicationDegreeDifference)
					chunkToRemove = nextChunk;
			}

			if (chunkToRemove.getActualReplicationDegree() <= 1) return;

			System.out.println("Chunk no. " + chunkToRemove.getChunkNumber() + " of file ID " + chunkToRemove.getFileId() + " removed!");

			storedChunks.remove(chunkToRemove);

			File fileToRemove = new File("chunks" + File.separator + chunkToRemove.getFileId() + File.separator + chunkToRemove.getChunkNumber());
			fileToRemove.delete();
		}
	}
}
