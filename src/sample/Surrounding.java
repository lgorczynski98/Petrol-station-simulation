package sample;

import java.util.Random;

public class Surrounding {

    int index;
    int limit;
    Random random;

    int petrolFuelingCount = 0;
    int LPGFuelingCount = 0;
    int ONFuelingCount = 0;
    int washedCarsCount = 0;

    public Surrounding(int limit){
        this.limit = limit;
        this.index = 0;
        this.random = new Random();
    }

    public Car newCar(Fuel fuel){
        Car s;
        do {
            s = new Car(index, fuel, random.nextBoolean(), random.nextBoolean());
        }while(!s.isTanking && !s.isWashing);

        if(s.isTanking){
            switch(fuel.toString()){
                case "Petrol":
                    petrolFuelingCount++;
                    break;
                case "LPG":
                    LPGFuelingCount++;
                    break;
                case "ON":
                    ONFuelingCount++;
                    break;
            }
        }
        if(s.isWashing) washedCarsCount++;

        index++;
        return s;
    }
}
