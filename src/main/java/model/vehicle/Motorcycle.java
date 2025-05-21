package model.vehicle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class Motorcycle extends Vehicle {
    @Override
    public String getVehicleType() {
        return "Motorcycle";
    }
}
