public class Swarm {

	private final int ps = 25;
	private final float theta = 0.9f, alpha = 2f, beta = 2f;
	private final int MaxIter = 1000;
	private java.util.List<Particle> swarm = null;
	private Particle g = null;

	public void execute() {
		initRand();
		evolve();
	}

	private void initRand() {
		swarm = new java.util.ArrayList<>();
		g = new Particle();
		Particle p = null;
		for (int i = 1; i <= ps; i++) {
			do {
				p = new Particle();
			} while (!p.isFeasible());
			p.updatePBest();
			swarm.add(p);
		}
		g.copy(swarm.get(0));
		for (int i = 1; i < ps; i++)
			if (swarm.get(i).isBetterThan(g))
				g.copy(swarm.get(i));
		log(0);
	}

	private void evolve() {
		int t = 1;
		while (t <= MaxIter) {
			Particle p = new Particle();
			for (int i = 0; i < ps; i++) {
				do {
					p.copy(swarm.get(i));
					p.move(g, theta, alpha, beta);
				} while (!p.isFeasible());
				if (p.isBetterThanPBest())
					p.updatePBest();
				swarm.get(i).copy(p);
			}
			for (int i = 0; i < ps; i++)
				if (swarm.get(i).isBetterThan(g))
					g.copy(swarm.get(i));

			log(t);
			t++;
		}
	}

	private void log(int t) {
		StdOut.printf("t=%d,\t%s\n", t, g);
	}
}