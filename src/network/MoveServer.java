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
    final static int SERVER_LISTEN_PORT = 4050;//allow user to pick port

    public MoveServer(Socket conn) {
        this.connection = conn;
    }

    public void run() {
        PrintStream outToClient = null;
        Scanner inFromClient = null;
        int oldRow, oldCol, newRow, newCol;
        char opCode;
        String fromGameClient;
        //while (true){
            try {
                inFromClient = new Scanner(connection.getInputStream());
                outToClient = new PrintStream(connection.getOutputStream());
                // Acknowledge connection from gameDisplay
                fromGameClient = inFromClient.nextLine();
                Scanner fromGameClientScanner = new Scanner (fromGameClient);
                System.out.println("MoveServer> line rec'd from client " + fromGameClient);

                if (fromGameClientScanner.next().equals("QUORIDOR")){
                    //"QUORIDOR <PlayerNo> <NumberOfPlayers>"
                    outToClient.println("QUORIDOR");

                    //get playerNo and NumberOfPlayers
                    PlayerNo = fromGameClientScanner.nextInt();
                    NumberOfPlayers = fromGameClientScanner.nextInt();
                    System.out.println("MoveServer> I am player number: " + PlayerNo + " and there are " + NumberOfPlayers + " playing.");
                }
                else {
                    System.err.println("");
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

                        String nextMove = "MOVE " + PlayerNo + " " + opCode + " " + oldRow + " "
                        + oldCol + " " + newRow + " " + newCol;


                        //send move to client
                        outToClient.println(nextMove);
                        System.out.println("MoveServer> Move sent was: " + nextMove);
                        
                        //get response from client
                        fromGameClient = inFromClient.nextLine();
                        if(fromGameClient.contains("MOVED" + nextMove.substring(6))){
                            System.out.println("MoveServer> Move has been accepted, making the move now");
                            //TODO: make player move
                        }else if (fromGameClient.equals("REMOVE!")){
                            System.out.println("MoveServer> Move was illegal, kicked out of game (Player " + (PlayerNo+1) +")" );
                            //connection.close();
                            
                           
                        }



                    }else if (fromGameClient.contains("MOVE!")){
                        opCode = fromGameClient.charAt(8);
                        oldRow = fromGameClient.charAt(10);
                        oldCol = fromGameClient.charAt(12);
                        newRow = fromGameClient.charAt(14);
                        newCol = fromGameClient.charAt(16);
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
    //}
    public static void main(String [] args) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(SERVER_LISTEN_PORT);
        System.out.println("MoveServer> Listening on port: " + SERVER_LISTEN_PORT);


        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            new MoveServer(connectionSocket).start();
        }
    }
}