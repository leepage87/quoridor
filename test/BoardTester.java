/**
 * Sarah Weller
 * teamOrangeBeard
 */

package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import src.main.BoardTestHelp;

public class BoardTester {


    BoardTestHelp x = new BoardTestHelp(2);

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
        x.setBoard(2);
        Assert.assertNotNull(grid);
    }

    /**
     * Tests if a player moves north when told    
     */
    @Test
    public void checkNorth(){
        x.setBoard(4);
        direction = 'N';
        x.movePieceBoard(direction, 4);
        int[] expected = new int[2];
        expected[0]=16;
        expected[1]=6;
        Assert.assertArrayEquals(x.playerPlace(4), expected);
    }
    
    /**
     * Tests if a player moves south when told
     */
    @Test
    public void checkSouth(){
        x.setBoard(4);
        direction = 'S';
        x.movePieceBoard(direction, 1);
        int[] expected = new int[2];
        expected[0]=8;
        expected[1]=2;
        Assert.assertArrayEquals(x.playerPlace(1), expected);
    }
    
   /**
    * Tests if a player moves east when told
    */
    @Test
    public void checkEast(){
        x.setBoard(4);
        direction = 'E';
        x.movePieceBoard(direction, 1);
        int[] expected = new int[2];
        expected[0]=10;
        expected[1]=0;
        Assert.assertArrayEquals(x.playerPlace(1), expected);
    }
    
   /**
    * Tests if a player moves west when told
    */
    @Test
    public void checkWest(){
        x.setBoard(4);
        direction = 'W';
        x.movePieceBoard(direction, 1);
        int[] expected = new int[2];
        expected[0]=6;
        expected[1]=0;
        Assert.assertArrayEquals(x.playerPlace(1), expected);
    }

    /**
     * Tests to make sure win check does not go to win when it shouldn't
     */
    @Test
    public void falseCheckWin(){
        x.setBoard(2);
        Assert.assertFalse("Wins when not true",x.winCheck(2, x.playerPlace(2)));
    }

    /**
     * Tests to make sure win check says win when it should
     */
    @Test
    public void trueCheckWin(){
        x.setBoard(4);
        x.moveToWin(4);
        int [] place = x.playerPlace(4);
        Assert.assertTrue("Doesn't win when in winning space", x.winCheck(4, place));

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
        x.setBoard(2);
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
		actual = x.doSearch(1);
		int [] expected = new int[3];
		expected[0] = 8;
		expected[1] = 2;
		expected[2] = 8;
		Assert.assertArrayEquals(actual, expected);
	}
     
}