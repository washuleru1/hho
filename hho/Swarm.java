import java.util.ArrayList;
import java.util.List;

public class Swarm {

    private List<Hawk> swarm = null;
    private Hawk g = null;
    private final int maxItter = 1000;
    private final int populationSize = 25;
    private double escapeEnergy = 0;
    private double baseEnergy = 0;
    private double q = 0;
    private double rabbitJump = 0;

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
            q = StdRandom.uniform();
            //setear E0
            baseEnergy = 2 * StdRandom.uniform(-1,1);
            //calcular energia de escape de la presa
            escapeEnergy = 2 * baseEnergy * (1 - (iteration/ maxItter)); 
            // calculo del salto del conejo "j"
            rabbitJump = 2 * (1 - StdRandom.uniform());
            // si el valor absoluto de E es >= 1 se utiliza función (1) paper
            if(Math.abs(escapeEnergy) >= 1 ) {
               //ecuacion 1 
                if(q >= 0.5) {
                    
                } else {    

                }

            } else {
                //comparar r y e
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
