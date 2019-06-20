package Garage.Client;

        import Garage.Administrator.AdministratorService;
        import Garage.Vehicle.CarVehicles.PoliceCar;
        import Garage.Vehicle.CarVehicles.SanitaryCar;
        import Garage.Vehicle.MotorVehicles.Motor;
        import Garage.Vehicle.VanVehicles.PoliceVan;
        import Garage.Vehicle.VanVehicles.SanitaryVan;
        import Garage.Vehicle.Vehicle;
        import Garage.Vehicle.VehicleRotation;
        import javafx.util.Pair;

        import java.io.File;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.nio.file.StandardOpenOption;
        import java.util.Date;
        import java.util.HashSet;
        import java.util.Objects;
        import java.util.Random;

public class VehicleSimulation extends Thread {

    static Object monitor = new Object();

    Vehicle vehicle;

    final Rules rules;

    static int finished = 0;

    static int started = 0;

    static int goneOut = 0;

    static HashSet<Vehicle> addedVehicles = new HashSet<>();

    Pair<Integer,Position> currentPosition;

    Pair<Integer,Position> positionBeforeMove;

    Pair<Integer,Position> destination;

    char vehicleMark;

    Random ranodom;

    boolean isParking;

    int bill = 0;
    public static Object lock = new Object();

    public VehicleSimulation(Vehicle vehicle,Rules rules,Pair<Integer,Position> startingPosition, Pair<Integer,Position> destination){
        this.vehicle= vehicle;
        this.rules= rules;
        this.currentPosition =startingPosition;
        this.destination=new Pair<>(destination.getKey(),destination.getValue());
        ranodom= new Random();

        positionBeforeMove=new Pair<>(startingPosition.getKey(),new Position(startingPosition.getValue()));

        if (destination.getValue().isParkingLot()) {
            isParking = true;
            addedVehicles.add(vehicle);
        }
        else
            isParking=false;

        checkVehicleMark();

        announcePosition();

    }

    private void announcePosition() {
        rules.announce(currentPosition,positionBeforeMove,vehicleMark);
    }

    private void checkVehicleMark() {
        if (vehicle instanceof VehicleRotation) {
            VehicleRotation vehicleRotation = (VehicleRotation) vehicle;
            if (vehicleRotation.isRotating())
                vehicleMark = 'R';
            else if (vehicleRotation instanceof PoliceCar)
                vehicleMark = 'P';
            else if (vehicleRotation instanceof Motor)
                vehicleMark = 'P';
            else if (vehicleRotation instanceof PoliceVan)
                vehicleMark = 'P';
            else if (vehicleRotation instanceof SanitaryCar)
                vehicleMark = 'H';
            else if (vehicleRotation instanceof SanitaryVan)
                vehicleMark = 'H';
            else
                vehicleMark = 'F';

        } else
            vehicleMark = 'V';
    }

    @Override
    public void run() {

        runVersTwo();

    }

    private void runVersTwo(){
        Date startingTime = new Date();
        if (!isParking)
            if(vehicleMark!='R')
            startingPause();
        else
            started++;

        moveTowardDestination();

        rules.vehicleSimulations.remove(this);

        announcePosition();

        Date endTime = new Date();

        if (!isParking) {
            if (!(vehicle instanceof VehicleRotation)) {
                long dif = startingTime.getTime() - endTime.getTime();
                if (dif <= 3600)
                    bill = 1;
                else if (3600 <= 10800)
                    bill = 3;
                else
                    bill = 8;
            }
            payout();
        }
        if(!isParking)
            goneOut++;
        finished++;
        if (finished == started){
            saveState();
        }
        System.out.println("FINISH "+getId());
    }

    private void saveState() {
        AdministratorService.saveAll(addedVehicles);
    }

    private synchronized void payout() {
        if (vehicle instanceof VehicleRotation)
            return;
        File log = new File("payouts.csv");
        if (!log.exists()) {
            try {
                log.createNewFile();
                FileWriter writer = new FileWriter(log);
                writer.write("registration plate,payment\n");
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Files.write(Paths.get(log.getPath()),(vehicle.getRegistrationPlate()+','+String.valueOf(bill)+'\n').getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitIfCollision() {
//   rules.vehicleSimulations.stream().filter( e -> rules.stoppedPlatforms.contains(positionBeforeMove.getValue())).forEach(e ->{
        try {
            synchronized (rules.syncList) {
                System.out.println("thread " + this.getId() + " is waiting for notify!");
                rules.syncList.wait();
                System.out.println("thread " + this.getId() + " is under wait()?!");
            }
        } catch (InterruptedException e1) {
            System.out.println("interrupted exception!! at vehicle simulation after wait()");

        } catch (IllegalMonitorStateException mex) {
            System.out.println("i'm thread and i'm in trouble " + this.getId()+" "+mex.getMessage());
        }
        rules.syncList.remove(this);
        //System.out.println("MY MARK: " + vehicleMark);
        //  });
    }

    private void moveTowardDestination() {

        while (true) {
            if ((rules.collision() && vehicleMark != 'R') && (rules.stoppedPlatforms.contains(this.currentPosition.getKey()))) {
                waitIfCollision();
            }
            if (currentPosition.getKey() < destination.getKey()) {
                if (moveTo(new Position(7, 8)))
                    goToUpperPlat();
            }else if (currentPosition.getKey() > destination.getKey()){
                if (moveTo(new Position(0,9)))
                    goToLowerPlat();
                if (currentPosition ==null) {
                    announcePosition();
                    return;
                }
            } else if (currentPosition.getKey() == destination.getKey()) {
                if (moveTo(destination.getValue()))
                    return;
            }

            announcePosition();

            try {

                sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("AT SLEEP 400 moveTowardDestination");
            }
        }
    }

    private void goToLowerPlat() {
        if (currentPosition.getKey()-1 > 0)
            currentPosition = new Pair<>(currentPosition.getKey()-1,new Position(7,9));
        else
            currentPosition =null;
    }

    private void goToUpperPlat() {
        currentPosition =new Pair<>(currentPosition.getKey()+1, new Position(0,8));
    }

    private boolean moveTo(Position target) {

        //remember old position so it can rewrite value
        positionBeforeMove = new Pair<>(currentPosition.getKey(),new Position(currentPosition.getValue()));

        //1st check if its on position
        if (currentPosition.getValue().equals(target) && vehicleMark!='R')
            return true;
        if (currentPosition.getValue().isNextToPosition(target) && vehicleMark!='R') //check if its next to position
            currentPosition.getValue().moveToPosition(target);
        else if (currentPosition.getValue().isNextToPosition(target) && vehicleMark=='R')
        {
            rotationNotify();
        }
        else if (currentPosition.getValue().closeToPosition(target) && vehicleMark!='R') //check if its close (2 fields away)
            currentPosition.getValue().moveCloserToPosition(target);
        else if (currentPosition.getValue().closeToPosition(target) && vehicleMark=='R'){
            rotationNotify();
        }

        else if (currentPosition.getValue().isParkingLot()) {        //check if its a parking lot
            currentPosition.getValue().moveOutOfParkingLot();
            rules.setParkingLot(true,positionBeforeMove);
        }
        else if (currentPosition.getValue().isInnerPath())       //if the vehicle at the inner road, then go its way
            currentPosition.getValue().moveByInner();
        else if (currentPosition.getValue().isOnOuterPath())     //if the is outer, then go outer
            currentPosition.getValue().moveByOuter();
        else if (currentPosition.getValue().isEnteringRoad()){   //if the current position at entering road
            if (target.isNextFloor())                              //check if its next floor
                currentPosition.getValue().moveTowardNextFloor();
            else if (target.isExit())                              //check if its the exit
                currentPosition.getValue().moveToExitingRoad();
            else if (target.isParkingLot())                        //check if target is parking lot
                currentPosition.getValue().moveTowardParkingLot();
            else if (target.isExitingRoad())                       //check the target is at the exiting road... move to exiting road
                currentPosition.getValue().moveToExitingRoad();
            else if (target.isEnteringRoad() && target.leftFromThis(currentPosition.getValue()))                      //check if its entering road... move that way
                currentPosition.getValue().moveToExitingRoad();
            else if (target.isEnteringRoad() && !target.leftFromThis(currentPosition.getValue()))
                currentPosition.getValue().moveTowardNextFloor();
        }else if(currentPosition.getValue().isExitingRoad()){      //if current is exiting road
            if (target.isExit())                                      //check if he's leaving
                currentPosition.getValue().moveToExit();
            else if (target.isEnteringRoad())                         //well if the target is entering road... move to entering road
                currentPosition.getValue().moveToEnteringRoad();
            else if (target.isExitingRoad())                          //if the target is exiting road... move to exiting road
                currentPosition.getValue().moveToExitingRoad();
        }
        currentPosition = checkForCollision(positionBeforeMove);
        return false;
    }

    private void startingPause(){
        try {
            sleep(ranodom.nextInt(100) + 500);
        } catch (InterruptedException e) {
            System.out.println("exception at starting pause");
        }
    }

    private Pair<Integer,Position> checkForCollision(Pair<Integer,Position> oldPosition) {
        if (vehicleMark == 'R')
            return currentPosition;
        else
            return rules.checkForCollision(currentPosition, oldPosition,this);
    }

    private synchronized void rotationNotify() {
        checkVehicleMark();
        destination = new Pair<>(1, new Position(0, 9));
        rules.makeRecord();
        System.out.println("police notify");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VehicleSimulation)) return false;
        VehicleSimulation that = (VehicleSimulation) o;
        return vehicle.equals(that.vehicle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicle, vehicleMark);
    }
}
