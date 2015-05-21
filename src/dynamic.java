numRequests++;
		if (numRequests == numRequestsThreshold) { // Edited by Hamed Khiabani for dynamic nodes
			numRequests = 0;
			if (dynamic && runningSimulation) {
				activeState = false;
				numRequestsTimer = new Timer();
				numRequestsTimer.schedule(new TimerTask(){
					@Override
					public void run() {
						activeState = true;
						numRequestsTimer.cancel();
					}
				},sleepingTimeoutMilis);
			}
		}
