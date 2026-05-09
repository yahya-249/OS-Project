import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResultsTablePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JLabel summaryLabel;

    public ResultsTablePanel(String title) {
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(title));

        model = new DefaultTableModel();
        model.addColumn("PID");
        model.addColumn("Arrival");
        model.addColumn("Burst");
        model.addColumn("Completion");
        model.addColumn("TAT");
        model.addColumn("WT");
        model.addColumn("RT");

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setRowHeight(22);
        table.setEnabled(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 160));
        add(scroll, BorderLayout.CENTER);

        summaryLabel = new JLabel(" ");
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        summaryLabel.setForeground(new Color(33, 37, 41));
        add(summaryLabel, BorderLayout.SOUTH);
    }

    public void setData(List<Process> processes, double avgWT, double avgTAT, double avgRT) {
        model.setRowCount(0);
        for (Process p : processes) {
            model.addRow(new Object[]{
                "P" + p.pid,
                p.arrivalTime,
                p.burstTime,
                p.completionTime,
                p.turnaroundTime,
                p.waitingTime,
                p.responseTime
            });
        }
        summaryLabel.setText(String.format(
            "   Avg TAT: %.2f  |  Avg WT: %.2f  |  Avg RT: %.2f",
            avgTAT, avgWT, avgRT));
    }
}
