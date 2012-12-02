/**
 * Tim Simmons
 * teamOrangeBeard
 */

package src.main;
import java.util.ArrayList;


public class AI{

    private Board AIboard;
    private int truePlayer;
    private boolean panic = false;
    private Board lastMove;

    public AI(Board b){
        AIboard = b;
        lastMove = b;
    }
    /**
     * @param player
     * @param rounds
     * @return Board after moving
     */
    public Board aiMove(int player, int rounds){
        truePlayer = player;
        int[] answer = new int[3];
        answer[0] = -201;
        answer[1] = 201;
        answer[2] = -201;
        ArrayList<Board> moves = findMoves(player, AIboard);
        ArrayList<Integer> goodMoves = new ArrayList<Integer>();
        int enemy = findEnemy(player, AIboard);
        if(enemy == -1)
            return moves.get(0);
        if(rounds == 0 || panic == true){
            goodMoves.add(0);
            int value = boardValue(player, enemy, moves.get(0));
            for(int i = 1; i < moves.size(); i++){
                int nextValue = boardValue(player, enemy, moves.get(i));
                if(nextValue > value){
                    value = nextValue;
                    goodMoves = new ArrayList<Integer>();
                    goodMoves.add(i);
                }else if(nextValue == value)
                    goodMoves.add(i);
            }
            int whichBoard = goodMoves.get((int) (Math.random() * goodMoves.size()));
            if(panic == true && wallPlaced(lastMove, moves.get(whichBoard)))
                panic = false;
            return moves.get(whichBoard);
        }
        int[] next = search(answer, nextPlayer(AIboard, player), moves.get(0), rounds-1);
        answer[0] = next[0];
        answer[2] = next[0];
        goodMoves.add(0);
        for(int i = 1; i < moves.size(); i++){
            next = search(answer, nextPlayer(AIboard, player), moves.get(i), rounds-1);
            if(next[0] > answer[0]){
                goodMoves = new ArrayList<Integer>();
                answer[0] = next[0];
                answer[2] = next[0];
                goodMoves.add(i);
            }else if(next[0] == answer[0]){
                goodMoves.add(i);
            }
        }
        if(splitMove(moves.get(0)))
            return aiMove(player, 0);
        lastMove = AIboard;
        if(goodMoves.size() > 8)
            return aiMove(player, 0);
        int whichBoard = goodMoves.get((int) (Math.random() * goodMoves.size()));			
        return moves.get(whichBoard);
    }
    /**
     * answer: int [] array of the current value + the alpha + the beta, 
     * the player, a board, and the number of rounds to look ahead
     * @param answer
     * @param player
     * @param b the board
     * @param numRounds
     * @return the same array, or the same array with either the alpha or beta changed
     */
    private int[] search(int[] answer, int player, Board b, int numRounds){
        ArrayList<Board> allMoves = findMoves(player, b);
        if(numRounds < 1)
            return baseCase(answer, player, allMoves);
        int[] nextAnswer = {answer[0], answer[1], answer[2]};
        if(player == truePlayer){
            int best = -201;
            for(int i = 0; i < allMoves.size(); i++){
                int[] value = search(nextAnswer, nextPlayer(b, player), allMoves.get(i), numRounds-1);
                if(value[0] > nextAnswer[2])
                    nextAnswer[2] = value[0];
                if(value[0] > nextAnswer[1]){
                    return nextAnswer; 
                }
            }
            int[] newAnswer = {best, answer[1], answer[2]};
            return newAnswer;
        }
        int worst = 201;
        for(int i = 0; i < allMoves.size(); i++){
            int[] value = search(nextAnswer, nextPlayer(b, player), allMoves.get(i), numRounds-1);
            if(value[0] < nextAnswer[1])
                nextAnswer[2] = value[0];
            if(value[0] < nextAnswer[2]){
                return nextAnswer; 
            }
        }
        int[] newAnswer = {worst, answer[1], answer[2]};
        return newAnswer;
    }
    /**
     * answer: ay of the current value + the alpha + the beta,
     *    the player, and an ArrayList of possible boards
     * @param answer
     * @param player
     * @param allMoves
     * @return an array of the best move, or a move that is lower than the beta, or a
     *   move that is higher than the alpha + the alpha + the beta
     */
    private int[] baseCase(int[] answer, int player, ArrayList<Board> allMoves){
        if(player == truePlayer){
            int best = -201;
            for(int i = 0; i < allMoves.size(); i++){
                int enemy = findEnemy(player, AIboard);
                int value = boardValue(player, enemy, allMoves.get(i));
                if(value > best)
                    best = value;
                if(value > answer[1]){
                    int[] newAnswer = {value, answer[1], answer[2]};
                    return newAnswer;
                }
            }
            int[] newAnswer = {best, answer[1], answer[2]};
            return newAnswer;
        }
        int worst = 201;
        for(int i = 0; i < allMoves.size(); i++){
            int value = boardValue(truePlayer, player, allMoves.get(i));
            if(value < worst)
                worst = value;
            if(value < answer[2]){
                int[] newAnswer = {value, answer[1], answer[2]};
                return newAnswer;
            }
        }
        int[] newAnswer = {worst, answer[1], answer[2]};
        return newAnswer;
    }
    /**
     * @param a board
     * @param a player
     * @return the player who goes next
     */
    private int nextPlayer(Board b, int player){
        for(int i = (player%AIboard.NUMPLAY)+1; i < (player%AIboard.NUMPLAY)+5; i++){
            if(b.playerPlace(i)[0] == -1)
                continue;
            return i;
        }
        return -1;
    }
    /**
     * @param turn
     * @param enemy closest to winning 
     * @param b that is being examined
     * @return number of moves it will take the enemy to win minus
     *   the number of moves it will take the player to win
     *   plus 2*the number of remaining walls
     */
    private int boardValue(int turn, int enemy, Board b){
        if(b.haveWon())
            return 200;
        int enemyMoves = b.bestMove(enemy)[2];
        int playerMoves = b.bestMove(turn)[2];
        //int walls = 2*b.playerWalls[turn-1];
        return enemyMoves-playerMoves;
    }
    /**
     * @param turn
     * @param b
     * @return the enemy who is closest to winning
     */
    private int findEnemy(int turn, Board b){
        int enemy = -1;
        int[] players = new int[4];
        for(int i = 1; i < 5; i++){
            int[] place = b.playerPlace(i);
            players[i-1] = place[0];
        }
        int best = 200;
        for(int i = 1; i < 5; i++){
            if(i==turn || players[i-1]==-1)
                continue;
            int movesAway = b.bestMove(i)[2];
            if(movesAway < best){
                best = movesAway;
                enemy = i;
            }
        }
        return enemy;
    }
    /**
     * PostCondition: board is set to panic
     * @param b containing the next move
     * @return if the board is trying to move back to its
     * old location despite no walls being placed
     */
    private boolean splitMove(Board b){
        // No new walls
        if(wallPlaced(lastMove, b))
            return false;
        // Moving to old location
        int[] oldPlace = lastMove.playerPlace(truePlayer);
        int[] newPlace = b.playerPlace(truePlayer);
        if(oldPlace[0] != newPlace[0] || oldPlace[1] != newPlace[1])
            return false;
        panic = true;
        return true;
    }
    /**
     * @param the previous board
     * @param the current board
     * @return if a wall has been placed in the last turn
     */
    private boolean wallPlaced(Board b1, Board b2){
        int oldWalls = 0;
        int newWalls = 0;
        for(int i = 0; i < 4; i++){
            oldWalls += b1.playerWalls[i];
            newWalls += b2.playerWalls[i];
        }
        return oldWalls != newWalls;
    }
    /**
     * @param whose turn it is
     * @param a board
     * @return an Array of all possible moves
     */
    private ArrayList<Board> findMoves(int turn, Board b){
        ArrayList<Board> posMoves = new ArrayList<Board>();
        if(b.playerWalls[turn-1] != 0)
            posMoves = wallPlacementSearch(b, turn);
        for(int row = 0; row < 17; row=row+2){
            for(int col =0; col < 17; col=col+2){
                int[] destination = new int[2];
                destination[0] = col;
                destination[1] = row;
                Board nextStep = eachStep(turn, destination, b);
                if(!nextStep.equals(b))
                    posMoves.add(nextStep);
            }
        }
        return posMoves;
    }
    /**
     * @param whose turn it is
     * @param a location on the board
     * @param a board
     * @return the board after moving the player to the destination, if possible
     */
    private Board eachStep(int turn, int[] destination, Board b){	
        Board temp = new Board(b);
        if(temp.aiCanMove(destination, turn))
            temp.quickMove(destination, turn);
        return temp;
    }
    /**
     * @param the board 
     * @param the current player
     * @return an ArrayList of one board for every possible wall placement
     */
    private ArrayList<Board> wallPlacementSearch(Board b, int player){
        ArrayList<Board> posMoves = new ArrayList<Board>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                int[] theWall = new int[3];
                theWall[0] = i;
                theWall[1] = j;
                theWall[2] = 0;
                if(b.canPlaceWall(theWall, player)){
                    Board tempB = new Board(b);
                    tempB.placeWallBoard(theWall, player);
                    posMoves.add(tempB);
                }
                theWall[2] = 1;
                if(b.canPlaceWall(theWall, player)){
                    Board tempB = new Board(b);
                    tempB.placeWallBoard(theWall, player);
                    posMoves.add(tempB);
                }
            }	
        }
        return posMoves;
    }

}



