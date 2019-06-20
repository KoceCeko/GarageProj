package Garage.Client;

import java.util.Objects;

public class Position {
    private Integer x;

    private Integer y;

    public Position(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position position) {
        this.x = position.x;
        this.y = position.y;
    }

    public static Position getExit() {
        return new Position(0,9);
    }

    public static Position getPositionToNextPlatform() {
        return new Position(7,8);
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {

        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "Position{ " +
                "x= " + x +
                ", y= " + y +
                '}';
    }

    public void setPosition(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void setPostition(Position postition) {
        this.x = postition.x;
        this.y = postition.y;
    }

    public boolean isParkingLot() {
        if (x == 0 && y < 8)
            return true;
        else if (x == 7 && y < 8)
            return true;
        else if ((x == 3 || x == 4) && (y > 1 && y < 8))
            return true;
        return false;
    }

    public void moveOutOfParkingLot() {
        if (x == 0 || x == 4)
            x++;
        else
            x--;
    }

    public boolean isOnOuterPath() {
        if (x == 1 && y < 8)
            return true;
        if ((x != 0 && x != 7) && y == 0)
            return true;
        if (x == 6 && y < 8)
            return true;
        return false;
    }

    public void moveByOuter() {
        if (x == 1 && (y < 8 && y != 0))
            y--;
        else if (x == 1 && y == 0)
            x++;
        else if ((x > 0 && x < 6) && y == 0)
            x++;
        else if (x == 6 && y < 8)
            y++;
    }

    public boolean isInnerPath() {
        if (x == 2 && (y < 8 && y != 0))
            return true;
        else if ((x > 1 && x < 6) && y == 1)
            return true;
        else if (x == 5 && (y < 8 && y != 0))
            return true;
        return false;
    }

    public void moveByInner() {
        if (x == 2 && (y < 8 && y != 0))
            y++;
        if ((x > 1 && x < 6) && y == 1)
            x--;
        if (x == 5 && (y < 8 && y != 0))
            y--;
    }

    public boolean isEnteringRoad() {
        if (y == 8)
            return true;
        return false;
    }

    public boolean isExitingRoad() {
        if (y == 9)
            return true;
        else
            return false;
    }

    public void moveToExitingRoad() {
        if (y == 8)
            y++;
    }

    public void moveToExit() {
        if (y == 9 && x > 1)
            x--;
    }

    public boolean isNextToPosition(Position goingTo) {

        if (y == goingTo.y)
            if (Math.abs(x-goingTo.x)==1)
                return true;
//        if (goingTo.isParkingLot()) {
//
//        }
//        if (x == goingTo.x-1 || x == goingTo.x+1)
//            return true;
        return false;
    }

    public void moveToPosition(Position goingTo) {
        x= goingTo.x;
        y= goingTo.y;
    }

    public boolean closeToPosition(Position goingTo) {
        if (y == goingTo.y)
            if (Math.abs(x - goingTo.x) == 2)
                return true;
        return false;
    }

    public void moveCloserToPosition(Position goingTo) {
        if (x - goingTo.x > 1)
            x--;
        else
            x++;
    }

    public boolean isExit() {
        if (x == 0 && y == 9)
            return true;
        return false;
    }

    public void moveTowardParkingLot() {
        if (x == 1 && (y == 8 || y==9))
            y--;
        else if (x==2 && y==9)
            x--;
        else if ((x>=2 && x<=4)&& y==8)
            x++;
        else if (((x>=2 && x<=4)) && y==9)
            x--;
        else if (x==0)
            x++;
        else if (x==5 && y>=8)
            y--;
        else if (x==6 && y==9)
            x--;
    }

    public void moveTowardNextFloor() {
        if (y==8)
            x++;
    }

    public void setPosition(Position oldPosition) {
        this.x= oldPosition.x;
        this.y= oldPosition.y;
    }

    public boolean isEntrance() {
        if (x==0 && y==8)
            return true;
        return false;
    }

    public boolean isNextFloor() {
        if (x==7 && y==8)
            return true;
        return false;
    }

    public void moveToEnteringRoad() {
        if (y==9)
            y--;
    }

    public boolean leftFromThis(Position value) {
        if(value.x>x)
            return true;
        else
            return false;
    }
}
