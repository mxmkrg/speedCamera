package model.speedCamera;

import lombok.Builder;

import java.util.NavigableMap;
import java.util.TreeMap;

@Builder
public class FineCalculator {
    private final NavigableMap<Double, Integer> fineThresholds = new TreeMap<>();

    public FineCalculator() {
        fineThresholds.put(10.0, 30);
        fineThresholds.put(15.0, 50);
        fineThresholds.put(20.0, 70);
        fineThresholds.put(25.0, 115);
        fineThresholds.put(30.0, 180);
        fineThresholds.put(40.0, 260);
        fineThresholds.put(50.0, 400);
        fineThresholds.put(60.0, 560);
        fineThresholds.put(70.0, 700);
        fineThresholds.put(Double.MAX_VALUE, 800);
    }

    public int calculateFine(double exceededBy) {
        // Get the entry with the smallest key greater than or equal to exceededBy
        return fineThresholds.ceilingEntry(exceededBy).getValue();
    }
}
