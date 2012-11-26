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
    private static int[] pieceHolder = new int[3]; // Used to correctly update GUI during a "double move"
    private static int breaker = 0; // primary loop in main exits when this is 1 (somebody won)
    // or 2 (new game started with File -> New Game)
    private static int[] isAI = new int[4];
    private static boolean networkGame = false;
    private static int[][] legalMovesArray = new int[9][9]; // used for tracking which squares get the legal move icon
    /*
     * The central game driver. Gets number of players, creates a new back end Board
     * and GUI, and loops until player wins or starts a new game.
     * */

    public static void main(String[] args) throws InterruptedException, IOException{
        GameBoardWithButtons gui = null; // instantiates GUI
        while (true){
            int numPlay = getNumPlay();
            if (numPlay == 0)
                System.exit(0);
            //get network information, initiate network
            NetworkClient network = getNetworkInfo(numPlay);
            if(!networkGame) 
                for (int i = 1; i <= numPlay; i++){
                    isAI[i-1] = getHumanOrAI(i);
                    System.out.println(isAI[i]);
                }
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

                if (isAI[turn-1] == 0) {
                    setLegalMoves(b.playerPlace(turn), b);
                    setLMIcons();
                }
                // initialize GUI button indicating turn
                GameBoardWithButtons.whoseTurn.setText("It is player " + turn + "'s turn.");
                // initialize GUI buttons indicating how many walls each player has left
                for (int i = 0; i < numPlay; i++)
                    GameBoardWithButtons.pWalls.get(i).setText("P" + (i+1) + ": " + b.playerWalls[i] + " walls");
                if(networkGame)
                    networkTurn(network, b);
                else {
                    if(isAI[turn-1] == 1)
                        b = makeAIMove(b);
                    else {
                        boolean fairMove = false;
                        while(!fairMove)
                            fairMove = takeTurn(b, false);
                        // Indicates player has elected to start a new game; exits the loop
                        if (breaker == 2)
                            break;
                    }
                    // tests win conditions and updates exit variable accordingly
                    //if (b.haveWon())
                    //  breaker = 1;
                    //System.out.println(b);
                }
                if (b.haveWon())
                    breaker = 1;
                // if somebody won, say so
                if (breaker == 1 && !networkGame)
                    JOptionPane.showMessageDialog(GameBoardWithButtons.contentPane, "Player " + turn + " Won!");
                if (breaker == 1 && networkGame){
                    JOptionPane.showMessageDialog(GameBoardWithButtons.contentPane, "Player " + turn + " Won!");
                    NetworkClient.broadcast("WINNER " + (turn-1));
                    network.kill();
                    networkGame = false;
                }
            }
        }
    }
    
    public static void setClicked(boolean tf) {
        clicked = tf;
    }
    
    public static void setNextMove (String name) {
        nextMove = name;
    }
    
    public static void setBreaker(int i) {
        breaker = i;
    }

    private static Board makeAIMove(Board b) {
        AI a = new AI(b);
        int[] startPlace = b.playerPlace(turn);
        Board tempB = a.aiMove(turn, 1);
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
            BoardButton.map.get("B"+endPlace[0]/2+endPlace[1]/2).setIcon(Board.map.get(turn));
            BoardButton.map.get("B"+startPlace[0]/2+startPlace[1]/2).setIcon(Board.map.get(0));
            b = tempB;
        }
        return b;
    }


    private static void networkTurn(NetworkClient network, Board b) throws IOException {
        String networkMove = NetworkClient.getMove(turn);
        System.out.println("Turn: " + turn + " Move: " +networkMove);
        setLegalMoves(b.playerPlace(turn), b);
        int move[] = new int[3];
        if(networkMove.charAt(5)== 'M'){//player movement
            int oldX = networkMove.charAt(11) - '0';
            int oldY = networkMove.charAt(8) - '0';
            int newX= networkMove.charAt(18) - '0';
            int newY = networkMove.charAt(15) - '0';
            if (legalMovesArray[newX][newY] == 1){
                //the move is legal

                //broadcast the move to all players
                NetworkClient.broadcast("MOVED " + (turn-1) + networkMove.substring(4));

                //make the move
                BoardButton.map.get("B"+newX+newY).setIcon(Board.map.get(turn));
                System.out.println("oldx " + oldX + "oldY " + oldY);
                BoardButton.map.get("B"+oldX+oldY).setIcon(Board.map.get(0));

                move [0] = (networkMove.charAt(18)-'0')*2;
                move [1] = (networkMove.charAt(15)-'0')*2;
                b.playerPlace(turn);
                System.out.println("Move[] " + move[0] + move[1]);
                b.quickMove(move, turn);
            }else{//the move is not legal, kick player
                NetworkClient.removePlayer(turn);
                int[] location = b.playerPlace(turn);
                b.grid[location[0]] [location[1]] = 0;
                System.out.println("KICKING PLAYER " + turn + " because of: " +networkMove);
                //TODO: FIX TURN TO NOT CALL KICKED PLAYERS
            }
            System.err.println("Player has been moved, printing board");
            System.err.println(b.toString());

        }else{//wall movement
            if(networkMove.charAt(8) == networkMove.charAt(15)){//horizontal wall
                if((networkMove.charAt(11)-'0') != 0){
                    System.out.println("charat15 is " + networkMove.charAt(15));
                    move[0] = (networkMove.charAt(11)-'0');
                    System.out.println("i move0 is:" + move[0]);
                }else{
                    move[0] = (networkMove.charAt(11)-'0');
                    System.out.println("e move0 is:" + move[0]);
                }
                move[1] = (networkMove.charAt(8)-'0') -1;
                move[2] = 0;
                System.out.println("Printing horz wall coords: "+ move[0] + " " + move[1]);
                placeWallPQ(b, move);
                NetworkClient.broadcast("MOVED " + (turn-1) + networkMove.substring(4));
            }else{//vertical wall
                move[0] = (networkMove.charAt(11)-'0') - 1;
                move[1] = (networkMove.charAt(8)-'0');//just edited removed +1
                move[2] = 1;
                System.out.println("Printing vert wall coords: "+ move[0] + " " + move[1]);
                placeWallPQ(b, move);
                NetworkClient.broadcast("MOVED " + (turn-1) + networkMove.substring(4));
            }
            System.err.println(b.toString());
        }
    }



    private static void setLMIcons() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (legalMovesArray[i][j] == 1) 
                    BoardButton.getButton("B" + i + j).setIcon(GameBoardWithButtons.legalMove);
    }

    private static void resetIcons(Board b) {
        for (int i = 0; i < 17; i+=2) 
            for (int j = 0; j < 17; j+=2) 
                if (b.grid[i][j] == 0) 
                    BoardButton.getButton("B" + i/2 + j/2).setIcon(src.ui.GameBoardWithButtons.defaultIcon);
    }

    private static int getHumanOrAI(int i) {
        String[] HumanOrAi = {"Human","AI"};
        return JOptionPane.showOptionDialog(GameBoardWithButtons.contentPane,
                "Player " + i + ": Human or AI?", 
                "YOU MUST CHOOSE",JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,HumanOrAi,HumanOrAi[0]);
    }

    private static NetworkClient getNetworkInfo(int numPlay) throws UnknownHostException, IOException {
        String[] options = new String[3];
        options [0] = "Local Game";
        options [1] = "Network Game";
        options [2] = "Quit";
        int n = JOptionPane.showOptionDialog(GameBoardWithButtons.contentPane, 
                "Play local or remote opponent?","Network Game?",
                JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
        NetworkClient network = new NetworkClient();
        String address = null;
        while (address != null) {
            address = JOptionPane.showInputDialog("Input addresses for " + numPlay + " move servers separated by spaces:", "ex. hostname1:port hostname2:port");
            if (address != null){
                Scanner addressScanner = new Scanner(address);
                String player1Address = addressScanner.next(); 
                String player2Address = addressScanner.next(); 
                if (n == 1 && numPlay == 2)
                {
                    network = new NetworkClient(player1Address, player2Address);
                    networkGame = true;
                }
                else if (n == 1 && numPlay == 4 ){
                    String player3Address = addressScanner.next(); 
                    String player4Address = addressScanner.next(); 
                    network = new NetworkClient(player1Address, player2Address, player3Address, player4Address);
                    networkGame = true;
                }
            }
            else if (n == 2)
                System.exit(0);
        }
        return network;
    }


    private static int getNumPlay() {
        String[] options = {"Two player game", "Four player game","Quit"};
        int n = JOptionPane.showOptionDialog(GameBoardWithButtons.contentPane, 
                "How many players want to play today?","Welcome to Quoridor!",
                JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
        if (n == 0)
            return 2;
        else if (n == 1)
            return 4;
        else if (n == 2)
            return 0;
        return 0;
    }


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

    public static boolean takeTurn(Board b, boolean extraMove) throws InterruptedException{
        /*

	  Get placeWall/movePiece from Player/AI, in int[] form
	      movePiece: gives int to get the char from
	      placeWall: gives int/int/int (row, column, direction)

	      Waits for mouse click (as notified by GUI); receives move data from GUI; 
	      tests some aspects of move's legality. 
         */

        /* Waits for click. If while in this loop, player decides to start a new game,
         * breaker is set and control returns to main. */

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
                b.grid[2*(int)(nextMove.charAt(1) -
                '0')][2*(int)(nextMove.charAt(2) - '0')] = turn;
                BoardButton.map.get("B" + playerPlace[0]/2 +
                        playerPlace[1]/2).setIcon(GameBoardWithButtons.defaultIcon);
                BoardButton.map.get("B" + nextMove.charAt(1) +
                        nextMove.charAt(2)).setIcon(Board.map.get(turn));
            }
            else {
                clicked = false;
                return false;
            }
        }else if (!extraMove){//it's a wall (rules out double jump with boolean extraMove)
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
        //System.out.println(b);
        clicked = false; // resets clicked so that method not called indefinitely
        return true; // legal move made; tells main this.

    }


    /* Parameters: the board, the player whose turn it is, the direction
     * that the player chose to move
     * PostCondition: the player's piece is moved, if it was legal */
    public static boolean movePiecePQ(Board b, char direction, boolean extraMove) throws InterruptedException{
        if(b.canMovePiece(direction, turn)){ // calls the back end method to test move legality in re: walls
            if(!b.pieceCollision(direction, turn)){ // calls back end method to test for piece collision
                b.movePieceBoard(direction, turn); // if it's legal and doesn't hit another pawn, make the move!
                // handled by back end, which informs GUI 
            }else{ // if pawn collision after otherwise legal move, call doubleMove to sort out particulars
                return doubleMove(b, direction, extraMove);
            }
            return true; // return true for legal move
        }
        else {
            //JOptionPane.showMessageDialog(GameBoardWithButtons.contentPane, "Illegal Move");
            return false; //return false for illegal move
        }
    }

    /* Parameters: the board, the player whose turn it is, and an
     * int[] containing the center location of a new wall and
     * an int determining if it is horizontal or vertical	
     * PostCondition: the wall is placed, if it was legal*/
    public static boolean placeWallPQ(Board b, int[] theWall){
        String wallName;

        /* if the player has walls left to play, and the back end says a wall can go here ... */

        if (b.canPlaceWall(theWall, turn)) 
        {
            b.placeWallBoard(theWall, turn); // place it with the back end (NOT GUI YET)

            /* Sets the wall name as found in the map in BoardWall. Sets that wall and the wall next to it. */
            if (theWall[2] == 0)
                wallName = "" + (theWall[0]+1) + (theWall[1]+1) + "H";
            else //its a 1, meaning vertical
                wallName = "" + (theWall[0]+1) + (theWall[1]+1) + "V";    
            BoardWall.map.get(wallName).setWall();
            BoardWall.map.get(wallName).nextWall().setWall();
            return true; // wall placed successfully
        }
        else // else if there are no more walls to play or if the back end has a problem with it ..
            return false; // return false for not a legal move

    }

    // Parameters: the board, the player whose turn it is, the direction
    //    that the player chose to move (which is onto another player)
    // PostCondition: the player's piece is moved and he moves again
    public static boolean doubleMove(Board b, char direction, boolean extraMove) throws InterruptedException{
        int[] startSpot = b.playerPlace(turn);
        int col = startSpot[0];
        int row = startSpot[1];
        int otherPlayer;
        if(direction == 'N')
            otherPlayer = b.grid[col][row-2];
        else if(direction == 'S')
            otherPlayer = b.grid[col][row+2];
        else if(direction == 'E')
            otherPlayer = b.grid[col+2][row];
        else otherPlayer = b.grid[col-2][row];
        int[] spot = b.playerPlace(otherPlayer);
        b.movePieceBoard(direction, turn);
        if(extraMove){
            BoardButton.map.get("B"+pieceHolder[0]/2+pieceHolder[1]/2).setIcon(Board.map.get(pieceHolder[2]));
            b.grid[pieceHolder[0]][pieceHolder[1]] = pieceHolder[2];
        }
        pieceHolder[0] = spot[0];
        pieceHolder[1] = spot[1];
        pieceHolder[2] = otherPlayer;
        while(b.grid[spot[0]][spot[1]] ==  turn)
            takeTurn(b, true);
        b.grid[spot[0]][spot[1]] = otherPlayer;
        BoardButton.map.get("B"+spot[0]/2+spot[1]/2).setIcon(Board.map.get(otherPlayer));
        if(b.grid[col][row] == turn)
            return false;
        return true;
    }

    private static void resetLegalMoves(){
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                legalMovesArray[i][j] = 0;
    }

}