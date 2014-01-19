import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ReadInputStream extends Thread{
	ObjectInputStream ois;
	@SuppressWarnings("rawtypes")
	ConcurrentLinkedQueue messageQueue;
	public ReadInputStream(Socket clientSocket, ConcurrentLinkedQueue messageQueue) throws IOException{
		ois = new ObjectInputStream(clientSocket.getInputStream());
		this.messageQueue = messageQueue;
	}

	@SuppressWarnings("unchecked")
	public void run(){
		while(true){
			synchronized(messageQueue){
				try {
					messageQueue.offer(ois.readObject());
				} catch (IOException e) {
					e.printStackTrace();
				}
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
