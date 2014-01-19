import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.yaml.snakeyaml.Yaml;


public class MessagePasser {

	LinkedHashMap networkTable;
	HashMap<String, Node> nodeMap = new HashMap<String, Node>();
	HashMap<String, Socket> socketMap = new HashMap<String, Socket>();
	ServerSocket serverSocket;
	ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();
	ArrayList<LinkedHashMap<String, String>> configList;
	ArrayList<LinkedHashMap<String, String>> sendRuleList;
	ArrayList<LinkedHashMap<String, String>> receiveRuleList;

	@SuppressWarnings("unchecked")
	public MessagePasser(String configuration_filename, String local_name) throws IOException{
		InputStream input = new FileInputStream(configuration_filename);
		Yaml yaml = new Yaml();
		Object data = yaml.load(input);
		input.close();
		networkTable = (LinkedHashMap)data;	//get the information
		configList = (ArrayList<LinkedHashMap<String, String>>) networkTable.get("configuration");
		sendRuleList = (ArrayList<LinkedHashMap<String, String>>) networkTable.get("sendRules");
		receiveRuleList = (ArrayList<LinkedHashMap<String, String>>) networkTable.get("receiveRules");
		System.out.println(sendRuleList.toString());
		System.out.println(receiveRuleList.toString());
		for(Map m : configList){
			String name = (String)m.get("name");
			String ip = (String)m.get("ip");
			int port = (int)m.get("port");
			nodeMap.put(name, new Node(ip, port));
		}

		int portNumber = nodeMap.get(local_name).port;
		serverSocket = new ServerSocket(portNumber);

	}
	
	

	void send(Message message) throws UnknownHostException, IOException{
		System.out.println("sending..................");
		message.set_action(checkSendingRules(message));
		System.out.println(message.action);
		switch(message.action){
		case "drop":
			
			break;
		case "duplicate":
			
			break;
		case "delay":
			
			break;
		case "none":
			
			break;
		}
		
		
		
		
		if(!socketMap.containsKey(message.destination)){
			Socket destSocket = new Socket(InetAddress.getByName(nodeMap.get(message.destination).ip), nodeMap.get(message.destination).port);
			socketMap.put(message.destination, destSocket);
		}
		Socket destSocket = socketMap.get(message.destination);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(destSocket.getOutputStream()),true);
		out.println(message);
	}

	Message receive(){
		
		Message receivedMessage;
		if(!messageQueue.isEmpty()){
			receivedMessage = messageQueue.poll();
			return receivedMessage;
		}
		else{
			return null;
		}
	}

	String checkSendingRules(Message message){

		for(Map m : sendRuleList){

			boolean srcMatch = false;
			boolean dstMatch = false;
			boolean seqMatch = false;
			boolean kindMatch = false;

			if(!m.containsKey("src")){
				srcMatch = true;
			}
			else if(m.get("src").equals(message.source)){
				srcMatch = true;
			}
			
			if(!m.containsKey("dest")){
				dstMatch = true;
			}
			else if(m.get("dest").equals(message.destination)){
				dstMatch = true;
			}

			if(!m.containsKey("seqNum")){
				seqMatch = true;
			}
			else if((int)m.get("seqNum") == message.sequenceNumber){
				seqMatch = true;
			}
			
			if(!m.containsKey("kind")){
				kindMatch = true;
			}
			else if(m.get("kind").equals(message.kind)){
				kindMatch = true;
			}

			if(srcMatch && dstMatch && seqMatch && kindMatch){
				return (String)m.get("action");
			}
				
		}
		return "none";
	}
	
	String checkReceivingRules(Message message){

		for(Map m : sendRuleList){

			boolean srcMatch = false;
			boolean dstMatch = false;
			boolean seqMatch = false;
			boolean kindMatch = false;

			if(m.get("src").equals(message.source))	            srcMatch = true;
			if(m.get("dest").equals(message.destination))	    dstMatch = true;
			if((int)m.get("seqNum") == message.sequenceNumber)	seqMatch = true;
			if(m.get("kind").equals(message.kind))	            kindMatch = true;

			if(srcMatch && dstMatch && seqMatch && kindMatch){
				return (String)m.get("action");
			}
				
		}
		return "none";
	}
}
