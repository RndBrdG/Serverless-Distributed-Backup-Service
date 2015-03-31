package main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Queue;

import serviceInterfaces.Backup;
import serviceInterfaces.MulticastChannel;
import console.Console;

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

			// Subscrever os canais de multicast MC, MDB e MDR
			MulticastChannel mc = new MulticastChannel(ipMC, portMC);
			mc.join();
			MulticastChannel mdb = new MulticastChannel(ipMDB, portMDB);
			mdb.join();
			MulticastChannel mdr = new MulticastChannel(ipMDR, portMDR);
			mdr.join();

			Queue<String> receivedMsgs = new LinkedList<String>(); // Fila com as mensagens escutadas no canal MC
			MulticastListener mcListener = new MulticastListener(mc, receivedMsgs);
			mcListener.start(); // Iniciar o thread de escuta

			console = new Console();

			switch (console.getUserOption()) {
			case "BACKUP":
				backup = new Backup();
			}

			// Parar o thread de escuta e fechar os canais de multicast
			mcListener.interrupt();
			mdr.close();
			mdb.close();
			mc.close();
		}

		console.endInput();
	}
}
