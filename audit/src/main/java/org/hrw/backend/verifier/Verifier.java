package org.hrw.backend.verifier;

import org.hrw.datamodels.HashRecord;
import org.hrw.datamodels.ServerRecord;
import org.hrw.hashing.Hasher;

import java.util.ArrayList;
import java.util.List;

public class Verifier {
    private final Hasher hasher;
    private final BlockchainHandler blockchainHandler;

    public Verifier(Hasher hasher, BlockchainHandler blockchainHandler) {
        this.hasher = hasher;
        this.blockchainHandler = blockchainHandler;
    }

    public boolean verify(List<ServerRecord> serverData) {
        List<HashRecord> hashedData = this.hasher.hashData(serverData);
        List<HashRecord> serverRootHashes = this.extractRootHashes(hashedData);
        List<String> blockchainRootHashes = this.blockchainHandler.getBlockchainHashes(serverRootHashes);

        for(int i=0; i<serverRootHashes.size(); i++) {
            String serverHash = serverRootHashes.get(i).hourHash();
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
            if(!hashData.hourHash().equals("")) {
                System.out.println(hashData.timestamp() + ": " + hashData.hourHash());
                rootHashes.add(hashData);
            }
        }
        return rootHashes;
    }
}
