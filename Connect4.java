/**
 * This class contains the main method for running the game.
 * 
 */

//import view.Connect4TextView;
import view.Connect4View;

import view.DialogBox;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observer;

import controller.Connect4Controller;
import model.Connect4Model;
import model.Connect4MoveMessage;
import model.IllegalArgumentException;
import model.Minimax_AlphaBeta;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import javafx.stage.Modality;
import javafx.scene.control.Alert;

public class Connect4 extends Application implements Runnable {
	private Observer currentView;
	private Observer guiView;
	private BorderPane window;

	private Connect4Model gameModel;

	private static final int width = 344;
	private static final int height = 326;

	private static final String SERIALIZED_MODEL_NAME = "save_game.dat";
	private DialogBox dialogBox;
	private ServerSocket server;
	private Socket socket;
	Connect4MoveMessage mvMsg = null;

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * This method is to generate a GUI that can interact with the user and contains
	 * a menu item to start a new game, and close with a saved game
	 * 
	 * @param primaryStage the window of the GUI
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		boolean foundSerializedFile = this.deserializeGameModel();
		if (!foundSerializedFile) {
			initGame();
		} else {

		}

		primaryStage.setTitle("Connect 4");
		window = new BorderPane();
		Scene scene = new Scene(window, width, height);

		// Add MenuBar
		MenuBar menuBar = new MenuBar();

		// Do this for more responsive UI when resizing the window.
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

		MenuItem newGameButton = new MenuItem();

		Menu FileMenu = new Menu();
		FileMenu.setText("File");

		MenuItem networkedButton = new MenuItem("Networked Game");

		newGameButton.setText("New Game");
		// construct a new game model

		newGameButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				// gameModel.newGame();
				gameModel = new Connect4Model();
				modelSetting(foundSerializedFile, true, false, true);
			}
		});

		networkedButton.setOnAction(event -> {
			dialogBox = new DialogBox();
			dialogBox.setTitle("Network Setup");
			dialogBox.initModality(Modality.APPLICATION_MODAL);
			dialogBox.getOk().setOnAction(e -> {
				gameModel = new Connect4Model();
				if (dialogBox.serverIsCheck() || dialogBox.clientIsCheck()) {
					if (dialogBox.humanIsCheck() || dialogBox.computerIsCheck()) {
						if (dialogBox.serverIsCheck()) {
							modelSetting(false, dialogBox.humanIsCheck(), true, true);
							createServer(dialogBox.getPort());

						} else {
							modelSetting(false, dialogBox.humanIsCheck(), true, false);
							createClient(dialogBox.getServerName(), dialogBox.getPort());

						}
					} else {
						new Alert(Alert.AlertType.ERROR, "Please select Human or Computer").showAndWait();
						return;
					}
				} else {
					new Alert(Alert.AlertType.ERROR, "Please select Server or Client").showAndWait();
					return;
				}

				Thread networkThread = new Thread(this);
				networkThread.start();
				dialogBox.close();

			});

			dialogBox.show();
		});

		FileMenu.getItems().add(newGameButton);
		FileMenu.getItems().add(networkedButton);
		menuBar.getMenus().add(FileMenu);

		window.setTop((Node) menuBar);

		modelSetting(foundSerializedFile, true, false, true);

		// if the game is not over, create a file storing the game(model object)
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				if (gameModel.isOver())
					((Connect4View) guiView).deleteSerializedFile();
				else
					serializeGameModel();

			}
		});

		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest((event) -> {
			closeNetwork();
		});
		primaryStage.show();
	}

	/**
	 * This method is to set up the model by linking it to the GUIView(Observer) and
	 * controller
	 * 
	 * @param foundSerializedFile true means that there is a serializedFile under
	 *                            the current directory otherwise not
	 */
	private void modelSetting(boolean foundSerializedFile, boolean isFirstHuman, boolean isNetworkGame,
			boolean moveFirst) {
		Connect4Controller gameController = new Connect4Controller(gameModel);
		guiView = new Connect4View(gameModel, gameController, foundSerializedFile, isFirstHuman, isNetworkGame,
				moveFirst);
		setViewTo(guiView);
		gameModel.addObserver(guiView);
		gameModel.addObserver(currentView);
	}

	private void createServer(int port) {
		try {
			server = new ServerSocket(port);
			socket = server.accept();

			System.out.println("Client connected: " + socket.getInetAddress());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createClient(String serverName, int port) {
		try {
			socket = new Socket(serverName, port);
			System.out.println("Connected to server at: " + socket.getInetAddress() + " with port: " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is to create a new instance for gameModel
	 */
	private void initGame() {
		gameModel = new Connect4Model();
	}

	/**
	 * This method is to set the GUI to the center of the pane
	 * 
	 * @param newView
	 */
	private void setViewTo(Observer newView) {
		window.setCenter(null);
		currentView = newView;
		window.setCenter((Node) currentView);
	}

	/**
	 * This method is to serialize the game model object.
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
	 * This method will return true if found a serialized file to deserialize from
	 * and false otherwise.
	 * 
	 * @return True if found a serialized file to deserialize from.
	 */
	public boolean deserializeGameModel() {
		try {
			FileInputStream inputFile = new FileInputStream("./" + SERIALIZED_MODEL_NAME);
			ObjectInputStream ois = new ObjectInputStream(inputFile);
			this.gameModel = (Connect4Model) ois.readObject();

			this.gameModel.setComputerPlayer(new Minimax_AlphaBeta());
			System.out.println("Found a serialized file.");
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

	@Override
	public void run() {
		Connect4MoveMessage oldMvMsg = null;
		ObjectOutputStream output = null;
		ObjectInputStream input = null;

		// This instance of program is a server.
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (!gameModel.isOver() && socket.isConnected()) {
			System.out.println("Networking thread is still running.");
			if (server != null) {
				try {
					if (gameModel.isHumanMove()) {
						if (!dialogBox.humanIsCheck()) {
							gameModel.computerMove(gameModel.getHumanChar(), true);
							mvMsg = gameModel.getMoveMsg();
							output.writeObject(mvMsg);
							output.flush();
							oldMvMsg = mvMsg;
							gameModel.switchTurn();
							mvMsg = null;
							continue;
						}

						mvMsg = gameModel.getMoveMsg();
						if (mvMsg == null)
							continue;

						if (oldMvMsg != null) {
							if ((mvMsg.getRow() == oldMvMsg.getRow() && mvMsg.getColumn() == oldMvMsg.getColumn()
									&& mvMsg.getColor() == oldMvMsg.getColor()))
								continue;
						}

						System.out.println(mvMsg.getRow() + " " + mvMsg.getColumn() + " " + mvMsg.getColor());
						if (!mvMsg.equals(oldMvMsg)) {
							if (oldMvMsg == null) {
								output.writeObject(mvMsg);
								output.flush();
								oldMvMsg = mvMsg;
								gameModel.switchTurn();
								mvMsg = null;
							} else {
								if ((mvMsg.getRow() != oldMvMsg.getRow()) || mvMsg.getColumn() != oldMvMsg.getColumn()
										|| mvMsg.getColor() != oldMvMsg.getColor()) {
									output.writeObject(mvMsg);
									output.flush();
									oldMvMsg = mvMsg;
									gameModel.switchTurn();
									mvMsg = null;
								}
							}
						}
					} else {
						mvMsg = (Connect4MoveMessage) input.readObject();
						if (mvMsg == null)
							continue;
						if (oldMvMsg != null) {
							if ((mvMsg.getRow() == oldMvMsg.getRow() && mvMsg.getColumn() == oldMvMsg.getColumn()
									&& mvMsg.getColor() == oldMvMsg.getColor()))
								continue;
						}
						if (!mvMsg.equals(oldMvMsg)) {
							if (oldMvMsg == null) {
								System.out.println(mvMsg.getRow() + " " + mvMsg.getColumn() + " " + mvMsg.getColor());

								((Connect4View) currentView).getController().placeToken(mvMsg.getColumn());

								gameModel.printRawBoard();
								gameModel.switchTurn();
							} else {
								if ((mvMsg.getRow() != oldMvMsg.getRow()) || mvMsg.getColumn() != oldMvMsg.getColumn()
										|| mvMsg.getColor() != oldMvMsg.getColor()) {
									System.out
											.println(mvMsg.getRow() + " " + mvMsg.getColumn() + " " + mvMsg.getColor());
									((Connect4View) currentView).getController().placeToken(mvMsg.getColumn());

									gameModel.printRawBoard();
									gameModel.switchTurn();
								}
							}
							oldMvMsg = mvMsg;
							mvMsg = null;
						}

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// This instance of program is a client.
			} else {
				try {
					if (!gameModel.isHumanMove()) {
						if (!dialogBox.humanIsCheck()) {
							gameModel.computerMove(gameModel.getComputerChar(), false);
							mvMsg = gameModel.getMoveMsg();
							output.writeObject(mvMsg);
							output.flush();
							oldMvMsg = mvMsg;
							gameModel.switchTurn();
							mvMsg = null;
							continue;

						}
						mvMsg = gameModel.getMoveMsg();
						if (mvMsg == null)
							continue;
						if (oldMvMsg != null) {
							if ((mvMsg.getRow() == oldMvMsg.getRow() && mvMsg.getColumn() == oldMvMsg.getColumn()
									&& mvMsg.getColor() == oldMvMsg.getColor()))
								continue;
						}

						System.out.println(mvMsg.getRow() + " " + mvMsg.getColumn() + " " + mvMsg.getColor());
						if (!mvMsg.equals(oldMvMsg)) {
							if (oldMvMsg == null) {
								output.writeObject(mvMsg);
								output.flush();
								oldMvMsg = mvMsg;
								gameModel.switchTurn();
								mvMsg = null;
							} else {
								if ((mvMsg.getRow() != oldMvMsg.getRow()) || mvMsg.getColumn() != oldMvMsg.getColumn()
										|| mvMsg.getColor() != oldMvMsg.getColor()) {
									output.writeObject(mvMsg);
									output.flush();
									oldMvMsg = mvMsg;
									gameModel.switchTurn();
									mvMsg = null;
								}
							}
						}

					} else {
						mvMsg = (Connect4MoveMessage) input.readObject();
						if (mvMsg == null)
							continue;
						if (oldMvMsg != null) {
							if ((mvMsg.getRow() == oldMvMsg.getRow() && mvMsg.getColumn() == oldMvMsg.getColumn()
									&& mvMsg.getColor() == oldMvMsg.getColor()))
								continue;
						}

						if (!mvMsg.equals(oldMvMsg)) {
							if (oldMvMsg == null) {
								System.out.println(mvMsg.getRow() + " " + mvMsg.getColumn() + " " + mvMsg.getColor());
								((Connect4View) currentView).getController().placeToken(mvMsg.getColumn());

								gameModel.printRawBoard();
								gameModel.switchTurn();
							} else {
								if ((mvMsg.getRow() != oldMvMsg.getRow()) || mvMsg.getColumn() != oldMvMsg.getColumn()
										|| mvMsg.getColor() != oldMvMsg.getColor()) {
									System.out
											.println(mvMsg.getRow() + " " + mvMsg.getColumn() + " " + mvMsg.getColor());

									((Connect4View) currentView).getController().placeToken(mvMsg.getColumn());

									gameModel.printRawBoard();
									gameModel.switchTurn();

								}
							}
							oldMvMsg = mvMsg;
							mvMsg = null;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void closeNetwork() {
		try {
			if (server != null)
				server.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
