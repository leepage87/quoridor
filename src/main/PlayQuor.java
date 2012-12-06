/**
 * Tim, Lee, Sarah, Jonathan
 * teamOrangeBeard
 * In main method, contains the loop that forms the beginning and end of each game experience.
 * Also has methods that check for legality of play, execute legal plays, ...
 */

package src.main;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import src.network.NetworkClient;
import src.ui.BoardButton;
import src.ui.BoardWall;
import src.ui.GameBoardWithButtons;

public class PlayQuor{

    private static boolean clicked = false; // When set true by the GUI, executes method takeTurn
    private static String nextMove = ""; // Receives data from GUI on next human player move
    private static int turn; // tracks which player's turn it is
    // private static int[] pieceHolder = new int[3]; // Used to correctly update GUI during a "double move" No longer used
    private static int breaker = 0; // primary loop in main exits when this is 1 (somebody won)
    // or 2 (new game started with File -> New Game)
    private static int[] isAI = new int[4];
    private static boolean networkGame = false;
    private static int[][] legalMovesArray = new int[9][9]; // used for tracking which squares get the legal move icon
    private static int[] networkPlayers;
    private static int playersLeft;
    private static boolean lastNetworkMoveLegal;

    /**
     * calls the play() method repeatedly
     * @param args
     * @throws IOException 
     * @throws UnknownHostException 
     * @throws InterruptedException
     */
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException{
        GameBoardWithButtons gui = null; // instantiates GUI
        while (true){
            gui = play(gui);
        }
    }
    /**
     * The central game driver. Gets number of players, creates a new back end Board
     * and GUI, and loops until player wins or starts a new game.
     * @param gui
     * @throws UnknownHostException
     * @throws IOException
     * @throws InterruptedException
     */
    private static GameBoardWithButtons play(GameBoardWithButtons gui) throws UnknownHostException, IOException, InterruptedException{
        NetworkClient network = null;
        int numPlay = getNumPlay();
        playersLeft = numPlay;
        if (numPlay == 0)
            System.exit(0);
        //get network information, initiate network
        String[] options = new String[3];
        options [0] = "Local Game";
        options [1] = "Network Game";
        options [2] = "Quit";
        int n = JOptionPane.showOptionDialog(GameBoardWithButtons.contentPane, 
                "Play local or remote opponent?","Network Game?",
                JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
        if (n == 1){
            network = getNetworkInfo(numPlay);
            networkPlayers = new int[numPlay];
            //poopulate the networkPlayers array with 1s representing players
            for (int i = 0; i < numPlay; i ++){
                networkPlayers[i] = 1;
            }
        }else if(n == 2){
            System.exit(0);
        }
        if(!networkGame) 
            for (int i = 1; i <= numPlay; i++)
                isAI[i-1] = getHumanOrAI(i);
        // create a new back end board with desired number of players
        Board b = new Board(numPlay);
        // Give each player the appropriate number of walls
        for(int i = 0; i < numPlay; i++)
            b.playerWalls[i] = 20/numPlay;
        // if GUI is not null, a game has just ended and its data must be thrown out.
        if (gui != null)
            gui.dispose();

        // Create brand new gui with board and number of players
        gui = new GameBoardWithButtons(b, numPlay);
        // set/reset control variables breaker, won, and turn
        breaker = 0;
        turn = 0;
        // Loop containing each game. If breaker becomes 1 or 2, the game has ended.
        while(breaker == 0){
            resetIcons(b);
            resetLegalMoves();
            turn = (turn%numPlay) + 1; // update turn          
            //if the turn is for a human
            if (isAI[turn-1] == 0 && !networkGame) {
                setLegalMoves(b.playerPlace(turn), b);
                setLMIcons();
            }
            // initialize GUI button indicating turn
            if(networkGame)
                GameBoardWithButtons.whoseTurn.setText("Turn: Player " + turn + " ("+NetworkClient.getName(turn)+").");
            else
                GameBoardWithButtons.whoseTurn.setText("It is player " + turn + "'s turn.");
            // initialize GUI buttons indicating how many walls each player has left
            for (int i = 0; i < numPlay; i++) {
                if (networkGame && networkPlayers[i] == 0)
                    GameBoardWithButtons.pWalls.get(i).setText("P" + (i+1) + ": KICKED");
                else
                    GameBoardWithButtons.pWalls.get(i).setText("P" + (i+1) + ": " + b.playerWalls[i] + " walls");
            }
            if(networkGame){
                if (networkPlayers[turn-1] != 0)
                    networkTurn(network, b);
            }else {
                if(isAI[turn-1] > 0)
                    b = makeAIMove(b);
                else {
                    boolean fairMove = false;
                    while(!fairMove)
                        fairMove = takeTurn(b);
                    // Indicates player has elected to start a new game; exits the loop
                    if (breaker == 2)
                        break;
                }
            }
            if (b.haveWon())
                breaker = 1;
            // if somebody won, say so

            if (playersLeft == 1 && lastNetworkMoveLegal == true)
                breaker = 1;

            if (breaker == 1 && !networkGame)
                JOptionPane.showMessageDialog(GameBoardWithButtons.contentPane, "Player " + turn + " Won!");

            if (breaker == 1 && networkGame){
                JOptionPane.showMessageDialog(GameBoardWithButtons.contentPane, "Player " + turn + " won!");
                NetworkClient.broadcast("WINNER " + (turn-1));
                network.kill();
                networkGame = false;
            }
            if(networkGame && playersLeft == 0){
                NetworkClient.broadcast("DRAW");
                JOptionPane.showMessageDialog(GameBoardWithButtons.contentPane, "DRAW!");
                network.kill();
                networkGame = false;
                breaker = 1;
            }
        }
        return gui;
    }

    /**
     * Sets the "clicked" boolean to true or false when or
     * after a player has made a move
     * @param tf
     */
    public static void setClicked(boolean tf) {
        clicked = tf;
    }

    /**
     * Gets the name of a button from GameBoardWithButtons when
     * that button is clicked
     * @param name
     */
    public static void setNextMove (String name) {
        nextMove = name;
    }

    /**
     * Sets the value of breaker to parameter i
     * @param i
     */
    public static void setBreaker(int i) {
        breaker = i;
    }

    /**
     * @param the board
     * @return the board after the AI moves
     */
    private static Board makeAIMove(Board b) {
        AI a = new AI(b);
        int[] startPlace = b.playerPlace(turn);
        Board tempB = a.aiMove(turn, isAI[turn-1]-1);
        int[] endPlace = tempB.playerPlace(turn);
        if(startPlace[0]==endPlace[0] && startPlace[1]==endPlace[1]){
            boolean getOut = false;
            for(int col = 0; col < 17; col++){
                for(int row = 0; row < 17; row++){      
                    if(b.grid[col][row] != tempB.grid[col][row]){
                        int[] aiWall = new int[3];
                        aiWall[0] = col/2;
                        aiWall[1] = row/2;
                        if(tempB.grid[col][row+1]==5)
                            aiWall[2] = 1;
                        placeWallPQ(b, aiWall);
                        getOut = true;
                    }
                    if(getOut)
                        break;
                }
                if(getOut)
                    break;
            }
        }else{ // Move, not Wall
            BoardButton.changeIcons(endPlace, startPlace, turn);
            b = tempB;
        }
        return b;
    }


    /**
     * Waits for mouse click (as notified by GUI); receives move data from GUI; 
     * tests some aspects of move's legality. 
     * @param b
     * @return
     * @throws InterruptedException
     */
    public static boolean takeTurn(Board b) throws InterruptedException{
        while (!clicked){
            if (breaker == 2)
                return true;
        }
        Thread.sleep(0);
        /* nextMove is set by GUI upon click. First character determines whether a board or wall is being moved/placed. */
        if (nextMove.charAt(0) == 'B') { //its a player move
            int[] playerPlace = b.playerPlace(turn);
            setLegalMoves(playerPlace, b);
            if (legalMovesArray[(int)(nextMove.charAt(1)-'0')
                                ][(int)(nextMove.charAt(2)-'0')] == 1)
            {
                b.grid[playerPlace[0]][playerPlace[1]] = 0;
                b.grid[2*(int)(nextMove.charAt(1) -'0')][2*(int)(nextMove.charAt(2) - '0')] = turn;
                int[] newPlace = {(nextMove.charAt(1)-'0')*2, (nextMove.charAt(2)-'0')*2};
                BoardButton.changeIcons(newPlace,playerPlace,turn);

            }
            else {
                clicked = false;
                return false;
            }
        }else {
            int gridCol = (int)(nextMove.charAt(0) - '0')-1;
            int gridRow = (int)(nextMove.charAt(1) - '0')-1;

            /* Take location of wall, add 0 for horizontal and 1 for vertical.
             * Send to placeWallPQ */
            int[] theWall = {gridCol, gridRow, 0};
            boolean fairWall = false;
            if (nextMove.charAt(2) == 'H'){
                fairWall = placeWallPQ(b, theWall);
            }else{
                theWall[2] = 1;
                fairWall = placeWallPQ(b, theWall);
            }

            // Inform user if wall placement is illegal. Return control to main,
            // which restarts the takeTurn method
            if (!fairWall)
            {
                JOptionPane.showMessageDialog(GameBoardWithButtons.contentPane, "Illegal Wall");
                clicked = false; // resets clicked so that method not called indefinitely
                return false; // returns false for move not made yet
            }
        }
        clicked = false; // resets clicked so that method not called indefinitely
        return true; // legal move made; tells main this.

    }
    /**
     * Requests a move from a moveServer located on the network
     * @param network
     * @param b
     * @throws IOException
     */
    private static void networkTurn(NetworkClient network, Board b) throws IOException {
        lastNetworkMoveLegal = true;
        String networkMove = NetworkClient.getMove(turn);
        setLegalMoves(b.playerPlace(turn), b);
        int move[] = new int[3];
        if(networkMove.charAt(5)== 'M'){//player movement
            int[] oldPlace = {(networkMove.charAt(11) - '0')*2, (networkMove.charAt(8)  - '0')*2};
            int[] newPlace = {(networkMove.charAt(18) - '0')*2, (networkMove.charAt(15) - '0')*2};
            if (legalMovesArray[newPlace[0]/2][newPlace[1]/2] == 1){
                //the move is legal
                //broadcast the move to all players
                NetworkClient.broadcast("MOVED " + (turn-1) + networkMove.substring(4));
                //make the move
                BoardButton.changeIcons(newPlace,oldPlace, turn);
                move [0] = (networkMove.charAt(18)-'0')*2;
                move [1] = (networkMove.charAt(15)-'0')*2;
                b.playerPlace(turn);
                b.move(move, turn);
            }else{//the move is not legal, kick player
                //0 represents the player no longer in the game
                removeNetworkPlayer(turn-1);
                playersLeft--;
                lastNetworkMoveLegal = false;
                NetworkClient.removePlayer(turn);
                int[] location = b.playerPlace(turn);
                b.grid[location[0]] [location[1]] = 0;
            }
        }else{//wall movement
            if(networkMove.charAt(8) == networkMove.charAt(15)){//horizontal wall
                if((networkMove.charAt(11)-'0') != 0)
                    move[0] = (networkMove.charAt(11)-'0');
                move[1] = (networkMove.charAt(8)-'0') -1;
                move[2] = 0;
                if (!placeWallPQ(b, move)){
                    //0 represents the player is no longer in the game
                    removeNetworkPlayer(turn-1);
                    playersLeft--;
                    lastNetworkMoveLegal = false;
                }
                NetworkClient.broadcast("MOVED " + (turn-1) + networkMove.substring(4));
            }else{//vertical wall
                move[0] = (networkMove.charAt(11)-'0') - 1;
                move[1] = (networkMove.charAt(8)-'0');//just edited removed +1
                move[2] = 1;
                if (!placeWallPQ(b, move)){
                    //0 represents the player is no longer in the game
                    removeNetworkPlayer(turn-1);
                    playersLeft--;
                    lastNetworkMoveLegal = false;
                }
                NetworkClient.broadcast("MOVED " + (turn-1) + networkMove.substring(4));
            }
        }
    }

    public static void removeNetworkPlayer(int player){
        networkPlayers[player] = 0;
    }
    private static NetworkClient getNetworkInfo(int numPlay) throws UnknownHostException, IOException {

        NetworkClient network = new NetworkClient();
        String moveServerAddress = JOptionPane.showInputDialog("Input addresses for " + numPlay
                + " move servers separated by spaces:",
                "ex. hostname1:port hostname2:port");

        String[] options = new String[2];
        options [0] = "Yes";
        options [1] = "No";
        String numObservers = "0";
        String observerAddress = null;
        int n = JOptionPane.showOptionDialog(GameBoardWithButtons.contentPane, 
                "Allow Observers?","Observers?",
                JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
        if (n == 0){
            numObservers = JOptionPane.showInputDialog("How many observers?",
                    "Enter an integer less than 10.");
            observerAddress = JOptionPane.showInputDialog("Input addresses for " + numObservers
                    + " observers:",
                    "ex. hostname1:port hostname2:port");

        }
        network = new NetworkClient(numPlay, moveServerAddress, Integer.parseInt(numObservers), observerAddress);
        networkGame = true;
        return network;
    }

    private static void setLMIcons() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (legalMovesArray[i][j] == 1) 
                    BoardButton.changeIcon(i,j,GameBoardWithButtons.legalMove);
    }

    private static void resetIcons(Board b) {
        for (int i = 0; i < 17; i+=2) 
            for (int j = 0; j < 17; j+=2) 
                if (b.grid[i][j] == 0)
                    BoardButton.changeIcon(i/2,j/2,GameBoardWithButtons.defaultIcon);
    }
    /**
     * Generates a board placing a 1 in every possible legal movement box
     * @param playerPlace
     * @param b
     */
    private static void setLegalMoves(int[] playerPlace, Board b) {
        int currentColumn = playerPlace[0];
        int currentRow = playerPlace[1];
        //west
        if ((currentColumn != 0) && (b.grid[currentColumn-1][currentRow] !=5)) 
        {
            if (b.grid[currentColumn - 2][currentRow] == 0) {
                legalMovesArray[(currentColumn-2)/2][currentRow/2] = 1;
            }
            else if (legalMovesArray[(currentColumn-2)/2][currentRow/2] != 2){
                legalMovesArray[currentColumn/2][currentRow/2] = 2;
                int[] temp = {(currentColumn-2),currentRow};
                setLegalMoves(temp, b);
            }
        }
        //east
        if ((currentColumn != 16) && (b.grid[currentColumn+1][currentRow] !=5)) 
        {
            if (b.grid[currentColumn + 2][currentRow] == 0) {
                legalMovesArray[(currentColumn+2)/2][currentRow/2] = 1;
            }
            else if (legalMovesArray[(currentColumn+2)/2][currentRow/2] != 2){
                legalMovesArray[currentColumn/2][currentRow/2] = 2;
                int[] temp = {(currentColumn+2),currentRow};
                setLegalMoves(temp, b);
            }
        }
        //north
        if ((currentRow != 0) && (b.grid[currentColumn][currentRow-1] !=5)) 
        {
            if (b.grid[currentColumn][currentRow-2] == 0) {
                legalMovesArray[(currentColumn)/2][(currentRow-2)/2] = 1;
            }
            else if (legalMovesArray[(currentColumn)/2][(currentRow-2)/2] != 2){
                legalMovesArray[currentColumn/2][currentRow/2] = 2;
                int[] temp = {(currentColumn),currentRow-2};
                setLegalMoves(temp, b);
            }
        }
        //south
        if ((currentRow != 16) && (b.grid[currentColumn][currentRow+1] !=5)) 
        {
            if (b.grid[currentColumn][currentRow+2] == 0) {
                legalMovesArray[(currentColumn)/2][(currentRow+2)/2] = 1;
            }
            else if (legalMovesArray[(currentColumn)/2][(currentRow+2)/2] != 2){
                legalMovesArray[currentColumn/2][currentRow/2] = 2;
                int[] temp = {(currentColumn),currentRow+2};
                setLegalMoves(temp, b);
            }
        }
    }

    private static void resetLegalMoves(){
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                legalMovesArray[i][j] = 0;
    }
    /**
     * Prompts user for the number of players
     * @return the number of players
     */
    private static int getNumPlay() {
        String[] options = {"Two player game", "Four player game","Quit"};
        int n = JOptionPane.showOptionDialog(GameBoardWithButtons.contentPane, 
                "How many players want to play today?","Welcome to Quoridor!",
                JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
        if(n == 0)
            return 2;
        else if(n == 1)
            return 4;
        return 0;
    }
    /**
     * Prompts the user to set each player as human or AI
     * @param one of the players
     * @return an int showing if the player is human controlled, an Easy AI, or a Hard AI
     */
    private static int getHumanOrAI(int i) {
        String[] HumanOrAi = {"Human","Easy AI","Hard AI"};
        return JOptionPane.showOptionDialog(GameBoardWithButtons.contentPane,
                "Player " + i + ": Human or AI?", 
                "YOU MUST CHOOSE",JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,HumanOrAi,HumanOrAi[0]);
    }
    /**
     * Checks if a wall placement is legal and places said wall
     * @param the board
     * @param an int[] representing the wall
     * @postcondition the wall is placed if it was legal
     * @return if the move was legal
     */
    public static boolean placeWallPQ(Board b, int[] theWall){
        String wallName;
        // If the player can place the wall, then place the wall
        if(b.canPlaceWall(theWall, turn)){
            b.placeWallBoard(theWall, turn);
            // Place the wall in the GUI
            if(theWall[2] == 0)
                wallName = "" + (theWall[0]+1) + (theWall[1]+1) + "H";
            else
                wallName = "" + (theWall[0]+1) + (theWall[1]+1) + "V";    
            BoardWall.map.get(wallName).setWall(turn);
            BoardWall.map.get(wallName).nextWall().setWall(turn);
            return true; // wall placed successfully
        }else // If wall placement is illegal, return false
            return false;
    }

}