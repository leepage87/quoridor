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



public class MoveServer extends Thread {
    int PlayerNo;
    int NumberOfPlayers;
    Socket connection;
    final static int SERVER_LISTEN_PORT = 4050;//subject to change, get official port from Ladd

    public MoveServer(Socket conn) {
        this.connection = conn;
    }

    public void run() {
        PrintStream outToClient = null;
        Scanner inFromClient = null;
        int oldRow, oldCol, newRow, newCol;
        char opCode;
        String fromGameClient;
        while (true){
            try {
                inFromClient = new Scanner(connection.getInputStream());
                outToClient = new PrintStream(connection.getOutputStream());
                // Acknowledge connection from gameDisplay
                fromGameClient = inFromClient.nextLine();
                System.out.println("MoveServer> line rec'd from client " + fromGameClient);

                if (fromGameClient.contains("HI")){
                    //"HI <PlayerNo> <NumberOfPlayers>"
                    outToClient.println("HI");
                    /*
                     * 
                     * Debug code for that random newline char
                     * 
                     *
                    System.out.println(hi.indexOf('\n'));
                    */
                    //get playerNo and NumberOfPlayers
                    PlayerNo = fromGameClient.charAt(3)-'0';
                    NumberOfPlayers = fromGameClient.charAt(5)- '0';
                    System.out.println("MoveServer> I am player number: " + PlayerNo + " and there are " + NumberOfPlayers + " playing.");
                    //Board gameBoard = new Board(NumberOfPlayers);
                }
                else {
                    System.err.println("???????????");
                }


                do {
                    //System.out.println("MoveServer> top of DO method");
                    fromGameClient = inFromClient.nextLine();
                    if (fromGameClient.contains("MOVE?")){
                        System.out.println("MoveServer> Received 'MOVE?' from client, issuing move");

                        //TODO: get a move from AI and break up into rows and cols
                        oldRow = 1;
                        oldCol = 1;
                        newRow = 1;
                        newCol = 1;
                        opCode = 'M';

                        String nextMove = "MOVE! " + PlayerNo + " " + opCode + " " + oldRow + " "
                                + oldCol + " " + newRow + " " + newCol;
                        
                        
                        /*
                         * Debug code for that random newline char

                        if (nextMove.charAt(0) == '\n'){
                            System.out.println("beginning of move!");
                        }
                        System.out.println(nextMove.indexOf('\n'));
                        *
                        */
                        //send move to client
                        outToClient.println(nextMove);
                        System.out.println("MoveServer> Move sent was: " + nextMove);
                        fromGameClient = inFromClient.nextLine();
                        if(fromGameClient.equals(nextMove)){
                            System.out.println("MoveServer> Move has been accepted, making the move now");
                            //TODO: make player move
                        }else if (fromGameClient.contains("REMOVE!")){
                            System.out.println("Move was illegal, kicked out of game");
                            System.exit(0);
                        }
                        
                        
                        
                    }else if (fromGameClient.contains("MOVE!")){
                        opCode = fromGameClient.charAt(8);
                        oldRow = fromGameClient.charAt(10);
                        oldCol = fromGameClient.charAt(12);
                        newRow = fromGameClient.charAt(14);
                        newCol = fromGameClient.charAt(16);
                    }else if (fromGameClient.contains("REMOVE!")){
                        //remove player from game
                    }else{
                        //anything else or game over or something
                    }
                    //TODO: change GAME OVER to whatever protocol is
                }while (!fromGameClient.contains("GAME OVER"));
                
                
                //TODO: display losing or winning message
                //close the connection
                connection.close();



            } catch (Exception e) {
            }
        }
    }

    public static void main(String [] args) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(SERVER_LISTEN_PORT);
        System.out.println("MoveServer> Listening on port: " + SERVER_LISTEN_PORT);


        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            new MoveServer(connectionSocket).start();
        }
    }
}