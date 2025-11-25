package a5;

/**
 * The Statistics class tracks performance metrics for graph search algorithms
 * such as DFS and BFS. Metrics recorded include runtime, path distance,
 * number of moves, visited vertices, and checked edges.
 *
 * This class contains two inner helper classes:
 *  - Timer: tracks execution time using nanoseconds.
 *  - Distance: tracks total distance traveled during a search.
 */
public class Statistics {

    /**
     * Inner Timer class that measures elapsed time in nanoseconds.
     * It starts timing when Start() is called and stops when Stop() is called.
     */
    private class Timer {
        private long start = 0;
        private long stop = 0;

        /**
         * Starts or restarts the timer.
         */
        public void Start() {
            start = System.nanoTime();
            stop = 0;
        }

        /**
         * Stops the timer.
         */
        public void Stop() {
            stop = System.nanoTime();
        }

        /**
         * Returns the elapsed time in nanoseconds.
         * If Stop() has not been called, returns the time since Start().
         *
         * @return elapsed time in nanoseconds
         */
        public long getTime() {
            if (stop == 0) {
                return System.nanoTime() - start;
            }
            return stop - start;
        }

        /**
         * Returns the elapsed time in milliseconds.
         *
         * @return elapsed time in milliseconds
         */
        public double getTimeMillis() {
            return getTime() / 1_000_000.0;
        }
    }

    /**
     * Inner Distance class used to track the accumulated distance
     * of the explored path.
     */
    private class Distance {
        private int distance;

        /**
         * Constructs a new Distance tracker with distance = 0.
         */
        public Distance() {
            this.distance = 0;
        }

        /**
         * Resets the stored distance to zero.
         */
        public void Start() {
            distance = 0;
        }

        /**
         * Adds a specified amount to the total distance.
         *
         * @param dist the amount to add
         */
        public void addDistance(int dist) {
            this.distance += dist;
        }

        /**
         * Returns the current total distance.
         *
         * @return total distance
         */
        public int getDistance() {
            return distance;
        }
    }

    // Statistics fields
    private Timer timer;
    private Distance distance;
    private int moves;
    private int verticesVisited;
    private int edgesChecked;

    /**
     * Constructs a Statistics object and initializes internal counters.
     */
    public Statistics() {
        this.timer = new Timer();
        this.distance = new Distance();
        this.moves = 0;
        this.verticesVisited = 0;
        this.edgesChecked = 0;
    }

    // Timer methods

    /**
     * Starts the timer for this statistics instance.
     */
    public void startTimer() {
        timer.Start();
    }

    /**
     * Stops the timer for this statistics instance.
     */
    public void stopTimer() {
        timer.Stop();
    }

    /**
     * Returns the measured time in nanoseconds.
     *
     * @return elapsed time in nanoseconds
     */
    public long getTime() {
        return timer.getTime();
    }

    /**
     * Returns the measured time in milliseconds.
     *
     * @return elapsed time in milliseconds
     */
    public double getTimeMillis() {
        return timer.getTimeMillis();
    }

    // Distance methods

    /**
     * Resets the distance tracker to zero.
     */
    public void startDistance() {
        distance.Start();
    }

    /**
     * Adds a given distance to the total recorded distance.
     *
     * @param dist distance to add
     */
    public void addDistance(int dist) {
        distance.addDistance(dist);
    }

    /**
     * Returns the total accumulated distance.
     *
     * @return total distance
     */
    public int getDistance() {
        return distance.getDistance();
    }

    // Moves methods

    /**
     * Sets the number of moves taken in the algorithm's path.
     *
     * @param moves number of moves
     */
    public void setMoves(int moves) {
        this.moves = moves;
    }

    /**
     * Returns the number of moves taken.
     *
     * @return move count
     */
    public int getMoves() {
        return moves;
    }

    /**
     * Increments the move counter by one.
     */
    public void incrementMoves() {
        this.moves++;
    }

    // Vertices visited methods

    /**
     * Sets the number of vertices visited during the search.
     *
     * @param count number of visited vertices
     */
    public void setVerticesVisited(int count) {
        this.verticesVisited = count;
    }

    /**
     * Returns the number of visited vertices.
     *
     * @return visited vertex count
     */
    public int getVerticesVisited() {
        return verticesVisited;
    }

    /**
     * Increments the visited vertex counter by one.
     */
    public void incrementVerticesVisited() {
        this.verticesVisited++;
    }

    // Edges checked methods

    /**
     * Sets the number of edges checked during the search.
     *
     * @param count number of edges checked
     */
    public void setEdgesChecked(int count) {
        this.edgesChecked = count;
    }

    /**
     * Returns the number of checked edges.
     *
     * @return checked edge count
     */
    public int getEdgesChecked() {
        return edgesChecked;
    }

    /**
     * Increments the checked edge counter by one.
     */
    public void incrementEdgesChecked() {
        this.edgesChecked++;
    }

    /**
     * Displays all recorded statistics for a given algorithm.
     *
     * @param algorithmName name of the algorithm (e.g., "DFS", "BFS")
     */
    public void display(String algorithmName) {
        System.out.println("\n=== " + algorithmName + " Statistics ===");
        System.out.println("Time: " + getTime() + " ns (" + getTimeMillis() + " ms)");
        System.out.println("Path Length (moves): " + getMoves());
        System.out.println("Total Distance: " + getDistance());
        System.out.println("Vertices Visited: " + getVerticesVisited());
        System.out.println("Edges Checked: " + getEdgesChecked());
    }
}
