package uebung09;

import uebung09.Philosopher;

public class Philomonitor {
	
	public Thread[] philoThreads = new Thread[5];
	
	/** in forks steht, welche Gabel wer hat; ohne Besitzer hat eine Gabel den Wert -1 */
	private int[] forks = {-1, -1, -1, -1, -1};
	
	public Philomonitor() {
		
		for(int i=0; i<5; i++) {
			philoThreads[i] = new Thread(new Philosopher(i, this));
		}
	}
	
	public void runThis() {
		for(int i=0; i<5; i++) {
			philoThreads[i].start();
		}
	}
	
	/** 
	 * zwei Gabeln nehmen
	 * @param id
	 */
	synchronized public void getForks(int ID) {
		
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
	
	synchronized private void getLeftFork(int ID) {
		
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
	
	synchronized private void getRightFork(int ID) {
		
		getLeftFork((ID+4)%5);
		
	}
	
	/** 
	 * zwei Gabeln wieder freigeben
	 * @param id
	 */
	synchronized public void releaseForks(int ID) {
		
		// Prüfen, ob der Thread mit ID überhaupt die Gabeln besitzt
		if(forks[ID] != ID || forks[(ID+1)%5] != ID) {
			System.out.println("Illegal forkRelease!");
			return;
		}
		
		forks[ID] = -1;
		forks[(ID+1)%5] = -1;
	}
}
