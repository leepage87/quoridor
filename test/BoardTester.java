/**
 * Sarah Weller
 * teamOrangeBeard
 */

package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import src.ui.Board; 

public class BoardTester {


    Board x = new Board(2);

    int grid[][]; 
    char direction;
    final int WALL = 5;

    @Before
    public void initialize(){
        grid = new int[17][17];
    }

    @Test
    public void checkBoardCreation(){
        x.setBoard(2);
        Assert.assertNotNull(grid);
    }

    //This test is now broken due to gui additions to the board
 /*   @Test
    public void checkNorth(){
        x.setBoard(4);
        direction = 'N';
        x.movePieceBoard(direction, 4);
        int[] expected = new int[2];
        expected[0]=14;
        expected[1]=8;
        testResults(x.playerPlace(4), expected);
        //resetPosition();
    }
    
    //This test is now broken due to gui additions to the board
    @Test
    public void checkSouth(){
        x.setBoard(4);
        direction = 'S';
        x.movePieceBoard(direction, 3);
        int[] expected = new int[2];
        expected[0]=2;
        expected[1]=8;
        testResults(x.playerPlace(3), expected);
        //resetPosition();
    }
    
    //This test is now broken due to gui additions to the board
    @Test
    public void checkEast(){
        x.setBoard(4);
        direction = 'E';
        x.movePieceBoard(direction, 3);
        int[] expected = new int[2];
        expected[0]=0;
        expected[1]=10;
        testResults(x.playerPlace(3), expected);
        //resetPosition();
    }
    
    //This test is now broken due to gui additions to the board
    @Test
    public void checkWest(){
        x.setBoard(4);
        direction = 'W';
        x.movePieceBoard(direction, 3);
        int[] expected = new int[2];
        expected[0]=0;
        expected[1]=6;
        testResults(x.playerPlace(3), expected);
        //resetPosition();
    }
*/
    //	@Test
    public void testResults(int[] actual, int[] expected){
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void falseCheckWin(){
        x.setBoard(2);
        Assert.assertFalse("Wins when not true",x.winCheck(2, x.playerPlace(2)));
    }

    @Test
    public void trueCheckWin(){
        x.setBoard(4);
        x.moveToWin(4);
        int [] place = x.playerPlace(4);
        System.out.println(place[0] + " " + place[1]);
        Assert.assertTrue("Doesn't win when in winning space", x.winCheck(4, place));

    }
  /*
>>>>>>> 77b5c3cf61c8a384e79393b20b4a93f2f5dbc763
	@Test
	public void checkWall(){
		x.setBoard(2);
		x.setWall();
		Assert.assertTrue("Did not come back with wall", x.wallCollision('S', 1));
	}

	@Test
	public void checkForAnotherPiece(){
		x.setBoard(2);
		x.placePiece();
		Assert.assertTrue("Did not find the piece", x.pieceCollision('S', 1));
	}
     */
    @Test
    public void ableToPlaceWall(){
        int [] testWall = new int[3] ;
        testWall[0] = 0;
        testWall[1] = 4;
        testWall[2] = 1;
        Assert.assertTrue("Wall was not placed", x.canPlaceWall(testWall));
    }

    @Test
    public void notAbleToPlaceWall(){
        x.placeTestWall();
        int [] testWall = new int[3];
        testWall[0] = 0;
        testWall[1] = 4;
        testWall[2] = 1;
        Assert.assertFalse("Wall not found", x.canPlaceWall(testWall));
    }

    @Test
    public void actuallyPlacesWall(){
        x.setBoard(2);
        int [] testWall = new int[3] ;
        testWall[0] = 0;
        testWall[1] = 4;
        testWall[2] = 1;
        x.placeWallBoard(testWall);
        Assert.assertFalse("Wall not Placed", x.canPlaceWall(testWall));
    }
    /*
	@Test
	public void searchTest(){
		int [] actual = new int[2];
		actual = x.doSearch(1);
		int [] expected = new int[2];
		expected[0] = 2;
		expected[1] = 8;
		testResults(actual, expected);
	}
     */
}