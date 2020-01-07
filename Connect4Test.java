import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Connect4Model;
import controller.Connect4Controller;

/**
 * This class contains test methods for our Connect4 game.
 * 
 * @author Minh Bui
 *
 */

public class Connect4Test {
	/**
	 * Test method for Connect4Model.wonByDiagonal().
	 */
	@Test
	void testWonByDiagonal() {
		Connect4Model gm = new Connect4Model();

		Connect4Controller controller = new Connect4Controller(gm);

		try {
			gm.setObjectAt(3, 0, gm.getHumanChar());
			gm.setObjectAt(2, 1, gm.getHumanChar());
			gm.setObjectAt(1, 2, gm.getHumanChar());
			gm.setObjectAt(0, 3, gm.getHumanChar());
			// System.out.println(controller.gameBoardToString());
			assert (gm.wonByDiagonal(gm.getHumanChar()));

			gm = new Connect4Model();
			controller = new Connect4Controller(gm);
			gm.setObjectAt(4, 0, gm.getHumanChar());
			gm.setObjectAt(3, 1, gm.getHumanChar());
			gm.setObjectAt(2, 2, gm.getHumanChar());
			gm.setObjectAt(1, 3, gm.getHumanChar());
			// System.out.println(controller.gameBoardToString());
			assert (gm.wonByDiagonal(gm.getHumanChar()));

			gm = new Connect4Model();
			controller = new Connect4Controller(gm);

			gm.setObjectAt(3, 1, gm.getHumanChar());
			gm.setObjectAt(2, 2, gm.getHumanChar());
			gm.setObjectAt(1, 3, gm.getHumanChar());
			gm.setObjectAt(0, 4, gm.getHumanChar());
			// System.out.println(controller.gameBoardToString());
			assert (gm.wonByDiagonal(gm.getHumanChar()));

			gm = new Connect4Model();
			controller = new Connect4Controller(gm);

			gm.setObjectAt(4, 1, gm.getHumanChar());
			gm.setObjectAt(3, 2, gm.getHumanChar());
			gm.setObjectAt(2, 3, gm.getHumanChar());
			gm.setObjectAt(1, 4, gm.getHumanChar());
			// System.out.println(controller.gameBoardToString());
			assert (gm.wonByDiagonal(gm.getHumanChar()));

			gm = new Connect4Model();
			controller = new Connect4Controller(gm);

			gm.setObjectAt(6, 0, gm.getHumanChar());
			gm.setObjectAt(5, 1, gm.getHumanChar());
			gm.setObjectAt(4, 2, gm.getHumanChar());
			gm.setObjectAt(3, 3, gm.getHumanChar());
			// System.out.println(controller.gameBoardToString());
			assert (gm.wonByDiagonal(gm.getHumanChar()));

			gm = new Connect4Model();
			controller = new Connect4Controller(gm);

			gm.setObjectAt(6, 2, gm.getHumanChar());
			gm.setObjectAt(5, 3, gm.getHumanChar());
			gm.setObjectAt(4, 4, gm.getHumanChar());
			gm.setObjectAt(3, 5, gm.getHumanChar());
			// System.out.println(controller.gameBoardToString());
			assert (gm.wonByDiagonal(gm.getHumanChar()));

			gm = new Connect4Model();
			controller = new Connect4Controller(gm);

			gm.setObjectAt(3, 0, gm.getHumanChar());
			gm.setObjectAt(4, 1, gm.getHumanChar());
			gm.setObjectAt(5, 2, gm.getHumanChar());
			gm.setObjectAt(6, 3, gm.getHumanChar());
			// System.out.println(controller.gameBoardToString());
			assert (gm.wonByDiagonal(gm.getHumanChar()));

			gm = new Connect4Model();
			controller = new Connect4Controller(gm);

			gm.setObjectAt(0, 2, gm.getHumanChar());
			gm.setObjectAt(1, 3, gm.getHumanChar());
			gm.setObjectAt(2, 4, gm.getHumanChar());
			gm.setObjectAt(3, 5, gm.getHumanChar());
			// System.out.println(controller.gameBoardToString());
			assert (gm.wonByDiagonal(gm.getHumanChar()));

			gm = new Connect4Model();
			controller = new Connect4Controller(gm);

			gm.setObjectAt(1, 1, gm.getHumanChar());
			gm.setObjectAt(2, 2, gm.getHumanChar());
			gm.setObjectAt(3, 3, gm.getHumanChar());
			gm.setObjectAt(4, 4, gm.getHumanChar());
			// System.out.println(controller.gameBoardToString());
			assert (gm.wonByDiagonal(gm.getHumanChar()));

			gm = new Connect4Model();
			controller = new Connect4Controller(gm);

			gm.setObjectAt(0, 2, gm.getHumanChar());
			gm.setObjectAt(1, 3, gm.getHumanChar());
			gm.setObjectAt(2, 4, gm.getHumanChar());
			gm.setObjectAt(3, 5, gm.getHumanChar());
			// System.out.println(controller.gameBoardToString());
			assert (gm.wonByDiagonal(gm.getHumanChar()));

			gm = new Connect4Model();
			controller = new Connect4Controller(gm);

			gm.setObjectAt(0, 0, gm.getHumanChar());
			gm.setObjectAt(1, 1, gm.getHumanChar());
			gm.setObjectAt(3, 3, gm.getHumanChar());
			gm.setObjectAt(4, 4, gm.getHumanChar());
			// System.out.println(controller.gameBoardToString());
			assert (!gm.wonByDiagonal(gm.getHumanChar()));

			gm = new Connect4Model();
			controller = new Connect4Controller(gm);

			gm.setObjectAt(2, 2, gm.getHumanChar());
			gm.setObjectAt(1, 1, gm.getHumanChar());
			gm.setObjectAt(3, 3, gm.getHumanChar());
			gm.setObjectAt(4, 4, gm.getHumanChar());
			// System.out.println(controller.gameBoardToString());
			assert (gm.wonByDiagonal(gm.getHumanChar()));
		} catch (model.IllegalArgumentException e) {
			// Nothing will happen. Test cases are all valid input.
		}
	}

	/**
	 * Test method for model.Connect4Model.wonByRow().
	 */
	@Test
	void testWonByRow() {
		Connect4Model gm = new Connect4Model();

		Connect4Controller controller = new Connect4Controller(gm);
		try {
		gm.setObjectAt(0, 0, gm.getHumanChar());
		gm.setObjectAt(1, 0, gm.getHumanChar());
		gm.setObjectAt(2, 0, gm.getHumanChar());
		gm.setObjectAt(3, 0, gm.getHumanChar());
		// System.out.println(controller.gameBoardToString());
		assert (gm.wonByRow(gm.getHumanChar()));

		gm = new Connect4Model();
		controller = new Connect4Controller(gm);

		gm.setObjectAt(0, 0, gm.getHumanChar());
		gm.setObjectAt(1, 0, gm.getHumanChar());
		gm.setObjectAt(4, 0, gm.getHumanChar());
		gm.setObjectAt(5, 0, gm.getHumanChar());
		// System.out.println(controller.gameBoardToString());
		assert (!gm.wonByRow(gm.getHumanChar()));
		} catch (model.IllegalArgumentException e) {
			// Test cases are all valid.
		}
	}

	/**
	 * Test method for Connect4Model.wonByCol().
	 */
	@Test
	void testWonByCol() {
		Connect4Model gm = new Connect4Model();

		Connect4Controller controller = new Connect4Controller(gm);
		try {
		gm.setObjectAt(0, 1, gm.getHumanChar());
		gm.setObjectAt(0, 2, gm.getHumanChar());
		gm.setObjectAt(0, 3, gm.getHumanChar());
		gm.setObjectAt(0, 4, gm.getHumanChar());
		// System.out.println(controller.gameBoardToString());
		assert (gm.wonByCol(gm.getHumanChar()));

		gm = new Connect4Model();
		controller = new Connect4Controller(gm);

		gm.setObjectAt(5, 1, gm.getHumanChar());
		gm.setObjectAt(5, 2, gm.getHumanChar());
		gm.setObjectAt(5, 4, gm.getHumanChar());
		gm.setObjectAt(5, 5, gm.getHumanChar());
		// System.out.println(controller.gameBoardToString());
		assert (!gm.wonByCol(gm.getHumanChar()));
		} catch (model.IllegalArgumentException e) {
			// Nothing to do.
		}
	}

	/**
	 * Test misc methods.
	 */
	@Test
	void testMisc() {
		Connect4Model gm = new Connect4Model();

		Connect4Controller controller = new Connect4Controller(gm);

		// Testing board toString() method.
		String empty_board = "_ _ _ _ _ _ _ " + "\n" + "_ _ _ _ _ _ _ " + "\n" + "_ _ _ _ _ _ _ " + "\n"
				+ "_ _ _ _ _ _ _ " + "\n" + "_ _ _ _ _ _ _ " + "\n" + "_ _ _ _ _ _ _ " + "\n" + "0 1 2 3 4 5 6\n";
		assertEquals(controller.gameBoardToString(), empty_board);

		// Testing other misc methods.
		assert (gm.isHumanMove());
		try {
			assertEquals(gm.getObjectAt(0, 0), gm.getBlankChar());
		} catch (model.IllegalArgumentException e) {
			// Guaranteed succeed. Nothing to do.
		}
		try {
			controller.placeToken(0);
		} catch (model.IllegalArgumentException e) {
			// Nothing to handle.
		}
		gm.switchTurn();
		assert (!gm.isHumanMove());
		gm.computerMove(gm.getComputerChar(), false);

		// Play a random game where computer vs computer.
		gm = new Connect4Model();
		controller = new Connect4Controller(gm);
		while (!controller.gameIsOver()) {
			gm.computerMove(gm.getHumanChar(), false);
			gm.switchTurn();
			gm.computerMove(gm.getComputerChar(), false);
			gm.switchTurn();
		}

		// Test tie game.
		gm = new Connect4Model();
		controller = new Connect4Controller(gm);
		for (int i = 0; i < gm.getMaxRow() * gm.getMaxCol(); i++)
			gm.switchTurn();
		try {
			gm.setObjectAt(0, 0, gm.getComputerChar());
			gm.setObjectAt(0, 1, gm.getHumanChar());
			gm.setObjectAt(0, 2, gm.getComputerChar());
			gm.setObjectAt(0, 3, gm.getHumanChar());
			gm.setObjectAt(0, 4, gm.getComputerChar());
			gm.setObjectAt(0, 5, gm.getHumanChar());

			gm.setObjectAt(1, 0, gm.getHumanChar());
			gm.setObjectAt(1, 1, gm.getComputerChar());
			gm.setObjectAt(1, 2, gm.getHumanChar());
			gm.setObjectAt(1, 3, gm.getComputerChar());
			gm.setObjectAt(1, 4, gm.getHumanChar());
			gm.setObjectAt(1, 5, gm.getComputerChar());

			gm.setObjectAt(2, 0, gm.getComputerChar());
			gm.setObjectAt(2, 1, gm.getHumanChar());
			gm.setObjectAt(2, 2, gm.getComputerChar());
			gm.setObjectAt(2, 3, gm.getHumanChar());
			gm.setObjectAt(2, 4, gm.getComputerChar());
			gm.setObjectAt(2, 5, gm.getHumanChar());

			gm.setObjectAt(3, 0, gm.getComputerChar());
			gm.setObjectAt(3, 1, gm.getHumanChar());
			gm.setObjectAt(3, 2, gm.getComputerChar());
			gm.setObjectAt(3, 3, gm.getHumanChar());
			gm.setObjectAt(3, 4, gm.getComputerChar());
			gm.setObjectAt(3, 5, gm.getHumanChar());

			gm.setObjectAt(4, 0, gm.getComputerChar());
			gm.setObjectAt(4, 1, gm.getHumanChar());
			gm.setObjectAt(4, 2, gm.getComputerChar());
			gm.setObjectAt(4, 3, gm.getHumanChar());
			gm.setObjectAt(4, 4, gm.getComputerChar());
			gm.setObjectAt(4, 5, gm.getHumanChar());

			gm.setObjectAt(5, 0, gm.getHumanChar());
			gm.setObjectAt(5, 1, gm.getComputerChar());
			gm.setObjectAt(5, 2, gm.getHumanChar());
			gm.setObjectAt(5, 3, gm.getComputerChar());
			gm.setObjectAt(5, 4, gm.getHumanChar());
			gm.setObjectAt(5, 5, gm.getComputerChar());

			gm.setObjectAt(6, 0, gm.getHumanChar());
			gm.setObjectAt(6, 1, gm.getComputerChar());
			gm.setObjectAt(6, 2, gm.getHumanChar());
			gm.setObjectAt(6, 3, gm.getComputerChar());
			gm.setObjectAt(6, 4, gm.getHumanChar());
			gm.setObjectAt(6, 5, gm.getComputerChar());
			
			// Try to insert a token into a full grid.
			assert(!controller.placeToken(0));
		} catch (model.IllegalArgumentException e) {
			// No exception will be caught.
		}
		assert (!gm.isBlank(6, 5));
		// System.out.println(controller.gameBoardToString());
		// System.out.println(gm.didWin(gm.getComputerChar()));
		// System.out.println(gm.didWin(gm.getHumanChar()));
		// System.out.println (gm.isTied());
		// System.out.println(gm.moveMaked);
		assert (gm.isTied());
		
		// Test exceptions and faulty input.
		final Connect4Model gm1 = new Connect4Model();
		final Connect4Controller controller1 = new Connect4Controller(gm1);
		assertThrows(model.IllegalArgumentException.class, () -> gm1.getObjectAt(-1, 0));
		assertThrows(model.IllegalArgumentException.class, () -> gm1.getObjectAt(7, 0));
		assertThrows(model.IllegalArgumentException.class, () -> gm1.getObjectAt(2, -1));
		assertThrows(model.IllegalArgumentException.class, () -> gm1.getObjectAt(2, 6));
		
		assertThrows(model.IllegalArgumentException.class, () -> gm1.setObjectAt(-1, 0, gm1.getHumanChar()));
		assertThrows(model.IllegalArgumentException.class, () -> gm1.setObjectAt(7, 0, gm1.getHumanChar()));
		assertThrows(model.IllegalArgumentException.class, () -> gm1.setObjectAt(2, -1, gm1.getHumanChar()));
		assertThrows(model.IllegalArgumentException.class, () -> gm1.setObjectAt(2, 6, gm1.getHumanChar()));
		
		assertThrows(model.IllegalArgumentException.class, () -> controller1.placeToken(-1));
		assertThrows(model.IllegalArgumentException.class, () -> controller1.placeToken(7));
		
		
	}

}
