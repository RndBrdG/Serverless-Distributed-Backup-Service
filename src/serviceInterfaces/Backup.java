package serviceInterfaces;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import console.Console;

public class Backup {

	private byte[] fileID;  // Hash
	private String filename; // used for hashing only
	private String filepath; // used to get the file
	private long filesize;
	private Integer replicationLevel; 
	private String owner; // used for hashing only
	private ArrayList<Chunk> chunkFiles = new ArrayList<Chunk>();

	public Backup() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		this.filepath = new String(Console.getInputFromUser("Where is the file you want to back up?"));
		this.replicationLevel = Integer.parseInt(new String(Console.getInputFromUser("What's the replication level?")));
		this.owner = new String(Console.getInputFromUser("Who's the owner of the file?"));
		
		getFileInfo();
		
		// CREATE HASH 
		String toBeHashed = filename + owner + System.currentTimeMillis() + replicationLevel;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(toBeHashed.getBytes("UTF-16"));
		this.fileID = md.digest();
		
		splitFile();
		
		for(int i = 0; i < chunkFiles.size(); i++){
			System.out.println("ChunkFile: " + chunkFiles.get(i).chuckNumber);
			System.out.println("-----");
			System.out.println(chunkFiles.get(i).text.toString());
		}
	}
	
	private void getFileInfo(){
		File bkpfile = new File(this.filepath);
		this.filename = bkpfile.getName();
		this.filesize = bkpfile.length();
	}
	
	private void splitFile(){
		File bckFile = new File(this.filepath);
		FileInputStream readStream;
		int fileS = (int)this.filesize;
		int readLength = 64000;
		int chunkNo = 0;
		byte[] byteChunkPart;
		try {
			readStream = new FileInputStream(bckFile);
			while( fileS > 0){
				System.out.println("ChunkFile: " + chunkNo);
				if (fileS < readLength){
					readLength = fileS;
				}
				byteChunkPart = new byte[readLength];
				int read = readStream.read(byteChunkPart, 0, readLength);
				fileS -= read;
				chunkNo+=1;

				Chunk part = new Chunk();
				part.chuckNumber = chunkNo;
				part.fileID = this.fileID;
				part.replicationDegree = this.replicationLevel;
				part.text = byteChunkPart;
			}
			readStream.close();
		}catch (IOException exception) {
            exception.printStackTrace();
        }
	}
}
