package model.vehicle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class Car extends Vehicle {
    @Override
    public String getVehicleType() {
        return "Car";
    }
}
