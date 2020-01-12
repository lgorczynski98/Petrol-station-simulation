package sample;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

public class CarWashing extends BasicSimEvent<PetrolStation, Car> {

    Car car;

    public CarWashing(PetrolStation entity, double delay, Car car) throws SimControlException {
        super(entity, delay, car);
        this.car = car;
    }

    @Override
    protected void stateChange() throws SimControlException {
        PetrolStation petrolStation = getSimObj();
        car.setCarWashTimeDelta(petrolStation.carWashingTime, simTime());
        petrolStation.carWashQueue.remove(car);
        String log = simTime() + " :: Samochod o ID " + car.ID + " wyjezdza z myjni";
        System.out.println(log);
        petrolStation.carWash.releaseCarWash();
        modifyMonitoredVar(petrolStation);
        petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
        Car nextCar = petrolStation.carWashQueue.removeFirst();
        if(nextCar != null){   // jesli czekaja samochody w kolejce
            petrolStation.carWash.occupieCarWash(nextCar);
            log = simTime() + " :: Samochod o ID: " + nextCar.ID + " wjezdza na myjnie";
            System.out.println(log);
            petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
            new CarWashing(petrolStation, petrolStation.simGenerator.exponential(petrolStation.ni_carWash), nextCar);
        }
    }

    private void modifyMonitoredVar(PetrolStation petrolStation){
        petrolStation.carsInCarWashQueueCount.setValue(petrolStation.carWashQueue.queue.size() + (petrolStation.carWash.zajeta ? 1 : 0));
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
