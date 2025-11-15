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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class AuditPdfCreator {

    private static final DateTimeFormatter DISPLAY_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.of("Europe/Berlin"));

    /**
     * Erzeugt einen kompakten Audit-Report als PDF und gibt ihn als byte[] zurück.
     */
    public byte[] generateReport(List<ServerRecord> data,
                                 AuditSummary summary) throws IOException {

        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Liste der ServerRecord-Daten darf nicht leer sein.");
        }

        // Zeitbereich aus den Daten bestimmen (auf Basis des String-Timestamps)
        String minTs = data.stream()
                .map(ServerRecord::timestamp)
                .min(Comparator.naturalOrder())
                .orElse("n/a");

        String maxTs = data.stream()
                .map(ServerRecord::timestamp)
                .max(Comparator.naturalOrder())
                .orElse("n/a");

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {

                float margin = 50;
                float y = page.getMediaBox().getHeight() - margin;
                float leading = 14;

                // Titel
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                y = writeLine(cs, "Audit Report – Telemetriedaten", margin, y, leading);

                cs.setFont(PDType1Font.HELVETICA, 11);
                y -= leading;

                // 1. Metadaten
                y = writeLine(cs, "1. Metadaten", margin, y, leading);
                y = writeLine(cs, "Report-ID: " + summary.reportId(), margin + 20, y, leading);
                y = writeLine(cs, "Audit-Tool-Version: " + summary.toolVersion(), margin + 20, y, leading);
                y = writeLine(cs, "Erstellungszeitpunkt: " + DISPLAY_FMT.format(Instant.now()),
                        margin + 20, y, leading);
                y = writeLine(cs, "Audit-Zeitraum (Daten): " + minTs + " – " + maxTs,
                        margin + 20, y, leading);

                y -= leading;

                // 2. Audit-Kontext
                y = writeLine(cs, "2. Audit-Kontext", margin, y, leading);
                y = writeLine(cs, "Datenquelle: " + summary.dataSource(), margin + 20, y, leading);

                if (summary.filterInfo() != null && !summary.filterInfo().isBlank()) {
                    y = writeWrappedText(cs, "Filter: " + summary.filterInfo(),
                            margin + 20, y, leading, 500);
                }

                y = writeLine(cs, "Anzahl geprüfter Datensätze: " + summary.totalRecords(),
                        margin + 20, y, leading);

                y -= leading;

                // 3. Integritätsprüfung (nur boolean isVerified)
                y = writeLine(cs, "3. Integritätsprüfung", margin, y, leading);

                String statusText = summary.isVerified()
                        ? "Integritätsstatus: ERFOLGREICH – alle geprüften Datensätze gelten als verifiziert."
                        : "Integritätsstatus: FEHLER – mindestens ein Datensatz konnte nicht erfolgreich verifiziert werden.";

                y = writeWrappedText(cs, statusText, margin + 20, y, leading, 500);

                y -= leading;

                // 4. Zusammenfassung (Kurztext)
                y = writeLine(cs, "4. Zusammenfassung", margin, y, leading);

                String summaryText = summary.isVerified()
                        ? "Im ausgewählten Zeitraum wurden alle Telemetriedaten erfolgreich über Hash-, Merkle-Tree " +
                        "und Blockchain-Verankerung geprüft."
                        : "Im ausgewählten Zeitraum traten Abweichungen bei der Integritätsprüfung auf. " +
                        "Details können im Audit-Tool eingesehen bzw. exportiert werden.";

                y = writeWrappedText(cs, summaryText, margin + 20, y, leading, 500);

                y -= leading;

                // 5. Datenstruktur (nur als Konzept, Attribute-Übersicht)
                y = writeLine(cs, "5. Datenstruktur (Auszug)", margin, y, leading);
                String attributes = data.get(0).getAttributeNames();
                y = writeWrappedText(cs,
                        "Attribute (CSV-Header-Ansicht): " + attributes,
                        margin + 20, y, leading, 500);
            }

            document.save(baos);
            return baos.toByteArray();
        }
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
            String candidate = line.length() == 0 ? word : line + " " + word;
            float textWidth =
                    PDType1Font.HELVETICA.getStringWidth(candidate) / 1000 * 11;

            if (textWidth > maxWidth) {
                y = writeLine(cs, line.toString(), x, y, leading);
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(candidate);
            }
        }

        if (line.length() > 0) {
            y = writeLine(cs, line.toString(), x, y, leading);
        }

        return y;
    }
}
