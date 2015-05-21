	public void run() {
		outcome = null;
		Set<String> Services = getRequestedServices();
		for (String s : Services)// gia kathe service pou thelw
		{
			if (s.equals("My service")) {
				Application_struct new_requiredService = get_app(s);
				outcome = new_requiredService.request_Service(); // request service base on Trust on followees
				if (outcome == null)
				{ if (Math.random() < 0.3 ) outcome=get_assistance(s,5,this.id);
				else outcome=get_recommendation(s);	
				
				}
				if (outcome == null) 
				{	TemplateTRM_Sensor recommended=platform.get_recommendation(s);
				if (recommended!=null){
					addfollowee(recommended,s,0.9);
					outcome = new_requiredService.request_Service();
					
			if (Math.random() <0.1) { this.report(); 
			this.blacklist(); 
}}}

