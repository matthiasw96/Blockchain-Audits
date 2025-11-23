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

/**
 * HTTP API for the audit backend.
 *
 * <p>This API exposes endpoints for:</p>
 * <ul>
 *     <li>Serving the single-page frontend (static resources)</li>
 *     <li>Selecting data for a given time interval</li>
 *     <li>Triggering integrity verification against the blockchain</li>
 *     <li>Downloading filtered data as CSV</li>
 *     <li>Downloading a generated audit report as PDF</li>
 * </ul>
 *
 * <p>It combines several core components:</p>
 * <ul>
 *     <li>{@link DataCollector} – retrieves telemetry data from the DatabaseAPI</li>
 *     <li>{@link Verifier} – compares recomputed hashes with blockchain hashes</li>
 *     <li>{@link Converter} – converts internal data structures to CSV</li>
 *     <li>{@link PdfGenerator} – creates PDF-based audit reports</li>
 * </ul>
 */
public class AuditAPI implements AutoCloseable {
    private final DataCollector dataCollector;
    private final Verifier verifier;
    private final Converter converter;
    private final DateTimeFormatter FORMATTER;
    private final HttpServer server;
    private final PdfGenerator pdfCreator;
    private final Gson gson;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private boolean isVerified;
    private List<ServerRecord> data;

    public AuditAPI(AuditAPIBuilder builder) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(8000), 0);
        this.dataCollector = builder.dataCollector;
        this.verifier = builder.verifier;
        this.converter = builder.converter;
        this.FORMATTER = builder.FORMATTER;
        this.gson = new Gson();
        this.pdfCreator = new PdfGenerator(FORMATTER);
        createContexts();
        System.out.println("Server started at localhost:8000/index.html");
    }

    /**
     * Registers all HTTP contexts (routes) for the audit API.
     */
    private void createContexts() {
        server.createContext("/", this::handleStatic);
        server.createContext("/selectData", this::handleSelectData);
        server.createContext("/verifyData", this::handleVerifyData);
        server.createContext("/downloadData", this::handleDownloadData);
        server.createContext("/downloadReport", this::handleDownloadReport);
    }

    /**
     * Serves static frontend resources (HTML, CSS, JS) from the classpath.
     *
     * <p>The requested path is resolved relative to the classpath, and the
     * appropriate {@code Content-Type} is set based on file extension.</p>
     */
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

    /**
     * Handles the {@code /selectData} endpoint.
     *
     * <p>Expected query parameters:</p>
     * <ul>
     *     <li>{@code start} – ISO-formatted timestamp</li>
     *     <li>{@code end} – ISO-formatted timestamp</li>
     * </ul>
     *
     * <p>The method:</p>
     * <ol>
     *     <li>parses the time range</li>
     *     <li>fetches data via {@link DataCollector}</li>
     *     <li>stores it in memory for subsequent operations (verify, download)</li>
     *     <li>returns a simple JSON status response</li>
     * </ol>
     */
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

    /**
     * Handles the {@code /verifyData} endpoint.
     *
     * <p>Uses the previously selected data (from {@link #handleSelectData}) and
     * executes {@link Verifier#verify(List)}. The result ({@code true}/{@code false})
     * is returned as a JSON response.</p>
     */
    private void handleVerifyData(HttpExchange httpExchange) throws IOException {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Verifying data");

        isVerified = verifier.verify(data);
        respond(httpExchange, "application/json", 200, jsonResponse("OK", Boolean.toString(isVerified)));
        httpExchange.close();

        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Verification completed");
    }

    /**
     * Handles the {@code /downloadData} endpoint.
     *
     * <p>Filters the currently loaded data to the selected time range and
     * returns it as a CSV file (semicolon-separated) using the shared
     * {@link Converter}.</p>
     */
    private void handleDownloadData(HttpExchange httpExchange) throws IOException {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Downloading data");

        data = filterData();
        byte[] csvData = converter.serverRecordToCsv(data);
        respond(httpExchange, "text/csv", 200, csvData);
        httpExchange.close();

        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Download completed");
    }


    /**
     * Handles the {@code /downloadReport} endpoint.
     *
     * <p>Filters the currently loaded data, builds an {@link AuditSummary} and
     * generates a PDF report using {@link PdfGenerator}. The PDF is sent as a
     * download attachment.</p>
     */

    private void handleDownloadReport(HttpExchange httpExchange) throws IOException {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Downloading report");

        data = filterData();
        AuditSummary summary = createSummary();
        byte[] doc = pdfCreator.generateReport(data, summary);
        respond(httpExchange, doc);
        httpExchange.close();

        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Download completed");
    }

    /**
     * Simple content type detection based on file extension for static resources.
     */
    private String guessContentType(String resourceName) {
        if (resourceName.endsWith(".html")) return "text/html; charset=utf-8";
        if (resourceName.endsWith(".css"))  return "text/css; charset=utf-8";
        if (resourceName.endsWith(".js"))   return "application/javascript; charset=utf-8";
        return "application/octet-stream";
    }

    /**
     * Builds an {@link AuditSummary} based on the current selection and verification result.
     */
    private AuditSummary createSummary() {
        return new AuditSummary(
                java.util.UUID.randomUUID().toString(),
                "1.0.0",
                dataCollector.getUri(),
                "Zeitraum: " + startDate + " - " + endDate,
                data.size(),
                isVerified
        );
    }

    /**
     * Filters the internally stored data to match the selected time range.
     *
     * @return list of records whose timestamps lie between {@code startDate} and {@code endDate}
     */
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

    /**
     * Sends a generic HTTP response with configurable content type and status code.
     */
    private void respond(HttpExchange ex, String contentType, int status, byte[] bytes) throws IOException {
        ex.getResponseHeaders().set("Content-Type", contentType);
        ex.sendResponseHeaders(status, bytes.length);
        try (ex; OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    /**
     * Sends the PDF report as a downloadable file response.
     */
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

    /**
     * Creates a small JSON status/message object and returns it as UTF-8 bytes.
     *
     * @param code status code (e.g. {@code "OK"}, {@code "ERROR"})
     * @param msg human-readable message
     * @return UTF-8 encoded JSON response
     */
    private byte[] jsonResponse(String code, String msg) {
        return gson.toJson(new JsonResponse(code, msg)).getBytes(StandardCharsets.UTF_8);
    }

    private record JsonResponse(String status, String message) {}

    /**
     * Builder for {@link AuditAPI} instances.
     *
     * <p>Allows configuring data collector, verifier, converter and formatting.</p>
     */
    public static class AuditAPIBuilder {
        private DataCollector dataCollector;
        private Verifier verifier;
        private Converter converter;
        private DateTimeFormatter FORMATTER;

        public AuditAPIBuilder setDataCollector(DataCollector dataCollector) {
            this.dataCollector = dataCollector;
            return this;
        }

        public AuditAPIBuilder setVerifier(Verifier verifier) {
            this.verifier = verifier;
            return this;
        }

        public AuditAPIBuilder setConverter(Converter converter) {
            this.converter = converter;
            return this;
        }

        public AuditAPIBuilder setFormat(DateTimeFormatter formatter) {
            this.FORMATTER = formatter;
            return this;
        }

        public AuditAPI build() throws IOException { return new AuditAPI(this);}
    }
}