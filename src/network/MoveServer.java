/** 
 * @author: Lee Page - teamOrangeBeard
 * MoveServer
 * CIS 405 Software Engineering 
 * Quoridor Project
 * Runs on default port 4050 or args input, listens for connections
 * delivers moves from AI to to the connected client 
 **/
package src.network;

import java.io.*;
import java.net.*;
import java.util.*;
import src.main.AI;
import src.main.Board;
/**
 * Multi-threaded server, listens on default port 4050 or args[0] and dishes out AI moves
 */
public class MoveServer extends Thread {
    private int playerNo;
    private int numberOfPlayers;
    private final static int DEFAULT_SERVER_LISTEN_PORT = 4050;//if user doesn't input port
    private Board b;
    private Socket connection;
    private AI ai;
    private String nextMove;
    private long tID;
    /**
     * constructor
     * @param conn the connection with network client
     */
    public MoveServer(Socket conn) {
        this.connection = conn;

    }
    /**
     * the multithreaded move server run method
     */
    public void run() {

        tID = Thread.currentThread().getId();
        PrintStream outToClient = null;
        Scanner inFromClient = null;

        String fromGameClient;

        try {
            inFromClient = new Scanner(connection.getInputStream());
            outToClient = new PrintStream(connection.getOutputStream());

            fromGameClient = inFromClient.nextLine();
            Scanner fromGameClientScanner = new Scanner (fromGameClient);
            if (fromGameClientScanner.next().equals("QUORIDOR")){
                //get playerNo and NumberOfPlayers
                numberOfPlayers = fromGameClientScanner.nextInt();
                playerNo = fromGameClientScanner.nextInt();
                
                // Acknowledge connection from gameDisplay
                outToClient.println("READY " + "OrangeBeard"+(playerNo+1));

                b = new Board(numberOfPlayers);
                ai = new AI (b);
            }else{
                System.err.println("MoveServer> ERROR: Protocol not followed, closing connection");
                connection.close();
            }


            do {
                //get a line from the client
                fromGameClient = inFromClient.nextLine();
                if(fromGameClient.contains("WINNER")){
                    System.out.println("Player " + fromGameClient.charAt(7) + " has won!");
                    connection.close();
                    break;
                }
                if (fromGameClient.contains("MOVE?")){
                    //get move, send to client
                    nextMove = getMove(playerNo, b, ai);
                    outToClient.println(nextMove);

                    //get response from client
                    fromGameClient = inFromClient.nextLine();

                    //your move has been confirmed by the server, make the move now
                    if(fromGameClient.contains("MOVED " + playerNo + nextMove.substring(4)))
                        b = move(fromGameClient, b);

                    //the client's response is that you've been kicked
                    else if (fromGameClient.contains("REMOVED")){
                        System.out.println("MoveServer> Move was illegal, you've been kicked out of game (Player " + (playerNo+1) +")" );
                        connection.close();
                    }
                }else if (fromGameClient.substring(0,5).equals("MOVED")){
                    //server telling you to move a different player's piece 
                    b = move(fromGameClient, b);
                }else if(fromGameClient.contains("REMOVED")){
                    //a different player has been removed  
                    b = kick(fromGameClient.charAt(8)-'0' + 1, b);
                }
            }while (!fromGameClient.contains("REMOVED " + playerNo));
        }catch (Exception e){

        }
    }    


    /**
     * gets an AI move to be delivered to the network client
     * @param PlayerNo
     * @param b a board containing current player and wall positions
     * @param ai the AI object that makes moves
     * @return the move made in network protocol format
     */
    private String getMove(int PlayerNo, Board b, AI ai){
        int rowOne, colOne, rowTwo, colTwo;
        char opCode;

        Board tempOldBoard = b;
        //record start location of player
        int[] startLocation = b.playerPlace(playerNo+1);
        //record final location of player
        Board tempNewBoard = ai.aiMove(playerNo+1, 1);
        int [] endLocation = tempNewBoard.playerPlace(playerNo+1);
        int[] wallLocation = aiWall(playerNo+1, tempOldBoard, tempNewBoard);

        if(startLocation[0] == endLocation[0] && startLocation[1] == endLocation[1]){//its a wall
            opCode = 'W';
            //a wall movement has been made
            if (wallLocation[2] == 0){
                //its a horizontal wall
                rowOne = wallLocation[1]+1;
                colOne = wallLocation[0];
                rowTwo = wallLocation[1]+1;
                colTwo = wallLocation[0]+2;
            }else{//its a vertical wall
                rowOne = wallLocation[1];
                colOne = wallLocation[0]+1;
                rowTwo = wallLocation[1]+2;
                colTwo = wallLocation[0]+1;
            }

            return ("MOVE "+ opCode + " ("+rowOne+", "
                    +colOne+") " + "("+rowTwo+", "+colTwo+")");
        }else{//its a player move
            opCode = 'M';
            rowOne = startLocation[1]/2;
            colOne = startLocation[0]/2;
            rowTwo = endLocation[1]/2;
            colTwo = endLocation[0]/2;

            return ("MOVE "+ opCode + " ("+rowOne+", "
                    +colOne+") " + "("+rowTwo+", "+colTwo+")");
        }
    }

    /**
     * Moves a player after receiving a MOVED line from the client
     * @param frago the line received from the client
     * @param moveBoard the board's current status
     * @return returns an updated board with the new move
     */
    private Board move(String frago, Board moveBoard){
        //fragmentary order: MOVED P M (R, C) (R, C)
        int player = (frago.charAt(6) - '0') + 1;
        int[] move = new int[3];
        if(frago.charAt(8)== 'M'){
            move [0] = (frago.charAt(21)-'0')*2;
            move [1] = (frago.charAt(18)-'0')*2;
            moveBoard.playerPlace(frago.charAt(6)-'0');
            moveBoard.move(move, ((frago.charAt(6)-'0')+1));
        }else{//its a wall placement

            if(frago.charAt(11) == frago.charAt(18)){//horizontal wall
                move[0] = (frago.charAt(14)-'0');
                move[1] = (frago.charAt(11)-'0') -1;
                move[2] = 0;
                moveBoard.placeWallBoard(move, player);
            }else{//vertical wall
                move[0] = (frago.charAt(14)-'0') -1;
                move[1] = (frago.charAt(11)-'0');
                move[2] = 1;
                moveBoard.placeWallBoard(move, player);
            }
        }
        return moveBoard;
    }

    /**
     * Kicks a player from the game
     * @param player which player is kicked
     * @param kickBoard the board the player is on
     * @return the board after the player has been removed
     */
    private Board kick(int player, Board kickBoard){
        System.out.println("MoveServer> Player: " + player + " has been kicked.");
        int[] location = kickBoard.playerPlace(player);
        kickBoard.grid[location[0]] [location[1]] = 0;
        return kickBoard;
    }

    /**
     * determines if a player moved or a wall was placed, if a wall
     * was placed, method returns said wall's location
     * @param player the player that just moved
     * @param old the previous state of the board
     * @param current the new state of the board
     * @return an array containing wall position
     */
    public int[] aiWall(int player, Board old, Board current){
        int[] startPlace = old.playerPlace(player);
        int[] endPlace = current.playerPlace(player);
        if(startPlace[0]!=endPlace[0] || startPlace[1]!=endPlace[1]){
            return null;
        }
        int[] aiWall = new int[3];
        boolean getOut = false;
        for(int col = 0; col < 17; col++){
            for(int row = 0; row < 17; row++){      
                if(old.grid[col][row] != current.grid[col][row]){
                    aiWall[0] = col/2;
                    aiWall[1] = row/2;
                    if(current.grid[col][row+1]==5){
                        aiWall[2] = 1;
                    }
                    getOut = true;
                }
                if(getOut)
                    break;
            }
            if(getOut)
                break;
        }
        return aiWall;
    }
    
    /**
     * method returns the player number of this MoveServer
     * @return this MoveServer's player number
     */
    public int getPlayerNo(){
        return playerNo;
    }

    /**
     * awaits an incoming connection then calls the run method
     * this is a multi-threaded server, it never stops running unless
     * the process is killed
     * @param args which port to listen on, if none is specified a default of 4050 is used
     * @throws Exception
     */
    public static void main(String [] args) throws Exception {
        ServerSocket welcomeSocket;
        if(args.length == 0){
            welcomeSocket = new ServerSocket(DEFAULT_SERVER_LISTEN_PORT);
            System.out.println("MoveServer> Listening on port: " + DEFAULT_SERVER_LISTEN_PORT);
        }else{
            welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("MoveServer> Listening on port: " + args[0]);
        }
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            new MoveServer(connectionSocket).start();
        }
    }
}
