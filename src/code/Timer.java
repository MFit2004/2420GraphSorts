package graph;

/**
 * @course CS-2420-001 Algorithms & Data Structures Fall 2025
 * @last_modified Nov 24, 2025
 * @assignment Team Project:
 * 				Algorithm Racing
 * @description updates to timer object 	
 * 			(neccessary for accuracy and stopwatch)
 * @author Trevor_Austin + Matthew_Fitzgerald
 */
public class Timer {
    private long start = 0;     
    private long elapsed = 0;   
    private boolean running = false;

    public void Start() {
        if (!running) {
            start = System.nanoTime();
            running = true;
        }
    }

    public void Stop() {
        if (running) {
            elapsed += System.nanoTime() - start; 
            running = false;
        }
    }

    /**
     * Check time
     * TODO: needed?
     */
    public long getTime() {
        if (running) {
            return elapsed + (System.nanoTime() - start); 
        }
        return elapsed; 
    }

    /**
     * TODO: needed?
     */
    public void reset() {
        elapsed = 0;
        running = false;
    }
}

