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
        network = new NetworkClient("localhost", "localhost");


    }


    @Test
    public void testNetworkExists(){
        assertNotNull(network);
    }


    @Test
    public void testAddressesOfMoveServerZero() {
        testResults(network.player0Address, "localhost");
    }
    @Test
    public void testAddressesOfMoveServerOne() {
        testResults(network.player1Address, "localhost");
    }
    @Test
    public void testSynchroniztionWithPlayers() throws UnknownHostException, IOException {
        network.syncWithPlayers();
        testResults(network.fromPlayer, "HI");
    }

    @Test
    public void getMoveFromPlayerZero() {
        String expectedMove = "MOVE! 0 M 1 1 1 1";
        testResults(network.getMove(1), expectedMove);
    }
    @Test
    public void getMoveFromPlayerOne() {
        String expectedMove = "MOVE! 1 M 1 1 1 1";
        testResults(network.getMove(2), expectedMove);
    }

    @Test
    public void kickPlayer1AndGetMoveFromPlayer0() {
        network.removePlayer(1);
        String expectedMove = "MOVE! 0 M 1 1 1 1";
        testResults(network.getMove(1), expectedMove);
    }
    
    private void testResults(String actual, String expected){
        assertEquals(actual, expected);
    }

    @AfterClass
    public static void tearDown() throws IOException{
        network.kill();
    }

}
