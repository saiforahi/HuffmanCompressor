package classes;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Map;

public class Data implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Integer, Integer> huffmanCodes;
	private BitSet bits;
	
	public Data() {
		
	}
	
	public Data(Map<Integer, Integer> newMap,BitSet newBits) {
		this.huffmanCodes=newMap;
		this.bits=newBits;
	}
	
	public Map<Integer, Integer> get_map() {
		return this.huffmanCodes;
	}
	
	public BitSet get_bits() {
		return this.bits;
	}
}