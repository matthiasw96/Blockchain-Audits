package org.hrw.datamodels;

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
