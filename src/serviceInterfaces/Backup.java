package serviceInterfaces;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
	}
	
	private void getFileInfo(){
		File bkpfile = new File(this.filepath);
		this.filename = bkpfile.getName();
		this.filesize = bkpfile.length();
	}
	
	private void splitFile(){
		/*
		double bytesAlreadyRead = 0;
		double bytesLeft = this.filesize;
		File bckfile = new File(this.filepath);
		boolean lastChunk = false;
		try {
			InputStream inStream = null;
			
			try {
				inStream = new BufferedInputStream( new FileInputStream(bckfile));
				
				while( bytesAlreadyRead < this.filesize){
					
					if (bytesLeft < 64000){
						lastChunk = true;
					}
					
					if (!lastChunk) 
				}
			}
		} */
	}
}
