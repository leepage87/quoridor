package src.ui;

/**
 * Tim Simmons
 * teamOrangeBeard
 */

public class PlayQuor{

    public static boolean clicked = false;
    public static String nextMove = "";
    public static String oldMove = "";
    public static int turn;


    public static void main(String[] args) throws InterruptedException{
        int numPlay = 4; // Number of Players
        Board b = new Board(numPlay);
        GameBoardWithButtons gui = new GameBoardWithButtons(b);
        // Create/assign AI to a number of Players
        boolean won = false;
        turn = 1;
        while(!won){
            boolean fairMove = false;
            while(!fairMove)
                fairMove = takeTurn(b, false);
            turn = (turn%numPlay) + 1;
            System.out.println("TURN: " + turn);
            won = b.haveWon();
        }
    }

    public static boolean takeTurn(Board b, boolean moveOnly) throws InterruptedException{
        /*
	  Get placeWall/movePiece from Player/AI, in int[] form
	      movePiece: gives int to get the char from
	      placeWall: gives int/int/int (row, column, direction)
         */
        while (!clicked){
            ;
        }
        Thread.sleep(0);
        if (nextMove.charAt(0) == 'B') {//its a player move
            boolean getOut = false;
            for (int col = 0; col < 17; col+=2)
            {
                for (int row = 0; row < 17; row +=2)
                {
                    if (b.grid[col][row] == turn)
                    {
                        oldMove = "" + col/2 + row/2;
                        int oldCol = (int)(oldMove.charAt(0) -'0');
                        int oldRow = (int)(oldMove.charAt(1)-'0');
                        int newCol = (int)(nextMove.charAt(1) - '0');
                        int newRow = (int)(nextMove.charAt(2) - '0');
                        System.out.println("Squiggle");
                        System.out.println(oldCol + " " + oldRow + " " + newCol + " " + newRow);
                        if(newCol == oldCol -1 && oldRow == newRow){
                            System.out.println("WE GO w");
                            if(!movePiece(b, 'W')){
                                clicked = false;
                                return false;
                            }
                            getOut = true;
                        }else if(newCol == oldCol +1 && oldRow == newRow){
                            System.out.println("WE GO E");
                            if(!movePiece(b, 'E')){
                                clicked = false;
                                return false;
                            }
                            getOut = true;
                        }else if(oldCol == newCol && newRow == oldRow -1){
                            System.out.println("WE GO N");
                            if(!movePiece(b, 'N')){
                                clicked = false;
                                return false;
                            }
                            getOut = true;
                        }else if(oldCol == newCol && newRow == oldRow +1){
                            System.out.println("WE GO S");
                            if(!movePiece(b, 'S')){
                                clicked = false;
                                return false;
                            }
                            getOut = true;
                        }else{//cheater
                            System.out.println("CHEATER");
                            clicked = false;
                            return false;
                        }
                    }
                    if(getOut)
                        break;
                }
                if(getOut)
                    break;
            }
        }else{//its a wall
            int gridCol = (int)(nextMove.charAt(0) - '0')-1;
            int gridRow = (int)(nextMove.charAt(1) - '0')-1;

            int[] theWall = {gridCol, gridRow, 0};
            boolean fairWall = false;
            
            if (nextMove.charAt(2) == 'H'){
            	fairWall = placeWall(b, theWall);
            }else{//its a vertical wall
                theWall[2] = 1;
                fairWall = placeWall(b, theWall);
            }
            if (!fairWall)
            {
                System.out.println("illegal wall placement");
                clicked = false;
                return false;
            }
                
        }
        System.out.println("no uh no ehhhh");
        for (int col = 0; col < 17; col ++) {
            for(int row = 0; row < 17; row++) {
                System.out.print(b.grid[row][col] + " ");
            }
            System.out.println();
        }

        clicked = false;
        return true;

    }

    // Parameters: the board, the player whose turn it is, the direction
    //    that the player chose to move
    // PostCondition: the player's piece is moved, if it was legal
    public static boolean movePiece(Board b, char direction) throws InterruptedException{
        System.out.println("Hey we in PQ.movepiece");
        if(b.canMovePiece(direction, turn)){
            System.out.println("pass 1st if");
            if(!b.pieceCollision(direction, turn)){
                b.movePiece(direction, turn);
            }else{
                doubleMove(b, direction);
            }
            return true;
        }
        return false;

    }

    // Parameters: the board, the player whose turn it is, and an
    //    int[] containing the center location of a new wall and
    //    an int determining if it is horizontal or vertical
    // PostCondition: the wall is placed, if it was legal
    public static boolean placeWall(Board b, int[] theWall){

        String wallName;
        for(int i = 0; i < theWall.length; i++)

            if (b.canPlaceWall(theWall))
            {
                System.out.println("here is good ");
                b.placeWall(theWall);

                if (theWall[2] == 0)
                    wallName = "" + (theWall[0]+1) + (theWall[1]+1) + "H";
                else//its a 1, meaning vertical
                    wallName = "" + (theWall[0]+1) + (theWall[1]+1) + "V";    


                BoardWall.map.get(wallName).setWall();
                BoardWall.map.get(wallName).nextWall().setWall();
                return true;

            }
            else
            {
                System.out.println("WTF");
                return false;
            }
        System.out.println("here is good too");
        return true;
    }

    // Parameters: the board, the player whose turn it is, the direction
    //    that the player chose to move (which is onto another player)
    // PostCondition: the player's piece is moved and he moves again
    public static void doubleMove(Board b, char direction) throws InterruptedException{

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
        b.movePiece(direction, turn);
        while(b.grid[spot[0]][spot[1]] ==  turn){

            takeTurn(b, true);
        }
        b.grid[spot[0]][spot[1]] = otherPlayer;
        //"B"+ across + down
        BoardButton.map.get("B"+spot[0]/2+spot[1]/2).setIcon(Board.map.get(otherPlayer));
    }


}