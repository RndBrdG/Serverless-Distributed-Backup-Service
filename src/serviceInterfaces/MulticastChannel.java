package serviceInterfaces;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

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
		System.out.println("Sent: " + new String(toSend));
	}
	
	public int receive(byte[] data) throws IOException {
		byte[] strBuf = new byte[8192];
		DatagramPacket packet = new DatagramPacket(strBuf, strBuf.length);
		socket.receive(packet);
		data = packet.getData();
		System.out.println("Received: " + new String(data));
		return packet.getLength();
	}
	
	public void close() throws IOException {
		socket.leaveGroup(group);
		socket.close();
	}
}
