public class Randoms {
    private double r1, r2, r3, r4, q;

    public Randoms () {
        
        q = StdRandom.uniform();
        r1 = StdRandom.uniform();
        r2 = StdRandom.uniform();
        r3 = StdRandom.uniform();
        r4 = StdRandom.uniform();
    }

    public double getR1(){
        return r1;
    }

    public double getR2(){
        return r2;
    }

    public double getR3(){
        return r3;
    }

    public double getR4(){
        return r4;
    }

    public double getQ(){
        return q;
    }

}
