package Garage.Vehicle.MotorVehicles;

import java.io.File;


public class PoliceMotor extends MotorWithRotation {
    public PoliceMotor(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image);
    }
}
