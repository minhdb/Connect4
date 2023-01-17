# Connect 4

## Description
Connect 4 is a two-player zero-sum connection game. For more details on rules, please refer to: https://en.wikipedia.org/wiki/Connect_Four

![Connect4](https://i.imgur.com/MibgAOA.png)

## How to run the code locally

### Requirements:

- Java RE 8
- JavaFX 2

Clone the repository (duh)

```
git clone https://github.com/minhdb/ChatRoom.git
```

Here are the steps for IntelliJ IDEA. Other IDEs should have similar settings.

`File -> Project Structure -> Module -> Select Java SDK`

Add JavaFX as a dependency.

`Run -> Edit Configuration ... -> Add VM option`

```
--module-path [PATH_TO_JAVA_FX]/lib --add-modules=javafx.controls,javafx.fxml
```

## Features
· Allows either the AI or the user to play.

· It is possible for the AI to play against itself.

· Auto-saved game on close using serialization.


## Implementation
The game's implemented using the MVC design pattern.

### Intelligent Computer Player
Computer player's using Minimax alogrithms with alpha-beta pruning. More details here: https://en.wikipedia.org/wiki/Minimax 

## Network Protocol
### Establishing Client and Server Roles
The Server will always take the first turn. If it is a human player, the player will click and send the event to the client. Otherwise the AI will generate its turn and send it to the client. The client will go second. This will repeat until the game is over.

A move made over network is implemented using serialization. Here's the details:
```
public class Connect4MoveMessage implements Serializable {

       public static int YELLOW = 1;

       public static int RED = 2;

       private static final long serialVersionUID = 1L;

       private int row;

       private int col;

       private int color;


       public Connect4MoveMessage(int row, int col, int color) { … }
      
       public int getRow() { … }

       public int getColumn() { … }

       public int getColor() { … }

}
```





 

 
