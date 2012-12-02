package src.network;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class NetworkObserver {
    public String playerAddress;
    public int playerPort;
    public Socket playerSocket;
    public PrintStream outToPlayer;
    public Scanner inFromPlayer;
    public String displayName;
    
    
    /**
     * creates a NetworkPlayer object to hold network information 
     * @param address hostname or IP address of network player
     * @param port port of network player
     */
    public NetworkObserver(String address, int port){
        this.playerAddress = address;
        this.playerPort = port;
    }
}
