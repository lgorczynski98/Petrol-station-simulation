package sample;

import java.util.LinkedList;
import java.util.List;

public class CarQueue {

    List<Car> queue;
    int size;

    public CarQueue(int size){
        this.size = size;
        this.queue = new LinkedList<>();
    }

    public boolean add(Car car){
        if(queue.size() < size){
            queue.add(car);
            return true;
        }
        return false;
    }

    public Car removeFirst(){
        try {
            Car s = queue.remove(0);
            return s;
        }
        catch(Exception e) {
            return null;
        }
    }

    public boolean remove(Car car){
        return this.queue.remove(car);
    }
}
