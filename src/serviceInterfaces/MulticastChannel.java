package serviceInterfaces;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;

import main.Main;

public class MulticastChannel {
	private InetAddress group;
	private int port;
	private int numberOfConfirmations;
	private MulticastSocket socket;

	public MulticastChannel(String ip, int port) throws UnknownHostException {
		group = InetAddress.getByName(ip);
		this.port = port;
		this.numberOfConfirmations = 0;
	}

	public void join() throws IOException {
		socket = new MulticastSocket(port);
		socket.setTimeToLive(1);
		socket.joinGroup(group);
		socket.setLoopbackMode(true);
	}

	public void send(byte[] toSend) throws IOException {
		DatagramPacket packet = new DatagramPacket(toSend, toSend.length, group, port);
		socket.send(packet);
		String logString = "[" + packet.getAddress().toString() + "] : one chunk!";
		Main.logfile.appendLog("[SENDING MESSAGE] > " + logString);
	}

	public byte[] receive(byte[] buf) throws IOException {
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		byte[] data = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
		Date dNow = new Date();
		SimpleDateFormat time = new SimpleDateFormat ("hh:mm:ss dd.MM.yyyy");
		String logString = "[" + time.format(dNow) + " ] * " + packet.getAddress().toString() + "!";
		Main.logfile.appendLog("[RECEIVED MESSAGE] > " + logString);
		return data;
	}

	public void close() throws IOException {
		socket.leaveGroup(group);
		socket.close();
	}
}
