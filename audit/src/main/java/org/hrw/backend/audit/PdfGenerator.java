package org.hrw.backend.audit;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.hrw.datamodels.ServerRecord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * Generates an audit report as pdf for a given set of {@link ServerRecord} data
 * and an {@link AuditSummary}.
 *
 * <p>The generated report contains:</p>
 * <ol>
 *     <li>Title section</li>
 *     <li>Metadata (report ID, tool version, time range)</li>
 *     <li>Audit context (data source, filters, number of records)</li>
 *     <li>Integrity check result (success / failure)</li>
 *     <li>Short textual summary</li>
 * </ol>
 *
 * <p>PDF creation is implemented using Apache PDFBox and returned as a byte array
 * suitable for download or storage.</p>
 */
public class PdfGenerator {

    private final DateTimeFormatter FORMATTER;
    private final float margin = 50;
    private final float leading = 14;

    public PdfGenerator(DateTimeFormatter FORMATTER) {
        this.FORMATTER = FORMATTER;
    }

    /**
     * Generates a complete audit report as a PDF document.
     *
     * <p>The method determines the minimum and maximum timestamp from the given
     * {@link ServerRecord} list, creates a single-page A4 PDF, writes the
     * different report sections and returns the document as a byte array.</p>
     *
     * @param data list of audited server records
     * @param summary summary metadata of the audit
     * @return PDF file content as a byte array
     * @throws IOException if PDF creation fails
     */
    public byte[] generateReport(List<ServerRecord> data, AuditSummary summary) throws IOException {
        System.out.println(LocalDateTime.now().format(FORMATTER) + ": Generating audit report");

        String minTs = createMinTimestamp(data);
        String maxTs = createMaxTimestamp(data);

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                float y = page.getMediaBox().getHeight() - margin;

                y = createTitle(y, cs);
                y = createMetaData(y, cs, minTs, maxTs, summary);
                y = createAuditContext(y, cs, summary);
                y = createIntegrityResults(y, cs, summary);
                y = createSummary(y, cs, summary);
            }
            document.save(baos);

            byte[] report = baos.toByteArray();

            System.out.println(LocalDateTime.now().format(FORMATTER) + ": Audit report generated");
            return report;
        }
    }

    /**
     * Determines the minimum timestamp of the provided data set and converts it
     * into a date/time string.
     *
     * @param data list of server records
     * @return formatted minimum timestamp, or {@code "n/a"} if none is present
     */
    private String createMinTimestamp(List<ServerRecord> data) {
        String unixTimestamp = data.stream()
                .map(ServerRecord::timestamp)
                .min(Comparator.naturalOrder())
                .orElse("n/a");
        return formatUnixSeconds(unixTimestamp);
    }


    /**
     * Determines the maximum timestamp of the provided data set and converts it
     * into a date/time string.
     *
     * @param data list of server records
     * @return formatted maximum timestamp, or {@code "n/a"} if none is present
     */
    private String createMaxTimestamp(List<ServerRecord> data) {
        String unixTimestamp =  data.stream()
                .map(ServerRecord::timestamp)
                .max(Comparator.naturalOrder())
                .orElse("n/a");
        return formatUnixSeconds(unixTimestamp);
    }

    /**
     * Writes the report title and prepares the font for subsequent sections.
     *
     * @param y  current vertical position
     * @param cs PDF content stream
     * @return new vertical position after writing
     */
    private float createTitle(float y, PDPageContentStream cs) throws IOException {
        cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
        y = writeLine(cs, "Audit Report – Systemmetriken", margin, y);

        cs.setFont(PDType1Font.HELVETICA, 11);
        y -= leading;
        return y;
    }

    /**
     * Writes the metadata section (report ID, tool version, timestamps).
     */
     private float createMetaData(float y,
                                 PDPageContentStream cs,
                                 String minTs,
                                 String maxTs,
                                 AuditSummary summary) throws IOException {
        y = writeLine(cs, "1. Metadaten", margin, y);
        y = writeLine(cs, "Report-ID: " + summary.reportId(), margin + 20, y);
        y = writeLine(cs, "Audit-Tool-Version: " + summary.toolVersion(), margin + 20, y);
        y = writeLine(cs, "Erstellungszeitpunkt: " + FORMATTER.format(Instant.now()),
                margin + 20, y);
        y = writeLine(cs, "Audit-Zeitraum (Daten): " + minTs + " – " + maxTs,
                margin + 20, y);

        y -= leading;
        return y;
    }

    /**
     * Writes the audit context section (data source, filters, record count).
     */
    private float createAuditContext(float y, PDPageContentStream cs, AuditSummary summary) throws IOException {
        y = writeLine(cs, "2. Audit-Kontext", margin, y);
        y = writeLine(cs, "Datenquelle: " + summary.dataSource(), margin + 20, y);

        y = writeWrappedText(cs, "Filter: " + summary.filterInfo(), y);

        y = writeLine(cs, "Anzahl geprüfter Datensätze: " + summary.totalRecords(),
                margin + 20, y);

        y -= leading;
        return y;
    }

    /**
     * Writes the integrity check result section based on {@link AuditSummary#isVerified()}.
     */
    private float createIntegrityResults(float y, PDPageContentStream cs, AuditSummary summary) throws IOException {
        y = writeLine(cs, "3. Integritätsprüfung", margin, y);

        String statusText = summary.isVerified()
                ? "Integritätsstatus: ERFOLGREICH – alle geprüften Datensätze gelten als verifiziert."
                : "Integritätsstatus: FEHLER – mindestens ein Datensatz konnte nicht erfolgreich verifiziert werden.";

        y = writeWrappedText(cs, statusText, y);

        y -= leading;
        return y;
    }

    /**
     * Writes a short textual summary of the audit outcome.
     */
    private float createSummary(float y, PDPageContentStream cs, AuditSummary summary) throws IOException {
        y = writeLine(cs, "4. Zusammenfassung", margin, y);

        String summaryText = summary.isVerified()
                ? "Im ausgewählten Zeitraum wurden alle Telemetriedaten erfolgreich über Merkle-Tree " +
                "und Blockchain-Verankerung geprüft."
                : "Im ausgewählten Zeitraum traten Abweichungen bei der Integritätsprüfung auf.";

        y = writeWrappedText(cs, summaryText, y);
        y -= leading;
        return y;
    }

    /**
     * Writes a single line of text at the given coordinates and moves the y-position
     * down by one line height.
     *
     * @param cs PDF content stream
     * @param text text to write
     * @param x horizontal position
     * @param y vertical position
     * @return new vertical position after writing
     */
    private float writeLine(PDPageContentStream cs,
                            String text,
                            float x,
                            float y) throws IOException {
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
        return y - leading;
    }

    /**
     * Writes a paragraph of text with simple word wrapping.
     *
     * <p>The text is split on spaces, and lines are wrapped once a maximum
     * width is exceeded. Each line is written via {@link #writeLine}.</p>
     *
     * @param cs PDF content stream
     * @param text text to wrap and write
     * @param y starting vertical position
     * @return new vertical position after writing the wrapped text
     */
    private float writeWrappedText(PDPageContentStream cs,
                                   String text,
                                   float y) throws IOException {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            float textWidth = PDType1Font.HELVETICA.getStringWidth(candidate) / 1000 * 11;

            if (textWidth > 500) {
                y = writeLine(cs, line.toString(), 70.0f, y);
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(candidate);
            }
        }

        if (!line.isEmpty()) {
            y = writeLine(cs, line.toString(), 70.f, y);
        }

        return y;
    }

    /**
     * Converts a Unix timestamp (in seconds, as string) to a formatted date-time
     * string using the configured formatter and the Europe/Berlin time zone.
     *
     * @param unixSeconds epoch seconds as string
     * @return formatted date-time string
     */
    public String formatUnixSeconds(String unixSeconds) {
        long unixLong = Long.parseLong(unixSeconds);
        Instant instant = Instant.ofEpochSecond(unixLong);
        ZonedDateTime dateTime = instant.atZone(ZoneId.of("Europe/Berlin"));
        return dateTime.format(FORMATTER);
    }
}