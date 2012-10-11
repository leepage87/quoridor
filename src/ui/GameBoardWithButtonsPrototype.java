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


public class GameBoardWithButtonsPrototype extends JFrame implements ActionListener
{
	public final static String MAIN_WINDOW_TITLE = "Quoridor";
	private JPanel contentPane;
	
	public GameBoardWithButtonsPrototype()  {
		super();
		JMenuBar menu = makeMenuBar();
		menu.setVisible(true);
		add(menu);
		setName(MAIN_WINDOW_TITLE);
		setTitle(MAIN_WINDOW_TITLE);
		setSize(720, 720);
		//setBackground(Color.BLACK);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		final int SIZE = 630;
		//setContentPane(new MyComponent());
		JButton[][] tokenSquares = new JButton[9][9];
		for (int i = 0; i < 9; i++)	{
			for (int j = 0; j < 9; j++)	{
				tokenSquares[i][j] = new BoardButton(""+i + j);
				tokenSquares[i][j].setBounds(SIZE*i/9 + 5, SIZE*j/9+55, 60,60);
				add(tokenSquares[i][j]);
				tokenSquares[i][j].setName("Button"+i + "" +j);
			}
		}
		
		JButton[][] vertwalls = new JButton[10][10];
		for (int i = 1; i < 9; i++)	{
			for (int j = 1; j < 10; j++)	{
				vertwalls[i][j] = new BoardWall("" + i + j + "V");
				vertwalls[i][j].setBounds(SIZE*i/9 - 5, SIZE*(j-1)/9 +55, 10, 60);
				vertwalls[i][j].setVisible(true);
				add(vertwalls[i][j]);
				BoardWall.map.put("" + i + j + "V", (BoardWall)vertwalls[i][j]);
				vertwalls[i][j].setName("VertWall" + i + j);
			}
		}
		
		JButton[][] horzwalls = new BoardWall[10][10];
		for (int i = 1; i < 9; i++) {
			for (int j = 1; j < 10; j++) {
				horzwalls[i][j] = new BoardWall("" + i + j + "H");
				horzwalls[i][j].setRolloverEnabled(false);
				horzwalls[i][j].setBounds(SIZE*(j-1)/9 + 5, SIZE*i/9 +45, 60, 10);
				horzwalls[i][j].setVisible(true);
				add(horzwalls[i][j]);
				BoardWall.map.put(""+ i + j + "H", (BoardWall) horzwalls[i][j]);
				horzwalls[i][j].setName("HorzWall" + i + j);
			}
		}
		setVisible(true);
		
	}
	
	public static void main(String... args) 
	{
		GameBoardWithButtonsPrototype window = new GameBoardWithButtonsPrototype();
	}
	
	static class MyComponent extends JComponent 
	{
		public void paint (Graphics g) 
		{
			final int SIZE = 630;
			g.setColor(new Color(0,109,99));
			g.fillRect(5,0,SIZE,SIZE);
			g.setColor(Color.BLACK);
			for (int i = 0; i < 10; i++)
			{
				g.fillRect((SIZE*i/9)-5, 0, 10, SIZE);
				g.fillRect(0,(SIZE*i/9)-5,SIZE,10);
			}
		}
	}

	
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
