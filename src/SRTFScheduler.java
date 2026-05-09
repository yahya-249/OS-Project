import java.util.*;

public class SRTFScheduler {
    public static SchedulerResult schedule(List<Process> original) {
        List<Process> processes = new ArrayList<>();
        for (Process p : original) processes.add(p.copy());
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        List<Process> readyList = new ArrayList<>();
        List<GanttEntry> gantt = new ArrayList<>();
        int currentTime = 0;
        int completed = 0;
        int n = processes.size();
        int index = 0;
        Process current = null;
        int lastPid = -2;
        int segmentStart = 0;

        while (completed < n) {
            // Arrivals
            while (index < n && processes.get(index).arrivalTime <= currentTime) {
                readyList.add(processes.get(index));
                index++;
            }

            // Find process with shortest remaining time
            Process shortest = null;
            for (Process p : readyList) {
                if (shortest == null || p.remainingTime < shortest.remainingTime) {
                    shortest = p;
                }
            }

            if (shortest == null) {
                // CPU idle
                if (current != null) {
                    gantt.add(new GanttEntry(lastPid, segmentStart, currentTime));
                    current = null;
                }
                int nextArrival = (index < n) ? processes.get(index).arrivalTime : currentTime + 1;
                if (lastPid != -1) {
                    gantt.add(new GanttEntry(lastPid, segmentStart, currentTime));
                    lastPid = -1;
                }
                gantt.add(new GanttEntry(-1, currentTime, nextArrival));
                segmentStart = nextArrival;
                currentTime = nextArrival;
                continue;
            }

            // Context switch if needed
            if (current != shortest) {
                if (current != null && lastPid != -1 && currentTime > segmentStart) {
                    gantt.add(new GanttEntry(lastPid, segmentStart, currentTime));
                }
                segmentStart = currentTime;
                lastPid = shortest.pid;
                current = shortest;
            }

            if (!shortest.started) {
                shortest.responseTime = currentTime - shortest.arrivalTime;
                shortest.started = true;
            }

            // Execute 1 time unit
            shortest.remainingTime--;
            currentTime++;

            if (shortest.remainingTime == 0) {
                shortest.completionTime = currentTime;
                shortest.turnaroundTime = shortest.completionTime - shortest.arrivalTime;
                shortest.waitingTime = shortest.turnaroundTime - shortest.burstTime;
                readyList.remove(shortest);
                completed++;
                gantt.add(new GanttEntry(shortest.pid, segmentStart, currentTime));
                lastPid = -2;
                segmentStart = currentTime;
                current = null;
            }
        }

        // Merge consecutive same-PID entries
        List<GanttEntry> merged = new ArrayList<>();
        for (GanttEntry e : gantt) {
            if (e.pid == -1) {
                merged.add(e);
                continue;
            }
            if (!merged.isEmpty() && merged.get(merged.size() - 1).pid == e.pid) {
                merged.get(merged.size() - 1).endTime = e.endTime;
            } else {
                merged.add(e);
            }
        }

        double avgWT = 0, avgTAT = 0, avgRT = 0;
        for (Process p : processes) {
            avgWT += p.waitingTime;
            avgTAT += p.turnaroundTime;
            avgRT += p.responseTime;
        }
        avgWT /= n;
        avgTAT /= n;
        avgRT /= n;

        return new SchedulerResult(processes, merged, avgWT, avgTAT, avgRT, currentTime, null);
    }
}