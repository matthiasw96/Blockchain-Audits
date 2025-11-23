package org.hrw.infrastructure.collector;

import org.hrw.mapping.Converter;
import org.hrw.datamodels.ServerRecord;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Collects telemetry data from a remote server and converts it into
 * {@link ServerRecord} objects.
 *
 * <p>This class retrieves XML-based data from a server endpoint,
 * parses it into a DOM {@link Document}, and delegates conversion to the
 * {@link Converter}. It is used by the processing pipeline to retrieve
 * server metrics before they are hashed and anchored.</p>
 *
 * <p>The collector is configured using a {@link CollectorBuilder} to supply
 * host address, credentials, query parameters and formatting options.</p>
 */
public class Collector {
    private final HttpClient client;
    private final String hostAddress;
    private final String user;
    private final String pass;
    private final int period;
    private final DateTimeFormatter FORMATTER;
    private final Converter converter;

    public Collector(CollectorBuilder builder) {
        this.hostAddress = builder.hostAddress;
        this.user = builder.user;
        this.pass = builder.pass;
        this.period = builder.period;
        this.client = HttpClient.newHttpClient();
        this.FORMATTER = builder.FORMATTER;
        this.converter = builder.converter;
    }

    /**
     * Fetches telemetry data from the configured server, parses it as XML
     * and converts it into a list of {@link ServerRecord}.
     *
     * <p>All failures are logged and result in a {@code null} return value to
     * avoid disrupting the processing pipeline.</p>
     *
     * @return list of parsed server records, or {@code null} if retrieval fails
     */
    public List<ServerRecord> fetch(){
        try {
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Fetching server data...");

            String rawData = getServerData();
            Document xmlDoc = buildXmlDoc(rawData);
            List<ServerRecord> serverData = converter.xmlToServerRecord(xmlDoc);

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Data received");
            return serverData;
        } catch (Exception e) {
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Fetching server data failed");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends an authenticated HTTP request to the server and retrieves the raw XML data.
     *
     * @return the raw server response body as a string
     * @throws IOException          if the request cannot be sent
     * @throws InterruptedException if the request is interrupted
     */
    private String getServerData() throws IOException, InterruptedException {
        HttpRequest request = createRequest();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Builds an authenticated {@link HttpRequest} for fetching server data.
     *
     * @return a prepared HTTP request
     */
    private HttpRequest createRequest() {
        String auth = user+ ":" + pass;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        return HttpRequest.newBuilder()
                .uri(URI.create("http://"+ hostAddress +"/rrd_updates?start=-" + period))
                .header("Authorization", "Basic " + encodedAuth)
                .GET()
                .build();
    }

    /**
     * Parses an XML string into a DOM {@link Document}.
     *
     * @param input the XML content as a string
     * @return DOM representation of the XML
     * @throws ParserConfigurationException if the XML parser cannot be configured
     * @throws IOException                  if parsing fails
     * @throws SAXException                 if the XML is malformed
     */
    private Document buildXmlDoc(String input) throws ParserConfigurationException, IOException, SAXException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(inputStream);
    }

    /**
     * Builder for {@link Collector} instances.
     *
     * <p>Allows configuring server address, credentials, query period,
     * converter implementation and log formatting.</p>
     */
    public static class CollectorBuilder {
        String hostAddress;
        String user;
        String pass;
        int period;
        Converter converter;
        DateTimeFormatter FORMATTER;

        public CollectorBuilder setHostAddress(String hostAddress) {
            this.hostAddress = hostAddress;
            return this;
        }

        public CollectorBuilder setUser(String user) {
            this.user = user;
            return this;
        }

        public CollectorBuilder setPass(String pass) {
            this.pass = pass;
            return this;
        }

        public CollectorBuilder setPeriod(int period) {
            this.period = period;
            return this;
        }

        public CollectorBuilder setConverter(Converter converter) {
            this.converter = converter;
            return this;
        }

        public CollectorBuilder setFormatter(DateTimeFormatter FORMATTER) {
            this.FORMATTER = FORMATTER;
            return this;
        }

        public Collector build() {
            return new Collector(this);
        }
    }
}
