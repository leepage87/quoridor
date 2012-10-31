/**
 * 
 */
package src.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
/* NO LONGER USED. DELETE. */
public class MenuBarPrototype extends JFrame implements ActionListener {
	
	
	public final static String MAIN_WINDOW_TITLE = "Quoridor";
	private JPanel contentPane;


	public MenuBarPrototype() {
		super();
		makeMenuBar();
		//setName(MAIN_WINDOW_TITLE);
		//setTitle(MAIN_WINDOW_TITLE);
		//setSize(640, 480);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		//getContentPane().setBackground(new Color(0,109,99)); 		
		setVisible(true);
		
	}
	
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
	    	JOptionPane.showMessageDialog(contentPane, "Programming by: Lee Page, Jonathan Gould," +
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

	/**
	 * @param args
	 */
	/*
	public static void main(String... args) {
		MenuBarPrototype window = new MenuBarPrototype();
	}*/

}
