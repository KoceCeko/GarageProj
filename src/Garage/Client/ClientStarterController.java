package Garage.Client;

import Garage.Administrator.AdministratorService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientStarterController implements Initializable {


    @FXML
    private Button addMoreVehiclesBtn;

    @FXML
    private Button startSimulationBtn;

    @FXML
    private Slider minVehicleNumber;

    @FXML
    private Label curretnNumLbl;

    @FXML
    private Slider numberOfFloorsSlider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBtnListeners();
        String temp = String.valueOf(AdministratorService.getGarage().size());
        curretnNumLbl.setText(temp);
    }

    private void setBtnListeners() {
        startSimulationBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                    Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("STARTING SIMULATION");
                    alert.setHeaderText("adding more vehicles is currently disabled");
                    alert.showAndWait();
                try {
                    SimulationController simulationController= new SimulationController(minVehicleNumber.valueProperty().intValue(),numberOfFloorsSlider.valueProperty().intValue());
                    FXMLLoader loader= new FXMLLoader();
                    loader.setController(simulationController);
                    loader.setLocation(this.getClass().getResource("Simulation.fxml"));
                    Scene scene= new Scene(loader.load());
                    ((Stage) startSimulationBtn.getScene().getWindow()).setScene(scene);
                } catch (IOException e) {
                    System.out.println("maybe here?");
                    e.printStackTrace();
                }
            }
        });
    }
}
