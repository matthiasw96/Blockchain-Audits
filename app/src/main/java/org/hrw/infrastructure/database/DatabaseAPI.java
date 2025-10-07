package org.hrw.infrastructure.database;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.hrw.datamodels.ServerData;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
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

    public DatabaseAPI(DatabaseAPIBuilder builder) {
        this.server = builder.server;
        this.db = builder.db;
        this.gson = new Gson();
        this.zoneId = builder.zoneId;

        server.createContext("/selectData", this::handleSelectData);
    }

    private void handleSelectData(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.parseQuery(exchange.getRequestURI().getRawQuery());

        String start = params.get("startDate");
        String end = params.get("endDate");

        if(start == null || end == null) {
            this.respond(exchange, 400, this.jsonError("missing_params","startDate and endDate required"));
            return;
        }

        ZonedDateTime startDate, endDate;

        try {
            startDate = LocalDateTime.parse(start).atZone(zoneId);
            endDate   = LocalDateTime.parse(end).atZone(zoneId);

            if(endDate.isBefore(startDate)) {
                this.respond(exchange, 400, this.jsonError("startDate", "startDate must be before endDate"));
            }

            List<ServerData> serverData = this.db.readFromDatabase(startDate.toEpochSecond(), endDate.toEpochSecond(), "test_serverdata");

            this.respond(exchange, 200, this.jsonData(serverData));
        } catch (DateTimeParseException e) {
            respond(exchange, 400, this.jsonError("invalid_date","Use YYYY-MM-DDTHH:MM:SS CTE"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    private String jsonData(List<ServerData> serverData) {
        String json = "{\"count\":"+serverData.size()+",\"data\":[";
        for(int i = 0; i < serverData.size(); i++) {
            ServerData sd = serverData.get(i);
            if(i < serverData.size() - 1) {
                json += this.gson.toJson(sd) + ",";
            } else {
                json += this.gson.toJson(sd);
            }
        }
        json += "]}";

        return json;
    }

    private String jsonError(String code, String msg) {
        return "{\"error\":\"" + code + "\",\"message\":\"" + msg + "\"}";
    }

    private Map<String,String> parseQuery(String query) {
        Map<String,String> map = new HashMap<>();

        if(query == null || query.isEmpty()) {
            return map;
        }

        for (String pair : query.split("&")) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? pair.substring(0, idx) : pair;
            String value = idx > 0 ? pair.substring(idx + 1) : "";

            map.put(key, value);
        }

        System.out.println("Map: " + map);

        return map;
    }

    public void start() {
        this.server.start();
    }

    @Override
    public void close() {
        this.server.stop(0);
    }

    public static class DatabaseAPIBuilder {
        private HttpServer server;
        private DatabaseHandler db;
        private ZoneId zoneId;

        public DatabaseAPIBuilder setServer(int port) throws IOException {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
            server.setExecutor(Executors.newSingleThreadExecutor());
            return this;
        }

        public DatabaseAPIBuilder setDB(DatabaseHandler db) {
            this.db = db;
            return this;
        }

        public DatabaseAPIBuilder setZoneId(ZoneId zoneId) {
            this.zoneId = zoneId;
            return this;
        }

        public DatabaseAPI build() {
            return new DatabaseAPI(this);
        }
    }
}

