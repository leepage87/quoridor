/**
 * Sarah Weller
 * teamOrangeBeard
 */

package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import src.main.*;


public class BoardTest{


    Board x = new Board(2);

    int grid[][]; 
    char direction;
    final int WALL = 5;

    /**
     * Initializes the Board
     */
    @Before
    public void initialize(){
        grid = new int[17][17];
    }

    /**
     * Tests to make sure the board is created
     */
    @Test
    public void checkBoardCreation(){
        x.getSetBoard(2);
        Assert.assertNotNull(grid);
    }

    /**
     * Tests to make sure the equals test works
     */
    @Test
    public void equalsTest(){
    	x.getSetBoard(2);
    	Board b = new Board(2);
    	b.getSetBoard(2);
    	Assert.assertTrue(x.equals(b));
    }
    
    /**
     * Tests the toString method
     */
    @Test
    public void toStringTest(){
    	x.getSetBoard(2);
    	 String given =  "";
    	given = x.toString();
    	grid[8][0] = 1;
    	grid[8][16] = 2;
    	String actual = "";
    	for(int i = 0; i < 17; i++){
			for(int j = 0; j < 17; j++){
				actual += (grid[j][i] + " ");
			}
			actual += "\n";
    	}
    	Assert.assertTrue(actual.equals(given));
    }
    
    /**
     * Tests the playerPlace method
     */
    @Test
    public void playerPlaceTest(){
    	x.getSetBoard(2);
    	int[] place = new int[2];	
    	place = x.playerPlace(1);
    	int []actual = new int[2];
    	actual[0] = 8;
    	actual[1] = 0;
    	Assert.assertArrayEquals(actual, place);
    }
    
    /**
     * Tests to make sure win check does not go to win when it shouldn't
     */
    @Test
    public void falseCheckWin(){
        x.getSetBoard(2);
        Assert.assertFalse("Wins when not true",x.getWinCheck(2, x.playerPlace(2)));
    }

    /**
     * Tests to make sure win check says win when it should
     */
    @Test
    public void trueCheckWin(){
        x.getSetBoard(4);
        x.moveToWin(4);
        int [] place = x.playerPlace(4);
        Assert.assertTrue("Doesn't win when in winning space", x.getWinCheck(4, place));

    }

    /**
     * Tests if the method canPlaceWall works
     */
    @Test
    public void ableToPlaceWall(){
        int [] testWall = new int[3] ;
        testWall[0] = 5;
        testWall[1] = 5;
        testWall[2] = 0;
        Assert.assertTrue("Wall was not placed", x.canPlaceWall(testWall, 1));
    }

    /**
     * Tests if the method canPlaceWall breaks when it can't place a wall
     */
    @Test
    public void notAbleToPlaceWall(){
        x.placeTestWall();
        int [] testWall = new int[3];
        testWall[0] = 0;
        testWall[1] = 4;
        testWall[2] = 1;
        Assert.assertFalse("Wall not found", x.canPlaceWall(testWall, 1));
    }

    /**
     * Tests if a wall is placed when told
     */
    @Test
    public void actuallyPlacesWall(){
        x.getSetBoard(2);
        int [] testWall = new int[3] ;
        testWall[0] = 0;
        testWall[1] = 4;
        testWall[2] = 1;
        x.placeWallBoard(testWall, 1);
        Assert.assertFalse("Wall not Placed", x.canPlaceWall(testWall, 1));
    }
    
    /**
     * Tests the doSearch method for the best move
     */
	@Test
	public void searchTest(){
		int [] actual = new int[3];
		actual = x.bestMove(1);
		int [] expected = new int[3];
		expected[0] = 8;
		expected[1] = 2;
		expected[2] = 8;
		Assert.assertArrayEquals(actual, expected);
	}
     
}