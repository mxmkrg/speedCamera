package model.speedCamera;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import model.person.Owner;
import model.service.LicensePlateDatabase;
import utility.SpeedViolationStack;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@Slf4j
public class SpeedCamera {
    @Builder.Default
    private final UUID speedCameraID = UUID.randomUUID();
    private int speedLimit;
    private String location;
    private float gpsLatitude;
    private float gpsLongitude;
    @Builder.Default
    private LocalDateTime installationDate = LocalDateTime.now();
    @Builder.Default
    private boolean active = false;

    @Builder.Default
    //Aggregation relationship 1:1: One speed camera has one fine calculator
    private final FineCalculator fineCalculator = FineCalculator.builder().build();

    //Aggregation relationship 1:n: One speed camera can have multiple measurements
    @Builder.Default
    private final List<SpeedMeasurement> speedMeasurements = new ArrayList<>();

    //Aggregation relationship 1:n: One speed camera can have multiple violations
    @Builder.Default
    private final Queue<SpeedViolation> speedViolations = new PriorityQueue<>(Comparator.comparing(SpeedViolation::getTimestamp));

    //Aggregation relationship n:m: One speed camera can you use multiple license plate databases, and a license plate database can be used by multiple speed cameras
    @Builder.Default
    private final List<LicensePlateDatabase> licensePlateDatabaseList = new ArrayList<>();

    //Aggregation relationship 1:n: One speed camera can have multiple processed speed violations in the history (max. 100)
    @Builder.Default
    private final SpeedViolationStack<SpeedViolation> speedViolationHistoryStack = new SpeedViolationStack<>(100);

    public void recordVehicle(String licensePlate, int speed) {
        if (!this.active) {
            return;
        }
        LocalDateTime timestamp = LocalDateTime.now();
        SpeedMeasurement measurement = SpeedMeasurement.builder()
                .licensePlate(licensePlate)
                .speed(speed)
                .timestamp(timestamp)
                .cameraID(this.getSpeedCameraID())
                .build();
        speedMeasurements.add(measurement);

        if (speed > speedLimit) {
            SpeedViolation speedViolation = SpeedViolation.builder()
                        .licensePlate(licensePlate)
                        .recordedSpeed(speed)
                        .speedLimit(speedLimit)
                        .timestamp(timestamp)
                        .fine(fineCalculator.calculateFine(speed - speedLimit))
                        .cameraID(this.getSpeedCameraID())
                        .speedViolationStatus(SpeedViolationStatus.UNPROCESSED)
                        .build();
            speedViolations.add(speedViolation);
        }
        log.info("Vehicle with the license plate \"{}\" got recorded ", licensePlate);
    }

    public void processAllViolations() {
        if (speedViolations.isEmpty()) {
            log.info("There are no violations to process.");
            return;
        }
        while (!speedViolations.isEmpty()) {
            processViolation();
        }
    }

    //Overloaded 1: Process only one violation
    public void processViolation() {
        if (speedViolations.isEmpty()) {
            log.info("There are no violations to process.");
            return;
        }

        SpeedViolation currentSpeedViolation = speedViolations.poll();

        Owner owner = null;
        for (LicensePlateDatabase licensePlateDatabase : licensePlateDatabaseList) {
            owner = licensePlateDatabase.lookupOwner(currentSpeedViolation.getLicensePlate());
            if (owner != null) {
                break;
            }
        }
        if (owner == null) {
            log.warn("Cannot process violation: no owner found for license plate {}", currentSpeedViolation.getLicensePlate());
            currentSpeedViolation.setSpeedViolationStatus(SpeedViolationStatus.ERROR);
            speedViolationHistoryStack.push(currentSpeedViolation);
            return;
        }
        if (!owner.getWallet().withdraw(currentSpeedViolation.getFine())) {
            log.warn("Failed to process violation for license plate {}: insufficient funds", currentSpeedViolation.getLicensePlate());
            currentSpeedViolation.setSpeedViolationStatus(SpeedViolationStatus.ERROR);
            speedViolationHistoryStack.push(currentSpeedViolation);
            return;
        }
        log.info("Successfully processed violation for license plate {}: {} euros", currentSpeedViolation.getLicensePlate(), currentSpeedViolation.getFine());
        currentSpeedViolation.setSpeedViolationStatus(SpeedViolationStatus.PROCESSED);
        speedViolationHistoryStack.push(currentSpeedViolation);


    }
    //Overloaded 2: Processes multiple number of violations based on the given number
    public void processViolation(Integer numberOfViolationsToProcess) {
        for(int i = 0; i < numberOfViolationsToProcess; i++) {
            processViolation();
        }
    }
}

