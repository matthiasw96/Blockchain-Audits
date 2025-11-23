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

/**
 * Application entry point for the data collection and blockchain anchoring tool.
 *
 * <p>This class loads configuration files, initializes all core components
 * (collector, hasher, database, blockchain anchor), and wires them together
 * using the graphical {@link UserInterface}. User interactions through the UI
 * trigger configuration, scheduling and execution of the processing workflow.</p>
 *
 * <p>The setup process is executed on a background thread to keep the UI responsive.
 * Once initialization is complete, the scheduler and database API are activated
 * and the UI enables start/stop controls.</p>
 */
public class Main {

    /**
     * Starts the application by:
     * <ol>
     *     <li>loading configuration values from {@code config.properties}</li>
     *     <li>initializing the user interface</li>
     *     <li>creating system components on confirmation (collector, hasher,
     *         database handler, scheduler, blockchain anchor)</li>
     *     <li>starting the embedded database API</li>
     *     <li>enabling scheduling controls in the UI</li>
     * </ol>
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

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
                            config.hashAlgorithm()
                    );

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
                            config.jobInterval()
                    );

                    DatabaseAPI databaseAPI = new DatabaseAPI.DatabaseAPIBuilder()
                            .setDbHandler(dbHandler)
                            .setInterval(config.hashIntervalMinutes())
                            .setServer(config.databaseApiPort())
                            .setFormatter(FORMATTER)
                            .build();

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