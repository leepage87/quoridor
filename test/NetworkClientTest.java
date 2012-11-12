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


public class NetworkClientTest {
    private static NetworkClient network;
    @Before
    public void setUp() throws Exception {
        network = new NetworkClient("localhost:4050", "localhost:4050");

    }

    /*
    @Test
    public void testNetworkExists(){
        assertNotNull(network);
    }


    @Test
    public void testAddressesOfMoveServerZero() {
        testResults("localhost",NetworkClient.players[0].playerAddress);
    }
    @Test
    public void testAddressesOfMoveServerOne() {
        testResults("localhost",NetworkClient.players[1].playerAddress);
    }
     */
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



    private void testResults(String actual, String expected){
        assertEquals(actual, expected);
    }

    @AfterClass
    public static void tearDown() throws IOException{
        network.kill();
    }

}
