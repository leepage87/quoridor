/** 
 * @author: Lee Page
 * teamOrangeBeard
 * NetworkClient
 * CIS 405 Software Engineering 
 * Quoridor Project
 **/
package src.network;

import java.io.*;
import java.net.*;
import java.util.*;

import src.ui.BoardButton;


public class NetworkClient {
    //TODO: GET REAL PORT
    final static int REMOTE_PORT = 4050;
    static String Player0Address;
    static String Player1Address;
    static String Player2Address;
    static String Player3Address;
    static Socket player0Socket;
    static Socket player1Socket;
    static Socket player2Socket;
    static Socket player3Socket;
    static PrintStream outToPlayer0;
    static PrintStream outToPlayer1;
    static PrintStream outToPlayer2;
    static PrintStream outToPlayer3;
    static Scanner inFromPlayer0;
    static Scanner inFromPlayer1;
    static Scanner inFromPlayer2;
    static Scanner inFromPlayer3;
    static int turn = 0;
    static HashMap<Integer, Scanner> networkInputMap = new HashMap<Integer, Scanner>();
    static HashMap<Integer, PrintStream> networkOutputMap = new HashMap<Integer, PrintStream>();

    //TODO: ALLOW USER INPUT OF NumberOfPlayers
    private static int numberOfPlayers = 2;

    public static void main(String [] args) throws Exception{

        //TODO: CHANGE ALL PLAYER ADDRESSES TO USER INPUT

        Scanner sc = new Scanner(System.in);
        System.out.print("NetworkClient> Please enter a host to contact: ");
        Player0Address = sc.next();
        //Player1Address = "localhost";
        player0Socket = new Socket(Player0Address, REMOTE_PORT);

        //player1Socket = new Socket(Player1Address, REMOTE_PORT);


        if(numberOfPlayers == 4){
            Player2Address = "localhost";
            Player3Address = "localhost";
            player2Socket = new Socket(Player2Address, REMOTE_PORT);

            player3Socket = new Socket(Player3Address, REMOTE_PORT);

        }


        //might be unnecessary
        int localPortForPlayer0 = player0Socket.getLocalPort();
        //int localPortForPlayer1 = player1Socket.getLocalPort();
        System.out.println("NetworkClient> Connected to player0 with local port "+localPortForPlayer0);

        networkOutputMap.put(0,outToPlayer0 = new PrintStream(player0Socket.getOutputStream()));
        networkInputMap.put(0, inFromPlayer0 = new Scanner(player0Socket.getInputStream()));

        //System.out.println("Connected to player1 with local port "+localPortForPlayer1);

        //scannerOutputMap.put(1, outToPlayer1 = new PrintStream(player1Socket.getOutputStream()));
        //scannerInputMap.put(1, inFromPlayer1 = new Scanner(player1Socket.getInputStream()));

        /*

        if(NumberOfPlayers ==4){
            ScannerOutputMap.put(2, outToPlayer2 = new PrintStream(player2Socket.getOutputStream()));
            ScannerInputMap.put(2, inFromPlayer2 = new Scanner(player2Socket.getInputStream()));

            ScannerOutputMap.put(3, outToPlayer3 = new PrintStream(player3Socket.getOutputStream()));
            ScannerInputMap.put(3, inFromPlayer3 = new Scanner(player3Socket.getInputStream()));

            System.out.println("Connected to player2 with local port "+player2Socket.getLocalPort());
            System.out.println("Connected to player3 with local port "+player3Socket.getLocalPort());
        }
         */

        //initial SYN with player0 and 1 OMG hi2u

        if (numberOfPlayers == 2){
            System.out.println("NetworkClient> Sending 'HI 0 2' to player 0");
            outToPlayer0.println("HI 0 2");
            //outToPlayer1.println("HI 1 2");
        }else{//NumberOfPlayers == 4
            outToPlayer0.println("HI 0 4");
            outToPlayer1.println("HI 1 4");
            outToPlayer2.println("HI 2 4");
            outToPlayer3.println("HI 3 4");
        }
        String fromPlayer = inFromPlayer0.nextLine();
        if(!fromPlayer.equals("HI")){
            System.err.println("NetworkClient> Unexpected response from Player 0");
            System.exit(1);
        }else{ //this else can be done away with
            System.out.println("NetworkClient> player0 says: " + fromPlayer);
        }
        //TODO: contact player1 to 3
        /*
        if(!inFromPlayer1.next().contains("HI")){
            System.err.println("Unexpected response from Player 1");
            System.exit(1);
        }*/

        //TODO: FIX gameOver
        boolean gameOver = false;
        String nextMove;
        while(!gameOver){//while a player hasn't won, ask for moves and relay info
            System.out.println("NetworkClient> Requesting a move from " + turn);
            nextMove = getMove();


            //TODO: get a isLegal? method to check if the move rec'd is legal
            /*if(nextMove.isLegal()){
                 //issue move to all players using broadcastMove()
            }else{
                 //REMOVE! player from game   
            }*/
            broadcastMove(nextMove);


            //TODO: fix gameOver
            //gameOver = true;
        }



        //close all the sockets
        player0Socket.close();
        //player1Socket.close();
        //player2Socket.close();
        //player3Socket.close();

    }


    /**
     * broadcastMove: broadcasts the last legal move made to all players
     * @param nextMove: the last legal move made
     */
    private static void broadcastMove(String nextMove) { 
        
        //TODO: test this with multiple players
        networkOutputMap.get(0).println(nextMove);
        //networkOutputMap.get(1).println(nextMove);
        /*
         * if (numberOfPlayers == 4){
                for (int i = 2; i <=3; i++)
                networkOutputMap.get(i).println(nextMove); 
                }
         */
    }


    /**
     * getMove(): this method gets a move from the player whos turn it is
     * @return: returns the move taken from the player
     */
    private static String getMove(){
        networkOutputMap.get(turn).println("MOVE?");
        String fromPlayer = networkInputMap.get(turn).nextLine();
        
        System.out.println("the first fromPlayer nextLine: " + fromPlayer);
        //for some reason theres a newline char in there somewhere, so we have to get the nextLine again.
     //   fromPlayer = networkInputMap.get(turn).nextLine();

        System.out.println("NetworkClient> Player " + turn + " has responded with a move: "+
                fromPlayer);

        return fromPlayer;
    }
}




