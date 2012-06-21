package uebung07;

import java.util.Random;

public abstract class MovingObject implements Runnable {
	
	private Crossing crossing;
	
	public MovingObject(Crossing crossing) {
		
		this.crossing = crossing;
	}
	
	/**
	 * Der ausfuehrende Thread versucht solange in einer zufaellig bestimmten Richtung 
	 * die Kreuzung zu ueberqueren, bis er abgeschossen wird
	 */
	@Override
	public void run() {
		
		int[][] crossabilities = crossing.crossabilities();
		
		while(true){
			
			int randir; // zufaellige Richtung auslosen; moegliche Richtungen stehen im crossing.crossabilities[][]

			randir = new Random().nextInt(crossabilities[0].length);

			crossing.cross(this, randir);
			
		}
	}
	
}