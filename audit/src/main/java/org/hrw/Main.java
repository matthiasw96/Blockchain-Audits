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