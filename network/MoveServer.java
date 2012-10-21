/* 
 * teamOrangeBeard
 * GameDisplayClient
 * CIS 405 Software Engineering 
 * Quoridor Project
 */
package network;

import java.io.*;
import src.ui.AI;
import src.ui.Board;
import java.net.*;
import java.util.*;



class MoveServer extends Thread {
    int PlayerNo;
    int NumberOfPlayers;
    Socket connection;
    final static int SERVER_LISTEN_PORT = 4050;//subject to change, get official port from Ladd

    public MoveServer(Socket conn) {
        this.connection = conn;
    }

    public void run() {
        while (true){
            PrintStream outToClient = null;
            Scanner inFromClient = null;
            int oldRow, oldCol, newRow, newCol;
            char opCode;
            String fromGameClient;
            Scanner lineScanner;
            int foreignPlayer;


            try {
                inFromClient = new Scanner(connection.getInputStream());
                outToClient = new PrintStream(connection.getOutputStream());
                // Acknowledge connection from gameDisplay
                fromGameClient = inFromClient.nextLine();

                if (fromGameClient.contains("HI")){
                    //"HI <PlayerNo> <NumberOfPlayers>"
                    outToClient.print("HI");
                    //get playerNo and NumberOfPlayers
                    PlayerNo = fromGameClient.charAt(3);
                    NumberOfPlayers = fromGameClient.charAt(5);
                    Board gameBoard = new Board(NumberOfPlayers);
                    
                }
                while(!inFromClient.hasNextLine());
                //wait for the client to respond

                fromGameClient = inFromClient.nextLine();
                //TODO: change GAME OVER to whatever protocol is
                while (!fromGameClient.contains("GAME OVER")){

                    if (fromGameClient.contains("MOVE?")){

                        //TODO: get a move from AI and break up into rows and cols
                        oldRow = 0;
                        oldCol = 0;
                        newRow = 0;
                        newCol = 0;
                        opCode = 'M';

                         

                        //send move to client
                        outToClient.print("MOVE! " + PlayerNo + " " + opCode + " " + oldRow + " "
                                + oldCol + " " + newRow + " " + newCol + " ");
                    }else if (fromGameClient.contains("MOVE!")){
                        //"MOVE! <ForeignPlayer> <opCode> R C R C"
                        foreignPlayer = fromGameClient.charAt(6);
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

                }
                //TODO: display losing or winning message
                //close the connection
                connection.close();



            } catch (Exception e) {
            }
        }
    }

    public static void main(String [] args) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(SERVER_LISTEN_PORT);


        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            new MoveServer(connectionSocket).start();
        }
    }
}