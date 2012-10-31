package src.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;
/* NO LONGER USED. DELETE.*/


public class GameBoardPrototype extends JFrame {
	public GameBoardPrototype() {
		super();
		getContentPane().setBackground(new Color(0,139,69));
		setSize(720, 720);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(new MyComponent());
		
		setVisible(true);
	}
	public static void main(String... args) {
		GameBoardPrototype window = new GameBoardPrototype();
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

