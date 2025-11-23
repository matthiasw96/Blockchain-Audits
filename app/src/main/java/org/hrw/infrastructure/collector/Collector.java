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

    private String getServerData() throws IOException, InterruptedException {
        String auth = user+ ":" + pass;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        HttpRequest request = createRequest(encodedAuth);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private HttpRequest createRequest(String auth) {
        return HttpRequest.newBuilder()
                .uri(URI.create("http://"+ hostAddress +"/rrd_updates?start=-" + period))
                .header("Authorization", "Basic " + auth)
                .GET()
                .build();
    }

    private Document buildXmlDoc(String input) throws ParserConfigurationException, IOException, SAXException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(inputStream);
    }

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
