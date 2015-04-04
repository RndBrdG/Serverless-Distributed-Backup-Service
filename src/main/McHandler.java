package main;

import java.io.File;
import java.util.Queue;

public class McHandler extends Thread {
		private Queue<String> msgQueue;
		private volatile int numberOfConfirmations;
		private boolean restore;
		
		public McHandler(Queue<String> msgQueue) {
			this.msgQueue = msgQueue;
			this.numberOfConfirmations = 0;
			this.restore = false;
		}

		@Override
		public void run() {
			while (!isInterrupted()) {
				if (!msgQueue.isEmpty()) {
					String[] msg = msgQueue.poll().split("\\s",5);
					boolean isValid = updateByteContents(msg);
					if (isValid && restore) {
						String filename = new String("CHUNKS" + File.separator + new String(msg[2]) + File.separator + msg[3]);
						File tmp = new File(filename);
					}
					else Main.errorsLog.appendLog("Message not properly written");
				}
			}
		}

		private boolean updateByteContents(String[] msg) {
			if (msg[0].equals("STORED") && msg[1].equals("1.0")) {
				this.numberOfConfirmations += 1;
				restore = false;
			}
			else if (msg[0].equals("GETCHUNK") && msg[1].equals("1.0")) {
				restore = true && fileExists(msg);
			}
			else return false;

			return true;
		}
		
		private boolean fileExists(String[] msg){
			File file = new File("BACKUP" + File.separator + msg[2] + File.separator + msg[3]);
			return file.exists();
		}
		
		public int getNumberOfconf(){
			return this.numberOfConfirmations;
		}
	
}
