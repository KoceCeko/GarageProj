package Garage.Client;

import Garage.Utils.Triplet;
import Garage.Vehicle.Vehicle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class SimulationController implements Initializable {

    @FXML
    private Button addVehicleToSim;

    @FXML
    private TextArea simArea;

    @FXML
    private Button restartSimBtn;

    @FXML
    private ComboBox<String> floorComboBox;

    @FXML
    private Button startSimBtn;


    Rules rules;

    private Integer minNumber;

    private Integer numberOfPlatforms;

    public SimulationController(Integer minNumber, Integer numberOfPlatforms) {
        this.minNumber = minNumber;
        this.numberOfPlatforms = numberOfPlatforms;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setUpComboBox();

        if (minNumber>numberOfPlatforms*28+2)
            minNumber = numberOfPlatforms*28+2;

        rules = new Rules(numberOfPlatforms, simArea, minNumber);

        setBtnListeners();

        rules.start();


    }

    private synchronized void setUpComboBox() {
        ObservableList<String> list = floorComboBox.getItems();
        for (int i = 1; i <= numberOfPlatforms; i++)
            list.add(String.valueOf(i));
        floorComboBox.setItems(list);
        floorComboBox.setOnAction(event -> {
            synchronized (rules) {
                rules.setSelectedPlatform(floorComboBox.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void setBtnListeners() {
        restartSimBtn.setOnAction(event -> {
            rules.vehicleSimulations.stream().forEach(e -> {
                if (e.isAlive()) {
                    System.out.println("thread " + e.getId() + "is alive?!?");
                }
            });
        } /*(rules = new Rules(numberOfPlatforms, simArea, minNumber)).start()*/);
        addVehicleToSim.setOnAction(event -> rules.addNewVehicleSimulation());

        startSimBtn.setOnAction(event -> {
            int i=0;
            final long percentage = Math.round(rules.vehicleSimulations.size()*0.20);
            for(VehicleSimulation v : rules.vehicleSimulations){
                if(i++<=percentage){
                    v.start();
                }
            }
            startSimBtn.setDisable(true);
        });
    }
}
