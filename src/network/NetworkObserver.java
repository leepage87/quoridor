/** 
 * @author: Lee Page
 * teamOrangeBeard
 * NetworkObserver
 * CIS 405 Software Engineering 
 * Quoridor Project
 **/

package src.network;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class NetworkObserver {

    private Socket playerSocket;
    private PrintStream outToObserver;
    
    
    /**
     * creates a NetworkPlayer object to hold network information 
     * @param address hostname or IP address of network player
     * @param port port of network player
     */
    public NetworkObserver(Socket playerSocket, PrintStream outToObserver){
        this.playerSocket = playerSocket;
        this.outToObserver = outToObserver;
    }
    /**
     * Sends a message to the observer
     * @param message the message to be sent
     */
    public void send(String message){
        outToObserver.print(message);
    }
    
    
    /**
     * Closes the socket
     * @throws IOException
     */
    public void kill() throws IOException{
        playerSocket.close();
    }
}
