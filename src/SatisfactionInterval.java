public class SatisfactionInterval implements Satisfaction {
	/** Lower bound of the interval */
	private double min;
	/** Upper bound of the interval */
	private double max;
	/** Actual satisfaction value */
	private double value;

	public SatisfactionInterval(double min, double max, double value) {
		if (min > max)
			min = max;
		if ((min > value) || (max < value))
			value = min;
		this.min = min;
		this.max = max;
		this.value = value;
	}
	
