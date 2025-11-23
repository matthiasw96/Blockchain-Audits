package org.hrw.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ConfigLoader {

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
