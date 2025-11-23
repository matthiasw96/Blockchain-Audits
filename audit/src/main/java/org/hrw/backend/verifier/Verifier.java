package org.hrw.backend.verifier;

import org.hrw.datamodels.HashRecord;
import org.hrw.datamodels.ServerRecord;
import org.hrw.hashing.Hasher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Performs audit verification by comparing locally recomputed root hashes
 * against the root hashes anchored on the blockchain.
 *
 * <p>The verifier executes the following steps:</p>
 * <ol>
 *     <li>Recompute all hierarchical hashes using {@link Hasher}</li>
 *     <li>Extract only the root hashes (interval boundaries)</li>
 *     <li>Load the corresponding root hashes from the blockchain via
 *         {@link BlockchainHandler}</li>
 *     <li>Perform a one-to-one comparison of all root hashes</li>
 * </ol>
 *
 * <p>If all hashes match, the integrity of the server data is verified.</p>
 */
public class Verifier {
    private final Hasher hasher;
    private final BlockchainHandler blockchainHandler;
    private final DateTimeFormatter FORMATTER;

    public Verifier(Hasher hasher, BlockchainHandler blockchainHandler, DateTimeFormatter FORMATTER) {
        this.hasher = hasher;
        this.blockchainHandler = blockchainHandler;
        this.FORMATTER = FORMATTER;
    }

    /**
     * Executes a full integrity verification against the blockchain.
     *
     * <p>This method:</p>
     * <ol>
     *     <li>Hashes all {@link ServerRecord} entries</li>
     *     <li>Removes the first entry (partial interval edge case)</li>
     *     <li>Extracts only entries with non-empty root hashes</li>
     *     <li>Loads the corresponding blockchain values</li>
     *     <li>Compares each root hash pair</li>
     * </ol>
     *
     * @param serverData server telemetry data for the audit interval
     * @return {@code true} if all hashes match; {@code false} otherwise
     */
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

    /**
     * Compares each locally computed root hash with the corresponding root hash
     * retrieved from the blockchain.
     *
     * @param serverRootHashes locally recomputed root hashes
     * @param blockchainRootHashes root hashes read from the blockchain
     * @return true if all pairs match, false otherwise
     */
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

    /**
     * Extracts only the {@link HashRecord} entries that contain a non-empty
     * root hash — i.e. hashes that were created at full aggregation intervals.
     *
     * @param hashedData list of all hierarchical hashes
     * @return list of root-hash records only
     */
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
