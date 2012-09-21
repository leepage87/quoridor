/**
 * 
 */
package ui;

import static java.awt.BorderLayout.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;


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
public class FirstWindow extends JFrame  implements ActionListener {

	public final static String MAIN_WINDOW_TITLE = "FirstWindow";
	
	public final static String LABEL_NAME = "TheLabel";

	// messages displayed by the label
	public static final String INITIAL_MESSAGE = "One fish,";
	public static final String[] MESSAGES = {"Two fish,", "Red fish,", "Blue fish."};
	
	public final static String BUTTON_NAME_PREFIX = "Button";
	public final static String[] BUTTON_TEXTS = {"One", "Two", "Three"};
	private JLabel label;
	private List<JButton> buttons;
	
	private void initializeButtons() {
		for (int i = 0; i < BUTTON_TEXTS.length; ++i) {
			JButton button = new JButton(BUTTON_TEXTS[i]); // sets the text
			button.setName(BUTTON_NAME_PREFIX+i);
			button.addActionListener(this);
			
			buttons.add(button);
			add(button);
		}
	}

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
		getContentPane().setBackground(new Color(0,139,69));
		setName(MAIN_WINDOW_TITLE);
		setTitle(MAIN_WINDOW_TITLE);
		setSize(720, 720);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setBackground(Color.BLUE);
		setContentPane(new MyComponent());
		
		setLayout(new FlowLayout());
		
/*		label = new JLabel();
		label.setName(LABEL_NAME);
		label.setText(INITIAL_MESSAGE);
		
		add(label);
				
		buttons = new ArrayList<JButton>();
		initializeButtons();*/
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent action) {
		for (int i = 0; i < buttons.size(); ++i) {
			if (action.getSource() == buttons.get(i)) {
				label.setText(MESSAGES[i]);
				break;
			}
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
		FirstWindow window = new FirstWindow();
	}
	static class MyComponent extends JComponent {
		public void paint (Graphics g) {
			final int SIZE = 630;
			g.setColor(new Color(0,139,69));
			g.fillRect(0,0,SIZE,SIZE);
			g.setColor(Color.BLACK);
			for (int i = 0; i < 10; i++)
			{
				g.fillRect((SIZE*i/9)-5, 0, 10, SIZE);
				g.fillRect(0,(SIZE*i/9)-5,SIZE,10);
			}
			g.setColor(Color.BLUE);
			for (int i = 0; i < 10; i++)
				for (int j = 0; j < 10; j++)
					g.fillOval((SIZE*i/9)-60, (SIZE*j/9)-60, 50, 50);
		}
	}
}

