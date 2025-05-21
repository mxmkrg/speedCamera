package model.speedCamera;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SpeedViolation {
    @Builder.Default
    private final UUID speedViolationId = UUID.randomUUID();
    private final UUID cameraID;
    private final String licensePlate;
    private final int fine;
    private final int recordedSpeed;
    private final int speedLimit;
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();
    @Builder.Default
    private SpeedViolationStatus speedViolationStatus = SpeedViolationStatus.UNPROCESSED;
}
