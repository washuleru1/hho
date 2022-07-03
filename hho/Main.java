public class Main {

	public static void main(String[] args) {
		try {
			for (int i = 0; i < 10; i++) {
				StdRandom.newSeed();
				new Swarm().execute();
			}
		} catch (Exception e) {
			StdOut.printf("%s\n%s", e.getMessage(), e.getCause());
		}
	}
}