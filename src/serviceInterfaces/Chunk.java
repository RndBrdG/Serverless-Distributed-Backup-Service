package serviceInterfaces;

public class Chunk {
	private byte[] fileId;
	private int replicationDegree;
	private int chunkNumber;
	private byte[] content;
	
	public Chunk(byte[] fileId, int replicationDegree, int chunkNumber, byte[] content) {
		this.fileId = fileId;
		this.replicationDegree = replicationDegree;
		this.chunkNumber = chunkNumber;
		this.content = content;
	}
	
	public byte[] getFileId() {
		return fileId;
	}
	
	public int getReplicationDegree() {
		return replicationDegree;
	}
	
	public int getChunkNumber() {
		return chunkNumber;
	}
	
	public byte[] getContent() {
		return content;
	}
}
