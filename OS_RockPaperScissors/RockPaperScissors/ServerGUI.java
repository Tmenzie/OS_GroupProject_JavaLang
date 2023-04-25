package RockPaperScissors;

import java.awt.*;
import javax.swing.*;

public class ServerGUI extends JFrame 
{

	// Data Fields
    private JPanel center; 	
    private JPanel north; 	
    private JPanel south; 	
    JLabel status;

	public ServerGUI() 
	{

		// Setting panel defaults
	    this.setTitle("Rock Paper Scissors - Server");
		this.setSize(400, 100);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create and add central panel
		center = new JPanel();
		getContentPane().add(center, BorderLayout.CENTER);
		center.setLayout(new BorderLayout(0, 0));

		// Creating panel for connection button
		north = new JPanel();
		center.add(north, BorderLayout.NORTH);

		// Creating panel to display the status of server
		JPanel south = new JPanel();
		center.add(south, BorderLayout.SOUTH);

		status = new JLabel();
		south.add(status);

	}

}