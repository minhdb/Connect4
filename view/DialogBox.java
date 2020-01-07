package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This DialogBox contains networking option for Connect4 networking game.
 * 
 * @author Weixiang Zhang
 *
 */
public class DialogBox extends Stage{

	private Button ok;
	private RadioButton rB1;
	private RadioButton rB2;
	private RadioButton rB3;
	private RadioButton rB4;
	private TextField serverName;
	private TextField portNumber;
	
	/**
	 * True if the server option is checked and false otherwise.
	 * 
	 * @return True if the server option is checked and false otherwise.
	 */
	public boolean serverIsCheck() {
		return rB1.isSelected();
	}
	
	/**
	 * True if human option is checked and false otherwise.
	 * 
	 * @return True if human option is checked and false otherwise.
	 */
	public boolean humanIsCheck() {
		return rB3.isSelected();
	}
	
	/**
	 * 
	 * @return The server's name in the text field.
	 */
	public String getServerName() {
		return serverName.getText();
	}
	
	/**
	 * 
	 * @return The port number in the text field.
	 */
	public int getPort() {
		return Integer.parseInt(portNumber.getText());
	}
	
	/**
	 * Returns the ok button object.
	 * 
	 * @return Returns the ok button object.
	 */
	public Button getOk() {
		return ok;
	}
	
	/**
	 * Constructs a dialog box that contains networking options.
	 */
	public DialogBox() {
		super();
		GridPane gridPane = new GridPane();
		gridPane.setVgap(25);
		gridPane.setPadding(new Insets(15, 15, 15, 15));
		
		Scene newScene = new Scene(gridPane);
		
		HBox hBox1 = new HBox();
		hBox1.setSpacing(8);
		Label label1 = new Label("Create: ");
		ToggleGroup group1 = new ToggleGroup();
		rB1 = new RadioButton("Server ");
		rB2 = new RadioButton("Client");
		rB1.setToggleGroup(group1);
		rB2.setToggleGroup(group1);
		
		hBox1.getChildren().addAll(label1,rB1,rB2);
		gridPane.add(hBox1, 0, 0);
		
		VBox vBox1 = new VBox();
		vBox1.setSpacing(25);
		
		HBox hBox2 = new HBox();
		hBox2.setSpacing(8);
		Label label2 = new Label("Play as: ");		
		ToggleGroup group2 = new ToggleGroup();
		rB3 = new RadioButton("Human ");
		rB4 = new RadioButton("Computer");
		rB3.setToggleGroup(group2);
		rB4.setToggleGroup(group2);
		
		hBox2.getChildren().add(label2);
		hBox2.getChildren().add(rB3);
		hBox2.getChildren().add(rB4);
			
		HBox hBox3 = new HBox();
		hBox3.setSpacing(8);
		Label server = new Label("Server");
		Label port = new Label("Port");
		serverName = new TextField("localhost");
		portNumber = new TextField("4000");
		hBox3.getChildren().addAll(server, serverName, port, portNumber);
		
		vBox1.getChildren().addAll(hBox2, hBox3);
		gridPane.add(vBox1, 0, 1);
		
		
		HBox hBox4 = new HBox();
		hBox4.setSpacing(8);
		ok = new Button("OK");
		Button cancel = new Button("Cancel");
		hBox4.getChildren().addAll(ok, cancel);
		
		gridPane.add(hBox4, 0, 2);
		cancel.setOnAction(ActionEvent -> {
			this.close();
		});
		this.setScene(newScene);
	}

	/**
	 * True if client button is checked.
	 * 
	 * @return True if client button is checked and false otherwise.
	 */
	public boolean clientIsCheck() {
		return rB2.isSelected();
	}

	/**
	 * True if computer button is checked.
	 * 
	 * @return True if computer button is checked.
	 */
	public boolean computerIsCheck() {
		return rB4.isSelected();
	}


}