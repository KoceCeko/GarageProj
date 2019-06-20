package Garage.Administrator.AddVehicle;

import Garage.Administrator.AdministratorController;
import Garage.Vehicle.CarVehicles.Car;
import Garage.Vehicle.CarVehicles.PoliceCar;
import Garage.Vehicle.CarVehicles.SanitaryCar;
import Garage.Vehicle.CarVehicles.SimpleCar;
import Garage.Vehicle.MotorVehicles.PoliceMotor;
import Garage.Vehicle.MotorVehicles.SimpleMotor;
import Garage.Vehicle.VanVehicles.*;
import Garage.Vehicle.Vehicle;
import Garage.Vehicle.VehicleRotation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AddVehicleController implements Initializable {

    @FXML
    private ComboBox<String> vehicleComboBox;

    @FXML
    private CheckBox isPublicCheck;

    @FXML
    private TextField nameText;

    @FXML
    private TextField chassisTxt;

    @FXML
    private TextField engineTxt;

    @FXML
    private Button addImageBtn;

    @FXML
    private Label imgLbl;

    @FXML
    private Label dorlbl;

    @FXML
    private Slider dorSlider;

    @FXML
    private Label weightLbl;

    @FXML
    private Slider weightSlider;

    @FXML
    private ComboBox<String> publicInstitutesBox;

    @FXML
    private Button finishBtn;

    @FXML
    private TextField registrationPlateField;

    private String vehicleType="simple";

    private File photoPath;

    private Scene oldScene;

    private javafx.scene.image.Image selectedImg;

    private AdministratorController administratorController;

    Vehicle passedVehicle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setStartingValues();

        setComboBoxListener();

        setButtonListeners();

        setCheckBoxListener();

        setSliderListener();

        finishBtn.setDisable(false);

    }

    private void setSliderListener() {

    }

    public void setOldScene(Scene scene, AdministratorController administratorController){
        oldScene=scene;
        this.administratorController=administratorController;

    }

    private void setCheckBoxListener() {
        isPublicCheck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isPublicCheck.isSelected()){
                    publicInstitutesBox.setPrefHeight(25);
                    publicInstitutesBox.setVisible(true);
                }else{
                    publicInstitutesBox.setPrefHeight(0);
                    publicInstitutesBox.setVisible(false);
                    vehicleType="simple";
                }
            }
        });

        publicInstitutesBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selected=publicInstitutesBox.getSelectionModel().getSelectedItem();
                if (selected!=null)
                    vehicleType=selected;
            }
        });
    }


    private void setButtonListeners() {
        addImageBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("chose image");
                    photoPath=fileChooser.showOpenDialog(addImageBtn.getScene().getWindow());
                    System.out.println("path: "+photoPath.getPath());
                    if (imgLbl.getHeight()<50){
                        imgLbl.setPrefHeight(153);
                        imgLbl.getScene().getWindow().setHeight(imgLbl.getScene().getWindow().getHeight()+153);
                    }
                     selectedImg = new Image(photoPath.toURI().toString(),270,153,false,true);
                    imgLbl.setGraphic(new ImageView(selectedImg));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        finishBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    Integer engineNum=Integer.parseInt(engineTxt.getText());
                    Integer chassisNum= Integer.parseInt(chassisTxt.getText());
                    if (nameText.getText()!="" && engineNum>0 && chassisNum>0 && registrationPlateField.getText()!=""){
                        Vehicle vehicle=createReturnObject(nameText.getText(),chassisNum,engineNum,registrationPlateField.getText());
                        if (passedVehicle!=null)
                            administratorController.updateVehicle(vehicle,passedVehicle);
                        else
                            administratorController.addNewVehicle(vehicle);
                        ((Stage) finishBtn.getScene().getWindow()).setScene(oldScene);
                    }else
                        throw new NumberFormatException("dummy");
                }catch (NumberFormatException nfe){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    if (vehicleComboBox.getSelectionModel().getSelectedItem()==null)
                        alert.setContentText("select vehicle type");
                    else if(nameText.getText()=="")
                        alert.setContentText("enter vehicle name");
                    else if (nfe.getMessage()!="dummy")
                        alert.setContentText("wrong number input");
                    else if (registrationPlateField.getText()=="")
                        alert.setContentText("enter registration plates");
                    alert.setTitle("Wrong input");
                    alert.setHeaderText("");
                    alert.showAndWait();
                }
            }


        });
    }
    private Vehicle createReturnObject(String name,Integer chassisNum,Integer engineNum,String regPlate) {
        switch (vehicleComboBox.getSelectionModel().getSelectedItem()){

            case "Motor":
                if (isPublicCheck.isSelected()){
                    switch (publicInstitutesBox.getSelectionModel().getSelectedItem()){
                        case "Police":
                            return new PoliceMotor(name,chassisNum,engineNum,regPlate,photoPath);
                        case "Sanitary":
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("motor can't be a sanitary vehicle");
                            break;
                        case "Fire truck":
                            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                            alert2.setContentText("motor can't be a fire fighting vehicle");
                            break;
                    }
                }else
                {
                    System.out.println("tick count: "+dorSlider.getMinorTickCount());
                    return new SimpleMotor(name,chassisNum,engineNum,regPlate,photoPath);
                }
            case "Car":
                if (isPublicCheck.isSelected()){
                    switch (publicInstitutesBox.getSelectionModel().getSelectedItem()){
                        case "Police":
                            return new PoliceCar(name,chassisNum,engineNum,regPlate,photoPath,dorSlider.valueProperty().intValue());
                        case "Sanitary":
                            return new SanitaryCar(name,chassisNum,engineNum,regPlate,photoPath,dorSlider.valueProperty().intValue());
                        case "Fire truck":
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Car can't be a fire fighting vehicle");
                            break;
                    }
                }else
                {
                    return new SimpleCar(name,chassisNum,engineNum,regPlate,photoPath,dorSlider.valueProperty().intValue());
                }
            case "Van":
                int weightValue= weightSlider.valueProperty().intValue();
                if (isPublicCheck.isSelected()){
                    switch (publicInstitutesBox.getSelectionModel().getSelectedItem()){
                        case "Police":
                            return new PoliceVan(name,chassisNum,engineNum,regPlate,photoPath,weightSlider.valueProperty().intValue());
                        case "Sanitary":
                            return new SanitaryVan(name,chassisNum,engineNum,regPlate,photoPath,weightSlider.valueProperty().intValue());
                        case "Fire truck":
                            return new FireTruck(name,chassisNum,engineNum,regPlate,photoPath,weightSlider.valueProperty().intValue());
                    }
                }else
                {
                    System.out.println("tick count: "+weightSlider.getValue());
                    return new SimpleVan(name,chassisNum,engineNum,regPlate,photoPath,weightValue);
                }
        }
        return null;
    }

    private void setComboBoxListener() {
        vehicleComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selected = vehicleComboBox.getSelectionModel().getSelectedItem();
                if (selected!=null)
                {
                    switch (selected){
                        case "Car":
                            setVisibleDorsAdd(true);
                            setVisibleWeightAdd(false);
                            break;
                        case "Van":
                            setVisibleWeightAdd(true);
                            setVisibleDorsAdd(false);
                            break;
                        case "Motor":
                            setVisibleWeightAdd(false);
                            setVisibleDorsAdd(false);
                            break;
                    }
                }
            }
        });

    }

    private void setStartingValues() {
        setVisibleWeightAdd(false);
        setVisibleDorsAdd(false);
        passedVehicle=null;
    }

    private void setVisibleWeightAdd(boolean isVisible) {
        weightSlider.setVisible(isVisible);
        weightLbl.setVisible(isVisible);
    }

    private void setVisibleDorsAdd(boolean isVisible) {
        dorlbl.setVisible(isVisible);
        dorSlider.setVisible(isVisible);
    }

    public void setUpdateValues(Vehicle temp) {
        passedVehicle = temp;
        if (temp instanceof Car){
            vehicleComboBox.getSelectionModel().select("Car");
            Car car = (Car) temp;
            if (car.getNumberOfDoors()!=null)
                dorSlider.setValue(car.getNumberOfDoors());
        }else if (temp instanceof Van){
            Van van = (Van) temp;
            if (van.getWeight()!=null)
                weightSlider.setValue(van.getWeight());
            vehicleComboBox.getSelectionModel().select("Van");
        }else
            vehicleComboBox.getSelectionModel().select("Motor");
        if (temp instanceof VehicleRotation){
            isPublicCheck.setSelected(true);
            if (temp instanceof  PoliceMotor || temp instanceof PoliceCar || temp instanceof PoliceVan)
                publicInstitutesBox.getSelectionModel().select("Police");
            else if (temp instanceof SanitaryCar || temp instanceof SanitaryVan)
                publicInstitutesBox.getSelectionModel().select("Sanitary");
            else
                publicInstitutesBox.getSelectionModel().select("Fire truck");
        }

        nameText.setText(temp.getName());
        chassisTxt.setText(temp.getChassisSerialNum().toString());
        engineTxt.setText(temp.getEngineSerialNum().toString());
        registrationPlateField.setText(temp.getRegistrationPlate());
        if (temp.getImage()!=null){
            photoPath = temp.getImage();
            if (imgLbl.getHeight()<50){
                imgLbl.setPrefHeight(153);
            }
            selectedImg = new Image(photoPath.toURI().toString(),270,153,false,true);
            imgLbl.setGraphic(new ImageView(selectedImg));
        }
    }
}
