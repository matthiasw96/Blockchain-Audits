package org.hrw.backend.datacollector;

import org.hrw.mapping.Mapper;
import org.hrw.datamodels.ServerRecord;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DataCollector {
    private final HttpClient client;
    private final String uri;
    private final Mapper mapper;

    public DataCollector(String uri, Mapper mapper) {
        this.client = HttpClient.newHttpClient();
        this.uri = uri;
        this.mapper = mapper;
    }

    public List<ServerRecord> getServerData(ZonedDateTime start, ZonedDateTime end) throws IOException, InterruptedException {
        System.out.println("Retrieving server data...");
        String rawBody = callDatabase(start, end);
        return mapper.jsonToServerRecord(rawBody);
    }

    private String callDatabase(ZonedDateTime start, ZonedDateTime end) throws IOException, InterruptedException {
        HttpRequest request = createRequest(start, end);
        System.out.println("Request created");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response received");
        return response.body();
    }

    private HttpRequest createRequest(ZonedDateTime start, ZonedDateTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String url = "http://"+uri+":8080/selectData?startDate="+start.format(formatter)+"&endDate="+end.format(formatter);
        System.out.println(url);
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }
}
