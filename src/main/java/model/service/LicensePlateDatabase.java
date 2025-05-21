package model.service;

import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import model.person.Owner;
import model.vehicle.Car;
import model.vehicle.FuelType;
import model.vehicle.Motorcycle;
import model.vehicle.Vehicle;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Builder
public class LicensePlateDatabase {
    @Builder.Default
    private final UUID licensePlateDatabaseID = UUID.randomUUID();

    @Builder.Default
    private final Map<String, Vehicle> licensePlateToVehicle = new HashMap<>();

    public void registerVehicle(Vehicle vehicle) {
        if (licensePlateToVehicle.containsKey(vehicle.getLicensePlate())) {
            log.warn("License plate already registered: {}", vehicle.getLicensePlate());
            return;
        }

        licensePlateToVehicle.put(vehicle.getLicensePlate(), vehicle);
        log.info("Registered vehicle with license plate: {}", vehicle.getLicensePlate());
    }

    public void removeVehicle(String licensePlate) {
        Vehicle removed = licensePlateToVehicle.remove(licensePlate);
        if (removed == null) {
            log.warn("No vehicle found with license plate: {}", licensePlate);
        }
        log.info("Removed vehicle with license plate: {}", licensePlate);
    }

    public Vehicle lookupVehicle(String licensePlate) {
        Vehicle vehicle = licensePlateToVehicle.get(licensePlate);
        if (vehicle == null) {
            log.warn("No vehicle found with license plate: {}", licensePlate);
            return null;
        }
        log.debug("Found vehicle with license plate: {}", licensePlate);
        return vehicle;
    }

    public Owner lookupOwner(String licensePlate) {
        Vehicle vehicle = lookupVehicle(licensePlate);
        if (vehicle == null) {
            return null;
        }
        Owner owner = vehicle.getOwner();
        if (owner == null) {
            log.warn("Vehicle with license plate {} has no registered owner", licensePlate);
            return null;
        }
        log.debug("Found owner for license plate {}: {}", licensePlate, owner.getLastName());
        return owner;
    }

    @SneakyThrows
    public void loadDataFromCSV(String vehicleCSVPath, String ownerCSVPath) {
        List<Owner> ownerList = new ArrayList<>();

        //Load vehicles.csv into license plates map
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(vehicleCSVPath))) {
            bufferedReader.lines().skip(1).forEach(line -> {
                String[] dataRow = line.split(",");

                if (dataRow.length < 9) {
                    log.warn("Skipping invalid data row: insufficient columns");
                    return;
                }

                Vehicle vehicle = createVehicle(
                                    dataRow[1],
                                    dataRow[0],
                                    dataRow[2],
                                    dataRow[3],
                                    Short.parseShort(dataRow[4]),
                                    LocalDate.parse(dataRow[5]),
                                    Byte.parseByte(dataRow[6]),
                                    Integer.parseInt(dataRow[7]),
                                    FuelType.valueOf(dataRow[8]));
                if (vehicle != null) {
                    registerVehicle(vehicle);
                    log.debug("Added vehicle with license plate: {}", dataRow[0]);
                }
            });
        }

        //Load owners.csv into an owner list
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(ownerCSVPath))) {
            bufferedReader.lines().skip(1).forEach(line -> {
                String[] dataRow = line.split(",");

                if (dataRow.length < 8) {
                    log.warn("Skipping invalid data row: insufficient columns");
                    return;
                }
                Owner owner = createOwner(
                        dataRow[0],
                        dataRow[1],
                        LocalDate.parse(dataRow[2]),
                        dataRow[3],
                        Long.parseLong(dataRow[4]),
                        dataRow[5],
                        dataRow[6].charAt(0),
                        Double.parseDouble(dataRow[7]));
                ownerList.add(owner);
            });
        }

        //Set every vehicle to one owner
        int personIndex = 0;
        for (Vehicle vehicle : licensePlateToVehicle.values()) {
            if (personIndex < ownerList.size()) {
                ownerList.get(personIndex).addVehicle(vehicle);
                personIndex++;
            }
        }
    }

    private Vehicle createVehicle(String carType,
                                  String licensePlate,
                                  String maker,
                                  String model,
                                  Short yearOfManufacture,
                                  LocalDate registrationDate,
                                  Byte numberOfSeats,
                                  Integer enginePower,
                                  FuelType fuelType) {
        Vehicle vehicle;
        if (carType.equals("car")) {
            vehicle = Car.builder()
                    .licensePlate(licensePlate)
                    .maker(maker)
                    .model(model)
                    .yearOfManufacture(yearOfManufacture)
                    .registrationDate(registrationDate)
                    .numberOfSeats(numberOfSeats)
                    .enginePower(enginePower)
                    .fuelType(fuelType)
                    .build();
        } else if (carType.equals("motorcycle")) {
            vehicle = Motorcycle.builder()
                    .licensePlate(licensePlate)
                    .maker(maker)
                    .model(model)
                    .yearOfManufacture(yearOfManufacture)
                    .registrationDate(registrationDate)
                    .numberOfSeats(numberOfSeats)
                    .enginePower(enginePower)
                    .fuelType(fuelType)
                    .build();
        } else {
            return null;
        }
        return vehicle;
    }

    private Owner createOwner(String firstName,
                              String lastname,
                              LocalDate dateOfBirth,
                              String address,
                              Long phoneNumber,
                              String email,
                              Character gender,
                              Double walletBalance) {
        Owner owner = Owner.builder()
                        .firstName(firstName)
                        .lastName(lastname)
                        .dateOfBirth(dateOfBirth)
                        .address(address)
                        .phoneNumber(phoneNumber)
                        .email(email)
                        .gender(gender)
                        .build();
        owner.getWallet().deposit(walletBalance);
        return owner;
    }
}
