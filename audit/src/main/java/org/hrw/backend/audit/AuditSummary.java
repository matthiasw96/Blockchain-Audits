package org.hrw.backend.audit;

/**
 * Summary information used for generating and displaying audit reports.
 *
 * <p>An {@code AuditSummary} bundles the essential metadata about a performed audit,
 * including:</p>
 *
 * <ul>
 *     <li>{@code reportId} – unique identifier for the generated report</li>
 *     <li>{@code toolVersion} – version of the audit tool used</li>
 *     <li>{@code dataSource} – source of the verified data (server ip address)</li>
 *     <li>{@code filterInfo} – description of the selected time range or filters applied</li>
 *     <li>{@code totalRecords} – number of telemetry records included in the audit</li>
 *     <li>{@code isVerified} – {@code true} if all hashes match the blockchain,
 *         {@code false} otherwise</li>
 * </ul>
 *
 * <p>This structure is used by the audit backend and the {@link PdfGenerator}
 * to present a concise overview of the audit results.</p>
 */
public record AuditSummary(
        String reportId,
        String toolVersion,
        String dataSource,
        String filterInfo,
        int totalRecords,
        boolean isVerified
) {}

