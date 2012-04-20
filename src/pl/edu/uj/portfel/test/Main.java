package pl.edu.uj.portfel.test;

public class Main {
	public static void main(String[] args) {
		test(new StringUtilsTest());
	}
	
	public static boolean test(TestNode node) {
		if(! doTest(node)) {
			System.out.println("Test failed: " + node.toString());
			System.exit(1);
		}
		
		return true;
	}
	
	public static boolean doTest(TestNode node) {
		if(! node.run())
			return false;
		else
			return true;
	}
}
