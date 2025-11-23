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

    public List<ServerRecord> getServerData(ZonedDateTime start, ZonedDateTime end) throws IOException, InterruptedException {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Retrieving Server Data");

        String rawBody = callDatabase(start, end);
        List<ServerRecord> serverData = converter.jsonToServerRecord(rawBody);

        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Server Data received");
        return serverData;
    }

    private String callDatabase(ZonedDateTime start, ZonedDateTime end) throws IOException, InterruptedException {
        HttpRequest request = createRequest(start, end);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

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
