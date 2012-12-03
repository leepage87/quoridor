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
	
		/**
		*Initializes the AI for testing creating a board, and an AI, and a temporary board
		*/
		@Before
		public void initialize(){
			b = new Board(2);
			AIboard = new AI(b);
			temp = new AI(b);
		}

		/**
		*Tests the boardValue method to make sure the starting position board value is 0
		*/
		@Test
		public void boardValueTest(){
			int given;
			given = AIboard.getBoardValue(1, 2, b);
			Assert.assertTrue(given == 0);
		}
		/**
		*Tests the aiMove method
		*/
		@Test
		public void aiMoveTest(){
			int [] placement = new int[2];
			placement[0] = 8;
			placement[1] = 2;
			AIboard.aiMove(1, 1);
			AIboard.move(placement, 1);
			int[] method = new int[2];
			int [] moved = new int[2];
			method  = AIboard.playerPlacee(1);
			moved = AIboard.playerPlacee(1);
			Assert.assertArrayEquals(method, moved);
		}
		/**
		*Tests the aiWall method for a vertical wall
		*/
		@Test
		public void aiWallTestV(){
			int [] placement = new int[3];
	        placement[0] = 3;
	        placement[1] = 3;
	        placement[2] = 1;
			int [] move = new int[2];
			move[0] = 8;
			move[1] = 2;
			Board temp = new Board(2);
			temp = AIboard.placeWall(placement);
			Board b = new Board(2);
			int[] result = new int[3];
			result = AIboard.aiWall(1, b, temp);
			int [] actual = new int[3];
			actual[0] = 3;
			actual[1] = 3;
			actual[2] = 1;
			Assert.assertArrayEquals(actual, result);
			
		}
		/**
		*Tests the aiWall method for a horizontal wall
		*/
		@Test
		public void aiWallTestH(){
			int [] placement = new int[3];
	        placement[0] = 3;
	        placement[1] = 3;
			int [] move = new int[2];
			move[0] = 8;
			move[1] = 2;
			Board temp = new Board(2);
			temp = AIboard.placeWall(placement);
			Board b = new Board(2);
			int[] result = new int[3];
			result = AIboard.aiWall(1, b, temp);
			int [] actual = new int[3];
			actual[0] = 3;
			actual[1] = 3;
			actual[2] = 0;
			Assert.assertArrayEquals(actual, result);
			
		}
		/**
		*Tests the boardValue method after moving a player to make sure it increments the boardValue
		*/
		@Test
		public void boardMoveValueTest(){
			int given;
			int [] placement = new int[2];
			placement[0] = 8;
			placement[1] = 2;
			this.b = AIboard.move(placement, 1);
			given = AIboard.getBoardValue(1, 2, b);
			Assert.assertTrue(given == 1);
		}
		/**
		*Tests the boardValue method after moving the first player, then the second, then the first again
		*/
		@Test
		public void boardMoveValueTest2(){
			int given;
			int [] placement = new int[2];
			placement[0] = 8;
			placement[1] = 2;
			this.b = AIboard.move(placement, 1);
			placement[0] = 8;
			placement[1] = 14;
			this.b = AIboard.move(placement, 2);
			placement[0] = 8;
			placement[1] = 4;
			this.b = AIboard.move(placement, 1);
			given = AIboard.getBoardValue(1, 2, b);
			Assert.assertTrue(given == 1);
		}
		/**
		*Tests the board value after a wall is placed
		*/
		@Test
		public void boardMoveValueTestWall(){
			int given;
			int [] placement = new int[3];
	        placement[0] = 3;
	        placement[1] = 3;
			AIboard.placeWall(placement);
			given = AIboard.getBoardValue(1, 2, b);
			Assert.assertTrue(given == 0);
		}
		/**
		*Tests the board value after a move is taken and a wall is placed
		*/
		@Test
		public void boardMoveValueTestWall2(){
			int given;
			int [] placement = new int[3];
	        placement[0] = 3;
	        placement[1] = 3;
			AIboard.placeWall(placement);
			int []place = new int[2];
			place[0] = 8;
			place[1] = 14;
			this.b = AIboard.move(place, 2);
			place[0] = 8;
			place[1] = 2;
			this.b = AIboard.move(place, 1);
			given = AIboard.getBoardValue(2, 1, b);
			Assert.assertTrue(given == 0);
		}
		/**
		*Tests the findEnemy method to make sure it finds the correct enemy by using its location
		*/
		@Test
		public void findEnemyTest(){
			int test=0;
			test = AIboard.getFindEnemy(1, b);
			int[] actual = new int[2];
			int [] given = new int[2];
			AIboard.playerPlacee(2);
			AIboard.playerPlacee(test);
			Assert.assertArrayEquals(given, actual);
		}
		/**
		*Tests the eachStep method by moving the temp board to where it should go and checking the locations of both
		*/
		@Test
		public void eachStepTest(){
			int [] dest = new int[2];
			dest[0] = 8;
			dest[1] = 2;
			AIboard.getEachStep(1, dest, b);
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
		/**
		*Tests the wallPlacementSearch to make sure it comes up with 
		*the correct amount of different boards where the walls can be placed
		*/
		@Test
		public void wallPlacementSearchTest(){
			ArrayList<Board> testMoves = new ArrayList<Board>();
			testMoves = AIboard.getWallPlacementSearch(b, 1);
			int sizeArray = testMoves.size();
			Assert.assertTrue(sizeArray==128);
			
		}
		/**
		*Tests if the AI places a wall
		*/
		@Test
		public void placeWallTest(){
			int [] placement = new int[3];
	        placement[0] = 3;
	        placement[1] = 3;
			AIboard.placeWall(placement);
			ArrayList<Board> testMoves = new ArrayList<Board>();
			testMoves = AIboard.getWallPlacementSearch(b, 1);
			int sizeArray = testMoves.size();
			Assert.assertTrue(sizeArray==124);
			
		
		}
		/**
		*Tests if true player works
		*/
		@Test
		public void aiMoveBTest(){
			b = new Board(2);
			AI a = new AI(b);
			a.setTruePlayer(1);

		}
		
		
}
