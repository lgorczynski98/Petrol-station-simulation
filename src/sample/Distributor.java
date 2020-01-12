package sample;

public class Distributor {

    public Fuel fuel;
    boolean isOccupied = false;
    Car car = null;
    int ID;

    private static int number = 1;

    public Distributor(Fuel fuel){
        this.ID = number++;
        this.fuel = fuel;
    }

    public void occupieDistributor(Car car){
        if(!isOccupied){
            isOccupied = true;
            this.car = car;
        }
    }

    public Car releaseDistributor(){
        if(isOccupied){
            isOccupied = false;
            Car s = this.car;
            this.car = null;
            return s;
        }
        return null;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("Distributor ");
        str.append(fuel.toString());
        str.append(" o numerze: ");
        str.append(ID);
        return str.toString();
    }
}
