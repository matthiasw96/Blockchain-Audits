package org.hrw.config;

/**
 * Immutable configuration container representing all application-relevant
 * settings loaded from the {@code config.properties} file.
 *
 * <p>This record aggregates configuration parameters used by:</p>
 * <ul>
 *     <li>the data collector (server IP, job interval)</li>
 *     <li>the hashing component (algorithm, interval)</li>
 *     <li>the database connection (IP, port)</li>
 *     <li>the blockchain anchoring service (URL, chain ID, address)</li>
 *     <li>the embedded database API (port)</li>
 *     <li>the audit tool (IP address)</li>
 * </ul>
 *
 * <p>All fields are required and must be present in the configuration file
 * loaded by {@link ConfigLoader}.</p>
 */
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
