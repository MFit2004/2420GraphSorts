package code;

import java.awt.EventQueue;
import javax.swing.JFrame;
/**
 * @author Matthew_Fitzgerald
 * @course CS-2420-001 Algorithms & Data Structures Fall 2025
 * @last_modified Nov 24, 2025
 * @assignment Team Project:
 * 				Algorithm Racing
 * @description Jframe demo of how to use a 
 * 				Stopwatch
 */
public class TestFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TestFrame frame = new TestFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public TestFrame() {
        setTitle("StopWatch Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new stopWatch("Test"));
        pack();
        setLocationRelativeTo(null); 
    }
}
