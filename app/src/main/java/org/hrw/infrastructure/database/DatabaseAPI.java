package org.hrw.infrastructure.database;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.hrw.datamodels.ServerRecord;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class DatabaseAPI implements AutoCloseable {
    private final HttpServer server;
    private final DatabaseHandler db;
    private final Gson gson;
    private final ZoneId zoneId;

    public DatabaseAPI(int port, DatabaseHandler db, ZoneId zoneId) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newSingleThreadExecutor());
        this.db = db;
        this.gson = new Gson();
        this.zoneId = zoneId;

        server.createContext("/selectData", this::handleSelectData);
    }

    private void handleSelectData(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseQuery(exchange.getRequestURI().getRawQuery());

        try {
            ZonedDateTime startDate = parseDate(params.get("startDate"));
            ZonedDateTime endDate = parseDate(params.get("endDate"));

            checkDates(startDate, endDate);

            List<ServerRecord> serverData = db.readFromDatabase(startDate.toEpochSecond(), endDate.toEpochSecond(), "serverdata");

            respond(exchange, 200, jsonData(serverData));
        } catch (DateTimeParseException e) {
            respond(exchange, 400, jsonError("Bad Request","Invalid date. Use yyyy-MM-ddTHH:mm:ss CET"));
        } catch (InvalidParameterException e){
            respond(exchange, 400, jsonError("Bad Request", "Impossible date range. StartDate must be before endDate"));
        } catch (IllegalArgumentException e) {
            respond(exchange, 400, jsonError("Bad Request","Missing params. StartDate and endDate required"));
        } catch (SQLException e) {
            respond(exchange, 500, jsonError("Internal Server Error","SQL Exception. Error reaching database"));
        }
    }

    private ZonedDateTime parseDate(String date) throws IllegalArgumentException, DateTimeParseException {
        if(date == null) {
            throw new IllegalArgumentException();
        }

        return LocalDateTime.parse(date).atZone(zoneId);
    }

    private void checkDates(ZonedDateTime startDate, ZonedDateTime endDate) throws InvalidParameterException {
        if(endDate.isBefore(startDate)) {
            throw new InvalidParameterException();
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

    private String jsonData(List<ServerRecord> data) {
        return gson.toJson(new DataResponse(data.size(), data));
    }
    private String jsonError(String code, String msg) {
        return gson.toJson(new ErrorResponse(code, msg));
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;

        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String val = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            map.put(key, val);
        }
        return map;
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
