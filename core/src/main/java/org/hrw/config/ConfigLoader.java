package org.hrw.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Utility class for loading application configuration from a properties file.
 *
 * <p>The configuration is parsed into a {@link ConfigRecord}
 * instance, which is then used throughout the application to initialize
 * collectors, hashing components, database connections and blockchain services.</p>
 *
 * <p>Expected keys in the properties file include:</p>
 * <ul>
 *     <li>{@code mapping.path}</li>
 *     <li>{@code hash.algorithm}</li>
 *     <li>{@code hash.intervalMinutes}</li>
 *     <li>{@code collector.server.ip}</li>
 *     <li>{@code collector.jobIntervalSeconds}</li>
 *     <li>{@code database.ip}, {@code database.port}</li>
 *     <li>{@code blockchain.url}, {@code blockchain.chainId}, {@code blockchain.address}</li>
 *     <li>{@code databaseApi.port}</li>
 *     <li>{@code tool.ip}</li>
 * </ul>
 *
 * <p>If loading fails, a {@link RuntimeException} is thrown to prevent the
 * application from starting with incomplete configuration.</p>
 */
public final class ConfigLoader {

    /**
     * Loads configuration properties from the given file and maps them to a
     * {@link ConfigRecord}.
     *
     * @param path path to the configuration {@code .properties} file
     * @return a populated {@link ConfigRecord} instance with all required settings
     * @throws RuntimeException if the file cannot be read
     */
    public static ConfigRecord loadFromFile(Path path) {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(path)) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config from " + path, e);
        }

        return new ConfigRecord(
                props.getProperty("mapping.path"),
                props.getProperty("hash.algorithm"),
                Integer.parseInt(props.getProperty("hash.intervalMinutes")),
                props.getProperty("collector.server.ip"),
                Integer.parseInt(props.getProperty("collector.jobIntervalSeconds")),
                props.getProperty("database.ip"),
                Integer.parseInt(props.getProperty("database.port")),
                props.getProperty("blockchain.url"),
                Byte.parseByte(props.getProperty("blockchain.chainId")),
                props.getProperty("blockchain.address"),
                Integer.parseInt(props.getProperty("databaseApi.port")),
                props.getProperty("tool.ip")
        );
    }
}
