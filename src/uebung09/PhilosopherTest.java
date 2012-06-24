package uebung09;

public class PhilosopherTest {
	public static void main(String[] args) {
		Thread tmp = new Thread(new Philosopher(0));
		tmp.run();
	}
}
