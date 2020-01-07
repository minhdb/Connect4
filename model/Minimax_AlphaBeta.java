package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements the minimax alpha-beta pruning algorithm.
 * 
 * @author Minh Bui
 *
 */

public class Minimax_AlphaBeta implements ComputerPlayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8226404171733596155L;
	/**
	 * Specify the maximum depth for the search tree.
	 */
	private final int init_depth = 5;

	/**
	 * Comment: Since minimax looks at all of the nodes in the search tree, we need
	 * a class that is similar to the game model.
	 * 
	 * It's quite inefficient since we have to create many instances of this class
	 * for each tree node.
	 * 
	 * @author Minh Bui
	 */
	private class GameState extends Connect4Model {

		/**
		 * Java generated serialize UID. Since GameState extends Connect4Model, it's
		 * just here for the sake of surpressing warning.
		 */
		private static final long serialVersionUID = 2L;

		/**
		 * Create a GameState object given a game model.
		 * 
		 * @param gameModel
		 *            The given game model.
		 */
		public GameState(Connect4Model gameModel) {
			// Create a deep copy of the board array.
			char[][] board = new char[gameModel.getMaxRow()][gameModel.getMaxCol()];
			char[][] orig_board = gameModel.getBoard();
			for (int i = 0; i < board.length; i++) {
				board[i] = Arrays.copyOf(orig_board[i], orig_board[i].length);
			}

			// Use setObjectAt instead of setBoard because it's a bad design.
			this.setBoard(board);
			this.setHumanTurn(gameModel.isHumanMove());
			this.setMoveMaked(gameModel.getMoveMaked());
		}

		/**
		 * Create a GameState object given the crucial information for a game state.
		 * Used to make a deep copy of another game state.
		 * 
		 * @param board
		 *            The board that models the game connect4.
		 * @param isHumanTurn
		 *            True if it's currently the human's turn in the given state.
		 * @param m
		 *            The number of moves made in the given state.
		 */
		public GameState(char[][] board, boolean isHumanTurn, int m) {
			// Create a deep copy of the board array.
			char[][] copy_board = new char[board.length][board[0].length];
			for (int i = 0; i < board.length; i++)
				copy_board[i] = Arrays.copyOf(board[i], board[0].length);
			this.setBoard(copy_board);
			this.setHumanTurn(isHumanTurn);
			this.setMoveMaked(m);
		}

		/**
		 * @return Returns a list of possible moves for this game state.
		 */
		private List<Integer> getLegalMoves() {

			List<Integer> availableMoves = new ArrayList<>();
			if (this.isOver())
				return availableMoves;
			for (int i = 0; i < this.getBoard().length; i++) {
				if (this.getBoard()[i][this.getBoard()[0].length - 1] == this.getBlankChar()) {
					availableMoves.add(i);
				}
			}
			return availableMoves;
		}
	}

	/**
	 * Given a game model object, use alpha-beta pruning minimax algorithm to
	 * calculate and return the next optimal move.
	 * 
	 * @param gameModel
	 *            The given game model object.
	 * @param max
	 *            True if the agent is playing to maximize its utility and false in
	 *            the case of minimizing utility.
	 */
	@Override
	public int getMove(Connect4Model gameModel, boolean max) {
		GameState currentState = new GameState(gameModel);

		List<Integer> legalMoves = currentState.getLegalMoves();

		if (legalMoves.isEmpty())
			return -1;
		int bestMove = 0;
		double bestUtil = 0;
		if (max) {
			// Need to get the first available's move utility to have an initial value to
			// compare to.
			bestMove = legalMoves.get(0);
			GameState nextState1 = getNextState(currentState, bestMove);
			bestUtil = minValue(init_depth, nextState1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

			// Get the move with the minimum utility value.
			for (int i = 1; i < legalMoves.size(); i++) {
				double thisMoveUtil = minValue(init_depth, getNextState(currentState, legalMoves.get(i)),
						Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
				if (bestUtil < thisMoveUtil) {
					bestUtil = thisMoveUtil;
					bestMove = legalMoves.get(i);
				}
			}
		} else {
			// Need to get the first available's move utility to have an initial value to
			// compare to.
			bestMove = legalMoves.get(0);
			GameState nextState1 = getNextState(currentState, bestMove);
			bestUtil = maxValue(init_depth, nextState1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

			// Get the move with the maximum utility value.
			for (int i = 1; i < legalMoves.size(); i++) {
				double thisMoveUtil = maxValue(init_depth, getNextState(currentState, legalMoves.get(i)),
						Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
				if (bestUtil > thisMoveUtil) {
					bestUtil = thisMoveUtil;
					bestMove = legalMoves.get(i);
				}
			}
		}
		System.out.println("Computer bestmove: " + bestMove);
		System.out.println("Utility: " + bestUtil);
		return bestMove;
	}

	/**
	 * Calculate the max value for the current state. (State or node for the human
	 * player)
	 * 
	 * @param depth
	 *            The current depth of the search tree.
	 * @param state
	 *            The current game state that needs to a minimax value.
	 * @param alpha
	 *            The value of the best choice we have found so far in the MAX path.
	 * @param beta
	 *            The value of the best choice we have found so far in the MIN path.
	 * @return The max value for the current state.
	 */
	private double maxValue(int depth, GameState state, double alpha, double beta) {
		if (depth == 0 || state.isOver())
			return utilityOf(state);
		double v = Double.NEGATIVE_INFINITY;
		for (int move : state.getLegalMoves()) {
			v = Math.max(v, minValue(depth - 1, getNextState(state, move), alpha, beta));
			if (v >= beta)
				return v;
			alpha = Math.max(alpha, v);
		}
		/*
		 * state.printRawBoard(); System.out.println(v);
		 */
		return v;
	}

	/**
	 * Calculate the min value for the current state. (State or node for the
	 * computer player)
	 * 
	 * @param depth
	 *            The current depth of the search tree.
	 * @param state
	 *            The current game state that needs to a minimax value.
	 * @param alpha
	 *            The value of the best choice we have found so far in the MAX path.
	 * @param beta
	 *            The value of the best choice we have found so far in the MIN path.
	 * @return The min value for the current state.
	 */
	private double minValue(int depth, GameState state, double alpha, double beta) {
		if (depth == 0 || state.isOver())
			return utilityOf(state);
		double v = Double.POSITIVE_INFINITY;
		for (int move : state.getLegalMoves()) {
			v = Math.min(v, maxValue(depth - 1, getNextState(state, move), alpha, beta));
			if (v <= alpha)
				return v;
			beta = Math.min(beta, v);
		}
		/*
		 * System.out.println("In min value"); state.printRawBoard();
		 * System.out.println(v);
		 */
		return v;
	}

	/**
	 * Given a move and the current game state, generate the next game state.
	 * 
	 * @param curState
	 *            the current game state
	 * @param move
	 *            The move that will be made in the next state.
	 * @return The next state with the move.
	 */
	private GameState getNextState(GameState curState, int move) {
		GameState nextState = new GameState(curState.getBoard(), curState.isHumanMove(), curState.getMoveMaked());
		// curState.printRawBoard();
		try {
			char c = '\0';
			if (curState.isHumanMove())
				c = curState.getHumanChar();
			else
				c = curState.getComputerChar();
			for (int i = 0; i < nextState.getMaxCol(); i++) {
				if (curState.isBlank(move, i)) {
					nextState.setObjectAt(move, i, c);
					nextState.switchTurn();
					break;
				}
			}
		} catch (Exception e) {

		}
		/*
		 * System.out.println(""); nextState.printRawBoard(); System.out.println("");
		 */
		return nextState;
	}

	/**
	 * Utility function assigning the utility values to terminal states of the game.
	 * Need a better evaluation function for non-terminal state. Right now we are
	 * relying on randomness.
	 * 
	 * @param curState
	 * @return A number indicating the utility (score) for the given state.
	 */
	private double utilityOf(GameState curState) {
		if (curState.isTied())
			return 0;
		else if (curState.didWin(curState.getHumanChar()))
			return 10;
		else if (curState.didWin(curState.getComputerChar()))
			return -10;
		else {
			// return Math.random() * -10 + Math.random() * 10;			
			double score = countConsecutives(curState, curState.getHumanChar(), 2)
					+ countConsecutives(curState, curState.getHumanChar(), 3) * 2
					- countConsecutives(curState, curState.getComputerChar(), 2)
					- countConsecutives(curState, curState.getComputerChar(), 3) * 2;
			if (score == 0)
				return Math.random() * -10 + Math.random() * 10;
			else
				return score;
		}
	}

	/**
	 * Scan the board and count for n consecutive character c that is right before a
	 * a blank character. Only does so partially for the sake of improving the evaluation 
	 * function.
	 * 
	 * @param s the current game state
	 * @param c the character that represent the player
	 * @param n the number of consecutive characters.
	 * @return
	 */
	private int countConsecutives(GameState s, char c, int n) {
		int count = 0;
		// Count n characters in columns.
		try {
			for (int i = 0; i < s.getMaxRow(); i++) {
				int colSum = 0;
				for (int j = 0; j < s.getMaxCol(); j++) {
					if (colSum == n) {
						if (s.getObjectAt(i, j) == s.getBlankChar())
							count++;
					}
					if (s.getObjectAt(i, j) == c) {
						colSum++;

					} else
						colSum = 0;

				}
			}

			// Count n characters in rows.
			for (int i = 0; i < s.getMaxCol(); i++) {
				int rowSum = 0;
				for (int j = 0; j < s.getMaxRow(); j++) {
					if (rowSum == n) {

						if (s.getObjectAt(j, i) == s.getBlankChar()) {
							count++;
						}
						if (s.getObjectAt(j, i) == c) {
							rowSum++;
						} else
							rowSum = 0;

					}
				}
			}
			int sum = 0;
			// Count n characters diagonally.// Covering from left to right.
			for (int i = 3; i < s.getMaxRow(); i++) {
				sum = 0;
				for (int j = 0; j < s.getMaxCol() && j <= i; j++) {
					if (sum == n) {
						if (s.getObjectAt(i - j, j) == s.getBlankChar())
							count++;
					}
					if (s.getObjectAt(i - j, j) == c) {
						sum++;
					} else {
						sum = 0;
					}
				}
			}

			for (int j = 1; j < 3; j++) {
				sum = 0;
				for (int k = 0; j + k < s.getMaxCol(); k++) {
					if (sum == n) {
						if (s.getObjectAt(s.getMaxRow() - 1 - k, j + k) == s.getBlankChar())
							count++;
					}
					if (s.getObjectAt(s.getMaxRow() - 1 - k, j + k) == c) {
						sum++;

					} else
						sum = 0;
				}
			}

			// Covering from right to left.
			for (int i = 3; i >= 0; i--) {
				sum = 0;
				for (int j = 0; j < s.getMaxCol() && (i + j) < s.getMaxRow(); j++) {
					if (sum == n) {
						if (s.getObjectAt(i + j, j) == s.getBlankChar())
							count++;
					}
					if (s.getObjectAt(i + j, j) == c) {
						sum++;
					} else {
						sum = 0;
					}
				}
			}

			for (int j = 1; j < 3; j++) {
				sum = 0;
				for (int k = 0; j + k < s.getMaxCol(); k++) {
					if (sum == n) {
						if (s.getObjectAt(0 + k, j + k) == s.getBlankChar())
							count++;
					}
					if (s.getObjectAt(0 + k, j + k) == c) {
						sum++;

					} else
						sum = 0;
				}
			}
		} catch (model.IllegalArgumentException e) {

		}
		return count;
	}
}
