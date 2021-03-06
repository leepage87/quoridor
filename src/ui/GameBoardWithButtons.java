package src.ui;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;

import src.main.Board;
import src.main.PlayQuor;



@SuppressWarnings("serial")

/**
 * The GUI. Creates the game board - more specifically, creates the buttons for tiles and walls;
 * creates the tile icons; creates the menu bar.
 */
public class GameBoardWithButtons extends JFrame implements ActionListener{

    /* The icons for the tiles. Used across multiple classes. */
    public static Icon playerOne = new ImageIcon(GameBoardWithButtons.class.getResource("player1.png"));
    public static Icon playerTwo = new ImageIcon(GameBoardWithButtons.class.getResource("player2.png"));
    public static Icon playerThree = new ImageIcon(GameBoardWithButtons.class.getResource("player3.png"));
    public static Icon playerFour = new ImageIcon(GameBoardWithButtons.class.getResource("player4.png"));
    public static Icon legalMove = new ImageIcon(GameBoardWithButtons.class.getResource("legalMove.png"));
    public static Icon defaultIcon = new ImageIcon(GameBoardWithButtons.class.getResource("default.gif"));
    public static JPanel contentPane;
    public static JLabel whoseTurn;
    public static ArrayList<JLabel> pWalls = new ArrayList<JLabel>();
    public final static String MAIN_WINDOW_TITLE = "Quoridor";
    
    private final int SIZE = 630; /*width/height of game board*/
    private final int DOWNOFFSET = 20; /*distance to draw board from top of window*/
    private final int RIGHTOFFSET = 20; /*distance to draw board from left side of window*/
    private final int WALLLENGTH = SIZE * 2/21;
    private final int WALLWIDTH = SIZE/9 - WALLLENGTH;

    /**
     * Takes the back end board and number of players. Paints the game board appropriately.
     * @param b the code representation of the board
     * @param numPlay the number of players
     */
    public GameBoardWithButtons(Board b, int numPlay)  {
        super();
        JMenuBar menu = makeMenuBar();
        menu.setVisible(true);
        add(menu);
        setName(MAIN_WINDOW_TITLE);
        setTitle(MAIN_WINDOW_TITLE);
        setSize(SIZE + 2*RIGHTOFFSET, SIZE + 2*DOWNOFFSET+50);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null); /*uses absolute layout*/

        whoseTurn = new JLabel();
        whoseTurn.setBounds(30, 640, 350, 40);
        add(whoseTurn); /*text supplied by PlayQuor*/

        /*Displays number of walls left for each player. Text supplied by PlayQuor. */
        for (int i = 0; i < 4; i++){
            JLabel wallEntry = new JLabel();
            wallEntry.setBounds(280 + 100*i, 640, 100, 40);
            pWalls.add(wallEntry);

        }
        for (int i = 0; i < numPlay; i++)
            add(pWalls.get(i));

        /*Creates the 81 tiles. Adds them to map in BoardButton so they can be accessed by name.*/
        JButton[][] tokenSquares = new JButton[9][9];
        for (int across = 0; across < 9; across++)	{
            for (int down = 0; down < 9; down++)	{
                tokenSquares[across][down] = new BoardButton("B"+across + down);
                tokenSquares[across][down].setBounds(SIZE*across/9 + WALLWIDTH/2, SIZE*down/9+DOWNOFFSET + WALLWIDTH/2, 
                        WALLLENGTH,WALLLENGTH);
                add(tokenSquares[across][down]);
                tokenSquares[across][down].setName("B"+across + "" +down);
                BoardButton.map.put("B"+ across + down, (BoardButton)tokenSquares[across][down]);

                /*Sets icons in appropriate places for a new game. */
                if (b.grid[across*2][down*2] == 1)
                    tokenSquares[across][down].setIcon(playerOne);
                else if (b.grid[across*2][down*2] == 2)
                    tokenSquares[across][down].setIcon(playerTwo);
                else if (b.grid[across*2][down*2] == 3)
                    tokenSquares[across][down].setIcon(playerThree);
                else if (b.grid[across*2][down*2] == 4)
                    tokenSquares[across][down].setIcon(playerFour);
            }
        }

        /*Draws vertical walls. Adds to map in BoardWall. */
        JButton[][] vertwalls = new JButton[10][10];
        for (int across = 1; across < 9; across++)	{
            for (int down = 1; down < 10; down++)	{
                vertwalls[across][down] = new BoardWall("" + across + down + "V");
                vertwalls[across][down].setBounds(SIZE*across/9 - WALLWIDTH/2, SIZE*(down-1)/9 +DOWNOFFSET+WALLWIDTH/2, 
                        WALLWIDTH, WALLLENGTH);
                vertwalls[across][down].setVisible(true);
                add(vertwalls[across][down]);
                BoardWall.map.put("" + across + down + "V", (BoardWall)vertwalls[across][down]);
                vertwalls[across][down].setName("V" + across + down);
            }
        }

        /*Draws horizontal walls. Adds to map in BoardWall. */ 
        JButton[][] horzwalls = new BoardWall[10][10];
        for (int across = 1; across < 10; across++) {
            for (int down = 1; down < 9; down++) {
                horzwalls[across][down] = new BoardWall("" + across + down + "H");
                horzwalls[across][down].setRolloverEnabled(false);
                horzwalls[across][down].setBounds(SIZE*(across-1)/9 + WALLWIDTH/2, SIZE*down/9 +DOWNOFFSET - WALLWIDTH/2, WALLLENGTH, WALLWIDTH);
                horzwalls[across][down].setVisible(true);
                add(horzwalls[across][down]);
                BoardWall.map.put("" + across + down + "H", (BoardWall) horzwalls[across][down]);
                horzwalls[across][down].setName("H" + across + down);
            }
        }
        /*make it all visible*/
        setVisible(true);
    }

    /**
     * makes a menu bar and returns it
     * @return JMenuBar returns the menubar
     */
    public JMenuBar makeMenuBar() {
        JMenuBar menuBar;
        JMenu fileMenu;
        JMenuItem menuItem;

        /*Making the menu bar*/
        menuBar = new JMenuBar();

        /*Adding the "File" menu to menuBar*/
        fileMenu = new JMenu("File");

        /*ALT+F selects File menu*/
        fileMenu.setMnemonic(KeyEvent.VK_F);

        menuBar.add(fileMenu);
        menuBar.setName("MenuBar");

        /*add "New Game" feature to File menu*/
        menuItem = new JMenuItem("New Game",
                KeyEvent.VK_N);
        menuItem.setActionCommand("New");  
        menuItem.addActionListener(this);
        fileMenu.add(menuItem);

        /*add "Quit" feature to file menu, action command "Quit"*/
        menuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        menuItem.setActionCommand("Quit");

        /*ActionEvent x = new ActionEvent();*/
        menuItem.addActionListener(this);
        fileMenu.add(menuItem);

        /*make a help menu*/ 
        fileMenu = new JMenu("Help");
        fileMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(fileMenu);

        /*add how to play menu item, action command "Instructions"*/
        menuItem = new JMenuItem("How to Play", KeyEvent.VK_P);
        menuItem.setActionCommand("Instructions");
        menuItem.addActionListener(this);
        fileMenu.add(menuItem);

        /*add about menu item, action command "About"*/
        menuItem = new JMenuItem("About", KeyEvent.VK_A);
        menuItem.setActionCommand("About");
        menuItem.addActionListener(this);
        fileMenu.add(menuItem);

        /*add the menu bar to the frame*/
        setJMenuBar(menuBar);
        return menuBar;
    }
    /**
     * performs actions on menu item clicks
     */
    public void actionPerformed(ActionEvent e) {
        if ("Quit".equals(e.getActionCommand())) {
            System.exit(0);
        } else if ("About".equals(e.getActionCommand())) {
            JOptionPane.showMessageDialog(contentPane, "Programming by: \nLee Page, Jonathan \"the Beard\" Gould," +
                    "\nSarah Weller, and Timothy Simmons\n\nCIS 405 - Sofware Engineering - Fall 2012", "Quoridor", 	
                    JOptionPane.INFORMATION_MESSAGE);
        } else if ("Instructions".equals(e.getActionCommand())){
            String [] options = {"Open Rules", "Close"};
            JLabel customFontText = new JLabel();
            customFontText.setFont (new Font ( "Default", Font.BOLD, 16) );  
            customFontText.setText("<html><body>Click a square to move your player. If the move " +
                    "is legal the square will be highlighted. <br /><br />" +
                    "Click the black lines to place a wall. " +
                    "Walls are placed from left to right or top " +
                    "to bottom.<br /><br />" + 
                    "You may perform a \"double jump\" - that is, " + 
                    "if your pawn is adjacent horizontally or vertically <br />" +
                    "to another pawn, you may move to any squares to which " + 
                    "that pawn can move.<br /><br />" +
                    "To win, move your pawn to the opposite side of the board.<br /><br />" +
            "This game contains no ponies. Jonathan apologizes for the inconvenience, Lee does not.<br /></body></html>");
            int n = JOptionPane.showOptionDialog(contentPane,
                    customFontText,
                    "How to Play",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,     /*do not use a custom Icon*/
                    options,  /*the titles of buttons*/
                    options[0]); /*default button title*/
            if (n == JOptionPane.YES_OPTION){
                OpenBrowser.openURL("http://en.wikipedia.org/wiki/Quoridor#Rules_of_the_game");
            }

        }else if ("New".equals(e.getActionCommand())){
            String[] options = {"Yes","No"};
            int n = JOptionPane.showOptionDialog(this.contentPane, 
                    "Restart the game and lose all progress?","Are you sure?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n == 0)
                PlayQuor.setBreaker(2);

        }
    }
} 