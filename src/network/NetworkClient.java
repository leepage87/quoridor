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

import javax.swing.JOptionPane;

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

    //TODO: turn is unnecessary once integrated with PlayQuor
    static int turn = 0;


    static HashMap<Integer, Scanner> networkInputMap = new HashMap<Integer, Scanner>();
    static HashMap<Integer, PrintStream> networkOutputMap = new HashMap<Integer, PrintStream>();

    //TODO: this is unnecessary once integrated with PlayQuor
    private static int numberOfPlayers = 2;

    //TODO: remove main and turn this bitch into a NetworkClient object
    public static void main(String [] args) throws Exception{
        syncWithPlayers();



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
        player1Socket.close();
        //player2Socket.close();
        //player3Socket.close();

    }


    private static void syncWithPlayers() throws UnknownHostException, IOException {
        //TODO: CHANGE ALL PLAYER ADDRESSES TO USER INPUT

        Scanner sc = new Scanner(System.in);
        System.out.print("NetworkClient> Please enter a host to contact player 0: ");
        Player0Address = sc.next();
        player0Socket = new Socket(Player0Address, REMOTE_PORT);

        System.out.print("NetworkClient> Please enter a host to contact player 1: ");
        Player1Address = sc.next();
        player1Socket = new Socket(Player1Address, REMOTE_PORT);

        networkOutputMap.put(0,outToPlayer0 = new PrintStream(player0Socket.getOutputStream()));
        networkOutputMap.put(1, outToPlayer1 = new PrintStream(player1Socket.getOutputStream()));

        networkInputMap.put(0, inFromPlayer0 = new Scanner(player0Socket.getInputStream())); 
        networkInputMap.put(1, inFromPlayer1 = new Scanner(player1Socket.getInputStream()));

        if(numberOfPlayers == 4){

            Player2Address = "localhost";
            Player3Address = "localhost";
            player2Socket = new Socket(Player2Address, REMOTE_PORT);
            player3Socket = new Socket(Player3Address, REMOTE_PORT);

            networkOutputMap.put(2, outToPlayer2 = new PrintStream(player2Socket.getOutputStream()));          
            networkOutputMap.put(3, outToPlayer3 = new PrintStream(player3Socket.getOutputStream()));

            networkInputMap.put(2, inFromPlayer2 = new Scanner(player2Socket.getInputStream()));
            networkInputMap.put(3, inFromPlayer3 = new Scanner(player3Socket.getInputStream()));
        }



        if (numberOfPlayers == 2){
            System.out.println("NetworkClient> Sending 'HI 0 2' to player 0");
            outToPlayer0.println("HI 0 2");
            outToPlayer1.println("HI 1 2");
        }else{//NumberOfPlayers == 4
            outToPlayer0.println("HI 0 4");
            outToPlayer1.println("HI 1 4");
            outToPlayer2.println("HI 2 4");
            outToPlayer3.println("HI 3 4");
        }
        String fromPlayer;
        for (int i = 0; i < 2; i++){
            fromPlayer = networkInputMap.get(i).nextLine();

            if(!fromPlayer.equals("HI")){
                System.err.println("NetworkClient> Unexpected response from Player " + i);
                System.exit(1);
            }else{ //this else can be done away with
                System.out.println("NetworkClient> player " + i +" says: " + fromPlayer);
            }
        }     
        if (numberOfPlayers == 4){
            for (int i = 2; i < 4; i++){
                fromPlayer = networkInputMap.get(i).nextLine();

                if(!fromPlayer.equals("HI")){
                    System.err.println("NetworkClient> Unexpected response from Player " + i);
                    System.exit(1);
                }else{ //this else can be done away with
                    System.out.println("NetworkClient> player " + i +" says: " + fromPlayer);
                }
            } 
        }




    }


    /**
     * broadcastMove: broadcasts the last legal move made to all players
     * @param nextMove: the last legal move made
     */
    private static void broadcastMove(String nextMove) { 

        //TODO: test this with multiple players
        networkOutputMap.get(0).println(nextMove);
        networkOutputMap.get(1).println(nextMove);

        if (numberOfPlayers == 4){
            for (int i = 2; i <=3; i++)
                networkOutputMap.get(i).println(nextMove); 
        }

    }


    /**
     * getMove(): this method gets a move from the player whos turn it is
     * @return: returns the move taken from the player
     */
    private static String getMove(){
        networkOutputMap.get(turn).println("MOVE?");
        String fromPlayer = networkInputMap.get(turn).nextLine();

        System.out.println("the first fromPlayer nextLine: " + fromPlayer);

        System.out.println("NetworkClient> Player " + turn + " has responded with a move: "+
                fromPlayer);

        return fromPlayer;
    }
}

/*
 * 
 * 
 * 
 * 
 
  
  
  String address = JOptionPane.showInputDialog("Input player addresses separated by spaces", "ex. hostname0 hostname1");
  
  
  
  
  
  
  
  
  
  
 * 
 * 
 * 
 */


