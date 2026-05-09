import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReadyQueuePanel extends JPanel {
    private JTextArea queueArea;

    public ReadyQueuePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Round Robin Ready Queue"));
        queueArea = new JTextArea(10, 30);
        queueArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        queueArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(queueArea);
        add(scroll, BorderLayout.CENTER);
    }

    public void setQueueHistory(List<String> history) {
        if (history == null || history.isEmpty()) {
            queueArea.setText("(No data)");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String line : history) {
            sb.append(line).append("\n");
        }
        queueArea.setText(sb.toString());
    }
}
