/** 
 * @author: Lee Page
 * teamOrangeBeard
 * Observer
 * CIS 405 Software Engineering 
 * Quoridor Project
 **/

package src.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.sql.Timestamp;
import java.util.Date;
/**
 * 
 * Multi-threaded server, listens on default port 4051 or args[0] and
 * records the progress of a game of Quoridor
 */
public class Observer {
    private static int DEFAULT_SERVER_LISTEN_PORT = 4051;
    Socket connection;

    public Observer(Socket conn) {
        this.connection = conn;
    }
    /**
     * Waits for lines from a game of quoridor until WINNER or DRAW is received
     * prints each line to ObserverLog.txt and standard output
     * @throws IOException
     */
    public void run() throws IOException{
        Scanner in = new Scanner(connection.getInputStream());
        String received = "";
        PrintStream out = new PrintStream(new File("ObserverLog.txt"));
        try{
            do{
                received = in.nextLine();
                out.println("["+new Timestamp(System.currentTimeMillis()) + "] " + received);
                System.out.println("["+new Timestamp(System.currentTimeMillis()) + "] " + received);
            }while (!received.contains("WINNER") && !received.contains("DRAW"));
        }catch(Exception e){
            System.err.println("ERROR: Next line not found!");
        }
        out.close();
        in.close();
        connection.close();
    }

    /**
     * This is a multi-threaded observer server, it will never stop
     * running until the process is killed
     * @param args specify which port to run on, if none is specified, the default port of 4051 is used
     * @throws Exception
     */
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
