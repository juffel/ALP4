package uebung09;

import java.util.Random;

import uebung09.diningPhilosophers.Philomonitor;

/**
 * Philosophen essen und pausieren(philosophieren) in unregelmäßigen
 * Abständen
 * @author julian
 *
 */
public class Philosopher implements Runnable {
	
	private final int ID;
	Random rnd = new Random();
	
	public Philosopher(int ID) {
		this.ID = ID;
	}
	
	public void run() {
		
		while(true) {
			think();
			eat();
		}
	}
	
	private void think() {
		
		// wenn ein Philosoph zuoft unterbrochen wird, kann er nicht mehr denken
		for(int i=0; i<10; i++) {
			
			try {
				does("ergründet die Geheimnisse des Lebens");
				wait(rnd.nextLong());
			}
			catch(InterruptedException e) {e.printStackTrace();}
		}
		
		says("So kann ich mich nicht konzentrieren!");
	}

	private void eat() {
		does("holt sich Gabeln");
		Philomonitor.getForks(ID);
		says("Guten Appetit!");
		
		while(true) {
			
			try {
				wait(rnd.nextLong());
				break;
			}
			catch(InterruptedException e) {e.printStackTrace();}
		
		}
		Philomonitor.releaseForks(ID);
		says("Yummy! Das war lecker :D");
		
	}
	
	private void says(String str) {
		System.out.println(ID+": "+str);
	}
	private void does(String str) {
		System.out.println(ID+" "+str);
	}
	
}