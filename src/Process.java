public class Process {
    public int pid;
    public int arrivalTime;
    public int burstTime;
    public int remainingTime;
    public int priority; // not used in RR/SRTF but kept for extensibility
    public int completionTime;
    public int waitingTime;
    public int turnaroundTime;
    public int responseTime;
    public boolean started;

    public Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = 0;
        this.started = false;
        this.completionTime = -1;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.responseTime = -1;
    }

    public Process copy() {
        Process p = new Process(pid, arrivalTime, burstTime);
        p.priority = priority;
        return p;
    }
}
