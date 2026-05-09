# Round Robin vs SJF (SRTF) — CPU Scheduling Simulator
**Operating Systems Course | C1 Project**

---

## How to Run

### Option 1 — VS Code (recommended)
1. Open this folder in VS Code: `File → Open Folder`
2. Make sure the **Extension Pack for Java** is installed (VS Code will prompt you)
3. Open `src/Main.java` and press **F5**, or click the ▶ Run button

### Option 2 — Command Line (Windows)
```
run.bat
```

### Option 3 — Command Line (Linux / Mac)
```
chmod +x run.sh
./run.sh
```

---

## Project Structure
```
scheduler_project/
├── src/
│   ├── Main.java              ← Entry point
│   ├── MainFrame.java         ← Full GUI (Swing)
│   ├── GanttPanel.java        ← Custom Gantt chart painter
│   ├── ResultsTablePanel.java ← Per-process metrics table
│   ├── Process.java           ← Process data model
│   ├── GanttEntry.java        ← Gantt time slice
│   ├── SchedulerResult.java   ← Result container
│   ├── RoundRobinScheduler.java   ← Round Robin algorithm
│   └── SRTFScheduler.java     ← Preemptive SJF (SRTF) algorithm
├── .vscode/
│   ├── launch.json
│   └── settings.java.json
├── run.bat                    ← Windows launcher
├── run.sh                     ← Linux/Mac launcher
└── README.md
```

---

## Features
- Dynamic process input with full validation
- Round Robin Scheduling with configurable quantum
- Preemptive SJF (SRTF) Scheduling
- Colour-coded Gantt charts for both algorithms
- Per-process WT, TAT, RT tables with averages
- Auto-generated Comparison Summary & Final Conclusion
- 3 preloaded test scenarios (Normal, Behavior-Revealing, Validation)

---

## Requirements
- Java JDK 11 or newer
