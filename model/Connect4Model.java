package model;

/**
 * This class represents the Connect4 game model.
 * 
 */

public class Connect4Model extends java.util.Observable implements java.io.Serializable {

	/**
	 * Java generated serialization ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Representation of the board.
	 */
	private char[][] board;

	/**
	 * The maximum size for each rows.
	 */
	private final int ROWS_NUM = 7;

	/**
	 * The maximum size of each column.
	 */
	private final int COLS_NUM = 6;

	/**
	 * The number of consecutive pieces either horizontally, vertically, or
	 * diagonally to win the game.
	 */
	private final int CONNECT_SIZE = 4;

	/**
	 * Indicates if it's currently the human turn.
	 */
	private boolean isHumanMove;

	/**
	 * The default character representation of the player.
	 */
	private final char humanChar = 'X';

	/**
	 * The default character representation of the computer player.
	 */
	private final char computerChar = 'O';

	/**
	 * The default character representation of an empty cell.
	 */
	private final char blankChar = '_';

	/**
	 * The object which represents the computer's strategy for playing the game.
	 */
	private transient ComputerPlayer computerPlayer;

	/**
	 * Counts the total number of moves that have been made.
	 */
	private int moveMaked;

	/**
	 * Encapsulate move information to send over to the views.
	 */
	private transient Connect4MoveMessage moveMsg;


	
	public Connect4Model() {
		board = new char[ROWS_NUM][COLS_NUM];
		for (int i = 0; i < ROWS_NUM; i++) {
			for (int j = 0; j < COLS_NUM; j++) {
				board[i][j] = blankChar;
			}
		}

		// Human moves first by default.
		isHumanMove = true;
		// computerPlayer = new RandomAI();
		computerPlayer = new Minimax_AlphaBeta();
		moveMaked = 0;
		moveMsg = null;
		setChanged();
		notifyObservers(moveMsg);
	}
/*
	public void newGame() {
		board = new char[ROWS_NUM][COLS_NUM];
		for (int i = 0; i < ROWS_NUM; i++) {
			for (int j = 0; j < COLS_NUM; j++) {
				board[i][j] = blankChar;
			}
		}
		// Human moves first by default.
		isHumanMove = true;
		// computerPlayer = new RandomAI();
		computerPlayer = new Minimax_AlphaBeta();
		moveMaked = 0;
		moveMsg = new Connect4MoveMessage();
		setChanged();
		notifyObservers(moveMsg);
	}*/

	public void setComputerPlayer(ComputerPlayer another) {
		this.computerPlayer = another;
	}

	/**
	 * Return a character on the board at row x and column y.
	 * 
	 * @param x The row number.
	 * @param y The column number.
	 * @return A character on the board at row x and column y.
	 * @throws IllegalArgumentException when x is not between 0 and ROWS_NUM or y is
	 *                                  not between 0 and COLS_NUM.
	 */
	public char getObjectAt(int x, int y) throws model.IllegalArgumentException {
		if ((x >= this.ROWS_NUM || x < 0) || (y >= this.COLS_NUM || y < 0)) {
			throw new model.IllegalArgumentException("Invalid arguments.");
		}
		return this.board[x][y];
	}

	/**
	 * Set an object at cell (x, y) to a character.
	 * 
	 * @param x   stands for row
	 * @param y   stands for column
	 * @param obj The character representation of any player (can be blank).
	 * @throws IllegalArgumentException when x is not between 0 and ROWS_NUM or y is
	 *                                  not between 0 and COLS_NUM.
	 */
	public void setObjectAt(int x, int y, char obj) throws model.IllegalArgumentException {
		if ((x >= this.ROWS_NUM || x < 0) || (y >= this.COLS_NUM || y < 0)) {
			throw new model.IllegalArgumentException("Invalid arguments.");
		}
		this.board[x][y] = obj;
		if (obj == this.getComputerChar()) { 
			moveMsg = new Connect4MoveMessage(this.board[0].length - y - 1, x , 2);
		} else 
			moveMsg = new Connect4MoveMessage(this.board[0].length - y - 1, x , 1);
		setChanged();
		notifyObservers(moveMsg);
	}

	/**
	 * Check if a cell is empty or not.
	 * 
	 * @param x stands for row
	 * @param y stand for column
	 * @return true if the cell (x,y) is empty.
	 */
	public boolean isBlank(int x, int y) {
		return this.board[x][y] == blankChar;
	}

	/**
	 * Return the maximum row of the board.
	 * 
	 * @return The maximum row of the board.
	 */
	public int getMaxRow() {
		return this.ROWS_NUM;
	}

	/**
	 * Return the maximum column of the board.
	 * 
	 * @return The maximum column of the board.
	 */
	public int getMaxCol() {
		return this.COLS_NUM;
	}

	/**
	 * Accessor to the model's character representation of the player.
	 * 
	 * @return A character representing the player.
	 */
	public char getHumanChar() {
		return humanChar;
	}

	/**
	 * Accessor to the model's character representation of the computer player.
	 * 
	 * @return A character representing the computer.
	 */
	public char getComputerChar() {
		return computerChar;
	}

	/**
	 * Accessor to the model's character representation of an empty slot.
	 * 
	 * @return A blank character.
	 */
	public char getBlankChar() {
		return blankChar;
	}

	/**
	 * An accessor to isHumanMove.
	 * 
	 * @return Return true if it's currently the human's move.
	 */
	public boolean isHumanMove() {
		return this.isHumanMove;
	}

	public void setHumanTurn(boolean isHumanTurn) {
		isHumanMove = isHumanTurn;
	}

	public void switchTurn() {
		this.moveMaked++;
		isHumanMove = !isHumanMove;
		// this.notifyObservers();
	}

	/**
	 * Check the winning condition by column.
	 * 
	 * @param playerChar A character that represents the player.
	 * @return True if playerChar wins the game by column and false otherwise.
	 */
	public boolean wonByCol(char playerChar) {
		for (int i = 0; i < ROWS_NUM; i++) {
			int colSum = 0;
			for (int j = 0; j < COLS_NUM; j++) {
				if (board[i][j] == playerChar) {
					colSum++;
					if (colSum == CONNECT_SIZE)
						return true;
				} else
					colSum = 0;

			}
		}
		return false;
	}

	/**
	 * Check the winning condition by row.
	 * 
	 * @param playerChar A character that represents the player.
	 * @return True if playerChar wins the game by row and false otherwise.
	 */
	public boolean wonByRow(char playerChar) {
		for (int i = 0; i < COLS_NUM; i++) {
			int rowSum = 0;
			for (int j = 0; j < ROWS_NUM; j++) {
				if (board[j][i] == playerChar) {
					rowSum++;
					if (rowSum == CONNECT_SIZE)
						return true;
				} else
					rowSum = 0;
			}
		}
		return false;
	}

	/**
	 * Check the winning condition diagonally, both left and right.
	 * 
	 * @param playerChar A character that represents the player.
	 * @return True if playerChar wins the game diagonally and false otherwise.
	 */
	public boolean wonByDiagonal(char playerChar) {
		int sum = 0;

		// Covering from left to right.
		for (int i = 3; i < ROWS_NUM; i++) {
			sum = 0;
			for (int j = 0; j < COLS_NUM && j <= i; j++) {
				if (board[i - j][j] == playerChar) {
					sum++;
					if (sum == CONNECT_SIZE)
						return true;
				} else {
					sum = 0;
				}
			}
		}

		for (int j = 1; j < 3; j++) {
			sum = 0;
			for (int k = 0; j + k < COLS_NUM; k++) {
				// System.out.println(ROWS_NUM - 1 - k + " " + (j + k));
				if (board[ROWS_NUM - 1 - k][j + k] == playerChar) {
					sum++;
					if (sum == CONNECT_SIZE)
						return true;

				} else
					sum = 0;
			}
		}

		// Covering from right to left.
		for (int i = 3; i >= 0; i--) {
			sum = 0;
			for (int j = 0; j < COLS_NUM && (i + j) < ROWS_NUM; j++) {
				if (board[i + j][j] == playerChar) {
					// System.out.println(i + j + " " + j);
					sum++;
					if (sum == CONNECT_SIZE)
						return true;
				} else {
					sum = 0;
				}
			}
		}

		for (int j = 1; j < 3; j++) {
			sum = 0;
			for (int k = 0; j + k < COLS_NUM; k++) {
				if (board[0 + k][j + k] == playerChar) {
					sum++;
					if (sum == CONNECT_SIZE)
						return true;
				} else
					sum = 0;
			}
		}
		return false;
	}

	/**
	 * Check if a player win the game either by row, column, or diagonal.
	 * 
	 * @param playerChar A character representing the player who won.
	 * @return True if playerChar win the game in some ways.
	 */
	public boolean didWin(char playerChar) {
		return wonByRow(playerChar) || wonByCol(playerChar) || wonByDiagonal(playerChar);
	}

	/**
	 * Check the tie condition of the game. Tie happens when nobody wins and there's
	 * no move left.
	 * 
	 * @return True if it's a tie and false otherwise.
	 */
	public boolean isTied() {
		// Tie condition: Out of moves AND nobody wins.
		return (moveMaked == ROWS_NUM * COLS_NUM && !(didWin(computerChar) || didWin(humanChar)));
		// Board is filled.
		/*
		 * boolean isFilled = true; for (int i = 0; i < board.length; i++) { for (int j
		 * = 0; j < board[0].length; j++) { if (board[i][j] == '_') { isFilled = false;
		 * break; } } } return (isFilled && !(didWin(computerChar) ||
		 * didWin(humanChar)));
		 */
	}

	/**
	 * Prompts the computer to make a move.
	 * 
	 * @param c The character token that will belong to the move.
	 */
	public void computerMove(char c, boolean max) {
		boolean foundAMove = false;
		while (!foundAMove) {
			int colMove = computerPlayer.getMove(this, max);
			if (colMove == -1)
				return;
			for (int i = 0; i < COLS_NUM; i++) {
				if (board[colMove][i] == blankChar) {
					board[colMove][i] = c;
					if (c == this.computerChar)
						moveMsg = new Connect4MoveMessage(board[0].length - i - 1, colMove , 2);
					else
						moveMsg = new Connect4MoveMessage(board[0].length - i - 1, colMove, 1);
					foundAMove = true;
					break;
				}
			}
		}
		setChanged();
		notifyObservers(moveMsg);
	}

	/**
	 * 
	 * @return A 2d characters array that represents the board.
	 */
	public char[][] getBoard() {
		return this.board;
	}

	/**
	 * 
	 * @return True if the game is already over.
	 */
	public boolean isOver() {
		return this.didWin(this.getComputerChar()) || this.didWin(this.getHumanChar()) || this.isTied();
	}

	/**
	 * @return A move message that is used to update the observers.
	 */
	public Connect4MoveMessage getMoveMsg() {
		return this.moveMsg;
	}

	/**
	 * 
	 */
	public void printRawBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Set the reference of the character array board to this.board. Its only
	 * purpose is to self assign a given board to model's subclass, in which
	 * this.board is not visible.
	 * 
	 * @param board A character array board.
	 */
	public void setBoard(char[][] board) {
		this.board = board;
	}

	/**
	 * Set the move made for this object. Similar to setBoard(), only to do a deep
	 * copy of model's subclass object. It shouldn't be called in other cases.
	 * 
	 * @param m The number of moves that should be assigned to this instance's move.
	 */
	public void setMoveMaked(int m) {
		this.moveMaked = m;
	}

	/**
	 * @return The number of moves that have been made in this model.
	 */
	public int getMoveMaked() {
		return moveMaked;
	}

}
