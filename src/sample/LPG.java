package sample;

import dissimlab.random.SimGenerator;

public class LPG extends Fuel {

    public LPG(double ni){
        simGenerator = new SimGenerator();
        this.ni = ni;
    }

    @Override
    public double getTankTime() {
        return simGenerator.exponential(ni);
    }

    @Override
    public String toString() {
        return "LPG";
    }
}
