package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import src.network.MoveServer;
import src.network.NetworkClient;


public class NetworkTest {
    private static NetworkClient network;
    @Before
    public void setUp() throws Exception {
        network = new NetworkClient(2, "localhost:4050 localhost:4050", 1, "localhost:4051");
        //ensure moveServer and observer are running for connection to work
    }

    
    @Test
    public void testNetworkExists(){
        assertNotNull(network);
    }


    @Test
    public void testNameOfMoveServerZero() {
        testResults("OrangeBeard1",NetworkClient.getNetworkPlayer(0).displayName);
    }

    /*
       
    @Test
    public void getMoveFromPlayerZero() {
        String expectedMove = "MOVE M (0, 4) (1, 4)";

        String receivedMove = NetworkClient.getMove(1);
        System.out.println("getMoveFromPlayerZero: " + receivedMove);
        //accept the move
        network.broadcast("MOVED 0" + expectedMove.substring(4));
        testResults(expectedMove, receivedMove);
        

    }
    @Test
    public void getMoveFromPlayerOne() {
        String expectedMove = "MOVE M (8, 4) (7, 4)";
        String receivedMove = NetworkClient.getMove(2);
        //accept the move
        network.broadcast("MOVED 1" + expectedMove.substring(4));
        testResults( expectedMove, NetworkClient.getMove(2));
    }

    @Test
    public void getMoveFromPlayerOneandTwo() {

        String receivedMove = NetworkClient.getMove(1);
        System.out.println("getMoveFromPlayerZero: " + receivedMove);
        //accept the move
        network.broadcast("MOVED 0" + receivedMove.substring(4));
        String expectedMove = "MOVE M (8, 4) (7, 4)";
        System.out.println("asking player 1 for a move");
        receivedMove = NetworkClient.getMove(2);
        System.out.println("getMoveFromPlayerOne: " + receivedMove);
        //accept the move
        network.broadcast("MOVED 1" + receivedMove.substring(4));
        testResults( expectedMove, NetworkClient.getMove(2));
    }

    @Test
    public void kickPlayer1AndGetMoveFromPlayer0() throws IOException {
        NetworkClient.getMove(2);
        NetworkClient.removePlayer(2);
        String expectedMove = "MOVE M (0, 4) (1, 4)";
        testResults(expectedMove,NetworkClient.getMove(1)) ;
    }


    @Test
    public void getAWholeBunchOfMoves() {
        String receivedMove;
        for(int i = 0; i <=99 ; i++){
            receivedMove = NetworkClient.getMove(i%2+1);
            System.out.println("getMove: " + receivedMove);
            network.broadcast("MOVED " + (i%2) + receivedMove.substring(4));
            //TODO: check indexing for playquor vs network
        }
        
    }*/
    
    private void testResults(String actual, String expected){
        assertEquals(actual, expected);
    }

    @AfterClass
    public static void tearDown() throws IOException{
        network.kill();
    }

}
