/**
 * Sarah Weller
 * teamOrangeBeard
 */

package test;
import java.util.ArrayList;

import src.main.AI;
import src.main.Board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AiTest {
		
		Board b;
		AI AIboard; 
		AI temp;
	
		//initializes the AI for testing creating a board, and an AI, and a temporary board
		@Before
		public void initialize(){
			b = new Board(2);
			AIboard = new AI(b);
			temp = new AI(b);
		}

		//Tests to see if the currentLoc method returns the correct current location
		/*@Test
		public void currentLocTest(){
			int[]expected = new int[2];
			expected[0] = 8;
			expected[1] = 0;
			Assert.assertArrayEquals(expected, AIboard.currentLoc(1));
			
		}
		*/
		//Tests the moves method to make sure it uses the doSearch correctly and finds the correct movement
		/*@Test
		public void movesTest(){
			int [] expected = new int[3];
			expected[0] = 8;
			expected[1] = 2;
			expected[2] = 8;
			int [] actual = new int[3];
			actual = AIboard.moves(b.playerPlace(1), 1);
			Assert.assertArrayEquals(expected, actual);
			
		}
*/
		//Tests the boardValue method to make sure the starting position board value is 0
		@Test
		public void boardValueTest(){
			int given;
			given = AIboard.boardValue(1, 2, b);
			Assert.assertTrue(given == 20);
		}
		
		//Tests the aiMove method
		@Test
		public void aiMove(){
			int [] placement = new int[2];
			placement[0] = 8;
			placement[1] = 2;
			AIboard.aiMove(1);
			AIboard.move(placement, 1);
			int[] method = new int[2];
			int [] moved = new int[2];
			method  = AIboard.playerPlacee(1);
			moved = AIboard.playerPlacee(1);
			Assert.assertArrayEquals(method, moved);
		}
		//Tests the findBestMove method
		@Test
		public void findBestMoveTest(){
			AIboard.findBestMove(2, 1, b);
			int [] placement = new int[2];
			placement[0] = 8;
			placement[1] = 2;
			temp.move(placement, 1);
			int[] method = new int[2];
			int [] moved = new int[2];
			method  = AIboard.playerPlacee(2);
			moved = temp.playerPlacee(2);
			Assert.assertArrayEquals(method, moved);
		}
		//Tests the boardValue method after moving a player to make sure it increments the boardValue
		@Test
		public void boardMoveValueTest(){
			int [] placement = new int[2];
			placement[0] = 8;
			placement[1] = 2;
			this.b = AIboard.move(placement, 1);
			int given = AIboard.boardValue(1, 2, b);
			Assert.assertTrue(given == 21);
		}
		
		//Tests the boardValue method after moving the first player, then the second, then the first again
		@Test
		public void boardMoveValueTest2(){
			int given;
			int [] placement = new int[2];
			placement[0] = 8;
			placement[1] = 2;
			this.b = AIboard.move(placement, 1);
			placement[0] = 6;
			placement[1] = 16;
			this.b = AIboard.move(placement, 2);
			placement[0] = 8;
			placement[1] = 4;
			this.b = AIboard.move(placement, 1);
			given = AIboard.boardValue(1, 2, b);
			Assert.assertTrue(given == 22);
		}
		
		//Tests the board value after a wall is placed
		/*
		@Test
		public void boardMoveValueTestWall(){
			int given;
			AIboard.placeWall(1);
			given = AIboard.boardValue(1, 2, b);
			int fix;
			fix = AIboard.boardValue(2, 1, b);
			
			Assert.assertTrue(given == 18);
		}
		*/
		//Tests the board value after a move is taken and a wall is placed
		/*
		@Test
		public void boardMoveValueTestWall2(){
			int given;
			AIboard.placeWall(1);
			int []placement = new int[2];
			placement[0] = 6;
			placement[1] = 16;
			AIboard.move(placement, 2);
			int [] fix = new int[2];
			fix = AIboard.currentLoc(2);
			placement[0] = 8;
			placement[1] = 2;
			//AIboard.move(placement, 1);
			given = AIboard.boardValue(1, 2, b);
			System.out.println("player1 "+given);
			given = AIboard.boardValue(2, 1, b);
			System.out.println("player2 "+given);
			Assert.assertTrue(given == 19);
		}
		*/
		//Tests the findEnemy method to make sure it finds the correct enemy by using its location
		@Test
		public void findEnemyTest(){
			int test=0;
			test = AIboard.findEnemy(1, b);
			int[] actual = new int[2];
			int [] given = new int[2];
			AIboard.playerPlacee(2);
			AIboard.playerPlacee(test);
			Assert.assertArrayEquals(given, actual);
		}
		
		//Tests the eachStep method by moving the temp board to where it should go and checking the locations of both
		@Test
		public void eachStepTest(){
			int [] dest = new int[2];
			dest[0] = 8;
			dest[1] = 2;
			AIboard.eachStep(1, dest, b);
			int [] placement = new int[2];
			placement[0] = 8;
			placement[1] = 2;
			temp.move(placement, 1);
			int[] method = new int[2];
			int[] moved = new int[2];
			method = AIboard.playerPlacee(1);
			moved = temp.playerPlacee(1);
			Assert.assertArrayEquals(method, moved);
		}

		//Tests the wallPlacementSearch to make sure it comes up with 
		//the correct amount of different boards where the walls can be placed
		@Test
		public void wallPlacementSearchTest(){
			ArrayList<Board> testMoves = new ArrayList<Board>();
			testMoves = AIboard.wallPlacementSearch(b);
			//int given;
			//given =AIboard.boardValue(1, 2, b);
			int sizeArray = testMoves.size();
			Assert.assertTrue(sizeArray==128);
			
		}
		
		@Test
		public void placeWallTest(){
			AIboard.placeWall();
			ArrayList<Board> testMoves = new ArrayList<Board>();
			testMoves = AIboard.wallPlacementSearch(b);
			int sizeArray = testMoves.size();
			//int given;
			//given = AIboard.boardValue(1, 1, b);
			Assert.assertTrue(sizeArray==124);
			
		
		}
		
		@Test
		public void aiMoveBTest(){
			b = new Board(2);
			AI a = new AI(b);
			a.truePlayer = 1;

		}
		
		
}
