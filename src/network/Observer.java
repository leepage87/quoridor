package src.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;



/**
 * 
 * 
 * 
 * 
 * TODO: IMPLEMENT "DRAW" ENDING IN PLAYQUOR AND MOVESERVERS
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 *
 */
public class Observer {
    private static int DEFAULT_SERVER_LISTEN_PORT = 4051;
    Socket connection;
    
    public Observer(Socket conn) {
        this.connection = conn;
    }
    
    public void run() throws IOException{
        Scanner in = new Scanner(connection.getInputStream());
        String received = "";
        PrintStream out = new PrintStream(new File("ObserverLog.txt"));
        
        do{
           received = in.nextLine();
           out.println(received);
           System.err.println("OBSOIVAH>" + received);
           
            
        }while (!received.contains("WINNER"));
        out.close();
        connection.close();
    }
    
    
    public static void main(String [] args) throws Exception {
        ServerSocket welcomeSocket;
        if(args.length == 0){
            welcomeSocket = new ServerSocket(DEFAULT_SERVER_LISTEN_PORT);
            System.out.println("Observer> Listening on port: " + DEFAULT_SERVER_LISTEN_PORT);
        }else{
            welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Observer> Listening on port: " + args[0]);
        }
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            new Observer(connectionSocket).run();
        }
    }
}
