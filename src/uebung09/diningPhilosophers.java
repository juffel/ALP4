package uebung09;

import java.util.Random;

public class diningPhilosophers {
	
	public static class Philomonitor {
		
		public static Thread[] philoThreads = new Thread[5];
		
		// true == fork on table | false == fork taken
		private static boolean[] forks = {true, true, true, true, true};
		
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
		
		private static void getLeftFork(int ID) {
			
		}
		
		private static void getRightFork(int ID) {
			
		}
		
		/** 
		 * zwei Gabeln wieder freigeben
		 * @param id
		 */
		synchronized public static void releaseForks(int ID) {
			// TODO evtl. abfragen, ob man die Gabeln überhaupt hat
			forks[(ID-1)%5]=true;
			forks[ID%5]=true;
		}
	}
}
