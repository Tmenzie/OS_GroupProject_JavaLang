package RockPaperScissors;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class GameClient {
	
	private GameClientGUI ClientGUI;
	private Socket socket;
	private int PORT = 8083;		
	private int playerNumber;
	private GameServer connect;
	
	private BufferedReader input;			
	private PrintWriter output;
	
	public GameClient(String serverAddress) throws Exception {
		
		// Setup Networking
		socket = new Socket(serverAddress, PORT);
		 
		// Setup the ability for the server to get input and output to/from client
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		
		
		// GUI Setup
		ClientGUI = new GameClientGUI();
		ClientGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		if (socket.isConnected())
		{
			ClientGUI.GameFeedbackBox.setText("Player's Connected");
			System.out.println("Connected");
		}
		else
		{
			ClientGUI.GameFeedbackBox.setText("Player's Disconnected");
			System.out.println("NOT Connected");
		}
	
		
		// Action listener for when rock button is pressed
		ClientGUI.RockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				output.println("PLAYER_CHOICE_ROCK");
				ClientGUI.RockButton.setEnabled(false);
				ClientGUI.PaperButton.setEnabled(false);
				ClientGUI.ScissorButton.setEnabled(false);
				ClientGUI.GameFeedbackBox.setText("You chose rock");
			}
		});
		
		// Action listener for when paper button is pressed
		ClientGUI.PaperButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				output.println("PLAYER_CHOICE_PAPER");
				ClientGUI.RockButton.setEnabled(false);
				ClientGUI.PaperButton.setEnabled(false);
				ClientGUI.ScissorButton.setEnabled(false);
				ClientGUI.GameFeedbackBox.setText("You chose paper");
			}
		});
		
		// Action listener for when scissors button is pressed
		ClientGUI.ScissorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				output.println("PLAYER_CHOICE_SCISSORS");
				ClientGUI.RockButton.setEnabled(false);
				ClientGUI.PaperButton.setEnabled(false);
				ClientGUI.ScissorButton.setEnabled(false);
				ClientGUI.GameFeedbackBox.setText("You chose scissors");
			}
		});
	}

	public void play() throws Exception {
		String serverResponse;
		try {
			
			serverResponse = input.readLine();
			
			// Sets up new player connection and updates GUI
			if (serverResponse.startsWith("NEW_"))
			{
				this.playerNumber = Integer.parseInt(serverResponse.substring(4));
				ClientGUI.setTitle("Rock Paper Scissors - Player #" + this.playerNumber);
				ClientGUI.GameFeedbackBox.setText("Connected - Waiting for Opponent");
			}
			
			// Awaits server response
			while (true) {
				serverResponse = input.readLine();
				if (serverResponse != null) 
				{
					
					// Tells players start
					if (serverResponse.startsWith("START")) 
					{
						ClientGUI.GameFeedbackBox.setText("Connection Established - Please Choice Rock, Paper, or Scissors");
						ClientGUI.RockButton.setEnabled(true);
						ClientGUI.PaperButton.setEnabled(true);
						ClientGUI.ScissorButton.setEnabled(true);
					} 
					
					// Outputs to user if a player won
					else if (serverResponse.startsWith("WIN"))
					{
						ClientGUI.GameFeedbackBox.setText("You have won!");
						break;
					}
					
					// Outputs to label if player was defeated
					else if (serverResponse.startsWith("DEFEAT")) 
					{
						ClientGUI.GameFeedbackBox.setText("You have been defeated!");
						break;					
					}
					
					// Outputs to label if both players tied
					else if (serverResponse.startsWith("TIE"))
					{
						ClientGUI.GameFeedbackBox.setText("You have both tied!");
						break;
					}
				}
			}
		} 
		finally {
			socket.close();		
		}
	}

	// Displays Popup box that asks if the Users would like to play again
	private boolean wantsToPlayAgain() 
	{
		int response = JOptionPane.showConfirmDialog(ClientGUI,"Do you want to play again?", "Play Again?", JOptionPane.YES_NO_OPTION);
		ClientGUI.dispose();
		return response == JOptionPane.YES_OPTION;
	}

	
	public static void main(String[] args) throws Exception {
		while (true)
		{	
			String serverAddress = (args.length == 0) ? "localhost" : args[0];	
			GameClient client = new GameClient(serverAddress);	
			client.play();																	
			
				if (!client.wantsToPlayAgain()) 
				{		
					client.output.println("QUIT");
					break;
				}
		}
	}
}

