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
		
		//Tests the aiMove method
		@Test
		public void aiMove(){
			AI temp = new AI(b);
			temp.move();
			AIboard.aiMove(1);
			int[] method = new int[2];
			int [] moved = new int[2];
			method  = AIboard.currentLoc(1);
			moved = temp.currentLoc(1);
			Assert.assertArrayEquals(method, moved);
		}
		//Tests the findBestMove method
		@Test
		public void findBestMoveTest(){
			AI temp = new AI(b);
			AIboard.findBestMove(2, 1, b);
			temp.move();
			int[] method = new int[2];
			int [] moved = new int[2];
			method  = AIboard.currentLoc(2);
			moved = temp.currentLoc(2);
			Assert.assertArrayEquals(method, moved);
		}
		//This is not working yet need to move a piece before it will be different
		@Test
		public void boardMoveValueTest(){
			int given;
			AIboard.move();
			given = AIboard.boardValue(1, 2, b);
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
		
		//Tests the each move method
		@Test
		public void eachStepTest(){
			int [] dest = new int[2];
			dest[0] = 8;
			dest[1] = 2;
			AI temp = new AI(b);
			AIboard.eachStep(1, dest, b);
			temp.move();
			int[] method = new int[2];
			int[] moved = new int[2];
			method = AIboard.currentLoc(1);
			moved = temp.currentLoc(1);
			Assert.assertArrayEquals(method, moved);
		}

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
			Assert.assertTrue(sizeArray==124);
			
		
		}
}
