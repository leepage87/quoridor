package src.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.util.*;

public class BoardWall extends JButton implements ActionListener {
	
	static HashMap<String, BoardWall> map = new HashMap<String, BoardWall>();

	private String name;
	private boolean wallPresent;
	
	public String getLocalName() {
		return name;
	}

	private JComponent me;
	
	public BoardWall nextWall () 
	{
		String oldname = name;
		int temp;
		//if (oldname.charAt(1) == '9')
			//return null;
			//temp = (int)(oldname.charAt(1)-'0')-1;
		//else
		temp = (int)(oldname.charAt(1)-'0')+1;
		String newname = oldname.charAt(0) + Integer.toString(temp) + oldname.charAt(2);
		BoardWall returnwall = getWall(newname);
			
		return returnwall;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		BoardWall wall = (BoardWall) me;
		BoardWall next;
		if (wall.name.charAt(1) != '9')
			next = wall.nextWall();
		else
			next = null;
		//JOptionPane.showMessageDialog(this.getParent(), "my name is " + button1.getLocalName());
		if (next != null)
			if ((!wall.wallPresent) && (!next.wallPresent)) {
				wall.setWall();
				next.setWall();
		}
	}
	
	public BoardWall getWall (String str) {
		BoardWall returnwall = map.get(str);
		return returnwall;
	}
	
	public void setWall() {
		//JOptionPane.showMessageDialog(this.getParent(), "my name is " + button1.getLocalName());
		//setRolloverEnabled(false);
		setBackground(Color.BLUE);
		wallPresent = true;
	}
	
	public void rolloverTests(){
		this.addMouseListener(new java.awt.event.MouseAdapter() {
	
        public void mouseEntered(java.awt.event.MouseEvent evt) {
        	BoardWall wall = (BoardWall) me;
        	BoardWall next;
        	if (wall.name.charAt(1) != '9') 
        		next = wall.nextWall();
        	else 
        		next = null;
        	if (next != null)
        		if ((wall.wallPresent == true) || (next.wallPresent == true))
        			;//setRolloverEnabled(false);
        		else{
        			setBackground(Color.GREEN);
        			next.setBackground(Color.GREEN);
        		}
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	BoardWall wall = (BoardWall) me;
            	BoardWall next;
            	if (wall.name.charAt(1) !=9) {
            		next = wall.nextWall();
            	}
            	else
            		next = null;
            	if (next != null)
            	{
            		if ((wall.wallPresent == true) || (next.wallPresent == true)) {
            			setRolloverEnabled(false);
            			next.setRolloverEnabled(false);
            		}
            		else
            		{
            			setBackground(Color.BLACK);
            			setRolloverEnabled(true);
            			next.setBackground(Color.BLACK);
            		
            		}	
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

		if (this.name.charAt(1) != '9') {
			this.setRolloverEnabled(true);
			addActionListener(this);
		}
		else
			this.setRolloverEnabled(false);
		
		rolloverTests();

	        	
	    }
	}

