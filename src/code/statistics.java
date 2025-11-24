package a5;

public class Statistics {
    // Inner Timer class
    private class Timer {
        private long start = 0;
        private long stop = 0;
        
        public void Start() {
            start = System.nanoTime();
            stop = 0;
        }
        
        public void Stop() {
            stop = System.nanoTime();
        }
        
        public long getTime() {
            if (stop == 0) {
                return System.nanoTime() - start;
            }
            return stop - start;
        }
        
        public double getTimeMillis() {
            return getTime() / 1_000_000.0;
        }
    }
    
    // Inner Distance class
    private class Distance {
        private int distance;
        
        public Distance() {
            this.distance = 0;
        }
        
        public void Start() {
            distance = 0;
        }
        
        public void addDistance(int dist) {
            this.distance += dist;
        }
        
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
    
    public Statistics() {
        this.timer = new Timer();
        this.distance = new Distance();
        this.moves = 0;
        this.verticesVisited = 0;
        this.edgesChecked = 0;
    }
    
    // Timer methods
    public void startTimer() {
        timer.Start();
    }
    
    public void stopTimer() {
        timer.Stop();
    }
    
    public long getTime() {
        return timer.getTime();
    }
    
    public double getTimeMillis() {
        return timer.getTimeMillis();
    }
    
    // Distance methods
    public void startDistance() {
        distance.Start();
    }
    
    public void addDistance(int dist) {
        distance.addDistance(dist);
    }
    
    public int getDistance() {
        return distance.getDistance();
    }
    
    // Moves methods
    public void setMoves(int moves) {
        this.moves = moves;
    }
    
    public int getMoves() {
        return moves;
    }
    
    public void incrementMoves() {
        this.moves++;
    }
    
    // Vertices visited methods
    public void setVerticesVisited(int count) {
        this.verticesVisited = count;
    }
    
    public int getVerticesVisited() {
        return verticesVisited;
    }
    
    public void incrementVerticesVisited() {
        this.verticesVisited++;
    }
    
    // Edges checked methods
    public void setEdgesChecked(int count) {
        this.edgesChecked = count;
    }
    
    public int getEdgesChecked() {
        return edgesChecked;
    }
    
    public void incrementEdgesChecked() {
        this.edgesChecked++;
    }
    
    // Display method
    public void display(String algorithmName) {
        System.out.println("\n=== " + algorithmName + " Statistics ===");
        System.out.println("Time: " + getTime() + " ns (" + getTimeMillis() + " ms)");
        System.out.println("Path Length (moves): " + getMoves());
        System.out.println("Total Distance: " + getDistance());
        System.out.println("Vertices Visited: " + getVerticesVisited());
        System.out.println("Edges Checked: " + getEdgesChecked());
    }
}
