package Garage.Vehicle.CarVehicles;


import java.io.File;

public class SimpleCar extends Car {

    public SimpleCar(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image, Integer numberOfDoors) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image, numberOfDoors);
    }
}
