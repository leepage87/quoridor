/* 
 * teamOrangeBeard
 * NetworkClient
 * CIS 405 Software Engineering 
 * Quoridor Project
 */
package network;

import java.io.*;
import src.ui.Board;
import java.net.*;
import java.util.*;

public class NetworkClient {
    static String Player1Address;
    static String Player2Address;
    private static int NumberOfPlayers;

    public static void main(String [] args) throws Exception{
        Socket player1Socket;
        Socket player2Socket;
        if (NumberOfPlayers == 2){
            Player1Address = "localhost";
            Player2Address = "localhost";

            player1Socket = new Socket(Player1Address, 4050);
            player2Socket = new Socket(Player2Address, 4050);


            int localPortForPlayer1 = player1Socket.getLocalPort();
            int localPortForPlayer2 = player2Socket.getLocalPort();
            System.out.println("Connected to player1 with local port "+localPortForPlayer1);
            PrintStream outToPlayer1 = new PrintStream(player1Socket.getOutputStream());
            Scanner inFromPlayer1 = new Scanner(player1Socket.getInputStream());

            System.out.println("Connected to player2 with local port "+localPortForPlayer2);
            OutputStream outToPlayer2 = player2Socket.getOutputStream();
            Scanner inFromPlayer2 = new Scanner(player2Socket.getInputStream());
            
            //initial SYN with player1
            outToPlayer1.print("HI 1 2");
            while(!inFromPlayer1.hasNextLine());
            //wait for Player1 to respond
            if(!inFromPlayer1.nextLine().equals("HI")){
                System.err.println("Unexpected response from Player 1");
                System.exit(1);
            }
            while(!inFromPlayer2.hasNextLine());
            //wait for Player2 to respond
            if(!inFromPlayer2.nextLine().equals("HI")){
                System.err.println("Unexpected response from Player 2");
                System.exit(1);
            }
            //contact player2
        


        }
        //close connection

        player1Socket.close();
        player2Socket.close();
    }
}