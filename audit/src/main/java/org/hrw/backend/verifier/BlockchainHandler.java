package org.hrw.backend.verifier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hrw.datamodels.HashRecord;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class BlockchainHandler {
    private final String uri;
    private final String address;
    private final ObjectMapper mapper;
    private final HttpClient client;

    public BlockchainHandler(String uri, String address) {
        this.mapper = new ObjectMapper();
        this.uri = uri;
        this.address = address;
        this.client = HttpClient.newHttpClient();
    }

    public List<String> getBlockchainHashes(List<HashRecord> hashedData) {
        List<String> hashes = new ArrayList<>();

        try {
            for (HashRecord hash : hashedData) {
                String singleHash = getSingleHash(hash);
                hashes.add(singleHash);
            }
            return hashes;
        } catch (IOException | InterruptedException e) {
            System.out.println("Retrieving blockchain hashes failed");
            e.printStackTrace();
        }
        return null;
    }

    private String getSingleHash(HashRecord hashData) throws IOException, InterruptedException {
        String timestamp = hashData.timestamp();

        String response = callBlockchain(timestamp);

        System.out.println(response);

        JsonNode root = mapper.readTree(response);

        return root.get("value").asText();
    }

    private String callBlockchain(String timestamp) throws IOException, InterruptedException {
        HttpRequest request = createRequest(timestamp);
        System.out.println("Request created");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response received");
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
