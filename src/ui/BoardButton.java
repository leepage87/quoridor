package src.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import src.main.PlayQuor;
/* Draws the game tiles. Has a map from Button name to Button.*/
public class BoardButton extends JButton implements ActionListener {

	// Map from button name to button itself. Allows other classes
	// to access a button using its name.
	public static HashMap<String, BoardButton> map = new HashMap<String, BoardButton>();

	private String name; // name of each button
	private static boolean playerPresent; // is there a pawn here?

	/* Return the button associated with the name passed. */
	public static BoardButton getButton (String str) {
		BoardButton returnButton = map.get(str);
		return returnButton;
	}

	public String getLocalName() {
		return name;
	}

	private JComponent me;

	/* When a tile is clicked, update fields in PlayQuor.
	 * Clicked ends the infinite loop (of waiting for a click).
	 * nextMove sends data on the button (tile/wall) clicked. */
	@Override
	public void actionPerformed(ActionEvent e) {

		BoardButton button = (BoardButton) me;
		button.setRolloverEnabled(false);
		PlayQuor.clicked = true;
		PlayQuor.nextMove = button.name;
	}

	/* Seems unused. Commented out for now. public void rolloverTests(){
		this.addMouseListener(new java.awt.event.MouseAdapter() {

			public void mouseEntered(java.awt.event.MouseEvent evt) {
				if(playerPresent == true)
					setRolloverEnabled(false);
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				if (playerPresent == false)
					setRolloverEnabled(false);
			}
		});
	}*/

	public static void setPlayerPresent(boolean setting, int col, int row){
		map.get("B"+col+row).playerPresent = setting;
	}

	/* Creates each button. Sets it to not contain a player. Sets default icon.
	 * Sets name. Sets rollover to legal move icon. Adds action listener. */
	BoardButton(String name) {
		playerPresent = false;
		this.setIcon(GameBoardWithButtons.defaultIcon);
		this.name = name;
		me = this;

		this.setRolloverEnabled(true);
		//this.setRolloverIcon(GameBoardWithButtons.legalMove);
		addActionListener(this);
	}
}

