package org.hrw.infrastructure.anchor;

import com.wavesplatform.wavesj.exceptions.NodeException;

import java.io.IOException;
import java.net.URISyntaxException;

import java.sql.SQLException;
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
import org.hrw.datamodels.HashData;

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

    private DataTransaction createTransaction(DataEntry entry) {
        return DataTransaction.
                builder(Collections.singletonList(entry))
                .fee(0)
                .timestamp(Instant.now().toEpochMilli())
                .chainId(this.networkChainId)
                .getSignedWith(this.privateKey);
    }

    private DataEntry createDataEntry(String timestamp, String hashValue) {
        return new StringEntry(timestamp, hashValue);
    }

    private void broadcastEntry(DataTransaction tx) throws NodeException, IOException {
        this.node.broadcast(tx);
    }

    public void anchorData(List<HashData> hashedData) throws SQLException {
        for(HashData hashEntry : hashedData) {
            long timestamp = Long.parseLong(hashEntry.getTimestamp());

            if(timestamp % 3600 == 0) {
                this.anchorHashTree(hashEntry);
            }
        }
    }

    private void anchorHashTree(HashData rootHash) throws SQLException {
        try{
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Anchoring Data");

            String timestamp = rootHash.getTimestamp();
            String hourHash = rootHash.getHourHash();

            DataEntry entry = this.createDataEntry(timestamp, hourHash);
            DataTransaction transaction = this.createTransaction(entry);
            this.broadcastEntry(transaction);

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Data Anchored");
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Anchoring data failed");
        }
    }

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
            this.node = new Node(url);
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
