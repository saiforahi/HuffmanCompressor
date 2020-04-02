package classes;

public class HuffmanNode implements Comparable<HuffmanNode>{
	
	public int frequency_value; 
    public int askii; 
    public HuffmanNode left; 
    public HuffmanNode right;
	@Override
	public int compareTo(HuffmanNode o) {
		if(this.frequency_value > o.frequency_value)
		    return 1;
		else if(this.frequency_value < o.frequency_value)
		    return -1;
		else
			return 0;
	}
	public int traverse(String s,int n) {
		if(this.right==null && this.left==null) {
			return this.askii;
		}
		else {
			if(s.charAt(n)=='0') {
				this.left.traverse(s,n+1);
			}
			else if(s.charAt(n)=='1') {
				this.right.traverse(s, n+1);
			}
		}
		return 0;
	}
}
