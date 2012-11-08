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
//import src.ui.Board;

import java.net.*;
import java.util.*;

import src.ui.AI;
import src.ui.Board;



public class MoveServer extends Thread {
    int playerNo;
    int numberOfPlayers;
    final static int DEFAULT_SERVER_LISTEN_PORT = 4050;//if user doesn't input port
    public Board b;
    Socket connection;
    private AI ai;
    private String nextMove;
    private Boolean justMoved;
    
    public MoveServer(Socket conn) {
        this.connection = conn;
    }

    public void run() {
        PrintStream outToClient = null;
        Scanner inFromClient = null;
        
        String fromGameClient;

        try {
            inFromClient = new Scanner(connection.getInputStream());
            outToClient = new PrintStream(connection.getOutputStream());
            // Acknowledge connection from gameDisplay
            fromGameClient = inFromClient.nextLine();
            Scanner fromGameClientScanner = new Scanner (fromGameClient);
            System.out.println("MoveServer " + playerNo + "> line rec'd from client " + fromGameClient);

            if (fromGameClientScanner.next().equals("QUORIDOR")){
                //"QUORIDOR <NumberOfPlayers> <playerNo>"
                //get playerNo and NumberOfPlayers
                numberOfPlayers = fromGameClientScanner.nextInt();
                playerNo = fromGameClientScanner.nextInt();

                outToClient.println("READY " + playerNo);

                b = new Board(numberOfPlayers);
                ai = new AI (b);
                System.out.println("MoveServer " + playerNo + "> I am player number: " + playerNo + " and there are " + numberOfPlayers + " playing.");
            }else{
                System.err.println("Protocol not followed");
            }


            do {
                fromGameClient = inFromClient.nextLine();

                if (fromGameClient.contains("MOVE?")){

                    System.out.println("MoveServer " + playerNo + "> Received 'MOVE?' from client, issuing move");
                    //send move to client
                    nextMove = getMove(playerNo, b, ai);
                    outToClient.println(nextMove);
                    justMoved = true;
                    System.out.println("MoveServer " + playerNo + "> Move sent was: " + nextMove);
                    //get response from client
                    fromGameClient = inFromClient.nextLine();
                    //MOVED <player-id> <op> <location-1> <location-2>
                    
                    if(fromGameClient.contains("MOVED " + playerNo + nextMove.substring(4))){
                        if(fromGameClient.charAt(8) == 'M'){
                            //move the player
                            b = move(fromGameClient, b);
                        }else{//its a wall placement
                            //place the wall
                            //b.placeWallBoard();
                        }
                    }else if (fromGameClient.equals("REMOVED")){
                        if(playerNo == ((int)fromGameClient.charAt(8))){
                            System.out.println("MoveServer " + playerNo + "> Move was illegal, you've been kicked out of game (Player " + (playerNo+1) +")" );
                            connection.close();
                            System.exit(0);
                        }else{//someone else was kicked
                            kick((int)fromGameClient.charAt(8), b);
                        }
                    }



                }else if (fromGameClient.contains("MOVED")&& !justMoved){
                    //server telling you to move a different player's piece 
                    b = move(fromGameClient, b);                      
                }else if(fromGameClient.contains("REMOVED")){
                    //a different player has been removed  
                    kick((int)fromGameClient.charAt(8), b);
                }
                System.out.println("here here");
            }while (!fromGameClient.contains("WINNER"));


            //TODO: display losing or winning message
            System.out.println("Player " + fromGameClient.charAt(7) + " has won!");
            connection.close();
            System.exit(0);

        } catch (Exception e) {
        }
    }

    private String getMove(int PlayerNo, Board b, AI ai){
        int rowOne, colOne, rowTwo, colTwo;
        char opCode;

        Board tempOldBoard = b;
        //record start location of player
        int[] startLocation = b.playerPlace(playerNo+1);
        //record final location of player
        Board tempNewBoard = ai.aiMove(playerNo+1);
        int [] endLocation = tempNewBoard.playerPlace(playerNo+1);
        int[] wallLocation = ai.aiWall(playerNo, tempOldBoard, tempNewBoard);
        if(wallLocation[0] != 0 && wallLocation[1] != 0){
            opCode = 'W';
            //a wall movement has been made
            //this is the center spot
            if (wallLocation[2] == 0){
                //its a horizontal wall
                rowOne = wallLocation[1]+1;
                colOne = wallLocation[0];
                rowTwo = wallLocation[1]+3;
                colTwo = wallLocation[0];
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
     * @param order - the line received from the client
     * @param moveBoard - the board's current status
     * @return moveBooard - returns an updated board with the new move
     */
    
    
    private Board move(String frago, Board moveBoard){
        //fragmentary order: MOVED P M (R, C) (R, C)
        int[] move = {(frago.charAt(21)-'0')*2, (frago.charAt(18)-'0')*2};
        System.out.println("Move[]" + move[0] + move[1]);
        moveBoard.quickMove(move, ((int)frago.charAt(6))+1);
        /*
         * 
         * 
         * CHECK OUT QUICKMOVE METHOD, GETTING STUCK
         * 
         * 
         * 
         */
        System.out.println("here here here");
        return moveBoard;
    }
    /**
     * Kicks a player from the game
     * @param player
     * @param kickBoard
     */
    private void kick(int player, Board kickBoard){
        int[] location = kickBoard.playerPlace(player+1);
        kickBoard.grid[location[0]*2][location[1]*2] = 0;
    }

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