import java.io.Serializable;


public class Message implements Serializable{
	
	String source;
	String destination;
	String kind;
	String data;
	int sequenceNumber;
	String action;
	
	public Message(String dest, String kind, Object data){
		destination = dest;
		this.kind = kind;
		this.data = (String)data;
	}
	
	public void set_source(String source){
		this.source = source;
	}
	
	public void set_seqNum(int sequenceNumber){
		this.sequenceNumber = sequenceNumber;
	}
	
	public void set_duplicate(Boolean dupe){
		
	}
	
	public void set_action(String action){
		this.action = action;
	}
}
