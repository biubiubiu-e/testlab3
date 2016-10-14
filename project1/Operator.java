package polynomialCalculation;

import java.util.ArrayList;

public class Operator extends node {
	
	private char ch;
	public ArrayList<node> son = new ArrayList<node>();
	
	public Operator(char ch) { this.ch = ch ; }
	
	public void set(char ch){ this.ch = ch ; }
	public char getContent(){ return ch; }
	public void addSon(node n){ son.add(n); }
	
	@Override
	public char[] get() {
		char[] ch = new char[1];
		ch[0] = this.ch ;
		return ch ;
	}

	@Override
	public node getLeft() { return left; }

	@Override
	public node getRight() { return right; }

}
