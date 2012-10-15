/**
 * @author Lee Page
 * @course CIS 405
 * Quoridor Project
 * Pawn.java - creates a pawn object
 */

package src.pawn;

public class Pawn {
	private String playerName;
	private String location;
	
	public Pawn(){
	}
	
	public Pawn(String playerName, String startingLocation){
		this.playerName = playerName;
		this.location = startingLocation;
	}
	public String getPlayerName(){
		return playerName;
	}
	public String getLocation(){
		return location;
	}
	public void move(String newLocation){
		this.location = newLocation;
	}
}
