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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Converts raw monitoring data between different representations and the
 * internal domain model ({@link ServerRecord}, {@link VMRecord}).
 *
 * <p>Supported conversions include:</p>
 * <ul>
 *     <li>XML (RRD updates) → {@link ServerRecord}</li>
 *     <li>JDBC {@link ResultSet} → {@link ServerRecord}</li>
 *     <li>JSON → {@link ServerRecord}</li>
 *     <li>{@link ServerRecord} → CSV byte array</li>
 * </ul>
 *
 * <p>The converter uses a mapping file (e.g. {@code map.csv}) to translate raw
 * legend names from the monitoring output into canonical column names used by
 * the application and database.</p>
 */
public class Converter {
    private final Map<String,String> map;
    private final ObjectMapper mapper;
    private final Path path;

    public Converter(String path) {
        this.path = Path.of(path);
        this.map = this.createColumnMap();
        this.mapper = new ObjectMapper();
    }

    /**
     * Converts an XML document containing monitoring data into a list of
     * {@link ServerRecord} objects.
     *
     * <p>The method:</p>
     * <ol>
     *     <li>extracts the data node and its rows,</li>
     *     <li>builds a legend list using the mapping file,</li>
     *     <li>maps each XML row to a {@link ServerRecord},</li>
     *     <li>reverses the list so data is ordered chronologically.</li>
     * </ol>
     *
     * @param xmlDoc source XML document
     * @return list of converted server records
     * @throws XPathExpressionException if the XPath queries fail
     */
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

    /**
     * Converts a JDBC {@link ResultSet} into a list of {@link ServerRecord} objects.
     *
     * <p>The method expects columns matching the flattened schema of
     * {@link ServerRecord} and {@link VMRecord} (e.g. {@code vm2_cpu_avg}, ...).</p>
     *
     * @param resultSet query result containing server metrics
     * @return list of server records constructed from the result set
     * @throws SQLException if column access fails
     */
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

    /**
     * Converts a JSON payload into a list of {@link ServerRecord} objects.
     *
     * <p>The JSON is expected to contain a top-level {@code data} array, whose
     * elements can be mapped directly to {@link ServerRecord}.</p>
     *
     * @param rawBody JSON string containing the data array
     * @return list of server records
     * @throws JsonProcessingException if parsing or mapping fails
     */
    public List<ServerRecord> jsonToServerRecord(String rawBody) throws JsonProcessingException {
        List<ServerRecord> serverData = new ArrayList<>();

        JsonNode root = mapper.readTree(rawBody);
        ArrayNode data = (ArrayNode) root.get("data");

        for(JsonNode node : data) {
            serverData.add(this.mapper.treeToValue(node, ServerRecord.class));
        }

        return serverData;
    }

    /**
     * Converts a list of {@link ServerRecord} objects into a CSV export.
     *
     * <p>The first line contains the attribute names derived from
     * {@link ServerRecord#getAttributeNames()}, followed by one line per
     * record. Commas are replaced by semicolons to avoid issues with
     * regional settings for csv files.</p>
     *
     * @param filteredData list of records to export
     * @return CSV content as UTF-8 encoded byte array
     */
    public byte[] serverRecordToCsv(List<ServerRecord> filteredData) {
        String csv = filteredData.getFirst().getAttributeNames() + "\n";

        for(ServerRecord record : filteredData) {
            csv += record.toString() + "\n";
        }

        return csv.replaceAll(",",";").getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Creates the legend list from the XML document by mapping raw legend names
     * to canonical names using the mapping file.
     *
     * @param sourceXml XML document containing a {@code legend} node
     * @return ordered list of canonical column names
     * @throws XPathExpressionException if XPath evaluation fails
     */
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

    /**
     * Maps a single XML row (representing one timestamp) into a {@link ServerRecord}.
     *
     * @param row node list representing the data row
     * @param legendList canonical names corresponding to the value positions
     * @return server record with VM metrics extracted from the row
     */
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

    /**
     * Extracts all metrics for a given VM from an XML row and constructs
     * a corresponding {@link VMRecord}.
     *
     * <p>Internally, this method uses regex filters on the legend list to
     * locate all relevant columns (CPU, memory, network, disk).</p>
     *
     * @param row XML row values
     * @param legendList canonical legend names
     * @param vm VM identifier (e.g. 2, 3, 4, 5)
     * @return VM record with aggregated metrics
     */
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

    /**
     * Extracts numeric values from an XML row based on a regex applied to
     * the legend list.
     *
     * @param regex regular expression to match legend entries
     * @param legendList canonical legend names in order
     * @param row XML row node list
     * @return list of parsed double values for all matching columns
     */
    private List<Double> extractValues(String regex, List<String> legendList, NodeList row) {
        return IntStream.range(0, legendList.size())
                .filter(i -> Pattern.matches(regex, legendList.get(i)))
                .map(i -> i + 1)
                .mapToObj(row::item)
                .map(Node::getTextContent)
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    /**
     * Reconstructs a {@link VMRecord} from a JDBC {@link ResultSet} for a
     * specific VM prefix (e.g. {@code vm2}, {@code vm3}, ...).
     *
     * @param resultSet SQL result set containing flattened VM metrics
     * @param vm VM identifier (used for column name prefixes)
     * @return reconstructed VM record
     * @throws SQLException if column access fails
     */
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

    /**
     * Loads the mapping from raw legend names to canonical names from the
     * configured mapping file.
     *
     * @return a map from raw to canonical column names, or {@code null} if loading fails
     */
    private Map<String, String> createColumnMap() {
        try (InputStream in = Files.newInputStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            return readLines(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads all lines from the mapping file and builds a lookup map.
     *     *
     * @param reader buffered reader over the mapping file
     * @return map containing raw → canonical name mappings
     * @throws IOException if reading fails
     */
    private static Map<String, String> readLines(BufferedReader reader) throws IOException {
        Map<String, String> serverMap = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            serverMap.put(parts[0].trim(), parts[1].trim());
        }
        return serverMap;
    }
}