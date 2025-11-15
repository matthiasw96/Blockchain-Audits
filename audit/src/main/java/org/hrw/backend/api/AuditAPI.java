package org.hrw.backend.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.hrw.backend.audit.AuditPdfCreator;
import org.hrw.backend.audit.AuditSummary;
import org.hrw.backend.datacollector.DataCollector;
import org.hrw.backend.verifier.BlockchainHandler;
import org.hrw.backend.verifier.Verifier;
import org.hrw.datamodels.ServerRecord;
import org.hrw.hashing.Hasher;
import org.hrw.mapping.Mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AuditAPI implements AutoCloseable {
    private HttpServer server;
    private DataCollector dataCollector;
    private Verifier verifier;
    private Mapper mapper;
    private Gson gson;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private boolean isVerified;
    private List<ServerRecord> data;
    private AuditPdfCreator pdfCreator;

    public AuditAPI() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(8000), 0);
        this.mapper = new Mapper();
        this.gson = new Gson();
        this.pdfCreator = new AuditPdfCreator();
        createContexts();
    }

    private void createContexts() {
        server.createContext("/setup", this::handleSetup);
        server.createContext("/selectData", this::handleSelectData);
        server.createContext("/verifyData", this::handleVerifyData);
        server.createContext("/downloadData", this::handleDownloadData);
        server.createContext("/downloadReport", this::handleDownloadReport);
    }

    private void handleSetup(HttpExchange httpExchange) throws IOException {
        String body = readRequestBody(httpExchange);
        JsonObject json = gson.fromJson(body, JsonObject.class);

        String bhUri = json.get("bhUri").getAsString();
        String bhAddress = json.get("bhAddress").getAsString();
        String collectorUri = json.get("collectorUri").getAsString();

        setup(bhUri, bhAddress, collectorUri);
        respond(httpExchange, "application/json", 200, jsonResponse("OK", "Setup completed"));
    }

    private void handleSelectData(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        String start = query.split("&")[0].split("=")[1];
        String end = query.split("&")[1].split("=")[1];

        try {
            startDate = LocalDateTime.parse(start).atZone(ZoneId.of("Europe/Berlin"));
            endDate = LocalDateTime.parse(end).atZone(ZoneId.of("Europe/Berlin"));

            checkDates(startDate, endDate);

            data = dataCollector.getServerData(startDate, endDate);
            respond(httpExchange, "application/json", 200, jsonResponse("OK", "Data retrieved"));
        } catch (InvalidParameterException e){
            respond(httpExchange, "application/json", 400, jsonResponse("Bad Request", "Impossible date range. Start date must be before end date"));
        } catch (InterruptedException e) {
            respond(httpExchange, "application/json", 500, jsonResponse("Internal Server Error","Error reaching database"));
        }
    }

    private void handleVerifyData(HttpExchange httpExchange) throws IOException {
        isVerified = verifier.verify(data);
        respond(httpExchange, "application/json", 200, jsonResponse("OK", Boolean.toString(isVerified)));
    }

    private void handleDownloadData(HttpExchange httpExchange) throws IOException {
        List<ServerRecord> filteredData = filterData();
        byte[] csvData = mapper.serverRecordToCsv(filteredData);
        respond(httpExchange, "text/csv", 200, csvData);
    }

    private void handleDownloadReport(HttpExchange httpExchange) throws IOException {
        AuditSummary summary = createSummary();
        byte[] doc = pdfCreator.generateReport(data, summary);
        respond(httpExchange, doc);
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private void setup(String bhUri, String bhAddress, String collectorUri) {
        dataCollector = new DataCollector(collectorUri, mapper);
        BlockchainHandler handler = new BlockchainHandler(bhUri, bhAddress);
        Hasher hasher = new Hasher(30);
        verifier = new Verifier(hasher, handler);
    }


    private AuditSummary createSummary() {
        return new AuditSummary(
                java.util.UUID.randomUUID().toString(),
                "1.0.0",
                "Telemetry-Server-01",
                "Zeitraum: " + startDate + " - " + endDate,
                data.size(),
                isVerified
        );
    }

    private List<ServerRecord> filterData() {
        long from = startDate.toEpochSecond();
        long to = endDate.toEpochSecond();

        return  data.stream()
                        .filter(r -> {
                            long ts = Long.parseLong(r.timestamp());
                            return ts >= from && ts <= to;
                        })
                        .toList();
    }

    private void checkDates(ZonedDateTime startDate, ZonedDateTime endDate) throws InvalidParameterException {
        if(endDate.isBefore(startDate)) {
            throw new InvalidParameterException();
        }
    }

    private void respond(HttpExchange ex, String contentType, int status, byte[] bytes) throws IOException {
        ex.getResponseHeaders().set("Content-Type", contentType);
        ex.sendResponseHeaders(status, bytes.length);
        try (ex; OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void respond(HttpExchange ex, byte[] bytes) throws IOException {
        ex.getResponseHeaders().set("Content-Type", "application/pdf");
        ex.getResponseHeaders().set("Content-Disposition", "attachment; filename=\"audit-report.pdf\"");
        ex.sendResponseHeaders(200, bytes.length);
        try (ex; OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    public void start() {
        server.start();
    }

    @Override
    public void close() {
        server.stop(0);
    }

    private byte[] jsonResponse(String code, String msg) {
        return gson.toJson(new JsonResponse(code, msg)).getBytes(StandardCharsets.UTF_8);
    }

    private record JsonResponse(String error, String message) {}
}