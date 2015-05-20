    @Override
    public Outcome aggregate(Collection<Outcome> outcomes) {
        double _avgSatisfaction = avgSatisfaction;
        double _avgPathLength = avgPathLength;
        int numBenevolentPaths = outcomes.size()+1;
        Satisfaction aggregatedSatisfaction = null;

        if ((outcomes == null) || (outcomes.size() == 0))
            return null;

        for (Outcome outcome : outcomes) {
            _avgSatisfaction += ((BasicOutcome)outcome).get_avgSatisfaction();

            if (aggregatedSatisfaction == null)
                aggregatedSatisfaction = outcome.get_satisfaction();
            else
				aggregatedSatisfaction = aggregatedSatisfaction
						.aggregate(outcome.get_satisfaction());

 

        _avgSatisfaction = _avgSatisfaction/(outcomes.size()+1);
        
        return new Outcome(aggregatedSatisfaction,_avgSatisfaction);

