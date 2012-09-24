/**
 * 
 */
package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

/**
 * A JFrame is a Java Frame...a rectangular region mapped onto the screen in
 * some way. It could be considered a "window" except that in terms of screen
 * widgets (widget is the generic term for things that display on the screen:
 * buttons, menus, checkboxes, and, yes, frames) a window is only the largest,
 * top-level widget and there can be multiple frames within any single
 * top-level.
 * 
 * This class demonstrates how to create a window, add a label, and test that
 * the window and the label appear using the WindowLicker toolkit.
 * 
 * @author blad
 * 
 */
public class MenuBarPrototype extends JFrame implements ActionListener {
	/**
	 * The text displayed above the window when it is created; shared with the
	 * test driver code
	 */
	
	
	public final static String MAIN_WINDOW_TITLE = "Quoridor";


	/**
	 * Create a new top-level window out of this type. The constructor calls the
	 * super constructor, sets the name, title, size, and what happens when the
	 * window closes, and then makes the window visible on the screen.
	 * 
	 * In order for WindowLicker to find the window, the window must be visible
	 * and have a name that WL knows (assuming you use JFrameDriver.named to
	 * find the window).
	 */
	public MenuBarPrototype() {
		super();
		makeMenuBar();
		setName(MAIN_WINDOW_TITLE);
		setTitle(MAIN_WINDOW_TITLE);
		setSize(640, 480);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setBackground(new Color(0,109,99)); 		
		setVisible(true);
		
	}
	
	private void makeMenuBar() {
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
		
	    
	  
		
	}
	
	public void actionPerformed(ActionEvent e) {
	    if ("Quit".equals(e.getActionCommand())) {
	    	System.exit(0);
	    } else {
	    	//new game
	    }
	} 

	/**
	 * The main function. Remember: main is just a name for a function. It is
	 * also the name of the function searched for by Java when running a class;
	 * that does not preclude calling the function directly. This is an example
	 * of dependency injection.
	 * 
	 * @param args
	 */
	public static void main(String... args) {
		MenuBarPrototype window = new MenuBarPrototype();
	}

}
