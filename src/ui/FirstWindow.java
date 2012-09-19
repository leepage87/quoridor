/**
 * 
 */
package ui;

import javax.swing.JFrame;

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
public class FirstWindow extends JFrame {
	/**
	 * The text displayed above the window when it is created; shared with the
	 * test driver code
	 */
	public final static String MAIN_WINDOW_TITLE = "FirstWindow";

	/**
	 * Create a new top-level window out of this type. The constructor calls the
	 * super constructor, sets the name, title, size, and what happens when the
	 * window closes, and then makes the window visible on the screen.
	 * 
	 * In order for WindowLicker to find the window, the window must be visible
	 * and have a name that WL knows (assuming you use JFrameDriver.named to
	 * find the window).
	 */
	public FirstWindow() {
		super();
		setName(MAIN_WINDOW_TITLE);
		setTitle(MAIN_WINDOW_TITLE);
		setSize(640, 480);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setVisible(true);
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
		FirstWindow window = new FirstWindow();
	}

}
