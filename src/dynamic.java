if (dynamic && runningSimulation && ( Math.random() < 0.05)) {
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
