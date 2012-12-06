/** 
 * @author: Lee Page
 * teamOrangeBeard
 * NetworkPlayer
 * CIS 405 Software Engineering 
 * Quoridor Project
 **/

package src.network;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
/**
 * Creates a network player object to hold socket, printStream,
 * and scanner information for each player
 * 
 */
public class NetworkPlayer {

    private Socket playerSocket;
    private PrintStream outToPlayer;
    private Scanner inFromPlayer;
    public String displayName;


    /**
     * Constructor
     * @param tempSock the playerSocket
     * @param tempPS the player's printStream
     * @param tempScan the player's scanner
     */
    public NetworkPlayer(Socket tempSock, PrintStream tempPS, Scanner tempScan) {
        this.playerSocket = tempSock;
        this.outToPlayer = tempPS;
        this.inFromPlayer = tempScan;
    }
    /**
     * Sends a message to whoever is connected via printStream
     * @param message the message to be sent
     */
    public void send(String message){
        outToPlayer.print(message);
    }
    
    /**
     * receives a message on the scanner
     * @return the message received
     */
    public String receive(){
        return inFromPlayer.nextLine();
    }
    
    /**
     * Closes the socket
     * @throws IOException
     */
    public void kill() throws IOException{
        playerSocket.close();
    }


}
