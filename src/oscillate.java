  /**
     * This method turns every benevolent server in the network into malicious and
     * counts the number of swapped servers. Then (when every server is malicious)
     * it randomly selects malicious servers and converts them into benevolent until
     * the number of benevolent servers is equal to the one before calling this method
     * @param service Service over what the oscillation is to be carried out
     */
    public void oscillate(Service service) {
 
            int numBenevolentServers = 0;
            for (Sensor server : servers)
                if ((server.offersService(service)) && (server.get_goodness(service) >= 0.5)) {
                    numBenevolentServers++;
                    server.set_goodness(service,0.0);
                }  
            double prob = ((double)numBenevolentServers/servers.size());
            while (numBenevolentServers > 0)
                for (Sensor server : servers)
                    if ((Math.random() < prob) && (server.offersService(service)) && (server.get_goodness(service)< 0.5)) {
                        server.set_goodness(service,1.0);
                        numBenevolentServers--;
                        if (numBenevolentServers == 0)
                            break;
                    }
    }
