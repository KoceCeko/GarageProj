package Garage.Vehicle.VanVehicles;

import java.io.File;

public class FireTruck extends VanWithRotation {
    public FireTruck(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image, Integer weight) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image, weight);
    }
}
