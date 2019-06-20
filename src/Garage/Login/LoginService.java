package Garage.Login;

import Garage.Administrator.Administrator;
import Garage.Administrator.AdministratorService;
import Garage.User;
import Garage.Vehicle.Vehicle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

public class LoginService {

    public static String USER_PATH = "users.usr";

    public static String GARAGE_PATH = "garage.ser";

    private static ArrayList<User> users = null;

    public static void checkFirstState() {
        File usersPath = new File(USER_PATH);
        File garagePath = new File(GARAGE_PATH);

        if (!usersPath.exists() || !garagePath.exists()) {

            users = new ArrayList<User>();
            users.add(new Administrator("a", "a"));
            HashSet<Vehicle> vehicles= new HashSet<>();


            System.out.println("files created");
            writeUsers();
            AdministratorService.writeGarage(vehicles);

        }
    }

    public static ArrayList<User> getUsers() {
        return users;
    }
    public static void addUser(User user)
    {
        users.add(user);
        writeUsers();
    }

    private static void writeUsers(){
        if (users!=null && !users.isEmpty())
        {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_PATH));
                oos.writeObject(users);
            }catch (IOException ioex){
                System.out.println("unable to write users");
                ioex.printStackTrace();
            }

        }
    }

}
