package org.hrw.backend.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.hrw.backend.audit.PdfGenerator;
import org.hrw.backend.audit.AuditSummary;
import org.hrw.backend.datacollector.DataCollector;
import org.hrw.backend.verifier.Verifier;
import org.hrw.datamodels.ServerRecord;
import org.hrw.mapping.Converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AuditAPI implements AutoCloseable {
    private final HttpServer server;
    private final DataCollector dataCollector;
    private final Verifier verifier;
    private final Converter converter;
    private final Gson gson;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private boolean isVerified;
    private List<ServerRecord> data;
    private final PdfGenerator pdfCreator;
    private final DateTimeFormatter FORMATTER;

    public AuditAPI(
            DataCollector dataCollector,
            Verifier verifier,
            Converter converter,
            DateTimeFormatter FORMATTER) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(8000), 0);
        this.dataCollector = dataCollector;
        this.verifier = verifier;
        this.converter = converter;
        this.FORMATTER = FORMATTER;
        this.gson = new Gson();
        this.pdfCreator = new PdfGenerator(FORMATTER);
        createContexts();
        System.out.println("Server started at localhost:8000");
    }

    private void createContexts() {
        server.createContext("/", this::handleStatic);
        server.createContext("/selectData", this::handleSelectData);
        server.createContext("/verifyData", this::handleVerifyData);
        server.createContext("/downloadData", this::handleDownloadData);
        server.createContext("/downloadReport", this::handleDownloadReport);
    }

    private void handleStatic(HttpExchange httpExchange) throws IOException {
        String resourceName = httpExchange.getRequestURI().getPath().substring(1);

        InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);

        assert is != null;
        byte[] bytes = is.readAllBytes();
        String contentType = guessContentType(resourceName);

        respond(httpExchange, contentType, 200, bytes);
        is.close();
        httpExchange.close();
    }

    private void handleSelectData(HttpExchange httpExchange) throws IOException {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Selecting Data");

        String query = httpExchange.getRequestURI().getQuery();
        String start = query.split("&")[0].split("=")[1];
        String end = query.split("&")[1].split("=")[1];

        try {
            startDate = LocalDateTime.parse(start).atZone(ZoneId.of("Europe/Berlin"));
            endDate = LocalDateTime.parse(end).atZone(ZoneId.of("Europe/Berlin"));

            data = dataCollector.getServerData(startDate, endDate);
            if(!data.isEmpty()) {
                respond(httpExchange, "application/json", 200, jsonResponse("OK", "Data retrieved"));
            } else {
                respond(httpExchange, "application/json", 200, jsonResponse("ERROR", "No data found"));
            }

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Data selection completed");
        } catch (InterruptedException e) {
            respond(httpExchange, "application/json", 500, jsonResponse("Internal Server Error","Error reaching database"));
        } finally {
            httpExchange.close();
        }
    }

    private void handleVerifyData(HttpExchange httpExchange) throws IOException {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Verifying data");

        isVerified = verifier.verify(data);
        respond(httpExchange, "application/json", 200, jsonResponse("OK", Boolean.toString(isVerified)));
        httpExchange.close();

        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Verification completed");
    }

    private void handleDownloadData(HttpExchange httpExchange) throws IOException {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Downloading data");

        data = filterData();
        byte[] csvData = converter.serverRecordToCsv(data);
        respond(httpExchange, "text/csv", 200, csvData);
        httpExchange.close();

        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Download completed");
    }

    private void handleDownloadReport(HttpExchange httpExchange) throws IOException {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Downloading report");

        data = filterData();
        AuditSummary summary = createSummary();
        byte[] doc = pdfCreator.generateReport(data, summary);
        respond(httpExchange, doc);
        httpExchange.close();

        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Download completed");
    }
    private String guessContentType(String resourceName) {
        if (resourceName.endsWith(".html")) return "text/html; charset=utf-8";
        if (resourceName.endsWith(".css"))  return "text/css; charset=utf-8";
        if (resourceName.endsWith(".js"))   return "application/javascript; charset=utf-8";
        return "application/octet-stream";
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

    private record JsonResponse(String status, String message) {}
}