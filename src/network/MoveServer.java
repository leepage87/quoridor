/* 
 * teamOrangeBeard
 * GameDisplayClient
 * CIS 405 Software Engineering 
 * Quoridor Project
 */
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
    AI ai;

    public MoveServer(Socket conn) {
        this.connection = conn;
    }

    public void run() {
        PrintStream outToClient = null;
        Scanner inFromClient = null;
        int rowOne, colOne, rowTwo, colTwo;
        char opCode;
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
            }
            else {
                System.err.println("");
            }


            do {
                fromGameClient = inFromClient.nextLine();
                if (fromGameClient.contains("MOVE?")){
                    System.out.println("MoveServer " + playerNo + "> Received 'MOVE?' from client, issuing move");
                    //record start location of player
                    int[] startLocation = b.playerPlace(playerNo);
                    //record final location of player
                    int [] endLocation = ai.aiMove(playerNo).playerPlace(playerNo);

                    //TODO: ACCOUNT FOR AI MAKING WALL PLACEMENTS

                    rowOne = startLocation[1];
                    colOne = startLocation[0];
                    rowTwo = endLocation[1];
                    colTwo = endLocation[0];
                    //if player move
                    opCode = 'M';
                    //if wall
                    //opCode = 'W';
                    String nextMove;
                    //if player move
                    nextMove = "MOVE "+ opCode + " ("+rowOne+", "
                            +colOne+") " + "("+rowTwo+", "+colTwo+")";


                    //send move to client
                    outToClient.println(nextMove);
                    System.out.println("MoveServer " + playerNo + "> Move sent was: " + nextMove);

                    //get response from client
                    fromGameClient = inFromClient.nextLine();
                    if(fromGameClient.contains("MOVED" + nextMove.substring(6))){

                        System.out.println("MoveServer " + playerNo + "> Move has been accepted, making the move now");
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
                            //TODO: kick the other player from the game
                        }
                    }//end if its a move request

                    //server telling you to move a different player's piece    
                }else if (fromGameClient.contains("MOVED")){
                    //move the piece
                    b = move(fromGameClient, b);

                }else if(fromGameClient.contains("REMOVED")){
                    //a different player has been removed
                }
                //TODO: change GAME OVER to whatever protocol is

                //change while REMOVE! to involve a player number or something, clarify protocol
            }while (!fromGameClient.contains("GAME OVER") || !fromGameClient.contains("REMOVE!"));


            //TODO: display losing or winning message
            //close the connection
            connection.close();



        } catch (Exception e) {
        }
    }
    /**
     * Moves a player after receiving a MOVED line from the client
     * @param order - the line received from the client
     * @param moveBoard - the board's current status
     * @return moveBooard - returns an updated board with the new move
     */
    private static Board move(String frago, Board moveBoard){
        //fragmentary order: MOVED P M (R, C) (R, C)
        int[] move = {frago.charAt(21), frago.charAt(18)};
        moveBoard.quickMove(move, frago.charAt(6));
        return moveBoard;
    }
    
    private static void kick(int player, Board kickBoard){
        int[] location = kickBoard.playerPlace(player);
        kickBoard.grid[location[0]][location[1]] = 0;
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