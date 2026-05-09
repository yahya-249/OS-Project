import java.util.List;

public class SchedulerResult {
    public List<Process> processes;
    public List<GanttEntry> gantt;
    public double avgWaitingTime;
    public double avgTurnaroundTime;
    public double avgResponseTime;
    public int totalTime;
    public List<String> readyQueueStates;  // for RR ready queue view

    public SchedulerResult(List<Process> processes, List<GanttEntry> gantt,
                           double avgWT, double avgTAT, double avgRT, int totalTime,
                           List<String> readyQueueStates) {
        this.processes = processes;
        this.gantt = gantt;
        this.avgWaitingTime = avgWT;
        this.avgTurnaroundTime = avgTAT;
        this.avgResponseTime = avgRT;
        this.totalTime = totalTime;
        this.readyQueueStates = readyQueueStates;
    }
}