package polynomialCalculation;

public class Character extends Data {

	private String word;
	private int index;
	
	public Character(String word){ 
		this.word = new String(word);
		index = 1;
	}
	public Character(char[] word){ 
		this.word = new String(word); 
		index = 1;
	}
	
	public String getContent(){ return word; }
	public int getIndex(){ return index; }
	
	public void set(String word){ this.word = new String(word); }
	public void setIndex(int index){ this.index = index; }
	@Override
	public char[] get() { return word.toCharArray(); } 

}
