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
import src.network.NetworkObserver;



public class ObserverTest {
    private static NetworkObserver observer;
    private static NetworkClient network;
    @Before
    public void setUp() throws UnknownHostException, IOException{
        //observer's default port is 4051
        network = new NetworkClient();
        observer = new NetworkObserver("localhost", 4051, //Scanner);
    }


        @Test
        public void testObserverReceive(){
            NetworkClient.broadcast("TEST!");
        }
 
}