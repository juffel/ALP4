package uebung09;

import java.util.Random;

public class diningPhilosophers {
	
	public static class Philomonitor {

		/** 
		 * zwei Gabeln nehmen
		 * @param id
		 */
		public void getForks(int id) {
			
		}
		
		/** 
		 * zwei Gabeln wieder freigeben
		 * @param id
		 */
		public void releaseForks(int id) {
			
		}

	}
	
	/**
	 * Philosophen essen und pausieren(philosophieren) in unregelmäßigen
	 * Abständen
	 * @author julian
	 *
	 */
	public class Philosopher implements Runnable {
		
		Random rnd = new Random();
		
		public void run() {
			
			while(true) {
				think();
				eat();
			}
		}
		
		public void think() {
			
		}
		
		public void eat() {
			
		}
		
	}

}
