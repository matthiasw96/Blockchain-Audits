package org.hrw;

import org.hrw.backend.api.AuditAPI;
import org.hrw.backend.datacollector.DataCollector;
import org.hrw.backend.verifier.BlockchainHandler;
import org.hrw.backend.verifier.Verifier;
import org.hrw.config.ConfigLoader;
import org.hrw.config.ConfigRecord;
import org.hrw.hashing.Hasher;
import org.hrw.mapping.Converter;

import java.io.IOException;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Entry point for the Audit Webservice.
 *
 * <p>This class initializes all required components of the audit backend,
 * including:</p>
 *
 * <ul>
 *     <li>Configuration loading from {@code config.properties}</li>
 *     <li>Converter for mapping raw data into domain objects</li>
 *     <li>Data collector for querying the DatabaseAPI</li>
 *     <li>Blockchain handler for retrieving anchored root hashes</li>
 *     <li>Hasher for local reconstruction of Merkle-based hashes</li>
 *     <li>Verifier for comparing server hashes with blockchain hashes</li>
 *     <li>AuditAPI (HTTP API + Web-GUI backend)</li>
 * </ul>
 *
 * <p>Once all components are configured, the HTTP server for the
 * AuditAPI is started on port 8000.</p>
 */
public class Main {
    public static void main(String[] args) throws IOException {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .withZone(ZoneId.of("Europe/Berlin"));

        ConfigRecord config = ConfigLoader.loadFromFile(Path.of("config.properties"));

        Converter converter = new Converter(config.mappingPath());

        DataCollector collector = new DataCollector.DataCollectorBuilder()
                .setUri(config.collectorServerIp())
                .setPort(config.databaseApiPort())
                .setConverter(converter)
                .setFormatter(FORMATTER)
                .build();

        BlockchainHandler blockchainHandler = new BlockchainHandler(
                config.blockchainUrl(),
                config.blockchainAddress(),
                FORMATTER
        );

        Hasher hasher = new Hasher(
                config.hashIntervalMinutes(),
                FORMATTER,
                config.hashAlgorithm()
        );

        Verifier verifier = new Verifier(
                hasher,
                blockchainHandler,
                FORMATTER
        );

        AuditAPI api = new AuditAPI.AuditAPIBuilder()
                .setConverter(converter)
                .setDataCollector(collector)
                .setVerifier(verifier)
                .setFormat(FORMATTER)
                .build();

        api.start();
    }
}