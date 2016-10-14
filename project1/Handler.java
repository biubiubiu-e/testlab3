package polynomialCalculation;

public class Handler {
	
	protected Calculator c ;
	
	protected Handler(Calculator c){
		this.c = c ;
	}
	
	public void doCmd(String cmd){
	}
	
	public boolean isExit() { return false ;}
}
