package classes;

import java.io.Serializable;
import java.util.Map;

public class Data implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	
	
	/*@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) { 
		try {
			this.huffmanCodes = (Map<Character,String>) in.readObject();
	        this.bits = in.readByte();
	        accountNumber = aInputStream.readInt();
	        dateOpened = new Date(aInputStream.readLong());
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			
		}
        
    }
	 
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
        aOutputStream.writeUTF(firstName);
        aOutputStream.writeUTF(lastName);
        aOutputStream.writeInt(accountNumber);
        aOutputStream.writeLong(dateOpened.getTime());
    }*/
}