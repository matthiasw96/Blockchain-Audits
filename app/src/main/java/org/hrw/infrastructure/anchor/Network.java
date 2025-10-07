package org.hrw.infrastructure.anchor;

import com.wavesplatform.transactions.common.ChainId;

public enum Network {
    MAINNET("Mainnet","https://nodes.wavesnodes.com", ChainId.MAINNET),
    TESTNET("Testnet","https://nodes-testnet.wavesnodes.com",ChainId.TESTNET),
    STAGENET("Stagenet","https://nodes-stagenet.wavesnodes.com",ChainId.STAGENET);

    final String label;
    final String nodeUri;
    final byte chainId;

    Network(String label, String nodeUri, byte chainId) {
        this.label = label;
        this.nodeUri = nodeUri;
        this.chainId = chainId;
    }

    public static Network parse(String s) {
        return switch (s) {
            case "MAINNET" -> MAINNET;
            case "TESTNET" -> TESTNET;
            case "STAGENET" -> STAGENET;
            default -> TESTNET;
        };
    }

    public String getNodeUri() {
        return nodeUri;
    }

    public byte getChainId() {
        return chainId;
    }

    @Override public String toString() { return label; }
}