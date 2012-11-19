/** 
 * @author: Lee Page - teamOrangeBeard
 * MoveServer
 * CIS 405 Software Engineering 
 * Quoridor Project
 * Runs on default port 4050 or args input, listens for connections
 * delivers moves from AI to to the connected client 
 **/
package src.network;

import java.io.*;
//import src.ui.Board;

import java.net.*;
import java.util.*;

import src.main.AI;
import src.main.Board;



public class MoveServer extends Thread {
    int playerNo;
    int numberOfPlayers;
    final static int DEFAULT_SERVER_LISTEN_PORT = 4050;//if user doesn't input port
    public Board b;
    Socket connection;
    private AI ai;
    private String nextMove;
    private long tID;
    /**
     * constructor
     * @param conn the connection with network client
     */
    public MoveServer(Socket conn) {
        this.connection = conn;

    }
    /**
     * the multithreaded move server run method
     */
    public void run() {

        tID = Thread.currentThread().getId();
        System.err.println("ID: "+ tID + " top of run()");
        PrintStream outToClient = null;
        Scanner inFromClient = null;

        String fromGameClient;

        try {
            inFromClient = new Scanner(connection.getInputStream());
            outToClient = new PrintStream(connection.getOutputStream());

            fromGameClient = inFromClient.nextLine();
            Scanner fromGameClientScanner = new Scanner (fromGameClient);
            System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo + "> line rec'd from client " + fromGameClient);
            if (fromGameClientScanner.next().equals("QUORIDOR")){
                //"QUORIDOR <NumberOfPlayers> <playerNo>"
                //get playerNo and NumberOfPlayers
                numberOfPlayers = fromGameClientScanner.nextInt();
                playerNo = fromGameClientScanner.nextInt();
                // Acknowledge connection from gameDisplay
                outToClient.println("READY " + playerNo);

                b = new Board(numberOfPlayers);
                ai = new AI (b);
                System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo + "> I am player number: " + playerNo + " and there are " + numberOfPlayers + " playing.");
            }else{
                System.err.println("ID: "+ tID + " Protocol not followed");
            }


            do {
                System.err.println("ID: "+ tID + " top of do/while");

                fromGameClient = inFromClient.nextLine();

                if (fromGameClient.contains("MOVE?")){

                    System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo +
                    "> Received 'MOVE?' from client, issuing move");
                    //get move, send to client
                    nextMove = getMove(playerNo, b, ai);
                    outToClient.println(nextMove);

                    System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo +
                            "> Move sent was: " + nextMove);
                    //get response from client
                    fromGameClient = inFromClient.nextLine();
                    System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo +
                            "> rec'd: " + fromGameClient);


                    //MOVED <player-id> <op> <location-1> <location-2>

                    if(fromGameClient.contains("MOVED " + playerNo + nextMove.substring(4))){
                        //move has been accepted, so make teh move

                        b = move(fromGameClient, b);
                        System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo +"> move confirmed, making move:");
                        System.out.println(b.toString());


                    }else if (fromGameClient.contains("REMOVED")){
                        System.out.println("Inside 1st else/if REMOVED");
                        System.out.println("Player no is: " + playerNo + "vs char: " + fromGameClient.charAt(8));
                        if(playerNo == (fromGameClient.charAt(8)-'0')){
                            System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo + "> Move was illegal, you've been kicked out of game (Player " + (playerNo+1) +")" );
                            //connection.close();
                            //System.exit(0);
                        }else{//someone else was kicked
                            System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo + "> Someone else made an illegal move (Player " +
                                    (playerNo+1) +") has been kicked (1st removed test)" );
                            b = kick((fromGameClient.charAt(8) -'0' +1), b);
                            System.err.println("MoveServer " + "ID " + tID +" p:" +  playerNo);
                            System.err.println(b.toString());
                        }
                    }
                }else if (fromGameClient.substring(0,5).equals("MOVED")){
                    //server telling you to move a different player's piece 
                    System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo + "> a different player has moved!" );
                    System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo + ">******moved******");
                    b = move(fromGameClient, b);
                    System.err.println("MoveServer " + "ID " + tID +" p:" +  playerNo + " Opponent has just moved");
                    System.err.println(b.toString());
                }else if(fromGameClient.contains("REMOVED")){
                    //a different player has been removed  
                    System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo + "> Someone else made an illegal move");
                    System.err.println("MoveServer " + "ID " + tID +" p:" +  playerNo + "> charAt8 is: " + fromGameClient.charAt(8));
                    b = kick(fromGameClient.charAt(8)-'0' + 1, b);
                    System.err.println("MoveServer " + "ID " + tID +" p:" +  playerNo);
                    System.err.println(b.toString());
                }
                System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo + ">at bottom of while loop");
            }while (!fromGameClient.contains("WINNER") || !fromGameClient.contains("REMOVED " + playerNo));
            System.err.println("MoveServer " + "ID " + tID +" p:" +  playerNo + "is outside the do/while loop");

            //TODO: display losing or winning message
            if(fromGameClient.contains("WINNER"));
            System.out.println("Player " + fromGameClient.charAt(7) + " has won!");
            //connection.close();
            // System.exit(0);
        }catch (Exception e){

        }
    }    


    /**
     * gets an AI move to be delivered to the network client
     * @param PlayerNo
     * @param b a board containing current player and wall positions
     * @param ai the AI object that makes moves
     * @return the move made in network protocol format
     */
    private String getMove(int PlayerNo, Board b, AI ai){
        int rowOne, colOne, rowTwo, colTwo;
        char opCode;

        Board tempOldBoard = b;
        //record start location of player
        int[] startLocation = b.playerPlace(playerNo+1);
        //record final location of player
        Board tempNewBoard = ai.aiMove(playerNo+1);
        int [] endLocation = tempNewBoard.playerPlace(playerNo+1);
        System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo + " getMove returned board: ");
        System.out.println(tempNewBoard.toString());
        int[] wallLocation = ai.aiWall(playerNo, tempOldBoard, tempNewBoard);

        if(startLocation[0] == endLocation[0] && startLocation[1] == endLocation[1]){//its a wall

            opCode = 'W';
            System.out.println("MoveServer " + "ID " + tID +" p:" +  playerNo + "> printing wall center location");
            System.out.println(wallLocation[0] + " " + wallLocation[1] + " " + wallLocation[2] + "." );
            //a wall movement has been made
            //this is the center spot
            if (wallLocation[2] == 0){
                //its a horizontal wall
                rowOne = wallLocation[1]+1;
                colOne = wallLocation[0];
                rowTwo = wallLocation[1]+3;
                colTwo = wallLocation[0];
            }else{//its a vertical wall
                rowOne = wallLocation[1];
                colOne = wallLocation[0]+1;
                rowTwo = wallLocation[1]+2;
                colTwo = wallLocation[0]+1;
            }

            return ("MOVE "+ opCode + " ("+rowOne+", "
                    +colOne+") " + "("+rowTwo+", "+colTwo+")");
        }else{//its a player move



            System.out.println("Player has moved, start location: " + startLocation[0] + " " + startLocation[1]);
            System.out.println("end location: " + endLocation[0] + " " + endLocation[1]);

            opCode = 'M';
            rowOne = startLocation[1]/2;
            colOne = startLocation[0]/2;
            rowTwo = endLocation[1]/2;
            colTwo = endLocation[0]/2;

            return ("MOVE "+ opCode + " ("+rowOne+", "
                    +colOne+") " + "("+rowTwo+", "+colTwo+")");
        }
    }

    /**
     * Moves a player after receiving a MOVED line from the client
     * @param frago the line received from the client
     * @param moveBoard the board's current status
     * @return returns an updated board with the new move
     */


    private Board move(String frago, Board moveBoard){
        //fragmentary order: MOVED P M (R, C) (R, C)
        int player = frago.charAt(5);
        int[] move = new int[3];
        if(frago.charAt(8)== 'M'){
            move [0] = (frago.charAt(21)-'0')*2;
            move [1] = (frago.charAt(18)-'0')*2;
            moveBoard.playerPlace(frago.charAt(6)-'0');
            System.out.println("Move[] " + move[0] + move[1]);
            moveBoard.quickMove(move, ((frago.charAt(6)-'0')+1));
        }else{//its a wall placement
            System.err.println("We're placing a wall");
            if(frago.charAt(11) == frago.charAt(18)){//horizontal wall
                if((frago.charAt(14)-'0') != 0){
                    move[0] = (frago.charAt(14)-'0')-1;
                }else{
                    move[0] = (frago.charAt(14)-'0');
                }
                move[1] = (frago.charAt(11)-'0');
                move[2] = 1;
                moveBoard.placeWallBoard(move, player);
            }else{//vertical wall
                move[0] = (frago.charAt(14)-'0');
                if((frago.charAt(11)-'0') != 0){
                    move[1] = (frago.charAt(11)-'0')-1;
                }else{
                    move[1] = (frago.charAt(11)-'0'); 
                }
                move[2] = 0;
                moveBoard.placeWallBoard(move, player);
            }

        }
        return moveBoard;

    }

    /**
     * Kicks a player from the game
     * @param player which player is kicked
     * @param kickBoard the board the player is on
     * @return the board after the player has been removed
     */
    private Board kick(int player, Board kickBoard){
        int[] location2 = kickBoard.playerPlace(1);
        System.err.println("MoveServer " + "ID " + tID +" p:" +  playerNo + "> player place test search for 1: " + location2[0] + location2[1]);

        System.err.println("MoveServer " + "ID " + tID +" p:" +  playerNo + "> inside kick, kicking player: " + player);
        System.err.println("MoveServer " + "ID " + tID +" p:" +  playerNo);
        System.err.println(kickBoard.toString());
        int[] location = kickBoard.playerPlace(player);
        System.err.println("MoveServer " + "ID " + tID +" p:" +  playerNo + "> player place: " + location[0] + location[1]);
        kickBoard.grid[location[0]] [location[1]] = 0;
        System.err.println("MoveServer " + "ID " + tID +" p:" +  playerNo);
        System.err.println(kickBoard.toString());
        return kickBoard;
    }
    /**
     * awaits an incoming connection then calls the run method
     * @param args which port to listen on, if none is specified a default of 4050 is used
     * @throws Exception
     */
    public static void main(String [] args) throws Exception {
        ServerSocket welcomeSocket;
        if(args.length == 0){
            welcomeSocket = new ServerSocket(DEFAULT_SERVER_LISTEN_PORT);
            System.out.println("MoveServer> Listening on port: " + DEFAULT_SERVER_LISTEN_PORT);
        }else{
            welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("MoveServer> Listening on port: " + args[0]);
        }



        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            new MoveServer(connectionSocket).start();
        }
    }
}
