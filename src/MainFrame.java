import javax.swing.*;
// import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {
    private JTextField quantumField;
    private JTable inputTable;
    private DefaultTableModel inputModel;
    private GanttPanel rrGantt, sjfGantt, srtfGantt;
    private ResultsTablePanel rrTable, sjfTable, srtfTable;
    private JTextArea comparisonArea, conclusionArea;
    private ReadyQueuePanel readyQueuePanel;
    private List<Process> currentProcesses;
    private int currentQuantum = 2;

    public MainFrame() {
        setTitle("CPU Scheduling Simulator — Round Robin vs SJF vs SRTF");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 980);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== Top Control Panel =====
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        controlPanel.setBackground(new Color(245, 245, 245));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel qLabel = new JLabel("Quantum:");
        qLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        quantumField = new JTextField("2", 4);
        quantumField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton addBtn = new JButton("Add Process");
        JButton removeBtn = new JButton("Remove Selected");
        JButton runBtn = new JButton("Run Simulation");
        JButton clearBtn = new JButton("Clear All");
        JButton scenarioNormal = new JButton("Test: Normal");
        JButton scenarioShortJob = new JButton("Test: Short-Job Heavy");
        JButton scenarioFairness = new JButton("Test: Fairness");
        JButton scenarioLongJob = new JButton("Test: Long-Job Sensitivity");
        JButton scenarioInvalid = new JButton("Test: Invalid Input");

        styleButton(addBtn, new Color(66, 133, 244));
        styleButton(removeBtn, new Color(234, 67, 53));
        styleButton(runBtn, new Color(52, 168, 83));
        styleButton(clearBtn, new Color(150, 150, 150));
        styleButton(scenarioNormal, new Color(171, 71, 188));
        styleButton(scenarioShortJob, new Color(255, 112, 67));
        styleButton(scenarioFairness, new Color(66, 133, 244));
        styleButton(scenarioLongJob, new Color(33, 150, 243));
        styleButton(scenarioInvalid, new Color(38, 198, 218));

        controlPanel.add(qLabel);
        controlPanel.add(quantumField);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(addBtn);
        controlPanel.add(removeBtn);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(runBtn);
        controlPanel.add(clearBtn);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(scenarioNormal);
        controlPanel.add(scenarioShortJob);
        controlPanel.add(scenarioFairness);
        controlPanel.add(scenarioLongJob);
        controlPanel.add(scenarioInvalid);

        add(controlPanel, BorderLayout.NORTH);

        // ===== Input Table =====
        inputModel = new DefaultTableModel();
        inputModel.addColumn("PID");
        inputModel.addColumn("Arrival Time");
        inputModel.addColumn("Burst Time");
        inputTable = new JTable(inputModel);
        inputTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inputTable.setRowHeight(24);
        inputTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane inputScroll = new JScrollPane(inputTable);
        inputScroll.setPreferredSize(new Dimension(800, 170));
        inputScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        inputScroll.setBorder(BorderFactory.createTitledBorder("Process Input"));

        // ===== Scheduling Results Tabs =====
        rrGantt = new GanttPanel("Round Robin Gantt Chart");
        sjfGantt = new GanttPanel("SJF (Non-preemptive) Gantt Chart");
        srtfGantt = new GanttPanel("SRTF (Preemptive) Gantt Chart");
        rrTable = new ResultsTablePanel("Round Robin Metrics");
        sjfTable = new ResultsTablePanel("SJF (Non-preemptive) Metrics");
        srtfTable = new ResultsTablePanel("SRTF (Preemptive) Metrics");

        // Build each tab: Gantt chart on top, results table below
        JPanel rrTab = createAlgoPanel(rrGantt, rrTable);
        JPanel sjfTab = createAlgoPanel(sjfGantt, sjfTable);
        JPanel srtfTab = createAlgoPanel(srtfGantt, srtfTable);

        JTabbedPane algoTabs = new JTabbedPane();
        algoTabs.addTab("Round Robin", rrTab);
        algoTabs.addTab("SJF (Non‑preemptive)", sjfTab);
        algoTabs.addTab("SRTF (Preemptive)", srtfTab);
        algoTabs.setPreferredSize(new Dimension(800, 550));

        // ===== Ready Queue Panel (RR only) =====
        readyQueuePanel = new ReadyQueuePanel();
        readyQueuePanel.setPreferredSize(new Dimension(800, 150));
        readyQueuePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        readyQueuePanel.setBorder(BorderFactory.createTitledBorder("Round Robin Ready Queue"));

        // ===== Comparison & Conclusion (bottom) =====
        comparisonArea = new JTextArea();
        comparisonArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        comparisonArea.setEditable(false);
        JScrollPane compScroll = new JScrollPane(comparisonArea);

        conclusionArea = new JTextArea();
        conclusionArea.setFont(new Font("Consolas", Font.BOLD, 13));
        conclusionArea.setEditable(false);
        JScrollPane concScroll = new JScrollPane(conclusionArea);

        JTabbedPane bottomTabs = new JTabbedPane();
        bottomTabs.addTab("Comparison Summary", compScroll);
        bottomTabs.addTab("Final Conclusion", concScroll);
        bottomTabs.setPreferredSize(new Dimension(800, 220));

        // ===== Central layout (vertical) =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        centerPanel.add(inputScroll);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(algoTabs);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(readyQueuePanel);
        centerPanel.add(Box.createVerticalStrut(8));

        JScrollPane centerScroll = new JScrollPane(centerPanel);
        centerScroll.setBorder(null);
        centerScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        centerScroll.getVerticalScrollBar().setUnitIncrement(16);

        add(centerScroll, BorderLayout.CENTER);
        add(bottomTabs, BorderLayout.SOUTH);

        // ===== Listeners =====
        addBtn.addActionListener(e -> addProcessRow());
        removeBtn.addActionListener(e -> removeSelectedRow());
        runBtn.addActionListener(e -> runSimulation());
        clearBtn.addActionListener(e -> clearAll());
        scenarioNormal.addActionListener(e -> loadScenarioNormal());
        scenarioShortJob.addActionListener(e -> loadScenarioShortJob());
        scenarioFairness.addActionListener(e -> loadScenarioFairness());
        scenarioLongJob.addActionListener(e -> loadScenarioLongJob());
        scenarioInvalid.addActionListener(e -> loadScenarioInvalid());

        // Preload normal scenario
        loadScenarioNormal();
    }

    private JPanel createAlgoPanel(GanttPanel gantt, ResultsTablePanel table) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.add(gantt, BorderLayout.NORTH);
        panel.add(table, BorderLayout.CENTER);
        return panel;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
    }

    private void addProcessRow() {
        int nextPid = inputModel.getRowCount() + 1;
        inputModel.addRow(new Object[]{nextPid, 0, 1});
    }

    private void removeSelectedRow() {
        int row = inputTable.getSelectedRow();
        if (row >= 0) {
            inputModel.removeRow(row);
            for (int i = 0; i < inputModel.getRowCount(); i++) {
                inputModel.setValueAt(i + 1, i, 0);
            }
        }
    }

    private void clearAll() {
        inputModel.setRowCount(0);
        rrGantt.setData(new ArrayList<>(), 0);
        sjfGantt.setData(new ArrayList<>(), 0);
        srtfGantt.setData(new ArrayList<>(), 0);
        rrTable.setData(new ArrayList<>(), 0, 0, 0);
        sjfTable.setData(new ArrayList<>(), 0, 0, 0);
        srtfTable.setData(new ArrayList<>(), 0, 0, 0);
        readyQueuePanel.setQueueHistory(null);
        comparisonArea.setText("");
        conclusionArea.setText("");
    }

    private boolean validateInput() {
        Set<Integer> pids = new HashSet<>();
        for (int i = 0; i < inputModel.getRowCount(); i++) {
            try {
                int pid = Integer.parseInt(inputModel.getValueAt(i, 0).toString().trim());
                int at = Integer.parseInt(inputModel.getValueAt(i, 1).toString().trim());
                int bt = Integer.parseInt(inputModel.getValueAt(i, 2).toString().trim());
                if (at < 0) {
                    JOptionPane.showMessageDialog(this, "Arrival time cannot be negative at row " + (i + 1));
                    return false;
                }
                if (bt <= 0) {
                    JOptionPane.showMessageDialog(this, "Burst time must be > 0 at row " + (i + 1));
                    return false;
                }
                if (!pids.add(pid)) {
                    JOptionPane.showMessageDialog(this, "Duplicate PID found: " + pid);
                    return false;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid numeric input at row " + (i + 1));
                return false;
            }
        }
        if (pids.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No processes entered.");
            return false;
        }
        try {
            currentQuantum = Integer.parseInt(quantumField.getText().trim());
            if (currentQuantum <= 0) {
                JOptionPane.showMessageDialog(this, "Quantum must be > 0.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantum value.");
            return false;
        }
        return true;
    }

    private void runSimulation() {
        if (!validateInput()) return;

        currentProcesses = new ArrayList<>();
        for (int i = 0; i < inputModel.getRowCount(); i++) {
            int pid = Integer.parseInt(inputModel.getValueAt(i, 0).toString().trim());
            int at = Integer.parseInt(inputModel.getValueAt(i, 1).toString().trim());
            int bt = Integer.parseInt(inputModel.getValueAt(i, 2).toString().trim());
            currentProcesses.add(new Process(pid, at, bt));
        }

        SchedulerResult rr = RoundRobinScheduler.schedule(currentProcesses, currentQuantum);
        SchedulerResult sjf = SJFScheduler.schedule(currentProcesses);
        SchedulerResult srtf = SRTFScheduler.schedule(currentProcesses);

        rrGantt.setData(rr.gantt, rr.totalTime);
        sjfGantt.setData(sjf.gantt, sjf.totalTime);
        srtfGantt.setData(srtf.gantt, srtf.totalTime);

        rrTable.setData(rr.processes, rr.avgWaitingTime, rr.avgTurnaroundTime, rr.avgResponseTime);
        sjfTable.setData(sjf.processes, sjf.avgWaitingTime, sjf.avgTurnaroundTime, sjf.avgResponseTime);
        srtfTable.setData(srtf.processes, srtf.avgWaitingTime, srtf.avgTurnaroundTime, srtf.avgResponseTime);

        readyQueuePanel.setQueueHistory(rr.readyQueueStates);

        generateComparison(rr, sjf, srtf);
    }

    private void generateComparison(SchedulerResult rr, SchedulerResult sjf, SchedulerResult srtf) {
        StringBuilder comp = new StringBuilder();
        comp.append("=== COMPARISON SUMMARY ===\n\n");
        comp.append(String.format("Round Robin (Quantum = %d):\n", currentQuantum));
        comp.append(String.format("  Avg WT: %.2f  |  Avg TAT: %.2f  |  Avg RT: %.2f\n",
                rr.avgWaitingTime, rr.avgTurnaroundTime, rr.avgResponseTime));
        comp.append(String.format("SJF (Non-preemptive):\n"));
        comp.append(String.format("  Avg WT: %.2f  |  Avg TAT: %.2f  |  Avg RT: %.2f\n",
                sjf.avgWaitingTime, sjf.avgTurnaroundTime, sjf.avgResponseTime));
        comp.append(String.format("SRTF (Preemptive):\n"));
        comp.append(String.format("  Avg WT: %.2f  |  Avg TAT: %.2f  |  Avg RT: %.2f\n\n",
                srtf.avgWaitingTime, srtf.avgTurnaroundTime, srtf.avgResponseTime));

        // Best waiting time
        double minWT = Math.min(rr.avgWaitingTime, Math.min(sjf.avgWaitingTime, srtf.avgWaitingTime));
        String bestWT = (minWT == rr.avgWaitingTime) ? "Round Robin" : (minWT == sjf.avgWaitingTime) ? "SJF" : "SRTF";
        comp.append("• Lowest average waiting time: " + bestWT + "\n");

        // Best response time
        double minRT = Math.min(rr.avgResponseTime, Math.min(sjf.avgResponseTime, srtf.avgResponseTime));
        String bestRT = (minRT == rr.avgResponseTime) ? "Round Robin" : (minRT == sjf.avgResponseTime) ? "SJF" : "SRTF";
        comp.append("• Lowest average response time: " + bestRT + "\n");

        comp.append("• Fairness: Round Robin guarantees equal time slices; SJF may starve long processes; SRTF improves responsiveness but still favors short bursts.\n");
        comp.append("• Quantum effect (RR): With quantum = " + currentQuantum + ", context switching is " +
                (currentQuantum <= 2 ? "frequent (good interactivity, more overhead)" : "moderate") + ".\n");
        comp.append("• Preemptive (SRTF) vs Non‑preemptive (SJF): SRTF often gives lower average waiting times, but may incur more context switches.\n");

        comparisonArea.setText(comp.toString());

        StringBuilder concl = new StringBuilder();
        concl.append("CONCLUSION:\n");
        concl.append("Based on the tested workload:\n");
        if (minWT == rr.avgWaitingTime) {
            concl.append("• Round Robin surprisingly gave the best average waiting time.\n");
        } else if (minWT == sjf.avgWaitingTime) {
            concl.append("• Non‑preemptive SJF achieved the best average waiting time.\n");
        } else {
            concl.append("• Preemptive SRTF achieved the best average waiting time.\n");
        }
        if (minRT == rr.avgResponseTime) {
            concl.append("• Round Robin was most responsive.\n");
        } else if (minRT == sjf.avgResponseTime) {
            concl.append("• SJF gave the fastest first response.\n");
        } else {
            concl.append("• SRTF offered the fastest first response.\n");
        }
        concl.append("• For interactive systems, Round Robin is recommended; for batch environments, SRTF/SJF are more efficient.\n");
        conclusionArea.setText(concl.toString());
    }

    // ===== Test Scenarios (unchanged) =====
    private void loadScenarioNormal() {
        inputModel.setRowCount(0);
        inputModel.addRow(new Object[]{1, 0, 5});
        inputModel.addRow(new Object[]{2, 1, 3});
        inputModel.addRow(new Object[]{3, 2, 8});
        inputModel.addRow(new Object[]{4, 3, 6});
        quantumField.setText("2");
    }

    private void loadScenarioShortJob() {
        inputModel.setRowCount(0);
        inputModel.addRow(new Object[]{1, 0, 2});
        inputModel.addRow(new Object[]{2, 0, 1});
        inputModel.addRow(new Object[]{3, 0, 3});
        inputModel.addRow(new Object[]{4, 0, 2});
        inputModel.addRow(new Object[]{5, 0, 1});
        inputModel.addRow(new Object[]{6, 0, 5});
        quantumField.setText("2");
    }

    private void loadScenarioFairness() {
        inputModel.setRowCount(0);
        inputModel.addRow(new Object[]{1, 0, 5});
        inputModel.addRow(new Object[]{2, 0, 5});
        inputModel.addRow(new Object[]{3, 0, 5});
        inputModel.addRow(new Object[]{4, 0, 5});
        quantumField.setText("2");
    }

    private void loadScenarioLongJob() {
        inputModel.setRowCount(0);
        inputModel.addRow(new Object[]{1, 0, 20});
        inputModel.addRow(new Object[]{2, 1, 2});
        inputModel.addRow(new Object[]{3, 2, 2});
        inputModel.addRow(new Object[]{4, 3, 2});
        inputModel.addRow(new Object[]{5, 4, 2});
        quantumField.setText("3");
    }

    private void loadScenarioInvalid() {
        inputModel.setRowCount(0);
        inputModel.addRow(new Object[]{1, 0, 5});
        inputModel.addRow(new Object[]{1, 2, 3});    // duplicate PID
        inputModel.addRow(new Object[]{3, -1, 4});   // negative arrival
        inputModel.addRow(new Object[]{4, 3, 0});    // zero burst
        quantumField.setText("0");                   // invalid quantum
        JOptionPane.showMessageDialog(this,
                "Invalid scenario loaded. Click 'Run Simulation' to see validation errors.",
                "Validation Test", JOptionPane.INFORMATION_MESSAGE);
    }
}