package uebung07;

import java.util.concurrent.Semaphore;

public abstract class Crossing{

	/** In dieser Tabelle steht, wie viele Ueberquerungsmoeglichkeiten jew. vorhanden sind und welche mit welchen in Konflikt stehen */
	private int[][] crossabilities;
	/** In dieser Tabelle sind die notwendigen Semaphore enthalten in der Reihenfolge:
	 * entry, delay_Fussgaenger, delay_Auto, block_Fussgaenger, block_Auto*/
	private Semaphores[][] semaphores;

	public Crossing(int[][] crossabilities) {
		
		this.crossabilities = crossabilities;
		
		// fuer alle Wegquerungen entsprechende Semaphore erstellen, ein entry,
		// jew. einen delaycounter fuer Autos und Fussgaenger und jeweils einen Blockadesemaphor fuer Autos und Fussgaenger
		for(int i=0; i<crossabilities.length; i++) {
			for(int j=0; j<crossabilities[i].length; j++) {
				if(crossabilities[i][j] == 1) {
					semaphores[i][j] = new Semaphores();
				}
			}
		}
	}
	
	/**
	 * Diese Methode wird aufgerufen, wenn ein Objekt an der Kreuzung ankommt
	 * und diese in einer bestimmten Richtung ueberqueren moechte
	 * @param mobj			das Objekt, welches die Kreuzung ueberqueren moechte
	 * @param direction		je nach Art der Kreuzung/des ueberquerenden Objekts verschiedene Anzahl von Ueberquerungsmoeglichkeiten
	 */
	public void cross(MovingObject mobj, int direction) {
		
		/* TODO Ein Objekt moechte die Kreuzung ueberqueren ->
		 * 		1. im crossability-Table nachschauen, welche Fahrbahnen kreuzen
		 * 		2. dementsprechende Semaphoren beanspruchen
		 * 		3. 'die Kreuzung ueberqueren'
		 * 		4. Semaphoren wieder freigeben
		 */
	}
	
	
	public void signal(int i, int j) {
		
		if((semaphores[i][j].nr_Auto == 0) && (semaphores[i][j].delay_Fussgaenger > 0)) {
			
			semaphores[i][j].delay_Fussgaenger--;
			semaphores[i][j].sem_Fussgaenger.release();
		}
		else if ((semaphores[i][j].nr_Fussgaenger == 0) && (semaphores[i][j].delay_Auto > 0)) {
			
			semaphores[i][j].delay_Auto--;
			semaphores[i][j].sem_Auto.release();
			
		} else {
			
			semaphores[i][j].entry.release();
		}
	}
	
	/**
	 * @return alle Ueberquerungsmoeglichkeiten
	 */
	public int[][] crossabilities() {
		
		return crossabilities;
	}
	
	/**
	 * Diese Klasse bietet einen ganzen Packen Semaphore inkl. countern fuer die Ueberwachung einer kritischen Sektion
	 */
	class Semaphores {
		
		Semaphore entry;
		int delay_Fussgaenger, delay_Auto;
		Semaphore sem_Fussgaenger;
		Semaphore sem_Auto;
		int nr_Fussgaenger, nr_Auto;
		
		Semaphores() {
			entry = new Semaphore(1);
			delay_Fussgaenger = delay_Auto = 0;
			sem_Fussgaenger = new Semaphore(1);
			sem_Auto = new Semaphore(1);
			nr_Auto = nr_Fussgaenger = 0;			
		}
	}	
}
