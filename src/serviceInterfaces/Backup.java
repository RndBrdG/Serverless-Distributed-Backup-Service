package serviceInterfaces;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import console.Console;
import main.Main;

public class Backup {

	private byte[] fileID;  // Hash
	private String filename; // used for hashing only
	private String filepath; // used to get the file
	private long filesize;
	private Integer replicationLevel; 
	private String owner; // used for hashing only
	private ArrayList<Chunk> chunkFiles = new ArrayList<Chunk>();

	public Backup() throws NoSuchAlgorithmException, IOException{
		this.filepath = new String(Console.getInputFromUser("Where is the file you want to back up?"));
		this.replicationLevel = Integer.parseInt(new String(Console.getInputFromUser("What's the replication level?")));
		this.owner = new String(Console.getInputFromUser("Who's the owner of the file?"));
		getFileInfo();

		// CREATE HASH 
		String toBeHashed = filename + "-" + owner + "-" + System.currentTimeMillis() + "-" + replicationLevel;
		System.out.println(toBeHashed);
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		this.fileID = md.digest( toBeHashed.getBytes("UTF-8"));

		splitFile();
		sendingChunks();
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
				if (fileS < readLength){
					readLength = fileS;
				}
				System.out.println("ChunkFile " + chunkNo + " with " + readLength + " bytes.");
				byteChunkPart = new byte[readLength];
				int read = readStream.read(byteChunkPart, 0, readLength);
				fileS -= read;
				chunkNo+=1;

				Chunk part = new Chunk();
				part.chuckNumber = chunkNo;
				part.fileID = this.fileID;
				part.replicationDegree = this.replicationLevel;
				part.text = byteChunkPart;
				this.chunkFiles.add(part);
			}
			readStream.close();
		}catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	private void sendingChunks() throws IOException{
		String message = "PUTCHUNK " + "1.0 " + Main.bytesToHex(this.fileID);
		for(int i = 0; i < this.chunkFiles.size(); i++){
			//String text  = new String(this.chunkFiles.get(i).text);
			ByteArrayOutputStream msgStream = new ByteArrayOutputStream();
			msgStream.write(message.getBytes());
			msgStream.write(" ".getBytes());
			msgStream.write(new Integer(this.chunkFiles.get(i).chuckNumber).byteValue());
			msgStream.write(" ".getBytes());
			msgStream.write(new Integer(this.chunkFiles.get(i).replicationDegree).byteValue());
			msgStream.write((byte) 0x0d);
			msgStream.write((byte) 0x0a);
			msgStream.write((byte) 0x0d);
			msgStream.write((byte) 0x0a);
			msgStream.write(this.chunkFiles.get(i).text);
			byte[] messageCompleted = msgStream.toByteArray();

			System.out.println("ARRAY: " + new String(messageCompleted));
			Main.mc.send(messageCompleted);
		}
	}
}
