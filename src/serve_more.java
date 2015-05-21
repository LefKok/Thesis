	/** 
	 * This Method returns the experience the VE had with another VE for a service
	 * @param id the sensor
	 * @param service the service
	 * @return the trust level
	 */
	public synchronized double ask_experience(String id, String service) {

		Application_struct f = friends.get(service);
		if (f!=null)	{	
			Followee_struct f1 = f.get_followee(id);
			if (f1!=null){
			 return f1.return_trust();

			}
		}

		return -0.1; //negative means no experience at all
	}

	/**
	 * 
	 * @param s the service
	 * @param i the TTL
	 * @param id id of the requester so that i do not lie to myself when colluding
	 * @return
	 */
	public synchronized Outcome get_assistance(String s, int i,int id) {
		Application_struct recommenders = friends.get("recommendation");
		PriorityQueue<ComparableFriend> MyQueue=recommenders.rank_friends("assists");
		while (!MyQueue.isEmpty() && outcome ==  null){
			ComparableFriend temp = MyQueue.poll();			
			if ( temp.value > 0.5)
			{ 
				
					TemplateTRM_Sensor current = recommenders.get_followee(temp.id).Sensor;
					Application_struct new_requiredService = current.get_app(s);
					if (new_requiredService!=null){

						outcome = new_requiredService.request_Service();
					}if (i>0 && outcome==null) outcome = current.get_assistance(s, i-1,this.id);
				}

				if (outcome!=null){
					recommenders.addNewAssist(temp.id,(MyOutcome)outcome);}		
			}else break;

		}
		return outcome;
	}
	
	/**
	 * This methods returns the actual service value
	 * @param service
	 * @return the satisfaction
	 */
	public synchronized double serve(String service) {
		if(servicesGoodness2.get(service)!=null && this.isActive()){
			double quality = servicesGoodness2.get(service);
			return Math.round(10 * (quality)) / 10.0;}
		else return -0.1;
	}

