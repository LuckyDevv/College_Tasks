package Cars;

public class Car {
    String model;
    int maxSpeed;
    int wheels;
    double engineVolume;
    String color;
    int yearOfIssue;
    String ownerFirstName;
    String ownerLastName;
    long price;
    boolean isNew;
    int placesInTheSalon;
    String salonMaterial;
    boolean insurance;
    String manufacturerCountry;
    int trunkVolume;
    int accelerationTo100km;

    public Car(String model, int maxSpeed) {
        this.model = model;
        this.maxSpeed = maxSpeed;
    }
}
