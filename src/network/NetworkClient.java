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

    final static int REMOTE_PORT = 4050;
    public static NetworkPlayer[] players;

    public NetworkClient(){

    }

    /**
     * Creates a NetworkClient object for one local human and one network player
     * @throws IOException 
     * @throws UnknownHostException 
     * 
     **/
    public NetworkClient(String player1Address) throws UnknownHostException, IOException{
        //break playerAddresses into hostnames and port
        int playerPort = getPort(player1Address);      
        player1Address = player1Address.substring(0, player1Address.indexOf(':'));
        NetworkPlayer one = new NetworkPlayer(player1Address, playerPort);
        //numberOfPlayers = 2;
        players = new NetworkPlayer[1];
        players[0] = one;
        syncWithPlayers();
    }

    /**
     * Creates a NetworkClient object for two network players
     * @throws IOException 
     * @throws UnknownHostException 
     * 
     **/
    public NetworkClient(String player0Address, String player1Address) throws UnknownHostException, IOException{
        //break playerAddresses into host names and port
        int playerPort = getPort(player0Address);      
        player0Address = player0Address.substring(0, player0Address.indexOf(':'));
        NetworkPlayer zero = new NetworkPlayer(player0Address, playerPort);
        playerPort = getPort(player1Address);  
        player1Address = player1Address.substring(0, player1Address.indexOf(':'));
        NetworkPlayer one = new NetworkPlayer(player1Address, playerPort);
        //numberOfPlayers = 2;
        players = new NetworkPlayer[2];
        players[0] = zero;
        players[1] = one;
        syncWithPlayers();

    }

    /**
     * Creates a NetworkClient object for one local human and three network players
     * @throws IOException 
     * @throws UnknownHostException 
     * 
     **/
    public NetworkClient(String player1Address, String player2Address, String player3Address) throws UnknownHostException, IOException{
        //break playerAddresses into hostnames and port

        int playerPort = getPort(player1Address);  
        player1Address = player1Address.substring(0, player1Address.indexOf(':'));
        NetworkPlayer one = new NetworkPlayer(player1Address, playerPort);

        playerPort = getPort(player2Address);
        player2Address = player2Address.substring(0, player2Address.indexOf(':'));
        NetworkPlayer two = new NetworkPlayer(player2Address, playerPort);

        playerPort = getPort(player1Address);      
        player3Address = player3Address.substring(0, player3Address.indexOf(':'));
        NetworkPlayer three = new NetworkPlayer(player1Address, playerPort);

        //numberOfPlayers = 4;
        players = new NetworkPlayer[4];
        players[0] = null;
        players[1] = one;
        players[2] = two;
        players[3] = three;
        syncWithPlayers();
    }
    /**
     * Creates a NetworkClient object for four players
     * @throws IOException 
     * @throws UnknownHostException 
     * 
     **/
    public NetworkClient(String player0Address, String player1Address, String player2Address, String player3Address) throws UnknownHostException, IOException{
        //break playerAddresses into hostnames and port

        String tempPort = player0Address.substring(player0Address.indexOf(':')+1, player0Address.length());
        int playerPort = getPort(player0Address);       
        player0Address = player0Address.substring(0, player0Address.indexOf(':'));
        NetworkPlayer zero = new NetworkPlayer(player0Address, playerPort);

        playerPort = getPort(player1Address); ;   
        player1Address = player1Address.substring(0, player1Address.indexOf(':'));
        NetworkPlayer one = new NetworkPlayer(player1Address, playerPort);

        playerPort = getPort(player2Address);       
        player2Address = player2Address.substring(0, player2Address.indexOf(':'));
        NetworkPlayer two = new NetworkPlayer (player2Address, playerPort);

        playerPort = getPort(player3Address);        
        player3Address = player3Address.substring(0, player3Address.indexOf(':'));
        NetworkPlayer three = new NetworkPlayer(player3Address, playerPort);

        players = new NetworkPlayer[4];
        players[0] = zero;
        players[1] = one;
        players[2] = two;
        players[3] = three;
        //numberOfPlayers = 4;
        syncWithPlayers();

    }//TODO: make player move

    /**
     * syncWithPlayers - sends initial message to each player, waits for a response
     * @throws UnknownHostException
     * @throws IOException
     */
    public void syncWithPlayers() throws UnknownHostException, IOException {
        //get input and output streams for all players' sockets, assign them to their respective fields
        for (int i = 0; i< players.length; i++){
            if(players[i] != null){
                players[i].playerSocket = new Socket(players[i].playerAddress, players[i].playerPort);
                players[i].outToPlayer = new PrintStream(players[i].playerSocket.getOutputStream());
                players[i].inFromPlayer = new Scanner(players[i].playerSocket.getInputStream());
            }
        }
        //initiate first contact
        for (int i = 0; i < players.length; i++){
            System.out.println("players.length: " + players.length + "loop i value: " + i);
            if(players[i] != null){
                players[i].outToPlayer.print("QUORIDOR " + players.length + " " + i +"\n");
                System.out.println("QUORIDOR " + players.length + " " + i +"\n");
            }
        }
        /**
         * 
         * 
         * 
         * DEBUG HERE SENDING MROE THAN ONE THING/
         * 
         * 
         * 
         */
        //listen for acknowledgment, get player name
        for (int i = 0; i < players.length; i++){
            if(players[i] != null){
                String fromPlayer = players[i].inFromPlayer.nextLine();
                if (!fromPlayer.contains("READY")){
                    System.err.println("Unexpected response from player " + i);
                }else{
                    players[i].displayName = fromPlayer.substring(6);
                    System.out.println("Player " + i + " says: " + fromPlayer);
                }
            }
        }




    }

    /**
     * tells a player that it has been kicked from the game
     * if that player has made an illegal move
     * @param playerID
     * @throws IOException 
     */
    public static void removePlayer(int playerID) throws IOException{
        System.out.println("We're in remove player");
        for (int i = 0; i < players.length; i++){
            players[i].outToPlayer.print("REMOVED " + (playerID-1) + "\n"); 
        }
        players[playerID-1].playerSocket.close();
        players[playerID-1] = null;
        System.out.println("Broadcasting REMOVED for player: " + (playerID-1));
    }

    /**
     * broadcastMove: broadcasts the last legal move made to all players
     * @param nextMove: the last legal move made
     */
    public static void broadcastMove(String nextMove) { 

        //TODO: test this with multiple players
        
        for (int i = 0; i < players.length; i++){
            System.out.println("NetworkClient> Broadcasting move to player " + i);
            if(players[i]!= null)
                players[i].outToPlayer.print(nextMove+"\n");
        }

    }


    /**
     * getMove(): this method gets a move from the player whose turn it is
     * @return: returns the move taken from the player
     */
    public static String getMove(int player){
        System.out.println("NetworkClient> asking player 1 for a move");
        players[player-1].outToPlayer.print("MOVE?\n");
        System.out.println("NetworkClient> request sent, waiting for response");
        String fromPlayer = players[player-1].inFromPlayer.nextLine();
        System.out.println("NetworkClient> Player " + (player-1) + " has responded with a move: "+
                fromPlayer);
        //TODO: figure out where you want to check if this is a legal move, probably in PlayQuor 
        return fromPlayer;
    }

    /**
     * closes all sockets
     * @throws IOException
     */

    public void kill() throws IOException{
        for (int i = 0; i < players.length; i++){
            if (players[i] != null){
                players[i].playerSocket.close();
            }
        }
    }

    private int getPort(String address){
        return (Integer.parseInt(address.substring(address.indexOf(':')+1, address.length())));
    }

}
