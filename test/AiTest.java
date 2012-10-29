package test;
import java.util.ArrayList;

import src.ui.AI;
import src.ui.Board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AiTest {
		
		Board b;
		AI AIboard; 
		AI temp;
	
		//initializes the AI for testing
		@Before
		public void initialize(){
			b = new Board(2);
			AIboard = new AI(b);
			temp = new AI(b);
		}

		//Tests to see if the currentLoc method returns the correct location
		@Test
		public void currentLocTest(){
			int[]expected = new int[2];
			expected[0] = 8;
			expected[1] = 0;
			Assert.assertArrayEquals(expected, AIboard.currentLoc(1));
			
		}
		
		//Tests the moves methos to make sure it uses the doSearch correctly and finds the correct moevment
		@Test
		public void movesTest(){
			int [] expected = new int[3];
			expected[0] = 8;
			expected[1] = 2;
			//TODO fix expected[2]'s count
			expected[2] = 8;
			int [] actual = new int[3];
			actual = AIboard.moves(AIboard.currentLoc(1), 1);
			Assert.assertArrayEquals(expected, actual);
			
		}

		//Tests the boardValue method to make sure the starting position board value is 0
		@Test
		public void boardValueTest(){
			int given;
			given = AIboard.boardValue(1, 2, b);
			Assert.assertTrue(given == 0);
		}
		
		//This is not working yet need to move a piece before it will be different
		@Test
		public void boardMoveValueTest(){
			int given;
			AIboard.move();
			int[] printer = new int[2];
			printer = AIboard.currentLoc(1);
			System.out.println(printer[0]);
			System.out.println(printer[1]);
			given = AIboard.boardValue(1, 2, b);
			System.out.println(given);
			Assert.assertTrue(given == 1);
		}
		
		//Tests the findEnemy method to make sure it finds the correct enemy by using its location
		@Test
		public void findEnemyTest(){
			int test=0;
			test = AIboard.findEnemy(1, b);
			int[] actual = new int[2];
			int [] given = new int[2];
			AIboard.currentLoc(2);
			AIboard.currentLoc(test);
			Assert.assertArrayEquals(given, actual);
		}
		//May be a null test that isn't needed
		/*@Test
		public void oneStepTestNorth(){
			temp = new AI(AIboard.oneStep(1, 1, b));
			int [] given = new int[2];
			given = temp.currentLoc(1);
			int [] test = new int[2];
			test[0] = 8;
			test[1] = 2;
			Assert.assertArrayEquals(given, test);
		}
*/
		//Tests the wallPlacementSearch to make sure it comes up with 
		//the correct amount of different boards where the walls can be placed
		@Test
		public void wallPlacementSearchTest(){
			ArrayList<Board> testMoves = new ArrayList<Board>();
			testMoves = AIboard.wallPlacementSearch(b);
			int sizeArray = testMoves.size();
			Assert.assertTrue(sizeArray==128);
			
		}
		
		@Test
		public void placeWallTest(){
			//TODO get this working
			AIboard.placeWall();
			ArrayList<Board> testMoves = new ArrayList<Board>();
			testMoves = AIboard.wallPlacementSearch(b);
			int sizeArray = testMoves.size();
			System.out.println(sizeArray);
			Assert.assertTrue(sizeArray==124);
			
		
		}
}
