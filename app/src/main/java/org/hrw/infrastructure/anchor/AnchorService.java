package org.hrw.infrastructure.anchor;

import com.wavesplatform.wavesj.exceptions.NodeException;

import java.io.IOException;
import java.net.URISyntaxException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import com.wavesplatform.transactions.DataTransaction;
import com.wavesplatform.transactions.data.StringEntry;
import com.wavesplatform.transactions.data.DataEntry;
import com.wavesplatform.transactions.account.PrivateKey;
import com.wavesplatform.wavesj.Node;
import org.hrw.datamodels.HashRecord;

/**
 * Service responsible for anchoring Merkle root hashes in the Waves blockchain.
 *
 * <p>The service creates and broadcasts {@link DataTransaction} entries containing
 * the root hash and timestamp provided by {@link HashRecord}. Each hash entry is
 * written to the blockchain as a {@link StringEntry} for later verification.</p>
 *
 * <p>This class is constructed through {@link AnchorServiceBuilder} to ensure
 * proper configuration of private key, node connection and chain ID.</p>
 */
public class AnchorService {
    private final PrivateKey privateKey;
    private final byte networkChainId;
    private final Node node;
    private final DateTimeFormatter FORMATTER;

    public AnchorService(AnchorServiceBuilder builder) {
        this.privateKey = builder.privateKey;
        this.networkChainId = builder.networkChainId;
        this.node = builder.node;
        this.FORMATTER = builder.FORMATTER;
    }

    /**
     * Checks each entry if it contains a root hash and anchors matches to the Waves blockchain
     *
     * @param hashedData list of {@link HashRecord} objects containing hashes of server data
     */
    public void anchorData(List<HashRecord> hashedData) {
        for(HashRecord hashEntry : hashedData) {
            if(!hashEntry.rootHash().isEmpty()) {
                anchorHashTree(hashEntry);
            }
        }
    }

    /**
     * Anchors a single Merkle root hash in the Waves blockchain.
     *
     * <p>The method prints progress information, creates a signed
     * {@link DataTransaction}, and broadcasts it to the configured Waves node.</p>
     *
     * @param rootHash the hash record to anchor
     */
    private void anchorHashTree(HashRecord rootHash) {
        try{
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Anchoring Data");

            DataEntry entry = new StringEntry(rootHash.timestamp(), rootHash.rootHash());
            DataTransaction transaction = createTransaction(entry);
            node.broadcast(transaction);

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Data Anchored");
        } catch(Exception e) {
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Anchoring data failed");
            e.printStackTrace();
        }
    }

    /**
     * Creates a signed Waves {@link DataTransaction} containing the given data entry.
     *
     * @param entry the data entry representing the root hash to be written
     * @return signed {@link DataTransaction}
     */
    private DataTransaction createTransaction(DataEntry entry) {
        return DataTransaction.
                builder(Collections.singletonList(entry))
                .fee(0)
                .timestamp(Instant.now().toEpochMilli())
                .chainId(networkChainId)
                .getSignedWith(privateKey);
    }

    /**
     * Builder for constructing {@link AnchorService} instances.
     *
     * <p>Allows configuration of private key, chain ID, Waves node connection
     * and logging date format.</p>
     */
    public static class AnchorServiceBuilder {
        PrivateKey privateKey;
        byte networkChainId;
        Node node;
        DateTimeFormatter FORMATTER;

        public AnchorServiceBuilder setPrivateKey(String seedPhrase) {
            this.privateKey = PrivateKey.fromSeed(seedPhrase);
            return this;
        }

        public AnchorServiceBuilder setNetworkChainId(byte networkChainId) {
            this.networkChainId = networkChainId;
            return this;
        }

        public AnchorServiceBuilder setNode(String url) throws NodeException, URISyntaxException, IOException {
            this.node = new Node("https://"+url);
            return this;
        }

        public AnchorServiceBuilder setFormatter(DateTimeFormatter FORMATTER) {
            this.FORMATTER = FORMATTER;
            return this;
        }

        public AnchorService build() {
            return new AnchorService(this);
        }
    }
}
