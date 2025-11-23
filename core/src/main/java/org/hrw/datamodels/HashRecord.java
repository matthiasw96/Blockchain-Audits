package org.hrw.datamodels;

/**
 * Represents a set of hash values produced during the hierarchical hashing process.
 *
 * <p>A {@code HashRecord} contains:</p>
 * <ul>
 *     <li>a {@code timestamp} identifying the data interval,</li>
 *     <li>a {@code secondHash} for second-level hash aggregation,</li>
 *     <li>a {@code minuteHash} for minute-level aggregation,</li>
 *     <li>a {@code rootHash} representing the final Merkle tree root.</li>
 * </ul>
 *
 * <p>The record implements {@link Datastructure} to allow dynamic SQL operations.</p>
 */
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
