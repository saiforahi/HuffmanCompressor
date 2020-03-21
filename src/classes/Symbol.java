package classes;

public class Symbol {
	private char character;
	private int frequency_value;
	
	public Symbol() {
		this.frequency_value=0;
	}
	
	public Symbol(char newChar,int newFrequency) {
		this.character=newChar;
		this.frequency_value=newFrequency;
	}
	public void set_character(char newChar) {
		this.character=newChar;
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
}
