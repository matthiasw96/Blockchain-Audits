package org.hrw.logging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public final class Logger {
    private final Supplier<Connection> connectionFactory;

    public enum Stage { SCHEDULER, SERVER, HASHER, ANCHOR, PROCESS, DB, JOB }
    public enum Status { STARTED, OK, FAIL, ERROR, DONE_OK, DONE_FAIL }

    public Logger(Supplier<Connection> connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /** Start of one scheduled run. Returns a jobId you pass to subsequent log calls. */
    public UUID startJob(String message, Map<String, ?> details) throws SQLException {
        UUID jobId = UUID.randomUUID();
        log(jobId, Stage.JOB, Status.STARTED, message, details);
        return jobId;
    }

    /** End of one run (success/failure). */
    public void endJob(UUID jobId, boolean success, String message, Map<String, ?> details) throws SQLException {
        log(jobId, Stage.JOB, success ? Status.DONE_OK : Status.DONE_FAIL, message, details);
    }

    /** Log any stage during the run. */
    public void log(UUID jobId, Stage stage, Status status, String message, Map<String, ?> details) throws SQLException {
        String sql = "INSERT INTO job_logs (job_id, ts_utc, stage, status, message, details) " +
                "VALUES (?, ?, ?, ?, ?, to_jsonb(?::json) )";
        try (Connection conn = connectionFactory.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, jobId);
            ps.setTimestamp(2, Timestamp.from(Instant.now()));
            ps.setString(3, stage.name());
            ps.setString(4, status.name());
            ps.setString(5, message);
            ps.setString(6, details == null ? null : toJson(details));
            ps.executeUpdate();
        }
    }

    // Super-small JSON encoder (good enough for simple maps with primitives/strings).
    private static String toJson(Map<String, ?> map) {
        if (map == null || map.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (var e : map.entrySet()) {
            if (!first) sb.append(',');
            first = false;
            sb.append('\"').append(escape(e.getKey())).append('\"').append(':');
            Object v = e.getValue();
            if (v == null) sb.append("null");
            else if (v instanceof Number || v instanceof Boolean) sb.append(v);
            else sb.append('\"').append(escape(v.toString())).append('\"');
        }
        sb.append('}');
        return sb.toString();
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}