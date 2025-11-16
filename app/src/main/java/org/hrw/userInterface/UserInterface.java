package org.hrw.userInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class UserInterface extends JFrame {
    private Consumer<Config> onStart;
    private Runnable onStop;

    private final JTextField hostServer = new JTextField("192.168.250.55");
    private final JTextField userServer = new JTextField("root");
    private final JPasswordField passServer = new JPasswordField();

    private final JTextField hostDb = new JTextField("192.168.250.51");
    private final JTextField userDb = new JTextField("postgres");
    private final JPasswordField passDb = new JPasswordField();

    private final String[] options = {"TESTNET","STAGENET","MAINNET"};
    private final JTextField seedPhraseAnchor = new JTextField();
    private final JComboBox<String> environmentAnchor = new JComboBox<>(options);

    private final JFormattedTextField dateOfFirstExecution = dateTimeField();

    private final JButton startBtn = new JButton("Start");
    private final JButton stopBtn = new JButton("Stop");
    private final JButton logViewerBtn = new JButton("Log Viewer");

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
        row = addRow(content, g, row, "Host Server", hostServer);
        row = addRow(content, g, row, "User Server", userServer);
        row = addRow(content, g, row, "Password Server", passServer);

        row = sectionLabel(content, g, row, "Database");
        row = addRow(content, g, row, "Host Database", hostDb);
        row = addRow(content, g, row, "User Database", userDb);
        row = addRow(content, g, row, "Pass Database", passDb);

        row = sectionLabel(content, g, row, "Anchor Service");
        row = addRow(content, g, row, "Seed Phrase Anchor", seedPhraseAnchor);
        row = addRow(content, g, row, "Environment Anchor", environmentAnchor);

        row = sectionLabel(content, g, row, "Schedule");
        row = addRow(content, g, row, "Date Of First Execution (yyyy-MM-dd HH:mm:ss)", dateOfFirstExecution);

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
            status.setText("Running (every 5 minutes)");
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
        console.hookSystemStreams();
        dlg.setContentPane(console);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private Config readConfig() {
        String hostSrv = reqText(hostServer, "Host Server");
        String userSrv = reqText(userServer, "User Server");
        String passSrv = new String(passServer.getPassword());

        String hDb = reqText(hostDb, "Host Database");
        String uDb = reqText(userDb, "User Database");
        String pDbPass = new String(passDb.getPassword());

        String seedPhrase = reqText(seedPhraseAnchor, "Seed Phrase Anchor");
        String environment = environmentAnchor.getSelectedItem().toString();

        Date first = (Date) dateOfFirstExecution.getValue();

        return new Config(hostSrv, userSrv, passSrv, hDb, uDb, pDbPass, seedPhrase, environment, first);
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

    public record Config(
            String hostServer, String userServer, String passServer,
            String hostDb, String userDb, String passDb, String seedPhraseAnchor, String environmentAnchor,
            Date dateOfFirstExecution
    ) {
    }

    public void setOnStart(Consumer<Config> handler) { this.onStart = handler; }
    public void setOnStop(Runnable handler) { this.onStop = handler; }
}