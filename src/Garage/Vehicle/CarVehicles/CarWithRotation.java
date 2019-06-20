package Garage.Vehicle.CarVehicles;

import Garage.Vehicle.VehicleRotation;

import java.io.File;


public abstract class CarWithRotation extends Car implements VehicleRotation {

    private boolean rotation;

    public CarWithRotation(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image, Integer numberOfDoors) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image, numberOfDoors);
    }

    @Override
    public void setRotation(boolean rotation) {
        this.rotation=rotation;
    }

    @Override
    public boolean isRotating() {
        return rotation;
    }
}
