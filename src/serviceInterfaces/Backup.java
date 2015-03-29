package serviceInterfaces;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import console.Console;

public class Backup {

	private byte[] hash;
	private String filename;
	private Integer replicationLevel;
	private String owner;
	
	public Backup() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		this.filename = new String(Console.getInputFromUser("What's the file you want to back up?"));
		this.replicationLevel = Integer.parseInt(new String(Console.getInputFromUser("What's the replication level?")));
		this.owner = new String(Console.getInputFromUser("Who's the owner of the file?"));
		
		// CREATE HASH 
		String toBeHashed = filename + owner + System.currentTimeMillis() + replicationLevel;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(toBeHashed.getBytes("UTF-16"));
		this.hash = md.digest();
		
		System.out.println(this.hash);
	}
}
