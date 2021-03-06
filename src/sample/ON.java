package sample;

import dissimlab.random.SimGenerator;

public class ON extends Fuel {

    public ON(double ni){
        simGenerator = new SimGenerator();
        this.ni = ni;
    }

    @Override
    public double getTankTime() {
        return simGenerator.exponential(ni);
    }

    @Override
    public String toString() {
        return "ON";
    }
}
