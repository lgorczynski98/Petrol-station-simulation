package sample;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

public class CarWashQueueEntering extends BasicSimEvent<PetrolStation, Car> {

    Car car;

    public CarWashQueueEntering(PetrolStation entity, double delay, Car car) throws SimControlException {
        super(entity, delay, car);
        this.car = car;
    }

    @Override
    protected void stateChange() throws SimControlException {
        PetrolStation petrolStation = getSimObj();
        car.setCarWashArrivalTime(simTime());
        if(petrolStation.carWash.zajeta){  //jesli carWash jest isOccupied
            handleOccupiedCarWash(petrolStation);
        }
        else if(petrolStation.carWashQueue.queue.size() > 0){      //jesli carWash nie jest isOccupied ale jest queue
            handleOccupiedCarWash(petrolStation);
        }
        else{       //jesli nie ma kolejki i carWash jest wolna
            String log = simTime() + " :: Car o ID: " + car.ID + " wjezdza na myjnie";
            System.out.println(log);
            petrolStation.carWash.occupieCarWash(car);
            modifyMonitoredVar(petrolStation);
            petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
            new CarWashing(petrolStation, petrolStation.simGenerator.exponential(petrolStation.ni_carWash), car);
        }
    }

    private void handleOccupiedCarWash(PetrolStation petrolStation){
        String log = simTime() + " :: Samochod o ID " + car.ID + " wjezdza do kolejki na myjnie";
        System.out.println(log);
        petrolStation.carWashQueue.add(car);
        modifyMonitoredVar(petrolStation);
        petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
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
