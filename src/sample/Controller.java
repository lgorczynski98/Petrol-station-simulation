package sample;

import dissimlab.monitors.Diagram;
import dissimlab.monitors.Statistics;
import dissimlab.simcore.SimManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    SimManager simManager;
    @FXML Label logLabel;
    @FXML GridPane gridPane;
    @FXML Button prevButton;
    @FXML Button nextButton;

    int currentGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addMouseScrolling(gridPane);

        try {
            SimStateMemorizer simStateMemorizer = new SimStateMemorizer();

            simManager = new SimManager();
            PetrolStation petrolStation = new PetrolStation(3, 2, 6, simManager, simStateMemorizer);
            new CarArrival(petrolStation, petrolStation.simGenerator.exponential(petrolStation.lambda));
            simManager.startSimulation();

            displayStatistics(petrolStation);

            displayPlots(petrolStation);

            GridPane tempGrid = new GridPane();
            tempGrid.getChildren().addAll(simStateMemorizer.gridPanes.get(0));
            gridPane.add(tempGrid, 0, 1);
            logLabel.setText(simStateMemorizer.logList.get(0));

            handleButtons(simStateMemorizer);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void addMouseScrolling(Node node) {
        node.setOnScroll((ScrollEvent event) -> {
            // Adjust the zoom factor as per your requirement
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0){
                zoomFactor = 2.0 - zoomFactor;
            }
            node.setScaleX(node.getScaleX() * zoomFactor);
            node.setScaleY(node.getScaleY() * zoomFactor);
            node.setLayoutY(65);
        });
    }

    private void displayStatistics(PetrolStation petrolStation){
        System.out.println("Benzyne tankowalo samochodow: " + petrolStation.surrounding.petrolFuelingCount);
        System.out.println("LPG tankowalo samochodow: " + petrolStation.surrounding.LPGFuelingCount);
        System.out.println("ON tankowalo samochodow: " + petrolStation.surrounding.ONFuelingCount);
        System.out.println("Samochody mylo: " + petrolStation.surrounding.washedCarsCount);
        System.out.println("Strata na paliwie wyniosla: " + petrolStation.loss.fuelLoss);
        System.out.println("Strata na myjni wyniosla: " + petrolStation.loss.carWashLoss);
        System.out.println("Srednia ilosc klientow w kolejce po benzyne: " + Statistics.arithmeticMean(petrolStation.carsInPetrolQueueCount));
        System.out.println("Srednia ilosc klientow w kolejce po LPG: " + Statistics.arithmeticMean(petrolStation.carsInLPGQueueCount));
        System.out.println("Srednia ilosc klientow w kolejce po ON: " + Statistics.arithmeticMean(petrolStation.carsInONQueueCount));
        System.out.println("Srednia ilosc klientow w kolejce do myjni: " + Statistics.arithmeticMean(petrolStation.carsInCarWashQueueCount));
        System.out.println("Sredni czas potrzebny na tankowanie: " + Statistics.arithmeticMean(petrolStation.fuelingTime));
        System.out.println("Sredni czas potrzebny na mycie: " + Statistics.arithmeticMean(petrolStation.carWashingTime));
        System.out.println("Graniczne prawdopodobieństwo rezygnacji klienta z obslugi przy tankowaniu: " + (double) petrolStation.loss.fuelLoss / (double) petrolStation.surrounding.limit);
        System.out.println("Graniczne prawdopodobieństwo rezygnacji klienta z obslugi na myjni: " + (double) petrolStation.loss.carWashLoss / (double) petrolStation.surrounding.limit);
    }

    private void displayPlots(PetrolStation petrolStation){
        Diagram carsInPetrolQueueBarChart = new Diagram(Diagram.DiagramType.TIME_FUNCTION, "Liczba klientow w kolejce po benzyne");
        Diagram carsInLPGQueueBarChart = new Diagram(Diagram.DiagramType.TIME_FUNCTION, "Liczba klientow w kolejce po LPG");
        Diagram carsInONQueueBarChart = new Diagram(Diagram.DiagramType.TIME_FUNCTION, "Liczba klientow w kolejce po ON");
        Diagram carsInCarWashQueueBarChart = new Diagram(Diagram.DiagramType.TIME_FUNCTION, "Liczba klientow w kolejce do myjni");
        Diagram fuelingTimePlot = new Diagram(Diagram.DiagramType.HISTOGRAM, "Czas tankowania");
        Diagram carWashingTimePlot = new Diagram(Diagram.DiagramType.HISTOGRAM, "Czas mycia");
        carsInPetrolQueueBarChart.add(petrolStation.carsInPetrolQueueCount, Color.RED);
        carsInLPGQueueBarChart.add(petrolStation.carsInLPGQueueCount, Color.BLUE);
        carsInONQueueBarChart.add(petrolStation.carsInONQueueCount, Color.GREEN);
        carsInCarWashQueueBarChart.add(petrolStation.carsInCarWashQueueCount, Color.BLACK);
        fuelingTimePlot.add(petrolStation.fuelingTime, Color.CYAN);
        carWashingTimePlot.add(petrolStation.carWashingTime, Color.MAGENTA);
        carsInPetrolQueueBarChart.show();
        carsInLPGQueueBarChart.show();
        carsInONQueueBarChart.show();
        carsInCarWashQueueBarChart.show();
        fuelingTimePlot.show();
        carWashingTimePlot.show();
    }

    private void handleButtons(SimStateMemorizer simStateMemorizer){
        currentGrid = 0;
        prevButton.setOnAction((mouseEvent -> {
            if(currentGrid > 0){
                currentGrid--;
                changeGrid(simStateMemorizer);
            }
        }));
        nextButton.setOnAction((mouseEvent -> {
            if(currentGrid < simStateMemorizer.gridPanes.size() - 1){
                currentGrid++;
                changeGrid(simStateMemorizer);
            }
        }));
    }

    private void changeGrid(SimStateMemorizer simStateMemorizer){
        GridPane g = (GridPane) gridPane.getChildren().get(0);
        g.getChildren().clear();
        g.getChildren().addAll(simStateMemorizer.gridPanes.get(currentGrid));
        logLabel.setText(simStateMemorizer.logList.get(currentGrid));
    }
}
