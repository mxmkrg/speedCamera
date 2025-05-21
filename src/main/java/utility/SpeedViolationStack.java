package utility;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class SpeedViolationStack<SpeedViolation> extends MyStack<SpeedViolation> {
    public SpeedViolationStack(int capacity) {
        super(capacity);
    }
}
