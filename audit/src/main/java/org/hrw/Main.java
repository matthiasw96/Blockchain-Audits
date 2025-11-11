package org.hrw;

import org.hrw.backend.datacollector.DataCollector;
import org.hrw.backend.verifier.BlockchainHandler;
import org.hrw.backend.verifier.Verifier;
import org.hrw.datamodels.ServerRecord;
import org.hrw.hashing.Hasher;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        LocalDateTime start = LocalDateTime.of(2025,11,11,15,5);
        LocalDateTime end = LocalDateTime.of(2025,11,11,15,7);

        String collectorUri = "localhost";
        int port = 8080;

        String bcUri = "nodes-testnet.wavesnodes.com";
        String bcAddress = "3MsGAHcmUcWLmRAURWFFkuHxMfFp5gossUH";

        String hasherAlgorithm = "SHA-256";


        DataCollector collector = new DataCollector(collectorUri, port);

        BlockchainHandler bcHandler = new BlockchainHandler(bcUri, bcAddress);

        Hasher hasher = new Hasher(hasherAlgorithm, 1);

        Verifier verifier = new Verifier(hasher, bcHandler);

        try {
            List<ServerRecord> data = collector.getServerData(start, end);

            System.out.println("Test 1");
            boolean verified = verifier.verify(data);
            System.out.println("Test 2");

            System.out.println(verified);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}