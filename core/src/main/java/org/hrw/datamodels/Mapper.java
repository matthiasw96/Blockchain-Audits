package org.hrw.datamodels;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Mapper {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private final Map<String,String> map;
    private final ObjectMapper mapper;

    public Mapper() {
        this.map = this.createColumnMap();
        this.mapper = new ObjectMapper();
    }

    private Map<String, String> createColumnMap() {
        try (InputStream in = Mapper.class.getResourceAsStream("/map.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            Map<String, String> serverMap = readLines(reader);

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Loaded " + serverMap.size() + " entries");

            return serverMap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Map<String, String> readLines(BufferedReader reader) throws IOException {
        Map<String, String> serverMap = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            serverMap.put(parts[0].trim(), parts[1].trim());
        }
        return serverMap;
    }

    public List<ServerData> resultSetToServerData(ResultSet resultSet) throws SQLException {
        List<ServerData> serverData = new ArrayList<>();
        List<String> columnNames = this.getColumnNames(resultSet);

        while (resultSet.next()) {
            Map<String,String> map = this.createMap(columnNames, resultSet);
            serverData.add(new ServerData(map));
        }
        return serverData;
    }

    private Map<String, String> createMap(List<String> columnNames, ResultSet resultSet) throws SQLException {
        Map<String, String> map = new HashMap<>();

        for(String columnName : columnNames) {
            map.put(columnName, resultSet.getString(columnName));
        }

        return map;
    }

    private List<String> getColumnNames(ResultSet resultSet) throws SQLException {
        List<String> columnNames = new ArrayList<>();

        for(int i=1; i<resultSet.getMetaData().getColumnCount(); i++) {
            columnNames.add(resultSet.getMetaData().getColumnName(i));
        }

        return columnNames;
    }

    public List<ServerData> xmlToServerData(Document xmlDoc) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node data = (Node) xPath.evaluate("//data", xmlDoc, XPathConstants.NODE);
        Node legend = (Node) xPath.evaluate("//legend", xmlDoc, XPathConstants.NODE);
        NodeList rows = data.getChildNodes();
        NodeList columns = legend.getChildNodes();

        List<ServerData> unpackedData = new ArrayList<>();

        for(int i=0; i<rows.getLength(); i++){
            Map<String, String> serverMap = new HashMap<>();

            NodeList rowEntries = rows.item(i).getChildNodes();
            serverMap.put("timestamp", rowEntries.item(0).getTextContent());

            for(int j=1; j<rowEntries.getLength(); j++){
                if(map.containsKey(columns.item(j-1).getTextContent())){
                    String entry = rowEntries.item(j).getTextContent();
                    String column_name = map.get(columns.item(j-1).getTextContent());
                    serverMap.put(column_name, entry);
                }
            }
            ServerData serverData = new ServerData(serverMap);
            unpackedData.add(serverData);
        }

        Collections.reverse(unpackedData);

        return unpackedData;
    }

    public List<ServerData> jsonToServerData(String rawBody) throws JsonProcessingException {
        List<ServerData> serverData = new ArrayList<>();

        JsonNode root = mapper.readTree(rawBody);
        ArrayNode data = (ArrayNode) root.get("data");

        for(JsonNode node : data) {
            Map<String,String> serverMap = new HashMap<>();

            node.fields().forEachRemaining(entry -> {
                serverMap.put(entry.getKey(), entry.getValue().asText());
            });

            serverData.add(new ServerData(serverMap));
        }

        return serverData;
    }
}
