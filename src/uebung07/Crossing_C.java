package uebung07;

public class Crossing_C extends Crossing{

	private static final int[][] crossabilities = {{1,0},{0,1},{1,1}};
	// soll heissen: es gibt fuer Fussgaenger drei und fuer Autos zwei Wege/Richtungen die Kreuzung zu ueberqueren
	// z.B. Weg 2 fuer Fussgaenger schneidet mit beiden Wegen 0,1 fuer Autos (crossabilities[2][0]==1),(crossabilities[2][1]==1)
	
	public Crossing_C() {
		
		super(crossabilities);
	}	
}
