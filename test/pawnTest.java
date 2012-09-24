/**
 * @author Lee Page
 * @course CIS 405
 * Quoridor Project
 * pawnTest.java - to test the pawn object
 */
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pawn.Pawn;


public class pawnTest {
	
	public final static String PAWN_NAME = "BillyBobThornton";
	public final static String DEFAULT_STARTING_LOCATION = "E9";
	
	private Pawn pawn;
	
	  @Before
	  public void initialize() {
	    pawn = new Pawn(PAWN_NAME, "E9");
	  }  
	
	@Test
	public void checkPawnName() {
		testResults(pawn.getPlayerName(), PAWN_NAME);
	}
	@Test
	public void checkPawnLocation(){
		testResults(pawn.getLocation(), DEFAULT_STARTING_LOCATION);
	}
	
	private void testResults(String actual, String expected){
	    assertEquals(actual, expected);
	}

}
