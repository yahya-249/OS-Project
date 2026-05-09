import java.util.*;

public class RoundRobinScheduler {
    public static SchedulerResult schedule(List<Process> original, int quantum) {
        List<Process> processes = new ArrayList<>();
        for (Process p : original) processes.add(p.copy());
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        Queue<Process> readyQueue = new LinkedList<>();
        List<GanttEntry> gantt = new ArrayList<>();
        List<String> queueHistory = new ArrayList<>();   // record ready queue states
        int currentTime = 0;
        int completed = 0;
        int n = processes.size();
        int index = 0;

        while (completed < n) {
            while (index < n && processes.get(index).arrivalTime <= currentTime) {
                readyQueue.add(processes.get(index));
                index++;
            }

            // Record ready queue snapshot
            StringBuilder sb = new StringBuilder("Time " + currentTime + ": [");
            List<Process> list = new ArrayList<>(readyQueue);
            for (int i = 0; i < list.size(); i++) {
                sb.append("P").append(list.get(i).pid);
                if (i < list.size() - 1) sb.append(", ");
            }
            sb.append("]");
            queueHistory.add(sb.toString());

            if (readyQueue.isEmpty()) {
                int nextArrival = (index < n) ? processes.get(index).arrivalTime : currentTime + 1;
                gantt.add(new GanttEntry(-1, currentTime, nextArrival));
                currentTime = nextArrival;
                continue;
            }

            Process current = readyQueue.poll();

            if (!current.started) {
                current.responseTime = currentTime - current.arrivalTime;
                current.started = true;
            }

            int execTime = Math.min(quantum, current.remainingTime);
            int start = currentTime;
            currentTime += execTime;
            current.remainingTime -= execTime;

            while (index < n && processes.get(index).arrivalTime <= currentTime) {
                readyQueue.add(processes.get(index));
                index++;
            }

            if (current.remainingTime == 0) {
                current.completionTime = currentTime;
                current.turnaroundTime = current.completionTime - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
                completed++;
            } else {
                readyQueue.add(current);
            }

            gantt.add(new GanttEntry(current.pid, start, currentTime));
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

        return new SchedulerResult(processes, gantt, avgWT, avgTAT, avgRT, currentTime, queueHistory);
    }
}