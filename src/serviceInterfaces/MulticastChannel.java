package serviceInterfaces;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import main.Main;

public class MulticastChannel {
	private InetAddress group;
	private int port;
	private MulticastSocket socket;

	public MulticastChannel(String ip, int port) throws UnknownHostException {
		group = InetAddress.getByName(ip);
		this.port = port;
	}

	public void join() throws IOException {
		socket = new MulticastSocket(port);
		socket.joinGroup(group);
	}

	public void send(byte[] toSend) throws IOException {
		DatagramPacket packet = new DatagramPacket(toSend, toSend.length, group, port);
		socket.send(packet);
		//System.out.println("Sent: " + new String(toSend));
		String logString = "Sent from " + packet.getAddress().toString() + " : one chunk!" + '\n' + "------------------";
		Main.logfile.appendLog(logString);
	}

	public void receive(byte[] data) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, data.length);
		socket.receive(packet);
		//System.out.println("Received: " + new String(data));
		String logString = "Received from " + packet.getAddress().toString() + " : One chunk!" + '\n' + "------------------";
		Main.logfile.appendLog(logString);
	}

	public void close() throws IOException {
		socket.leaveGroup(group);
		socket.close();
	}
}
