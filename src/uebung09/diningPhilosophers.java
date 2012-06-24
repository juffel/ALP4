package uebung09;

public class diningPhilosophers {
	
	public static class Philomonitor {
		
		public static Thread[] philoThreads = new Thread[5];
		
		// true == fork on table | false == fork taken
		// private static boolean[] forks = {true, true, true, true, true};
		/** in forks steht, welche Gabel wer hat; ohne Besitzer hat eine Gabel den Wert -1 */
		private static int[] forks = {-1, -1, -1, -1, -1};
		
		public Philomonitor() {
			
			for(int i=0; i<5; i++) {
				philoThreads[i] = new Thread(new Philosopher(i));
			}
			
			for(int i=0; i<5; i++) {
				philoThreads[i].run();
			}
		}
		
		/** 
		 * zwei Gabeln nehmen
		 * @param id
		 */
		synchronized public static void getForks(int ID) {
			
			// um Deadlocks zu vermeiden wird einer der Philosophen zuerst nach der rechten Gabel greifen,
			// die restlichen Philosopen zuerst nach der rechten
			if(ID==0) {
				getRightFork(ID);
				getLeftFork(ID);
			} else {
				getLeftFork(ID);
				getRightFork(ID);
			}
		
		}
		
		synchronized private static void getLeftFork(int ID) {
			
			while(true) {
				
				try {
				
					if(forks[ID] < 0) {
						forks[ID] = ID;
					} else {
						philoThreads[ID].wait();
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		synchronized private static void getRightFork(int ID) {
			
			getLeftFork((ID-1)%5);
			
		}
		
		/** 
		 * zwei Gabeln wieder freigeben
		 * @param id
		 */
		synchronized public static void releaseForks(int ID) {
			
			// Prüfen, ob der Thread mit ID überhaupt die Gabeln besitzt
			if(forks[ID] != ID || forks[(ID+1)%5] != ID) {
				System.out.println("Illegal forkRelease!");
				return;
			}
			
			forks[ID] = -1;
			forks[(ID+1)%5] = -1;
		}
	}
}
