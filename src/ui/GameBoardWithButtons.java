package src.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;


public class GameBoardWithButtons extends JFrame implements ActionListener
{
	
	static Icon playerOne = new ImageIcon(GameBoardWithButtonsPrototype.class.getResource("player1.png"));
	static Icon playerTwo = new ImageIcon(GameBoardWithButtonsPrototype.class.getResource("player2.png"));
	static Icon playerThree = new ImageIcon(GameBoardWithButtonsPrototype.class.getResource("player3.png"));
	static Icon playerFour = new ImageIcon(GameBoardWithButtonsPrototype.class.getResource("player4.png"));
	static Icon legalMove = new ImageIcon(GameBoardWithButtonsPrototype.class.getResource("legalMove.png"));
	static Icon defaultIcon = new ImageIcon(GameBoardWithButtonsPrototype.class.getResource("default.gif"));
	public final static String MAIN_WINDOW_TITLE = "Quoridor";
	private JPanel contentPane;
	
	final int SIZE = 630; // width/height of game board
	final int DOWNOFFSET = 20; // distance to draw board from top of window
	final int RIGHTOFFSET = 20; // distance to draw board from left side of window
	final int WALLLENGTH = SIZE * 2/21;
	final int WALLWIDTH = SIZE/9 - WALLLENGTH;
	
	public GameBoardWithButtons(Board b)  {
		super();
		JMenuBar menu = makeMenuBar();
		menu.setVisible(true);
		add(menu);
		setName(MAIN_WINDOW_TITLE);
		setTitle(MAIN_WINDOW_TITLE);
		setSize(SIZE + 2*RIGHTOFFSET, SIZE + 2*DOWNOFFSET+50);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		
		JButton[][] tokenSquares = new JButton[9][9];
		for (int across = 0; across < 9; across++)	{
			for (int down = 0; down < 9; down++)	{
				tokenSquares[across][down] = new BoardButton("B"+across + down);
				tokenSquares[across][down].setBounds(SIZE*across/9 + WALLWIDTH/2, SIZE*down/9+DOWNOFFSET + WALLWIDTH/2, WALLLENGTH,WALLLENGTH);
				add(tokenSquares[across][down]);
				tokenSquares[across][down].setName("Button"+across + "" +down);
				BoardButton.map.put("B"+ across + down, (BoardButton)tokenSquares[across][down]);
				if (b.grid[2*across][2*down] == 1)
					tokenSquares[across][down].setIcon(playerOne);
				else if (b.grid[2*across][2*down] == 2)
					tokenSquares[across][down].setIcon(playerTwo);
				else if (b.grid[2*across][2*down] == 3)
					tokenSquares[across][down].setIcon(playerThree);
				else if (b.grid[2*across][2*down] == 4)
					tokenSquares[across][down].setIcon(playerFour);
			}
		}
		
		JButton[][] vertwalls = new JButton[10][10];
		for (int across = 1; across < 9; across++)	{
			for (int down = 1; down < 10; down++)	{
				vertwalls[across][down] = new BoardWall("" + across + down + "V");
				vertwalls[across][down].setBounds(SIZE*across/9 - WALLWIDTH/2, SIZE*(down-1)/9 +DOWNOFFSET+WALLWIDTH/2, WALLWIDTH, WALLLENGTH);
				vertwalls[across][down].setVisible(true);
				add(vertwalls[across][down]);
				BoardWall.map.put("" + across + down + "V", (BoardWall)vertwalls[across][down]);
				vertwalls[across][down].setName("VertWall" + across + down);
			}
		}
		
		JButton[][] horzwalls = new BoardWall[10][10];
		for (int across = 1; across < 10; across++) {
			for (int down = 1; down < 9; down++) {
				horzwalls[across][down] = new BoardWall("" + across + down + "H");
				horzwalls[across][down].setRolloverEnabled(false);
				horzwalls[across][down].setBounds(SIZE*(across-1)/9 + WALLWIDTH/2, SIZE*down/9 +DOWNOFFSET - WALLWIDTH/2, WALLLENGTH, WALLWIDTH);
				horzwalls[across][down].setVisible(true);
				add(horzwalls[across][down]);
				BoardWall.map.put("" + across + down + "H", (BoardWall) horzwalls[across][down]);
				horzwalls[across][down].setName("HorzWall" + across + down);
			}
		}
		
		
/*		for (int down = 1; down < 9; down++) {
			for (int across = 1; across < 10; across++) {
				horzwalls[down][across] = new BoardWall("" + down + across + "H");
				horzwalls[down][across].setRolloverEnabled(false);
				horzwalls[down][across].setBounds(SIZE*(across-1)/9 + WALLWIDTH/2, SIZE*down/9 +DOWNOFFSET - WALLWIDTH/2, WALLLENGTH, WALLWIDTH);
				horzwalls[down][across].setVisible(true);
				add(horzwalls[down][across]);
				BoardWall.map.put(""+ down + across + "H", (BoardWall) horzwalls[down][across]);
				horzwalls[down][across].setName("HorzWall" + down + across);
			}
		}*/
		setVisible(true);
		
	}
	
/*	public static void main(String... args) 
	{
		GameBoardWithButtonsPrototype window = new GameBoardWithButtonsPrototype();
	}*/
	

	
	/*
	public void mouseClicked(MouseEvent e) {
	       //e.getComponent().setIcon(playerOne);
	       }
	*/
	/*
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("Appear".equals(e.getActionCommand())) {
			
		}
		
	}
	*/
	
	public JMenuBar makeMenuBar() {
		JMenuBar menuBar;
		JMenu fileMenu;
		JMenuItem menuItem;
		
		//Making the menu bar
		menuBar = new JMenuBar();
		//Adding the "File" menu to menuBar
		fileMenu = new JMenu("File");
		//ALT+F selects File menu
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
		menuBar.setName("MenuBar");
		
		//add "New Game" feature to File menu
		menuItem = new JMenuItem("New Game",
                KeyEvent.VK_N);
	    menuItem.setActionCommand("New");  
	    menuItem.addActionListener(this);
		fileMenu.add(menuItem);
		
		//add "Quit" feature to file menu, action command "Quit"
		menuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
	    menuItem.setActionCommand("Quit");
	    //ActionEvent x = new ActionEvent();
	    menuItem.addActionListener(this);
		fileMenu.add(menuItem);
		//make a help menu, 
		fileMenu = new JMenu("Help");
		fileMenu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(fileMenu);
		//add how to play menu item, action command "Instructions"
		menuItem = new JMenuItem("How to Play", KeyEvent.VK_P);
		menuItem.setActionCommand("Instructions");
		menuItem.addActionListener(this);
		fileMenu.add(menuItem);
		
		//add about menu item, action command "About"
		menuItem = new JMenuItem("About", KeyEvent.VK_A);
		menuItem.setActionCommand("About");
		menuItem.addActionListener(this);
		fileMenu.add(menuItem);
		//add the menu bar to the frame
		setJMenuBar(menuBar);
		return menuBar;
		
	    
	  
		
	}
	
	public void actionPerformed(ActionEvent e) {
	    if ("Quit".equals(e.getActionCommand())) {
	    	System.exit(0);
	    } else if ("About".equals(e.getActionCommand())) {
	    	JOptionPane.showMessageDialog(contentPane, "Programming by: \nLee Page, Jonathan \"the Beard\" Gould," +
	    			"\nSarah Weller, and Timothy Simmons\n\nCIS 405 - Sofware Engineering - Fall 2012", "Quoridor", 	JOptionPane.INFORMATION_MESSAGE);
	    } else if ("Instructions".equals(e.getActionCommand())){
	    	String [] options = {"Open Rules", "Close"};
	    	int n = JOptionPane.showOptionDialog(contentPane,
	    		    "Click square to move your player, if the move" +
	    		    "\nis legal the square will be highlighted.\n" +
	    		    "Click the black lines to place a wall.\n" +
	    		    "Walls are placed from left to right or top\n" +
	    		    "to bottom.",
	    		    "How to Play",
	    		    JOptionPane.YES_NO_OPTION,
	    		    JOptionPane.QUESTION_MESSAGE,
	    		    null,     //do not use a custom Icon
	    		    options,  //the titles of buttons
	    		    options[0]); //default button title
	    	if (n == JOptionPane.YES_OPTION){
	    		OpenBrowser.openURL("http://en.wikipedia.org/wiki/Quoridor#Rules_of_the_game");
	    	}
	    }
	} 
	
	
}
