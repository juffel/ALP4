package uebung05;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.PriorityQueue;

/* 
	This file is just a proposal, you may change it at your convenience.
	Handed in "Framework.java" files will not be considered and you also
	don't need to hand it in again. What this means is simply:
	"Don't change the given framework!"
*/



public class YourCode implements ILabeler {
	
	public static final int MAX_THREADS = 4;
	
    @Override
    public void process(final int[][] image, int[][] label) {
    	
        PriorityQueue<Tuple> argList = new PriorityQueue<Tuple>(10, new StripeComparator());
        
        // Queue initialisieren, mit 1px hohen Streifen füllen; das Tupel (x, y) gibt die
        // Grenzen des Streifens jeweils einschließlich x und y an
        for(int i=0; i<image.length; i++) {
        	argList.add(new Tuple(i, i));
        }

    	LinkedList<Thread> threads = new LinkedList<Thread>();
    	
    	for(int i=0; i < MAX_THREADS; i++) {
    		threads.add(new Thread(new labelRegion(argList, image, label)));
    	}
        
    	// dafür sorgen, dass alle Threads immer aktiv sind (Behandlung von InterruptedExceptions)
   		for(int i=0; i<MAX_THREADS; i++) {
       		threads.get(i).start();
      	}
   		
   		while(true) {
   			try {
   				
   				for(int i=0; i<MAX_THREADS; i++) {
   					threads.get(i).join();
   				}
   				break;
   				
   			} catch(InterruptedException e) {
   				e.printStackTrace();
   			}
   		}
    }
    
    
    // In dieser Klasse wird beschrieben, was die einzelnen Threads ausführen
    class labelRegion implements Runnable{

        private PriorityQueue<Tuple> args;
        volatile private int[][] image;
        volatile private int[][] label;
    	
    	public labelRegion(PriorityQueue<Tuple> args, int[][] image, int[][] label) {
    		this.args = args;
    		this.image = image;
    		this.label = label;
    	}
    	
		@Override
		public void run() {
			
			outerLoop:
			while(args.size() > 1) {
				
				Tuple stripe1, stripe2;
				
				// Die Streifen sind aufsteigend geordnet, es befinden sich jedoch nicht alle im ArgumentenPool, da
				// jeder Thread zwei Streifen rausgenommen hat, daher muss sich ein Thread zwei nebeneinanderliegende fischen
				synchronized(labelRegion.class) {
					
					if(!(args.size() > 1))
						break;
					
					// nebeneinanderliegende Streifen finden
					Iterator<Tuple>	it = args.iterator();
					
					stripe1 = it.next();
					stripe2 = it.next();
					
					while(stripe1.y != stripe2.x - 1) {
						if(!it.hasNext())
							break outerLoop;
						
						stripe1 = stripe2;
						stripe2 = it.next();
					}
					args.remove(stripe1);
					args.remove(stripe2);
					
				}
				
				// eventuell sind die zwei gezogenen Streifen nur einen Pixel hoch, dann wird
				// auf horizontale Nachbarn ueberprueft
				if(stripe1.x == stripe1.y) {
					for(int i=1; i<image[0].length; i++) {
						if(image[stripe1.x][i-1] == image[stripe1.x][i])
							label[stripe1.x][i] = label[stripe1.x][i-1];
					}
				}
				if(stripe2.x == stripe2.y) {
					for(int i=1; i<image[0].length; i++) {
						if(image[stripe2.x][i-1] == image[stripe2.x][i])
							label[stripe2.x][i] = label[stripe2.x][i-1];
					}
				}
				// jetzt sind die beiden gezogenen Streifen auf jeden Fall vorverarbeitet und koennen zusammengefuehrt werden
				// dafuer muss lediglich der Grenzbereich der beiden Streifen betrachtet werden und dort wiederum nur die
				// vertikalen Nachbarn (durch die Vorverarbeitung haben wir bereits alle horizontalen Nachbarn ueberprueft)
				for(int i = 0; i < image[0].length; i++) {
					if(image[stripe1.y][i] == image[stripe2.x][i])
						label[stripe2.x][i]	= label[stripe1.y][i];
				}

				synchronized(labelRegion.class) {
					args.add(new Tuple(stripe1.x, stripe2.y));
				}
				
			}
		}
    }
    
    
    // Warum gibts in Java kein Tupel Objekt ._.
    public class Tuple {
        public final int x;
        public final int y;
        public Tuple(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public Tuple(Tuple t) {
        	this.x = t.x;
        	this.y = t.y;
        }
    }
    
    
    public static class StripeComparator implements Comparator<Tuple> {
		@Override
		public int compare(Tuple o1, Tuple o2) {
			if(o1.x < o2.x)
				return -1;
			else if(o1.x > o2.x)
				return 1;
			else
				return 0;

		}
    	
    }

}
