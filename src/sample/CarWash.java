package sample;

public class CarWash {

    Car car = null;
    boolean zajeta = false;

    public void occupieCarWash(Car car){
        this.zajeta = true;
        this.car = car;
    }

    public Car releaseCarWash(){
        this.zajeta = false;
        Car s = this.car;
        this.car = null;
        return s;
    }
}
