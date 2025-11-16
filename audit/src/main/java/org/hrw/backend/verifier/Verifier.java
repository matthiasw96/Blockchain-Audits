package org.hrw.backend.verifier;

import org.hrw.datamodels.HashRecord;
import org.hrw.datamodels.ServerRecord;
import org.hrw.hashing.Hasher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Verifier {
    private final Hasher hasher;
    private final BlockchainHandler blockchainHandler;
    private final DateTimeFormatter FORMATTER;

    public Verifier(Hasher hasher, BlockchainHandler blockchainHandler, DateTimeFormatter FORMATTER) {
        this.hasher = hasher;
        this.blockchainHandler = blockchainHandler;
        this.FORMATTER = FORMATTER;
    }

    public boolean verify(List<ServerRecord> serverData) {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Verifying root hashes");

        List<HashRecord> hashedData = hasher.hashData(serverData);
        hashedData.removeFirst();
        List<HashRecord> serverRootHashes = extractRootHashes(hashedData);
        List<String> blockchainRootHashes = blockchainHandler.getBlockchainHashes(serverRootHashes);
        boolean isVerified = checkHashes(serverRootHashes, blockchainRootHashes);

        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Verification process completed");
        return isVerified;
    }

    private boolean checkHashes(List<HashRecord> serverRootHashes, List<String> blockchainRootHashes) {
        for(int i=0; i<serverRootHashes.size(); i++) {
            String serverHash = serverRootHashes.get(i).rootHash();
            String blockchainHash = blockchainRootHashes.get(i);

            if(!serverHash.equals(blockchainHash)) {
                return false;
            }
        }
        return true;
    }

    private List<HashRecord> extractRootHashes(List<HashRecord> hashedData) {
        List<HashRecord> rootHashes = new ArrayList<>();
        for (HashRecord hashData : hashedData) {
            if(!hashData.rootHash().isEmpty()) {
                rootHashes.add(hashData);
            }
        }
        return rootHashes;
    }
}
