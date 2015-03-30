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
	
	public void send(String msg) throws IOException {
		DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, group, port);
		socket.send(packet);
		System.out.println("Sent: " + msg);
	}
	
	public String receive() throws IOException {
		byte[] strBuf = new byte[4096];
		DatagramPacket packet = new DatagramPacket(strBuf, strBuf.length);
		socket.receive(packet);
		String received = new String(packet.getData(), 0, packet.getLength());
		System.out.println("Received: " + received);
		return received;
	}
	
	public void close() throws IOException {
		socket.leaveGroup(group);
		socket.close();
	}
}
