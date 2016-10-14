package polynomialCalculation;

public class HandlerDerivation extends Handler {

	public HandlerDerivation(Calculator c) {
		super(c);
	}

	@Override
	public void doCmd(String cmd) {
		c.derivation(cmd.substring(4));
	}

}
