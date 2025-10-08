package org.hrw.backend.verifier;

import org.hrw.datamodels.HashData;
import org.hrw.datamodels.ServerData;
import org.hrw.hashing.Hasher;

import java.util.ArrayList;
import java.util.List;

public class Verifier {
    private final Hasher hasher;
    private final BlockchainHandler blockchainHandler;

    public Verifier(VerifierBuilder builder ) {
        this.hasher = builder.hasher;
        this.blockchainHandler = builder.blockchainHandler;
    }

    public boolean verify(List<ServerData> serverData) {
        List<HashData> hashedData = this.hasher.hashData(serverData);
        List<String> serverRootHashes = this.extractRootHashes(hashedData);
        List<String> blockchainRootHashes = this.blockchainHandler.getBlockchainHashes(hashedData);

        for(int i=0; i<serverRootHashes.size(); i++) {
            String serverHash = serverRootHashes.get(i);
            String blockchainHash = blockchainRootHashes.get(i);

            if(!serverHash.equals(blockchainHash)) {
                return false;
            }
        }
        return true;
    }

    private List<String> extractRootHashes(List<HashData> hashedData) {
        List<String> rootHashes = new ArrayList<>();
        for (HashData hashData : hashedData) {
            if(hashData.getHourHash() != null) {
                rootHashes.add(hashData.getHourHash());
            }
        }
        return rootHashes;
    }

    public static class VerifierBuilder {
        private Hasher hasher;
        private BlockchainHandler blockchainHandler;

        public VerifierBuilder setHasher(Hasher hasher) {
            this.hasher = hasher;
            return this;
        }

        public VerifierBuilder setBlockchainHandler(BlockchainHandler blockchainHandler) {
            this.blockchainHandler = blockchainHandler;
            return this;
        }

        public Verifier build() {
            return new Verifier(this);
        }
    }
}
