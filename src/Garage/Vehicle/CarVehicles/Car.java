package Garage.Vehicle.CarVehicles;

import Garage.Vehicle.Vehicle;

import java.io.File;


public abstract class Car extends Vehicle {

    Integer numberOfDoors;


    public Car(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image, Integer numberOfDoors) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image);
        this.numberOfDoors=numberOfDoors;
    }

    public Integer getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(Integer numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }
}
