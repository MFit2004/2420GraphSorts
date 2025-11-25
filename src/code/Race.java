package a5;

import edu.princeton.cs.algs4.DepthFirstPaths;
import edu.princeton.cs.algs4.BreadthFirstPaths;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Race class manages the full DFS vs BFS "Algorithm Race" program.
 * It generates the maze, runs both algorithms, animates their progress,
 * tracks runtime statistics, and prints final results including the winner.
 *
 * Responsibilities of this class include:
 * - Initializing the MazeGraph and visualization window
 * - Running DFS and BFS simultaneously on separate threads
 * - Feeding animation updates into the MazeVisualizer
 * - Measuring algorithm performance through Statistics objects
 * - Printing comparison results and announcing the winner
 */
public class Race {

    /**
     * Internal timer class used to measure the total race duration.
     * Tracks time in nanoseconds with conversion to milliseconds.
     */
    private class Timer {
        private long start = 0;
        private long stop = 0;

        /** Starts the timer. */
        public void Start() {
            start = System.nanoTime();
            stop = 0;
        }

        /** Stops the timer. */
        public void Stop() {
            stop = System.nanoTime();
        }

        /**
         * Returns the elapsed time in nanoseconds.
         * @return total elapsed time
         */
        public long getTime() {
            if (stop == 0) {
                return System.nanoTime() - start;
            }
            return stop - start;
        }

        /**
         * Returns the elapsed time in milliseconds.
         * @return elapsed time in ms
         */
        public double getTimeMillis() {
            return getTime() / 1_000_000.0;
        }
    }

    private Timer timer;
    private Statistics dfsStats;
    private Statistics bfsStats;
    private MazeGraph graph;

    private int startVertex;
    private int targetVertex;

    private MazeVisualizer visualizer;
    private JFrame frame;

    /**
     * Creates the Race controller, building the maze and preparing statistics.
     *
     * @param vertices total number of vertices in the maze
     * @param start starting vertex for both algorithms
     * @param target target vertex the algorithms attempt to reach
     */
    public Race(int vertices, int start, int target) {
        this.graph = new MazeGraph(vertices);
        this.startVertex = start;
        this.targetVertex = target;
        this.timer = new Timer();
        this.dfsStats = new Statistics();
        this.bfsStats = new Statistics();
    }

    /**
     * Starts the entire race:
     * - prints maze info
     * - builds the GUI
     * - computes DFS & BFS paths
     * - animates both simultaneously
     * - prints results once both are finished
     */
    public void Start() {
        System.out.println("Starting Algorithm Race...");
        graph.displayMaze();
        System.out.println("Start vertex: " + startVertex);
        System.out.println("Target vertex: " + targetVertex);

        setupVisualization();

        DepthFirstPaths dfs = new DepthFirstPaths(graph.getGraph(), startVertex);
        BreadthFirstPaths bfs = new BreadthFirstPaths(graph.getGraph(), startVertex);

        List<Integer> dfsPath = new ArrayList<>();
        List<Integer> bfsPath = new ArrayList<>();

        if (dfs.hasPathTo(targetVertex)) {
            for (int v : dfs.pathTo(targetVertex)) {
                dfsPath.add(v);
            }
        }

        if (bfs.hasPathTo(targetVertex)) {
            for (int v : bfs.pathTo(targetVertex)) {
                bfsPath.add(v);
            }
        }

        visualizer.setDFSPath(dfsPath);
        visualizer.setBFSPath(bfsPath);

        System.out.println("\nBoth algorithms racing simultaneously...\n");

        timer.Start();

        Thread dfsThread = new Thread(() -> runDFSVisual(dfsPath));
        Thread bfsThread = new Thread(() -> runBFSVisual(bfsPath));

        dfsThread.start();
        bfsThread.start();

        try {
            dfsThread.join();
            bfsThread.join();
        } catch (InterruptedException e) {
            System.err.println("Race interrupted: " + e.getMessage());
        }

        timer.Stop();

        System.out.println("\nRace Complete!");
        displayResults();
    }

    /**
     * Creates the JFrame window and places the MazeVisualizer inside it.
     */
    private void setupVisualization() {
        frame = new JFrame("Algorithm Racer - DFS vs BFS");
        visualizer = new MazeVisualizer(graph, startVertex, targetVertex);

        frame.add(visualizer);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Executes DFS animation step-by-step, updating statistics
     * and notifying the GUI of each move.
     *
     * @param path the DFS path list of vertices
     */
    private void runDFSVisual(List<Integer> path) {
        dfsStats.startTimer();
        dfsStats.startDistance();

        for (int i = 0; i < path.size(); i++) {
            dfsStats.incrementVerticesVisited();

            if (i > 0) {
                dfsStats.addDistance(1);
                dfsStats.incrementEdgesChecked();
            }

            SwingUtilities.invokeLater(() -> visualizer.advanceDFS());

            try {
                Thread.sleep(100);  // animation pacing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        dfsStats.setMoves(path.size() - 1);
        dfsStats.stopTimer();
        System.out.println("🔴 DFS finished!");
    }

    /**
     * Executes BFS animation step-by-step, updating statistics
     * and notifying the GUI of each move.
     *
     * @param path the BFS path list of vertices
     */
    private void runBFSVisual(List<Integer> path) {
        bfsStats.startTimer();
        bfsStats.startDistance();

        for (int i = 0; i < path.size(); i++) {
            bfsStats.incrementVerticesVisited();

            if (i > 0) {
                bfsStats.addDistance(1);
                bfsStats.incrementEdgesChecked();
            }

            SwingUtilities.invokeLater(() -> visualizer.advanceBFS());

            try {
                Thread.sleep(100); // animation pacing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        bfsStats.setMoves(path.size() - 1);
        bfsStats.stopTimer();
        System.out.println("🔵 BFS finished!");
    }

    /**
     * Prints the final comparison table including:
     * - Total runtime
     * - Path lengths
     * - Vertices visited
     * - Edge checks
     * - Winner declaration
     */
    private void displayResults() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           ALGORITHM RACE RESULTS");
        System.out.println("=".repeat(50));

        dfsStats.display("Depth-First Search (DFS)");
        bfsStats.display("Breadth-First Search (BFS)");

        System.out.println("\n" + "=".repeat(50));
        System.out.println("Total Race Time: " + timer.getTime() + " ns (" 
                           + timer.getTimeMillis() + " ms)");
        System.out.println("=".repeat(50));

        compareResults();
        announceWinner();
    }

    /**
     * Compares DFS and BFS side-by-side in:
     * - speed
     * - number of moves
     * - nodes visited
     */
    private void compareResults() {
        System.out.println("\n=== COMPARISON ===");

        if (dfsStats.getTime() < bfsStats.getTime()) {
            double diff = (bfsStats.getTime() - dfsStats.getTime()) / 1_000_000.0;
            System.out.println("Faster: DFS by " + diff + " ms");
        } else {
            double diff = (dfsStats.getTime() - bfsStats.getTime()) / 1_000_000.0;
            System.out.println("Faster: BFS by " + diff + " ms");
        }

        if (dfsStats.getMoves() < bfsStats.getMoves()) {
            System.out.println("Shorter Path: DFS (" + dfsStats.getMoves() +
                               " moves vs " + bfsStats.getMoves() + ")");
        } else if (bfsStats.getMoves() < dfsStats.getMoves()) {
            System.out.println("Shorter Path: BFS (" + bfsStats.getMoves() +
                               " moves vs " + dfsStats.getMoves() + ")");
        } else {
            System.out.println("Same Path Length: " + dfsStats.getMoves() + " moves");
        }

        if (dfsStats.getVerticesVisited() < bfsStats.getVerticesVisited()) {
            System.out.println("Fewer Vertices Visited: DFS (" +
                                dfsStats.getVerticesVisited() + " vs " +
                                bfsStats.getVerticesVisited() + ")");
        } else {
            System.out.println("Fewer Vertices Visited: BFS (" +
                                bfsStats.getVerticesVisited() + " vs " +
                                dfsStats.getVerticesVisited() + ")");
        }
    }

    /**
     * Prints the final winner based on finishing time only.
     */
    private void announceWinner() {
        System.out.println("\n" + "=".repeat(50));
        if (dfsStats.getTime() < bfsStats.getTime()) {
            System.out.println("🏆 WINNER: DFS (Finished First!)");
        } else if (bfsStats.getTime() < dfsStats.getTime()) {
            System.out.println("🏆 WINNER: BFS (Finished First!)");
        } else {
            System.out.println("🏆 TIE! Both algorithms finished simultaneously!");
        }
        System.out.println("=".repeat(50));
    }

    /** @return DFS statistics object */
    public Statistics getDFSStats() {
        return dfsStats;
    }

    /** @return BFS statistics object */
    public Statistics getBFSStats() {
        return bfsStats;
    }

    /** @return total race time in nanoseconds */
    public long getTotalTime() {
        return timer.getTime();
    }

    /** @return the underlying MazeGraph */
    public MazeGraph getMazeGraph() {
        return graph;
    }
}
