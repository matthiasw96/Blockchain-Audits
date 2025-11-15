package org.hrw.datamodels;

public record HashRecord(
        String timestamp,
        String secondHash,
        String minuteHash,
        String rootHash
) implements Datastructure {

    @Override
    public String getAttributeNames() {
        return "timestamp," +
                "secondHash," +
                "minuteHash," +
                "rootHash";
    }

    @Override
    public String toString() {
        return timestamp + ",'" +
                secondHash + "','" +
                minuteHash + "','" +
                rootHash + "'";
    }
}
