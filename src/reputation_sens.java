
	/**
	 * This method returns the outcome of finding a new friend from recommendation and requesting the service s
	 * @param String s the Service 
	 * */
	private  Outcome get_recommendation(String s) {
		Application_struct struct1=null;
		TemplateTRM_Sensor my_new_sensor =null;
		Application_struct recommenders = friends.get("recommendation");
		PriorityQueue<ComparableFriend> MyQueue=recommenders.rank_friends("trust");
		ArrayList<Followee_struct> recommendations =  new ArrayList<Followee_struct>();
		LinkedList<TemplateTRM_Sensor> recommender =  new LinkedList<TemplateTRM_Sensor>();
		while (!MyQueue.isEmpty() && recommendations.size()<4){//mazeyw protaseiw apo most trusted filous
			ComparableFriend temp = MyQueue.poll();
			if ( temp.value > 0.5)
			{			
				TemplateTRM_Sensor current = recommenders.get_followee(temp.id).Sensor;
				if (current!=null)  struct1 = current.get_app(s);
				if (struct1!=null)	my_new_sensor = struct1.request_Sensor(collusion,malicious);
				if (my_new_sensor!=null && my_new_sensor.isActive()){Followee_struct	next =new Followee_struct(my_new_sensor);
				recommendations.add(next);	recommender.add(current);}	
			}else break;
		}
		//Exw tis protaseis zhtaw feedback gia ton kathena kai vriskw reputation apo ton most reputable  ton kanw filo kai zhtaw service 
		double reputation=0.0;
		for (Followee_struct current : recommendations){
			current.reputation = calculate_reputation(current.Sensor.id,s);
			// System.out.println(current.reputation);
			if (current.reputation > reputation) current.reputation = reputation;}
		for (Followee_struct current : recommendations){
			TemplateTRM_Sensor his_recommender = recommender.pollFirst();
			if (current.reputation ==  reputation) 
			{		Application_struct Service = friends.get(s);
			addfollowee(current.Sensor,s,0.0);			//vazw filo kai zhtaw service
			Service.change_reputation(current.Sensor.id,s,current.reputation);
			MyOutcome outcome1=current.requestService(s);
			Service.addNewShare(Integer.toString(current.Sensor.id), outcome1);
			recommenders.addNewShare(Integer.toString(his_recommender.id), outcome1);//kanw evaluate ton arxiko recommender 
			return outcome1;
			}


		}

		return null;
	}
	/**
	 * This method caluclates the reputation of a VE for a Service
	 * @param id the VE 
	 * @param String service the Service 
	 * */
	private double calculate_reputation(int id, String service) {
		//zhtaw feedback apo high reputable kai to vazw se lista mazi me to pios to edwse
		LinkedList<MyTransaction> feedback = new LinkedList<MyTransaction>();
		double m = 0,s=0;
		Application_struct recommenders = friends.get("recommendation");
		PriorityQueue<ComparableFriend> MyQueue=recommenders.rank_friends("trust");
		for(int i=0; i<5 && !MyQueue.isEmpty() ;i++ ){
			ComparableFriend temp = MyQueue.poll();
			double experience=recommenders.get_followee(temp.id).Sensor.ask_experience(Integer.toString(id),service);
			if (experience >=0){
				double weight=temp.value;
				feedback.add(new MyTransaction(new MyOutcome(new SatisfactionInterval(0.0, 1.0, experience)),weight,temp.id));}
		}
		// calculate intial reputation distribution
		double sum = 0.0;// wi*xi
		double weights = 0.0;// wi
		double wx2 = 0.0;// wi*xi^2
		int j=0;

		if (feedback.size()>0){
			for ( j = 0; j < feedback.size(); j++) {
				MyTransaction t = feedback.get(j);
				double weight = t.fading * t.severity;
				double xi = t.getSatisfaction().getSatisfactionValue();
				double temp = weight * xi;
				sum += temp; // �wi*xi
				wx2 += temp * xi;// �wi*xi*xi
				weights += weight;// �wi
			}
			m = sum / weights;
			s = (wx2 * weights - Math.pow(sum, 2)) / Math.pow(weights, 2);
		}
		//next get more feedback
		for(int i=0; i<15 && !MyQueue.isEmpty() ;i++ ){
			ComparableFriend temp = MyQueue.poll();
			double experience=recommenders.get_followee(temp.id).Sensor.ask_experience(Integer.toString(id),service);
			if (experience >=0){
				double weight=temp.value;
				if ((experience < m-0.5*s) || (experience > m+0.5*s)) {
					recommenders.addNewShare(temp.id, (new MyOutcome(new SatisfactionInterval(0.0, 1.0, 0.3))));
				}
				else feedback.add(new MyTransaction(new MyOutcome(new SatisfactionInterval(0.0, 1.0, experience)),weight,temp.id));}
		}
		if (feedback.size()>=j){ //continue calculating the reputation with the new feedbacks
			for ( ; j < feedback.size(); j++) {
				MyTransaction t = feedback.get(j);
				double weight = t.fading * t.severity;
				double xi = t.getSatisfaction().getSatisfactionValue();
				double temp = weight * xi;
				sum += temp; // �wi*xi
				wx2 += temp * xi;// �wi*xi*xi
				weights += weight;// �wi
			}
			m = sum / (weights+0.000001);
			s = (wx2 * weights - Math.pow(sum, 2)) / Math.pow(weights+0.00001, 2);
			for (int i=0; i<feedback.size();i++){ 			//kane evaluate tous arxikous		
				MyTransaction t = feedback.get(i);
				if ((t.getSatisfaction().getSatisfactionValue() < m-0.5*s) || (t.getSatisfaction().getSatisfactionValue() > m+0.5*s)) {
					recommenders.addNewShare(t.id, (new MyOutcome(new SatisfactionInterval(0.0, 1.0, 0.0))));
				}else 	recommenders.addNewShare(t.id, (new MyOutcome(new SatisfactionInterval(0.0, 1.0, 0.9))));
			}
			return m-s;
		}
		else return -1;
	}
