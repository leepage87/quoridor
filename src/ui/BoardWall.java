package src.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class BoardWall extends JButton implements ActionListener {

	private String name;
	private boolean wallPresent;

	

	
	public String getLocalName() {
		return name;
	}

	private JComponent me;
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		BoardWall wall = (BoardWall) me;
		//JOptionPane.showMessageDialog(this.getParent(), "my name is " + button1.getLocalName());
		wall.setBackground(Color.BLUE);
		wall.setRolloverEnabled(false);
		wallPresent = true;
		if ((wall.name.charAt(2) == 'H') && (wall.name.charAt(0) == '8'))
		{
			String oldname = wall.name;
			int temp = (int)(oldname.charAt(0))-1;
			String newname = Integer.toString(temp) + oldname.charAt(1) + oldname.charAt(2);
			setWall(getWall(newname));
		}
		else if (wall.name.charAt(2) == 'H')
			;//tell wall to the right to draw
		if ((wall.name.charAt(2) == 'V') && (wall.name.charAt(1) == '8'))
			;//tell wall above to draw
		else
			;//tell wall below to draw
	}
	
	public BoardWall getWall (String str) {
		// get the wall button corresponding to this string
		return null;
	}
	
	public void setWall(BoardWall wall) {
		//JOptionPane.showMessageDialog(this.getParent(), "my name is " + button1.getLocalName());
		wall.setBackground(Color.BLUE);
		wall.setRolloverEnabled(false);
		wall.wallPresent = true;
	}
	
	public void rolloverTests(){
		this.addMouseListener(new java.awt.event.MouseAdapter() {
	
        public void mouseEntered(java.awt.event.MouseEvent evt) {
        	if(wallPresent == true)
        		setRolloverEnabled(false);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	if (wallPresent == false)
            	{
            		setRolloverEnabled(true);
            		setBackground(Color.BLACK);
            	}

            }
            });
	}
	
	public void setWallPresent(boolean setting){
		this.wallPresent = setting;
	}
	

	BoardWall(String name) {
		wallPresent = false;
		setBackground(Color.BLACK);
		this.name = name;
		me = this;

		this.setRolloverEnabled(true);
		addActionListener(this);
		rolloverTests();

	        	
	    }
	}

