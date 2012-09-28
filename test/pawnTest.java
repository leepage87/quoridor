/**
 * @author Lee Page
 * @course CIS 405
 * Quoridor Project
 * pawnTest.java - to test the pawn object
 */

package test;

import static org.junit.Assert.*;
import src.pawn.*;
import org.junit.Before;
import org.junit.Test;

public class pawnTest {
	
	public final static String PAWN_NAME = "BillyBobThornton";
	public final static String DEFAULT_STARTING_LOCATION = "E9";
	
	private Pawn pawn;
	
	  @Before
	  public void initialize() {
	    pawn = new Pawn(PAWN_NAME, "E9");
	  }  
	
	@Test
	public void checkPawnNameTest() {
		testResults(pawn.getPlayerName(), PAWN_NAME);
	}
	@Test
	public void checkPawnLocationTest(){
		testResults(pawn.getLocation(), DEFAULT_STARTING_LOCATION);
	}
	
	@Test
	public void movePawnLocationTest() {
		pawn.move("E8");
		testResults(pawn.getLocation(), "E8");
	}
	
	private void testResults(String actual, String expected){
	    assertEquals(actual, expected);
	}

}
