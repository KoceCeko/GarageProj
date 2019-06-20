package Garage.Vehicle.CarVehicles;

import java.io.File;

public class PoliceCar extends CarWithRotation {


    public PoliceCar(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image, Integer numberOfDoors) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image, numberOfDoors);
    }
}
