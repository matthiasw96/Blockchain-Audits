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
        this.algorithm = algorithm; //TODO: Übernahme aus Konfig
    }

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

    public String createEntryHash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }


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
