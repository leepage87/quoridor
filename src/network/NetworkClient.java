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


import src.main.Board;
import src.main.PlayQuor;
import src.ui.BoardButton;
import src.ui.GameBoardWithButtons;


public class NetworkClient {

    final static int REMOTE_PORT = 4050;
    private static NetworkPlayer[] players;
    private static NetworkObserver[] observers;

    /**
     * default constructor
     */
    public NetworkClient(){

    }

    /**
     * Constructs a network client object then adds the required players and observers
     * @param players the number of network players
     * @param moveAddresses the addresses of the move servers
     * @param numObservers the number of observers
     * @param observerAddress the addresses of the observers
     * @throws IOException
     */
    public NetworkClient(int players, String moveAddresses, int numObservers, String observerAddress) throws IOException{
        addPlayers(players, moveAddresses);
        syncWithPlayers();
        if(numObservers > 0){
            addObserver(numObservers, observerAddress);
            observerBroadcast("WATCH " + players);
        }
        
    }

    /**
     * Creates sockets, printStreams, and scanners for each network player
     * @param numPlayers
     * @param addresses
     * @throws IOException
     */
    private void addPlayers(int numPlayers, String addresses) throws IOException{
        players = new NetworkPlayer[numPlayers];
        Scanner sc = new Scanner(addresses);
        String addressWithoutPort;
        int port;
        Socket tempSock;
        PrintStream tempPS;
        Scanner tempScan;
        for (int i = 0; i < numPlayers; i++){
            String tempAddress = sc.next();
            addressWithoutPort = tempAddress.substring(0, tempAddress.indexOf(':'));
            port = getPort(tempAddress);
            tempSock = new Socket(addressWithoutPort, port);
            tempPS = new PrintStream(tempSock.getOutputStream());
            tempScan = new Scanner(tempSock.getInputStream());
            players[i] = new NetworkPlayer(tempSock,tempPS,tempScan);
        }
        sc.close();
    }
    
    /**
     * Adds NetworkObserver objects to an array for reference while playing
     * @param numObservers number of observers observing
     * @param addresses the addresses of all the observers
     * @throws IOException 
     */
    public void addObserver(int numObservers, String addresses) throws IOException{
        observers = new NetworkObserver[numObservers];
        Scanner sc = new Scanner(addresses);
        String addressWithoutPort;
        int port;
        Socket tempSock;
        PrintStream tempPS;
        for (int i = 0; i < numObservers; i++){
            String tempAddress = sc.next();
            addressWithoutPort = tempAddress.substring(0, tempAddress.indexOf(':'));
            port = getPort(tempAddress);
            tempSock = new Socket(addressWithoutPort, port);
            tempPS = new PrintStream(tempSock.getOutputStream());
            observers[i] = new NetworkObserver(tempSock,tempPS);
        }
        sc.close();
    }

    /**
     * Sends initial message to each player, waits for a response
     * @throws UnknownHostException
     * @throws IOException
     */
    private void syncWithPlayers() throws IOException{
        for (int i = 0; i< players.length; i++){
            if(players[i] != null){

                //syn with player
                players[i].send("QUORIDOR " + players.length + " " + i +"\n");

                //await player ack
                String fromPlayer = players[i].receive();
                //if the ack is something out of protocol, kick the player
                if (!fromPlayer.contains("READY")){
                    System.err.println("ERROR: Response from player " + i + " does not follow protocol!");
                    removePlayer(i);
                    PlayQuor.removeNetworkPlayer(i);

                }else{//protocol has been followed
                    //set the network player's name
                    players[i].displayName = fromPlayer.substring(6);
                }
            }
        }
    }

    /**
     * Tells a player that it has been kicked from the game
     * if that player has made an illegal move
     * @param playerID
     * @throws IOException 
     */
    public static void removePlayer(int playerID) throws IOException{
        broadcast("REMOVED " + (playerID-1));
        players[playerID-1].kill();
        players[playerID-1] = null;

    }

    /**
     * Broadcasts a message to all players
     * @param message the message to be broadcast
     */
    public static void broadcast(String message) { 
        //broadcast the message to players
        for (int i = 0; i < players.length; i++){
            if(players[i]!= null){
                players[i].send(message+"\n");
            }
        }
        //broadcast the message to observers
       observerBroadcast(message);
    }
    /**
     * Broadcasts a massage to observers
     * @param message the message to be broadcast
     */
    private static void observerBroadcast(String message){
        if(observers != null){
            for (int i = 0; i < observers.length; i++){
                    observers[i].send(message+"\n");
            }
        }
    }


    /**
     * Bets a move from the player whose turn it is
     * @return returns the move taken from the player
     */
    public static String getMove(int player){
        players[player-1].send("MOVE?\n");
        return players[player-1].receive();
    }

    /**
     * Closes all sockets
     * @throws IOException
     */
    public void kill() throws IOException{
        for (int i = 0; i < players.length; i++){
            if (players[i] != null){
                players[i].kill();
            }
        }
        if(observers != null){
            for (int i = 0; i < observers.length; i++){
                if(observers[i]!= null)
                    observers[i].kill();
            }
        }
    }
 
    /**
     * Gets the port from the given address
     * @param address
     * @return the port associated with the given address
     */
    private int getPort(String address){
        return (Integer.parseInt(address.substring(address.indexOf(':')+1, address.length())));
    }
    
    /**
     * Returns the display name of the network player
     * @param turn which player to look at
     * @return the network player's display name
     */
    public static String getName(int turn){
        return players[turn-1].displayName;
    }
    
    /**
     * Returns the requested NetworkPlayer object
     * @param index the index location of the player to be returned
     * @return the NetworkPlayer
     */
    public static NetworkPlayer getNetworkPlayer(int index){
        return players[index];
    }
    
    /**
     * Returns the requested Observer object
     * @param index the index location of the observer to be returned
     * @return the observer
     */
    public static NetworkObserver getObserver(int index){
        return observers[index];
    }

}