package model;

/**
 * An interface for generating strategies.
 * 
 *
 */

public interface ComputerPlayer {
	public int getMove(Connect4Model gameModel, boolean max);
}
