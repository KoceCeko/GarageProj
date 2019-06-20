package Garage.Administrator;

import Garage.Administrator.AddVehicle.AddVehicleController;
import Garage.Client.Client;
import Garage.Vehicle.CarVehicles.Car;
import Garage.Vehicle.MotorVehicles.Motor;
import Garage.Vehicle.VanVehicles.Van;
import Garage.Vehicle.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class AdministratorController implements Initializable {

    @FXML
    private ComboBox<String> vehicleTypeBox;

    @FXML
    private TableView<Vehicle> vehicleTable;

    @FXML
    private Button addVehicleBtn;

    @FXML
    private ComboBox<?> floorComboBox;

    @FXML
    private Button startClientBtn;


    @FXML
    private TableColumn<Vehicle, String> typeColumn;

    @FXML
    private TableColumn<?, ?> brandColumn;

    @FXML
    private TableColumn<Vehicle, File> photoColumn;

    @FXML
    private TableColumn<Vehicle,Integer> plateColumn;

    @FXML
    private TableColumn<Car, Integer> dorsColumn;

    @FXML
    private TableColumn<Van, Integer> weightColumn;

    AdministratorService service;


    private HashSet<Vehicle> vehicles;

    private ContextMenu menu;
    private MenuItem removeItem;
    private MenuItem editItem;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        vehicles= AdministratorService.getGarage();

        setUpTable();

        updateTable();

        setBtnListeners();

        setSelectListener();
    }

    private void setSelectListener() {
        vehicleTypeBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (vehicleTypeBox.getSelectionModel().getSelectedItem()!=null)
                    updateTable();
            }
        });
        vehicleTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });
    }

    private void setUpTable(){


        menu = new ContextMenu();
        removeItem= new MenuItem("remove");
        editItem = new MenuItem("edit");
        menu.getItems().add(removeItem);
        menu.getItems().add(editItem);

        ObservableList<Vehicle> observableList= FXCollections.observableArrayList();
        try {
            observableList.addAll(vehicles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        vehicleTable.setItems(observableList);
        typeColumn.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("name"));
        plateColumn.setCellValueFactory(new PropertyValueFactory<Vehicle,Integer>("registrationPlate"));
        photoColumn.setCellValueFactory(new PropertyValueFactory<Vehicle,File>("image"));
        dorsColumn.setCellValueFactory(new PropertyValueFactory<Car,Integer>("numberOfDoors"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<Van,Integer>("weight"));
        brandColumn.setVisible(false);
        dorsColumn.setVisible(false);
        weightColumn.setVisible(false);

        vehicleTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getButton() == MouseButton.SECONDARY){
                    menu.show(vehicleTable, event.getScreenX(),event.getScreenY());
                }
            }
        });
    }

    private void setBtnListeners(){

        AdministratorController thisController=this;
        addVehicleBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("add new Items");

                try {
                    FXMLLoader loader = new FXMLLoader(AddVehicleController.class.getResource("AddVehicle.fxml"));
                    Scene oldScene = addVehicleBtn.getScene();
                    Parent parent = loader.load();
                    ((AddVehicleController) loader.getController()).setOldScene(oldScene, thisController);
                    Scene scene = new Scene(parent);
                    ((Stage) addVehicleBtn.getScene().getWindow()).setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // vehicles.add(new SimpleCar("asd",1,1,2,null,3));
                // updateTable();
            }

        });

        removeItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Vehicle temp=vehicleTable.getSelectionModel().getSelectedItem();
                    vehicles.remove(temp);
                    vehicleTable.getItems().remove(temp);
                    updateTable();
                    AdministratorService.saveAll(vehicles);
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("failed to remove");
                }
            }
        });

        editItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                    Vehicle temp=vehicleTable.getSelectionModel().getSelectedItem();
                    try {

                        FXMLLoader loader = new FXMLLoader(AddVehicleController.class.getResource("AddVehicle.fxml"));
                        Scene oldScene = addVehicleBtn.getScene();
                        Parent parent = loader.load();
                        ((AddVehicleController) loader.getController()).setOldScene(oldScene, thisController);
                        ((AddVehicleController) loader.getController()).setUpdateValues(temp);
                        Scene scene = new Scene(parent);
                        ((Stage) addVehicleBtn.getScene().getWindow()).setScene(scene);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Vehicle temp=vehicleTable.getSelectionModel().getSelectedItem();
                    vehicles.remove(temp);
                    vehicleTable.getItems().remove(temp);
                    updateTable();
                    AdministratorService.saveAll(vehicles);
                    System.out.println("success");
                }
        });

        startClientBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader loader = new FXMLLoader(Client.class.getResource("ClientStarter.fxml"));
                try {
                    Stage clientStage= new Stage();
                    AdministratorService.saveAll(vehicles);
                    clientStage.setScene(new Scene(loader.load()));
                    clientStage.show();
                    ((Stage)startClientBtn.getScene().getWindow()).close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateVehicle(Vehicle vehicle,Vehicle toRemove){
        vehicles.remove(toRemove);
        vehicles.add(vehicle);
        updateTable();
        AdministratorService.saveAll(vehicles);
    }

    public void addNewVehicle(Vehicle vehicle){
        vehicles.add(vehicle);
        updateTable();
        AdministratorService.saveAll(vehicles);
    }

    private void updateTable() {
        ObservableList<Vehicle> observableList= FXCollections.observableArrayList();
        weightColumn.setVisible(false);
        dorsColumn.setVisible(false);
        String selected=vehicleTypeBox.getSelectionModel().getSelectedItem();
        if (selected==null)
            observableList.addAll(vehicles);
        else {
            switch (selected) {
                case "All": {
                    for (Vehicle v : vehicles) {
                        observableList.add(v);
                        weightColumn.setVisible(false);
                        dorsColumn.setVisible(false);
                    }
                    break;
                }
                case "Cars": {
                    for (Vehicle v : vehicles) {
                        if (v instanceof Car) {
                            observableList.add( v);
                            dorsColumn.setVisible(true);
                            weightColumn.setVisible(false);
                        }
                    }
                    break;
                }
                case "Vans": {
                    for (Vehicle v : vehicles) {
                        if (v instanceof Van) {
                            observableList.add(v);
                            weightColumn.setVisible(true);
                            dorsColumn.setVisible(false);
                        }
                    }
                    break;
                }
                case "Motors": {
                    for (Vehicle v : vehicles) {
                        if (v instanceof Motor) {
                            observableList.add(v);
                            weightColumn.setVisible(false);
                            dorsColumn.setVisible(false);
                        }
                    }
                    break;
                }
            }
        }
        vehicleTable.setItems(observableList);
        vehicleTable.refresh();
    }
}
