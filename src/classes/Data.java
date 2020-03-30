package classes;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Data implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private Map<Integer, Integer> huffmanCodes;
	private TreeMap<Integer, TreeSet<Character> > huffmanCodes;
	private BitSet bits;
	//private String bits;
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