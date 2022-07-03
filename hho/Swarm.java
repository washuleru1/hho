import java.util.ArrayList;
import java.util.List;

public class Swarm {

    private final float theta = 0.9f, alpha = 2f, betaMove = 2f;
    private List<Hawk> swarm = null;
    private Hawk g = null;
    private final int maxItter = 1000;
    private final int populationSize = 25;
    private static final double beta = 1.5;
    private double escapeEnergy = 0;
    private double baseEnergy = 0;
    private double r = 0.5;
    private double[] nextLocation;
    private double deltaXt = 0;
    private double levyFlight = 0;
    private Randoms randomValues;


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
        double averageHawksPosition;
        while (iteration <= maxItter){
            //creación de random hawk
            Hawk randomHawk = new Hawk();
            int[] actualHawkPosition;
            // seteo de valores random
            // pasar q y r's a función move, mejor pasarlo como objeto estilo: move(randoms, ....) donde  randoms contiene: randoms.r1, randoms.r2, randoms.q, ...
            randomValues = new Randoms();
            //setear E0
            baseEnergy = 2 * StdRandom.uniform(-1,1);
            //calcular energia de escape de la presa
            escapeEnergy = 2 * baseEnergy * (1 - (iteration/ maxItter)); 
            // calculo del salto del conejo "j"
            // si el valor absoluto de E es >= 1 se utiliza función (1) paper
            for (int i_hawk = 0; i_hawk < populationSize; i_hawk++) {
                do {
                    // En nuestro caso, debemos considerar el g y el randomhawk, el cual debe utilizarse en el move según las ecuaciones del paper
                    randomHawk.copy(swarm.get(StdRandom.uniform(populationSize)));
                    randomHawk.move(g, theta, alpha, beta, escapeEnergy, randomValues, averagePositionOfSwarm());
                } while(!randomHawk.isFeasible());
            }
            for (int i_hawk = 0; i_hawk < populationSize; i_hawk++) {
                if (swarm.get(i_hawk).isBetterThan(g)) {
                    g.copy(swarm.get(i_hawk));
                }
            }
            log(iteration);
            iteration++;
        }
    }


    private double averagePositionOfSwarm() {
        double sumxt = 0;
        return sumxt/populationSize;
    }

    private void log(int t) {
        StdOut.printf("t=%d,\t%s\n", t, g);
    }
    
}
