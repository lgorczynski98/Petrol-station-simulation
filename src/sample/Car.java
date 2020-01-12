package sample;

import dissimlab.monitors.MonitoredVar;

public class Car {

    int ID;
    Fuel fuelType;
    boolean isTanking;
    boolean isWashing;
    Distributor occupiedDistributor = null;
    double arrivalTime;
    double carWashArrivalTime;

    public Car(int ID, Fuel fuel, boolean isTanking, boolean isWashing){
        this.ID = ID;
        this.fuelType = fuel;
        this.isTanking = isTanking;
        this.isWashing = isWashing;
        this.arrivalTime = 0;
        this.carWashArrivalTime = 0;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setTankTimeDelta(MonitoredVar monitoredVar, double endTime){
        monitoredVar.setValue(endTime - this.arrivalTime);
    }

    public void setCarWashArrivalTime(double carWashArrivalTime){
        this.carWashArrivalTime = carWashArrivalTime;
    }

    public void setCarWashTimeDelta(MonitoredVar monitoredVar, double endTime){
        monitoredVar.setValue(endTime - this.carWashArrivalTime);
    }
}
