package test;
import src.ui.AI;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AiTest {
		
		
		AI a = new AI(2);
		
		char direction;
		final int WALL = 5;
		int grid[][]; 
	
		
		@Before
		public void initialize(){
			grid = new int[17][17];
		}

		@Test
		public void checkAiMoved(){
			a.setAI(2);
			int[] expected = new int[2];
			expected[0] = 2;
			expected[1] = 8;
		//	testResults(expected, a.moves(1));
		}
	//	@Test
	//	public void testResults(int[] actual, int[] expected){
	//		Assert.assertArrayEquals(expected, actual);
	//	}
		
			
		}
		

