package org.hrw.userInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class UserInterface extends JFrame {
    private Consumer<Config> onStart;   // callback for Start
    private Runnable onStop;            // callback for Stop

    // Server
    private final JTextField hostServer = new JTextField("192.168.250.55");
    private final JTextField userServer = new JTextField("root");
    private final JPasswordField passServer = new JPasswordField();

    // DB
    private final JTextField hostDb = new JTextField("192.168.250.51");
    private final JFormattedTextField portDb = intField(5432);
    private final JTextField userDb = new JTextField("postgres");
    private final JPasswordField passDb = new JPasswordField();
    private final JTextField dbName = new JTextField("postgres");

    // Hasher
    private final JTextField hasherAlgorithm = new JTextField("SHA-256");

    // Anchor Service
    private final String[] options = {"TESTNET","STAGENET","MAINNET"};
    private final JTextField seedPhraseAnchor = new JTextField();
    private final JComboBox<String> environmentAnchor = new JComboBox<>(options);
    private final JFormattedTextField intervalAnchor = intField(60);

    // Scheduling
    private final JFormattedTextField timePeriod = intField(60); // seconds
    private final JFormattedTextField dateOfFirstExecution = dateTimeField();

    // Buttons
    private final JButton startBtn = new JButton("Start");
    private final JButton stopBtn = new JButton("Stop");
    private final JButton logViewerBtn = new JButton("Log Viewer");

    // Status
    private final JLabel status = new JLabel("Idle");

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
        row = addRow(content, g, row, "hostServer", hostServer);
        row = addRow(content, g, row, "userServer", userServer);
        row = addRow(content, g, row, "passServer", passServer);

        row = sectionLabel(content, g, row, "Database");
        row = addRow(content, g, row, "hostDb", hostDb);
        row = addRow(content, g, row, "portDb", portDb);
        row = addRow(content, g, row, "userDb", userDb);
        row = addRow(content, g, row, "passDb", passDb);
        row = addRow(content, g, row, "dbName", dbName);

        row = sectionLabel(content, g, row, "Hasher");
        row = addRow(content, g, row, "hasherAlgorithm", hasherAlgorithm);

        row = sectionLabel(content, g, row, "Anchor Service");
        row = addRow(content, g, row, "seedPhraseAnchor", seedPhraseAnchor);
        row = addRow(content, g, row, "environmentAnchor", environmentAnchor);
        row = addRow(content, g, row, "intervalAnchor", intervalAnchor);

        row = sectionLabel(content, g, row, "Schedule");
        row = addRow(content, g, row, "timePeriod (s)", timePeriod);
        row = addRow(content, g, row, "DateOfFirstExecution (yyyy-MM-dd HH:mm:ss)", dateOfFirstExecution);

        add(content, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        controls.add(startBtn);
        controls.add(stopBtn);
        controls.add(logViewerBtn);
        controls.add(new JLabel("Status:"));
        controls.add(status);
        add(controls, BorderLayout.SOUTH);

        // Initial states
        stopBtn.setEnabled(false);
        dateOfFirstExecution.setValue(new Date());

        // Actions
        startBtn.addActionListener(this::onStart);
        stopBtn.addActionListener(this::onStop);
        logViewerBtn.addActionListener(this::onOpenLogViewer);

        pack();
        setLocationRelativeTo(null);
    }

    private void onStart(ActionEvent e) {
        try {
            Config cfg = readConfig();
            status.setText("Running (every " + cfg.timePeriodSeconds + " s)");
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            this.onStart.accept(cfg);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onStop(ActionEvent e) {
        status.setText("Stopped");
        startBtn.setEnabled(true);
        stopBtn.setEnabled(false);
        this.onStop.run();
    }

    private void onOpenLogViewer(ActionEvent e) {
        JDialog dlg = new JDialog(this, "Console Output", false);
        ConsolePanel console = new ConsolePanel();
        console.hookSystemStreams(); // redirect System.out/err to the panel
        dlg.setContentPane(console);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private Config readConfig() {
        String hostSrv = reqText(hostServer, "hostServer");
        String userSrv = reqText(userServer, "userServer");
        String passSrv = new String(passServer.getPassword());

        String hDb = reqText(hostDb, "hostDb");
        int pDb = ((Number) portDb.getValue()).intValue();
        String uDb = reqText(userDb, "userDb");
        String pDbPass = new String(passDb.getPassword());
        String nameDb = reqText(dbName, "dbName");

        String algorithm = reqText(hasherAlgorithm, "hasherAlgorithm");

        String seedPhrase = reqText(seedPhraseAnchor, "seedPhraseAnchor");
        String environment = environmentAnchor.getSelectedItem().toString();
        int interval = ((Number) intervalAnchor.getValue()).intValue();

        int period = ((Number) timePeriod.getValue()).intValue();
        Date first = (Date) dateOfFirstExecution.getValue();

        if (period <= 0) throw new IllegalArgumentException("timePeriod must be > 0 seconds");
        return new Config(hostSrv, userSrv, passSrv, hDb, pDb, uDb, pDbPass, nameDb, seedPhrase, environment, algorithm,
                period, first, interval);
    }

    private static String reqText(JTextField field, String name) {
        String v = field.getText().trim();
        if (v.isEmpty()) throw new IllegalArgumentException(name + " must not be empty");
        return v;
    }

    private static int addRow(JPanel panel, GridBagConstraints g, int row, String label, JComponent input) {
        g.gridx = 0; g.gridy = row; g.weightx = 0; panel.add(new JLabel(label + ":"), g);
        g.gridx = 1; g.gridy = row; g.weightx = 1; panel.add(input, g);
        return row + 1;
    }

    private static int sectionLabel(JPanel panel, GridBagConstraints g, int row, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        g.gridx = 0; g.gridy = row; g.gridwidth = 2; g.weightx = 1;
        panel.add(lbl, g);
        g.gridwidth = 1;
        return row + 1;
    }

    private static JFormattedTextField intField(int defaultValue) {
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        JFormattedTextField field = new JFormattedTextField(formatter);
        field.setValue(defaultValue);
        return field;
    }

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

    // Simple container for the collected config
    public record Config(
            String hostServer, String userServer, String passServer,
            String hostDb, int portDb, String userDb, String passDb, String dbName, String seedPhraseAnchor, String environmentAnchor, String hasherAlgorithm,
            int timePeriodSeconds, Date dateOfFirstExecution, int intervalAnchor
    ) {
    }

    public void setOnStart(Consumer<Config> handler) { this.onStart = handler; }
    public void setOnStop(Runnable handler) { this.onStop = handler; }

    private Config readConfigFromFields() {
        // read values from your text fields (example names)
        return new Config(
                hostServer.getText(), userServer.getText(), new String(passServer.getPassword()),
                hostDb.getText(), ((Number) portDb.getValue()).intValue(), userDb.getText(),
                new String(passDb.getPassword()), dbName.getText(), seedPhraseAnchor.getText(),
                environmentAnchor.getSelectedItem().toString(), hasherAlgorithm.getText(), ((Number) timePeriod.getValue()).intValue(),
                (Date) dateOfFirstExecution.getValue(), ((Number) intervalAnchor.getValue()).intValue()
        );
    }
}