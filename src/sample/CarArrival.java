package sample;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

import java.util.List;
import java.util.Random;

public class CarArrival extends BasicSimEvent<PetrolStation, Object> {

    public CarArrival(PetrolStation petrolStation, double delay) throws SimControlException {
        super(petrolStation, delay);
    }

    @Override
    protected void stateChange() throws SimControlException {
        PetrolStation petrolStation = getSimObj();
        Random random = new Random();
        Fuel fuel = PetrolStation.fuelTypes[random.nextInt(3)];
        Car car = petrolStation.surrounding.newCar(fuel);
        car.setArrivalTime(simTime());
        String log = simTime() + " :: Przybycie samochodu o ID " + car.ID + "(tankowanie = " + car.isTanking + ", mycie = " + car.isWashing + ")";
        System.out.println(log);
        Distributor unoccupiedDistributor = petrolStation.getUnoccupiedDistributor(fuel);
        petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
        if(car.isTanking){    //jesli bedzie tankowac
            if(unoccupiedDistributor == null){   //jesli nie ma wolnych dystrybutorow
                queueEntering(petrolStation, car, fuel);
            }
            else if(petrolStation.getFuelQueue(fuel).queue.size() > 0){   //jesli distributor jest wolny i sa samochody w kolejce
                queueEntering(petrolStation, car, fuel);
            }
            else{       //jesli dytrybutor jest wolny i nie ma samochodow w kolejce
                unoccupiedDistributor.occupieDistributor(car);
                car.occupiedDistributor = unoccupiedDistributor;
                modifyMonitoredVar(petrolStation, car);
                log = simTime() + " :: Zajecie " + unoccupiedDistributor.toString() + " przez samochod o ID " + car.ID + " oraz rozpoczecie tankowania";
                System.out.println(log);
                petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
                new Fueling(petrolStation, petrolStation.simGenerator.exponential(car.fuelType.ni), unoccupiedDistributor);
            }
        }
        else{   //jesli tylko myje car
            if(petrolStation.carWashQueue.queue.size() >= petrolStation.carWashQueue.size){
                log = simTime() + " :: Samochod o ID " + car.ID + " trafia na straty myjni";
                System.out.println(log);
                petrolStation.loss.incrementCarWashLoss();
                petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
            }
            else{
                new CheckoutQueueEntering(petrolStation, 0, car);
            }
        }

        if(petrolStation.surrounding.index < petrolStation.surrounding.limit)
            new CarArrival(petrolStation, petrolStation.simGenerator.exponential(petrolStation.lambda));
    }

    private void queueEntering(PetrolStation petrolStation, Car car, Fuel fuel){
        if (!petrolStation.getFuelQueue(fuel).add(car)) {     //dodajemy do kolejki jesli jest miejsce
            String log = simTime() + " :: Samochod o ID " + car.ID + " trafia na straty paliwa";
            System.out.println(log);
            petrolStation.loss.incrementFuelLoss();
            petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
        }
        else{
            modifyMonitoredVar(petrolStation, car);
            String log = simTime() + " :: Car o ID " + car.ID + " wchodzi do kolejki po fuel";
            System.out.println(log);
            petrolStation.simStateMemorizer.memorizeState(petrolStation, log);
        }
    }

    private void modifyMonitoredVar(PetrolStation petrolStation, Car car){
        switch (car.fuelType.toString()){
            case "Petrol":{
                int count = getOccupiedDistributorCount(petrolStation.petrolDistributors);
                petrolStation.carsInPetrolQueueCount.setValue(petrolStation.petrolQueue.queue.size() + count);
                break;
            }
            case "LPG":{
                int count = getOccupiedDistributorCount(petrolStation.LPGDistributors);
                petrolStation.carsInLPGQueueCount.setValue(petrolStation.LPGQueue.queue.size() + count);
                break;
            }
            case "ON":{
                int count = getOccupiedDistributorCount(petrolStation.ONDistributors);
                petrolStation.carsInONQueueCount.setValue(petrolStation.ONQueue.queue.size() + count);
                break;
            }
        }
    }

    private int getOccupiedDistributorCount(List<Distributor> distributors){
        int count = 0;
        for (Distributor distributor : distributors){
            if(distributor.isOccupied)
                count++;
        }
        return count;
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
