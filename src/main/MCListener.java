package main;

import java.io.IOException;
import java.net.SocketException;
import java.util.Queue;

import serviceInterfaces.MulticastChannel;

public class MCListener extends Thread {
	private MulticastChannel mc;
	private Queue<String> receivedMsgs;

	public MCListener(MulticastChannel MC, Queue<String> receivedMsgs) {
		this.mc = MC;
		this.receivedMsgs = receivedMsgs;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				byte[] received = new byte[8192];
				mc.receive(received);
				receivedMsgs.add(new String(received));
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
}
