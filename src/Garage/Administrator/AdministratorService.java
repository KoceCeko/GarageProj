package Garage.Administrator;

import Garage.Login.LoginService;
import Garage.Vehicle.Vehicle;

import java.io.*;
import java.util.HashSet;

public class AdministratorService extends Thread {

    public static void writeGarage(HashSet<Vehicle> vehicles){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LoginService.GARAGE_PATH));
            oos.writeObject(vehicles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashSet<Vehicle> getGarage() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LoginService.GARAGE_PATH));
            return (HashSet<Vehicle>) ois.readObject();
        } catch (IOException e) {
            System.out.println("unable to open garage.ser");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("wrong cast in reading garage.ser");
        }
        return null;

    }

    public static void saveAll(HashSet<Vehicle> vehicles) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LoginService.GARAGE_PATH));
            oos.writeObject(vehicles);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
