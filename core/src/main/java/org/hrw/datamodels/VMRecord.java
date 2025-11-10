package org.hrw.datamodels;

public record VMRecord(
        int id,
        double cpu_avg,
        double cpu_max,
        double memory,
        double net_rx_total,
        double net_tx_total,
        double disk_iops_total,
        double disk_throughput_total,
        double disk_latency_avg
) {

    public String toString() {
        return  cpu_avg + "," +
                cpu_max + "," +
                memory + "," +
                net_rx_total + "," +
                net_tx_total + "," +
                disk_iops_total + "," +
                disk_throughput_total + "," +
                disk_latency_avg;
    }

    public String getAttributeNames() {
         return "vm" + id + "_cpu_avg," +
                "vm" + id + "_cpu_max," +
                "vm" + id + "_memory," +
                "vm" + id + "_net_rx_total," +
                "vm" + id + "_net_tx_total," +
                "vm" + id + "_disk_iops_total," +
                "vm" + id + "_disk_throughput_total," +
                "vm" + id + "_disk_latency_avg";
    }
}
