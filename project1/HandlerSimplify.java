package polynomialCalculation;

public class HandlerSimplify extends Handler {

	public HandlerSimplify(Calculator c) {
		super(c);
	}

	@Override
	public void doCmd(String cmd) {
		c.simplify(cmd.substring(9));
	}

}
