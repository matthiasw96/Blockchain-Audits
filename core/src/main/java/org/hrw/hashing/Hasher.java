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

    public Hasher(String algorithm) {
        this.algorithm = algorithm;
        this.secondHashes = new ArrayList<>();
        this.minuteHashes = new ArrayList<>();
        this.FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    public List<HashRecord> hashData(List<ServerRecord> data) {
        try {
            List<HashRecord> hashedData = new ArrayList<>();
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Hashing data");

            for(ServerRecord entry : data) {
                long timestamp = Long.parseLong(entry.timestamp());

                byte[] unpackedData = entry.toString().getBytes(StandardCharsets.UTF_8);
                byte[] entryHash = this.createEntryHash(unpackedData);
                byte[] hourHash = this.createHourHash(timestamp);
                byte[] minuteHash = this.createMinuteHash(timestamp);

                this.secondHashes.add(entryHash);

                if(minuteHash != null) {
                    HashRecord hashData = this.createHashData(minuteHash, hourHash, entry);
                    hashedData.add(hashData);
                }
            }

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Hashing data completed");

            return hashedData;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Hashing failed");
        }
        return null;
    }

    public byte[] createEntryHash(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(this.algorithm);
        digest.update((byte) 0x00);
        digest.update(data);
        return digest.digest();
    }

    private byte[] createHourHash(long timestamp) throws NoSuchAlgorithmException {
        if(timestamp % 3600 == 0) {
            byte[] hourHash = this.createHashTree(this.minuteHashes, (byte) 0x02);

            for(byte[] hash : minuteHashes) {
                System.out.println(timestamp + ": " + HexFormat.of().formatHex(hash));
            }

            System.out.println(timestamp + ": " + HexFormat.of().formatHex(hourHash));
            System.out.println("............................................");
            System.out.println();

            this.minuteHashes.clear();
            return hourHash;
        } else {
            return null;
        }
    }

    private byte[] createMinuteHash(long timestamp) throws NoSuchAlgorithmException {
        if(timestamp % 60 == 0) {
            byte[] minuteHash = this.createHashTree(this.secondHashes, (byte) 0x03);
            this.minuteHashes.add(minuteHash);
            this.secondHashes.clear();
            return minuteHash;
        } else {
            return null;
        }
    }

    private byte[] createHashTree(List<byte[]> data, byte level) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(this.algorithm);
        digest.update(level);
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
