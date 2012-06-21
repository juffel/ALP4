package uebung07;

public class Crossing_A extends Crossing {
	
	private static final int[][] crossabilities = {{1}};
	// soll heissen: es gibt sowohl fuer Autos als auch fuer Fussgaenger genau eine Moeglichkeit
	// die Kreuzung zu ueberqueren und diese zwei Wege ueberschneiden sich (1)
	
	public Crossing_A() {
		
		super(crossabilities);
	}
	
}
