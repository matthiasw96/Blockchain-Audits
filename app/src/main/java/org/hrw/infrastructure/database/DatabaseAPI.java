package org.hrw.infrastructure.database;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.hrw.datamodels.ServerRecord;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;

public class DatabaseAPI implements AutoCloseable {
    private final HttpServer server;
    private final DatabaseHandler db;
    private final Gson gson;
    private final ZoneId zoneId;
    private final DateTimeFormatter FORMATTER;
    private final int interval;

    public DatabaseAPI(int port, DatabaseHandler db, int interval, DateTimeFormatter FORMATTER) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newSingleThreadExecutor());
        this.db = db;
        this.interval = interval;
        this.gson = new Gson();
        this.zoneId = ZoneId.of("Europe/Berlin");
        this.FORMATTER = FORMATTER;
        createContexts();
    }

    private void createContexts() {
        server.createContext("/selectData", this::handleSelectData);

        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Database API running");
    }

    private void handleSelectData(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String start = query.split("&")[0].split("=")[1];
        String end = query.split("&")[1].split("=")[1];

        try {
            ZonedDateTime startDate = calculateStart(start);
            ZonedDateTime endDate = calculateEnd(end);

            List<ServerRecord> serverData = db.readFromDatabase(startDate.toEpochSecond(), endDate.toEpochSecond(), "serverdata");

            respond(exchange, 200, jsonData(serverData));
        } catch (SQLException e) {
            respond(exchange, 500, jsonError("Internal Server Error","SQL Exception. Error reaching database"));
        } finally {
            exchange.close();
        }
    }

    private void respond(HttpExchange ex, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(status, bytes.length);
        try (ex; OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private ZonedDateTime calculateStart(String start) {
        ZonedDateTime startDate = parseDate(start);

        long minutesSinceEpoch = startDate.toEpochSecond() / 60;
        long floored = (minutesSinceEpoch / interval) * interval;

        return Instant.ofEpochSecond(floored * 60)
                .atZone(zoneId)
                .withSecond(0)
                .withNano(0);
    }

    private ZonedDateTime calculateEnd(String end) {
        ZonedDateTime endDate = parseDate(end);

        long minutesSinceEpoch = endDate.toEpochSecond() / 60;
        long ceiled = ((minutesSinceEpoch + interval - 1) / interval) * interval;

        return Instant.ofEpochSecond(ceiled * 60)
                .atZone(zoneId)
                .withSecond(0)
                .withNano(0);
    }

    private ZonedDateTime parseDate(String date) {
        return LocalDateTime.parse(date)
                .atZone(zoneId)
                .truncatedTo(ChronoUnit.MINUTES);
    }


    private String jsonData(List<ServerRecord> data) {
        return gson.toJson(new DataResponse(data.size(), data));
    }

    private String jsonError(String code, String msg) {
        return gson.toJson(new ErrorResponse(code, msg));
    }

    public void start() {
        server.start();
    }

    @Override
    public void close() {
        server.stop(0);
    }

    private record DataResponse(int count, List<ServerRecord> data) {}
    private record ErrorResponse(String error, String message) {}
}
