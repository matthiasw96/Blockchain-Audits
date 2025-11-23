package org.hrw.config;

public record ConfigRecord(
        String mappingPath,
        String hashAlgorithm,
        int hashIntervalMinutes,
        String collectorServerIp,
        int jobInterval,
        String databaseIp,
        int databasePort,
        String blockchainUrl,
        byte blockchainChainId,
        String blockchainAddress,
        int databaseApiPort,
        String auditToolIp){}
