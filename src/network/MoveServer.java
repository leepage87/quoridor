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

import src.ui.Board;



public class MoveServer extends Thread {
    int playerNo;
    int numberOfPlayers;
    Socket connection;
    final static int DEFAULT_SERVER_LISTEN_PORT = 4050;//allow user to pick port
    Board b;

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
            System.out.println("MoveServer> line rec'd from client " + fromGameClient);

            if (fromGameClientScanner.next().equals("QUORIDOR")){
                //"QUORIDOR <NumberOfPlayers> <PlayerNo>"
                //get playerNo and NumberOfPlayers
                numberOfPlayers = fromGameClientScanner.nextInt();
                playerNo = fromGameClientScanner.nextInt();
                
                outToClient.println("READY " + playerNo);

                b = new Board(numberOfPlayers);
                System.out.println("MoveServer> I am player number: " + playerNo + " and there are " + numberOfPlayers + " playing.");
            }
            else {
                System.err.println("");
            }


            do {
                fromGameClient = inFromClient.nextLine();
                if (fromGameClient.contains("MOVE?")){
                    System.out.println("MoveServer> Received 'MOVE?' from client, issuing move");

                  //TODO: get a move from AI and break up into rows and cols
                    rowOne = 1;
                    colOne = 1;
                    rowTwo = 1;
                    colTwo = 1;
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
                    System.out.println("MoveServer> Move sent was: " + nextMove);

                    //get response from client
                    fromGameClient = inFromClient.nextLine();
                    if(fromGameClient.contains("MOVED" + nextMove.substring(6))){
                        System.out.println("MoveServer> Move has been accepted, making the move now");
                        if(fromGameClient.charAt(6) == 'M'){
                            //move the player
                        }else{//its a wall placement
                            //place the wall
                            
                            //b.placeWallBoard();
                        }
                    }else if (fromGameClient.equals("REMOVED")){
                        if(playerNo == ((int)fromGameClient.charAt(8))){
                        System.out.println("MoveServer> Move was illegal, you've been kicked out of game (Player " + (playerNo+1) +")" );
                        //connection.close();
                        }else{//someone else was kicked
                            //TODO: kick the other player from the game
                        }
                    }//end if its a move request
                    
                //server telling you to move a piece    
                }else if (fromGameClient.contains("MOVED")){
                    //MOVED M (0,0) (0,0)
                    opCode = fromGameClient.charAt(6);
                    rowOne = fromGameClient.charAt(9);
                    colOne = fromGameClient.charAt(11);
                    rowTwo = fromGameClient.charAt(15);
                    colTwo = fromGameClient.charAt(17);
                    //move the piece
                }else{
                    //anything else or game over or something
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