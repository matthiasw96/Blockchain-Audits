package org.hrw.datamodels;

/**
 * Represents a complete snapshot of server telemetry data for a given timestamp.
 *
 * <p>A {@code ServerRecord} bundles the measurement values of multiple virtual
 * machines (VM2–VM5) together with the timestamp at which the metrics were
 * collected. Each contained {@link VMRecord} contributes its own set of
 * attributes and values.</p>
 *
 * <p>The record implements {@link Datastructure} so it can be dynamically
 * persisted by the Database Handler.
 * Attribute names and values are flattened into a single row for SQL insertion.</p>
 */
public record ServerRecord(
        String timestamp,
        VMRecord vm2,
        VMRecord vm3,
        VMRecord vm4,
        VMRecord vm5
        ) implements Datastructure  {

    public String getAttributeNames() {
        return "timestamp,"  +
                vm2.getAttributeNames() + "," +
                vm3.getAttributeNames() + "," +
                vm4.getAttributeNames() + "," +
                vm5.getAttributeNames();
    }

    @Override
    public String toString() {
        return timestamp + "," +
                vm2.toString() + "," +
                vm3.toString() + "," +
                vm4.toString() + "," +
                vm5.toString();
    }
}
