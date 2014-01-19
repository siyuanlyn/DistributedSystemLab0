import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Application {
	
	static int sequenceNumber = 0;
	public static int generateSeqNum(){
		return sequenceNumber++;
	}
	
	public static void main(String[] args) throws IOException{
		MessagePasser messagePasser = new MessagePasser(args[0], args[1]);
		Thread listenerThread = new ListenerThread(messagePasser.serverSocket, messagePasser.messageQueue);
		listenerThread.start();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			System.out.println("What do you want to do?!?! send or receive?!?!");
			String method = in.readLine();
			String dest, sendingMessage;
			switch(method){
			case "send":
				System.out.println("Who do you want to send to?");
				dest = in.readLine();
				System.out.println("What do you want to say?");
				sendingMessage = in.readLine();
				Message message = new Message(dest,"Ack", sendingMessage);
				message.set_source(args[1]);
				message.set_seqNum(generateSeqNum());
				messagePasser.send(message);
				break;
			case "receive":
				System.out.println(messagePasser.receive().data);
				break;
			}
		}
	}
}
