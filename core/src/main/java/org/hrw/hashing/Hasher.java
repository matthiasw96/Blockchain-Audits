package org.hrw.hashing;

import org.hrw.datamodels.HashData;
import org.hrw.datamodels.ServerData;

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

    public Hasher(HasherBuilder builder) {
        this.algorithm = builder.algorithm;
        this.secondHashes = new ArrayList<>();
        this.minuteHashes = new ArrayList<>();
        this.FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    public List<HashData> hashData(List<ServerData> data) {
        try {
            List<HashData> hashedData = new ArrayList<>();
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Hashing data");

            for(ServerData entry : data) {
                long timestamp = Long.parseLong(entry.getTimestamp());

                byte[] unpackedData = entry.toString().getBytes(StandardCharsets.UTF_8);
                byte[] entryHash = this.createEntryHash(unpackedData);
                byte[] hourHash = this.createHourHash(timestamp);
                byte[] minuteHash = this.createMinuteHash(timestamp);

                this.secondHashes.add(entryHash);

                if(minuteHash != null) {
                    HashData hashData = this.createHashData(minuteHash, hourHash, entry);
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

    private HashData createHashData(byte[] minuteHash, byte[] hourHash, ServerData entry) {
        String minuteHex = HexFormat.of().formatHex(minuteHash);
        String hourHex = hourHash == null ? "" : HexFormat.of().formatHex(hourHash);

        return new HashData.HashDataBuilder()
                .setJobId(entry.getJobId())
                .setTimestamp(entry.getTimestamp())
                .setHourHash(hourHex)
                .setMinuteHash(minuteHex)
                .build();
    }

    public static class HasherBuilder {
        String algorithm;

        public HasherBuilder setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public Hasher build() {
            return new Hasher(this);
        }
    }
}
