package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;

import serviceInterfaces.Chunk;

public class MdrHandler extends Thread{
	private Queue<String> msgQueue;
	private Chunk currentChunk;
	
	public MdrHandler(Queue<String> msgQueue) {
		this.msgQueue = msgQueue;
	}
	
	public void run() {
		while (!isInterrupted()) {
			if (!msgQueue.isEmpty()) {
				String[] msg = msgQueue.poll().split("\\s",5);
				boolean isValid = updateByteContents(msg);
				if (isValid){
					createFile();
				}
				else Main.errorsLog.appendLog("Something went to the wrong channel!");
			}
		}
	}

	private void createFile() {
		String filename = new String("restore" + File.separator + new String(currentChunk.getFileId()) + File.separator + currentChunk.getChunkNumber());
		try {
			File tmp = new File(filename);
			if (!tmp.getParentFile().exists())
				tmp.getParentFile().mkdirs();
			if (!tmp.exists())
				tmp.createNewFile();
			
			FileOutputStream out = new FileOutputStream(filename);
			out.write(currentChunk.getContent());
			out.close();
			
			
			String[] value = getCurrentFile();
			if (currentChunk.getChunkNumber() == 1+Integer.parseInt(value[1])/64000){
				mergeFiles(value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean updateByteContents(String[] msg) {
		if (!msg[0].equals("CHUNK") || !msg[1].equals("1.0")) return false;
		String stringBody = new String(msg[4].substring(4));
		currentChunk = new Chunk(msg[2].getBytes(),0, Integer.parseInt(msg[3]), stringBody.getBytes(StandardCharsets.ISO_8859_1));
		return true;
	}
	
	private String[] getCurrentFile(){
		String[] temp = null;
		for (Map.Entry<String, String> entry : Main.files.entrySet()) {
			String value = entry.getValue();
			value += " " + entry.getKey();
			 temp = value.split(" ");
			 if (temp[0].equals(new String(currentChunk.getFileId()))){
				 break;
			 }
		}
		return temp;
	}
	
	private void mergeFiles(String[] info){
		File newFile = new File("RESTORE"+ File.separator + info[0] + File.separator + info[2]);
		FileOutputStream out;
		FileInputStream in;
		byte[] infoBytes;
		
		ArrayList<File> filesList = new ArrayList<File>();
		for(int i = 0; i <= Integer.parseInt(info[1]) / 64000; i++){
			int j = i + 1;
			filesList.add(new File("restore"+ File.separator + info[0] + File.separator + j));
		}
		
		try {
			out = new FileOutputStream(newFile,true);
			for (File file : filesList) {
				in = new FileInputStream(file);
				infoBytes = new byte[(int) file.length()];
				in.read(infoBytes, 0, (int) file.length());
				out.write(infoBytes);
				out.flush();
				in.close();
				in = null;
				infoBytes = null;
				file.delete();
			}
			out.close();
			filesList.clear();
		} catch (Exception exception){
            exception.printStackTrace();
        }
	}
}
