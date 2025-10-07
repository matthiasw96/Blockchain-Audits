package org.hrw.application;

import org.hrw.infrastructure.anchor.Network;
import org.hrw.hashing.Hasher;
import org.hrw.infrastructure.anchor.AnchorService;
import org.hrw.infrastructure.collector.Collector;
import org.hrw.infrastructure.database.DatabaseAPI;
import org.hrw.infrastructure.database.DatabaseHandler;
import org.hrw.logging.Logger;
import org.hrw.userInterface.UserInterface;
import org.hrw.datamodels.Mapper;

import javax.swing.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            UserInterface ui = new UserInterface();

            ui.setOnStart(cfg -> new Thread(() -> {
                String url = "jdbc:postgresql://" + cfg.hostDb() + ":" + cfg.portDb() + "/" + cfg.dbName();
                System.out.println("Connecting to " + url);
                try {
                    Logger logger = new Logger(() -> {
                        try { return DriverManager.getConnection(url, cfg.userDb(), cfg.passDb()); }
                        catch (SQLException e) { throw new RuntimeException(e); }
                    });

                    Map<String, String> map = Mapper.createColumnMap();

                    DatabaseHandler dbHandler = new DatabaseHandler.DatabaseHandlerBuilder()
                            .setHostAddress(cfg.hostDb())
                            .setPort(cfg.portDb())
                            .setUsername(cfg.userDb())
                            .setPassword(cfg.passDb())
                            .setDatabaseName(cfg.dbName())
                            .setLogger(logger)
                            .build();

                    Collector collector = new Collector.CollectorBuilder()
                            .setUri(cfg.hostServer())
                            .setUser(cfg.userServer())
                            .setPass(cfg.passServer())
                            .setPeriod(cfg.timePeriodSeconds())
                            .setMap(map)
                            .setLogger(logger)
                            .build();

                    Hasher hasher = new Hasher.HasherBuilder()
                            .setAlgorithm(cfg.hasherAlgorithm())
                            .build();

                    Network selected = Network.parse(cfg.environmentAnchor());
                    AnchorService anchorService = new AnchorService.AnchorServiceBuilder()
                            .setLogger(logger)
                            .setPrivateKey(cfg.seedPhraseAnchor())
                            .setNode(selected.getNodeUri())
                            .setNetworkChainId(selected.getChainId())
                            .build();

                    Processor processor = new Processor.ProcessorBuilder()
                            .setDbHandler(dbHandler)
                            .setCollector(collector)
                            .setHasher(hasher)
                            .setLogger(logger)
                            .setAnchorService(anchorService)
                            .build();

                    Scheduler scheduler = new Scheduler.SchedulerBuilder()
                            .setTimerTask(processor)
                            .setTimePeriod(cfg.timePeriodSeconds())
                            .build();

                    DatabaseAPI databaseAPI = new DatabaseAPI.DatabaseAPIBuilder()
                            .setServer(8080)
                            .setDB(dbHandler)
                            .setZoneId(ZoneId.of("Europe/Berlin"))
                            .build();

                    scheduler.executeTimer(cfg.dateOfFirstExecution());
                    databaseAPI.start();
                    ui.setOnStop(() -> {
                        try {
                            scheduler.stop();
                            databaseAPI.close();
                        } catch (Exception ignored) {}
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(ui, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE)
                    );
                }
            }, "boot-thread").start());

            ui.setOnStop(() -> System.out.println("Stop requested."));

            ui.setVisible(true);
        });
    }

}