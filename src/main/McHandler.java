package main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Queue;

import serviceInterfaces.Chunk;

public class McHandler extends Thread {
		private Queue<String> msgQueue;
		private volatile int numberOfConfirmations;
		private boolean restore;
		private boolean delete;
		
		public McHandler(Queue<String> msgQueue) {
			this.msgQueue = msgQueue;
			this.numberOfConfirmations = 0;
			this.restore = false;
			this.delete = false;
		}

		@Override
		public void run() {
			while (!isInterrupted()) {
				if (!msgQueue.isEmpty()) {
					String[] msg = msgQueue.poll().split("\\s",5);
					boolean isValid = updateByteContents(msg);
					if (isValid && restore) {
						
						String filename = new String("chunks" + File.separator + new String(msg[2]) + File.separator + msg[3]);
						File tmp = new File(filename);
						long tmpSize = tmp.length();
						long readLength = 64000;
						FileInputStream readStream;
						byte[] byteChunkPart;
						try {
							readStream = new FileInputStream(tmp);
							if (tmpSize < 64000)
								readLength = tmpSize;
							byteChunkPart = new byte[(int) readLength];
							int read = readStream.read(byteChunkPart, 0, (int)readLength);
							readStream.close();
							sendChunk( createChunkMessage(byteChunkPart,msg[2],msg[3]));
						} catch (IOException exception) {
							exception.printStackTrace();
						}
					}
					else if (isValid && !restore && delete) deleteMessage(msg);
					else if (isValid && !restore) {}
					else Main.errorsLog.appendLog("Message is not properly written. FILE ID : " + msg[2]);
				}
			}
		}

		private boolean updateByteContents(String[] msg) {
			if (msg[0].equals("STORED") && msg[1].equals("1.0")) {
				numberOfConfirmations += 1;
				
				Chunk currentChunk = new Chunk(msg[2].getBytes(), 0, Integer.parseInt(msg[3]), msg[2].getBytes());
				
				for(Chunk tmp : Main.spaceManager.getStoredChunks()){
					if (tmp.compareTo(currentChunk) == 0)
						tmp.incrementReplication();
				}
				restore = false;
			}
			else if (msg[0].equals("GETCHUNK") && msg[1].equals("1.0")) {
				restore = true;
				return fileExists(msg);
			}
			else if (msg[0].equals("REMOVED") && msg[1].equals("1.0")) {
				Main.spaceManager.decrementChunkReplication(msg[2].getBytes(), Integer.parseInt(msg[3]));
				restore = false;
			}
			else if (msg[0].equals("DELETE") && msg[1].equals("1.0")){
				restore = false;
				delete = true;
			}
			else return false;

			return true;
		}
		
		private boolean fileExists(String[] msg){
			File file = new File("chunks" + File.separator + msg[2] + File.separator + msg[3]);
			return file.exists();
		}
		
		public int getNumberOfconf(){
			return numberOfConfirmations;
		}
		
		public void resetNumberOfConf(){
			numberOfConfirmations = 0;
		}
	
		private void sendChunk(byte[] messageCompleted) throws IOException{
			Main.mdr.send(messageCompleted);
		}
		
		private byte[] createChunkMessage(byte[] data, String fileid, String chunkNo) throws IOException{
			String chunkInformation = "CHUNK 1.0 " + fileid + " " + chunkNo + " ";
			ByteArrayOutputStream msgStream = new ByteArrayOutputStream();
			msgStream.write(chunkInformation.getBytes());
			msgStream.write((byte) 0x0d);
			msgStream.write((byte) 0x0a);
			msgStream.write((byte) 0x0d);
			msgStream.write((byte) 0x0a);
			msgStream.write(data);
			byte[] messageCompleted = msgStream.toByteArray();
			return messageCompleted;
		}
		
		private void deleteMessage(String[] msg) {
			String path = "chunks" + File.separator + msg[2];
			File direc = new File(path);
			if (direc.isDirectory()){
				String[] children = direc.list();
				for(int i = 0; i < children.length; i++){
					File f = new File("chunks" + File.separator + msg[2] + File.separator + children[i]);
					f.delete();
				}
				direc.delete();
			}
		}
}
