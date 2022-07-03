public class Bat extends Problem {

	private int[] x = new int[nVars];
	private double[] v = new double[nVars];
	private double[] f = new double[nVars];
	private double[] r = new double[nVars];
	private double[] r0 = new double[nVars];
	private double[] A = new double[nVars];

	public Bat() {
		for (int j = 0; j < nVars; j++) {
			x[j] = StdRandom.uniform(2);
			f[j] = StdRandom.uniform();
			r0[j] = StdRandom.uniform();
			A[j] = StdRandom.uniform();
			v[j] = StdRandom.uniform();
		}
	}
	
	protected boolean isFeasible() {
		return checkConstraint(x);
	}

	protected boolean isBetterThan(Bat g) {
		return computeFitness(x) < computeFitness(g.x);
	}

	private int toBinary(double x) {
		return StdRandom.uniform() <= (1 / (1 + Math.pow(Math.E, -x))) ? 1 : 0;
	}
	
	protected void randomWalk(final double epsilon, final double avgA) {
        for (int j = 0; j < nVars; j++) {
            x[j] = toBinary(x[j] + epsilon * avgA);
        }
    }
	
	protected void decreasesLoudness(final double alpha) {
		for (int j = 0; j < nVars; j++) {
			A[j] = alpha * A[j];
        }
    }

    protected void increasesPulseEmission(final double gamma, final  int t) {
    	for (int j = 0; j < nVars; j++) {
    		r[j] = r0[j] * (1 - (Math.pow(Math.E, (-gamma * t))));
        }
    }
    
    protected boolean hasExploration() {
        return avg(r) < StdRandom.uniform();
    }

    protected boolean hasExploitation() {
        return avg(A) > StdRandom.uniform();
        
    }

    protected double avgA() {
    	return avg(A);
    }
    
    private double avg(double[] a) {
    	 return java.util.Arrays.stream(a).average().getAsDouble();
    }
    
    protected void move(final Bat g, final double fmin, final double fmax) {
        double beta;
        for (int j = 0; j < nVars; j++) {
        	beta = StdRandom.uniform();
        	f[j] = fmin + (fmax - fmin) * beta;
            v[j] = v[j] + (g.x[j] - x[j]) * f[j];
            x[j] = toBinary(x[j] + v[j]);
        }
    }
    private float diff() {
		return computeFitness(x) - optimum();
	}

	private float rpd() {
		return diff() / optimum() * 100f;
	}

	private String showSolution() {
		return java.util.Arrays.toString(x);
	}

	protected void copy(Object object) {
		if (object instanceof Bat) {
			System.arraycopy(((Bat) object).x, 0, this.x, 0, nVars);
			System.arraycopy(((Bat) object).f, 0, this.f, 0, nVars);
			System.arraycopy(((Bat) object).v, 0, this.v, 0, nVars);
		}
	}
	
	@Override
	public String toString() {
		return String.format("optimal value: %d, fitness: %d, diff: %.1f, rpd: %.2f%%, p: %s", optimum(),
				computeFitness(x), diff(), rpd(), showSolution());
	}
}
