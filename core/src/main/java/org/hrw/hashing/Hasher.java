package org.hrw.hashing;

import org.hrw.datamodels.HashRecord;
import org.hrw.datamodels.ServerRecord;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Performs hierarchical hashing of server telemetry data.
 *
 * <p>The {@code Hasher} converts incoming {@link ServerRecord} entries into
 * a three-level hash structure:</p>
 * <ul>
 *     <li><b>secondHash</b> – hash of a single record (one timestamp)</li>
 *     <li><b>minuteHash</b> – aggregation of all distinct second hashes within a minute</li>
 *     <li><b>rootHash</b> – aggregation of all minute hashes within a given interval
 *         (in minutes)</li>
 * </ul>
 *
 * <p>For each input record, a {@link HashRecord} is produced, containing the
 * corresponding hash values. Aggregation is performed purely based on the
 * timestamp (epoch seconds) passed with each record.</p>
 */
public class Hasher {
    private List<String> secondHashes;
    private List<String> minuteHashes;
    private final DateTimeFormatter FORMATTER;
    private final int interval;
    private final String algorithm;


    public Hasher(int interval, DateTimeFormatter FORMATTER, String algorithm) {
        this.secondHashes = new ArrayList<>();
        this.minuteHashes = new ArrayList<>();
        this.FORMATTER = FORMATTER;
        this.interval = interval;
        this.algorithm = algorithm;
    }

    /**
     * Computes hierarchical hash values for a list of {@link ServerRecord} entries.
     *
     * <p>For each record:</p>
     * <ol>
     *     <li>parses the timestamp (epoch seconds)</li>
     *     <li>computes a {@code secondHash} over the record contents</li>
     *     <li>aggregates second hashes into {@code minuteHash} at full-minute boundaries</li>
     *     <li>aggregates minute hashes into {@code rootHash} at interval boundaries</li>
     *     <li>creates a {@link HashRecord} with all three hash values</li>
     * </ol>
     *
     * <p>Only new second hashes (not previously seen) within the current window
     * are used for aggregation, to avoid duplication.</p>
     *
     * @param data list of {@link ServerRecord} entries, each with a timestamp in epoch seconds
     * @return list of {@link HashRecord} entries, or {@code null} if hashing fails
     */
    public List<HashRecord> hashData(List<ServerRecord> data) {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Hashing data");
        List<HashRecord> hashedData = new ArrayList<>();

        try {
            for(ServerRecord entry : data) {
                long timestamp = Long.parseLong(entry.timestamp());
                String secondHash = createEntryHash(entry.toString());

                if(!secondHashes.contains(secondHash)) {
                    String minuteHash = createMinuteHash(timestamp);
                    String rootHash = createRootHash(timestamp);
                    secondHashes.add(secondHash);

                    HashRecord hashEntry = new HashRecord(entry.timestamp(), secondHash, minuteHash, rootHash);

                    hashedData.add(hashEntry);
                }
            }

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Hashing data completed");
            return hashedData;
        } catch (Exception e) {
            System.out.println("Hashing failed");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Computes the hash for a single data record using the configured algorithm.
     *
     * @param data input string representation of the record
     * @return hex-encoded hash string
     * @throws NoSuchAlgorithmException if the configured algorithm is not available
     */
    public String createEntryHash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }

    /**
     * Computes the root hash for the given timestamp.
     *
     * <p>If the timestamp lies exactly on an interval boundary
     * ({@code timestamp % (interval * 60) == 0}), all currently collected
     * {@code minuteHashes} are aggregated into a new root hash and the
     * internal minute list is cleared. Otherwise, an empty string is returned
     * to indicate that no root hash is produced at this time.</p>
     *
     * @param timestamp epoch seconds of the data point
     * @return newly computed root hash, or an empty string if no aggregation is due
     * @throws NoSuchAlgorithmException if the configured algorithm is not available
     */
    public String createRootHash(long timestamp) throws NoSuchAlgorithmException {
        if(timestamp % (interval * 60L) == 0) {
            String rootHash = this.createHashTree(minuteHashes);
            this.minuteHashes.clear();
            return rootHash;
        } else {
            return "";
        }
    }

    public String createMinuteHash(long timestamp) throws NoSuchAlgorithmException {
        if(timestamp % 60 == 0) {
            String minuteHash = this.createHashTree(secondHashes);
            if(!minuteHashes.contains(minuteHash)) {
                minuteHashes.add(minuteHash);
            }
            secondHashes.clear();
            return minuteHash;
        } else {
            return "";
        }
    }

    private String createHashTree(List<String> data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        for (String row : data) {
            byte[] entryBytes = row.getBytes(StandardCharsets.UTF_8);
            digest.update(entryBytes);
        }
        return HexFormat.of().formatHex(digest.digest());
    }
}
