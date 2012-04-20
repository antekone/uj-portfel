package pl.edu.uj.portfel.test;

public abstract class TestNode {
	public abstract boolean run();
	
	public void assertEquals(String s1, String s2) {
		if(s1.equals(s2))
			return;
		else {
			System.out.println("Assertion failed, s1=" + s1 + ", s2=" + s2);
			System.exit(1);
		}
	}
}
