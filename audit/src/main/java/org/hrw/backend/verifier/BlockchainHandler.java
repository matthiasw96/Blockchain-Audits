package org.hrw.backend.verifier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hrw.datamodels.HashRecord;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BlockchainHandler {
    private final String uri;
    private final String address;
    private final ObjectMapper mapper;
    private final HttpClient client;
    private final DateTimeFormatter FORMATTER;

    public BlockchainHandler(String uri, String address, DateTimeFormatter FORMATTER) {
        this.mapper = new ObjectMapper();
        this.uri = uri;
        this.address = address;
        this.client = HttpClient.newHttpClient();
        this.FORMATTER = FORMATTER;
    }

    public List<String> getBlockchainHashes(List<HashRecord> hashedData) {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Retrieving Blockchain Hashes");
        List<String> hashes = new ArrayList<>();

        try {
            for (HashRecord hash : hashedData) {
                String singleHash = getSingleHash(hash);
                hashes.add(singleHash);
            }

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Blockchain Hashes received");

            return hashes;
        } catch (IOException | InterruptedException e) {
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Retrieving blockchain hashes failed");
            e.printStackTrace();
        }
        return null;
    }

    private String getSingleHash(HashRecord hashData) throws IOException, InterruptedException {
        String timestamp = hashData.timestamp();
        String response = callBlockchain(timestamp);

        JsonNode root = mapper.readTree(response);
        return root.get("value").asText();
    }

    private String callBlockchain(String timestamp) throws IOException, InterruptedException {
        HttpRequest request = createRequest(timestamp);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private HttpRequest createRequest(String timestamp) {
        String url = "https://"+uri+"/addresses/data/"+address+"/"+timestamp;
        System.out.println(url);
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }
}
