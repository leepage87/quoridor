package board_test;

import static ui.Board.*;

public class BoardTest {
	
		public void checkCorrectDirection() throws Exception{
			//player position = same row decrement column by 2 'North'
			for(direction == 'N'){
				here[0].isEqual(here[0]-2)
			//player position = same row increment column by 2 'South'
			for(direction == 'S')
				here[0].isEqual(here[0]+2)
			//player position = same column decrement row by 2 'West'
			for(direction == 'E')
				here[1].isEqual(here[1]+2)
			//player position = same column increment row by 2 'East'
			for(direction == 'W')
				here[1].isEqual(here[1]-2)
		}

		
		public void checkWall() throws Exception{
			//player position with column-1 is wall free 'North'
			//player position with column+1 is wall free 'South'
			//player position with row-1 is wall free 'West'
			//player position with row+1 is wall free 'East'
	}
		
		public void checkWin() throws Exception{
			
			//if the player has won make sure the game says so
		}
		
		public void checkAmountOfPlayers() throws Exception{
			//check to see if you board is using the correct amount of players
		}

		public void checkBoardSize() throws Exception{
			//make sure the board size is correct
			
		}
		
		public void checkMovementOrder() throws Excption{
			//checks to make the moving player is moving on his turn
		}
		
		public void checkLegalWall() throws Exception{
			//checks wall placement is legal
		}

}
