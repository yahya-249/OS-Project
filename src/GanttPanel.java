import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GanttPanel extends JPanel {
    private List<GanttEntry> gantt;
    private Map<Integer, Color> colorMap;
    private int totalTime;
    private String title;

    private static final Color[] PALETTE = {
        new Color(66, 133, 244),
        new Color(234, 67, 53),
        new Color(251, 188, 5),
        new Color(52, 168, 83),
        new Color(171, 71, 188),
        new Color(255, 112, 67),
        new Color(38, 198, 218),
        new Color(124, 179, 66),
    };

    public GanttPanel(String title) {
        this.title = title;
        this.gantt = new ArrayList<>();
        this.colorMap = new HashMap<>();
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 140));
    }

    public void setData(List<GanttEntry> gantt, int totalTime) {
        this.gantt = gantt;
        this.totalTime = totalTime;
        this.colorMap = new HashMap<>();
        int colorIdx = 0;
        for (GanttEntry e : gantt) {
            if (e.pid != -1 && !colorMap.containsKey(e.pid)) {
                colorMap.put(e.pid, PALETTE[colorIdx % PALETTE.length]);
                colorIdx++;
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int margin = 40;
        int top = 35;
        int barHeight = 45;
        int width = getWidth() - 2 * margin;

        // Title
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(title, margin, 22);

        if (gantt == null || gantt.isEmpty() || totalTime == 0) return;

        // Draw bars
        for (GanttEntry e : gantt) {
            int x1 = margin + (int) ((e.startTime / (double) totalTime) * width);
            int x2 = margin + (int) ((e.endTime / (double) totalTime) * width);
            int barW = Math.max(x2 - x1, 2);

            if (e.pid == -1) {
                g2.setColor(new Color(220, 220, 220));
                g2.fillRect(x1, top, barW, barHeight);
                g2.setColor(Color.GRAY);
                g2.drawRect(x1, top, barW, barHeight);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.drawString("Idle", x1 + 2, top + barHeight / 2 + 4);
            } else {
                Color c = colorMap.getOrDefault(e.pid, Color.LIGHT_GRAY);
                g2.setColor(c);
                g2.fillRect(x1, top, barW, barHeight);
                g2.setColor(c.darker());
                g2.drawRect(x1, top, barW, barHeight);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String label = "P" + e.pid;
                int sw = g2.getFontMetrics().stringWidth(label);
                if (barW > sw + 4) {
                    g2.drawString(label, x1 + (barW - sw) / 2, top + barHeight / 2 + 5);
                }
            }

            // Time markers
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            String t1 = String.valueOf(e.startTime);
            g2.drawString(t1, x1, top + barHeight + 14);
        }
        // Last time marker
        int xLast = margin + width;
        g2.drawString(String.valueOf(totalTime), xLast - 10, top + barHeight + 14);
    }
}
