package src.network;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class NetworkObserver {

    public Socket playerSocket;
    public PrintStream outToObserver;
    public Scanner inFromPlayer;
    
    
    /**
     * creates a NetworkPlayer object to hold network information 
     * @param address hostname or IP address of network player
     * @param port port of network player
     */
    public NetworkObserver(Socket playerSocket, PrintStream outToObserver, Scanner inFromPlayer){
        this.playerSocket = playerSocket;
        this.outToObserver = outToObserver;
        this.inFromPlayer = inFromPlayer;
    }
}
