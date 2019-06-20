package Garage.Vehicle.MotorVehicles;

import Garage.Vehicle.VehicleRotation;

import java.io.File;

public abstract class MotorWithRotation  extends Motor implements VehicleRotation {

    private boolean rotation;

    public MotorWithRotation(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image);
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
