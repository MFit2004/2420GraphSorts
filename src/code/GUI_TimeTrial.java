package code;

import java.util.Scanner;

public class GUI_TimeTrial {
    
	public void Start() {

		Query info = new Query((v, s, t) -> {
		    System.out.println("Launching race with: " + v + ", " + s + ", " + t);
		    Race race = new Race(v);
		    race.setupVisualization(); 
		});


	    info.setVisible(true);
	}
    
    public int RaceGetter(Scanner scanner) {
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
    
    public static void main(String[] args) {
        GUI_TimeTrial gui = new GUI_TimeTrial();
        gui.Start();
    }
}
