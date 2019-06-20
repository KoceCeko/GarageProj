package Garage.Vehicle.MotorVehicles;


import java.io.File;

public class SimpleMotor extends Motor {

    public SimpleMotor(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image) {
        super(name, chassisSerialNum, engineSerialNum, registrationPlate, image);
    }
}
