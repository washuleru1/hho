public class Hawk extends Problem {
    //posición del halcon

    private int[] x = new int[nVars];
	private int[] p = new int[nVars];
	private int S = 0;
	private double v, r, u = 0;
	private final int LB = 0;
	private final int UB = 1;
	private double rabbitJump = 0;
	private double sigma = 0;
	private double deltaXt = 0;
	private double Y, levyFlight, Z = 0;

	public Hawk() {
		for (int j = 0; j < nVars; j++) {
			x[j] = StdRandom.uniform(2);
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

	protected void exploration(Hawk g, Randoms randomValues, double averageHawksPosition) {
		for (int j = 0; j < nVars; j++) {
			if(randomValues.getQ() >= 0.5) {
				x[j]  =  toBinary(x[j] - randomValues.getR1() * Math.abs(x[j] - (2 * randomValues.getR2() * g.x[j])));
			} else {
				x[j] = toBinary(p[j] - averageHawksPosition - randomValues.getR3() * (LB + randomValues.getR4() * (UB - LB)));
			}
		}
	}

	protected void softBeseige(Hawk g, double escapeEnergy, double rabbitJump) {
		for (int j = 0; j < nVars; j++) {
			deltaXt = g.x[j] - x[j];
			x[j] = toBinary((deltaXt) - escapeEnergy * Math.abs((rabbitJump * g.x[j]) - x[j]));
		}	
	}

	protected void hardBeseige(Hawk g, double escapeEnergy) {
		for (int j = 0; j < nVars; j++) {
			deltaXt = g.x[j] - x[j];
			x[j] = toBinary(g.x[j] - escapeEnergy * Math.abs(deltaXt));
		}
	}

	protected void softBeseigeProgresive(Hawk g, double escapeEnergy, double beta, double S, double u, double v, double rabbitJump) {
		// la duda es si rabbitJump es en cada nvars o por cada iteracion
		
		for (int j = 0; j < nVars; j++) {
			S = StdRandom.uniform(2);
			u = StdRandom.uniform();
			v = StdRandom.uniform();
			//primero calcular (7)
			Y = g.x[j] - escapeEnergy * Math.abs((rabbitJump * g.x[j]) - x[j]);
			//Calcular (9)
			double gamma1 = gammaFunction(1 + beta);
			double gamma2 = gammaFunction((1 + beta)/2);
			sigma = Math.pow((gamma1 * Math.sin((Math.PI*beta)/2))/( gamma2 * beta * (2*(beta-1)/2)), (1/beta));
			levyFlight = 0.01 * (u*sigma/Math.pow(Math.abs(v), (1/beta)));
			//calcular (10)
			//System.out.println("Y:"+Y);
			//System.out.println("P:"+g.p[j]);
			Z = Y + S * levyFlight;
			if (toBinary(Y) < x[j]) {
				x[j] = toBinary(Y);
			}
			if (toBinary(Z) < x[j]) {
				x[j] = toBinary(Z);
			}
		}			
	}

	protected void hardBeseigeProgresive(Hawk g, double escapeEnergy, double beta, double averageHawksPosition, double S, double u, double v, double rabbitJump) {
		
		for (int j = 0; j < nVars; j++) {
			S = StdRandom.uniform(2);
			//primero calcular (7)
			Y = g.x[j] - escapeEnergy * Math.abs((rabbitJump * g.x[j]) - averageHawksPosition);
			//System.out.println("Y:"+Y);
			//System.out.println("P:"+g.p[j]);
			//Calcular (9)
			double gamma1 = gammaFunction(1 + beta);
			double gamma2 = gammaFunction((1 + beta)/2);
			sigma = Math.pow((gamma1 * Math.sin((Math.PI*beta)/2))/( gamma2 * beta * (2*(beta-1)/2)), (1/beta));
			levyFlight = 0.01 * (u*sigma/Math.pow(Math.abs(v), (1/beta)));
			//calcular (10)
			Z = Y + S * levyFlight;
			if (toBinary(Y) < computeFitness(x)) {
				x[j] = toBinary(Y);
			}
			if (toBinary(Z) < computeFitness(x)) {
				x[j] = toBinary(Z);
			}
		}
					
	}

	protected void move(Hawk g, float theta, float alpha, double beta, double escapeEnergy, Randoms randomValues, double averageHawksPosition) {
		// el randomHawk es el this, es el halcon seleccionado de forma randomica
		for (int j = 0; j < nVars; j++) {
			/* Actualizar posicion */
			//ver si se deja lo de arriba, x[j] es la posición del hawk, pero no utilizamos las velocidades
			if(Math.abs(escapeEnergy) >= 1 ) {
				//ecuacion 1 
				if(randomValues.getQ() >= 0.5) {
					//System.out.println("Position ecc 1.1");
					x[j]  =  toBinary(x[j] - randomValues.getR1() * Math.abs(g.x[j] - (2 * randomValues.getR2() * g.x[j])));
				} else {  
					//System.out.println("Position ecc 1.2");  
					x[j] = toBinary(g.p[j] - averageHawksPosition - randomValues.getR3() * (LB + randomValues.getR4() * (UB - LB)));
				}
 
			} else {
				r = StdRandom.uniform();
				u = StdRandom.uniform();
				v = StdRandom.uniform();
				//comparar r y e
				rabbitJump = 2 * (1 - StdRandom.uniform());
				if(r >= 0.5 && Math.abs(escapeEnergy) >= 0.5) { //soft beseige
					deltaXt = p[j] - x[j];
					x[j] = toBinary((deltaXt) - escapeEnergy * Math.abs((rabbitJump * p[j]) - x[j]));
				} else if (r >= 0.5 && Math.abs(escapeEnergy) < 0.5) { // hard beseige
					// // update the location vector (updatePBest?) usando la ecuación (6) del paper
					x[j] = toBinary(p[j] - escapeEnergy * Math.abs(deltaXt));
				} else if (r < 0.5 && Math.abs(escapeEnergy) >= 0.5) { // soft beseige with progressive rapid dives
					// // update the location vector (updatePBest?) usando la ecuación (10) del paper
					S = StdRandom.uniform(2);
					//primero calcular (7)
					Y = p[j] - escapeEnergy * Math.abs((rabbitJump * p[j]) - x[j]);
					//Calcular (9)
					sigma = Math.pow((gammaFunction(1 + beta) * Math.sin((Math.PI*beta)/2))/(gammaFunction((1 + beta)/2) * beta * (2*(beta-1)/2)), (1/beta));
					levyFlight = 0.01 * (u*sigma/Math.pow(Math.abs(v), (1/beta)));
					//calcular (10)
					Z = Y + S * levyFlight;
					if (toBinary(Y) < x[j]) x[j] = toBinary(Y);
					if (toBinary(Z) < x[j]) x[j] = toBinary(Z);

				} else if (r < 0.5 && Math.abs(escapeEnergy) < 0.5) { // hard beseige with progressive rapid dives
					// // update the location vector (updatePBest?) usando la ecuación (11) del paper
					S = StdRandom.uniform(2);
					//primero calcular (7)
					Y = p[j] - escapeEnergy * Math.abs((rabbitJump * p[j]) - averageHawksPosition);
					//Calcular (9)
					sigma = Math.pow((gammaFunction(1 + beta) * Math.sin((Math.PI*beta)/2))/(gammaFunction((1 + beta)/2) * beta * (2*(beta-1)/2)), (1/beta));
					levyFlight = 0.01 * (u*sigma/Math.pow(Math.abs(v), (1/beta)));
					//calcular (10)
					Z = Y + S * levyFlight;
					if (toBinary(Y) < x[j]) x[j] = toBinary(Y);
					if (toBinary(Z) < x[j]) x[j] = toBinary(Z);
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
		}
	}

	@Override
	public String toString() {
		return String.format("optimal value: %d, fitness: %d, diff: %.1f, rpd: %.2f%%, p: %s", optimum(),
				computeFitnessPBest(), diff(), rpd(), showSolution());
	}

	static double logGamma(double x) {
		double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
		double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
						 + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
						 +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
		return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
	}

	static double gammaFunction(double x) { return Math.exp(logGamma(x)); }

	public double avg(int[] a) {
		return java.util.Arrays.stream(a).average().getAsDouble();
   	}

	protected double avgA() {
    	return avg(x);
    }

}
