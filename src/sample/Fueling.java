package sample;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

public class Fueling extends BasicSimEvent<PetrolStation, Distributor> {

    Distributor distributor;

    public Fueling(PetrolStation entity, double delay, Distributor distributor) throws SimControlException {
        super(entity, delay, distributor);
        this.distributor = distributor;
    }

    @Override
    protected void stateChange() throws SimControlException {
        PetrolStation petrolStation = getSimObj();
        String log = simTime() + " :: Zakonczenie tankowania: " + distributor.toString() + " przez samochod o ID " + distributor.car.ID;
        System.out.println(log);
        petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
        new CheckoutQueueEntering(petrolStation, 0, distributor.car);
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
