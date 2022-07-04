import java.util.ArrayList;
import java.util.List;

public class Swarm {

    private final float theta = 0.9f, alpha = 2f;
    private List<Hawk> swarm = null;
    private Hawk g = null;
    private final int maxItter = 400;
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
        swarm = new java.util.ArrayList<>();
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
        Hawk randomHawk = new Hawk();
        while (iteration <= maxItter){
            System.out.println("Itteration: "+ iteration);
            //creación de random hawk
            // seteo de valores random
            // pasar q y r's a función move, mejor pasarlo como objeto estilo: move(randoms, ....) donde  randoms contiene: randoms.r1, randoms.r2, randoms.q, ...
            randomValues = new Randoms();
            
           
            //calcular energia de escape de la presa
            double r = StdRandom.uniform();
            // calculo del salto del conejo "j"
            // si el valor absoluto de E es >= 1 se utiliza función (1) paper
            double S = StdRandom.uniform(2);
			double u = StdRandom.uniform();
			double v = StdRandom.uniform();
            double rabbitJump = 2 * (1 - StdRandom.uniform());
            double averageItterationPositionOfSwarm = averagePositionOfSwarm();
            for (int i_hawk = 0; i_hawk < populationSize; i_hawk++) {
                S = StdRandom.uniform(2);
                //setear E0
                //--- desde aca se updatean para cada halcon segun el paper
                baseEnergy = StdRandom.uniform(Lower, Upper);
                escapeEnergy = 2 * baseEnergy * (1 - (iteration/ maxItter));
                rabbitJump = 2 * (1 - StdRandom.uniform());
                // hasta aca se updatean para cada halcon segun el paper
                r = StdRandom.uniform();
                if (Math.abs(escapeEnergy) >= 1) {
                    //exploration
                    averageItterationPositionOfSwarm = averagePositionOfSwarm();
                    System.out.println("Exploration");
                    do {
                        // En nuestro caso, debemos considerar el g y el randomhawk, el cual debe utilizarse en el move según las ecuaciones del paper
                        randomHawk.copy(swarm.get(StdRandom.uniform(populationSize)));
                        randomHawk.exploration(g, randomValues, averageItterationPositionOfSwarm);
                    } while(!randomHawk.isFeasible());
                    swarm.get(i_hawk).copy(randomHawk);
                } else {
                    System.out.println("Exploit");
                    //Exploitation
                    randomHawk.copy(swarm.get(i_hawk));
                    if(r >= 0.5 && Math.abs(escapeEnergy) >= 0.5) { //soft beseige
                        System.out.println("Soft Beseige");
                        do {
                            randomHawk.softBeseige(g, escapeEnergy, rabbitJump);
                            
                        } while (!randomHawk.isFeasible());
                    } else if (r >= 0.5 && Math.abs(escapeEnergy) < 0.5) { //hard beseige
                        System.out.println("Hard Beseige");
                        do {
                            randomHawk.hardBeseige(g, escapeEnergy);
                        } while (!randomHawk.isFeasible());
                    } else if (r < 0.5 && Math.abs(escapeEnergy) >= 0.5) { // soft beseige with progresive
                        System.out.println("SOFT PROGRESIVE");
                        do {
                            randomHawk.softBeseigeProgresive(g, escapeEnergy, beta, S, u, v, rabbitJump);
                        } while (!randomHawk.isFeasible());
                    } else if (r < 0.5 && Math.abs(escapeEnergy) < 0.5) { // hard beseige with progresive
                        System.out.println("HARD PROGRESIVE");
                        averageItterationPositionOfSwarm = averagePositionOfSwarm();
                        do {
                            randomHawk.hardBeseigeProgresive(g, escapeEnergy, beta, averageItterationPositionOfSwarm, S, u, v, rabbitJump);
                        } while (!randomHawk.isFeasible());
                    }
                    swarm.get(i_hawk).copy(randomHawk);

                }
                if (randomHawk.isBetterThan(g)) {
					g.copy(randomHawk);
				}
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
            //System.out.println("baseEnergy: "+baseEnergy+ " EscapeEnergy"+escapeEnergy);
            iteration++;
        }
    }

    private double averagePositionOfSwarm() {
		double xm = 0;
        for (int i = 0; i < populationSize; i++) {
            xm = xm + swarm.get(i).avgA();
        }
        double promedio = (xm/populationSize);
        //double random = StdRandom.uniform()/populationSize;
        //System.out.println("Random: "+random);
		return promedio;
	}

    private void log(int t) {
        StdOut.printf("t=%d,\t%s\n", t, g);
    }
    
}
