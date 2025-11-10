package org.hrw.datamodels;

public record ServerRecord(
        String timestamp,
        double vm2_cpu_avg,
        double vm2_cpu_max,
        double vm2_memory,
        double vm2_net_rx_total,
        double vm2_net_tx_total,
        double vm2_disk_iops_total,
        double vm2_disk_throughput_total,
        double vm2_disk_latency_avg,
        double vm3_cpu_avg,
        double vm3_cpu_max,
        double vm3_memory,
        double vm3_net_rx_total,
        double vm3_net_tx_total,
        double vm3_disk_iops_total,
        double vm3_disk_throughput_total,
        double vm3_disk_latency_avg) implements Datastructure  {

    public String getAttributeNames() {
        return "timestamp," +
                "vm2_cpu_avg," +
                "vm2_cpu_max," +
                "vm2_memory," +
                "vm2_net_rx_total," +
                "vm2_net_tx_total," +
                "vm2_disk_iops_total," +
                "vm2_disk_throughput_total," +
                "vm2_disk_latency_avg," +
                "vm3_cpu_avg," +
                "vm3_cpu_max," +
                "vm3_memory," +
                "vm3_net_rx_total," +
                "vm3_net_tx_total," +
                "vm3_disk_iops_total," +
                "vm3_disk_throughput_total," +
                "vm3_disk_latency_avg";
    }

    @Override
    public String toString() {
        return timestamp + "," +
                vm2_cpu_avg + "," +
                vm2_cpu_max + "," +
                vm2_memory + "," +
                vm2_net_rx_total + "," +
                vm2_net_tx_total + "," +
                vm2_disk_iops_total + "," +
                vm2_disk_throughput_total + "," +
                vm2_disk_latency_avg + "," +
                vm3_cpu_avg + "," +
                vm3_cpu_max + "," +
                vm3_memory + "," +
                vm3_net_rx_total + "," +
                vm3_net_tx_total + "," +
                vm3_disk_iops_total + "," +
                vm3_disk_throughput_total + "," +
                vm3_disk_latency_avg;
    }
}
