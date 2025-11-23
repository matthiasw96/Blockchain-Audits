package org.hrw.userInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

/**
 * Graphical user interface for the configuration of the data collection and blockchain
 * anchoring application.
 *
 * <p>This window allows the user to configure:</p>
 * <ul>
 *     <li>server credentials</li>
 *     <li>database credentials</li>
 *     <li>anchor service seed phrase</li>
 *     <li>execution schedule</li>
 * </ul>
 *
 * <p>Additionally, buttons allow starting/stopping the processor job and
 * opening a log viewer.</p>
 *
 * <p>User actions trigger handlers provided via
 * {@link #setOnConfirm(Consumer)}, {@link #setOnStart(Runnable)} and
 * {@link #setOnStop(Runnable)}.</p>
 */
public class UserInterface extends JFrame {
    private Consumer<UIConfig> onConfirm;
    private Runnable onStart;
    private Runnable onStop;

    private final JTextField userServer = new JTextField();
    private final JPasswordField passServer = new JPasswordField();

    private final JTextField userDb = new JTextField();
    private final JPasswordField passDb = new JPasswordField();

    private final JTextField seedPhraseAnchor = new JTextField();

    private final JFormattedTextField dateOfFirstExecution = dateTimeField();

    private final  JButton confirmBtn = new JButton("Confirm");
    private final JButton startBtn = new JButton("Start");
    private final JButton stopBtn = new JButton("Stop");
    private final JButton logViewerBtn = new JButton("Log Viewer");

    private final JLabel status = new JLabel("Idle");

    /**
     * Builds the full graphical configuration interface, initializes layout,
     * registers event handlers and prepares UI elements.
     */
    public UserInterface() {
        super("Blockchain Audits – Config");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(720, 560));

        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(16, 16, 16, 16));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0;

        int row = 0;
        row = sectionLabel(content, g, row, "Server");
        row = addRow(content, g, row, "User Server", userServer);
        row = addRow(content, g, row, "Password Server", passServer);

        row = sectionLabel(content, g, row, "Database");
        row = addRow(content, g, row, "User Database", userDb);
        row = addRow(content, g, row, "Pass Database", passDb);

        row = sectionLabel(content, g, row, "Anchor Service");
        row = addRow(content, g, row, "Seed Phrase Anchor", seedPhraseAnchor);

        row = sectionLabel(content, g, row, "Schedule");
        row = addRow(content, g, row, "Date Of First Execution (yyyy-MM-dd HH:mm:ss)", dateOfFirstExecution);

        add(content, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        controls.add(confirmBtn);
        controls.add(startBtn);
        controls.add(stopBtn);
        controls.add(logViewerBtn);
        controls.add(new JLabel("Status:"));
        controls.add(status);
        add(controls, BorderLayout.SOUTH);

        startBtn.setEnabled(false);
        stopBtn.setEnabled(false);
        dateOfFirstExecution.setValue(new Date());

        confirmBtn.addActionListener(this::onConfirm);
        startBtn.addActionListener(this::onStart);
        stopBtn.addActionListener(this::onStop);
        logViewerBtn.addActionListener(this::onOpenLogViewer);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Handles the "Confirm" button. Validates fields and forwards the
     * config through {@link #onConfirm}.
     */
    private void onConfirm(ActionEvent e) {
        try {
            UIConfig cfg = readConfig();
            this.onConfirm.accept(cfg);
            confirmBtn.setEnabled(false);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid input", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Handles the "Start" button and triggers the externally provided start action.
     */
    private void onStart(ActionEvent e) {
        status.setText("Running (every 5 minutes)");
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);
        this.onStart.run();
    }

    /**
     * Handles the "Stop" button and triggers the externally provided stop action.
     */
    private void onStop(ActionEvent e) {
        status.setText("Stopped");
        startBtn.setEnabled(true);
        stopBtn.setEnabled(false);
        this.onStop.run();
    }

    /**
     * Opens a new dialog window showing live console output.
     * Uses {@link ConsolePanel} to attach stdout/stderr.
     */
    private void onOpenLogViewer(ActionEvent e) {
        JDialog dlg = new JDialog(this, "Console Output", false);
        ConsolePanel console = new ConsolePanel();
        console.hookSystemStreams();
        dlg.setContentPane(console);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    /**
     * Reads and validates user-provided settings from the UI.
     *
     * @return a populated {@link UIConfig}
     * @throws IllegalArgumentException if required fields are empty
     */
    private UIConfig readConfig() {
        String userSrv = reqText(userServer, "User Server");
        String passSrv = new String(passServer.getPassword());

        String uDb = reqText(userDb, "User Database");
        String pDbPass = new String(passDb.getPassword());

        String seedPhrase = reqText(seedPhraseAnchor, "Seed Phrase Anchor");

        Date first = (Date) dateOfFirstExecution.getValue();

        return new UIConfig(userSrv, passSrv, uDb, pDbPass, seedPhrase, first);
    }

    /**
     * Ensures a text field contains non-empty input.
     *
     * @throws IllegalArgumentException if the field is empty
     */
    private static String reqText(JTextField field, String name) {
        String v = field.getText().trim();
        if (v.isEmpty()) throw new IllegalArgumentException(name + " must not be empty");
        return v;
    }

    /**
     * Adds a labeled input row to a {@link JPanel} using the given
     * {@link GridBagConstraints}.
     *
     * <p>The row consists of a text label in the first column and the provided
     * input component in the second column. The updated row index is returned
     * to allow chaining in layout construction.</p>
     *
     * @param panel the panel to which the row is added
     * @param g grid bag constraints used for placement
     * @param row current row index
     * @param label text label describing the input field
     * @param input the input component to place next to the label
     * @return the next row index
     */
    private static int addRow(JPanel panel, GridBagConstraints g, int row, String label, JComponent input) {
        g.gridx = 0; g.gridy = row; g.weightx = 0; panel.add(new JLabel(label + ":"), g);
        g.gridx = 1; g.gridy = row; g.weightx = 1; panel.add(input, g);
        return row + 1;
    }

    /**
     * Adds a section heading label to the form layout.
     *
     * <p>The label spans two columns, appears in bold, and visually groups
     * related input rows under a common section title.</p>
     *
     * @param panel the panel to which the section header is added
     * @param g grid bag constraints used for placement
     * @param row current row index
     * @param text section title text
     * @return the next row index
     */
    private static int sectionLabel(JPanel panel, GridBagConstraints g, int row, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        g.gridx = 0; g.gridy = row; g.gridwidth = 2; g.weightx = 1;
        panel.add(lbl, g);
        g.gridwidth = 1;
        return row + 1;
    }

    /**
     * Creates a {@link JFormattedTextField} for entering date-time values
     * in the format {@code yyyy-MM-dd HH:mm:ss}.
     *
     * <p>The returned field overrides {@link JFormattedTextField#getValue()}
     * to ensure that the value is always resolved to a valid {@link Date},
     * falling back to the current date on parsing errors.</p>
     *
     * @return a preconfigured date-time input field
     */
    private static JFormattedTextField dateTimeField() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JFormattedTextField field = new JFormattedTextField(sdf) {
            @Override
            public Object getValue() {
                Object v = super.getValue();
                if (v instanceof Date) return v;
                try {
                    return sdf.parse(getText());
                } catch (ParseException e) {
                    return new Date();
                }
            }
        };
        field.setColumns(16);
        field.setValue(new Date());
        return field;
    }

    /**
     * Container for configuration values read from the UI.
     */
    public record UIConfig(
            String userServer, String passServer,
            String userDb, String passDb,
            String seedPhraseAnchor,
            Date dateOfFirstExecution
    ) {
    }

    public void setStartEnabled(boolean enabled) { startBtn.setEnabled(enabled); }
    public void setOnStart(Runnable handler) { this.onStart = handler; }
    public void setOnConfirm(Consumer<UIConfig> handler) { this.onConfirm = handler; }
    public void setOnStop(Runnable handler) { this.onStop = handler; }
}