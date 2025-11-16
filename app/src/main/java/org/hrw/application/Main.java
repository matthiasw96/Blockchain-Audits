package org.hrw.application;

import org.hrw.infrastructure.anchor.Network;
import org.hrw.hashing.Hasher;
import org.hrw.infrastructure.anchor.AnchorService;
import org.hrw.infrastructure.collector.Collector;
import org.hrw.infrastructure.database.DatabaseAPI;
import org.hrw.infrastructure.database.DatabaseHandler;
import org.hrw.userInterface.UserInterface;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            UserInterface ui = new UserInterface();

            ui.setOnStart(cfg -> new Thread(() -> {
                String url = "jdbc:postgresql://" + cfg.hostDb() + ":5432/postgres";
                System.out.println(LocalDateTime.now().format(FORMATTER) + ": Connecting to " + url);
                try {
                    DatabaseHandler dbHandler = new DatabaseHandler.DatabaseHandlerBuilder()
                            .setHostAddress(cfg.hostDb())
                            .setUsername(cfg.userDb())
                            .setPassword(cfg.passDb())
                            .setFormatter(FORMATTER)
                            .build();

                    Collector collector = new Collector.CollectorBuilder()
                            .setUri(cfg.hostServer())
                            .setUser(cfg.userServer())
                            .setPass(cfg.passServer())
                            .setPeriod(310)
                            .setFormatter(FORMATTER)
                            .build();

                    Hasher hasher = new Hasher(30, FORMATTER);

                    Network selected = Network.parse(cfg.environmentAnchor());

                    AnchorService anchorService = new AnchorService.AnchorServiceBuilder()
                            .setPrivateKey(cfg.seedPhraseAnchor())
                            .setNode(selected.getNodeUri())
                            .setNetworkChainId(selected.getChainId())
                            .setFormatter(FORMATTER)
                            .setInterval(30)
                            .build();

                    Processor processor = new Processor.ProcessorBuilder()
                            .setDbHandler(dbHandler)
                            .setCollector(collector)
                            .setHasher(hasher)
                            .setAnchorService(anchorService)
                            .setFormatter(FORMATTER)
                            .build();

                    Scheduler scheduler = new Scheduler(processor, 300);

                    DatabaseAPI databaseAPI = new DatabaseAPI(8080, dbHandler);

                    scheduler.executeTimer(cfg.dateOfFirstExecution());
                    databaseAPI.start();
                    ui.setOnStop(() -> {
                        try {
                            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Stop requested.");
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

            ui.setOnStop(() -> System.out.println(LocalDateTime.now().format(FORMATTER) + ": Stop requested."));

            ui.setVisible(true);
        });
    }

}