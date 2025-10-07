package org.hrw.userInterface;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class ConsolePanel extends JPanel {
    private final JTextArea area = new JTextArea(20, 80);

    public ConsolePanel() {
        super(new BorderLayout());
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        // auto-scroll
        ((DefaultCaret) area.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    /** Redirects System.out and System.err into this panel. Call once. */
    public void hookSystemStreams() {
        PrintStream outPs = new PrintStream(new TextAreaOutputStream(area), true);
        System.setOut(outPs);
        System.setErr(outPs);
    }

    /** If you just want a logger-like API */
    public void println(String s) {
        SwingUtilities.invokeLater(() -> area.append(s + System.lineSeparator()));
    }

    /** Optional: clear / limit size */
    public void clear() { area.setText(""); }

    // --- OutputStream that safely appends to the JTextArea ---
    private static class TextAreaOutputStream extends OutputStream {
        private final JTextArea area;
        private final StringBuilder buffer = new StringBuilder();
        TextAreaOutputStream(JTextArea area) { this.area = area; }

        @Override public void write(int b) {
            if (b == '\r') return; // ignore CR
            if (b == '\n') {
                flushLine();
            } else {
                buffer.append((char) b);
            }
        }
        @Override public void write(byte[] b, int off, int len) {
            for (int i = off; i < off + len; i++) write(b[i]);
        }
        @Override public void flush() { flushLine(); }

        private void flushLine() {
            if (buffer.isEmpty()) return;
            String line = buffer.toString();
            buffer.setLength(0);
            SwingUtilities.invokeLater(() -> area.append(line + System.lineSeparator()));
        }
    }
}


