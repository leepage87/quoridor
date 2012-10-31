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
/* Creates each wall button. Contains a map from button name to button. */
public class BoardWall extends JButton implements ActionListener {

	static HashMap<String, BoardWall> map = new HashMap<String, BoardWall>();

	private String name;
	private boolean wallPresent;

	public String getLocalName() {
		return name;
	}

	private JComponent me;

	/* Gets the wall to the south or east of the current wall.
	 * Used when previewing/drawing two-unit lines. */
	public BoardWall nextWall () 
	{
		BoardWall returnwall;
		String oldname = name;
		int temp;
		String newname;
		if (oldname.charAt(2) == 'V'){ // it's a vertical wall
			temp = (int)(oldname.charAt(1)-'0')+1;
			newname = oldname.charAt(0) + Integer.toString(temp) + oldname.charAt(2);
			returnwall = getWall(newname);
		}else{ // it's a horizontal wall
			temp = (int)(oldname.charAt(0)-'0')+1;
			newname = Integer.toString(temp) + oldname.charAt(1) + oldname.charAt(2);
			returnwall = getWall(newname);
		}
		return returnwall;
	}

	/* When a wall is clicked, this runs. */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		BoardWall wall = (BoardWall) me;
		BoardWall next;
		/* Since walls are exclusively drawn down and to the right, the bottommost vertical
		 * and rightmost horizontal walls are excluded. */
		if (wall.name.charAt(1) != '9') 
			next = wall.nextWall();
		else
			next = null;

		/* Only performs these actions if a legal (nonbottom nonright wall) was clicked. */
		if (next != null)
			/* Informs PlayQuor that a click was registered, and passes information on the (wall) button. */
			if ((!wall.wallPresent) && (!next.wallPresent)) {
				PlayQuor.nextMove = wall.name;
				PlayQuor.clicked = true;
			}

	}

	/* Returns the wall with the name same as the string passed. */
	public BoardWall getWall (String str) {
		BoardWall returnwall = map.get(str);
		return returnwall;
	}

	/* When Board determines a wall placement legal, it sets the wall clicked
	 * and the next wall with this method. */
	public void setWall() {
		setBackground(Color.ORANGE);
		wallPresent = true;
	}

	/* Handles mouseovers. */
	public void rolloverTests(){
		this.addMouseListener(new java.awt.event.MouseAdapter() {

			public void mouseEntered(java.awt.event.MouseEvent evt) {
				BoardWall wall = (BoardWall) me;
				BoardWall next;
				next = wall.nextWall();
				if (next != null)
					if ((wall.wallPresent == true) || (next.wallPresent == true))
						;
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

	/* Constructor for the wall buttons. Sets rollover only if not in bottom row of vert walls
	 * or right column of horz walls*/
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