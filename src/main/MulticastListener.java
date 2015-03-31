package main;

import java.io.IOException;
import java.net.SocketException;
import java.util.Queue;

import serviceInterfaces.MulticastChannel;

public class MulticastListener extends Thread {
	private MulticastChannel MC;
	private Queue<String> receivedMsgs;

	public MulticastListener(MulticastChannel MC, Queue<String> receivedMsgs) {
		this.MC = MC;
		this.receivedMsgs = receivedMsgs;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				receivedMsgs.add(MC.receive());
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
