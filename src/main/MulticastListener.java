package main;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import serviceInterfaces.MulticastChannel;

public class MulticastListener extends Thread {
	private MulticastChannel mchannel;
	private ArrayList<String> receivedMsgs;

	public MulticastListener(MulticastChannel MC) {
		this.mchannel = MC;
		ArrayList<String> receivedMsgs = new ArrayList<String>();
		this.receivedMsgs = receivedMsgs;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				byte[] received = new byte[8192];
				//System.out.println("Listener");
				mchannel.receive(received);
				receivedMsgs.add(new String(received));
				//System.out.println(new String(received));
			}
			catch (SocketException e) {
				break;
			}
			catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public ArrayList<String> getQueue(){
		return this.receivedMsgs;
	}
}
