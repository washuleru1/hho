
public class Swarm {
	
	private final int ps = 25;
	private final int T = 1000;
	private final double fmin = 0;
	private final double fmax = 1;
	private final double alpha = 0.9;
	private final double gamma = 0.9;
	private final double epsilon = StdRandom.uniform(-1.0,1.0);
	
	private java.util.List<Bat> swarm = null;
	private Bat g = null;
	
	public void execute() {
		init();
		evolve();
	}
	
	private void init() {
		swarm = new java.util.ArrayList<Bat>();
		g = new Bat();
		Bat b;
		for(int i = 1; i <= ps; i++) {
			do {
				b = new Bat();	
			} while(!b.isFeasible());
			swarm.add(b);
		}
		g.copy(swarm.get(0));
		for (int i = 1; i < ps; i++) {
			if (swarm.get(i).isBetterThan(g)) {
				g.copy(swarm.get(i));
			}
		}
		log(0);
	}
	
	private void evolve() {
		int t = 1;
		Bat b = new Bat();
		while(t <= T) {
			for (int i = 0; i < ps; i++) {
				if (swarm.get(i).hasExploration()) {
					do {
						b.copy(swarm.get(StdRandom.uniform(ps)));
						b.randomWalk(epsilon, average());	
					} while (!b.isFeasible());
					swarm.get(i).copy(b);
				}
				do {
					b.copy(swarm.get(i));
					b.move(g, fmin, fmax);
				} while (!b.isFeasible());
				if (b.hasExploitation() && !b.isBetterThan(g)) {
					b.decreasesLoudness(alpha);
					b.increasesPulseEmission(gamma, t);
				}
				if (b.isBetterThan(g)) {
					g.copy(b);
				}
				swarm.get(i).copy(b);
			}
			log(t);
			t++;
		}
	}
	
	private void log(int t) {
		StdOut.printf("t=%d,\t%s\n", t, g);
	}
	
	private double average() {
		double[] data = new double[ps];
		for (int i = 0; i < ps; i++) {
			data[i] = swarm.get(i).avgA();
		} 
		return java.util.Arrays.stream(data).average().getAsDouble();
	}
}
