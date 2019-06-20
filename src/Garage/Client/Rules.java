package Garage.Client;

import Garage.Administrator.AdministratorService;
import Garage.Utils.Triplet;
import Garage.Vehicle.CarVehicles.SanitaryCar;
import Garage.Vehicle.MotorVehicles.PoliceMotor;
import Garage.Vehicle.MotorVehicles.SimpleMotor;
import Garage.Vehicle.VanVehicles.FireTruck;
import Garage.Vehicle.Vehicle;
import Garage.Vehicle.VehicleRotation;
import javafx.scene.control.TextArea;
import javafx.util.Pair;
import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

public class Rules extends Thread {

    HashMap<Integer, Platform> floorsAndPlatforms;

    final TextArea textArea;

    Integer selectedPlatform;

    Integer numberOfPlatforms;

    ArrayList<VehicleSimulation> vehicleSimulations;

    ArrayList<Triplet<Integer, Position, Boolean>> setOfAvailibleParkingLots;

    ArrayList<Integer> stoppedPlatforms;

    public static List<VehicleSimulation> syncList = new ArrayList<>();

    Integer collisionPlatform;

    private static Integer incrementRegPlate = 0;

    private Integer minVehicleCount;

    public static Random random = new Random();

    private boolean collision;

    private VehicleSimulation bumped;
    private VehicleSimulation bummer;

    public Rules(Integer numberOfPlatforms, TextArea textArea, Integer minVehicleCount) {
        this.collision =false;
        vehicleSimulations = new ArrayList<>();
        floorsAndPlatforms = new HashMap<>();
        stoppedPlatforms = new ArrayList<>();
        this.minVehicleCount = minVehicleCount;
        selectedPlatform = 1;
        this.textArea = textArea;
        this.numberOfPlatforms = numberOfPlatforms;

        createAllPlatforms();

        initializeAllParkingLots();

        this.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("Uncaught exception");
            }
        });
    }

    private void initializeAllParkingLots() {
        setOfAvailibleParkingLots = new ArrayList<>();
        int key = 1;
        Position position;
        while (key <= numberOfPlatforms) {
            for (int x = 0; x < 8; x++)
                for (int y = 0; y < 10; y++)
                    if ((position = new Position(x, y)).isParkingLot())
                        setOfAvailibleParkingLots.add(new Triplet<>(key, position, true));
            key++;
        }

        setOfAvailibleParkingLots.add(new Triplet<>(numberOfPlatforms, new Position(7, 8), true));
        setOfAvailibleParkingLots.add(new Triplet<>(numberOfPlatforms, new Position(7, 9), true));


    }

    private void createAllPlatforms() {
        for (int i = 1; i <= numberOfPlatforms; i++) {
            floorsAndPlatforms.put(i, new Platform(i, numberOfPlatforms));

        }
    }

    public Pair<Integer,Position> checkForCollision(Pair<Integer,Position> position,Pair<Integer,Position> oldPosition, VehicleSimulation sender) {
        //synchronized (random) {
        try {
            VehicleSimulation frontVehicle;
            synchronized (this) {
                frontVehicle = vehicleSimulations.parallelStream().filter(e -> !e.equals(sender) && (e.currentPosition.getValue().equals(position.getValue()) && e.currentPosition.getKey().equals(position.getKey()))).findFirst().get();
            }
            if (frontVehicle == null)
                    return position;
            synchronized (frontVehicle) {
                if (!position.getValue().equals(new Position(0, 9)))
                    return position;
                int randomVal = sender.ranodom.nextInt(100);
                if (randomVal < 10) {
                    System.out.println(randomVal + "<---- value");
                    System.out.println("A COLLISION ON PLATFORM: " + position.getKey());
                    collide(position,frontVehicle,sender);
                    return position;
                } else {
                    return oldPosition;
                }
        }
        }catch (NoSuchElementException nsex){

        } catch (Exception e) {
            //System.out.println("UNABLE TO WAIT FOR NEXT VEHICLE TO MOVE!!why?! well "+e.getMessage());
        }
        return position;
    }

    public void announce(Pair<Integer, Position> currentPosition, Pair<Integer, Position> oldPosition, char vehicleMark) {

        if (currentPosition.getValue().isExit()) {
            vehicleMark = ' ';
        }

        Platform platform = floorsAndPlatforms.get(currentPosition.getKey());
        Platform oldPlatform = floorsAndPlatforms.get(oldPosition.getKey());

        platform.setPlatformElement(currentPosition.getValue(), vehicleMark);

        if (oldPosition.getValue().equals(currentPosition.getValue()))
            platform.setPlatformElement(currentPosition.getValue(), vehicleMark);
        else if (oldPosition.getValue().isParkingLot())
            oldPlatform.setPlatformElement(oldPosition.getValue(), '*');
        else
            oldPlatform.setPlatformElement(oldPosition.getValue(), ' ');


        if ((currentPosition.getKey() == oldPosition.getKey())) {
            if (currentPosition.getKey() == selectedPlatform) {
                String outputString = platform.getPlatformAsString();
                print(outputString);
            }
        } else if (currentPosition.getKey() == selectedPlatform) {
            String outputString = platform.getPlatformAsString();
            print(outputString);
        } else if (oldPosition.getKey() == selectedPlatform) {
            String outputString = oldPlatform.getPlatformAsString();
            print(outputString);
        }

    }

    private synchronized void print( String outputText){
        if (textArea!=null || outputText!=null)
            textArea.setText(outputText);
    }

    public synchronized void writeCurrentState() {
        Platform platform = floorsAndPlatforms.get(selectedPlatform);
        textArea.setText(platform.getPlatformAsString());
    }

    public void setSelectedPlatform(String selectedItem) {
        try {
            selectedPlatform = Integer.parseInt(selectedItem);
            writeCurrentState();
        } catch (NumberFormatException nfex) {
            System.out.println("UNABLE TO PARSE PLATFORM: " + nfex.getMessage());
            selectedPlatform = 1;
        }
    }


    private void startSimulation() {

        HashSet<Vehicle> vehicles = AdministratorService.getGarage();
        while (vehicles.size() < minVehicleCount) {
            vehicles.add(generateNewVehicle());
        }
        System.out.println("STARTING NUMBER OF VEHICLES: " + vehicles.size());
        for (Vehicle v : vehicles) {
            VehicleSimulation simulation;

            Triplet<Integer,Position,Boolean> triplet = setOfAvailibleParkingLots.get(random.nextInt(setOfAvailibleParkingLots.size()));
            while (!triplet.third)
                triplet = setOfAvailibleParkingLots.get(random.nextInt(setOfAvailibleParkingLots.size()));

            triplet.third=false;

            simulation = new VehicleSimulation(v, this, new Pair<>(triplet.first, new Position(triplet.second)), new Pair<>(1, Position.getExit()));

            vehicleSimulations.add(simulation);
        }

        for (VehicleSimulation sim : vehicleSimulations) {
            sim.setDaemon(true);
        }
    }

    public Vehicle generateNewVehicle() {
        String generatedRegistrationPlate = "A";
        Integer ran = random.nextInt(100);
        if (ran <=15){
            if (incrementRegPlate%3 == 0)
                return new PoliceMotor(generatedRegistrationPlate+"Police",incrementRegPlate,incrementRegPlate,generatedRegistrationPlate+(incrementRegPlate++).toString(),null);
            else if (incrementRegPlate%3 == 1)
                return new FireTruck(generatedRegistrationPlate+"FireFighter",incrementRegPlate,incrementRegPlate,generatedRegistrationPlate+(incrementRegPlate++).toString(),null,incrementRegPlate);
            else
                return new SanitaryCar(generatedRegistrationPlate+"Sanitary",incrementRegPlate,incrementRegPlate,generatedRegistrationPlate+(incrementRegPlate++).toString(),null,incrementRegPlate);
        }

        return new SimpleMotor(generatedRegistrationPlate,incrementRegPlate,incrementRegPlate++, generatedRegistrationPlate +(incrementRegPlate++).toString(),null);
    }

    public void collide(Pair<Integer,Position> position,VehicleSimulation bumped, VehicleSimulation bummer) {

        VehicleSimulation rotation1 = null;
        this.bumped = bumped;
        this.bummer = bummer;
        synchronized (syncList) {
            collision = true;
            collisionPlatform = position.getKey();
            stoppedPlatforms.add(collisionPlatform);
            //syncList.add(vehicleSimulation);

            try {
                rotation1 = vehicleSimulations.stream().filter(vehicle -> (vehicle.vehicle instanceof VehicleRotation) && vehicle.currentPosition.getKey() == position.getKey()).findFirst().get();
            } catch (NoSuchElementException e) {
                System.out.println("No car with rotation around");
            }
        }
        if (rotation1 != null) {
            synchronized (VehicleSimulation.class) {
                System.out.println("ROTATION id: " + rotation1.getId());
                rotation1.vehicleMark = 'R';
                rotation1.destination = position;
                rotation1.setPriority(Thread.MAX_PRIORITY);
                if (!rotation1.isAlive())
                    rotation1.start();
                try {
                    while(rotation1.vehicleMark=='R'){}
                } catch (IllegalMonitorStateException ilme) {
                    System.out.println("EXCEPTION AT WAITING POINT IN RULES.COLLIDE()");
                }
            }
        } else {
            try {
                for (int i = 0; i < 3; i++) {
                    System.out.println("time left: " + (4 - i));
                    sleep(1000);
                }
                System.out.println("FORWARD!!! " );
            } catch (InterruptedException e) {
                System.out.println("exception at sleep 3 sec in rules.colide!!! " + this.getId());
            }
        }
        synchronized (syncList) {
            syncList.notifyAll();
            collision = false;
            stoppedPlatforms.remove(collisionPlatform);
        }
    }

    @Override
    public void run() {
        startSimulation();
    }

    public void setParkingLot(boolean free,Pair<Integer,Position> position) {

        try {
            synchronized (setOfAvailibleParkingLots) {
                setOfAvailibleParkingLots.stream().filter(e -> (e.first == position.getKey() && e.second.equals(position.getValue()))).findFirst().get().third = free;
            }
        } catch (Exception e) {
        }
    }

    public synchronized void addNewVehicleSimulation() {
        Triplet<Integer,Position,Boolean> triplet = setOfAvailibleParkingLots.get(random.nextInt(setOfAvailibleParkingLots.size()));
        if (vehicleSimulations.size()+VehicleSimulation.addedVehicles.size()-VehicleSimulation.goneOut >= setOfAvailibleParkingLots.size())
            return;
        while (!triplet.third){
            triplet = setOfAvailibleParkingLots.get(random.nextInt(setOfAvailibleParkingLots.size()));
        }
        triplet.third=false;
        VehicleSimulation sim = new VehicleSimulation(generateNewVehicle(), this, new Pair<>(1, new Position(0, 8)), new Pair<>(triplet.first, triplet.second));
        vehicleSimulations.add(sim);
        sim.start();
    }

    public boolean collision() {
        return collision;
    }

    public void makeRecord() {
        File recordPath = new File("policeRecord.txt");
        try{

            if (!recordPath.exists())
                recordPath.createNewFile();
            String record = "time: "+ LocalDateTime.now().toString()+"\nbummer: "+bummer.getName()+"\nbumped: "+bummer.getName();
            BufferedWriter writer = new BufferedWriter(new FileWriter(recordPath));
            writer.append(record);
        }catch (IOException ioex){
            System.out.println("exception at police record");
        }catch (Exception ex){
            System.out.println("probably no bummer or bumped vehicle");
        }

    }
}
