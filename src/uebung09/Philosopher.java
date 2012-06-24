package uebung09;

import java.util.Random;

import uebung09.Philomonitor;

/**
 * Philosophen essen und pausieren(philosophieren) in unregelmäßigen
 * Abständen
 * @author julian
 */
public class Philosopher implements Runnable {
	
	private final int ID;
	Philomonitor phimo;
	Random rnd = new Random();
	
	public Philosopher(int ID, Philomonitor phimo) {
		this.ID = ID;
		this.phimo = phimo;
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
				int tmp = rnd.nextInt(10000);
				does("ergründet die Geheimnisse des Lebens ("+tmp+")");
				Thread.sleep(tmp);
				return;
			}
			catch(InterruptedException e) {e.printStackTrace();}
		}
		
		says("So kann ich mich nicht konzentrieren!");
	}

	private void eat() {
		does("wartet auf Gabeln");
		phimo.getForks(ID);
		int tmp = rnd.nextInt(10000);
		says("Guten Appetit! ("+tmp+")");
		
		while(true) {
			
			try {
				Thread.sleep(tmp);
				break;
			}
			catch(InterruptedException e) {e.printStackTrace();}
		
		}
		phimo.releaseForks(ID);
		says("Yummy! Das war lecker :D");
		
	}
	
	private void says(String str) {
		System.out.println(ID+": \""+str+"\"");
	}
	private void does(String str) {
		System.out.println(ID+" "+str);
	}
	
}