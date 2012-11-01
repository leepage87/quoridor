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
        testResults("localhost",network.player0Address);
    }
    @Test
    public void testAddressesOfMoveServerOne() {
        testResults("localhost",network.player1Address);
    }
    @Test
    public void testSynchroniztionWithPlayers() throws UnknownHostException, IOException {
        network.syncWithPlayers();
        testResults("QUORIDOR",network.fromPlayer);
    }

    @Test
    public void getMoveFromPlayerZero() {
        String expectedMove = "MOVE 0 M 1 1 1 1";
        testResults(expectedMove, network.getMove(1));
    }
    @Test
    public void getMoveFromPlayerOne() {
        String expectedMove = "MOVE 1 M 1 1 1 1";
        testResults( expectedMove, network.getMove(2));
    }

    @Test
    public void kickPlayer1AndGetMoveFromPlayer0() {
        network.removePlayer(1);
        String expectedMove = "MOVE 0 M 1 1 1 1";
        testResults(expectedMove,network.getMove(1)) ;
    }
    

    
    private void testResults(String actual, String expected){
        assertEquals(actual, expected);
    }

    @AfterClass
    public static void tearDown() throws IOException{
        network.kill();
    }

}
