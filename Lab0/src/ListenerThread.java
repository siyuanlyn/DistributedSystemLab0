import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("rawtypes")
public class ListenerThread extends Thread{
	ServerSocket serverSocket;
	ConcurrentLinkedQueue messageQueue;
	public ListenerThread(ServerSocket serverSocket, ConcurrentLinkedQueue messageQueue) throws IOException{
		this.serverSocket = serverSocket;
		this.messageQueue = messageQueue;
	}
	public void run(){
		
		while(true){
			try {
				Socket client = serverSocket.accept();
				Thread readInputStreamThread = new ReadInputStream(client, messageQueue);
				readInputStreamThread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
