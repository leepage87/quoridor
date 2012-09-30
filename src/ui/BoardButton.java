package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class BoardButton extends JButton implements ActionListener {

	private String name;
	private boolean playerPresent;
	Icon playerOne = new ImageIcon(GameBoardWithButtonsPrototype.class.getResource("player1.gif"));
	Icon legalMove = new ImageIcon(GameBoardWithButtonsPrototype.class.getResource("legalMove.png"));
	Icon defaultIcon = new ImageIcon(GameBoardWithButtonsPrototype.class.getResource("default.gif"));
	

	
	public String getLocalName() {
		return name;
	}

	private JComponent me;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		BoardButton button = (BoardButton) me;
		//JOptionPane.showMessageDialog(this.getParent(), "my name is " + button1.getLocalName());
		button.setRolloverEnabled(false);
		button.setIcon(playerOne);
		button.setVisible(true);
		playerPresent = true;
		for (long i = 0; i < 999999999; i++);
		
		
		playerPresent = false;
	}
	
	public void rolloverTests(){
		this.addMouseListener(new java.awt.event.MouseAdapter() {
	
        public void mouseEntered(java.awt.event.MouseEvent evt) {
        	if(playerPresent == true)
        		setRolloverEnabled(false);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	if (playerPresent == false)
            		setRolloverEnabled(true);

            }
            });
	}
	
	public void setPlayerPresent(boolean setting){
		this.playerPresent = setting;
	}
	

 BoardButton(String name) {
		playerPresent = false;
		this.setIcon(defaultIcon);
		this.name = name;
		me = this;

		this.setRolloverEnabled(true);
		this.setRolloverIcon(legalMove);
		addActionListener(this);
		rolloverTests();

	        	
	    }
	}

