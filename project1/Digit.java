package polynomialCalculation;

public class Digit extends Data {

	private Integer d;
	
	public Digit(int d) { this.d = new Integer(d); }

	public int getContent(){ return d.intValue(); }
	
	public void set(int d) { this.d = new Integer(d); }

	@Override
	public char[] get() { return d.toString().toCharArray();} 
	
}
