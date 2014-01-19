import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ReadInputStream extends Thread{
	BufferedReader in;
	ConcurrentLinkedQueue messageQueue;
	public ReadInputStream(Socket clientSocket, ConcurrentLinkedQueue messageQueue) throws IOException{
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//		System.out.println("newed!");
		this.messageQueue = messageQueue;
	}
	public void run(){
		while(true){
//			System.out.println("start reading stream");
			
			synchronized(messageQueue){
//				System.out.println(clientSocket.getInputStream().available());
				try {
					messageQueue.offer(in.readLine());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				System.out.println(clientSocket.getInputStream().available());
			}
//			System.out.println("finish reading stream");
		}
	}
}
