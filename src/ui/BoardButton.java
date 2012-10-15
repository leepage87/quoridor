package src.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import src.ui.GameBoardWithButtonsPrototype;

public class BoardButton extends JButton implements ActionListener {

	static HashMap<String, BoardButton> map = new HashMap<String, BoardButton>();

	private String name;
	private static boolean playerPresent;


	public BoardButton getButton (String str) {
		BoardButton returnButton = map.get(str);
		return returnButton;
	}

	public String getLocalName() {
		return name;
	}

	private JComponent me;

	@Override
	public void actionPerformed(ActionEvent e) {

		BoardButton button = (BoardButton) me;
		//JOptionPane.showMessageDialog(this.getParent(), "my name is " + button1.getLocalName());
		button.setRolloverEnabled(false);
		if (PlayQuor.turn == 1)
			button.setIcon(GameBoardWithButtons.playerOne);
		if (PlayQuor.turn == 2)
			button.setIcon(GameBoardWithButtons.playerTwo);
		if (PlayQuor.turn == 3)
			button.setIcon(GameBoardWithButtons.playerThree);
		if (PlayQuor.turn == 4)
			button.setIcon(GameBoardWithButtons.playerFour);
		button.setVisible(true);
		playerPresent = true;
		PlayQuor.clicked = true;
		PlayQuor.nextMove = button.name;

	}

	public void rolloverTests(){
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
	}

	public static void setPlayerPresent(boolean setting, int col, int row){
		map.get("B"+col/2+row/2).playerPresent = setting;
		map.get("B"+col/2+row/2).setIcon(GameBoardWithButtons.defaultIcon);



	}


	BoardButton(String name) {
		playerPresent = false;
		this.setIcon(GameBoardWithButtons.defaultIcon);
		this.name = name;
		me = this;

		this.setRolloverEnabled(true);
		this.setRolloverIcon(GameBoardWithButtons.legalMove);
		addActionListener(this);
		//rolloverTests();


	}
}

