package org.hrw;

import org.hrw.backend.api.AuditAPI;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
            .withZone(ZoneId.of("Europe/Berlin"));

    public static void main(String[] args) throws IOException {
        AuditAPI api = new AuditAPI(FORMATTER);

        api.start();
    }
}