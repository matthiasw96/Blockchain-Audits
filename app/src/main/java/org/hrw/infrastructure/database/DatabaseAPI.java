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

/**
 * Provides an HTTP API for external components
 * to query server telemetry data stored in the database.
 *
 * <p>The API exposes the endpoint {@code /selectData}, which accepts
 * a start and end timestamp and returns matching {@link ServerRecord} entries
 * as JSON. Time boundaries are aligned to the configured interval to ensure
 * consistent batching with the data collection process.</p>
 *
 * <p>This class uses the internal {@link DatabaseHandler} to access the database
 * and wraps responses using small JSON record classes. It runs on a single-threaded
 * HTTP server and implements {@link AutoCloseable} for clean shutdown.</p>
 */
public class DatabaseAPI implements AutoCloseable {
    private final HttpServer server;
    private final DatabaseHandler db;
    private final DateTimeFormatter FORMATTER;
    private final int interval;
    private final Gson gson;
    private final ZoneId zoneId;

    public DatabaseAPI(DatabaseAPIBuilder builder) {
        this.server = builder.server;
        server.setExecutor(Executors.newSingleThreadExecutor());
        this.db = builder.dbHandler;
        this.interval = builder.interval;
        this.FORMATTER = builder.FORMATTER;
        this.gson = new Gson();
        this.zoneId = ZoneId.of("Europe/Berlin");
        createContexts();
    }


    /**
     * Registers all HTTP endpoints of the Database API.
     */
    private void createContexts() {
        server.createContext("/selectData", this::handleSelectData);
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Database API running");
    }

    /**
     * Handles the {@code /selectData} request by:
     * <ol>
     *     <li>Parsing start/end timestamps from query parameters</li>
     *     <li>Aligning them to interval boundaries</li>
     *     <li>Retrieving matching {@link ServerRecord} entries from the database</li>
     *     <li>Returning the data as JSON</li>
     * </ol>
     *
     * Responds with status 500 if an SQL error occurs.
     */
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

    /**
     * Sends an HTTP response with JSON body, status code and UTF-8 encoding.
     *
     * @param ex     the exchange object
     * @param status HTTP status code
     * @param body   JSON body to send
     */
    private void respond(HttpExchange ex, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(status, bytes.length);
        try (ex; OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    /**
     * Calculates and floors the start timestamp to the next lower anchor interval boundary.
     *
     * @param start timestamp string in {@link LocalDateTime#parse(CharSequence)} format
     * @return floored start timestamp
     */
    private ZonedDateTime calculateStart(String start) {
        ZonedDateTime startDate = parseDate(start);

        long minutesSinceEpoch = startDate.toEpochSecond() / 60;
        long floored = (minutesSinceEpoch / interval) * interval;

        return Instant.ofEpochSecond(floored * 60)
                .atZone(zoneId)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * Calculates and ceilings the end timestamp to the next upper anchor interval boundary.
     *
     * @param end timestamp string in {@link LocalDateTime#parse(CharSequence)} format
     * @return ceiled end timestamp
     */
    private ZonedDateTime calculateEnd(String end) {
        ZonedDateTime endDate = parseDate(end);

        long minutesSinceEpoch = endDate.toEpochSecond() / 60;
        long ceiled = ((minutesSinceEpoch + interval - 1) / interval) * interval;

        return Instant.ofEpochSecond(ceiled * 60)
                .atZone(zoneId)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * Parses a timestamp string and normalizes it to minute precision.
     */
    private ZonedDateTime parseDate(String date) {
        return LocalDateTime.parse(date)
                .atZone(zoneId)
                .truncatedTo(ChronoUnit.MINUTES);
    }

    /**
     * Stops the API and shuts down the underlying HTTP server.
     */
    @Override
    public void close() {
        server.stop(0);
    }

    /**
     * Starts the embedded HTTP server.
     */
    public void start() {
        server.start();
    }

    /**
     * Wraps database results in a compact JSON response object.
     */
    private String jsonData(List<ServerRecord> data) {
        return gson.toJson(new DataResponse(data.size(), data));
    }

    /**
     * Creates a standardized JSON error message.
     */
    private String jsonError(String code, String msg) { return gson.toJson(new ErrorResponse(code, msg)); }

    /**
     * JSON wrapper containing the number of returned records and the data itself.
     */
    private record DataResponse(int count, List<ServerRecord> data) {}

    /**
     * JSON wrapper containing error information.
     */
    private record ErrorResponse(String error, String message) {}

    /**
     * Builder for constructing {@link DatabaseAPI} instances.
     *
     * <p>Allows configuration of server port, database handler, anchor interval
     * and logging date format.</p>
     */
    public static class DatabaseAPIBuilder {
        private HttpServer server;
        private DatabaseHandler dbHandler;
        private int interval;
        private DateTimeFormatter FORMATTER;

        public DatabaseAPIBuilder setDbHandler(DatabaseHandler dbHandler) {
            this.dbHandler = dbHandler;
            return this;
        }

        public DatabaseAPIBuilder setServer(int port) throws IOException {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
            return this;
        }

        public DatabaseAPIBuilder setInterval(int interval) {
            this.interval = interval;
            return this;
        }

        public DatabaseAPIBuilder setFormatter(DateTimeFormatter FORMATTER) {
            this.FORMATTER = FORMATTER;
            return this;
        }

        public DatabaseAPI build() { return new DatabaseAPI(this); }
    }
}
