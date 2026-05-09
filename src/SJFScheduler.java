import java.util.*;

public class SJFScheduler {
    public static SchedulerResult schedule(List<Process> original) {
        List<Process> processes = new ArrayList<>();
        for (Process p : original) processes.add(p.copy());
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        List<GanttEntry> gantt = new ArrayList<>();
        int currentTime = 0;
        int completed = 0;
        int n = processes.size();
        int index = 0;

        while (completed < n) {
            // Add newly arrived processes to a list of available
            while (index < n && processes.get(index).arrivalTime <= currentTime) {
                index++;
            }

            // Among arrived but not completed, pick the one with smallest burstTime
            Process selected = null;
            for (int i = 0; i < index; i++) {
                Process p = processes.get(i);
                if (p.remainingTime > 0) {
                    if (selected == null || p.burstTime < selected.burstTime) {
                        selected = p;
                    }
                }
            }

            if (selected == null) {
                // CPU idle until next arrival
                int nextArrival = (index < n) ? processes.get(index).arrivalTime : currentTime;
                if (nextArrival > currentTime) {
                    gantt.add(new GanttEntry(-1, currentTime, nextArrival));
                    currentTime = nextArrival;
                } else {
                    currentTime++;
                }
                continue;
            }

            // Non-preemptive: run the selected process to completion
            if (!selected.started) {
                selected.responseTime = currentTime - selected.arrivalTime;
                selected.started = true;
            }

            int start = currentTime;
            currentTime += selected.remainingTime;
            selected.completionTime = currentTime;
            selected.turnaroundTime = selected.completionTime - selected.arrivalTime;
            selected.waitingTime = selected.turnaroundTime - selected.burstTime;
            selected.remainingTime = 0;
            completed++;

            gantt.add(new GanttEntry(selected.pid, start, currentTime));
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

        return new SchedulerResult(processes, gantt, avgWT, avgTAT, avgRT, currentTime, null);
    }
}