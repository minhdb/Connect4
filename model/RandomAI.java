package model;

/**
 * The class implements a random strategy for connect4 game.
 */

import model.Connect4Model;
import java.util.Random;

public class RandomAI implements ComputerPlayer {
	public int getMove(Connect4Model gameModel, boolean max) {
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(gameModel.getMaxRow());
	}
}
