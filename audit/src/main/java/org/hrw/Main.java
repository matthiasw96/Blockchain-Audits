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
        LocalDateTime start = LocalDateTime.of(2025,10,8,10,0);
        LocalDateTime end = LocalDateTime.of(2025,10,8,16,0);

        String collectorUri = "localhost";
        int port = 8080;

        String bcUri = "nodes-testnet.wavesnodes.com";
        String bcAddress = "3MsGAHcmUcWLmRAURWFFkuHxMfFp5gossUH";

        String hasherAlgorithm = "SHA-256";


        DataCollector collector = new DataCollector(collectorUri, port);

        BlockchainHandler bcHandler = new BlockchainHandler(bcUri, bcAddress);

        Hasher hasher = new Hasher(hasherAlgorithm);

        Verifier verifier = new Verifier(hasher, bcHandler);

        try {
            List<ServerRecord> data = collector.getServerData(start, end);

            boolean verified = verifier.verify(data);

            System.out.println(verified);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}