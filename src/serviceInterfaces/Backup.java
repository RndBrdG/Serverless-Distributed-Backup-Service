package serviceInterfaces;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
		//System.out.println(toBeHashed);
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		this.fileID = md.digest( toBeHashed.getBytes("UTF-8"));

		Main.files.put(this.filename, Main.bytesToHex(this.fileID));
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
				if (fileS < readLength)
					readLength = fileS;
				byteChunkPart = new byte[readLength];
				int read = readStream.read(byteChunkPart, 0, readLength);
				fileS -= read;
				chunkNo+=1;

				Chunk part = new Chunk(fileID, replicationLevel, chunkNo, byteChunkPart);
				this.chunkFiles.add(part);
			}
			readStream.close();

		}catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	private void sendingChunks() throws IOException{
		long Initial_t, current_t;
		long wait_multiplier = 1;
		int tries = 0;
		for(int i = 0; i < this.chunkFiles.size(); i++){
			Main.logfile.appendLog("-------- CHUNK " + Main.bytesToHex(this.fileID) + " No." + this.chunkFiles.get(i).getChunkNumber() + " --------" + '\n');
			String chunkInformation  = new String();
			chunkInformation = "PUTCHUNK " + "1.0 " + Main.bytesToHex(this.fileID) + " " + new Integer(this.chunkFiles.get(i).getChunkNumber()) + " " + new Integer(this.chunkFiles.get(i).getTargetReplicationDegree());
			ByteArrayOutputStream msgStream = new ByteArrayOutputStream();
			msgStream.write(chunkInformation.getBytes());
			msgStream.write((byte) 0x0d);
			msgStream.write((byte) 0x0a);
			msgStream.write((byte) 0x0d);
			msgStream.write((byte) 0x0a);
			msgStream.write(this.chunkFiles.get(i).getContent());
			byte[] messageCompleted = msgStream.toByteArray();
			do{
				Main.mdb.send(messageCompleted);
				Initial_t = System.currentTimeMillis();
				current_t = System.currentTimeMillis();
				while( current_t < Initial_t + 500*wait_multiplier)
					current_t = System.currentTimeMillis();
				Main.logfile.appendLog('\n' + "[INFORMATION] > CHUNK REPLICATION: " + this.replicationLevel + " | NUMBERFCONFIRMATIONS: " + Main.getNumberOfConfirmation());
				if ( Main.getNumberOfConfirmation() >= this.replicationLevel) break;
				else {
					wait_multiplier += 1;
					tries += 1;
					if (tries > 3) {
						Main.errorsLog.appendLog("There was something wrong. We tried 3 times to get it right, but we didn't have good answers");
						break;
					}
				}
			}while(true);
		}
		Main.logfile.appendLog('\n'+"-------- END OF CHUNK --------\n"+'\n');
	}
}
