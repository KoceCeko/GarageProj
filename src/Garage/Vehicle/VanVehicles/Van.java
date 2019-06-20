package Garage.Vehicle.VanVehicles;

import Garage.Vehicle.Vehicle;

import java.io.File;


public abstract class Van extends Vehicle {

    private Integer weight;

    public Van(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image, Integer weight) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image);
        this.weight=weight;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }


}
