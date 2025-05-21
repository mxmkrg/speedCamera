package model.speedCamera;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SpeedMeasurement {
    @Builder.Default
    private final UUID speedMeasurementId = UUID.randomUUID();
    private final String licensePlate;
    private final int speed;
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();
    private UUID cameraID;
}
