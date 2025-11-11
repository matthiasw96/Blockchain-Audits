package org.hrw.hashing;

import org.hrw.datamodels.HashRecord;
import org.hrw.datamodels.ServerRecord;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Hasher {
    private final String algorithm;
    private List<byte[]> secondHashes;
    private List<byte[]> minuteHashes;
    private final DateTimeFormatter FORMATTER;
    private final int interval;

    public Hasher(String algorithm, int interval) {
        this.algorithm = algorithm;
        this.secondHashes = new ArrayList<>();
        this.minuteHashes = new ArrayList<>();
        this.FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        this.interval = interval;
    }

    public List<HashRecord> hashData(List<ServerRecord> data) {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Hashing data");
        try {
            List<HashRecord> hashedData = new ArrayList<>();

            for(ServerRecord entry : data) {

                long timestamp = Long.parseLong(entry.timestamp());

                byte[] unpackedData = entry.toString().getBytes(StandardCharsets.UTF_8);
                byte[] entryHash = createEntryHash(unpackedData);
                byte[] hourHash = createHourHash(timestamp);
                byte[] minuteHash = createMinuteHash(timestamp);

                this.secondHashes.add(entryHash);

                if(minuteHash != null) {
                    HashRecord hashData = createHashData(minuteHash, hourHash, entry);
                    hashedData.add(hashData);
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

    public byte[] createEntryHash(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(data);
        return digest.digest();
    }

    private byte[] createHourHash(long timestamp) throws NoSuchAlgorithmException {
        if(timestamp % (interval * 60L) == 0) {
            byte[] hourHash = this.createHashTree(this.minuteHashes);
            this.minuteHashes.clear();
            return hourHash;
        } else {
            return null;
        }
    }

    private byte[] createMinuteHash(long timestamp) throws NoSuchAlgorithmException {
        if(timestamp % 60 == 0) {
            byte[] minuteHash = this.createHashTree(secondHashes);
            minuteHashes.add(minuteHash);
            secondHashes.clear();
            return minuteHash;
        } else {
            return null;
        }
    }

    private byte[] createHashTree(List<byte[]> data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        for (byte[] row : data) {
            digest.update(row);
        }
        return digest.digest();
    }

    private HashRecord createHashData(byte[] minuteHash, byte[] hourHash, ServerRecord entry) {
        String minuteHex = HexFormat.of().formatHex(minuteHash);
        String hourHex = hourHash == null ? "" : HexFormat.of().formatHex(hourHash);

        return new HashRecord(entry.timestamp(), minuteHex, hourHex);
    }
}
