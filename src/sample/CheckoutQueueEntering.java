package sample;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

public class CheckoutQueueEntering extends BasicSimEvent<PetrolStation, Car> {

    Car car;

    public CheckoutQueueEntering(PetrolStation entity, double delay, Car car) throws SimControlException {
        super(entity, delay, car);
        this.car = car;
    }

    @Override
    protected void stateChange() throws SimControlException {
        PetrolStation petrolStation = getSimObj();
        Checkout checkout = petrolStation.getUnoccupieCheckout();
        if(checkout == null){   //jesli nie ma wolnej checkouts
            handleOccupiedCheckouts(petrolStation);
        }
        else if(petrolStation.checkoutQueue.queue.size() > 0 ){       //jesli jest wolna checkout ale sa osoby w kolejce
            handleOccupiedCheckouts(petrolStation);
        }
        else{
            String log = simTime() + " :: Kierowca samochodu o ID: " + car.ID + " podchodzi do kasy numer " + checkout.ID;
            System.out.println(log);
            checkout.occupieCheckout(car);
            petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
            new CheckoutRelease(petrolStation, petrolStation.simGenerator.exponential(petrolStation.ni_checkout), checkout);
        }
    }

    private void handleOccupiedCheckouts(PetrolStation petrolStation){
        String log = simTime() + " :: Kierowca samochodu o ID: " + car.ID + " wchodzi do kolejki do checkouts";
        System.out.println(log);
        petrolStation.checkoutQueue.add(car);
        petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
