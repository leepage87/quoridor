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


import src.ui.Board;
import src.ui.BoardButton;
import src.ui.GameBoardWithButtons;


public class NetworkClient {
    //TODO: GET REAL PORT
    final static int REMOTE_PORT = 4050;
    public static String player0Address;
    public static String player1Address;
    public static String player2Address;
    public static String player3Address;
    public static int player0Port;
    public static int player1Port;
    public static int player2Port;
    public static int player3Port;
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
    public static String fromPlayer;
    static int turn;
    private static int numberOfPlayers;
    static HashMap<Integer, Scanner> networkInputMap = new HashMap<Integer, Scanner>();
    static HashMap<Integer, PrintStream> networkOutputMap = new HashMap<Integer, PrintStream>();



    public NetworkClient(){

    }

    /**
     * Creates a NetworkClient object for one local human and one network AI
     * 
     **/
    public NetworkClient(String player1Address){
        //break playerAddresses into hostnames and port
        player1Port = getPort(player1Address);      
        this.player1Address = player1Address.substring(0, player1Address.indexOf(':'));
        
        numberOfPlayers = 2;
    }

    /**
     * Creates a NetworkClient object for two network AI players
     * 
     **/
    public NetworkClient(String player0Address, String player1Address){
        //break playerAddresses into host names and port
        player0Port = getPort(player0Address);      
        this.player0Address = player0Address.substring(0, player0Address.indexOf(':'));

        player1Port = getPort(player1Address);  
        this.player1Address = player1Address.substring(0, player1Address.indexOf(':'));

        numberOfPlayers = 2;

    }

    /**
     * Creates a NetworkClient object for one local human and three network AI players
     * 
     **/
    public NetworkClient(String player1Address, String player2Address, String player3Address){
        //break playerAddresses into hostnames and port
          
        player1Port = getPort(player1Address);  
        this.player1Address = player1Address.substring(0, player1Address.indexOf(':'));
        
        player2Port = getPort(player2Address);
        this.player2Address = player2Address.substring(0, player2Address.indexOf(':'));
        
        player3Port = getPort(player1Address);      
        this.player3Address = player3Address.substring(0, player3Address.indexOf(':'));
        
        numberOfPlayers = 4;
    }
    /**
     * Creates a NetworkClient object for four players
     * 
     **/
    public NetworkClient(String player0Address, String player1Address, String player2Address, String player3Address){
        //break playerAddresses into hostnames and port
        
        String tempPort = player0Address.substring(player0Address.indexOf(':')+1, player0Address.length());
        player0Port = getPort(player0Address);       
        this.player0Address = player0Address.substring(0, player0Address.indexOf(':'));
        
        player1Port = getPort(player1Address); ;   
        this.player1Address = player1Address.substring(0, player1Address.indexOf(':'));
        
        player2Port = getPort(player2Address);       
        this.player2Address = player2Address.substring(0, player2Address.indexOf(':'));
        
        player3Port = getPort(player3Address);        
        this.player3Address = player3Address.substring(0, player3Address.indexOf(':'));
        numberOfPlayers = 4;

    }//TODO: make player move

    /**
     * syncWithPlayers - sends initial message to each player, waits for a response
     * @throws UnknownHostException
     * @throws IOException
     */
    public void syncWithPlayers() throws UnknownHostException, IOException {


        Scanner sc = new Scanner(System.in);

        player0Socket = new Socket(player0Address, REMOTE_PORT);

        player1Socket = new Socket(player1Address, REMOTE_PORT);

        networkOutputMap.put(0,outToPlayer0 = new PrintStream(player0Socket.getOutputStream()));
        networkOutputMap.put(1, outToPlayer1 = new PrintStream(player1Socket.getOutputStream()));

        networkInputMap.put(0, inFromPlayer0 = new Scanner(player0Socket.getInputStream())); 
        networkInputMap.put(1, inFromPlayer1 = new Scanner(player1Socket.getInputStream()));

        if(numberOfPlayers == 4){
            player2Socket = new Socket(player2Address, REMOTE_PORT);
            player3Socket = new Socket(player3Address, REMOTE_PORT);

            networkOutputMap.put(2, outToPlayer2 = new PrintStream(player2Socket.getOutputStream()));          
            networkOutputMap.put(3, outToPlayer3 = new PrintStream(player3Socket.getOutputStream()));

            networkInputMap.put(2, inFromPlayer2 = new Scanner(player2Socket.getInputStream()));
            networkInputMap.put(3, inFromPlayer3 = new Scanner(player3Socket.getInputStream()));
        }



        if (numberOfPlayers == 2){
            outToPlayer0.println("QUORIDOR 2 0");
            outToPlayer1.println("QUORIDOR 2 1");
        }else{//NumberOfPlayers == 4
            outToPlayer0.println("QUORIDOR 4 0");
            outToPlayer1.println("QUORIDOR 4 1");
            outToPlayer2.println("QUORIDOR 4 2");
            outToPlayer3.println("QUORIDOR 4 3");
        }

        for (int i = 0; i < 2; i++){
            fromPlayer = networkInputMap.get(i).nextLine();

            if(!fromPlayer.contains("READY")){
                System.err.println("NetworkClient> Unexpected response from Player " + i);
                System.exit(1);
            }else{ //this else can be done away with
                System.out.println("NetworkClient> player " + i +" says: " + fromPlayer);
            }
        }     
        if (numberOfPlayers == 4){
            for (int i = 2; i < 4; i++){
                fromPlayer = networkInputMap.get(i).nextLine();

                if(!fromPlayer.contains("READY")){
                    System.err.println("NetworkClient> Unexpected response from Player " + i);
                    System.exit(1);
                }else{ //this else can be done away with
                    System.out.println("NetworkClient> player " + i +" says: " + fromPlayer);
                }
            } 
        }




    }

    /**
     * tells a player that it has been kicked from the game
     * if that player has made an illegal move
     * @param playerID
     */
    public static void removePlayer(int playerID){
        networkOutputMap.get(playerID-1).println("REMOVE!");
        System.out.println("sending REMOVE! to player: " + (playerID-1));
    }

    //TODO: check if we're broadcasting an updated board object to everybody.
    /**
     * broadcastMove: broadcasts the last legal move made to all players
     * @param nextMove: the last legal move made
     */
    public static void broadcastMove(String nextMove) { 

        //TODO: test this with multiple players
        networkOutputMap.get(0).println(nextMove);
        networkOutputMap.get(1).println(nextMove);

        if (numberOfPlayers == 4){
            for (int i = 2; i <=3; i++)
                networkOutputMap.get(i).println(nextMove); 
        }

    }


    /**
     * getMove(): this method gets a move from the player whose turn it is
     * @return: returns the move taken from the player
     */
    public static String getMove(int player){
        turn = player-1;
        networkOutputMap.get(turn).println("MOVE?");
        fromPlayer = networkInputMap.get(turn).nextLine();

        
        System.out.println("NetworkClient> Player " + turn + " has responded with a move: "+
                fromPlayer);
         
        return fromPlayer;
    }

    /**
     * closes all sockets
     * @throws IOException
     */

    public void kill() throws IOException{
        //close all the sockets
        player0Socket.close();
        player1Socket.close();
        if(numberOfPlayers == 4){
            player2Socket.close();
            player3Socket.close();
        }
    }
    
    public int getPort(String address){
        return (Integer.parseInt(address.substring(address.indexOf(':')+1, address.length())));
    }

}

/*
 * 
 * 
 * 
 * 


 

    options [0] = "Local Game";
    options [1] = "Network Game";
    options [2] = "Quit";
           int n = JOptionPane.showOptionDialog(GameBoardWithButtons.contentPane, 
                    "Play local or remote opponent?","Network Game?",
                    JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
           
           if (n == 1 && numPlay == 2){
                String address = JOptionPane.showInputDialog("Input player address separated by spaces", "ex. hostname0:port");
                NetworkClient network = new NetworkClient(address);
                networkGame = true;
            }
                else if (n == 1 && numPlay == 4 ){
                String address = JOptionPane.showInputDialog("Input three player addresses separated by spaces", "ex. hostname0:port hostname1:port hostname2:port");
                Scanner addressScanner = new Scanner(address);
                String player1Address = addressScanner.next(); 
                String player2Address = addressScanner.next(); 
                String player3Address = addressScanner.next(); 
                NetworkClient network = new NetworkClient(player1Address, player2Address, player3Address);
                networkGame = true;
            }
            else if (n == 2)
                System.exit(0);

 * 
 * 
 * 
 */


