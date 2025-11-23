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

/**
 * Retrieves anchored hash values from the Waves blockchain for audit verification.
 *
 * <p>Each {@link HashRecord} contains a timestamp that is used as a key
 * for reading the related `DataEntry` from the blockchain node's
 * REST API. The returned value corresponds to the root hash that was
 * anchored at that time.</p>
 *
 * <p>This class is used by the {@code Verifier} to compare on-chain hashes
 * with locally recomputed hashes.</p>
 */
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

    /**
     * Retrieves a list of blockchain-anchored root hashes corresponding to the
     * provided list of {@link HashRecord} timestamps.
     *
     * <p>Each timestamp is used to query the node's data endpoint.</p>
     *
     * @param hashedData locally computed hash records
     * @return list of on-chain hash values, or {@code null} if retrieval fails
     */
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

    /**
     * Retrieves the blockchain hash anchored at the timestamp of a single
     * {@link HashRecord}.
     *
     * @param hashData the local hash entry containing the timestamp
     * @return the value stored in the blockchain data entry
     */
    private String getSingleHash(HashRecord hashData) throws IOException, InterruptedException {
        String timestamp = hashData.timestamp();
        String response = callBlockchain(timestamp);

        JsonNode root = mapper.readTree(response);
        return root.get("value").asText();
    }

    /**
     * Sends the HTTP request to the Waves node and returns the raw JSON response.
     *
     * @param timestamp timestamp key used to retrieve the blockchain entry
     * @return JSON response from the node
     */
    private String callBlockchain(String timestamp) throws IOException, InterruptedException {
        HttpRequest request = createRequest(timestamp);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Builds the HTTP GET request for querying a Waves data entry by timestamp.
     *
     * <p>Constructs a URL for the requests to the Waves API</p>
     *
     * @param timestamp key used for retrieving the anchored root hash
     * @return configured HTTP request
     */
    private HttpRequest createRequest(String timestamp) {
        String url = "https://"+uri+"/addresses/data/"+address+"/"+timestamp;
        System.out.println(url);
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }
}
