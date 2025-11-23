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

//TODO: Port datenbank
public class DataCollector {
    private final HttpClient client;
    private final String uri;
    private final Converter converter;
    private final DateTimeFormatter FORMATTER;

    //TODO: Port datenbank
    public DataCollector(String uri, Converter converter, DateTimeFormatter FORMATTER) {
        this.client = HttpClient.newHttpClient();
        this.uri = uri;
        this.converter = converter;
        this.FORMATTER = FORMATTER;
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

    //TODO: Port Datenbank
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
