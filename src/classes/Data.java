package classes;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Map;

public class Data implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Character, String> huffmanCodes;
	private BitSet bits;
	public Data() {
		
	}
	
	public Data(Map<Character, String> huffmanCodes,BitSet newBits) {
		this.huffmanCodes=huffmanCodes;
		this.bits=newBits;
	}
	
	
	public BitSet get_bits() {
		return this.bits;
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