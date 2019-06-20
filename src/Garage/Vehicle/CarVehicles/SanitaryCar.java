package Garage.Vehicle.CarVehicles;

import java.io.File;

public class SanitaryCar extends CarWithRotation {

    public SanitaryCar(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image, Integer numberOfDoors) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image, numberOfDoors);
    }
}
