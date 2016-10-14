package polynomialCalculation;

public class HandlerExit extends Handler{

	public HandlerExit(Calculator c) {
		super(c);
	}

	@Override
	public boolean isExit() 
	{
		return true ;	
	}
	
}
