package main;

import java.util.Queue;

public class McHandler extends Thread {
		private Queue<String> msgQueue;
		private volatile int numberOfConfirmations;
		public McHandler(Queue<String> msgQueue) {
			this.msgQueue = msgQueue;
			this.numberOfConfirmations = 0;
		}

		@Override
		public void run() {
			while (!isInterrupted()) {
				if (!msgQueue.isEmpty()) {
					String[] msg = msgQueue.poll().split("\\s",5);
					boolean isValid = updateByteContents(msg);
					if (isValid){
						this.numberOfConfirmations += 1;
					}
				}
			}
		}

		private boolean updateByteContents(String[] msg) {
			// POR AGORA E SO STORED
			if (!msg[0].equals("STORED") || !msg[1].equals("1.0")) return false;

			return true;
		}
		
		public int getNumberOfconf(){
			return this.numberOfConfirmations;
		}
	}
