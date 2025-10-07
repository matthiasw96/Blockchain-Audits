package org.hrw.infrastructure.anchor;

import com.wavesplatform.wavesj.exceptions.NodeException;

import java.io.IOException;
import java.net.URISyntaxException;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import com.wavesplatform.transactions.DataTransaction;
import com.wavesplatform.transactions.data.StringEntry;
import com.wavesplatform.transactions.data.DataEntry;
import com.wavesplatform.transactions.account.PrivateKey;
import com.wavesplatform.transactions.account.Address;
import com.wavesplatform.wavesj.Node;
import org.hrw.datamodels.HashData;
import org.hrw.logging.Logger;

public class AnchorService {
    Logger logger;
    PrivateKey privateKey;
    byte networkChainId;
    Node node;


    public AnchorService(AnchorServiceBuilder builder) {
        this.logger = builder.logger;
        this.privateKey = builder.privateKey;
        this.networkChainId = builder.networkChainId;
        this.node = builder.node;
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

    public void anchorHashTree(HashData rootHash) throws SQLException {
        try{
            System.out.println("Anchoring Data");
            String timestamp = rootHash.getTimestamp();
            String hourHash = rootHash.getHourHash();

            DataEntry entry = this.createDataEntry(timestamp, hourHash);
            DataTransaction transaction = this.createTransaction(entry);
            this.broadcastEntry(transaction);

            logger.log(UUID.fromString(rootHash.getJobId()), Logger.Stage.ANCHOR, Logger.Status.OK, "Data Anchored", Map.of(
                    "address", Address.from(networkChainId, privateKey.publicKey()).toString(),
                    "transaction", transaction.toJson()
            ));

            System.out.println("Data Anchored");
        } catch(Exception e) {
            logger.log(UUID.fromString(rootHash.getJobId()), Logger.Stage.ANCHOR, Logger.Status.FAIL, "Error", Map.of("error", e.toString()));
            e.printStackTrace();
            System.out.println("Anchoring data failed");
        }
    }

    public static class AnchorServiceBuilder {
        Logger logger;
        PrivateKey privateKey;
        byte networkChainId;
        Node node;

        public AnchorServiceBuilder setLogger(Logger logger) {
            this.logger = logger;
            return this;
        }

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

        public AnchorService build() {
            return new AnchorService(this);
        }
    }
}
