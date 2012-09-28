package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import src.ui.Board; 

public class BoardTester {
	
	static int grid[][]; 
	final static int P1 = 1;
	final static int P2 = 2;
	static char direction;
	
	@Before
	public void initialize(){
		grid = new int[17][17];
	}
	
	@Test
	public void checkNorth(){
		Board.createBoard(2);
		direction = 'N';
		Board.movePiece(direction, P2);
		int[] expected = new int[2];
		expected[0]=14;
		expected[1]=8;
		testResults(Board.playerPlace(P2), expected);
		//resetPosition();
	}
	@Test
	public void checkSouth(){
		Board.createBoard(2);
		direction = 'S';
		Board.movePiece(direction, P1);
		int[] expected = new int[2];
		expected[0]=2;
		expected[1]=8;
		testResults(Board.playerPlace(P1), expected);
		//resetPosition();
	}
	@Test
	public void checkEast(){
		Board.createBoard(2);
		direction = 'E';
		Board.movePiece(direction, P1);
		int[] expected = new int[2];
		expected[0]=0;
		expected[1]=10;
		testResults(Board.playerPlace(P1), expected);
		//resetPosition();
	}
	@Test
	public void checkWest(){
		Board.createBoard(2);
		direction = 'W';
		Board.movePiece(direction, P1);
		int[] expected = new int[2];
		expected[0]=0;
		expected[1]=6;
		testResults(Board.playerPlace(P1), expected);
		//resetPosition();
	}
	
	public void testResults(int[] actual, int[] expected){
		Assert.assertArrayEquals(expected, actual);
	}
	
	public void checkWall(){
		//Wall is not written yet**
	}
	
	public void falseCheckWin(){
		for (int i =0; i<16; i++){
			grid[0][i] = 0;
			grid[16][i] = 0;
			grid[i][0] = 0;
			grid[i][16] =0;
		}
		checkWinning(Board.winCheck(), false);
	}
	
	public void trueCheckWin(){
		grid[16][0] = P1;
		checkWinning(Board.winCheck(), true);
	}

	public void checkWinning(boolean actual, boolean expected){
		
	}
	public void checkMovementOrder(){
		//checks to make the moving player is moving on his turn
	}
	
	public void checkLegalWall(){
		//checks wall placement is legal
	}
}
