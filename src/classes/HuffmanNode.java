package classes;

public class HuffmanNode implements Comparable<HuffmanNode> {
	public int frequency_value; 
    public int askii; 
    public HuffmanNode left; 
    public HuffmanNode right;
	@Override
	public int compareTo(HuffmanNode o) {
		if(this.frequency_value > o.frequency_value)
		    return 1;
		else 
		    return -1;
	} 
}
