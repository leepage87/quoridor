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


    @Test
    public void testNetworkExists(){
        assertNotNull(network);
    }


    @Test
    public void testAddressesOfMoveServerZero() {
        testResults("localhost",NetworkClient.player0Address);
    }
    @Test
    public void testAddressesOfMoveServerOne() {
        testResults("localhost",NetworkClient.player1Address);
    }
    @Test
    public void testSynchroniztionWithPlayers() throws UnknownHostException, IOException {
        network.syncWithPlayers();
        testResults("READY 1",NetworkClient.fromPlayer);
    }

    @Test
    public void getMoveFromPlayerZero() {
        String expectedMove = "MOVE M (1, 1) (1, 1)";
        testResults(expectedMove, NetworkClient.getMove(1));
    }
    @Test
    public void getMoveFromPlayerOne() {
        String expectedMove = "MOVE M (1, 1) (1, 1)";
        testResults( expectedMove, NetworkClient.getMove(2));
    }

    @Test
    public void kickPlayer1AndGetMoveFromPlayer0() {
        NetworkClient.removePlayer(1);
        String expectedMove = "MOVE M (1, 1) (1, 1)";
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
