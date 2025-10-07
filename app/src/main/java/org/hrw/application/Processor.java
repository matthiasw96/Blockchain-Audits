package org.hrw.application;

import org.hrw.datamodels.Datastructure;
import org.hrw.datamodels.HashData;
import org.hrw.hashing.Hasher;
import org.hrw.infrastructure.anchor.AnchorService;
import org.hrw.infrastructure.collector.Collector;
import org.hrw.infrastructure.database.DatabaseHandler;
import org.hrw.logging.Logger;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Processor extends TimerTask {
    Collector collector;
    AnchorService anchorService;
    DatabaseHandler dbHandler;
    Hasher hasher;
    Logger logger;

    public Processor(ProcessorBuilder builder) {
        this.collector = builder.collector;
        this.anchorService = builder.anchorService;
        this.dbHandler = builder.dbHandler;
        this.logger = builder.logger;
        this.hasher = builder.hasher;
    }

    public void run() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timePeriod = now.format(fmt);

        try {
            UUID jobId = logger.startJob("Scheduled run", Map.of(
                    "periodSec", timePeriod
            ));

            try {
                System.out.println("Starting process...");
                List<Datastructure> serverData = this.collector.fetch(jobId);

                List<Datastructure> hashedData = this.hasher.hashData(serverData);

                dbHandler.writeToDatabase(serverData, jobId, "test_serverdata");
                dbHandler.writeToDatabase(hashedData, jobId, "test_hashtable");

                this.anchorData(hashedData);

                System.out.println("Finished processing.\n\n");
                System.out.println("--------------------------------------------------");
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                    System.out.println("Process failed");
                    logger.endJob(jobId, false, "Job failed", Map.of("error", e.getMessage()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void anchorData(List<Datastructure> hashedData) throws SQLException {
        for(Datastructure data : hashedData) {
            HashData hashEntry = (HashData) data;
            long timestamp = Long.parseLong(hashEntry.getTimestamp());

            if(timestamp % 60 == 0) {
                anchorService.anchorHashTree(hashEntry);
            }
        }
    }

    public static class ProcessorBuilder {
        Collector collector;
        AnchorService anchorService;
        DatabaseHandler dbHandler;
        Logger logger;
        Hasher hasher;

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

        public ProcessorBuilder setLogger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public ProcessorBuilder setHasher(Hasher hasher) {
            this.hasher = hasher;
            return this;
        }

        public Processor build() {
            return new Processor(this);
        }
    }
}
