package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import console.Console;
import serviceInterfaces.*;

public class Main {
	
	public static String ipMC;
	public static Integer portMC;
	public static String ipMDB;
	public static Integer portMDB;
	public static String ipMDR;
	public static Integer portMDR;
	private static Console console = null;
	private static serviceInterfaces.Backup backup = null;
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		if (args.length != 6){
			System.out.println("Usage: java <ipMC> <portMC> <ipMDB> <portMDB> <ipMDR> <portMDR>");
			return;
		}
		else {
			ipMC = args[0];
			portMC = Integer.parseInt(args[1]);
			ipMDB = args[2];
			portMDB = Integer.parseInt(args[3]);
			ipMDR = args[4];
			portMDR = Integer.parseInt(args[5]);
			
			// Subscreve o canal de multicast MC. Subscrever também o MDB e o MDR logo ao início?
			MulticastChannel MC = new MulticastChannel(ipMC, portMC);
			MC.join();
			
			console = new Console();
			
			switch(console.getUserOption()){
			case "BACKUP":
				backup = new Backup();
			}
		}
	}
}
