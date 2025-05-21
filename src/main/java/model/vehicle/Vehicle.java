package model.vehicle;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import model.person.Owner;

import java.time.LocalDate;

@Data
@SuperBuilder
public abstract class Vehicle {
    private String licensePlate;
    private final String maker;
    private final String model;
    private final short yearOfManufacture;
    private final LocalDate registrationDate;
    private int enginePower;
    private final FuelType fuelType;
    private final byte numberOfSeats;

    //1:1 relationship
    private Owner owner;

    public abstract String getVehicleType();
}
