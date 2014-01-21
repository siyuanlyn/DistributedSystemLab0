import java.io.Serializable;


@SuppressWarnings("serial")
public class Message implements Serializable{
	
	String source;
	String destination;
	String kind;
	String data;
	int sequenceNumber;
	String action;
	boolean duplicate = false;
	
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
		duplicate = dupe;
	}
	
	public void set_action(String action){
		this.action = action;
	}
	
	public String toString(){
		return "[source=" + source + "; destination=" + destination + "; kind=" + kind
				+ "; data=" + data + "; seqNum=" + sequenceNumber + "; action=" + action
				+ "; duplicate=" + duplicate + "]" ;
	}
}
