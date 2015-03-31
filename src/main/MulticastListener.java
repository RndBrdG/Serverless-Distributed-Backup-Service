package main;

import java.io.IOException;
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
		while (!Thread.interrupted()) {
			try {
				receivedMsgs.add(MC.receive());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Interrupted!\n");
	}
}
