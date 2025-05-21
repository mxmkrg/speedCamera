package model.person;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import model.vehicle.Vehicle;
import model.wallet.Wallet;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Owner extends Person {
    //Composition relationship 1:1: The person has only one wallet
    @Builder.Default
    private Wallet wallet = Wallet.builder()
            .balance(0)
            .active(true)
            .build();

    //Aggregation relationship 1:n: The owner can have multiple vehicles
    @Builder.Default
    private List<Vehicle> vehicles = new ArrayList<>();

    //Adds a vehicle to this owner's collection and sets this owner as the vehicle's owner
    public void addVehicle(Vehicle vehicle) {
        if (vehicle == null || vehicles.contains(vehicle)) {
            return;
        }
        vehicles.add(vehicle);
        vehicle.setOwner(this);
    }

    public boolean removeVehicle(Vehicle vehicle) {
        boolean removed = vehicles.remove(vehicle);
        if (removed && this.equals(vehicle.getOwner())) {
            vehicle.setOwner(null);
        }
        return removed;
    }
}
