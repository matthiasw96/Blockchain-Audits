package org.hrw.backend.datacollector;

import org.hrw.datamodels.Mapper;
import org.hrw.datamodels.ServerRecord;

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

    public DataCollector(String uri, int port) {
        this.client = HttpClient.newHttpClient();
        this.uri = uri;
        this.port = port;
        this.mapper = new Mapper();
    }

    public List<ServerRecord> getServerData(LocalDateTime start, LocalDateTime end) throws IOException, InterruptedException {
        System.out.println("Retrieving server data...");
        String rawBody = this.callDatabase(start, end);
        return this.mapper.jsonToServerRecord(rawBody);
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
}
