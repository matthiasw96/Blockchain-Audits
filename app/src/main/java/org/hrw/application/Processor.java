package org.hrw.application;

import org.hrw.datamodels.HashData;
import org.hrw.datamodels.ServerData;
import org.hrw.hashing.Hasher;
import org.hrw.infrastructure.anchor.AnchorService;
import org.hrw.infrastructure.collector.Collector;
import org.hrw.infrastructure.database.DatabaseHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Processor extends TimerTask {
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

    public void run() {
        try {
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Starting process...");

            List<ServerData> serverData = this.collector.fetch();
            List<HashData> hashedData = this.hasher.hashData(serverData);

            dbHandler.writeToDatabase(serverData, "test_serverdata");
            dbHandler.writeToDatabase(hashedData, "test_hashtable");

            this.anchorService.anchorData(hashedData);

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Finished processing.\n\n");
            System.out.println("--------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Process failed");
        }
    }

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
