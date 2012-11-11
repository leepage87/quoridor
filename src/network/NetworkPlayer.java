package src.network;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class NetworkPlayer {
    public String playerAddress;
    public int playerPort;
    public Socket playerSocket;
    public PrintStream outToPlayer;
    public Scanner inFromPlayer;
    public String displayName;
    
    public NetworkPlayer(String address, int port){
        this.playerAddress = address;
        this.playerPort = port;
    }

}
