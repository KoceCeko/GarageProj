package Garage.Vehicle.VanVehicles;

import Garage.Vehicle.VehicleRotation;

import java.io.File;


public abstract class VanWithRotation extends Van implements VehicleRotation {

    private boolean rotation;

    public VanWithRotation(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image, Integer weight) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image, weight);
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
