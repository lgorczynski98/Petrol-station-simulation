package sample;

import dissimlab.monitors.MonitoredVar;
import dissimlab.random.SimGenerator;
import dissimlab.simcore.BasicSimObj;
import dissimlab.simcore.SimManager;
import java.util.ArrayList;
import java.util.List;

public class PetrolStation extends BasicSimObj {

    static final Fuel[] fuelTypes = {new Petrol(4), new LPG(6), new ON(5)};

    private static final int carsLimit = 100;
    private static final int petrolQueueSize = 5;
    private static final int LPGQueueSize = 3;
    private static final int ONQueueSize = 4;
    private static final int carWashQueueSize = 4;

    List<Distributor> petrolDistributors;
    List<Distributor> LPGDistributors;
    List<Distributor> ONDistributors;
    CarQueue petrolQueue;
    CarQueue LPGQueue;
    CarQueue ONQueue;
    int petrolDistributorsCount = 2;
    int LPGDistributorsCount = 1;
    int ONDistributorsCount = 1;

    Loss loss;
    Surrounding surrounding;
    double lambda;
    double ni_checkout;
    double ni_carWash;

    List<Checkout> checkouts;
    int checkoutsCount = 1;
    CarQueue checkoutQueue;

    CarWash carWash;
    CarQueue carWashQueue;

    SimGenerator simGenerator;
    SimManager simManager;

    MonitoredVar carsInPetrolQueueCount;
    MonitoredVar carsInLPGQueueCount;
    MonitoredVar carsInONQueueCount;
    MonitoredVar carsInCarWashQueueCount;
    MonitoredVar fuelingTime;
    MonitoredVar carWashingTime;

    SimStateMemorizer simStateMemorizer;

    public PetrolStation(double lambda, double ni_checkout, double ni_carWash, SimManager simManager, SimStateMemorizer simStateMemorizer){
        this.simStateMemorizer = simStateMemorizer;
        this.lambda = lambda;
        this.ni_checkout = ni_checkout;
        this.ni_carWash = ni_carWash;
        this.simManager = simManager;
        prepareMonitoredVars();
        prepareQueues();
        prepareDistributors();
        prepareCheckouts();
        loss = new Loss();
        surrounding = new Surrounding(carsLimit);
        carWash = new CarWash();
        simGenerator = new SimGenerator();
    }

    private void prepareMonitoredVars(){
        this.carsInPetrolQueueCount = new MonitoredVar(simManager);
        this.carsInLPGQueueCount = new MonitoredVar(simManager);
        this.carsInONQueueCount = new MonitoredVar(simManager);
        this.carsInCarWashQueueCount = new MonitoredVar(simManager);
        this.fuelingTime = new MonitoredVar(simManager);
        this.carWashingTime = new MonitoredVar(simManager);
    }

    private void prepareQueues(){
        petrolQueue = new CarQueue(petrolQueueSize);
        LPGQueue = new CarQueue(LPGQueueSize);
        ONQueue = new CarQueue(ONQueueSize);
        carWashQueue = new CarQueue(carWashQueueSize);
        checkoutQueue = new CarQueue(1000);
    }

    private void prepareDistributors(){
        petrolDistributors = new ArrayList<>();
        for (int i = 0; i < petrolDistributorsCount; i++) {
            petrolDistributors.add(new Distributor(fuelTypes[0]));
        }

        LPGDistributors = new ArrayList<>();
        for (int i = 0; i < LPGDistributorsCount; i++) {
            LPGDistributors.add(new Distributor(fuelTypes[1]));
        }

        ONDistributors = new ArrayList<>();
        for (int i = 0; i < ONDistributorsCount; i++) {
            ONDistributors.add(new Distributor(fuelTypes[2]));
        }
    }

    private void prepareCheckouts(){
        checkouts = new ArrayList<>();
        for (int i = 0; i < checkoutsCount; i++) {
            checkouts.add(new Checkout());
        }
    }

    public Distributor getUnoccupiedDistributor(Fuel fuel){
        switch (fuel.toString()){
            case "Petrol":{
                for (Distributor distributor : petrolDistributors) {
                    if(!distributor.isOccupied)
                        return distributor;
                }
                break;
            }
            case "LPG":{
                for (Distributor distributor : LPGDistributors) {
                    if(!distributor.isOccupied)
                        return distributor;
                }
                break;
            }
            case "ON":{
                for (Distributor distributor : ONDistributors) {
                    if(!distributor.isOccupied)
                        return distributor;
                }
                break;
            }
        }
        return null;
    }

    public CarQueue getFuelQueue(Fuel fuel){
        switch (fuel.toString()){
            case "Petrol":
                return petrolQueue;
            case "LPG":
                return LPGQueue;
            case "ON":
                return ONQueue;
        }
        return null;
    }

    public Checkout getUnoccupieCheckout(){
        for (Checkout checkout : checkouts) {
            if(!checkout.isOccupied)
                return checkout;
        }
        return null;
    }
}
