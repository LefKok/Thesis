	public synchronized void fade() {
		for (MyTransaction t : shares)
			if ((t.fading -= 0.02) <= 0)
				shares.remove(t);}

	double calculate_Trust() {
		if ((Math.random() < 0.2) && (shares.size() < 2))
			return 1.0; // if new friend (20% chance to trust him)
		if (shares.size() >10){
		double temp1 = get_trust(shares, shares.size()/10);
		double temp2 = get_trust(shares, shares.size());
		trust = Math.min(temp1, temp2);
		}
		else if(shares.size() >0) trust = get_trust(shares, shares.size());
		return trust;
	}

	private double get_trust( LinkedBlockingDeque<MyTransaction> assists2, int size) {
		double sum = 0.0;// wi*xi
		double weights = 0.0;// wi
		double wx2 = 0.0;// wi*xi^2
		for (int i = 0; i < size; i++) {
			MyTransaction t = assists2.poll();
			if (t!=null){
			double weight = t.fading * t.severity;
			double xi = t.getSatisfaction().getSatisfactionValue();
			double temp = weight * xi;
			sum += temp; // �wi*xi
			wx2 += temp * xi;// �wi*xi*xi
			weights += weight;// �wi
			}}
		fade();
		double m = sum / weights;
		double s = (wx2 * weights - Math.pow(sum, 2)) / Math.pow(weights, 2);
		return m - s;
	}

	public MyOutcome requestService(String Service) {
		SatisfactionInterval satisfaction = null;
			double temp=Sensor.serve(Service);
			satisfaction = new SatisfactionInterval(0.0, 1.0,
					temp);
		return new MyOutcome(satisfaction);
	}

