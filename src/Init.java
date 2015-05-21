
public class TemplateTRM_Network extends Network {

	protected HashMap<String, Application_struct> Service_VEs; //

	/**
	 * This constructor creates a new random TemplateTRM Network using the given
	 * parameters
	 * 
	 * @param numSensors
	 *            Network number sensors
	 * @param probClients
	 *            The network will have a number of clients depending of this
	 *            parameter.
	 * @param rangeFactor
	 *            Range Factor.
	 * @param probServices
	 *            Probability of servers offers services and clients requesting
	 *            services.
	 * @param probGoodness
	 *            Probability of servers being good.
	 * @param services
	 *            Services that servers offers to clients.
	 */
	public TemplateTRM_Network(int numSensors, double probClients,
			double rangeFactor, Collection<Double> probServices,
			Collection<Double> probGoodness, Collection<Service> services) {
		super(null, null, null);
		Service_VEs = new HashMap<String, Application_struct>();//struct me tis listes ton VE ana service gia na ginoun recommendations
		clients = new ArrayList<Sensor>();
		servers = new ArrayList<Sensor>();
		sensors = new ArrayList<Sensor>();
		this.services = new ArrayList<Service>();// ola ta services tou
	
		for (Service service : services)
			Service_VEs.put(service.id,new Application_struct(service.id,
							Math.random() * 0.5 + 0.5)); // create log+files
		Sensor.resetId();

		for (int i = 0; i < numSensors; i++) {
			if (Math.random() <= probClients) { // client intialize
				TemplateTRM_Sensor client = newSensor();
				clients.add(client);
				sensors.add(client);
				Iterator<Double> itProbServices = probServices.iterator();
				for (Service service : services)
					// add requested services
					if (Math.random() <= itProbServices.next().doubleValue()) {
						client.addNewService(service.id);
					}

			} else { // server intialize
				TemplateTRM_Sensor server = newSensor();
				clients.add(server);
				servers.add(server);
				sensors.add(server);

				Iterator<Double> itProbServices1 = probServices.iterator();
				Iterator<Double> itProbServices2 = probServices.iterator();
				Iterator<Double> itProbGoodness = probGoodness.iterator();

				for (Service service : services)
					// add requested services
					if (Math.random() <= itProbServices1.next().doubleValue()) {
						server.addNewService(service.id);
					}

				for (Service service : services){
					// add provided services
					if (Math.random() <= itProbServices2.next().doubleValue()) {
						Service_VEs.get(service.id).followee_put(server,0.0);
						if (Math.random() <= itProbGoodness.next()
								.doubleValue()){// check quality of service
						 server.addService(service, 1.0);
						}else{
							server.addService(service, 0.0); 
							server.malicious=true;
						}
					
				}}}}

		// We are going to add some friends to the sensors two for each service
		// it requests
		for (Sensor client : clients) {
			TemplateTRM_Sensor myclient = (TemplateTRM_Sensor) client;
			Set<String> ServiceSet = myclient.getRequestedServices();
			for (String service : ServiceSet) {
				if (service.equals("My service")){
				myclient.addfollowee(Service_VEs.get(service).get_random_entry(), service,0.9);
			       myclient.addfollowee(Service_VEs.get(service).get_random_entry(), service,0.9);}
		}}
