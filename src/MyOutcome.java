public class MyOutcome extends BasicOutcome {

    protected SatisfactionDiscreteScale satisfaction;

	public MyOutcome(SatisfactionInterval satisfaction) {
		super(satisfaction,satisfaction.getSatisfactionValue(),1);

	}

}
