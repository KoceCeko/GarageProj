package Garage.Vehicle.VanVehicles;


import java.io.File;

public class SimpleVan extends Van {
    public SimpleVan(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image, Integer weight) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image, weight);
    }
}
