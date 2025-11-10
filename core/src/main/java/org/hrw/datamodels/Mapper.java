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

    public List<ServerRecord> resultSetToServerData(ResultSet resultSet) throws SQLException {
        List<ServerRecord> serverData = new ArrayList<>();

        while (resultSet.next()) {
            ServerRecord record = new ServerRecord(
                    resultSet.getString("timestamp"),
                    resultSet.getDouble("vm2_cpu_avg"),
                    resultSet.getDouble("vm2_cpu_max"),
                    resultSet.getDouble("vm2_memory"),
                    resultSet.getDouble("vm2_net_rx_total"),
                    resultSet.getDouble("vm2_net_tx_total"),
                    resultSet.getDouble("vm2_disk_iops_total"),
                    resultSet.getDouble("vm2_disk_throughput_total"),
                    resultSet.getDouble("vm2_disk_latency_avg"),
                    resultSet.getDouble("vm3_cpu_avg"),
                    resultSet.getDouble("vm3_cpu_max"),
                    resultSet.getDouble("vm3_memory"),
                    resultSet.getDouble("vm3_net_rx_total"),
                    resultSet.getDouble("vm3_net_tx_total"),
                    resultSet.getDouble("vm3_disk_iops_total"),
                    resultSet.getDouble("vm3_disk_throughput_total"),
                    resultSet.getDouble("vm3_disk_latency_avg")
            );

            serverData.add(record);

        }
        return serverData;
    }

    public List<ServerRecord> xmlToServerData(Document xmlDoc) throws XPathExpressionException {
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

            // VM2
            List<Double> vm2_cpu = List.of(
                    Double.parseDouble(row.item(legendList.indexOf("vm2_cpu0")+1).getTextContent()),
                    Double.parseDouble(row.item(legendList.indexOf("vm2_cpu1")+1).getTextContent()),
                    Double.parseDouble(row.item(legendList.indexOf("vm2_cpu2")+1).getTextContent()),
                    Double.parseDouble(row.item(legendList.indexOf("vm2_cpu3")+1).getTextContent())
            );

            double vm2_cpu_avg = vm2_cpu.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
            double vm2_cpu_max = vm2_cpu.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);

            double vm2_memory = Double.parseDouble(row.item(legendList.indexOf("vm2_memory")+1).getTextContent());

            double vm2_net_rx_total = Double.parseDouble(row.item(legendList.indexOf("vm2_vif_1_rx")+1).getTextContent());
            double vm2_net_tx_total = Double.parseDouble(row.item(legendList.indexOf("vm2_vif_1_tx")+1).getTextContent());

            List<Double> vm2_disk_iops = List.of(
                    Double.parseDouble(row.item(legendList.indexOf("vm2_vbd_xvda_iops_total")+1).getTextContent()),
                    Double.parseDouble(row.item(legendList.indexOf("vm2_vbd_xvdd_iops_total")+1).getTextContent())
            );

            double vm2_disk_iops_total = vm2_disk_iops.stream().mapToDouble(Double::doubleValue).sum();

            List<Double> vm2_disk_throughput = List.of(
                    Double.parseDouble(row.item(legendList.indexOf("vm2_vbd_xvda_io_throughput_total")+1).getTextContent()),
                    Double.parseDouble(row.item(legendList.indexOf("vm2_vbd_xvdd_io_throughput_total")+1).getTextContent())
            );

            double vm2_disk_throughput_total = vm2_disk_throughput.stream().mapToDouble(Double::doubleValue).sum();

            List<Double> vm2_disk_latency = List.of(
                    Double.parseDouble(row.item(legendList.indexOf("vm2_vbd_xvda_latency")+1).getTextContent()),
                    Double.parseDouble(row.item(legendList.indexOf("vm2_vbd_xvdd_latency")+1).getTextContent())
            );

            double vm2_disk_latency_avg = vm2_disk_latency.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);

        //VM3
        List<Double> vm3_cpu = List.of(
                Double.parseDouble(row.item(legendList.indexOf("vm3_cpu0")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_cpu1")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_cpu2")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_cpu3")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_cpu4")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_cpu5")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_cpu6")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_cpu7")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_cpu8")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_cpu9")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_cpu10")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_cpu11")+1).getTextContent())
        );

        double vm3_cpu_avg = vm3_cpu.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        double vm3_cpu_max = vm3_cpu.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);

        double vm3_memory = Double.parseDouble(row.item(legendList.indexOf("vm3_memory")+1).getTextContent());

        double vm3_net_rx_total = Double.parseDouble(row.item(legendList.indexOf("vm3_vif_0_rx")+1).getTextContent());
        double vm3_net_tx_total = Double.parseDouble(row.item(legendList.indexOf("vm3_vif_0_tx")+1).getTextContent());

        List<Double> vm3_disk_iops = List.of(
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvda_iops_total")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvdb_iops_total")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvdc_iops_total")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvde_iops_total")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvdf_iops_total")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvdg_iops_total")+1).getTextContent())
        );

        double vm3_disk_iops_total = vm3_disk_iops.stream().mapToDouble(Double::doubleValue).sum();

        List<Double> vm3_disk_throughput = List.of(
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvda_io_throughput_total")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvdb_io_throughput_total")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvdc_io_throughput_total")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvde_io_throughput_total")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvdf_io_throughput_total")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvdg_io_throughput_total")+1).getTextContent())
        );

        double vm3_disk_throughput_total = vm3_disk_throughput.stream().mapToDouble(Double::doubleValue).sum();

        List<Double> vm3_disk_latency = List.of(
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvda_latency")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvdb_latency")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvdc_latency")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvde_latency")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvdf_latency")+1).getTextContent()),
                Double.parseDouble(row.item(legendList.indexOf("vm3_vbd_xvdg_latency")+1).getTextContent())
        );

        double vm3_disk_latency_avg = vm3_disk_latency.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);

            return new ServerRecord(
                    timestamp,
                    vm2_cpu_avg,
                    vm2_cpu_max,
                    vm2_memory,
                    vm2_net_rx_total,
                    vm2_net_tx_total,
                    vm2_disk_iops_total,
                    vm2_disk_throughput_total,
                    vm2_disk_latency_avg,
                    vm3_cpu_avg,
                    vm3_cpu_max,
                    vm3_memory,
                    vm3_net_rx_total,
                    vm3_net_tx_total,
                    vm3_disk_iops_total,
                    vm3_disk_throughput_total,
                    vm3_disk_latency_avg
            );
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
}
