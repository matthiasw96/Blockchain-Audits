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

public class AnchorService {
    private final PrivateKey privateKey;
    private final byte networkChainId;
    private final Node node;
    private final DateTimeFormatter FORMATTER;
    private final int interval;

    public AnchorService(AnchorServiceBuilder builder) {
        this.privateKey = builder.privateKey;
        this.networkChainId = builder.networkChainId;
        this.node = builder.node;
        this.FORMATTER = builder.FORMATTER;
        this.interval = builder.interval;
    }

    public void anchorData(List<HashRecord> hashedData) {
        for(HashRecord hashEntry : hashedData) {
            long timestamp = Long.parseLong(hashEntry.timestamp());

            if(timestamp % (interval * 60L) == 0) {
                anchorHashTree(hashEntry);
            }
        }
    }

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

    private DataTransaction createTransaction(DataEntry entry) {
        return DataTransaction.
                builder(Collections.singletonList(entry))
                .fee(0)
                .timestamp(Instant.now().toEpochMilli())
                .chainId(networkChainId)
                .getSignedWith(privateKey);
    }

    public static class AnchorServiceBuilder {
        PrivateKey privateKey;
        byte networkChainId;
        Node node;
        DateTimeFormatter FORMATTER;
        int interval;

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

        public AnchorServiceBuilder setInterval(int interval) {
            this.interval = interval;
            return this;
        }

        public AnchorService build() {
            return new AnchorService(this);
        }
    }
}
