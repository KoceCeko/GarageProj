package Garage.Vehicle;


import java.io.File;
import java.io.Serializable;
import java.util.Objects;

public abstract class Vehicle implements Serializable {
    private String name;

    private Integer chassisSerialNum;

    private Integer engineSerialNum;

    private String registrationPlate;

    private File image;

    public Vehicle(String name, Integer chassisSerialNum, Integer engineSerialNum, String registrationPlate, File image) {
        this.name = name;
        this.chassisSerialNum = chassisSerialNum;
        this.engineSerialNum = engineSerialNum;
        this.registrationPlate = registrationPlate;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getChassisSerialNum() {
        return chassisSerialNum;
    }

    public void setChassisSerialNum(Integer chassisSerialNum) {
        this.chassisSerialNum = chassisSerialNum;
    }

    public Integer getEngineSerialNum() {
        return engineSerialNum;
    }

    public void setEngineSerialNum(Integer engineSerialNum) {
        this.engineSerialNum = engineSerialNum;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(getRegistrationPlate(), vehicle.getRegistrationPlate());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getRegistrationPlate());
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "name='" + name + '\'' +
                ", registrationPlate=" + registrationPlate +
                '}';
    }
}
