package org.hrw.application;

import org.hrw.datamodels.HashRecord;
import org.hrw.datamodels.ServerRecord;
import org.hrw.hashing.Hasher;
import org.hrw.infrastructure.anchor.AnchorService;
import org.hrw.infrastructure.collector.Collector;
import org.hrw.infrastructure.database.DatabaseHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Coordinates the complete processing workflow:
 * <ul>
 *     <li>Fetches raw server telemetry data via {@link Collector}</li>
 *     <li>Hashes the collected data and builds Merkle-related structures via {@link Hasher}</li>
 *     <li>Persists both raw and hashed data via {@link DatabaseHandler}</li>
 *     <li>Anchors the calculated hash data in the blockchain using {@link AnchorService}</li>
 * </ul>
 *
 * <p>This class is executed periodically by the {@link org.hrw.application.Scheduler}
 * or manually via the user interface. It acts as the main orchestration unit for
 * the end-to-end integrity verification pipeline.</p>
 *
 * <p>The class is instantiated using the {@link ProcessorBuilder} to simplify dependency
 * injection and configuration.</p>
 */
public class Processor {
    private final Collector collector;
    private final AnchorService anchorService;
    private final DatabaseHandler dbHandler;
    private final Hasher hasher;
    private final DateTimeFormatter FORMATTER;

    public Processor(ProcessorBuilder builder) {
        this.collector = builder.collector;
        this.anchorService = builder.anchorService;
        this.dbHandler = builder.dbHandler;
        this.hasher = builder.hasher;
        this.FORMATTER = builder.FORMATTER;
    }

    /**
     * Executes the full processing pipeline:
     * <ol>
     *     <li>Logs the start time</li>
     *     <li>Fetches raw server data</li>
     *     <li>Hashes the data</li>
     *     <li>Writes both raw and hashed data to the database</li>
     *     <li>Anchors the hashed values in the blockchain</li>
     *     <li>Logs the completion time</li>
     * </ol>
     *
     * <p>All exceptions are caught internally to prevent scheduler interruption.
     * Errors are logged to the console for later inspection.</p>
     */
    public void run() {
        try {
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Starting process...");

            List<ServerRecord> serverData = collector.fetch();
            List<HashRecord> hashedData = hasher.hashData(serverData);

            dbHandler.writeToDatabase(serverData, "serverdata");
            dbHandler.writeToDatabase(hashedData, "hashdata");

            anchorService.anchorData(hashedData);

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Finished processing.\n\n");
            System.out.println("--------------------------------------------------");
        } catch (Exception e) {
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Process failed");
            e.printStackTrace();
        }
    }


    /**
     * Builder for constructing {@link Processor} instances.
     *
     * <p>Allows configuration of collector, anchor service, database handler, hasher
     * and logging date format.</p>
     */
    public static class ProcessorBuilder {
        Collector collector;
        AnchorService anchorService;
        DatabaseHandler dbHandler;
        Hasher hasher;
        DateTimeFormatter FORMATTER;

        public ProcessorBuilder setCollector(Collector collector) {
            this.collector = collector;
            return this;
        }

        public ProcessorBuilder setAnchorService(AnchorService anchorService) {
            this.anchorService = anchorService;
            return this;
        }

        public ProcessorBuilder setDbHandler(DatabaseHandler dbHandler) {
            this.dbHandler = dbHandler;
            return this;
        }

        public ProcessorBuilder setHasher(Hasher hasher) {
            this.hasher = hasher;
            return this;
        }

        public ProcessorBuilder setFormatter(DateTimeFormatter FORMATTER) {
            this.FORMATTER = FORMATTER;
            return this;
        }

        public Processor build() {
            return new Processor(this);
        }
    }
}
