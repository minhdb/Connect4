/**
 * A text view for the Connect4 game.
 */

package view;

import controller.Connect4Controller;
import model.Connect4Model;
import java.util.Scanner;

public class Connect4TextView {

	/**
	 * Controller object for view-model interaction.
	 */
	private Connect4Controller gameController;

	/**
	 * Scanner object to get user input.
	 */
	private Scanner in;

	public Connect4TextView(Connect4Controller gameController) {
		this.gameController = gameController;
		in = new Scanner(System.in);
		System.out.println("Welcome to Connect 4\n");
	}

	/**
	 * Prints out the current state of the game.
	 * 
	 */
	public void printState() {
		System.out.println(this.gameController.gameBoardToString());
		System.out.print("You are ");
		System.out.println(this.gameController.playerTurnToString());
		System.out.println("");
	}

	/**
	 * Get a move from user.
	 */
	public void getMove() {
		System.out.println("What column would you like to place your token in?");
		while (true) {
			int inputMove = in.nextInt();
			try {
				while (!gameController.placeToken(inputMove)) {
					System.out.println("That's not a legit move. Please try again: ");
					inputMove = in.nextInt();
				}
				break;
			} catch (model.IllegalArgumentException e) {
				System.err.println("That's not a legit move. Please try again: ");
			}
		}
	}

	/**
	 * Print the result of the game. Only 1 of these cases can happen: computer win,
	 * human win, and a tie.
	 * 
	 * @param gameModel the game model to gather the state of the game.
	 */
	public void printResult(Connect4Model gameModel) {
		if (gameModel.isTied()) {
			System.out.println("Draw.");
		} else {
			System.out.println(gameModel.didWin(gameModel.getHumanChar()) ? gameModel.getHumanChar() + " wins"
					: gameModel.getComputerChar() + " wins");
		}
	}
}
