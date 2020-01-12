package sample;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;
import java.util.List;

public class SimStateMemorizer {
    List<GridPane> gridPanes = new ArrayList<>();
    List<String> logList = new ArrayList<>();

    public void memorizeState(PetrolStation petrolStation, String log){
        int column = 0;
        GridPane gridPane = new GridPane();

        logList.add(log);

        memorizePetrolQueue(gridPane, petrolStation, column);
        column++;
        memorizeLPGQueue(gridPane, petrolStation, column);
        column++;
        memorizeONQueue(gridPane, petrolStation, column);
        column++;
        memorizeCheckoutQueue(gridPane, petrolStation, column);
        column++;
        memorizeCarWashQueue(gridPane, petrolStation, column);
        column++;
        column = memorizePetrolDistributors(gridPane, petrolStation, column);
        column = memorizeLPGDistributors(gridPane, petrolStation, column);
        column = memorizeONDistributors(gridPane, petrolStation, column);
        column = memorizeCheckouts(gridPane, petrolStation, column);
        memorizeCarWash(gridPane, petrolStation, column);

        gridPanes.add(gridPane);
    }

    private StackPane getCarCircle(int ID){
        final Circle circle = new Circle(40);
        circle.setStrokeWidth(5);
        circle.setStroke(Color.RED);
        circle.setFill(Color.AZURE);
        circle.setStrokeType(StrokeType.INSIDE);

        final Text text = new Text("" + ID);
        text.setFont(new Font(25));
        text.setBoundsType(TextBoundsType.VISUAL);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(circle, text);

        return stackPane;
    }

    private void memorizePetrolQueue(GridPane gridPane, PetrolStation petrolStation, int column){
        ImageView petrolQueueImage = new ImageView("95.jpg");
        int row = 0;
        gridPane.add(petrolQueueImage, column, row++);
        for (Car car : petrolStation.petrolQueue.queue){
            gridPane.add(getCarCircle(car.ID), column, row++);
        }
    }

    private void memorizeLPGQueue(GridPane gridPane, PetrolStation petrolStation, int column){
        ImageView LPGQueueImage = new ImageView("LPG.jpg");
        int row = 0;
        gridPane.add(LPGQueueImage, column, row++);
        for (Car car : petrolStation.LPGQueue.queue){
            gridPane.add(getCarCircle(car.ID), column, row++);
        }
    }

    private void memorizeONQueue(GridPane gridPane, PetrolStation petrolStation, int column){
        ImageView ONQueueImage = new ImageView("ON.jpg");
        int row = 0;
        gridPane.add(ONQueueImage, column, row++);
        for (Car car : petrolStation.ONQueue.queue){
            gridPane.add(getCarCircle(car.ID), column, row++);
        }
    }

    private void memorizeCheckoutQueue(GridPane gridPane, PetrolStation petrolStation, int column){
        ImageView checkoutQueueImage = new ImageView("kolejkaDoKasy.jpg");
        int row = 0;
        gridPane.add(checkoutQueueImage, column, row++);
        for (Car car : petrolStation.checkoutQueue.queue){
            gridPane.add(getCarCircle(car.ID), column, row++);
        }
    }

    private void memorizeCarWashQueue(GridPane gridPane, PetrolStation petrolStation, int column){
        ImageView carWashQueueImage = new ImageView("kolejkaDoMyjni.jpg");
        int row = 0;
        gridPane.add(carWashQueueImage, column, row++);
        for (Car car : petrolStation.carWashQueue.queue){
            gridPane.add(getCarCircle(car.ID), column, row++);
        }
    }

    private int memorizePetrolDistributors(GridPane gridPane, PetrolStation petrolStation, int column){
        for (Distributor distributor : petrolStation.petrolDistributors) {
            ImageView petrolDistributorImage = new ImageView("dyspozytor95.jpg");
            int row = 0;
            gridPane.add(petrolDistributorImage, column, row++);
            addingCarCircles(gridPane, distributor, row, column);
            column++;
        }
        return column;
    }

    private int memorizeLPGDistributors(GridPane gridPane, PetrolStation petrolStation, int column){
        for (Distributor distributor : petrolStation.LPGDistributors) {
            ImageView LPGDistributorImage = new ImageView("dyspozytorLPG.jpg");
            int row = 0;
            gridPane.add(LPGDistributorImage, column, row++);
            addingCarCircles(gridPane, distributor, row, column);
            column++;
        }
        return column;
    }

    private int memorizeONDistributors(GridPane gridPane, PetrolStation petrolStation, int column){
        for (Distributor distributor : petrolStation.ONDistributors) {
            ImageView ONDistributorImage = new ImageView("dyspozytorON.jpg");
            int row = 0;
            gridPane.add(ONDistributorImage, column, row++);
            addingCarCircles(gridPane, distributor, row, column);
            column++;
        }
        return column;
    }

    private void addingCarCircles(GridPane gridPane, Distributor distributor, int row, int column){
        if(distributor.car != null)
            gridPane.add(getCarCircle(distributor.car.ID), column, row);
    }

    private int memorizeCheckouts(GridPane gridPane, PetrolStation petrolStation, int column){
        for (Checkout checkout : petrolStation.checkouts) {
            ImageView checkoutImage = new ImageView("kasa.jpg");
            int row = 0;
            gridPane.add(checkoutImage, column, row++);
            if(checkout.car != null)
                gridPane.add(getCarCircle(checkout.car.ID), column++, row);
            else
                column++;
        }
        return column;
    }

    private void memorizeCarWash(GridPane gridPane, PetrolStation petrolStation, int column){
        ImageView carWashImage = new ImageView("myjnia.jpg");
        int row = 0;
        gridPane.add(carWashImage, column, row++);
        if(petrolStation.carWash.car != null)
            gridPane.add(getCarCircle(petrolStation.carWash.car.ID), column, row);
    }
}
