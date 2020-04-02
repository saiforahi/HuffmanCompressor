package classes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class Data{
	private Map<Character, String> huffmanCodes;
	private byte[] bytes;
	public Data() {
		
	}
	
	public Data(Map<Character, String> huffmanCodes,byte[] newBits) {
		this.huffmanCodes=huffmanCodes;
		this.bytes=newBits;
	}
	
	
	public byte[] get_bytes() {
		return this.bytes;
	}
	public Map<Character, String> getCodeBook() {
		return this.huffmanCodes;
	}
	
	public void set_bytes(byte[] newBits) {
		this.bytes=newBits;
	}
	public void setCodeBook(Map<Character, String> huffmanCodes) {
		this.huffmanCodes=huffmanCodes;
	}
	
	@SuppressWarnings("unchecked")
	public void readObject(ObjectInputStream in) { 
		try {
			this.huffmanCodes = (Map<Character,String>) in.readObject();
	        this.bytes = (byte[])in.readObject();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			
		}
        
    }
	 
    public void writeObject(ObjectOutputStream out) {
    	try {
    		//out.defaultWriteObject();
    		out.writeObject(huffmanCodes);
    		out.writeObject(bytes);
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
       
    }
}