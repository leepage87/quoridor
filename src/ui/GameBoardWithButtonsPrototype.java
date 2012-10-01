package src.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;


public class GameBoardWithButtonsPrototype extends JFrame /*implements ActionListener*/
{

	
	public GameBoardWithButtonsPrototype()  {
		super();
		//getContentPane().setBackground(new Color(55,99,109));
		setSize(720, 720);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		final int SIZE = 630;
		setContentPane(new MyComponent());
		JButton[][] tokenSquares = new JButton[9][9];
		for (int i = 0; i < 9; i++)	{
			for (int j = 0; j < 9; j++)	{
				tokenSquares[i][j] = new BoardButton(""+i + j);
				tokenSquares[i][j].setBounds(SIZE*i/9 + 5, SIZE*j/9+5, 60,60);
				//tokenSquares[i][j].setBackground(new Color(0,99,109));
				//tokenSquares[i][j].setIcon(defaultIcon);
				//tokenSquares[i][j].setPressedIcon(playerOne);

				
				//tokenSquares[i][j].setSelectedIcon(playerOne);
				add(tokenSquares[i][j]);
				//tokenSquares[i][j].addMouseListener(this);
				
				//tokenSquares[i][j].setActionCommand("Appear");
			}
		}
			/*tokenSquares[0][8].setIcon(player1Icon);
			add(tokenSquares[0][8]);
			tokenSquares[0][8].setVisible(true);*/
		
		JButton[][] vertwalls = new JButton[10][10];
		for (int i = 1; i < 9; i++)	{
			for (int j = 1; j < 10; j++)	{
				vertwalls[i][j] = new BoardWall("" + i + j + "V");
				vertwalls[i][j].setBounds(SIZE*i/9 - 5, SIZE*(j-1)/9 + 5, 10, 60);
				//vertwalls[i][j].setBackground(Color.BLUE);
				vertwalls[i][j].setVisible(true);
				add(vertwalls[i][j]);
			}
		}
		
		JButton[][] horzwalls = new BoardWall[10][10];
		for (int i = 1; i < 9; i++) {
			for (int j = 1; j < 10; j++) {
				horzwalls[i][j] = new BoardWall("" + i + j + "H");
				horzwalls[i][j].setBounds(SIZE*(j-1)/9 + 5, SIZE*i/9 - 5, 60, 10);
				//horzwalls[i][j].setBackground(Color.ORANGE);
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
			g.setColor(new Color(0,109,99));
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
}
