	/**
	 * This method is called by VE want it reports its experience
	 * 
	 * @param reportedService
	 *            the application struct of the VE containing its experience for the service
	 * @param value
	 *            the value of the service it provides it can be changed to boolean malicous on another version
	 *            
	 *   @param collusion
	 *   		if there is collusion on the network         
	 */

	public synchronized void report(Application_struct reportedService, Double value , Boolean collusion) {
		Application_struct myStruct = Service_VEs.get(reportedService.service.id); //retrieve the platforms struct for the service
		ArrayList<String> keysAsArray = new ArrayList<String>(
				reportedService.followees.keySet());//get the friends of the VE reporting
		for (String id : keysAsArray) { //for each friend add a share containing satisfaction as the trust index
			Followee_struct global = myStruct.followees.get(id);
			Followee_struct local = reportedService.followees.get(id);

			if (collusion && (value >=0)){ //Value>=0 ===> I am also a server for the service so i check if I colude
				if ( value <=0.5){//I am malicous so I colude and return reverse values
					//System.out.println("I am bad = "+value);
					//System.out.println("he is good = "+(1-local.calculate_Trust()));
					myStruct.addNewShare(id, new MyOutcome(new SatisfactionInterval(0.0, 0.9,1-local.calculate_Trust())));	
				}else 	myStruct.addNewShare(id, new MyOutcome(new SatisfactionInterval(0.0, 0.9,local.calculate_Trust())));
			}else if (value>=0) 	myStruct.addNewShare(id, new MyOutcome(new SatisfactionInterval(0.0, 0.9,local.calculate_Trust())));
			global.fade(0.005);//fading effect for the struct of platform
		}

	}
	/**
	 * This method is called by VE that wants a new friend
	 * 
	 * @param s
	 *            service that the friend should have       
	 */
	public synchronized TemplateTRM_Sensor get_recommendation(String s) {
		Application_struct myStruct = Service_VEs.get(s);
		PriorityQueue<ComparableFriend>  MyQueue = new PriorityQueue<ComparableFriend>();
		MyQueue = myStruct.rank_friends("trust");
		double propability = Math.random();//0.4 o prwtos 0.4 o deyteros 0.2 random me miden trust
		if (propability<0.4 && !MyQueue.isEmpty())
		{
			String f = MyQueue.poll().id;
			if (myStruct.followees.get(f)!=null)
				return myStruct.followees.get(f).Sensor;
		}
		MyQueue.poll();
		if (propability<0.8 && !MyQueue.isEmpty())
		{
			String f = MyQueue.poll().id;
			if (myStruct.followees.get(f)!=null)
				return myStruct.followees.get(f).Sensor;
		}
		if(!MyQueue.isEmpty()){
			ComparableFriend f1 = MyQueue.poll();
			while (!MyQueue.isEmpty() & f1.value>0.0)
				MyQueue.poll();
			if(!MyQueue.isEmpty()){
				String f = MyQueue.poll().id;
				if (myStruct.followees.get(f)!=null)
					return myStruct.followees.get(f).Sensor;
			}
		}return null;
	}


}
