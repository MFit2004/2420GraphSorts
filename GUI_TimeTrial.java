package code;

import java.util.Scanner;

public class GUI_TimeTrial {
    
	/**
	 * @wbp.parser.entryPoint
	 */
	public void Start() {

		Query info = new Query((v, s, t) -> {
		    System.out.println("Launching race with: " + v + ", " + s + ", " + t);
		    Race race = new Race(v, s, t);
		    race.setupVisualization(); 
		});
	

	    info.setVisible(true);
	}

    
    /**
     * @wbp.parser.entryPoint
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
     * @wbp.parser.entryPoint
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
     * @wbp.parser.entryPoint
     */
    public static void main(String[] args) {
        GUI_TimeTrial gui = new GUI_TimeTrial();
        gui.Start();
    }
}
