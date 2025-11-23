package org.hrw;

import org.hrw.application.Processor;
import org.hrw.application.Scheduler;
import org.hrw.config.ConfigLoader;
import org.hrw.config.ConfigRecord;
import org.hrw.hashing.Hasher;
import org.hrw.infrastructure.anchor.AnchorService;
import org.hrw.infrastructure.collector.Collector;
import org.hrw.infrastructure.database.DatabaseAPI;
import org.hrw.infrastructure.database.DatabaseHandler;
import org.hrw.mapping.Converter;
import org.hrw.userInterface.UserInterface;

import javax.swing.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static void main(String[] args) {
        ConfigRecord config = ConfigLoader.loadFromFile(Path.of("config.properties"));

        SwingUtilities.invokeLater(() -> {
            UserInterface ui = new UserInterface();

            ui.setOnConfirm(uiInput -> new Thread(() -> {
                try {
                    Converter converter = new Converter(
                            config.mappingPath()
                    );

                    DatabaseHandler dbHandler = new DatabaseHandler.DatabaseHandlerBuilder()
                            .setHostAddress(config.databaseIp())
                            .setPort(config.databasePort())
                            .setUsername(uiInput.userDb())
                            .setPassword(uiInput.passDb())
                            .setConverter(converter)
                            .setFormatter(FORMATTER)
                            .build();

                    Collector collector = new Collector.CollectorBuilder()
                            .setHostAddress(config.collectorServerIp())
                            .setUser(uiInput.userServer())
                            .setPass(uiInput.passServer())
                            .setPeriod(config.jobInterval() + 10)
                            .setConverter(converter)
                            .setFormatter(FORMATTER)
                            .build();

                    Hasher hasher = new Hasher(
                            config.hashIntervalMinutes(),
                            FORMATTER,
                            config.hashAlgorithm());

                    AnchorService anchorService = new AnchorService.AnchorServiceBuilder()
                            .setPrivateKey(uiInput.seedPhraseAnchor())
                            .setNode(config.blockchainUrl())
                            .setNetworkChainId(config.blockchainChainId())
                            .setFormatter(FORMATTER)
                            .build();

                    Processor processor = new Processor.ProcessorBuilder()
                            .setDbHandler(dbHandler)
                            .setCollector(collector)
                            .setHasher(hasher)
                            .setAnchorService(anchorService)
                            .setFormatter(FORMATTER)
                            .build();

                    Scheduler scheduler = new Scheduler(
                            processor,
                            config.jobInterval());

                    DatabaseAPI databaseAPI = new DatabaseAPI(
                            config.databaseApiPort(),
                            dbHandler,
                            config.hashIntervalMinutes(),
                            FORMATTER);

                    databaseAPI.start();

                    SwingUtilities.invokeLater(() -> {
                        ui.setOnStart(() -> scheduler.executeTimer(uiInput.dateOfFirstExecution()));
                        ui.setOnStop(scheduler::stop);
                        ui.setStartEnabled(true);
                    });

                    System.out.println(LocalDateTime.now().format(FORMATTER) + ": Configuration completed");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(ui, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE)
                    );
                }
            }, "boot-thread").start());

            ui.setVisible(true);
        });
    }
}