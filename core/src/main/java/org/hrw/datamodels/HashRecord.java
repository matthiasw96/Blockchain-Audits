package org.hrw.datamodels;

public record HashRecord(
        String timestamp,
        String minuteHash,
        String hourHash
) implements Datastructure {

    @Override
    public String getAttributeNames() {
        return "timestamp," +
                "minuteHash," +
                "hourHash";
    }

    @Override
    public String toString() {
        return timestamp + ",'" +
                minuteHash + "','" +
                hourHash + "'";
    }
}
