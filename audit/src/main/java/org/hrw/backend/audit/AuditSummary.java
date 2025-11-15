package org.hrw.backend.audit;

public record AuditSummary(
        String reportId,
        String toolVersion,
        String dataSource,
        String filterInfo,
        int totalRecords,
        boolean isVerified
) {}

