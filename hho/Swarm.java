import java.util.ArrayList;
import java.util.List;

public class Swarm {

    private final float theta = 0.9f, alpha = 2f;
    private List<Hawk> swarm = null;
    private Hawk g = null;
    private final int maxItter = 1000;
    private final int populationSize = 25;
    private static final double beta = 1.5;
    private double escapeEnergy = 0;
    private double baseEnergy = 0;
    private Randoms randomValues;
    private double Upper = 1;
    private double Lower = -1;


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
            //System.out.println("Itteration: "+ iteration);
            //creación de random hawk
            Hawk randomHawk = new Hawk();
            // seteo de valores random
            // pasar q y r's a función move, mejor pasarlo como objeto estilo: move(randoms, ....) donde  randoms contiene: randoms.r1, randoms.r2, randoms.q, ...
            randomValues = new Randoms();
            //setear E0
            baseEnergy = StdRandom.uniform(Lower, Upper);
            //calcular energia de escape de la presa
            escapeEnergy = 2 * baseEnergy * (1 - (iteration/ maxItter)); 
            // calculo del salto del conejo "j"
            // si el valor absoluto de E es >= 1 se utiliza función (1) paper
            double averageItterationPositionOfSwarm = averagePositionOfSwarm();
            for (int i_hawk = 0; i_hawk < populationSize; i_hawk++) {
                do {
                    // En nuestro caso, debemos considerar el g y el randomhawk, el cual debe utilizarse en el move según las ecuaciones del paper
                    randomHawk.copy(swarm.get(StdRandom.uniform(populationSize)));
                    randomHawk.move(g, theta, alpha, beta, escapeEnergy, randomValues, averageItterationPositionOfSwarm);
                } while(!randomHawk.isFeasible());
                if (randomHawk.isBetterThanPBest())
					randomHawk.updatePBest();
				swarm.get(i_hawk).copy(randomHawk);
            }
            for (int i_hawk = 0; i_hawk < populationSize; i_hawk++) {
                if (swarm.get(i_hawk).isBetterThan(g)) {
                    g.copy(swarm.get(i_hawk));
                }
            }
            log(iteration);
            System.out.println("baseEnergy: "+baseEnergy+ " EscapeEnergy"+escapeEnergy);
            iteration++;
        }
    }

    private double averagePositionOfSwarm() {
		double xm = 0;
        for (int i = 0; i < populationSize; i++) {
            xm = xm + swarm.get(i).avgA();
        }
        double promedio = xm/populationSize;
        System.out.println("Promedio: "+promedio);
        double random = StdRandom.uniform()/populationSize;
        System.out.println("Random: "+random);

		return promedio;
	}

    private void log(int t) {
        StdOut.printf("t=%d,\t%s\n", t, g);
    }
    
}
