/**
 * GUI view for Connect4.
 * 
 * @author Minh Bui
 */

package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;

import controller.Connect4Controller;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Connect4Model;
import model.Connect4MoveMessage;

public class Connect4View extends TilePane implements java.util.Observer, Runnable {
	private Connect4MoveMessage moveMessage;
	private Connect4Model gameModel;
	private Connect4Controller controller;

	private final int CIRCLE_RADIUS = 20;

	public int getCircleRadius() {
		return CIRCLE_RADIUS;
	}

	public int getSpace() {
		return SPACE;
	}

	private final int SPACE = 8;

	private static final Color HUMAN_COLOR = Color.YELLOW;
	private static final Color COMPU_COLOR = Color.RED;

	private static final String SERIALIZED_MODEL_NAME = "save_game.dat";

	public Connect4View(Connect4Model gameModel, Connect4Controller gameController, boolean foundSerializedFile,
			boolean isHuman, boolean isNetworkGame, boolean moveFirst) {
		this.gameModel = gameModel;
		this.controller = gameController;

		Insets inset = new Insets(SPACE, SPACE, SPACE, SPACE);
		this.setPadding(inset);
		this.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
		this.setHgap(SPACE);
		this.setVgap(SPACE);
		boardInit();
		if (!foundSerializedFile) {

		} else {
			updateTile();
		}

		if (isNetworkGame) {
			if (isHuman) {
				this.setOnMouseClicked((event) -> {
					System.out.println("This is a network game and is played by human.");
					double x = event.getSceneX();
					int col = 0;
					if (x >= 0 && x <= CIRCLE_RADIUS * 2 + SPACE + SPACE / 2)
						col = 0;
					else if (x >= (CIRCLE_RADIUS * 2 + SPACE + SPACE / 2 + (CIRCLE_RADIUS * 2 + SPACE) * 5))
						col = 6;
					else {
						col = (int) x / (CIRCLE_RADIUS * 2 + SPACE);
					}

					try {
						if (!controller.placeToken(col)) {
							Platform.runLater(new Runnable() {
								public void run() {
									showAlert(true, "Column fill, pick somewhere else!");
								}
							});
						} else {
							//this.gameModel.switchTurn();
						}
					} catch (model.IllegalArgumentException e) {
						
					}
				});
				
			} else {

				//this.gameModel.computerMove(controller.playerTurnToString().charAt(0), moveFirst);
				//this.gameModel.switchTurn();
			}
		} else {
			this.setOnMouseClicked((event) -> {
				System.out.println("Normal game.");
				double x = event.getSceneX();
				try {
					if (!this.gameModel.isOver()) {
						int col = 0;
						if (x >= 0 && x <= CIRCLE_RADIUS * 2 + SPACE + SPACE / 2)
							col = 0;
						else if (x >= (CIRCLE_RADIUS * 2 + SPACE + SPACE / 2 + (CIRCLE_RADIUS * 2 + SPACE) * 5))
							col = 6;
						else {
							col = (int) x / (CIRCLE_RADIUS * 2 + SPACE);
						}

						if (!controller.placeToken(col)) {
							showAlert(true, "Column fill, pick somewhere else!");
						} else {

							this.gameModel.switchTurn();

							if (this.gameModel.isOver()) {
								// Delete the serialize file if it exists.
								deleteSerializedFile();
								return;
							}
							this.gameModel.computerMove(this.gameModel.getComputerChar(), false);
							this.gameModel.switchTurn();

						}

						updateTile();
					} else {
						// Delete the serialized file if it exists.
						deleteSerializedFile();

					}
				} catch (model.IllegalArgumentException e) {

				}
			});
		}

	}

	/**
	 * Delete the serialized file if it exists.
	 */
	public void deleteSerializedFile() {
		File savData = new File("./" + SERIALIZED_MODEL_NAME);
		if (savData.exists()) {
			System.out.println("save_data.dat exists. Deleting.");
			savData.delete();
		}
	}

	/**
	 * Initialize every cell in this board to white color.
	 */
	private void boardInit() {
		for (int i = 0; i < gameModel.getMaxCol() * gameModel.getMaxRow(); i++)
			this.getChildren()
					.add(new Circle(CIRCLE_RADIUS + SPACE, CIRCLE_RADIUS + SPACE, CIRCLE_RADIUS, Color.WHITE));
	}

	/**
	 * This method is called whenever there's a change in the game's board.
	 */
	private void updateTile() {

		char[][] board = gameModel.getBoard();

		// Note:
		// Changing from creating a new circle to only changing the color of the circle.
		// Much more efficient.
		// Credit: Weixiang.
		for (int j = gameModel.getMaxCol() - 1; j >= 0; j--) {
			for (int i = 0; i < gameModel.getMaxRow(); i++)
				if (board[i][j] == gameModel.getBlankChar()) {
					// this.getChildren().set(((gameModel.getMaxCol() - j - 1) * board.length + i),
					// new Circle(CIRCLE_RADIUS + SPACE, CIRCLE_RADIUS + SPACE, CIRCLE_RADIUS,
					// Color.WHITE));

					((Circle) this.getChildren().get(((gameModel.getMaxCol() - j - 1) * board.length + i)))
							.setFill(Color.WHITE);
				} else if (board[i][j] == gameModel.getHumanChar()) {
					// this.getChildren().set(((gameModel.getMaxCol() - 1 - j) * board.length + i),
					// new Circle(CIRCLE_RADIUS + SPACE, CIRCLE_RADIUS + SPACE, CIRCLE_RADIUS,
					// HUMAN_COLOR));

					((Circle) this.getChildren().get(((gameModel.getMaxCol() - j - 1) * board.length + i)))
							.setFill(HUMAN_COLOR);
				} else if (board[i][j] == gameModel.getComputerChar()) {
					// this.getChildren().set(((gameModel.getMaxCol() - 1 - j) * board.length + i),
					// new Circle(CIRCLE_RADIUS + SPACE, CIRCLE_RADIUS + SPACE, CIRCLE_RADIUS,
					// COMPU_COLOR));

					((Circle) this.getChildren().get(((gameModel.getMaxCol() - j - 1) * board.length + i)))
							.setFill(COMPU_COLOR);
				}
		}

		/**
		 * COMMENT: Connect4MoveMessage class seems redundant here.
		 */
		/*
		 * 
		 * 
		 * 
		 * Connect4MoveMessage moveMessage = gameModel.getmoveMessage();
		 * 
		 * // Fill out the circle in the slot that has been changed. if
		 * (moveMessage.getPlayer() == gameModel.getComputerChar()) {
		 * this.getChildren().set( ((gameModel.getMaxCol() - 1 - moveMessage.getY()) *
		 * gameModel.getMaxRow() + moveMessage.getX()), new Circle(CIRCLE_RADIUS +
		 * SPACE, CIRCLE_RADIUS + SPACE, CIRCLE_RADIUS, COMPU_COLOR)); } else if
		 * (moveMessage.getPlayer() == gameModel.getHumanChar()) {
		 * this.getChildren().set( ((gameModel.getMaxCol() - 1 - moveMessage.getY()) *
		 * gameModel.getMaxRow() + moveMessage.getX()), new Circle(CIRCLE_RADIUS +
		 * SPACE, CIRCLE_RADIUS + SPACE, CIRCLE_RADIUS, HUMAN_COLOR)); }
		 */

	}

	/**
	 * This method issues a message to the user. In this project, it's used to
	 * declare who's the winner or if the game's tied. It also indicates if there
	 * was an error or not.
	 * 
	 * @param error true if the issued message is an error and false if it's a
	 *              normal message.
	 * @param msg   A string for message.
	 */
	private void showAlert(boolean error, String msg) {
		Alert alert = null;
		if (error) {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
		} else
			alert = new Alert(AlertType.INFORMATION);

		alert.setContentText(msg);
		alert.showAndWait();
	}

	private void sub_update(Object arg) {
		moveMessage = (Connect4MoveMessage) arg;
		System.out.println("Row: " + moveMessage.getRow() + " Col: " + moveMessage.getColumn());
		// Second player is 2 (red). First player is 1 (yellow).
		if (moveMessage.getColor() == 2) {
			int ind = (moveMessage.getRow()) * gameModel.getMaxRow() + moveMessage.getColumn();

			((Circle) this.getChildren().get(ind)).setFill(COMPU_COLOR);
		} else if (moveMessage.getColor() == 1) {
			int ind = (moveMessage.getRow()) * gameModel.getMaxRow() + moveMessage.getColumn();
			((Circle) this.getChildren().get(ind)).setFill(HUMAN_COLOR);
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		sub_update(arg);
		gameModel = (Connect4Model) o;
		if (gameModel.didWin(gameModel.getHumanChar())) {
			
			Platform.runLater(new Runnable() {
				public void run() {
					showAlert(false, "First player win!");
				}
			});
			//showAlert(false, "You win!");
			deleteSerializedFile();
		} else if (gameModel.didWin(gameModel.getComputerChar())) {
			Platform.runLater(new Runnable() {
				public void run() {
					showAlert(false, "Second player win!");
				}
			});
			//showAlert(false, "You lose!");
			deleteSerializedFile();
		} else if (gameModel.isTied()) {
			Platform.runLater(new Runnable() {
				public void run() {
					showAlert(false, "Tied game.");
				}
			});
			//showAlert(false, "Tied game.");
			deleteSerializedFile();
		}

	}

	/**
	 * Serialize the game model object.
	 */
	public void serializeGameModel() {
		try {
			FileOutputStream outputFile = new FileOutputStream("./" + SERIALIZED_MODEL_NAME);
			ObjectOutputStream oos = new ObjectOutputStream(outputFile);
			oos.writeObject(gameModel);
			oos.close();
			outputFile.close();
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
	}

	/**
	 * Returns true if found a serialized file to deserialize from and false
	 * otherwise.
	 * 
	 * @return True if found a serialized file to deserialize from.
	 */
	public boolean deserializeGameModel() {
		try {
			FileInputStream inputFile = new FileInputStream("./" + SERIALIZED_MODEL_NAME);
			ObjectInputStream ois = new ObjectInputStream(inputFile);
			gameModel = (Connect4Model) ois.readObject();
			ois.close();
			inputFile.close();
			return true;
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			return false;
		} catch (ClassNotFoundException ex) {
			System.err.println(ex.getStackTrace());
			return false;
		}
	}

	public Connect4Controller getController() {
		return controller;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		updateTile();
	}
}
