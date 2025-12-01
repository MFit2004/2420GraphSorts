package code;

import java.awt.EventQueue;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Query extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	
	private static int vertices;        
	private static int startV;   
	private static int targetV;      

	private int step = 0;  

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Query frame = new Query();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

      

	public Query() { 
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 450, 300);
	    contentPane = new JPanel();
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);

	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    contentPane.add(panel);

	    JLabel lblNewLabel = new JLabel("ALGORITHM RACER - DFS vs BFS");
	    lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(lblNewLabel);

	    JLabel infoLabel = new JLabel(
	        "<html><div style='text-align: center;'>"
	      + "Enter the approximate number of vertices for the maze<br>"
	      + "(Will be adjusted to create a square grid)<br>"
	      + "Vertices (e.g., 100 for 10x10 grid):"
	      + "</div></html>"
	    );
	    infoLabel.setFont(new Font("Arial", Font.PLAIN, 10));
	    infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(infoLabel);

	    JPanel panel_2 = new JPanel();
	    panel.add(panel_2);

	    JLabel lblNewLabel_1 = new JLabel("Vertices");
	    panel_2.add(lblNewLabel_1);

	    textField = new JTextField();
	    panel_2.add(textField);
	    textField.setColumns(10);

	    JButton btnNewButton = new JButton("Submit");
	    panel_2.add(btnNewButton);

	    btnNewButton.addActionListener(e -> {

	        String input = textField.getText();

	        try {
	            if (step == 0) {
	                vertices = Integer.parseInt(input);
	                System.out.println("Saved vertices: " + vertices);

	                lblNewLabel_1.setText("Enter start vertex");
	                textField.setText("");
	                step = 1;
	            }

	            else if (step == 1) {
	                startV = Integer.parseInt(input);
	                System.out.println("Saved second value: " + startV);

	                lblNewLabel_1.setText("Enter end vertex (not in use):");
	                textField.setText("");
	                step = 2;
	            }

	            else if (step == 2) {
	                targetV = Integer.parseInt(input);
	                System.out.println("Saved third value: " + targetV);

	                dispose();  // close window

	                if (listener != null) {
	                    listener.onQueryComplete(vertices, startV, targetV);
	                }
	            }


	        } catch (NumberFormatException ex) {
	            System.out.println("Invalid number");
	        }
	    });
	}
	
	protected static int[] data() {
		return new int[]{vertices, startV, targetV};
	}
	
	public interface QueryListener {
	    void onQueryComplete(int vertices, int start, int target);
	}

	private QueryListener listener;

	/**
	 * pass data to main GUI
	 * @param listener
	 */
	public Query(QueryListener listener) {
	    this();
	    this.listener = listener;
	}

}