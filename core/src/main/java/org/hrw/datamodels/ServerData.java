package org.hrw.datamodels;

import java.util.Map;

public class ServerData implements Datastructure {
    String job_id;
    String timestamp;
    String vm1_cpu0;
    String vm1_cpu1;
    String vm1_cpu10;
    String vm1_cpu11;
    String vm1_cpu12;
    String vm1_cpu13;
    String vm1_cpu14;
    String vm1_cpu15;
    String vm1_cpu2;
    String vm1_cpu3;
    String vm1_cpu4;
    String vm1_cpu5;
    String vm1_cpu6;
    String vm1_cpu7;
    String vm1_cpu8;
    String vm1_cpu9;
    String vm1_memory;
    String tw_mw001_cpu0;
    String tw_mw001_cpu1;
    String tw_mw001_cpu2;
    String tw_mw001_cpu3;
    String tw_mw001_memory;
    String tw_mw001_vbd_xvda_avgqu_sz;
    String tw_mw001_vbd_xvda_inflight;
    String tw_mw001_vbd_xvda_io_throughput_read;
    String tw_mw001_vbd_xvda_io_throughput_total;
    String tw_mw001_vbd_xvda_io_throughput_write;
    String tw_mw001_vbd_xvda_iops_read;
    String tw_mw001_vbd_xvda_iops_total;
    String tw_mw001_vbd_xvda_iops_write;
    String tw_mw001_vbd_xvda_iowait;
    String tw_mw001_vbd_xvda_latency;
    String tw_mw001_vbd_xvda_read;
    String tw_mw001_vbd_xvda_read_latency;
    String tw_mw001_vbd_xvda_write;
    String tw_mw001_vbd_xvda_write_latency;
    String tw_mw001_memory_target;
    String tw_mw001_vbd_xvdd_avgqu_sz;
    String tw_mw001_vbd_xvdd_inflight;
    String tw_mw001_vbd_xvdd_io_throughput_read;
    String tw_mw001_vbd_xvdd_io_throughput_total;
    String tw_mw001_vbd_xvdd_io_throughput_write;
    String tw_mw001_vbd_xvdd_iops_read;
    String tw_mw001_vbd_xvdd_iops_total;
    String tw_mw001_vbd_xvdd_iops_write;
    String tw_mw001_vbd_xvdd_iowait;
    String tw_mw001_vbd_xvdd_latency;
    String tw_mw001_vbd_xvdd_read;
    String tw_mw001_vbd_xvdd_read_latency;
    String tw_mw001_vbd_xvdd_write;
    String tw_mw001_vbd_xvdd_write_latency;
    String tw_mw001_vif_1_rx;
    String tw_mw001_vif_1_tx;
    String ts_mp01_memory;
    String ts_mp01_cpu11;
    String ts_mp01_cpu10;
    String ts_mp01_cpu9;
    String ts_mp01_cpu8;
    String ts_mp01_cpu7;
    String ts_mp01_cpu6;
    String ts_mp01_cpu5;
    String ts_mp01_cpu4;
    String ts_mp01_cpu3;
    String ts_mp01_cpu2;
    String ts_mp01_cpu1;
    String ts_mp01_cpu0;
    String ts_mp01_vif_0_tx;
    String ts_mp01_vif_0_rx;
    String ts_mp01_vbd_xvde_avgqu_sz;
    String ts_mp01_vbd_xvde_latency;
    String ts_mp01_vbd_xvdf_avgqu_sz;
    String ts_mp01_vbd_xvdf_latency;
    String ts_mp01_vbd_xvde_inflight;
    String ts_mp01_vbd_xvde_iowait;
    String ts_mp01_vbd_xvde_iops_total;
    String ts_mp01_vbd_xvde_iops_write;
    String ts_mp01_vbd_xvde_iops_read;
    String ts_mp01_vbd_xvde_io_throughput_total;
    String ts_mp01_vbd_xvde_io_throughput_write;
    String ts_mp01_vbd_xvde_io_throughput_read;
    String ts_mp01_vbd_xvde_write_latency;
    String ts_mp01_vbd_xvde_read_latency;
    String ts_mp01_vbd_xvde_write;
    String ts_mp01_vbd_xvde_read;
    String ts_mp01_vbd_xvdf_inflight;
    String ts_mp01_vbd_xvdf_iowait;
    String ts_mp01_vbd_xvdf_iops_total;
    String ts_mp01_vbd_xvdf_iops_write;
    String ts_mp01_vbd_xvdf_iops_read;
    String ts_mp01_vbd_xvdf_io_throughput_total;
    String ts_mp01_vbd_xvdf_io_throughput_write;
    String ts_mp01_vbd_xvdf_io_throughput_read;
    String ts_mp01_vbd_xvdf_write_latency;
    String ts_mp01_vbd_xvdf_read_latency;
    String ts_mp01_vbd_xvdf_write;
    String ts_mp01_vbd_xvdf_read;
    String ts_mp01_memory_target;
    String ts_mp01_vbd_xvda_avgqu_sz;
    String ts_mp01_vbd_xvda_inflight;
    String ts_mp01_vbd_xvda_io_throughput_read;
    String ts_mp01_vbd_xvda_io_throughput_total;
    String ts_mp01_vbd_xvda_io_throughput_write;
    String ts_mp01_vbd_xvda_iops_read;
    String ts_mp01_vbd_xvda_iops_total;
    String ts_mp01_vbd_xvda_iops_write;
    String ts_mp01_vbd_xvda_iowait;
    String ts_mp01_vbd_xvda_latency;
    String ts_mp01_vbd_xvda_read;
    String ts_mp01_vbd_xvda_read_latency;
    String ts_mp01_vbd_xvda_write;
    String ts_mp01_vbd_xvda_write_latency;
    String ts_mp01_vbd_xvdb_avgqu_sz;
    String ts_mp01_vbd_xvdb_inflight;
    String ts_mp01_vbd_xvdb_io_throughput_read;
    String ts_mp01_vbd_xvdb_io_throughput_total;
    String ts_mp01_vbd_xvdb_io_throughput_write;
    String ts_mp01_vbd_xvdb_iops_read;
    String ts_mp01_vbd_xvdb_iops_total;
    String ts_mp01_vbd_xvdb_iops_write;
    String ts_mp01_vbd_xvdb_iowait;
    String ts_mp01_vbd_xvdb_latency;
    String ts_mp01_vbd_xvdb_read;
    String ts_mp01_vbd_xvdb_read_latency;
    String ts_mp01_vbd_xvdb_write;
    String ts_mp01_vbd_xvdb_write_latency;
    String ts_mp01_vbd_xvdc_avgqu_sz;
    String ts_mp01_vbd_xvdc_inflight;
    String ts_mp01_vbd_xvdc_io_throughput_read;
    String ts_mp01_vbd_xvdc_io_throughput_total;
    String ts_mp01_vbd_xvdc_io_throughput_write;
    String ts_mp01_vbd_xvdc_iops_read;
    String ts_mp01_vbd_xvdc_iops_total;
    String ts_mp01_vbd_xvdc_iops_write;
    String ts_mp01_vbd_xvdc_iowait;
    String ts_mp01_vbd_xvdc_latency;
    String ts_mp01_vbd_xvdc_read;
    String ts_mp01_vbd_xvdc_read_latency;
    String ts_mp01_vbd_xvdc_write;
    String ts_mp01_vbd_xvdc_write_latency;
    String ts_mp01_vbd_xvdg_avgqu_sz;
    String ts_mp01_vbd_xvdg_inflight;
    String ts_mp01_vbd_xvdg_io_throughput_read;
    String ts_mp01_vbd_xvdg_io_throughput_total;
    String ts_mp01_vbd_xvdg_io_throughput_write;
    String ts_mp01_vbd_xvdg_iops_read;
    String ts_mp01_vbd_xvdg_iops_total;
    String ts_mp01_vbd_xvdg_iops_write;
    String ts_mp01_vbd_xvdg_iowait;
    String ts_mp01_vbd_xvdg_latency;
    String ts_mp01_vbd_xvdg_read;
    String ts_mp01_vbd_xvdg_read_latency;
    String ts_mp01_vbd_xvdg_write;
    String ts_mp01_vbd_xvdg_write_latency;
    String ro2_memory;
    String ro2_cpu7;
    String ro2_cpu6;
    String ro2_cpu5;
    String ro2_cpu4;
    String ro2_cpu3;
    String ro2_cpu2;
    String ro2_cpu1;
    String ro2_cpu0;
    String ro2_vif_0_tx;
    String ro2_vif_0_rx;
    String ro2_vbd_xvda_avgqu_sz;
    String ro2_vbd_xvda_latency;
    String ro2_vbd_xvde_avgqu_sz;
    String ro2_vbd_xvde_latency;
    String ro2_vbd_xvdc_avgqu_sz;
    String ro2_vbd_xvdc_latency;
    String ro2_vbd_xvdb_avgqu_sz;
    String ro2_vbd_xvdb_latency;
    String ro2_vbd_xvda_inflight;
    String ro2_vbd_xvda_iowait;
    String ro2_vbd_xvda_iops_total;
    String ro2_vbd_xvda_iops_write;
    String ro2_vbd_xvda_iops_read;
    String ro2_vbd_xvda_io_throughput_total;
    String ro2_vbd_xvda_io_throughput_write;
    String ro2_vbd_xvda_io_throughput_read;
    String ro2_vbd_xvda_write_latency;
    String ro2_vbd_xvda_read_latency;
    String ro2_vbd_xvda_write;
    String ro2_vbd_xvda_read;
    String ro2_vbd_xvde_inflight;
    String ro2_vbd_xvde_iowait;
    String ro2_vbd_xvde_iops_total;
    String ro2_vbd_xvde_iops_write;
    String ro2_vbd_xvde_iops_read;
    String ro2_vbd_xvde_io_throughput_total;
    String ro2_vbd_xvde_io_throughput_write;
    String ro2_vbd_xvde_io_throughput_read;
    String ro2_vbd_xvde_write_latency;
    String ro2_vbd_xvde_read_latency;
    String ro2_vbd_xvde_write;
    String ro2_vbd_xvde_read;
    String ro2_vbd_xvdc_inflight;
    String ro2_vbd_xvdc_iowait;
    String ro2_vbd_xvdc_iops_total;
    String ro2_vbd_xvdc_iops_write;
    String ro2_vbd_xvdc_iops_read;
    String ro2_vbd_xvdc_io_throughput_total;
    String ro2_vbd_xvdc_io_throughput_write;
    String ro2_vbd_xvdc_io_throughput_read;
    String ro2_vbd_xvdc_write_latency;
    String ro2_vbd_xvdc_read_latency;
    String ro2_vbd_xvdc_write;
    String ro2_vbd_xvdc_read;
    String ro2_vbd_xvdb_inflight;
    String ro2_vbd_xvdb_iowait;
    String ro2_vbd_xvdb_iops_total;
    String ro2_vbd_xvdb_iops_write;
    String ro2_vbd_xvdb_iops_read;
    String ro2_vbd_xvdb_io_throughput_total;
    String ro2_vbd_xvdb_io_throughput_write;
    String ro2_vbd_xvdb_io_throughput_read;
    String ro2_vbd_xvdb_write_latency;
    String ro2_vbd_xvdb_read_latency;
    String ro2_vbd_xvdb_write;
    String ro2_vbd_xvdb_read;
    String ro2_memory_internal_free;
    String ro2_memory_target;
    String ro2_vbd_xvdf_avgqu_sz;
    String ro2_vbd_xvdf_inflight;
    String ro2_vbd_xvdf_io_throughput_read;
    String ro2_vbd_xvdf_io_throughput_total;
    String ro2_vbd_xvdf_io_throughput_write;
    String ro2_vbd_xvdf_iops_read;
    String ro2_vbd_xvdf_iops_total;
    String ro2_vbd_xvdf_iops_write;
    String ro2_vbd_xvdf_iowait;
    String ro2_vbd_xvdf_latency;
    String ro2_vbd_xvdf_read;
    String ro2_vbd_xvdf_read_latency;
    String ro2_vbd_xvdf_write;
    String ro2_vbd_xvdf_write_latency;
    String ro2_vbd_xvdg_avgqu_sz;
    String ro2_vbd_xvdg_inflight;
    String ro2_vbd_xvdg_io_throughput_read;
    String ro2_vbd_xvdg_io_throughput_total;
    String ro2_vbd_xvdg_io_throughput_write;
    String ro2_vbd_xvdg_iops_read;
    String ro2_vbd_xvdg_iops_total;
    String ro2_vbd_xvdg_iops_write;
    String ro2_vbd_xvdg_iowait;
    String ro2_vbd_xvdg_latency;
    String ro2_vbd_xvdg_read;
    String ro2_vbd_xvdg_read_latency;
    String ro2_vbd_xvdg_write;
    String ro2_vbd_xvdg_write_latency;
    String tw_cho01_cpu0;
    String tw_cho01_cpu1;
    String tw_cho01_cpu2;
    String tw_cho01_cpu3;
    String tw_cho01_memory;
    String tw_cho01_vbd_xvda_avgqu_sz;
    String tw_cho01_vbd_xvda_inflight;
    String tw_cho01_vbd_xvda_io_throughput_read;
    String tw_cho01_vbd_xvda_io_throughput_total;
    String tw_cho01_vbd_xvda_io_throughput_write;
    String tw_cho01_vbd_xvda_iops_read;
    String tw_cho01_vbd_xvda_iops_total;
    String tw_cho01_vbd_xvda_iops_write;
    String tw_cho01_vbd_xvda_iowait;
    String tw_cho01_vbd_xvda_latency;
    String tw_cho01_vbd_xvda_read;
    String tw_cho01_vbd_xvda_read_latency;
    String tw_cho01_vbd_xvda_write;
    String tw_cho01_vbd_xvda_write_latency;
    String tw_cho01_vif_1_rx;
    String tw_cho01_vif_1_tx;
    String tw_cho01_memory_target;
    String rh2_memory;
    String rh2_cpu31;
    String rh2_cpu30;
    String rh2_cpu29;
    String rh2_cpu28;
    String rh2_cpu27;
    String rh2_cpu26;
    String rh2_cpu25;
    String rh2_cpu24;
    String rh2_cpu23;
    String rh2_cpu22;
    String rh2_cpu21;
    String rh2_cpu20;
    String rh2_cpu19;
    String rh2_cpu18;
    String rh2_cpu17;
    String rh2_cpu16;
    String rh2_cpu15;
    String rh2_cpu14;
    String rh2_cpu13;
    String rh2_cpu12;
    String rh2_cpu11;
    String rh2_cpu10;
    String rh2_cpu9;
    String rh2_cpu8;
    String rh2_cpu7;
    String rh2_cpu6;
    String rh2_cpu5;
    String rh2_cpu4;
    String rh2_cpu3;
    String rh2_cpu2;
    String rh2_cpu1;
    String rh2_cpu0;
    String rh2_vif_1_tx;
    String rh2_vif_1_rx;
    String rh2_vbd_xvdc_avgqu_sz;
    String rh2_vbd_xvdc_latency;
    String rh2_vbd_xvde_avgqu_sz;
    String rh2_vbd_xvde_latency;
    String rh2_vbd_xvdc_inflight;
    String rh2_vbd_xvdc_iowait;
    String rh2_vbd_xvdc_iops_total;
    String rh2_vbd_xvdc_iops_write;
    String rh2_vbd_xvdc_iops_read;
    String rh2_vbd_xvdc_io_throughput_total;
    String rh2_vbd_xvdc_io_throughput_write;
    String rh2_vbd_xvdc_io_throughput_read;
    String rh2_vbd_xvdc_write_latency;
    String rh2_vbd_xvdc_read_latency;
    String rh2_vbd_xvdc_write;
    String rh2_vbd_xvdc_read;
    String rh2_vbd_xvde_inflight;
    String rh2_vbd_xvde_iowait;
    String rh2_vbd_xvde_iops_total;
    String rh2_vbd_xvde_iops_write;
    String rh2_vbd_xvde_iops_read;
    String rh2_vbd_xvde_io_throughput_total;
    String rh2_vbd_xvde_io_throughput_write;
    String rh2_vbd_xvde_io_throughput_read;
    String rh2_vbd_xvde_write_latency;
    String rh2_vbd_xvde_read_latency;
    String rh2_vbd_xvde_write;
    String rh2_vbd_xvde_read;
    String rh2_memory_internal_free;
    String rh2_memory_target;
    String rh2_vbd_xvda_avgqu_sz;
    String rh2_vbd_xvda_inflight;
    String rh2_vbd_xvda_io_throughput_read;
    String rh2_vbd_xvda_io_throughput_total;
    String rh2_vbd_xvda_io_throughput_write;
    String rh2_vbd_xvda_iops_read;
    String rh2_vbd_xvda_iops_total;
    String rh2_vbd_xvda_iops_write;
    String rh2_vbd_xvda_iowait;
    String rh2_vbd_xvda_latency;
    String rh2_vbd_xvda_read;
    String rh2_vbd_xvda_read_latency;
    String rh2_vbd_xvda_write;
    String rh2_vbd_xvda_write_latency;
    String rh2_vbd_xvdb_avgqu_sz;
    String rh2_vbd_xvdb_inflight;
    String rh2_vbd_xvdb_io_throughput_read;
    String rh2_vbd_xvdb_io_throughput_total;
    String rh2_vbd_xvdb_io_throughput_write;
    String rh2_vbd_xvdb_iops_read;
    String rh2_vbd_xvdb_iops_total;
    String rh2_vbd_xvdb_iops_write;
    String rh2_vbd_xvdb_iowait;
    String rh2_vbd_xvdb_latency;
    String rh2_vbd_xvdb_read;
    String rh2_vbd_xvdb_read_latency;
    String rh2_vbd_xvdb_write;
    String rh2_vbd_xvdb_write_latency;
    String rh2_vbd_xvdf_avgqu_sz;
    String rh2_vbd_xvdf_inflight;
    String rh2_vbd_xvdf_io_throughput_read;
    String rh2_vbd_xvdf_io_throughput_total;
    String rh2_vbd_xvdf_io_throughput_write;
    String rh2_vbd_xvdf_iops_read;
    String rh2_vbd_xvdf_iops_total;
    String rh2_vbd_xvdf_iops_write;
    String rh2_vbd_xvdf_iowait;
    String rh2_vbd_xvdf_latency;
    String rh2_vbd_xvdf_read;
    String rh2_vbd_xvdf_read_latency;
    String rh2_vbd_xvdf_write;
    String rh2_vbd_xvdf_write_latency;

    public ServerData(Map<String, String> record_map) {
        this.job_id = record_map.get("job_id");
        this.timestamp = record_map.get("timestamp");
        this.vm1_cpu0 = record_map.get("vm1_cpu0");
        this.vm1_cpu1 = record_map.get("vm1_cpu1");
        this.vm1_cpu10 = record_map.get("vm1_cpu10");
        this.vm1_cpu11 = record_map.get("vm1_cpu11");
        this.vm1_cpu12 = record_map.get("vm1_cpu12");
        this.vm1_cpu13 = record_map.get("vm1_cpu13");
        this.vm1_cpu14 = record_map.get("vm1_cpu14");
        this.vm1_cpu15 = record_map.get("vm1_cpu15");
        this.vm1_cpu2 = record_map.get("vm1_cpu2");
        this.vm1_cpu3 = record_map.get("vm1_cpu3");
        this.vm1_cpu4 = record_map.get("vm1_cpu4");
        this.vm1_cpu5 = record_map.get("vm1_cpu5");
        this.vm1_cpu6 = record_map.get("vm1_cpu6");
        this.vm1_cpu7 = record_map.get("vm1_cpu7");
        this.vm1_cpu8 = record_map.get("vm1_cpu8");
        this.vm1_cpu9 = record_map.get("vm1_cpu9");
        this.vm1_memory = record_map.get("vm1_memory");
        this.tw_mw001_cpu0 = record_map.get("tw_mw001_cpu0");
        this.tw_mw001_cpu1 = record_map.get("tw_mw001_cpu1");
        this.tw_mw001_cpu2 = record_map.get("tw_mw001_cpu2");
        this.tw_mw001_cpu3 = record_map.get("tw_mw001_cpu3");
        this.tw_mw001_memory = record_map.get("tw_mw001_memory");
        this.tw_mw001_vbd_xvda_avgqu_sz = record_map.get("tw_mw001_vbd_xvda_avgqu_sz");
        this.tw_mw001_vbd_xvda_inflight = record_map.get("tw_mw001_vbd_xvda_inflight");
        this.tw_mw001_vbd_xvda_io_throughput_read = record_map.get("tw_mw001_vbd_xvda_io_throughput_read");
        this.tw_mw001_vbd_xvda_io_throughput_total = record_map.get("tw_mw001_vbd_xvda_io_throughput_total");
        this.tw_mw001_vbd_xvda_io_throughput_write = record_map.get("tw_mw001_vbd_xvda_io_throughput_write");
        this.tw_mw001_vbd_xvda_iops_read = record_map.get("tw_mw001_vbd_xvda_iops_read");
        this.tw_mw001_vbd_xvda_iops_total = record_map.get("tw_mw001_vbd_xvda_iops_total");
        this.tw_mw001_vbd_xvda_iops_write = record_map.get("tw_mw001_vbd_xvda_iops_write");
        this.tw_mw001_vbd_xvda_iowait = record_map.get("tw_mw001_vbd_xvda_iowait");
        this.tw_mw001_vbd_xvda_latency = record_map.get("tw_mw001_vbd_xvda_latency");
        this.tw_mw001_vbd_xvda_read = record_map.get("tw_mw001_vbd_xvda_read");
        this.tw_mw001_vbd_xvda_read_latency = record_map.get("tw_mw001_vbd_xvda_read_latency");
        this.tw_mw001_vbd_xvda_write = record_map.get("tw_mw001_vbd_xvda_write");
        this.tw_mw001_vbd_xvda_write_latency = record_map.get("tw_mw001_vbd_xvda_write_latency");
        this.tw_mw001_memory_target = record_map.get("tw_mw001_memory_target");
        this.tw_mw001_vbd_xvdd_avgqu_sz = record_map.get("tw_mw001_vbd_xvdd_avgqu_sz");
        this.tw_mw001_vbd_xvdd_inflight = record_map.get("tw_mw001_vbd_xvdd_inflight");
        this.tw_mw001_vbd_xvdd_io_throughput_read = record_map.get("tw_mw001_vbd_xvdd_io_throughput_read");
        this.tw_mw001_vbd_xvdd_io_throughput_total = record_map.get("tw_mw001_vbd_xvdd_io_throughput_total");
        this.tw_mw001_vbd_xvdd_io_throughput_write = record_map.get("tw_mw001_vbd_xvdd_io_throughput_write");
        this.tw_mw001_vbd_xvdd_iops_read = record_map.get("tw_mw001_vbd_xvdd_iops_read");
        this.tw_mw001_vbd_xvdd_iops_total = record_map.get("tw_mw001_vbd_xvdd_iops_total");
        this.tw_mw001_vbd_xvdd_iops_write = record_map.get("tw_mw001_vbd_xvdd_iops_write");
        this.tw_mw001_vbd_xvdd_iowait = record_map.get("tw_mw001_vbd_xvdd_iowait");
        this.tw_mw001_vbd_xvdd_latency = record_map.get("tw_mw001_vbd_xvdd_latency");
        this.tw_mw001_vbd_xvdd_read = record_map.get("tw_mw001_vbd_xvdd_read");
        this.tw_mw001_vbd_xvdd_read_latency = record_map.get("tw_mw001_vbd_xvdd_read_latency");
        this.tw_mw001_vbd_xvdd_write = record_map.get("tw_mw001_vbd_xvdd_write");
        this.tw_mw001_vbd_xvdd_write_latency = record_map.get("tw_mw001_vbd_xvdd_write_latency");
        this.tw_mw001_vif_1_rx = record_map.get("tw_mw001_vif_1_rx");
        this.tw_mw001_vif_1_tx = record_map.get("tw_mw001_vif_1_tx");
        this.ts_mp01_memory = record_map.get("ts_mp01_memory");
        this.ts_mp01_cpu11 = record_map.get("ts_mp01_cpu11");
        this.ts_mp01_cpu10 = record_map.get("ts_mp01_cpu10");
        this.ts_mp01_cpu9 = record_map.get("ts_mp01_cpu9");
        this.ts_mp01_cpu8 = record_map.get("ts_mp01_cpu8");
        this.ts_mp01_cpu7 = record_map.get("ts_mp01_cpu7");
        this.ts_mp01_cpu6 = record_map.get("ts_mp01_cpu6");
        this.ts_mp01_cpu5 = record_map.get("ts_mp01_cpu5");
        this.ts_mp01_cpu4 = record_map.get("ts_mp01_cpu4");
        this.ts_mp01_cpu3 = record_map.get("ts_mp01_cpu3");
        this.ts_mp01_cpu2 = record_map.get("ts_mp01_cpu2");
        this.ts_mp01_cpu1 = record_map.get("ts_mp01_cpu1");
        this.ts_mp01_cpu0 = record_map.get("ts_mp01_cpu0");
        this.ts_mp01_vif_0_tx = record_map.get("ts_mp01_vif_0_tx");
        this.ts_mp01_vif_0_rx = record_map.get("ts_mp01_vif_0_rx");
        this.ts_mp01_vbd_xvde_avgqu_sz = record_map.get("ts_mp01_vbd_xvde_avgqu_sz");
        this.ts_mp01_vbd_xvde_latency = record_map.get("ts_mp01_vbd_xvde_latency");
        this.ts_mp01_vbd_xvdf_avgqu_sz = record_map.get("ts_mp01_vbd_xvdf_avgqu_sz");
        this.ts_mp01_vbd_xvdf_latency = record_map.get("ts_mp01_vbd_xvdf_latency");
        this.ts_mp01_vbd_xvde_inflight = record_map.get("ts_mp01_vbd_xvde_inflight");
        this.ts_mp01_vbd_xvde_iowait = record_map.get("ts_mp01_vbd_xvde_iowait");
        this.ts_mp01_vbd_xvde_iops_total = record_map.get("ts_mp01_vbd_xvde_iops_total");
        this.ts_mp01_vbd_xvde_iops_write = record_map.get("ts_mp01_vbd_xvde_iops_write");
        this.ts_mp01_vbd_xvde_iops_read = record_map.get("ts_mp01_vbd_xvde_iops_read");
        this.ts_mp01_vbd_xvde_io_throughput_total = record_map.get("ts_mp01_vbd_xvde_io_throughput_total");
        this.ts_mp01_vbd_xvde_io_throughput_write = record_map.get("ts_mp01_vbd_xvde_io_throughput_write");
        this.ts_mp01_vbd_xvde_io_throughput_read = record_map.get("ts_mp01_vbd_xvde_io_throughput_read");
        this.ts_mp01_vbd_xvde_write_latency = record_map.get("ts_mp01_vbd_xvde_write_latency");
        this.ts_mp01_vbd_xvde_read_latency = record_map.get("ts_mp01_vbd_xvde_read_latency");
        this.ts_mp01_vbd_xvde_write = record_map.get("ts_mp01_vbd_xvde_write");
        this.ts_mp01_vbd_xvde_read = record_map.get("ts_mp01_vbd_xvde_read");
        this.ts_mp01_vbd_xvdf_inflight = record_map.get("ts_mp01_vbd_xvdf_inflight");
        this.ts_mp01_vbd_xvdf_iowait = record_map.get("ts_mp01_vbd_xvdf_iowait");
        this.ts_mp01_vbd_xvdf_iops_total = record_map.get("ts_mp01_vbd_xvdf_iops_total");
        this.ts_mp01_vbd_xvdf_iops_write = record_map.get("ts_mp01_vbd_xvdf_iops_write");
        this.ts_mp01_vbd_xvdf_iops_read = record_map.get("ts_mp01_vbd_xvdf_iops_read");
        this.ts_mp01_vbd_xvdf_io_throughput_total = record_map.get("ts_mp01_vbd_xvdf_io_throughput_total");
        this.ts_mp01_vbd_xvdf_io_throughput_write = record_map.get("ts_mp01_vbd_xvdf_io_throughput_write");
        this.ts_mp01_vbd_xvdf_io_throughput_read = record_map.get("ts_mp01_vbd_xvdf_io_throughput_read");
        this.ts_mp01_vbd_xvdf_write_latency = record_map.get("ts_mp01_vbd_xvdf_write_latency");
        this.ts_mp01_vbd_xvdf_read_latency = record_map.get("ts_mp01_vbd_xvdf_read_latency");
        this.ts_mp01_vbd_xvdf_write = record_map.get("ts_mp01_vbd_xvdf_write");
        this.ts_mp01_vbd_xvdf_read = record_map.get("ts_mp01_vbd_xvdf_read");
        this.ts_mp01_memory_target = record_map.get("ts_mp01_memory_target");
        this.ts_mp01_vbd_xvda_avgqu_sz = record_map.get("ts_mp01_vbd_xvda_avgqu_sz");
        this.ts_mp01_vbd_xvda_inflight = record_map.get("ts_mp01_vbd_xvda_inflight");
        this.ts_mp01_vbd_xvda_io_throughput_read = record_map.get("ts_mp01_vbd_xvda_io_throughput_read");
        this.ts_mp01_vbd_xvda_io_throughput_total = record_map.get("ts_mp01_vbd_xvda_io_throughput_total");
        this.ts_mp01_vbd_xvda_io_throughput_write = record_map.get("ts_mp01_vbd_xvda_io_throughput_write");
        this.ts_mp01_vbd_xvda_iops_read = record_map.get("ts_mp01_vbd_xvda_iops_read");
        this.ts_mp01_vbd_xvda_iops_total = record_map.get("ts_mp01_vbd_xvda_iops_total");
        this.ts_mp01_vbd_xvda_iops_write = record_map.get("ts_mp01_vbd_xvda_iops_write");
        this.ts_mp01_vbd_xvda_iowait = record_map.get("ts_mp01_vbd_xvda_iowait");
        this.ts_mp01_vbd_xvda_latency = record_map.get("ts_mp01_vbd_xvda_latency");
        this.ts_mp01_vbd_xvda_read = record_map.get("ts_mp01_vbd_xvda_read");
        this.ts_mp01_vbd_xvda_read_latency = record_map.get("ts_mp01_vbd_xvda_read_latency");
        this.ts_mp01_vbd_xvda_write = record_map.get("ts_mp01_vbd_xvda_write");
        this.ts_mp01_vbd_xvda_write_latency = record_map.get("ts_mp01_vbd_xvda_write_latency");
        this.ts_mp01_vbd_xvdb_avgqu_sz = record_map.get("ts_mp01_vbd_xvdb_avgqu_sz");
        this.ts_mp01_vbd_xvdb_inflight = record_map.get("ts_mp01_vbd_xvdb_inflight");
        this.ts_mp01_vbd_xvdb_io_throughput_read = record_map.get("ts_mp01_vbd_xvdb_io_throughput_read");
        this.ts_mp01_vbd_xvdb_io_throughput_total = record_map.get("ts_mp01_vbd_xvdb_io_throughput_total");
        this.ts_mp01_vbd_xvdb_io_throughput_write = record_map.get("ts_mp01_vbd_xvdb_io_throughput_write");
        this.ts_mp01_vbd_xvdb_iops_read = record_map.get("ts_mp01_vbd_xvdb_iops_read");
        this.ts_mp01_vbd_xvdb_iops_total = record_map.get("ts_mp01_vbd_xvdb_iops_total");
        this.ts_mp01_vbd_xvdb_iops_write = record_map.get("ts_mp01_vbd_xvdb_iops_write");
        this.ts_mp01_vbd_xvdb_iowait = record_map.get("ts_mp01_vbd_xvdb_iowait");
        this.ts_mp01_vbd_xvdb_latency = record_map.get("ts_mp01_vbd_xvdb_latency");
        this.ts_mp01_vbd_xvdb_read = record_map.get("ts_mp01_vbd_xvdb_read");
        this.ts_mp01_vbd_xvdb_read_latency = record_map.get("ts_mp01_vbd_xvdb_read_latency");
        this.ts_mp01_vbd_xvdb_write = record_map.get("ts_mp01_vbd_xvdb_write");
        this.ts_mp01_vbd_xvdb_write_latency = record_map.get("ts_mp01_vbd_xvdb_write_latency");
        this.ts_mp01_vbd_xvdc_avgqu_sz = record_map.get("ts_mp01_vbd_xvdc_avgqu_sz");
        this.ts_mp01_vbd_xvdc_inflight = record_map.get("ts_mp01_vbd_xvdc_inflight");
        this.ts_mp01_vbd_xvdc_io_throughput_read = record_map.get("ts_mp01_vbd_xvdc_io_throughput_read");
        this.ts_mp01_vbd_xvdc_io_throughput_total = record_map.get("ts_mp01_vbd_xvdc_io_throughput_total");
        this.ts_mp01_vbd_xvdc_io_throughput_write = record_map.get("ts_mp01_vbd_xvdc_io_throughput_write");
        this.ts_mp01_vbd_xvdc_iops_read = record_map.get("ts_mp01_vbd_xvdc_iops_read");
        this.ts_mp01_vbd_xvdc_iops_total = record_map.get("ts_mp01_vbd_xvdc_iops_total");
        this.ts_mp01_vbd_xvdc_iops_write = record_map.get("ts_mp01_vbd_xvdc_iops_write");
        this.ts_mp01_vbd_xvdc_iowait = record_map.get("ts_mp01_vbd_xvdc_iowait");
        this.ts_mp01_vbd_xvdc_latency = record_map.get("ts_mp01_vbd_xvdc_latency");
        this.ts_mp01_vbd_xvdc_read = record_map.get("ts_mp01_vbd_xvdc_read");
        this.ts_mp01_vbd_xvdc_read_latency = record_map.get("ts_mp01_vbd_xvdc_read_latency");
        this.ts_mp01_vbd_xvdc_write = record_map.get("ts_mp01_vbd_xvdc_write");
        this.ts_mp01_vbd_xvdc_write_latency = record_map.get("ts_mp01_vbd_xvdc_write_latency");
        this.ts_mp01_vbd_xvdg_avgqu_sz = record_map.get("ts_mp01_vbd_xvdg_avgqu_sz");
        this.ts_mp01_vbd_xvdg_inflight = record_map.get("ts_mp01_vbd_xvdg_inflight");
        this.ts_mp01_vbd_xvdg_io_throughput_read = record_map.get("ts_mp01_vbd_xvdg_io_throughput_read");
        this.ts_mp01_vbd_xvdg_io_throughput_total = record_map.get("ts_mp01_vbd_xvdg_io_throughput_total");
        this.ts_mp01_vbd_xvdg_io_throughput_write = record_map.get("ts_mp01_vbd_xvdg_io_throughput_write");
        this.ts_mp01_vbd_xvdg_iops_read = record_map.get("ts_mp01_vbd_xvdg_iops_read");
        this.ts_mp01_vbd_xvdg_iops_total = record_map.get("ts_mp01_vbd_xvdg_iops_total");
        this.ts_mp01_vbd_xvdg_iops_write = record_map.get("ts_mp01_vbd_xvdg_iops_write");
        this.ts_mp01_vbd_xvdg_iowait = record_map.get("ts_mp01_vbd_xvdg_iowait");
        this.ts_mp01_vbd_xvdg_latency = record_map.get("ts_mp01_vbd_xvdg_latency");
        this.ts_mp01_vbd_xvdg_read = record_map.get("ts_mp01_vbd_xvdg_read");
        this.ts_mp01_vbd_xvdg_read_latency = record_map.get("ts_mp01_vbd_xvdg_read_latency");
        this.ts_mp01_vbd_xvdg_write = record_map.get("ts_mp01_vbd_xvdg_write");
        this.ts_mp01_vbd_xvdg_write_latency = record_map.get("ts_mp01_vbd_xvdg_write_latency");
        this.ro2_memory = record_map.get("ro2_memory");
        this.ro2_cpu7 = record_map.get("ro2_cpu7");
        this.ro2_cpu6 = record_map.get("ro2_cpu6");
        this.ro2_cpu5 = record_map.get("ro2_cpu5");
        this.ro2_cpu4 = record_map.get("ro2_cpu4");
        this.ro2_cpu3 = record_map.get("ro2_cpu3");
        this.ro2_cpu2 = record_map.get("ro2_cpu2");
        this.ro2_cpu1 = record_map.get("ro2_cpu1");
        this.ro2_cpu0 = record_map.get("ro2_cpu0");
        this.ro2_vif_0_tx = record_map.get("ro2_vif_0_tx");
        this.ro2_vif_0_rx = record_map.get("ro2_vif_0_rx");
        this.ro2_vbd_xvda_avgqu_sz = record_map.get("ro2_vbd_xvda_avgqu_sz");
        this.ro2_vbd_xvda_latency = record_map.get("ro2_vbd_xvda_latency");
        this.ro2_vbd_xvde_avgqu_sz = record_map.get("ro2_vbd_xvde_avgqu_sz");
        this.ro2_vbd_xvde_latency = record_map.get("ro2_vbd_xvde_latency");
        this.ro2_vbd_xvdc_avgqu_sz = record_map.get("ro2_vbd_xvdc_avgqu_sz");
        this.ro2_vbd_xvdc_latency = record_map.get("ro2_vbd_xvdc_latency");
        this.ro2_vbd_xvdb_avgqu_sz = record_map.get("ro2_vbd_xvdb_avgqu_sz");
        this.ro2_vbd_xvdb_latency = record_map.get("ro2_vbd_xvdb_latency");
        this.ro2_vbd_xvda_inflight = record_map.get("ro2_vbd_xvda_inflight");
        this.ro2_vbd_xvda_iowait = record_map.get("ro2_vbd_xvda_iowait");
        this.ro2_vbd_xvda_iops_total = record_map.get("ro2_vbd_xvda_iops_total");
        this.ro2_vbd_xvda_iops_write = record_map.get("ro2_vbd_xvda_iops_write");
        this.ro2_vbd_xvda_iops_read = record_map.get("ro2_vbd_xvda_iops_read");
        this.ro2_vbd_xvda_io_throughput_total = record_map.get("ro2_vbd_xvda_io_throughput_total");
        this.ro2_vbd_xvda_io_throughput_write = record_map.get("ro2_vbd_xvda_io_throughput_write");
        this.ro2_vbd_xvda_io_throughput_read = record_map.get("ro2_vbd_xvda_io_throughput_read");
        this.ro2_vbd_xvda_write_latency = record_map.get("ro2_vbd_xvda_write_latency");
        this.ro2_vbd_xvda_read_latency = record_map.get("ro2_vbd_xvda_read_latency");
        this.ro2_vbd_xvda_write = record_map.get("ro2_vbd_xvda_write");
        this.ro2_vbd_xvda_read = record_map.get("ro2_vbd_xvda_read");
        this.ro2_vbd_xvde_inflight = record_map.get("ro2_vbd_xvde_inflight");
        this.ro2_vbd_xvde_iowait = record_map.get("ro2_vbd_xvde_iowait");
        this.ro2_vbd_xvde_iops_total = record_map.get("ro2_vbd_xvde_iops_total");
        this.ro2_vbd_xvde_iops_write = record_map.get("ro2_vbd_xvde_iops_write");
        this.ro2_vbd_xvde_iops_read = record_map.get("ro2_vbd_xvde_iops_read");
        this.ro2_vbd_xvde_io_throughput_total = record_map.get("ro2_vbd_xvde_io_throughput_total");
        this.ro2_vbd_xvde_io_throughput_write = record_map.get("ro2_vbd_xvde_io_throughput_write");
        this.ro2_vbd_xvde_io_throughput_read = record_map.get("ro2_vbd_xvde_io_throughput_read");
        this.ro2_vbd_xvde_write_latency = record_map.get("ro2_vbd_xvde_write_latency");
        this.ro2_vbd_xvde_read_latency = record_map.get("ro2_vbd_xvde_read_latency");
        this.ro2_vbd_xvde_write = record_map.get("ro2_vbd_xvde_write");
        this.ro2_vbd_xvde_read = record_map.get("ro2_vbd_xvde_read");
        this.ro2_vbd_xvdc_inflight = record_map.get("ro2_vbd_xvdc_inflight");
        this.ro2_vbd_xvdc_iowait = record_map.get("ro2_vbd_xvdc_iowait");
        this.ro2_vbd_xvdc_iops_total = record_map.get("ro2_vbd_xvdc_iops_total");
        this.ro2_vbd_xvdc_iops_write = record_map.get("ro2_vbd_xvdc_iops_write");
        this.ro2_vbd_xvdc_iops_read = record_map.get("ro2_vbd_xvdc_iops_read");
        this.ro2_vbd_xvdc_io_throughput_total = record_map.get("ro2_vbd_xvdc_io_throughput_total");
        this.ro2_vbd_xvdc_io_throughput_write = record_map.get("ro2_vbd_xvdc_io_throughput_write");
        this.ro2_vbd_xvdc_io_throughput_read = record_map.get("ro2_vbd_xvdc_io_throughput_read");
        this.ro2_vbd_xvdc_write_latency = record_map.get("ro2_vbd_xvdc_write_latency");
        this.ro2_vbd_xvdc_read_latency = record_map.get("ro2_vbd_xvdc_read_latency");
        this.ro2_vbd_xvdc_write = record_map.get("ro2_vbd_xvdc_write");
        this.ro2_vbd_xvdc_read = record_map.get("ro2_vbd_xvdc_read");
        this.ro2_vbd_xvdb_inflight = record_map.get("ro2_vbd_xvdb_inflight");
        this.ro2_vbd_xvdb_iowait = record_map.get("ro2_vbd_xvdb_iowait");
        this.ro2_vbd_xvdb_iops_total = record_map.get("ro2_vbd_xvdb_iops_total");
        this.ro2_vbd_xvdb_iops_write = record_map.get("ro2_vbd_xvdb_iops_write");
        this.ro2_vbd_xvdb_iops_read = record_map.get("ro2_vbd_xvdb_iops_read");
        this.ro2_vbd_xvdb_io_throughput_total = record_map.get("ro2_vbd_xvdb_io_throughput_total");
        this.ro2_vbd_xvdb_io_throughput_write = record_map.get("ro2_vbd_xvdb_io_throughput_write");
        this.ro2_vbd_xvdb_io_throughput_read = record_map.get("ro2_vbd_xvdb_io_throughput_read");
        this.ro2_vbd_xvdb_write_latency = record_map.get("ro2_vbd_xvdb_write_latency");
        this.ro2_vbd_xvdb_read_latency = record_map.get("ro2_vbd_xvdb_read_latency");
        this.ro2_vbd_xvdb_write = record_map.get("ro2_vbd_xvdb_write");
        this.ro2_vbd_xvdb_read = record_map.get("ro2_vbd_xvdb_read");
        this.ro2_memory_internal_free = record_map.get("ro2_memory_internal_free");
        this.ro2_memory_target = record_map.get("ro2_memory_target");
        this.ro2_vbd_xvdf_avgqu_sz = record_map.get("ro2_vbd_xvdf_avgqu_sz");
        this.ro2_vbd_xvdf_inflight = record_map.get("ro2_vbd_xvdf_inflight");
        this.ro2_vbd_xvdf_io_throughput_read = record_map.get("ro2_vbd_xvdf_io_throughput_read");
        this.ro2_vbd_xvdf_io_throughput_total = record_map.get("ro2_vbd_xvdf_io_throughput_total");
        this.ro2_vbd_xvdf_io_throughput_write = record_map.get("ro2_vbd_xvdf_io_throughput_write");
        this.ro2_vbd_xvdf_iops_read = record_map.get("ro2_vbd_xvdf_iops_read");
        this.ro2_vbd_xvdf_iops_total = record_map.get("ro2_vbd_xvdf_iops_total");
        this.ro2_vbd_xvdf_iops_write = record_map.get("ro2_vbd_xvdf_iops_write");
        this.ro2_vbd_xvdf_iowait = record_map.get("ro2_vbd_xvdf_iowait");
        this.ro2_vbd_xvdf_latency = record_map.get("ro2_vbd_xvdf_latency");
        this.ro2_vbd_xvdf_read = record_map.get("ro2_vbd_xvdf_read");
        this.ro2_vbd_xvdf_read_latency = record_map.get("ro2_vbd_xvdf_read_latency");
        this.ro2_vbd_xvdf_write = record_map.get("ro2_vbd_xvdf_write");
        this.ro2_vbd_xvdf_write_latency = record_map.get("ro2_vbd_xvdf_write_latency");
        this.ro2_vbd_xvdg_avgqu_sz = record_map.get("ro2_vbd_xvdg_avgqu_sz");
        this.ro2_vbd_xvdg_inflight = record_map.get("ro2_vbd_xvdg_inflight");
        this.ro2_vbd_xvdg_io_throughput_read = record_map.get("ro2_vbd_xvdg_io_throughput_read");
        this.ro2_vbd_xvdg_io_throughput_total = record_map.get("ro2_vbd_xvdg_io_throughput_total");
        this.ro2_vbd_xvdg_io_throughput_write = record_map.get("ro2_vbd_xvdg_io_throughput_write");
        this.ro2_vbd_xvdg_iops_read = record_map.get("ro2_vbd_xvdg_iops_read");
        this.ro2_vbd_xvdg_iops_total = record_map.get("ro2_vbd_xvdg_iops_total");
        this.ro2_vbd_xvdg_iops_write = record_map.get("ro2_vbd_xvdg_iops_write");
        this.ro2_vbd_xvdg_iowait = record_map.get("ro2_vbd_xvdg_iowait");
        this.ro2_vbd_xvdg_latency = record_map.get("ro2_vbd_xvdg_latency");
        this.ro2_vbd_xvdg_read = record_map.get("ro2_vbd_xvdg_read");
        this.ro2_vbd_xvdg_read_latency = record_map.get("ro2_vbd_xvdg_read_latency");
        this.ro2_vbd_xvdg_write = record_map.get("ro2_vbd_xvdg_write");
        this.ro2_vbd_xvdg_write_latency = record_map.get("ro2_vbd_xvdg_write_latency");
        this.tw_cho01_cpu0 = record_map.get("tw_cho01_cpu0");
        this.tw_cho01_cpu1 = record_map.get("tw_cho01_cpu1");
        this.tw_cho01_cpu2 = record_map.get("tw_cho01_cpu2");
        this.tw_cho01_cpu3 = record_map.get("tw_cho01_cpu3");
        this.tw_cho01_memory = record_map.get("tw_cho01_memory");
        this.tw_cho01_vbd_xvda_avgqu_sz = record_map.get("tw_cho01_vbd_xvda_avgqu_sz");
        this.tw_cho01_vbd_xvda_inflight = record_map.get("tw_cho01_vbd_xvda_inflight");
        this.tw_cho01_vbd_xvda_io_throughput_read = record_map.get("tw_cho01_vbd_xvda_io_throughput_read");
        this.tw_cho01_vbd_xvda_io_throughput_total = record_map.get("tw_cho01_vbd_xvda_io_throughput_total");
        this.tw_cho01_vbd_xvda_io_throughput_write = record_map.get("tw_cho01_vbd_xvda_io_throughput_write");
        this.tw_cho01_vbd_xvda_iops_read = record_map.get("tw_cho01_vbd_xvda_iops_read");
        this.tw_cho01_vbd_xvda_iops_total = record_map.get("tw_cho01_vbd_xvda_iops_total");
        this.tw_cho01_vbd_xvda_iops_write = record_map.get("tw_cho01_vbd_xvda_iops_write");
        this.tw_cho01_vbd_xvda_iowait = record_map.get("tw_cho01_vbd_xvda_iowait");
        this.tw_cho01_vbd_xvda_latency = record_map.get("tw_cho01_vbd_xvda_latency");
        this.tw_cho01_vbd_xvda_read = record_map.get("tw_cho01_vbd_xvda_read");
        this.tw_cho01_vbd_xvda_read_latency = record_map.get("tw_cho01_vbd_xvda_read_latency");
        this.tw_cho01_vbd_xvda_write = record_map.get("tw_cho01_vbd_xvda_write");
        this.tw_cho01_vbd_xvda_write_latency = record_map.get("tw_cho01_vbd_xvda_write_latency");
        this.tw_cho01_vif_1_rx = record_map.get("tw_cho01_vif_1_rx");
        this.tw_cho01_vif_1_tx = record_map.get("tw_cho01_vif_1_tx");
        this.tw_cho01_memory_target = record_map.get("tw_cho01_memory_target");
        this.rh2_memory = record_map.get("rh2_memory");
        this.rh2_cpu31 = record_map.get("rh2_cpu31");
        this.rh2_cpu30 = record_map.get("rh2_cpu30");
        this.rh2_cpu29 = record_map.get("rh2_cpu29");
        this.rh2_cpu28 = record_map.get("rh2_cpu28");
        this.rh2_cpu27 = record_map.get("rh2_cpu27");
        this.rh2_cpu26 = record_map.get("rh2_cpu26");
        this.rh2_cpu25 = record_map.get("rh2_cpu25");
        this.rh2_cpu24 = record_map.get("rh2_cpu24");
        this.rh2_cpu23 = record_map.get("rh2_cpu23");
        this.rh2_cpu22 = record_map.get("rh2_cpu22");
        this.rh2_cpu21 = record_map.get("rh2_cpu21");
        this.rh2_cpu20 = record_map.get("rh2_cpu20");
        this.rh2_cpu19 = record_map.get("rh2_cpu19");
        this.rh2_cpu18 = record_map.get("rh2_cpu18");
        this.rh2_cpu17 = record_map.get("rh2_cpu17");
        this.rh2_cpu16 = record_map.get("rh2_cpu16");
        this.rh2_cpu15 = record_map.get("rh2_cpu15");
        this.rh2_cpu14 = record_map.get("rh2_cpu14");
        this.rh2_cpu13 = record_map.get("rh2_cpu13");
        this.rh2_cpu12 = record_map.get("rh2_cpu12");
        this.rh2_cpu11 = record_map.get("rh2_cpu11");
        this.rh2_cpu10 = record_map.get("rh2_cpu10");
        this.rh2_cpu9 = record_map.get("rh2_cpu9");
        this.rh2_cpu8 = record_map.get("rh2_cpu8");
        this.rh2_cpu7 = record_map.get("rh2_cpu7");
        this.rh2_cpu6 = record_map.get("rh2_cpu6");
        this.rh2_cpu5 = record_map.get("rh2_cpu5");
        this.rh2_cpu4 = record_map.get("rh2_cpu4");
        this.rh2_cpu3 = record_map.get("rh2_cpu3");
        this.rh2_cpu2 = record_map.get("rh2_cpu2");
        this.rh2_cpu1 = record_map.get("rh2_cpu1");
        this.rh2_cpu0 = record_map.get("rh2_cpu0");
        this.rh2_vif_1_tx = record_map.get("rh2_vif_1_tx");
        this.rh2_vif_1_rx = record_map.get("rh2_vif_1_rx");
        this.rh2_vbd_xvdc_avgqu_sz = record_map.get("rh2_vbd_xvdc_avgqu_sz");
        this.rh2_vbd_xvdc_latency = record_map.get("rh2_vbd_xvdc_latency");
        this.rh2_vbd_xvde_avgqu_sz = record_map.get("rh2_vbd_xvde_avgqu_sz");
        this.rh2_vbd_xvde_latency = record_map.get("rh2_vbd_xvde_latency");
        this.rh2_vbd_xvdc_inflight = record_map.get("rh2_vbd_xvdc_inflight");
        this.rh2_vbd_xvdc_iowait = record_map.get("rh2_vbd_xvdc_iowait");
        this.rh2_vbd_xvdc_iops_total = record_map.get("rh2_vbd_xvdc_iops_total");
        this.rh2_vbd_xvdc_iops_write = record_map.get("rh2_vbd_xvdc_iops_write");
        this.rh2_vbd_xvdc_iops_read = record_map.get("rh2_vbd_xvdc_iops_read");
        this.rh2_vbd_xvdc_io_throughput_total = record_map.get("rh2_vbd_xvdc_io_throughput_total");
        this.rh2_vbd_xvdc_io_throughput_write = record_map.get("rh2_vbd_xvdc_io_throughput_write");
        this.rh2_vbd_xvdc_io_throughput_read = record_map.get("rh2_vbd_xvdc_io_throughput_read");
        this.rh2_vbd_xvdc_write_latency = record_map.get("rh2_vbd_xvdc_write_latency");
        this.rh2_vbd_xvdc_read_latency = record_map.get("rh2_vbd_xvdc_read_latency");
        this.rh2_vbd_xvdc_write = record_map.get("rh2_vbd_xvdc_write");
        this.rh2_vbd_xvdc_read = record_map.get("rh2_vbd_xvdc_read");
        this.rh2_vbd_xvde_inflight = record_map.get("rh2_vbd_xvde_inflight");
        this.rh2_vbd_xvde_iowait = record_map.get("rh2_vbd_xvde_iowait");
        this.rh2_vbd_xvde_iops_total = record_map.get("rh2_vbd_xvde_iops_total");
        this.rh2_vbd_xvde_iops_write = record_map.get("rh2_vbd_xvde_iops_write");
        this.rh2_vbd_xvde_iops_read = record_map.get("rh2_vbd_xvde_iops_read");
        this.rh2_vbd_xvde_io_throughput_total = record_map.get("rh2_vbd_xvde_io_throughput_total");
        this.rh2_vbd_xvde_io_throughput_write = record_map.get("rh2_vbd_xvde_io_throughput_write");
        this.rh2_vbd_xvde_io_throughput_read = record_map.get("rh2_vbd_xvde_io_throughput_read");
        this.rh2_vbd_xvde_write_latency = record_map.get("rh2_vbd_xvde_write_latency");
        this.rh2_vbd_xvde_read_latency = record_map.get("rh2_vbd_xvde_read_latency");
        this.rh2_vbd_xvde_write = record_map.get("rh2_vbd_xvde_write");
        this.rh2_vbd_xvde_read = record_map.get("rh2_vbd_xvde_read");
        this.rh2_memory_internal_free = record_map.get("rh2_memory_internal_free");
        this.rh2_memory_target = record_map.get("rh2_memory_target");
        this.rh2_vbd_xvda_avgqu_sz = record_map.get("rh2_vbd_xvda_avgqu_sz");
        this.rh2_vbd_xvda_inflight = record_map.get("rh2_vbd_xvda_inflight");
        this.rh2_vbd_xvda_io_throughput_read = record_map.get("rh2_vbd_xvda_io_throughput_read");
        this.rh2_vbd_xvda_io_throughput_total = record_map.get("rh2_vbd_xvda_io_throughput_total");
        this.rh2_vbd_xvda_io_throughput_write = record_map.get("rh2_vbd_xvda_io_throughput_write");
        this.rh2_vbd_xvda_iops_read = record_map.get("rh2_vbd_xvda_iops_read");
        this.rh2_vbd_xvda_iops_total = record_map.get("rh2_vbd_xvda_iops_total");
        this.rh2_vbd_xvda_iops_write = record_map.get("rh2_vbd_xvda_iops_write");
        this.rh2_vbd_xvda_iowait = record_map.get("rh2_vbd_xvda_iowait");
        this.rh2_vbd_xvda_latency = record_map.get("rh2_vbd_xvda_latency");
        this.rh2_vbd_xvda_read = record_map.get("rh2_vbd_xvda_read");
        this.rh2_vbd_xvda_read_latency = record_map.get("rh2_vbd_xvda_read_latency");
        this.rh2_vbd_xvda_write = record_map.get("rh2_vbd_xvda_write");
        this.rh2_vbd_xvda_write_latency = record_map.get("rh2_vbd_xvda_write_latency");
        this.rh2_vbd_xvdb_avgqu_sz = record_map.get("rh2_vbd_xvdb_avgqu_sz");
        this.rh2_vbd_xvdb_inflight = record_map.get("rh2_vbd_xvdb_inflight");
        this.rh2_vbd_xvdb_io_throughput_read = record_map.get("rh2_vbd_xvdb_io_throughput_read");
        this.rh2_vbd_xvdb_io_throughput_total = record_map.get("rh2_vbd_xvdb_io_throughput_total");
        this.rh2_vbd_xvdb_io_throughput_write = record_map.get("rh2_vbd_xvdb_io_throughput_write");
        this.rh2_vbd_xvdb_iops_read = record_map.get("rh2_vbd_xvdb_iops_read");
        this.rh2_vbd_xvdb_iops_total = record_map.get("rh2_vbd_xvdb_iops_total");
        this.rh2_vbd_xvdb_iops_write = record_map.get("rh2_vbd_xvdb_iops_write");
        this.rh2_vbd_xvdb_iowait = record_map.get("rh2_vbd_xvdb_iowait");
        this.rh2_vbd_xvdb_latency = record_map.get("rh2_vbd_xvdb_latency");
        this.rh2_vbd_xvdb_read = record_map.get("rh2_vbd_xvdb_read");
        this.rh2_vbd_xvdb_read_latency = record_map.get("rh2_vbd_xvdb_read_latency");
        this.rh2_vbd_xvdb_write = record_map.get("rh2_vbd_xvdb_write");
        this.rh2_vbd_xvdb_write_latency = record_map.get("rh2_vbd_xvdb_write_latency");
        this.rh2_vbd_xvdf_avgqu_sz = record_map.get("rh2_vbd_xvdf_avgqu_sz");
        this.rh2_vbd_xvdf_inflight = record_map.get("rh2_vbd_xvdf_inflight");
        this.rh2_vbd_xvdf_io_throughput_read = record_map.get("rh2_vbd_xvdf_io_throughput_read");
        this.rh2_vbd_xvdf_io_throughput_total = record_map.get("rh2_vbd_xvdf_io_throughput_total");
        this.rh2_vbd_xvdf_io_throughput_write = record_map.get("rh2_vbd_xvdf_io_throughput_write");
        this.rh2_vbd_xvdf_iops_read = record_map.get("rh2_vbd_xvdf_iops_read");
        this.rh2_vbd_xvdf_iops_total = record_map.get("rh2_vbd_xvdf_iops_total");
        this.rh2_vbd_xvdf_iops_write = record_map.get("rh2_vbd_xvdf_iops_write");
        this.rh2_vbd_xvdf_iowait = record_map.get("rh2_vbd_xvdf_iowait");
        this.rh2_vbd_xvdf_latency = record_map.get("rh2_vbd_xvdf_latency");
        this.rh2_vbd_xvdf_read = record_map.get("rh2_vbd_xvdf_read");
        this.rh2_vbd_xvdf_read_latency = record_map.get("rh2_vbd_xvdf_read_latency");
        this.rh2_vbd_xvdf_write = record_map.get("rh2_vbd_xvdf_write");
        this.rh2_vbd_xvdf_write_latency = record_map.get("rh2_vbd_xvdf_write_latency");
    }

    public String getJob_id() {
        return this.job_id;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String toString() {
        return this.timestamp + "," +
                this.vm1_cpu0 + "," +
                this.vm1_cpu1 + "," +
                this.vm1_cpu10 + "," +
                this.vm1_cpu11 + "," +
                this.vm1_cpu12 + "," +
                this.vm1_cpu13 + "," +
                this.vm1_cpu14 + "," +
                this.vm1_cpu15 + "," +
                this.vm1_cpu2 + "," +
                this.vm1_cpu3 + "," +
                this.vm1_cpu4 + "," +
                this.vm1_cpu5 + "," +
                this.vm1_cpu6 + "," +
                this.vm1_cpu7 + "," +
                this.vm1_cpu8 + "," +
                this.vm1_cpu9 + "," +
                this.vm1_memory + "," +
                this.tw_mw001_cpu0 + "," +
                this.tw_mw001_cpu1 + "," +
                this.tw_mw001_cpu2 + "," +
                this.tw_mw001_cpu3 + "," +
                this.tw_mw001_memory + "," +
                this.tw_mw001_vbd_xvda_avgqu_sz + "," +
                this.tw_mw001_vbd_xvda_inflight + "," +
                this.tw_mw001_vbd_xvda_io_throughput_read + "," +
                this.tw_mw001_vbd_xvda_io_throughput_total + "," +
                this.tw_mw001_vbd_xvda_io_throughput_write + "," +
                this.tw_mw001_vbd_xvda_iops_read + "," +
                this.tw_mw001_vbd_xvda_iops_total + "," +
                this.tw_mw001_vbd_xvda_iops_write + "," +
                this.tw_mw001_vbd_xvda_iowait + "," +
                this.tw_mw001_vbd_xvda_latency + "," +
                this.tw_mw001_vbd_xvda_read + "," +
                this.tw_mw001_vbd_xvda_read_latency + "," +
                this.tw_mw001_vbd_xvda_write + "," +
                this.tw_mw001_vbd_xvda_write_latency + "," +
                this.tw_mw001_memory_target + "," +
                this.tw_mw001_vbd_xvdd_avgqu_sz + "," +
                this.tw_mw001_vbd_xvdd_inflight + "," +
                this.tw_mw001_vbd_xvdd_io_throughput_read + "," +
                this.tw_mw001_vbd_xvdd_io_throughput_total + "," +
                this.tw_mw001_vbd_xvdd_io_throughput_write + "," +
                this.tw_mw001_vbd_xvdd_iops_read + "," +
                this.tw_mw001_vbd_xvdd_iops_total + "," +
                this.tw_mw001_vbd_xvdd_iops_write + "," +
                this.tw_mw001_vbd_xvdd_iowait + "," +
                this.tw_mw001_vbd_xvdd_latency + "," +
                this.tw_mw001_vbd_xvdd_read + "," +
                this.tw_mw001_vbd_xvdd_read_latency + "," +
                this.tw_mw001_vbd_xvdd_write + "," +
                this.tw_mw001_vbd_xvdd_write_latency + "," +
                this.tw_mw001_vif_1_rx + "," +
                this.tw_mw001_vif_1_tx + "," +
                this.ts_mp01_memory + "," +
                this.ts_mp01_cpu11 + "," +
                this.ts_mp01_cpu10 + "," +
                this.ts_mp01_cpu9 + "," +
                this.ts_mp01_cpu8 + "," +
                this.ts_mp01_cpu7 + "," +
                this.ts_mp01_cpu6 + "," +
                this.ts_mp01_cpu5 + "," +
                this.ts_mp01_cpu4 + "," +
                this.ts_mp01_cpu3 + "," +
                this.ts_mp01_cpu2 + "," +
                this.ts_mp01_cpu1 + "," +
                this.ts_mp01_cpu0 + "," +
                this.ts_mp01_vif_0_tx + "," +
                this.ts_mp01_vif_0_rx + "," +
                this.ts_mp01_vbd_xvde_avgqu_sz + "," +
                this.ts_mp01_vbd_xvde_latency + "," +
                this.ts_mp01_vbd_xvdf_avgqu_sz + "," +
                this.ts_mp01_vbd_xvdf_latency + "," +
                this.ts_mp01_vbd_xvde_inflight + "," +
                this.ts_mp01_vbd_xvde_iowait + "," +
                this.ts_mp01_vbd_xvde_iops_total + "," +
                this.ts_mp01_vbd_xvde_iops_write + "," +
                this.ts_mp01_vbd_xvde_iops_read + "," +
                this.ts_mp01_vbd_xvde_io_throughput_total + "," +
                this.ts_mp01_vbd_xvde_io_throughput_write + "," +
                this.ts_mp01_vbd_xvde_io_throughput_read + "," +
                this.ts_mp01_vbd_xvde_write_latency + "," +
                this.ts_mp01_vbd_xvde_read_latency + "," +
                this.ts_mp01_vbd_xvde_write + "," +
                this.ts_mp01_vbd_xvde_read + "," +
                this.ts_mp01_vbd_xvdf_inflight + "," +
                this.ts_mp01_vbd_xvdf_iowait + "," +
                this.ts_mp01_vbd_xvdf_iops_total + "," +
                this.ts_mp01_vbd_xvdf_iops_write + "," +
                this.ts_mp01_vbd_xvdf_iops_read + "," +
                this.ts_mp01_vbd_xvdf_io_throughput_total + "," +
                this.ts_mp01_vbd_xvdf_io_throughput_write + "," +
                this.ts_mp01_vbd_xvdf_io_throughput_read + "," +
                this.ts_mp01_vbd_xvdf_write_latency + "," +
                this.ts_mp01_vbd_xvdf_read_latency + "," +
                this.ts_mp01_vbd_xvdf_write + "," +
                this.ts_mp01_vbd_xvdf_read + "," +
                this.ts_mp01_memory_target + "," +
                this.ts_mp01_vbd_xvda_avgqu_sz + "," +
                this.ts_mp01_vbd_xvda_inflight + "," +
                this.ts_mp01_vbd_xvda_io_throughput_read + "," +
                this.ts_mp01_vbd_xvda_io_throughput_total + "," +
                this.ts_mp01_vbd_xvda_io_throughput_write + "," +
                this.ts_mp01_vbd_xvda_iops_read + "," +
                this.ts_mp01_vbd_xvda_iops_total + "," +
                this.ts_mp01_vbd_xvda_iops_write + "," +
                this.ts_mp01_vbd_xvda_iowait + "," +
                this.ts_mp01_vbd_xvda_latency + "," +
                this.ts_mp01_vbd_xvda_read + "," +
                this.ts_mp01_vbd_xvda_read_latency + "," +
                this.ts_mp01_vbd_xvda_write + "," +
                this.ts_mp01_vbd_xvda_write_latency + "," +
                this.ts_mp01_vbd_xvdb_avgqu_sz + "," +
                this.ts_mp01_vbd_xvdb_inflight + "," +
                this.ts_mp01_vbd_xvdb_io_throughput_read + "," +
                this.ts_mp01_vbd_xvdb_io_throughput_total + "," +
                this.ts_mp01_vbd_xvdb_io_throughput_write + "," +
                this.ts_mp01_vbd_xvdb_iops_read + "," +
                this.ts_mp01_vbd_xvdb_iops_total + "," +
                this.ts_mp01_vbd_xvdb_iops_write + "," +
                this.ts_mp01_vbd_xvdb_iowait + "," +
                this.ts_mp01_vbd_xvdb_latency + "," +
                this.ts_mp01_vbd_xvdb_read + "," +
                this.ts_mp01_vbd_xvdb_read_latency + "," +
                this.ts_mp01_vbd_xvdb_write + "," +
                this.ts_mp01_vbd_xvdb_write_latency + "," +
                this.ts_mp01_vbd_xvdc_avgqu_sz + "," +
                this.ts_mp01_vbd_xvdc_inflight + "," +
                this.ts_mp01_vbd_xvdc_io_throughput_read + "," +
                this.ts_mp01_vbd_xvdc_io_throughput_total + "," +
                this.ts_mp01_vbd_xvdc_io_throughput_write + "," +
                this.ts_mp01_vbd_xvdc_iops_read + "," +
                this.ts_mp01_vbd_xvdc_iops_total + "," +
                this.ts_mp01_vbd_xvdc_iops_write + "," +
                this.ts_mp01_vbd_xvdc_iowait + "," +
                this.ts_mp01_vbd_xvdc_latency + "," +
                this.ts_mp01_vbd_xvdc_read + "," +
                this.ts_mp01_vbd_xvdc_read_latency + "," +
                this.ts_mp01_vbd_xvdc_write + "," +
                this.ts_mp01_vbd_xvdc_write_latency + "," +
                this.ts_mp01_vbd_xvdg_avgqu_sz + "," +
                this.ts_mp01_vbd_xvdg_inflight + "," +
                this.ts_mp01_vbd_xvdg_io_throughput_read + "," +
                this.ts_mp01_vbd_xvdg_io_throughput_total + "," +
                this.ts_mp01_vbd_xvdg_io_throughput_write + "," +
                this.ts_mp01_vbd_xvdg_iops_read + "," +
                this.ts_mp01_vbd_xvdg_iops_total + "," +
                this.ts_mp01_vbd_xvdg_iops_write + "," +
                this.ts_mp01_vbd_xvdg_iowait + "," +
                this.ts_mp01_vbd_xvdg_latency + "," +
                this.ts_mp01_vbd_xvdg_read + "," +
                this.ts_mp01_vbd_xvdg_read_latency + "," +
                this.ts_mp01_vbd_xvdg_write + "," +
                this.ts_mp01_vbd_xvdg_write_latency + "," +
                this.ro2_memory + "," +
                this.ro2_cpu7 + "," +
                this.ro2_cpu6 + "," +
                this.ro2_cpu5 + "," +
                this.ro2_cpu4 + "," +
                this.ro2_cpu3 + "," +
                this.ro2_cpu2 + "," +
                this.ro2_cpu1 + "," +
                this.ro2_cpu0 + "," +
                this.ro2_vif_0_tx + "," +
                this.ro2_vif_0_rx + "," +
                this.ro2_vbd_xvda_avgqu_sz + "," +
                this.ro2_vbd_xvda_latency + "," +
                this.ro2_vbd_xvde_avgqu_sz + "," +
                this.ro2_vbd_xvde_latency + "," +
                this.ro2_vbd_xvdc_avgqu_sz + "," +
                this.ro2_vbd_xvdc_latency + "," +
                this.ro2_vbd_xvdb_avgqu_sz + "," +
                this.ro2_vbd_xvdb_latency + "," +
                this.ro2_vbd_xvda_inflight + "," +
                this.ro2_vbd_xvda_iowait + "," +
                this.ro2_vbd_xvda_iops_total + "," +
                this.ro2_vbd_xvda_iops_write + "," +
                this.ro2_vbd_xvda_iops_read + "," +
                this.ro2_vbd_xvda_io_throughput_total + "," +
                this.ro2_vbd_xvda_io_throughput_write + "," +
                this.ro2_vbd_xvda_io_throughput_read + "," +
                this.ro2_vbd_xvda_write_latency + "," +
                this.ro2_vbd_xvda_read_latency + "," +
                this.ro2_vbd_xvda_write + "," +
                this.ro2_vbd_xvda_read + "," +
                this.ro2_vbd_xvde_inflight + "," +
                this.ro2_vbd_xvde_iowait + "," +
                this.ro2_vbd_xvde_iops_total + "," +
                this.ro2_vbd_xvde_iops_write + "," +
                this.ro2_vbd_xvde_iops_read + "," +
                this.ro2_vbd_xvde_io_throughput_total + "," +
                this.ro2_vbd_xvde_io_throughput_write + "," +
                this.ro2_vbd_xvde_io_throughput_read + "," +
                this.ro2_vbd_xvde_write_latency + "," +
                this.ro2_vbd_xvde_read_latency + "," +
                this.ro2_vbd_xvde_write + "," +
                this.ro2_vbd_xvde_read + "," +
                this.ro2_vbd_xvdc_inflight + "," +
                this.ro2_vbd_xvdc_iowait + "," +
                this.ro2_vbd_xvdc_iops_total + "," +
                this.ro2_vbd_xvdc_iops_write + "," +
                this.ro2_vbd_xvdc_iops_read + "," +
                this.ro2_vbd_xvdc_io_throughput_total + "," +
                this.ro2_vbd_xvdc_io_throughput_write + "," +
                this.ro2_vbd_xvdc_io_throughput_read + "," +
                this.ro2_vbd_xvdc_write_latency + "," +
                this.ro2_vbd_xvdc_read_latency + "," +
                this.ro2_vbd_xvdc_write + "," +
                this.ro2_vbd_xvdc_read + "," +
                this.ro2_vbd_xvdb_inflight + "," +
                this.ro2_vbd_xvdb_iowait + "," +
                this.ro2_vbd_xvdb_iops_total + "," +
                this.ro2_vbd_xvdb_iops_write + "," +
                this.ro2_vbd_xvdb_iops_read + "," +
                this.ro2_vbd_xvdb_io_throughput_total + "," +
                this.ro2_vbd_xvdb_io_throughput_write + "," +
                this.ro2_vbd_xvdb_io_throughput_read + "," +
                this.ro2_vbd_xvdb_write_latency + "," +
                this.ro2_vbd_xvdb_read_latency + "," +
                this.ro2_vbd_xvdb_write + "," +
                this.ro2_vbd_xvdb_read + "," +
                this.ro2_memory_internal_free + "," +
                this.ro2_memory_target + "," +
                this.ro2_vbd_xvdf_avgqu_sz + "," +
                this.ro2_vbd_xvdf_inflight + "," +
                this.ro2_vbd_xvdf_io_throughput_read + "," +
                this.ro2_vbd_xvdf_io_throughput_total + "," +
                this.ro2_vbd_xvdf_io_throughput_write + "," +
                this.ro2_vbd_xvdf_iops_read + "," +
                this.ro2_vbd_xvdf_iops_total + "," +
                this.ro2_vbd_xvdf_iops_write + "," +
                this.ro2_vbd_xvdf_iowait + "," +
                this.ro2_vbd_xvdf_latency + "," +
                this.ro2_vbd_xvdf_read + "," +
                this.ro2_vbd_xvdf_read_latency + "," +
                this.ro2_vbd_xvdf_write + "," +
                this.ro2_vbd_xvdf_write_latency + "," +
                this.ro2_vbd_xvdg_avgqu_sz + "," +
                this.ro2_vbd_xvdg_inflight + "," +
                this.ro2_vbd_xvdg_io_throughput_read + "," +
                this.ro2_vbd_xvdg_io_throughput_total + "," +
                this.ro2_vbd_xvdg_io_throughput_write + "," +
                this.ro2_vbd_xvdg_iops_read + "," +
                this.ro2_vbd_xvdg_iops_total + "," +
                this.ro2_vbd_xvdg_iops_write + "," +
                this.ro2_vbd_xvdg_iowait + "," +
                this.ro2_vbd_xvdg_latency + "," +
                this.ro2_vbd_xvdg_read + "," +
                this.ro2_vbd_xvdg_read_latency + "," +
                this.ro2_vbd_xvdg_write + "," +
                this.ro2_vbd_xvdg_write_latency + "," +
                this.tw_cho01_cpu0 + "," +
                this.tw_cho01_cpu1 + "," +
                this.tw_cho01_cpu2 + "," +
                this.tw_cho01_cpu3 + "," +
                this.tw_cho01_memory + "," +
                this.tw_cho01_vbd_xvda_avgqu_sz + "," +
                this.tw_cho01_vbd_xvda_inflight + "," +
                this.tw_cho01_vbd_xvda_io_throughput_read + "," +
                this.tw_cho01_vbd_xvda_io_throughput_total + "," +
                this.tw_cho01_vbd_xvda_io_throughput_write + "," +
                this.tw_cho01_vbd_xvda_iops_read + "," +
                this.tw_cho01_vbd_xvda_iops_total + "," +
                this.tw_cho01_vbd_xvda_iops_write + "," +
                this.tw_cho01_vbd_xvda_iowait + "," +
                this.tw_cho01_vbd_xvda_latency + "," +
                this.tw_cho01_vbd_xvda_read + "," +
                this.tw_cho01_vbd_xvda_read_latency + "," +
                this.tw_cho01_vbd_xvda_write + "," +
                this.tw_cho01_vbd_xvda_write_latency + "," +
                this.tw_cho01_vif_1_rx + "," +
                this.tw_cho01_vif_1_tx + "," +
                this.tw_cho01_memory_target + "," +
                this.rh2_memory + "," +
                this.rh2_cpu31 + "," +
                this.rh2_cpu30 + "," +
                this.rh2_cpu29 + "," +
                this.rh2_cpu28 + "," +
                this.rh2_cpu27 + "," +
                this.rh2_cpu26 + "," +
                this.rh2_cpu25 + "," +
                this.rh2_cpu24 + "," +
                this.rh2_cpu23 + "," +
                this.rh2_cpu22 + "," +
                this.rh2_cpu21 + "," +
                this.rh2_cpu20 + "," +
                this.rh2_cpu19 + "," +
                this.rh2_cpu18 + "," +
                this.rh2_cpu17 + "," +
                this.rh2_cpu16 + "," +
                this.rh2_cpu15 + "," +
                this.rh2_cpu14 + "," +
                this.rh2_cpu13 + "," +
                this.rh2_cpu12 + "," +
                this.rh2_cpu11 + "," +
                this.rh2_cpu10 + "," +
                this.rh2_cpu9 + "," +
                this.rh2_cpu8 + "," +
                this.rh2_cpu7 + "," +
                this.rh2_cpu6 + "," +
                this.rh2_cpu5 + "," +
                this.rh2_cpu4 + "," +
                this.rh2_cpu3 + "," +
                this.rh2_cpu2 + "," +
                this.rh2_cpu1 + "," +
                this.rh2_cpu0 + "," +
                this.rh2_vif_1_tx + "," +
                this.rh2_vif_1_rx + "," +
                this.rh2_vbd_xvdc_avgqu_sz + "," +
                this.rh2_vbd_xvdc_latency + "," +
                this.rh2_vbd_xvde_avgqu_sz + "," +
                this.rh2_vbd_xvde_latency + "," +
                this.rh2_vbd_xvdc_inflight + "," +
                this.rh2_vbd_xvdc_iowait + "," +
                this.rh2_vbd_xvdc_iops_total + "," +
                this.rh2_vbd_xvdc_iops_write + "," +
                this.rh2_vbd_xvdc_iops_read + "," +
                this.rh2_vbd_xvdc_io_throughput_total + "," +
                this.rh2_vbd_xvdc_io_throughput_write + "," +
                this.rh2_vbd_xvdc_io_throughput_read + "," +
                this.rh2_vbd_xvdc_write_latency + "," +
                this.rh2_vbd_xvdc_read_latency + "," +
                this.rh2_vbd_xvdc_write + "," +
                this.rh2_vbd_xvdc_read + "," +
                this.rh2_vbd_xvde_inflight + "," +
                this.rh2_vbd_xvde_iowait + "," +
                this.rh2_vbd_xvde_iops_total + "," +
                this.rh2_vbd_xvde_iops_write + "," +
                this.rh2_vbd_xvde_iops_read + "," +
                this.rh2_vbd_xvde_io_throughput_total + "," +
                this.rh2_vbd_xvde_io_throughput_write + "," +
                this.rh2_vbd_xvde_io_throughput_read + "," +
                this.rh2_vbd_xvde_write_latency + "," +
                this.rh2_vbd_xvde_read_latency + "," +
                this.rh2_vbd_xvde_write + "," +
                this.rh2_vbd_xvde_read + "," +
                this.rh2_memory_internal_free + "," +
                this.rh2_memory_target + "," +
                this.rh2_vbd_xvda_avgqu_sz + "," +
                this.rh2_vbd_xvda_inflight + "," +
                this.rh2_vbd_xvda_io_throughput_read + "," +
                this.rh2_vbd_xvda_io_throughput_total + "," +
                this.rh2_vbd_xvda_io_throughput_write + "," +
                this.rh2_vbd_xvda_iops_read + "," +
                this.rh2_vbd_xvda_iops_total + "," +
                this.rh2_vbd_xvda_iops_write + "," +
                this.rh2_vbd_xvda_iowait + "," +
                this.rh2_vbd_xvda_latency + "," +
                this.rh2_vbd_xvda_read + "," +
                this.rh2_vbd_xvda_read_latency + "," +
                this.rh2_vbd_xvda_write + "," +
                this.rh2_vbd_xvda_write_latency + "," +
                this.rh2_vbd_xvdb_avgqu_sz + "," +
                this.rh2_vbd_xvdb_inflight + "," +
                this.rh2_vbd_xvdb_io_throughput_read + "," +
                this.rh2_vbd_xvdb_io_throughput_total + "," +
                this.rh2_vbd_xvdb_io_throughput_write + "," +
                this.rh2_vbd_xvdb_iops_read + "," +
                this.rh2_vbd_xvdb_iops_total + "," +
                this.rh2_vbd_xvdb_iops_write + "," +
                this.rh2_vbd_xvdb_iowait + "," +
                this.rh2_vbd_xvdb_latency + "," +
                this.rh2_vbd_xvdb_read + "," +
                this.rh2_vbd_xvdb_read_latency + "," +
                this.rh2_vbd_xvdb_write + "," +
                this.rh2_vbd_xvdb_write_latency + "," +
                this.rh2_vbd_xvdf_avgqu_sz + "," +
                this.rh2_vbd_xvdf_inflight + "," +
                this.rh2_vbd_xvdf_io_throughput_read + "," +
                this.rh2_vbd_xvdf_io_throughput_total + "," +
                this.rh2_vbd_xvdf_io_throughput_write + "," +
                this.rh2_vbd_xvdf_iops_read + "," +
                this.rh2_vbd_xvdf_iops_total + "," +
                this.rh2_vbd_xvdf_iops_write + "," +
                this.rh2_vbd_xvdf_iowait + "," +
                this.rh2_vbd_xvdf_latency + "," +
                this.rh2_vbd_xvdf_read + "," +
                this.rh2_vbd_xvdf_read_latency + "," +
                this.rh2_vbd_xvdf_write + "," +
                this.rh2_vbd_xvdf_write_latency;
    }

    @Override
    public String getJobId() {
        return this.job_id;
    }

    @Override
    public String getAttributeNames() {
        return "job_id,\"timestamp\",vm1_cpu0,vm1_cpu1,vm1_cpu10,vm1_cpu11,vm1_cpu12,vm1_cpu13,vm1_cpu14,vm1_cpu15," +
                "vm1_cpu2,vm1_cpu3,vm1_cpu4,vm1_cpu5,vm1_cpu6,vm1_cpu7,vm1_cpu8,vm1_cpu9,vm1_memory,tw_mw001_cpu0," +
                "tw_mw001_cpu1,tw_mw001_cpu2,tw_mw001_cpu3,tw_mw001_memory,tw_mw001_vbd_xvda_avgqu_sz," +
                "tw_mw001_vbd_xvda_inflight,tw_mw001_vbd_xvda_io_throughput_read," +
                "tw_mw001_vbd_xvda_io_throughput_total,tw_mw001_vbd_xvda_io_throughput_write," +
                "tw_mw001_vbd_xvda_iops_read,tw_mw001_vbd_xvda_iops_total,tw_mw001_vbd_xvda_iops_write," +
                "tw_mw001_vbd_xvda_iowait,tw_mw001_vbd_xvda_latency,tw_mw001_vbd_xvda_read," +
                "tw_mw001_vbd_xvda_read_latency,tw_mw001_vbd_xvda_write,tw_mw001_vbd_xvda_write_latency," +
                "tw_mw001_memory_target,tw_mw001_vbd_xvdd_avgqu_sz,tw_mw001_vbd_xvdd_inflight," +
                "tw_mw001_vbd_xvdd_io_throughput_read,tw_mw001_vbd_xvdd_io_throughput_total," +
                "tw_mw001_vbd_xvdd_io_throughput_write,tw_mw001_vbd_xvdd_iops_read,tw_mw001_vbd_xvdd_iops_total," +
                "tw_mw001_vbd_xvdd_iops_write,tw_mw001_vbd_xvdd_iowait,tw_mw001_vbd_xvdd_latency," +
                "tw_mw001_vbd_xvdd_read,tw_mw001_vbd_xvdd_read_latency,tw_mw001_vbd_xvdd_write," +
                "tw_mw001_vbd_xvdd_write_latency,tw_mw001_vif_1_rx,tw_mw001_vif_1_tx,ts_mp01_memory,ts_mp01_cpu11," +
                "ts_mp01_cpu10,ts_mp01_cpu9,ts_mp01_cpu8,ts_mp01_cpu7,ts_mp01_cpu6,ts_mp01_cpu5,ts_mp01_cpu4," +
                "ts_mp01_cpu3,ts_mp01_cpu2,ts_mp01_cpu1,ts_mp01_cpu0,ts_mp01_vif_0_tx,ts_mp01_vif_0_rx," +
                "ts_mp01_vbd_xvde_avgqu_sz,ts_mp01_vbd_xvde_latency,ts_mp01_vbd_xvdf_avgqu_sz," +
                "ts_mp01_vbd_xvdf_latency,ts_mp01_vbd_xvde_inflight,ts_mp01_vbd_xvde_iowait," +
                "ts_mp01_vbd_xvde_iops_total,ts_mp01_vbd_xvde_iops_write,ts_mp01_vbd_xvde_iops_read," +
                "ts_mp01_vbd_xvde_io_throughput_total,ts_mp01_vbd_xvde_io_throughput_write," +
                "ts_mp01_vbd_xvde_io_throughput_read,ts_mp01_vbd_xvde_write_latency,ts_mp01_vbd_xvde_read_latency," +
                "ts_mp01_vbd_xvde_write,ts_mp01_vbd_xvde_read,ts_mp01_vbd_xvdf_inflight,ts_mp01_vbd_xvdf_iowait," +
                "ts_mp01_vbd_xvdf_iops_total,ts_mp01_vbd_xvdf_iops_write,ts_mp01_vbd_xvdf_iops_read," +
                "ts_mp01_vbd_xvdf_io_throughput_total,ts_mp01_vbd_xvdf_io_throughput_write," +
                "ts_mp01_vbd_xvdf_io_throughput_read,ts_mp01_vbd_xvdf_write_latency,ts_mp01_vbd_xvdf_read_latency," +
                "ts_mp01_vbd_xvdf_write,ts_mp01_vbd_xvdf_read,ts_mp01_memory_target,ts_mp01_vbd_xvda_avgqu_sz," +
                "ts_mp01_vbd_xvda_inflight,ts_mp01_vbd_xvda_io_throughput_read,ts_mp01_vbd_xvda_io_throughput_total," +
                "ts_mp01_vbd_xvda_io_throughput_write,ts_mp01_vbd_xvda_iops_read,ts_mp01_vbd_xvda_iops_total," +
                "ts_mp01_vbd_xvda_iops_write,ts_mp01_vbd_xvda_iowait,ts_mp01_vbd_xvda_latency," +
                "ts_mp01_vbd_xvda_read,ts_mp01_vbd_xvda_read_latency,ts_mp01_vbd_xvda_write," +
                "ts_mp01_vbd_xvda_write_latency,ts_mp01_vbd_xvdb_avgqu_sz,ts_mp01_vbd_xvdb_inflight," +
                "ts_mp01_vbd_xvdb_io_throughput_read,ts_mp01_vbd_xvdb_io_throughput_total," +
                "ts_mp01_vbd_xvdb_io_throughput_write,ts_mp01_vbd_xvdb_iops_read,ts_mp01_vbd_xvdb_iops_total," +
                "ts_mp01_vbd_xvdb_iops_write,ts_mp01_vbd_xvdb_iowait,ts_mp01_vbd_xvdb_latency," +
                "ts_mp01_vbd_xvdb_read,ts_mp01_vbd_xvdb_read_latency,ts_mp01_vbd_xvdb_write," +
                "ts_mp01_vbd_xvdb_write_latency,ts_mp01_vbd_xvdc_avgqu_sz,ts_mp01_vbd_xvdc_inflight," +
                "ts_mp01_vbd_xvdc_io_throughput_read,ts_mp01_vbd_xvdc_io_throughput_total," +
                "ts_mp01_vbd_xvdc_io_throughput_write,ts_mp01_vbd_xvdc_iops_read,ts_mp01_vbd_xvdc_iops_total," +
                "ts_mp01_vbd_xvdc_iops_write,ts_mp01_vbd_xvdc_iowait,ts_mp01_vbd_xvdc_latency," +
                "ts_mp01_vbd_xvdc_read,ts_mp01_vbd_xvdc_read_latency,ts_mp01_vbd_xvdc_write," +
                "ts_mp01_vbd_xvdc_write_latency,ts_mp01_vbd_xvdg_avgqu_sz,ts_mp01_vbd_xvdg_inflight," +
                "ts_mp01_vbd_xvdg_io_throughput_read,ts_mp01_vbd_xvdg_io_throughput_total," +
                "ts_mp01_vbd_xvdg_io_throughput_write,ts_mp01_vbd_xvdg_iops_read,ts_mp01_vbd_xvdg_iops_total," +
                "ts_mp01_vbd_xvdg_iops_write,ts_mp01_vbd_xvdg_iowait,ts_mp01_vbd_xvdg_latency," +
                "ts_mp01_vbd_xvdg_read,ts_mp01_vbd_xvdg_read_latency,ts_mp01_vbd_xvdg_write," +
                "ts_mp01_vbd_xvdg_write_latency,ro2_memory,ro2_cpu7,ro2_cpu6,ro2_cpu5,ro2_cpu4,ro2_cpu3,ro2_cpu2," +
                "ro2_cpu1,ro2_cpu0,ro2_vif_0_tx,ro2_vif_0_rx,ro2_vbd_xvda_avgqu_sz,ro2_vbd_xvda_latency," +
                "ro2_vbd_xvde_avgqu_sz,ro2_vbd_xvde_latency,ro2_vbd_xvdc_avgqu_sz,ro2_vbd_xvdc_latency," +
                "ro2_vbd_xvdb_avgqu_sz,ro2_vbd_xvdb_latency,ro2_vbd_xvda_inflight,ro2_vbd_xvda_iowait," +
                "ro2_vbd_xvda_iops_total,ro2_vbd_xvda_iops_write,ro2_vbd_xvda_iops_read," +
                "ro2_vbd_xvda_io_throughput_total,ro2_vbd_xvda_io_throughput_write," +
                "ro2_vbd_xvda_io_throughput_read,ro2_vbd_xvda_write_latency,ro2_vbd_xvda_read_latency," +
                "ro2_vbd_xvda_write,ro2_vbd_xvda_read,ro2_vbd_xvde_inflight,ro2_vbd_xvde_iowait," +
                "ro2_vbd_xvde_iops_total,ro2_vbd_xvde_iops_write,ro2_vbd_xvde_iops_read," +
                "ro2_vbd_xvde_io_throughput_total,ro2_vbd_xvde_io_throughput_write," +
                "ro2_vbd_xvde_io_throughput_read,ro2_vbd_xvde_write_latency,ro2_vbd_xvde_read_latency," +
                "ro2_vbd_xvde_write,ro2_vbd_xvde_read,ro2_vbd_xvdc_inflight,ro2_vbd_xvdc_iowait," +
                "ro2_vbd_xvdc_iops_total,ro2_vbd_xvdc_iops_write,ro2_vbd_xvdc_iops_read," +
                "ro2_vbd_xvdc_io_throughput_total,ro2_vbd_xvdc_io_throughput_write," +
                "ro2_vbd_xvdc_io_throughput_read,ro2_vbd_xvdc_write_latency,ro2_vbd_xvdc_read_latency," +
                "ro2_vbd_xvdc_write,ro2_vbd_xvdc_read,ro2_vbd_xvdb_inflight,ro2_vbd_xvdb_iowait," +
                "ro2_vbd_xvdb_iops_total,ro2_vbd_xvdb_iops_write,ro2_vbd_xvdb_iops_read," +
                "ro2_vbd_xvdb_io_throughput_total,ro2_vbd_xvdb_io_throughput_write," +
                "ro2_vbd_xvdb_io_throughput_read,ro2_vbd_xvdb_write_latency,ro2_vbd_xvdb_read_latency," +
                "ro2_vbd_xvdb_write,ro2_vbd_xvdb_read,ro2_memory_internal_free,ro2_memory_target," +
                "ro2_vbd_xvdf_avgqu_sz,ro2_vbd_xvdf_inflight,ro2_vbd_xvdf_io_throughput_read," +
                "ro2_vbd_xvdf_io_throughput_total,ro2_vbd_xvdf_io_throughput_write,ro2_vbd_xvdf_iops_read," +
                "ro2_vbd_xvdf_iops_total,ro2_vbd_xvdf_iops_write,ro2_vbd_xvdf_iowait,ro2_vbd_xvdf_latency," +
                "ro2_vbd_xvdf_read,ro2_vbd_xvdf_read_latency,ro2_vbd_xvdf_write,ro2_vbd_xvdf_write_latency," +
                "ro2_vbd_xvdg_avgqu_sz,ro2_vbd_xvdg_inflight,ro2_vbd_xvdg_io_throughput_read," +
                "ro2_vbd_xvdg_io_throughput_total,ro2_vbd_xvdg_io_throughput_write,ro2_vbd_xvdg_iops_read," +
                "ro2_vbd_xvdg_iops_total,ro2_vbd_xvdg_iops_write,ro2_vbd_xvdg_iowait,ro2_vbd_xvdg_latency," +
                "ro2_vbd_xvdg_read,ro2_vbd_xvdg_read_latency,ro2_vbd_xvdg_write,ro2_vbd_xvdg_write_latency," +
                "tw_cho01_cpu0,tw_cho01_cpu1,tw_cho01_cpu2,tw_cho01_cpu3,tw_cho01_memory," +
                "tw_cho01_vbd_xvda_avgqu_sz,tw_cho01_vbd_xvda_inflight,tw_cho01_vbd_xvda_io_throughput_read," +
                "tw_cho01_vbd_xvda_io_throughput_total,tw_cho01_vbd_xvda_io_throughput_write," +
                "tw_cho01_vbd_xvda_iops_read,tw_cho01_vbd_xvda_iops_total,tw_cho01_vbd_xvda_iops_write," +
                "tw_cho01_vbd_xvda_iowait,tw_cho01_vbd_xvda_latency,tw_cho01_vbd_xvda_read," +
                "tw_cho01_vbd_xvda_read_latency,tw_cho01_vbd_xvda_write,tw_cho01_vbd_xvda_write_latency," +
                "tw_cho01_vif_1_rx,tw_cho01_vif_1_tx,tw_cho01_memory_target,rh2_memory,rh2_cpu31,rh2_cpu30," +
                "rh2_cpu29,rh2_cpu28,rh2_cpu27,rh2_cpu26,rh2_cpu25,rh2_cpu24,rh2_cpu23,rh2_cpu22,rh2_cpu21," +
                "rh2_cpu20,rh2_cpu19,rh2_cpu18,rh2_cpu17,rh2_cpu16,rh2_cpu15,rh2_cpu14,rh2_cpu13,rh2_cpu12," +
                "rh2_cpu11,rh2_cpu10,rh2_cpu9,rh2_cpu8,rh2_cpu7,rh2_cpu6,rh2_cpu5,rh2_cpu4,rh2_cpu3," +
                "rh2_cpu2,rh2_cpu1,rh2_cpu0,rh2_vif_1_tx,rh2_vif_1_rx,rh2_vbd_xvdc_avgqu_sz," +
                "rh2_vbd_xvdc_latency,rh2_vbd_xvde_avgqu_sz,rh2_vbd_xvde_latency,rh2_vbd_xvdc_inflight," +
                "rh2_vbd_xvdc_iowait,rh2_vbd_xvdc_iops_total,rh2_vbd_xvdc_iops_write,rh2_vbd_xvdc_iops_read," +
                "rh2_vbd_xvdc_io_throughput_total,rh2_vbd_xvdc_io_throughput_write," +
                "rh2_vbd_xvdc_io_throughput_read,rh2_vbd_xvdc_write_latency,rh2_vbd_xvdc_read_latency," +
                "rh2_vbd_xvdc_write,rh2_vbd_xvdc_read,rh2_vbd_xvde_inflight,rh2_vbd_xvde_iowait," +
                "rh2_vbd_xvde_iops_total,rh2_vbd_xvde_iops_write,rh2_vbd_xvde_iops_read," +
                "rh2_vbd_xvde_io_throughput_total,rh2_vbd_xvde_io_throughput_write," +
                "rh2_vbd_xvde_io_throughput_read,rh2_vbd_xvde_write_latency,rh2_vbd_xvde_read_latency," +
                "rh2_vbd_xvde_write,rh2_vbd_xvde_read,rh2_memory_internal_free,rh2_memory_target," +
                "rh2_vbd_xvda_avgqu_sz,rh2_vbd_xvda_inflight,rh2_vbd_xvda_io_throughput_read," +
                "rh2_vbd_xvda_io_throughput_total,rh2_vbd_xvda_io_throughput_write,rh2_vbd_xvda_iops_read," +
                "rh2_vbd_xvda_iops_total,rh2_vbd_xvda_iops_write,rh2_vbd_xvda_iowait,rh2_vbd_xvda_latency," +
                "rh2_vbd_xvda_read,rh2_vbd_xvda_read_latency,rh2_vbd_xvda_write,rh2_vbd_xvda_write_latency," +
                "rh2_vbd_xvdb_avgqu_sz,rh2_vbd_xvdb_inflight,rh2_vbd_xvdb_io_throughput_read," +
                "rh2_vbd_xvdb_io_throughput_total,rh2_vbd_xvdb_io_throughput_write,rh2_vbd_xvdb_iops_read," +
                "rh2_vbd_xvdb_iops_total,rh2_vbd_xvdb_iops_write,rh2_vbd_xvdb_iowait,rh2_vbd_xvdb_latency," +
                "rh2_vbd_xvdb_read,rh2_vbd_xvdb_read_latency,rh2_vbd_xvdb_write,rh2_vbd_xvdb_write_latency," +
                "rh2_vbd_xvdf_avgqu_sz,rh2_vbd_xvdf_inflight,rh2_vbd_xvdf_io_throughput_read," +
                "rh2_vbd_xvdf_io_throughput_total,rh2_vbd_xvdf_io_throughput_write,rh2_vbd_xvdf_iops_read," +
                "rh2_vbd_xvdf_iops_total,rh2_vbd_xvdf_iops_write,rh2_vbd_xvdf_iowait,rh2_vbd_xvdf_latency," +
                "rh2_vbd_xvdf_read,rh2_vbd_xvdf_read_latency,rh2_vbd_xvdf_write,rh2_vbd_xvdf_write_latency";
    }
}