package org.hrw.backend.datacollector;

import org.hrw.mapping.Converter;
import org.hrw.datamodels.ServerRecord;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Fetches server telemetry data for the audit process by querying the
 * DatabaseAPI running on the data collection system.
 *
 * <p>The data is requested for a defined time range using HTTP GET parameters
 * and then converted from JSON into {@link ServerRecord} instances via the
 * shared {@link Converter} class.</p>
 */
public class DataCollector {
    private final HttpClient client;
    private final String uri;
    private final Converter converter;
    private final DateTimeFormatter FORMATTER;
    private final int port;

    public DataCollector(DataCollectorBuilder builder) {
        this.client = HttpClient.newHttpClient();
        this.uri = builder.uri;
        this.converter = builder.converter;
        this.FORMATTER = builder.FORMATTER;
        this.port = builder.port;
    }

    /**
     * Retrieves telemetry data for the given time range from the external DatabaseAPI.
     *
     * <p>The method sends an HTTP GET request with {@code startDate} and
     * {@code endDate} parameters and converts the returned JSON structure
     * into a list of {@link ServerRecord} objects.</p>
     *
     * @param start start of the requested time range (inclusive)
     * @param end end of the requested time range (inclusive)
     * @return list of server records in the given time window
     * @throws IOException if the HTTP call fails
     * @throws InterruptedException if the request is interrupted
     */
    public List<ServerRecord> getServerData(ZonedDateTime start, ZonedDateTime end) throws IOException, InterruptedException {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Retrieving Server Data");

        String rawBody = callDatabase(start, end);
        List<ServerRecord> serverData = converter.jsonToServerRecord(rawBody);

        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Server Data received");
        return serverData;
    }

    /**
     * Sends the HTTP request to the DatabaseAPI and returns the raw JSON response.
     *
     * @param start start timestamp for the query
     * @param end end timestamp for the query
     * @return JSON response from the DatabaseAPI as string
     */
    private String callDatabase(ZonedDateTime start, ZonedDateTime end) throws IOException, InterruptedException {
        HttpRequest request = createRequest(start, end);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Builds an HTTP GET request with formatted {@code startDate} and {@code endDate}
     * parameters.
     *
     * @param start start timestamp
     * @param end end timestamp
     * @return configured HTTP request instance
     */
    private HttpRequest createRequest(ZonedDateTime start, ZonedDateTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String url = "http://"+uri+":"+port+"/selectData?startDate="+start.format(formatter)+"&endDate="+end.format(formatter);
        System.out.println(url);
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }

    public String getUri() { return uri; }

    /**
     * Builder for {@link DataCollector} instances.
     *
     * <p>Allows configuring endpoint URI, port, converter and formatting.</p>
     */
    public static class DataCollectorBuilder {
        private String uri;
        private Converter converter;
        private DateTimeFormatter FORMATTER;
        private int port;

        public DataCollectorBuilder setUri(String uri) {
            this.uri = uri;
            return this;
        }

        public DataCollectorBuilder setConverter(Converter converter) {
            this.converter = converter;
            return this;
        }

        public DataCollectorBuilder setFormatter(DateTimeFormatter formatter) {
            this.FORMATTER = formatter;
            return this;
        }

        public DataCollectorBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        public DataCollector build() { return new DataCollector(this); }
    }
}
