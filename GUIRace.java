package code;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;

public class GUIRace extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIRace frame = new GUIRace();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUIRace() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPane);
		mainPane.setLayout(new BorderLayout(0, 0));
		
		JPanel racePanel = new JPanel();
		mainPane.add(racePanel, BorderLayout.CENTER);
		racePanel.setLayout(new GridLayout(0, 1, 0, 0));
		// Graph, start vertex, target vertex
		GUI_TimeTrial timeTrial = new GUI_TimeTrial();
		//MazeVisualizer race1 = new MazeVisualizer(timeTrial, 10, 10);
		/*racePanel.add(race1);
		race1.setLayout(new GridLayout(1, 0, 0, 0));
		*/
		
		JPanel race2 = new JPanel();
		racePanel.add(race2);
		
		JPanel topMenu = new JPanel();
		mainPane.add(topMenu, BorderLayout.NORTH);
		topMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton StartButton = new JButton("START");
		topMenu.add(StartButton);
		
		JLabel Title = new JLabel("The Great Graph Race ");
		topMenu.add(Title);
		
		JPanel TopTimer = new JPanel();
		topMenu.add(TopTimer);
		
		//Tick speed for running time and graphs		
		

	}

}
