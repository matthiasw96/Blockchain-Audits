package org.hrw.datamodels;

public record HashRecord(
        String timestamp,
        String secondHash,
        String minuteHash,
        String hourHash,
        String debug,
        String debug2
) implements Datastructure {

    @Override
    public String getAttributeNames() {
        return "timestamp," +
                "secondHash," +
                "minuteHash," +
                "hourHash," +
                "debug, " +
                "debug2";
    }

    @Override
    public String toString() {
        return timestamp + ",'" +
                secondHash + "','" +
                minuteHash + "','" +
                hourHash + "','" +
                debug + "','" +
                debug2 + "'";
    }
}
