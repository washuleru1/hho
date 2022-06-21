import java.util.ArrayList;
import java.util.List;

public class Swarm {

    private List<Hawk> swarm = null;
    private Hawk g = null;
    private final int maxItter = 1000;
    private final int populationSize = 25;
    private static final double beta = 1.5;
    private double u, v = 0;
    private double escapeEnergy = 0;
    private double baseEnergy = 0;
    private double q, r1, r2, r3, r4 = 0;
    private double rabbitJump = 0;
    private double UB = 1;
    private double LB = 0;
    private double r = 0.5;
    private double nextLocation = 0;
    private double deltaXt = 0;
    private double sigma = 0;
    private double levyFlight = 0;


    public void execute() {
        init();
        evolve();
    }

    private void init() {
        swarm = new ArrayList<Hawk>();
        g = new Hawk();
        Hawk hawk = null;
        for (int i = 1; i <= populationSize; i++) {
			do {
				hawk = new Hawk();
			} while (!hawk.isFeasible());
			hawk.updatePBest();
			swarm.add(hawk);
		}
		g.copy(swarm.get(0));
		for (int i = 1; i < populationSize; i++)
			if (swarm.get(i).isBetterThan(g))
				g.copy(swarm.get(i));
		log(0);


    }

    private void evolve() {
        int iteration = 1;
        while (iteration <= maxItter){
            // seteo de valores random
            q = StdRandom.uniform();
            r1 = StdRandom.uniform();
            r2 = StdRandom.uniform();
            r3 = StdRandom.uniform();
            r4 = StdRandom.uniform();
            //setear E0
            baseEnergy = 2 * StdRandom.uniform(-1,1);
            //calcular energia de escape de la presa
            escapeEnergy = 2 * baseEnergy * (1 - (iteration/ maxItter)); 
            // calculo del salto del conejo "j"
            // si el valor absoluto de E es >= 1 se utiliza función (1) paper
            
            if(Math.abs(escapeEnergy) >= 1 ) {
               //ecuacion 1 
                if(q >= 0.5) {
                    Hawk randomHawk = swarm.get(StdRandom.uniform(populationSize));
                    nextLocation  = randomHawk.computeFitnessPBest() - r1 * Math.abs(randomHawk.computeFitnessPBest() - (2 * r2*randomHawk.computeFitness()));
                } else {    
                    X_m = 
                    X_rabbit - X_m - r3 * (LB + r4 * (UB - LB));
                }

            } else {
                r = StdRandom.uniform();
                u = StdRandom.uniform();
                v = StdRandom.uniform();
                sigma = Math.pow((r * (1 + beta) * Math.sin((Math.PI*beta)/2))/(r * ((1 + beta)/2) * beta * (2*(beta-1)/2)), (1/beta));
                //comparar r y e
                rabbitJump = 2 * (1 - StdRandom.uniform());
                if(r >= 0.5 && Math.abs(escapeEnergy) >= 0.5) { //soft beseige
                    nextLocation = (deltaXt) - escapeEnergy * Math.abs((rabbitJump * xrabbit) - xt);
                    // update the location vector (updatePBest?) usando la ecuación (4) del paper
                    // X(t+1)
                } else if (r >= 0.5 && Math.abs(escapeEnergy) < 0.5) { // hard beseige
                    // // update the location vector (updatePBest?) usando la ecuación (6) del paper
                    nextLocation = xrabbit - escapeEnergy * Math.abs(deltaXt);
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

            //algoritmo

            log(iteration);
            iteration++;
        }
    }


    private void log(int t) {
        StdOut.printf("t=%d,\t%s\n", t, g);
    }
    
}
