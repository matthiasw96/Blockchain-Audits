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

public class AuditPdfCreator {

    private final DateTimeFormatter FORMATTER;
    private final float margin = 50;
    private final float leading = 14;

    public AuditPdfCreator(DateTimeFormatter FORMATTER) {
        this.FORMATTER = FORMATTER;
    }

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

    private String createMinTimestamp(List<ServerRecord> data) {
        String unixTimestamp = data.stream()
                .map(ServerRecord::timestamp)
                .min(Comparator.naturalOrder())
                .orElse("n/a");
        return formatUnixSeconds(unixTimestamp);
    }

    private String createMaxTimestamp(List<ServerRecord> data) {
        String unixTimestamp =  data.stream()
                .map(ServerRecord::timestamp)
                .max(Comparator.naturalOrder())
                .orElse("n/a");
        return formatUnixSeconds(unixTimestamp);
    }

    private float createTitle(float y, PDPageContentStream cs) throws IOException {
        cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
        y = writeLine(cs, "Audit Report – Telemetriedaten", margin, y, leading);

        cs.setFont(PDType1Font.HELVETICA, 11);
        y -= leading;
        return y;
    }

    private float createMetaData(float y,
                                 PDPageContentStream cs,
                                 String minTs,
                                 String maxTs,
                                 AuditSummary summary) throws IOException {
        y = writeLine(cs, "1. Metadaten", margin, y, leading);
        y = writeLine(cs, "Report-ID: " + summary.reportId(), margin + 20, y, leading);
        y = writeLine(cs, "Audit-Tool-Version: " + summary.toolVersion(), margin + 20, y, leading);
        y = writeLine(cs, "Erstellungszeitpunkt: " + FORMATTER.format(Instant.now()),
                margin + 20, y, leading);
        y = writeLine(cs, "Audit-Zeitraum (Daten): " + minTs + " – " + maxTs,
                margin + 20, y, leading);

        y -= leading;
        return y;
    }

    private float createAuditContext(float y, PDPageContentStream cs, AuditSummary summary) throws IOException {
        y = writeLine(cs, "2. Audit-Kontext", margin, y, leading);
        y = writeLine(cs, "Datenquelle: " + summary.dataSource(), margin + 20, y, leading);

        if (summary.filterInfo() != null && !summary.filterInfo().isBlank()) {
            y = writeWrappedText(cs, "Filter: " + summary.filterInfo(),
                    margin + 20, y, leading, 500);
        }

        y = writeLine(cs, "Anzahl geprüfter Datensätze: " + summary.totalRecords(),
                margin + 20, y, leading);

        y -= leading;
        return y;
    }

    private float createIntegrityResults(float y, PDPageContentStream cs, AuditSummary summary) throws IOException {
        y = writeLine(cs, "3. Integritätsprüfung", margin, y, leading);

        String statusText = summary.isVerified()
                ? "Integritätsstatus: ERFOLGREICH – alle geprüften Datensätze gelten als verifiziert."
                : "Integritätsstatus: FEHLER – mindestens ein Datensatz konnte nicht erfolgreich verifiziert werden.";

        y = writeWrappedText(cs, statusText, margin + 20, y, leading, 500);

        y -= leading;
        return y;
    }

    private float createSummary(float y, PDPageContentStream cs, AuditSummary summary) throws IOException {
        y = writeLine(cs, "4. Zusammenfassung", margin, y, leading);

        String summaryText = summary.isVerified()
                ? "Im ausgewählten Zeitraum wurden alle Telemetriedaten erfolgreich über Merkle-Tree " +
                "und Blockchain-Verankerung geprüft."
                : "Im ausgewählten Zeitraum traten Abweichungen bei der Integritätsprüfung auf.";

        y = writeWrappedText(cs, summaryText, margin + 20, y, leading, 500);
        y -= leading;
        return y;
    }

    private float writeLine(PDPageContentStream cs,
                            String text,
                            float x,
                            float y,
                            float leading) throws IOException {
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
        return y - leading;
    }

    private float writeWrappedText(PDPageContentStream cs,
                                   String text,
                                   float x,
                                   float y,
                                   float leading,
                                   float maxWidth) throws IOException {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            float textWidth = PDType1Font.HELVETICA.getStringWidth(candidate) / 1000 * 11;

            if (textWidth > maxWidth) {
                y = writeLine(cs, line.toString(), x, y, leading);
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(candidate);
            }
        }

        if (!line.isEmpty()) {
            y = writeLine(cs, line.toString(), x, y, leading);
        }

        return y;
    }

    public String formatUnixSeconds(String unixSeconds) {
        long unixLong = Long.parseLong(unixSeconds);
        Instant instant = Instant.ofEpochSecond(unixLong);
        ZonedDateTime dateTime = instant.atZone(ZoneId.of("Europe/Berlin"));
        return dateTime.format(FORMATTER);
    }

}
