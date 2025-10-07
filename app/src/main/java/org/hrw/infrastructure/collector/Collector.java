package org.hrw.infrastructure.collector;

import org.hrw.datamodels.Datastructure;
import org.hrw.datamodels.ServerData;
import org.hrw.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Collector {
    private final HttpClient client;
    private final String uri;
    private final String user;
    private final String pass;
    private final int period;
    private final Map<String, String> map;
    private final Logger logger;
    private final DateTimeFormatter FORMATTER;

    public Collector(CollectorBuilder builder) {
        this.uri = builder.uri;
        this.user = builder.user;
        this.pass = builder.pass;
        this.period = builder.period;
        this.map = builder.map;
        this.client = HttpClient.newHttpClient();
        this.logger = builder.logger;
        this.FORMATTER = builder.FORMATTER;
    }

    public List<Datastructure> fetch(UUID jobId) throws SQLException {
        try {
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Fetching server data...");
            String rawData = this.getServerData();

            Document xmlDoc = this.buildXmlDoc(rawData);
            List<Datastructure> serverData = this.createDatastructure(xmlDoc, jobId);

            logger.log(jobId, Logger.Stage.SERVER, Logger.Status.OK, "Fetched server", Map.of(
                    "rows", serverData.size()-1
            ));

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Data received");
            return serverData;
        } catch (Exception e) {
            logger.log(jobId, Logger.Stage.SERVER, Logger.Status.FAIL, "Error", Map.of("error", e.toString()));
            e.printStackTrace();
            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Fetching server data failed");
        }
        return new LinkedList<>();
    }

    private String getServerData() throws IOException, InterruptedException {
        String auth = user+ ":" + pass;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://"+uri+"/rrd_updates?start=-" + period))
                .header("Authorization", "Basic " + encodedAuth)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private Document buildXmlDoc(String input) throws ParserConfigurationException, IOException, SAXException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(inputStream);
    }

    private List<Datastructure> createDatastructure(Document xmlDoc, UUID job_id) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node data = (Node) xPath.evaluate("//data", xmlDoc, XPathConstants.NODE);
        Node legend = (Node) xPath.evaluate("//legend", xmlDoc, XPathConstants.NODE);
        NodeList rows = data.getChildNodes();
        NodeList columns = legend.getChildNodes();

        List<String> columnNames = new ArrayList<>();
        List<Datastructure> unpackedData = new ArrayList<>();

        for(int i = 0; i< columns.getLength(); i++){
            String legendEntry = columns.item(i).getTextContent();
            if(map.containsKey(legendEntry)){
                columnNames.add(map.get(legendEntry));
            }
        }

        for(int i=0; i<rows.getLength(); i++){
            Node row = rows.item(i);
            NodeList rowEntries = row.getChildNodes();
            Map<String, String> record_map = new HashMap<>();

            record_map.put("job_id", job_id.toString());
            record_map.put("timestamp", rowEntries.item(0).getTextContent());

            for(int j=1; j<rowEntries.getLength(); j++){
                if(map.containsKey(columns.item(j-1).getTextContent())){
                    String entry = rowEntries.item(j).getTextContent();
                    String column_name = map.get(columns.item(j-1).getTextContent());
                    record_map.put(column_name, entry);
                }
            }
            Datastructure datastructure = new ServerData(record_map);
            unpackedData.add(datastructure);
        }

        Collections.reverse(unpackedData);

        return unpackedData;
    }

    public static class CollectorBuilder {
        HttpClient client;
        String uri;
        String user;
        String pass;
        int period;
        Map<String, String> map;
        Logger logger;
        DateTimeFormatter FORMATTER;

        public CollectorBuilder setUri(String uri) {
            this.uri = uri;
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

        public CollectorBuilder setMap(Map<String, String> map) {
            this.map = map;
            return this;
        }

        public CollectorBuilder setLogger(Logger logger) {
            this.logger = logger;
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

