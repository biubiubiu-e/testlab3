package polynomialCalculation;

public class node{	
	
	protected node left;
	protected node right;	
	
	public node()
	{
		left = null;
		right = null;
	}

	public node getLeft(){ return null; }
	public node getRight(){ return null; }
	public char[] get(){ return null; }
	
	public void manageLeft(node n) {
		this.left = n ;
	}
	
	public void manageRight(node n) {
		this.right = n ;
	}

}