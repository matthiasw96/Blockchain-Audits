package org.hrw.backend.datacollector;

import org.hrw.datamodels.Mapper;
import org.hrw.datamodels.ServerData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

public class DataCollector {
    private final HttpClient client;
    private final String uri;
    private final int port;
    private final Mapper mapper;

    public DataCollector(DataCollectorBuilder builder) {
        this.client = builder.client;
        this.uri = builder.uri;
        this.port = builder.port;
        this.mapper = new Mapper();
    }

    public List<ServerData> getServerData(LocalDateTime start, LocalDateTime end) throws IOException, InterruptedException {
        System.out.println("Retrieving server data...");
        String rawBody = this.callDatabase(start, end);
        System.out.println(rawBody);
        return this.mapper.jsonToServerData(rawBody);
    }

    private String callDatabase(LocalDateTime start, LocalDateTime end) throws IOException, InterruptedException {
        HttpRequest request = this.createRequest(start, end);
        System.out.println("Request created");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response received");
        return response.body();
    }

    private HttpRequest createRequest(LocalDateTime start, LocalDateTime end) {
        String url = "http://"+uri+":"+port+"/selectData?startDate="+start+"&endDate="+end;
        System.out.println(url);
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }

    public static class DataCollectorBuilder {
        HttpClient client;
        String uri;
        int port;

        public DataCollectorBuilder setClient(HttpClient client) {
            this.client = client;
            return this;
        }

        public DataCollectorBuilder setUri(String uri) {
            this.uri = uri;
            return this;
        }

        public DataCollectorBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        public DataCollector build() {
            return new DataCollector(this);
        }
    }
}
