package ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JButton;

public class GameBoardWithButtonsPrototype extends JFrame 
{
	public GameBoardWithButtonsPrototype() {
		super();
		getContentPane().setBackground(new Color(0,139,69));
		setSize(720, 720);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		final int SIZE = 630;
		setContentPane(new MyComponent());
		JButton[][] tokenSquares = new JButton[9][9];
		for (int i = 0; i < 9; i++)	{
			for (int j = 0; j < 9; j++)	{
				tokenSquares[i][j] = new JButton();
				tokenSquares[i][j].setBounds(SIZE*i/9 + 5, SIZE*j/9+5, 60,60);
				tokenSquares[i][j].setBackground(Color.RED);
				tokenSquares[i][j].setVisible(true);
				add(tokenSquares[i][j]);
			}
		}
		
		JButton[][] vertwalls = new JButton[10][10];
		for (int i = 1; i < 9; i++)	{
			for (int j = 1; j < 10; j++)	{
				vertwalls[i][j] = new JButton();
				vertwalls[i][j].setBounds(SIZE*i/9 - 5, SIZE*(j-1)/9 + 5, 10, 60);
				vertwalls[i][j].setBackground(Color.BLUE);
				vertwalls[i][j].setVisible(true);
				add(vertwalls[i][j]);
			}
		}
		
		JButton[][] horzwalls = new JButton[10][10];
		for (int i = 1; i < 9; i++) {
			for (int j = 1; j < 10; j++) {
				horzwalls[i][j] = new JButton();
				horzwalls[i][j].setBounds(SIZE*(j-1)/9 + 5, SIZE*i/9 - 5, 60, 10);
				horzwalls[i][j].setBackground(Color.ORANGE);
				horzwalls[i][j].setVisible(true);
				add(horzwalls[i][j]);
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
			g.setColor(new Color(0,139,69));
			g.fillRect(0,0,SIZE,SIZE);
			g.setColor(Color.BLACK);
			for (int i = 0; i < 10; i++)
			{
				g.fillRect((SIZE*i/9)-5, 0, 10, SIZE);
				g.fillRect(0,(SIZE*i/9)-5,SIZE,10);
			}
			
			/* NOTES:
			 * use button.setEnabled() to turn walls/tokens on/off
			 * Can buttons go *anywhere*? Ask Dr. Ladd
			 * Button has icon/image that changes? Ask!
			 */
		}
	}
}
