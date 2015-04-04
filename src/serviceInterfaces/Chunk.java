package serviceInterfaces;

public class Chunk implements Comparable<Chunk> {
	private byte[] fileId;
	private int targetReplicationDegree;
	private int actualReplicationDegree;
	private int chunkNumber;
	private byte[] content;
	
	public Chunk(byte[] fileId, int targetReplicationDegree, int chunkNumber, byte[] content) {
		this.fileId = fileId;
		this.targetReplicationDegree = targetReplicationDegree;
		this.chunkNumber = chunkNumber;
		this.content = content;
	}
	
	public byte[] getFileId() {
		return fileId;
	}
	
	public int getTargetReplicationDegree() {
		return targetReplicationDegree;
	}
	
	public int getActualReplicationDegree() {
		return actualReplicationDegree;
	}
	
	public int getChunkNumber() {
		return chunkNumber;
	}
	
	public byte[] getContent() {
		return content;
	}

	@Override
	public int compareTo(Chunk chunk2) {
		return actualReplicationDegree < chunk2.actualReplicationDegree ? -1 : actualReplicationDegree > chunk2.actualReplicationDegree ? 1 : 0;
	}
}
