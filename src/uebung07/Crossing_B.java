package uebung07;

public class Crossing_B extends Crossing {
	
	private static final int[][] crossabilities = {{1,0},{0,1}};
	// soll heissen: es gibt jew. fuer Fussgaenger und Autos zwei Wege/Richtungen die Kreuzung zu ueberqueren
	// von denen sich zwei ueberschneiden (1) und zwei nicht (0)
	// z.B. Weg 0 fuer Fussgaenger schneidet mit Weg 0 fuer Autos (crossabilities[0][0]==1)
	// Weg 0 fuer Fussgaenger schneidet sich nicht mit Weg 1 fuer Autos (crossabilities[0][1]==0)
	
	public Crossing_B() {
		
		super(crossabilities);
	}

}
