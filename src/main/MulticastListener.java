package main;

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

import serviceInterfaces.MulticastChannel;

public class MulticastListener extends Thread {
	private MulticastChannel multicastChannel;
	private Queue<String> receivedMsgs;

	public MulticastListener(MulticastChannel multicastChannel) {
		this.multicastChannel = multicastChannel;
		Queue<String> receivedMsgs = new LinkedList<String>();
		this.receivedMsgs = receivedMsgs;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				byte[] buf = new byte[65536];
				//System.out.println("Listener");
				byte[] data = multicastChannel.receive(buf);
				receivedMsgs.add(new String(data, StandardCharsets.ISO_8859_1));
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
	
	public Queue<String> getQueue() {
		return this.receivedMsgs;
	}
}
