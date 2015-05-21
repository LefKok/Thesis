
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
				if(malicious && collusion){ //if malicous and collusion return reverse experience
					return 1-f1.return_trust();
				}else return f1.return_trust();

			}
		}

		return -0.1; //negative means no experience at all
	}

	public synchronized Application_struct get_app(String S){
		return friends.get(S);


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
				if(collusion && (id!=this.id) && malicious){ //collude in order to return outcome of a malicous friend

					Application_struct bad_struct = get_app(s);
					if (bad_struct!=null){
						PriorityQueue<ComparableFriend> BadQueue= bad_struct.rank_friends("trust");
						while (!BadQueue.isEmpty()){
							ComparableFriend bad_temp = BadQueue.poll();
							if (bad_temp.value < 0.3){ //found bad friend
								Followee_struct ff = bad_struct.get_followee(temp.id);
								if (ff!=null){
									TemplateTRM_Sensor current = ff.Sensor;
									Application_struct new_requiredService = current.get_app(s);
									if (new_requiredService!=null){
										outcome = new_requiredService.request_Service();}
								}}
						}
					}


				}
				if (outcome!=null){
					recommenders.addNewAssist(temp.id,(MyOutcome)outcome);}		
			}else break;

		}
		return outcome;
	}
	
