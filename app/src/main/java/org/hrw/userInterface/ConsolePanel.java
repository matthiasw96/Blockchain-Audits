package org.hrw.userInterface;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Panel that redirects {@link System#out} and {@link System#err} output
 * into a scrollable {@link JTextArea} inside the application UI.
 *
 * <p>This allows console logs and status messages to be displayed directly
 * in the graphical user interface instead of a separate terminal window.</p>
 */
public class ConsolePanel extends JPanel {
    private final JTextArea area = new JTextArea(20, 80);

    public ConsolePanel() {
        super(new BorderLayout());
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        ((DefaultCaret) area.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    /**
     * Redirects the global {@link System#out} and {@link System#err} streams
     * to this panel's text area.
     *
     * <p>After calling this method, all standard output and error messages
     * will be appended to the console view.</p>
     */
    public void hookSystemStreams() {
        PrintStream outPs = new PrintStream(new TextAreaOutputStream(area), true);
        System.setOut(outPs);
        System.setErr(outPs);
    }

    /**
     * {@link OutputStream} implementation that appends written text
     * line by line to a {@link JTextArea} on the Swing event dispatch thread.
     */
    private static class TextAreaOutputStream extends OutputStream {
        private final JTextArea area;
        private final StringBuilder buffer = new StringBuilder();
        TextAreaOutputStream(JTextArea area) { this.area = area; }

        /**
         * Writes a single byte to the internal buffer.
         *
         * <p>Carriage returns ({@code '\r'}) are ignored. Line feeds ({@code '\n'})
         * trigger a flush of the current buffer content, causing the line to be
         * appended to the text area.</p>
         *
         * @param b the byte to write
         */
        @Override
        public void write(int b) {
            if (b == '\r') return; // ignore CR
            if (b == '\n') {
                flush();
            } else {
                buffer.append((char) b);
            }
        }

        /**
         * Writes a sequence of bytes to this output stream.
         *
         * <p>The method delegates to {@link #write(int)} for each byte to ensure
         * consistent handling of newline characters and buffering.</p>
         *
         * @param b   the byte array containing data
         * @param off the start offset in the array
         * @param len the number of bytes to write
         */
        @Override
        public void write(byte[] b, int off, int len) {
            for (int i = off; i < off + len; i++) write(b[i]);
        }

        /**
         * Flushes the current buffer content and appends it to the text area as a new line.
         *
         * <p>If the buffer is empty, nothing is written. The actual UI update is executed
         * on the Swing event dispatch thread.</p>
         */
        @Override
        public void flush() {
            if (buffer.isEmpty()) return;
            String line = buffer.toString();
            buffer.setLength(0);
            SwingUtilities.invokeLater(() -> area.append(line + System.lineSeparator()));
        }
    }
}