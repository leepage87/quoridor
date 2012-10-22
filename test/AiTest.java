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
			testMoves = AIboard.wallPlacementSearch(1, 2);
			int sizeArray = testMoves.size();
			Assert.assertTrue(sizeArray==128);
			
		}
		@Test
		public void placedWall(){
			//TODO get this working
			temp.placeWall();
			ArrayList<Board> testMoves = new ArrayList<Board>();
			testMoves = temp.wallPlacementSearch(1, 2);
			int sizeArray = testMoves.size();
			Assert.assertTrue(sizeArray==126);
		
		}

}
