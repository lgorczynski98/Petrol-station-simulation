package sample;

import dissimlab.random.SimGenerator;

public abstract class Fuel {

    protected SimGenerator simGenerator;
    protected double ni;

    public abstract double getTankTime();
    public abstract String toString();
}
