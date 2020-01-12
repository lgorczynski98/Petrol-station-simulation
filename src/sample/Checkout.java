package sample;

public class Checkout {

    Car car = null;
    boolean isOccupied = false;
    int ID;
    private static int number = 1;

    public Checkout(){
        this.ID = number++;
    }

    public void occupieCheckout(Car car){
        this.isOccupied = true;
        this.car = car;
    }

    public Car releaseCheckout(){
        this.isOccupied = false;
        Car s = this.car;
        this.car = null;
        return s;
    }
}
