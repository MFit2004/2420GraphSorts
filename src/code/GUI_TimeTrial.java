package a5;

import java.util.Scanner;

/**
 * GUI_TimeTrial handles the text-based user interface for running
 * DFS vs BFS time trials on generated grid mazes. It prompts the user
 * for maze size, start and target vertices, and restarts if requested.
 */
public class GUI_TimeTrial {
    
    /**
     * Starts the user interface loop. Displays the header, collects input,
     * initializes a Race object, and optionally repeats based on user input.
     */
    public void Start() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=".repeat(60));
        System.out.println("          ALGORITHM RACER - DFS vs BFS");
        System.out.println("=".repeat(60));
        
        int v = RaceGetter(scanner);
        int start = getStartVertex(scanner, v);
        int target = getTargetVertex(scanner, v, start);
        
        Race race = new Race(v, start, target);
        race.Start();
        
        System.out.print("\nRun another race? (y/n): ");
        String response = scanner.next();
        if (response.toLowerCase().startsWith("y")) {
            Start();
        } else {
            System.out.println("\nThanks for using Algorithm Racer!");
        }
        
        scanner.close();
    }
    
    /**
     * Prompts the user for an approximate number of vertices for the maze.
     * Ensures the number is valid and adjusts it to produce a square grid.
     *
     * @param scanner the Scanner used for user input
     * @return the validated number of vertices
     */
    public int RaceGetter(Scanner scanner) {
        System.out.println("\nEnter the approximate number of vertices for the maze");
        System.out.println("(Will be adjusted to create a square grid)");
        System.out.print("Vertices (e.g., 100 for 10x10 grid): ");
        
        int v = 100;
        if (scanner.hasNextInt()) {
            v = scanner.nextInt();
            if (v < 4) {
                System.out.println("Too small! Using minimum of 4 vertices (2x2 grid)");
                v = 4;
            }
        }
        
        return v;
    }
    
    /**
     * Prompts the user for a start vertex and validates that it falls within
     * the range of valid vertices for the generated grid.
     *
     * @param scanner     the Scanner used for input
     * @param maxVertices the approximate number of vertices entered by the user
     * @return a valid start vertex
     */
    private int getStartVertex(Scanner scanner, int maxVertices) {
        int gridSize = (int) Math.sqrt(maxVertices);
        int actualVertices = gridSize * gridSize;
        
        System.out.println("\nMaze will be a " + gridSize + "x" + gridSize + " grid");
        System.out.println("Vertices range from 0 to " + (actualVertices - 1));
        System.out.print("Enter start vertex (default: 0): ");
        
        int start = 0;
        if (scanner.hasNextInt()) {
            start = scanner.nextInt();
            if (start < 0 || start >= actualVertices) {
                System.out.println("Invalid! Using default start: 0");
                start = 0;
            }
        }
        
        return start;
    }
    
    /**
     * Prompts the user for a target vertex, validates the value, ensures
     * that it differs from the start vertex, and falls within the grid bounds.
     *
     * @param scanner     the Scanner used for input
     * @param maxVertices the approximate number of vertices entered by the user
     * @param start       the previously selected start vertex
     * @return a valid target vertex
     */
    private int getTargetVertex(Scanner scanner, int maxVertices, int start) {
        int gridSize = (int) Math.sqrt(maxVertices);
        int actualVertices = gridSize * gridSize;
        
        System.out.print("Enter target vertex (default: " + (actualVertices - 1) + "): ");
        
        int target = actualVertices - 1;
        if (scanner.hasNextInt()) {
            target = scanner.nextInt();
            if (target < 0 || target >= actualVertices) {
                System.out.println("Invalid! Using default target: " + (actualVertices - 1));
                target = actualVertices - 1;
            }
            if (target == start) {
                System.out.println("Target cannot be same as start! Using: " + (actualVertices - 1));
                target = actualVertices - 1;
            }
        }
        
        return target;
    }
    
    /**
     * Entry point for running the Algorithm Racer interface.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        GUI_TimeTrial gui = new GUI_TimeTrial();
        gui.Start();
    }
}
