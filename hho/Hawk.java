import java.util.Random;

public class Hawk extends Problem {
    //posición del halcon

    private int[] x = new int[nVars];
	private int[] p = new int[nVars];
	private double[] v = new double[nVars];

	public Hawk() {
		for (int j = 0; j < nVars; j++) {
			x[j] = StdRandom.uniform(2);
			v[j] = 0;
		}
	}

    public int[] getPositionVector(){
        return this.x;
    }


    public double randomPosition() {
        return 2.3;
    }

    protected int computeFitness() {
		return computeFitness(x);
	}

	protected int computeFitnessPBest() {
		return computeFitness(p);
	}

	protected boolean isBetterThanPBest() {
		return computeFitness() < computeFitnessPBest();
	}

	protected boolean isBetterThan(Hawk g) {
		return computeFitnessPBest() < g.computeFitnessPBest();
	}

	protected void updatePBest() {
		System.arraycopy(x, 0, p, 0, x.length);
	}

	protected boolean isFeasible() {
		return checkConstraint(x);
	}

	protected void move(Hawk g, float theta, float alpha, float beta, double escapeEnergy) {
		for (int j = 0; j < nVars; j++) {
			/* Actualizar velocidad */
			v[j] = v[j] * theta + alpha * StdRandom.uniform() * (g.p[j] - x[j]) + beta * StdRandom.uniform() * (p[j] - x[j]);
			/* Actualizar posicion */
			x[j] = toBinary(x[j] + v[j]);

			//ver si se deja lo de arriba, x[j] es la posición del hawk, pero no utilizamos las velocidades
			if(Math.abs(escapeEnergy) >= 1 ) {
				//ecuacion 1 
				if(q >= 0.5) {
					actualHawkPosition = swarm.get(i_hawk).getPositionVector();
					x[j+1]  =  g.x[j] - r1 * Math.abs(randomHawk.getPositionVector() - (2 * r2* actualHawkPosition));
				} else {    
					averageHawksPosition = averagePositionOfSwarm(swarm);
					x[j+1] =randomHawk. - averageHawksPosition - r3 * (LB + r4 * (UB - LB));
				}
				swarm.get(i_hawk).copy(randomHawk);
 
			} else {
				r = StdRandom.uniform();
				u = StdRandom.uniform();
				v = StdRandom.uniform();
				sigma = Math.pow((r * (1 + beta) * Math.sin((Math.PI*beta)/2))/(r * ((1 + beta)/2) * beta * (2*(beta-1)/2)), (1/beta));
				//comparar r y e
				rabbitJump = 2 * (1 - StdRandom.uniform());
				if(r >= 0.5 && Math.abs(escapeEnergy) >= 0.5) { //soft beseige
					x[j+1] = (deltaXt) - escapeEnergy * Math.abs((rabbitJump * xrabbit) - xt);
					// update the location vector (updatePBest?) usando la ecuación (4) del paper
					// X(t+1)
				} else if (r >= 0.5 && Math.abs(escapeEnergy) < 0.5) { // hard beseige
					// // update the location vector (updatePBest?) usando la ecuación (6) del paper
					x[j+1] = xrabbit - escapeEnergy * Math.abs(deltaXt);
				} else if (r < 0.5 && Math.abs(escapeEnergy) >= 0.5) { // soft beseige with progressive rapid dives
					// // update the location vector (updatePBest?) usando la ecuación (10) del paper
					//primero calcular (7)
					Y = xrabbit - escapeEnergy * Math.abs((rabbitJump * xrabbit) - xt);
					//Calcular (9)
					levyFlight = 0.01 * (u*sigma/Math.pow(Math.abs(v), (1/beta)));
					//calcular (10)
				} else if (r < 0.5 && Math.abs(escapeEnergy) < 0.5) { // hard beseige with progressive rapid dives
					// // update the location vector (updatePBest?) usando la ecuación (11) del paper
				} 
			}


		}
	}

	private float diff() {
		return computeFitness(p) - optimum();
	}

	private float rpd() {
		return diff() / optimum() * 100f;
	}

	private String showSolution() {
		return java.util.Arrays.toString(p);
	}

	private int toBinary(double x) {
		return StdRandom.uniform() <= (1 / (1 + Math.pow(Math.E, -x))) ? 1 : 0;
	}

	protected void copy(Object object) {
		if (object instanceof Hawk) {
			System.arraycopy(((Hawk) object).x, 0, this.x, 0, nVars);
			System.arraycopy(((Hawk) object).p, 0, this.p, 0, nVars);
			System.arraycopy(((Hawk) object).v, 0, this.v, 0, nVars);
		}
	}

	@Override
	public String toString() {
		return String.format("optimal value: %d, fitness: %d, diff: %.1f, rpd: %.2f%%, p: %s", optimum(),
				computeFitnessPBest(), diff(), rpd(), showSolution());
	}



}
