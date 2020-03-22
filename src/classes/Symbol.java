package classes;

public class Symbol implements Comparable<Symbol>{
	private char character;
	private int frequency_value;
	private int ascii_value;
	private String path;
	public Symbol() {
		this.frequency_value=0;
		this.path=null;
	}
	public Symbol(char newChar,int newFrequency) {
		this.character=newChar;
		this.frequency_value=newFrequency;
		this.ascii_value=(int)this.character;
		this.path=null;
	}
	public void set_character(char newChar) {
		this.character=newChar;
		this.ascii_value=(int)newChar;
	}
	public char get_character() {
		return this.character;
	}
	public int get_frequency() {
		return this.frequency_value;
	}
	public void set_frequency(int newValue) {
		this.frequency_value=newValue;
	}
	
	public void increment_frequency(int value) {
		this.frequency_value+=value;
	}
	
	public int get_ascii_value() {
		return this.ascii_value;
	}
	
	public String get_path() {
		return this.path;
	}
	
	public void set_path(String newPath) {
		this.path=newPath;
	}
	@Override
	public int compareTo(Symbol arg0) {
		if(this.get_frequency() < arg0.get_frequency())
		    return 1;
		else 
		    return -1;
		
	}

}
