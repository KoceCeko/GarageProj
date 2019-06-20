package Garage.Client;

public class Platform {

    private int floor;

    private Integer numberOfPlatforms;

    private char[][] platform;

    public int getFloor() {
        return floor;
    }


    private String platformAsString;

    public Platform( Integer floor, Integer numberOfPlatforms) {


        this.numberOfPlatforms = numberOfPlatforms;
        this.floor = floor;

        createPlatform();

        updateAsString();
    }


    public Integer getNumberOfPlatforms() {
        return numberOfPlatforms;
    }

    public synchronized boolean hasVechileAtPosition(Position position){
        if (platform[9-position.getY()][position.getX()]!=' ' || platform[9-position.getY()][position.getX()]!='*')
            return true;
        return false;
    }

    public boolean hasFreeParkingLot(){
     if (platformAsString.contains("*"))
         return true;
     else
         return false;
    }

    private synchronized void updateAsString(){
        StringBuilder builder= new StringBuilder();
        for (int i = 0; i < platform.length; i++) {
            for (int j = 0; j < platform[i].length; j++) {
                builder.append(platform[i][j]);
            }
            builder.append("\n");
        }
        platformAsString = builder.toString();
    }

    public char[][] getPlatform() {
        return platform;
    }

    public String getPlatformAsString() {
        return platformAsString;
    }

    public synchronized void setPlatformElement(Position newPosition,Position oldPosition, char value){
        try {
            if (oldPosition.isParkingLot())
                platform[9-oldPosition.getY()][oldPosition.getX()] = '*';
            else
                platform[9-oldPosition.getY()][oldPosition.getX()] = ' ';
        } catch (Exception e) {
            e.printStackTrace();
        }

        platform[9-newPosition.getY()][newPosition.getX()] = value;
        updateAsString();
    }

    public synchronized void setPlatformElement(Position oldPosition, char mark) {
        platform[9-oldPosition.getY()][oldPosition.getX()] = mark;
        updateAsString();
    }

    private void createPlatform() {
        platform= new char[10][8];

        for (int i = 0; i < platform.length; i++) {
            platform[i] = new char[8];
            for (int j = 0; j < platform[i].length; j++) {
                if (i < 2 && j == 0)
                    platform[i][j] = ' ';
                else if (i >= 2 && j == 0)
                    platform[i][j] = '*';
                else if ((i <= 7 && i > 1) && (j == 3 || j == 4))
                    platform[i][j] = '*';
                else if (j == 7 && i >= 2)
                    platform[i][j] = '*';
                else
                    platform[i][j] = ' ';
            }
        }
        if (floor == numberOfPlatforms)
            platform[0][7]=platform[1][7]='*';
    }


    public Position getFreeParkingLot() {
        for (int i=0;i<platform.length; i++){
            for (int j=0;j<platform[i].length;j++){
                if (platform[i][j]=='*')
                    return new Position(j,9-i);
            }
        }
        return null;
    }
}
