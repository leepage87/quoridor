package src.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;

import src.main.Board;
import src.main.PlayQuor;
/* Draws the game tiles. Has a map from Button name to Button.*/
@SuppressWarnings("serial")
public class BoardButton extends JButton implements ActionListener {
    // Map from button name to button itself. Allows other classes
    // to access a button using its name.
    public static HashMap<String, BoardButton> map = new HashMap<String, BoardButton>();
    private String name; // name of each button
    private JComponent me;


    /**
     * Creates each button. Sets it to not contain a player. Sets default icon.
     * Sets name. Sets rollover to legal move icon. Adds action listener. 
     * @Param name the name of the button
     */
    BoardButton(String name) {
        this.setIcon(GameBoardWithButtons.defaultIcon);
        this.name = name;
        me = this;
        addActionListener(this);
    }

    /**
     * Takes the name of a button and returns the button object
     * @param str the name of the button
     * @return the button object
     */
    public static BoardButton getButton (String str) {
        BoardButton returnButton = map.get(str);
        return returnButton;
    }
    /**
     * @return the name of the button
     */
    public String getLocalName() {
        return name;
    }

    /**
     * When a tile is clicked, update fields in PlayQuor.
     * Clicked ends the infinite loop (of waiting for a click).
     * nextMove sends data on the button (tile/wall) clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        BoardButton button = (BoardButton) me;
        PlayQuor.setClicked(true);
        PlayQuor.setNextMove(button.name);
    }

    /**
     * changes the icons to reflect pawn movement
     * @param endPlace the new pawn location
     * @param startPlace the old pawn location
     * @param turn the player that just moved
     */
    public static void changeIcons(int[] endPlace, int[] startPlace, int turn) {
        map.get("B"+endPlace[0]/2+ "" + endPlace[1]/2).setIcon(Board.map.get(turn));
        map.get("B"+startPlace[0]/2+startPlace[1]/2).setIcon(GameBoardWithButtons.defaultIcon);
    }
    /**
     * Changes one icon at a time for highlighting/unhighlighting legal moves
     * @param i x coord
     * @param j y coord
     * @param icon the new icon
     */
    public static void changeIcon(int i, int j, Icon icon) {
        getButton("B" + i + j).setIcon(icon);
    }
}

