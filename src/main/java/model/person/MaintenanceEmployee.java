package model.person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import model.service.LicensePlateDatabase;
import model.speedCamera.SpeedCamera;

import java.time.LocalDateTime;

//TODO: Needs to be implemented

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class MaintenanceEmployee extends Person {
    public SpeedCamera setupSpeedCamera(String location, Integer speedLimit, Float gpsLatitude, Float gpsLongitude) {
        return SpeedCamera.builder()
                    .location(location)
                    .speedLimit(speedLimit)
                    .installationDate(LocalDateTime.now())
                    .gpsLatitude(gpsLatitude)
                    .gpsLongitude(gpsLongitude)
                    .active(true)
                    .build();
    }

    public void addLicensePlateDatabaseToSpeedCamera(SpeedCamera speedCamera, LicensePlateDatabase licensePlateDatabase) {
        speedCamera.setActive(false);
        speedCamera.getLicensePlateDatabaseList().add(licensePlateDatabase);
        speedCamera.setActive(true);
    }
}
