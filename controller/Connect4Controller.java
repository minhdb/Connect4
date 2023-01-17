/**
 * This class represents the Connect4 game controller, handling interaction with 
 * the game model.
 * 
 */

package controller;

import model.Connect4Model;

public class Connect4Controller {

	private final Connect4Model gameModel;
	
	public Connect4Controller(final Connect4Model model) {

		this.gameModel = model;
	}
	
	/**
	 * Constructs a string that represents the current state of the board.
	 * 
	 * @return A string representing the board.
	 */
	public String gameBoardToString() {
		StringBuilder boardString = new StringBuilder();
		int max_row = gameModel.getMaxRow();
		int max_col = gameModel.getMaxCol();
		
		for (int j = max_col-1; j >= 0; j--) {
			for (int i = 0; i < max_row; i++) {
				try {
					boardString.append(gameModel.getObjectAt(i, j)).append(" ");
				} catch (model.IllegalArgumentException e) {
					// It's guaranteed that the try block always succeed.
				}
			}
			boardString.append("\n");
		}
		
		boardString.append("0 1 2 3 4 5 6\n");
		
		return boardString.toString();
	}
	
	/**
	 * Returns a string indicating whose turn is it.
	 * 
	 * @return A string indicating a player's turn.
	 */
	public String playerTurnToString() {
		return gameModel.isHumanMove() ? gameModel.getHumanChar() + "" : gameModel.getComputerChar() + "";
	}
	
	/**
	 * Returns true if successfully place a token of the current player into the atCol-th
	 * column and false otherwise. 
	 * 
	 * @param atCol The column in which the token will be attempted to place.
	 * @return True if success and false otherwise.
	 * @throws model.IllegalArgumentException is thrown if atCol is not between 0 and max row.
	 */
	public boolean placeToken(int atCol) throws model.IllegalArgumentException {
		if (atCol < 0 || atCol >= gameModel.getMaxRow()) {
			throw new model.IllegalArgumentException("Invalid arguments.");
		}
		for (int i = 0; i < gameModel.getMaxCol(); i++) {
			if (gameModel.isBlank(atCol, i)) {
				gameModel.setObjectAt(atCol, i, this.playerTurnToString().charAt(0)); 
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the game is over, meaning either 1 of the player wins or it's a tie.
	 * 
	 * @return true if the game is over.
	 */
	public boolean gameIsOver() {
		return gameModel.isTied() || 
				gameModel.didWin(gameModel.getComputerChar()) || 
				gameModel.didWin(gameModel.getHumanChar());
	}
	
}
