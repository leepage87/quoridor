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
	
		
		@Before
		public void initialize(){
			b = new Board(2);
			AIboard = new AI(b);
			temp = new AI(b);
		}

		@Test
		public void checkPosition(){
			int[]expected = new int[2];
			expected[0] = 8;
			expected[1] = 0;
			Assert.assertArrayEquals(expected, AIboard.currentLoc(1));
			
		}
		@Test
		public void checkSearch(){
			int [] expected = new int[3];
			expected[0] = 8;
			expected[1] = 2;
			//TODO fix expected[2]'s count
			expected[2] = 8;
			int [] actual = new int[3];
			actual = AIboard.moves(AIboard.currentLoc(1), 1);
			System.out.println(actual[0] + " " + actual[1] + " " + actual[2]);
			Assert.assertArrayEquals(expected, actual);
			
		}
		@Test
		public void wallTest(){
			ArrayList<Board> testMoves = new ArrayList<Board>();
			testMoves = AIboard.wallPlacementSearch(b);
			int sizeArray = testMoves.size();
			Assert.assertTrue(sizeArray==128);
			
		}
		@Test
		public void placedWall(){
			//TODO get this working
			initialize();
			temp = new AI(temp.placeWall());
			ArrayList<Board> testMoves = new ArrayList<Board>();
			testMoves = temp.wallPlacementSearch(b);
			int sizeArray = testMoves.size();
			Assert.assertTrue(sizeArray==128);
		
		}
		
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
		
		@Test
		public void oneStepTestNorth(){
			temp = new AI(AIboard.oneStep(1, 1, b));
			int [] given = new int[2];
			given = temp.currentLoc(1);
			int [] test = new int[2];
			test[0] = 8;
			test[1] = 2;
			Assert.assertArrayEquals(given, test);
		}

}
