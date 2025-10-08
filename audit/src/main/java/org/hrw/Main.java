package org.hrw;

import org.hrw.backend.datacollector.DataCollector;
import org.hrw.datamodels.ServerData;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        LocalDateTime start = LocalDateTime.of(2025,10,6,0,0);
        LocalDateTime end = LocalDateTime.of(2025,10,6,23,30);

        HttpClient client = HttpClient.newHttpClient();
        String uri = "localhost";
        int port = 8080;


        DataCollector collector = new DataCollector.DataCollectorBuilder()
                .setClient(client)
                .setUri(uri)
                .setPort(port)
                .build();
        try {
            List<ServerData> data = collector.getServerData(start, end);

            for(ServerData sd : data) {
                System.out.println(sd);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}