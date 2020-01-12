package sample;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

public class CheckoutRelease extends BasicSimEvent<PetrolStation, Checkout> {

    Checkout checkout;

    public CheckoutRelease(PetrolStation petrolStation, double delay, Checkout checkout) throws SimControlException {
        super(petrolStation, delay, checkout);
        this.checkout = checkout;
    }

    @Override
    protected void stateChange() throws SimControlException {
        PetrolStation petrolStation = getSimObj();
        Car car = checkout.releaseCheckout();
        petrolStation.checkoutQueue.remove(car);
        if(car.isTanking){    //jesli car tankowal
            petrolStation.getFuelQueue(car.fuelType).remove(car);
            car.setTankTimeDelta(petrolStation.fuelingTime, simTime());
            String log = simTime() + " :: Kierowca samochodu o ID " + car.ID + " odchodzi od kasy numer " + checkout.ID + " oraz zwalnia dystrybutor o numerze " + car.occupiedDistributor.ID;
            System.out.println(log);
            Distributor distributor = car.occupiedDistributor;
            car.occupiedDistributor.releaseDistributor();

            petrolStation.simStateMemorizer.memorizeState(petrolStation, log);

            modifyMonitoredVar(petrolStation, car);
            releasePlaceInQueueToCheckout(petrolStation);

            if(car.isWashing){
                new CarWashQueueEntering(petrolStation, 0, car);
            }

            if(petrolStation.getFuelQueue(car.fuelType).queue.size() > 0){
                Car nextCar = petrolStation.getFuelQueue(distributor.fuel).removeFirst();
                if(nextCar == null){
                    System.out.println("Zwolnienie kasy - nastepny to null");
                    return;
                }
                distributor.occupieDistributor(nextCar);
                nextCar.occupiedDistributor = distributor;
                log = simTime() + " :: Zajecie " + distributor.toString() + " przez samochod o ID " + nextCar.ID + " oraz rozpoczecie tankowania";
                System.out.println(log);
                petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
                new Fueling(petrolStation, petrolStation.simGenerator.exponential(nextCar.fuelType.ni), distributor);
            }
        }
        else if(car.isWashing){     //jesli bedzie myc car
            String log = simTime() + " :: Kierowca samochodu o ID " + car.ID + " odchodzi od kasy numer " + checkout.ID;
            System.out.println(log);
            petrolStation.simStateMemorizer.memorizeState(petrolStation, log);

            releasePlaceInQueueToCheckout(petrolStation);

            if(petrolStation.carWashQueue.queue.size() >= petrolStation.carWashQueue.size){
                log = simTime() + " :: Samochod o ID " + car.ID + " trafia na straty myjni";
                System.out.println(log);
                petrolStation.loss.incrementCarWashLoss();
                petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
            }
            else
                new CarWashQueueEntering(petrolStation, 0, car);
        }
    }

    private void releasePlaceInQueueToCheckout(PetrolStation petrolStation) throws SimControlException{
        if (petrolStation.checkoutQueue.queue.size() > 0){
            Car nextCar = petrolStation.checkoutQueue.removeFirst();
            String log = simTime() + " :: Kierowca samochodu o ID: " + nextCar.ID + " podchodzi do kasy numer " + checkout.ID;
            System.out.println(log);
            checkout.occupieCheckout(nextCar);
            petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
            new CheckoutRelease(petrolStation, petrolStation.simGenerator.exponential(petrolStation.ni_checkout), checkout);
        }
    }

    private void modifyMonitoredVar(PetrolStation petrolStation, Car car){
        switch (car.fuelType.toString()){
            case "Petrol":{
                int count = 0;
                for (Distributor distributor : petrolStation.petrolDistributors)
                    if(distributor.isOccupied)
                        count++;
                petrolStation.carsInPetrolQueueCount.setValue(petrolStation.petrolQueue.queue.size() + count);
            }
            case "LPG":{
                int count = 0;
                for (Distributor distributor : petrolStation.LPGDistributors)
                    if(distributor.isOccupied)
                        count++;
                petrolStation.carsInLPGQueueCount.setValue(petrolStation.LPGQueue.queue.size() + count);
            }
            case "ON":{
                int count = 0;
                for (Distributor distributor : petrolStation.ONDistributors)
                    if(distributor.isOccupied)
                        count++;
                petrolStation.carsInONQueueCount.setValue(petrolStation.ONQueue.queue.size() + count);
            }
        }
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
