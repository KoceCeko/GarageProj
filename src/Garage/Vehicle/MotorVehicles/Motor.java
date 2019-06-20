package Garage.Vehicle.MotorVehicles;

import Garage.Vehicle.Vehicle;

import java.io.File;


public abstract class Motor extends Vehicle {
    public Motor(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image);
    }
}
