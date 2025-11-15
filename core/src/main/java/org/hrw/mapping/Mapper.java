package org.hrw.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.hrw.datamodels.ServerRecord;
import org.hrw.datamodels.VMRecord;
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
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public List<ServerRecord> resultSetToServerRecord(ResultSet resultSet) throws SQLException {
        List<ServerRecord> serverData = new ArrayList<>();

        while (resultSet.next()) {
            VMRecord vm2 = this.resultSetToVMRecord(resultSet, 2);
            VMRecord vm3 = this.resultSetToVMRecord(resultSet, 3);
            VMRecord vm4 = this.resultSetToVMRecord(resultSet, 4);
            VMRecord vm5 = this.resultSetToVMRecord(resultSet, 5);

            ServerRecord record = new ServerRecord(
                    resultSet.getString("timestamp"),
                    vm2,
                    vm3,
                    vm4,
                    vm5
            );

            serverData.add(record);
        }
        return serverData;
    }


    private VMRecord resultSetToVMRecord(ResultSet resultSet, int vm) throws SQLException {
        double cpu_avg = resultSet.getDouble("vm"+vm+"_cpu_avg");
        double cpu_max = resultSet.getDouble("vm"+vm+"_cpu_max");
        double memory = resultSet.getDouble("vm"+vm+"_memory");
        double net_rx_total = resultSet.getDouble("vm"+vm+"_net_rx_total");
        double net_tx_total = resultSet.getDouble("vm"+vm+"_net_tx_total");
        double disk_iops_total =resultSet.getDouble("vm"+vm+"_disk_iops_total");
        double disk_throughput_total =resultSet.getDouble("vm"+vm+"_disk_throughput_total");
        double disk_latency_avg = resultSet.getDouble("vm"+vm+"_disk_latency_avg");

        return new VMRecord(
                vm,
                cpu_avg,
                cpu_max,
                memory,
                net_rx_total,
                net_tx_total,
                disk_iops_total,
                disk_throughput_total,
                disk_latency_avg
        );
    }

    public List<ServerRecord> xmlToServerRecord(Document xmlDoc) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node data = (Node) xPath.evaluate("//data", xmlDoc, XPathConstants.NODE);
        NodeList rows = data.getChildNodes();

        List<String> legendList = this.createLegendList(xmlDoc);

        List<ServerRecord> unpackedData = new ArrayList<>();

        for(int i=0; i<rows.getLength(); i++){
            NodeList row = rows.item(i).getChildNodes();
            ServerRecord record = this.mapXmlMessage(row, legendList);
            unpackedData.add(record);
        }

        Collections.reverse(unpackedData);

        return unpackedData;
    }

    private ServerRecord mapXmlMessage(NodeList row, List<String> legendList) {
            String timestamp = row.item(0).getTextContent();

            VMRecord vm2 = this.nodeListToVMRecord(row, legendList, 2);
            VMRecord vm3 = this.nodeListToVMRecord(row, legendList, 3);
            VMRecord vm4 = this.nodeListToVMRecord(row, legendList, 4);
            VMRecord vm5 = this.nodeListToVMRecord(row, legendList, 5);

        return new ServerRecord(
                timestamp,
                vm2,
                vm3,
                vm4,
                vm5
            );
    }

    private VMRecord nodeListToVMRecord(NodeList row, List<String> legendList, int vm) {
        List<Double> cpu = this.extractValues("^vm"+vm+"_cpu\\d+$", legendList, row);
        List<Double> memory = this.extractValues("^vm"+vm+"_memory$", legendList, row);
        List<Double> net_rx_total = this.extractValues("^vm"+vm+"_vif_\\d+_rx$", legendList, row);
        List<Double> net_tx_total = this.extractValues("^vm"+vm+"_vif_\\d+_tx$", legendList, row);
        List<Double> disk_iops = this.extractValues("^vm"+vm+"_vbd_xvd[a-z]_iops_total$", legendList, row);
        List<Double> disk_throughput = this.extractValues("^vm"+vm+"_vbd_xvd[a-z]_io_throughput$", legendList, row);
        List<Double> disk_latency = this.extractValues("^vm"+vm+"_vbd_xvd[a-z]_latency$", legendList, row);

        double cpu_avg = cpu.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        double cpu_max = cpu.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);
        double disk_iops_total = disk_iops.stream().mapToDouble(Double::doubleValue).sum();
        double disk_throughput_total = disk_throughput.stream().mapToDouble(Double::doubleValue).sum();
        double disk_latency_avg = disk_latency.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);

        return new VMRecord(
                vm,
                cpu_avg,
                cpu_max,
                memory.getFirst(),
                net_rx_total.getFirst(),
                net_tx_total.getFirst(),
                disk_iops_total,
                disk_throughput_total,
                disk_latency_avg
        );
    }

    private List<Double> extractValues(String regex, List<String> legendList, NodeList row) {
        return IntStream.range(0, legendList.size())
                .filter(i -> Pattern.matches(regex, legendList.get(i)))
                .map(i -> i + 1)
                .mapToObj(row::item)
                .map(Node::getTextContent)
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    private List<String> createLegendList(Document sourceXml) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node legend = (Node) xPath.evaluate("//legend", sourceXml, XPathConstants.NODE);
        NodeList columns = legend.getChildNodes();

        List<String> legendList = new ArrayList<>();

        for(int i=0; i<columns.getLength(); i++){
            legendList.add(this.map.get(columns.item(i).getTextContent()));
        }

        return legendList;
    }

    public List<ServerRecord> jsonToServerRecord(String rawBody) throws JsonProcessingException {
        List<ServerRecord> serverData = new ArrayList<>();

        JsonNode root = mapper.readTree(rawBody);
        ArrayNode data = (ArrayNode) root.get("data");

        for(JsonNode node : data) {
            serverData.add(this.mapper.treeToValue(node, ServerRecord.class));
        }

        return serverData;
    }

    public byte[] serverRecordToCsv(List<ServerRecord> filteredData) {
        String csv = filteredData.getFirst().getAttributeNames() + "\n";

        for(ServerRecord record : filteredData) {
            csv += record.toString() + "\n";
        }

        return csv.replaceAll(",",";").getBytes(StandardCharsets.UTF_8);
    }

}