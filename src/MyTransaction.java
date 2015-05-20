public class MyTransaction {
	private MyOutcome outcome;

	protected final double severity;
	protected double fading;
	protected String id=null;

	public MyTransaction(MyOutcome outcome, double severity,String id) {
		this.outcome = outcome;
		fading = 1.0;
		this.severity = severity;
		this.id=id;
	}
}
