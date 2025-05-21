import lombok.extern.slf4j.Slf4j;
import model.person.MaintenanceEmployee;
import model.service.LicensePlateDatabase;
import model.speedCamera.SpeedCamera;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Slf4j
public class Application {
    public static void main(String[] args) {
        //Initialize the components
        //Initialize license plate database 1
        Path ressourcePath = Paths.get("src", "main", "resources");
        LicensePlateDatabase licensePlateDatabase1 = LicensePlateDatabase.builder().build();
        licensePlateDatabase1.loadDataFromCSV(ressourcePath + "\\vehicles.csv", ressourcePath + "\\owners.csv");
        //Initialize license plate database 2
        LicensePlateDatabase licensePlateDatabase2 = LicensePlateDatabase.builder().build();
        licensePlateDatabase2.loadDataFromCSV(ressourcePath + "\\vehicles2.csv", ressourcePath + "\\owners2.csv");
        //Initialize speed camera
        MaintenanceEmployee maintenanceEmployee = MaintenanceEmployee.builder()
                .firstName("Marcel")
                .lastName("Davis")
                .dateOfBirth(LocalDate.of(1982,7,21))
                .gender('M')
                .address("Heerstraße 80 13595 Berlin")
                .email("marcel.baumann@davis.de")
                .phoneNumber(491782634587L)
                .build();
        SpeedCamera speedCamera = maintenanceEmployee.setupSpeedCamera("Lauda", 30, 49.4084f, 8.6910f);
        //Add license plate databases to the speed camera list
        maintenanceEmployee.addLicensePlateDatabaseToSpeedCamera(speedCamera, licensePlateDatabase1);
        maintenanceEmployee.addLicensePlateDatabaseToSpeedCamera(speedCamera,licensePlateDatabase2);

        //Simulate traffic and record vehicle
        speedCamera.recordVehicle("HD-AB-123",25);
        speedCamera.recordVehicle("HD-QR-567", 30);
        speedCamera.recordVehicle("HD-GH-012", 35);
        speedCamera.recordVehicle("HD-KL-678",50);
        speedCamera.recordVehicle("HD-UV-111", 40);
        speedCamera.recordVehicle("HD-AA-444",20);

        //Process all violations
        speedCamera.processViolation(2);
        speedCamera.processAllViolations();
        log.info("Simulation ended");
    }
}
