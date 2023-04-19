# 2048
2048 project from CIS1200. First independent Javascript project. 
Warning: I do not condone cheating off my code.

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays:
     2D Arrays are perfect for 2048 because the game is played on a 4 by 4 layout.
     I plan on using a 2D array of Tiles that store the current int value on the
     associated tile (i.e. 2, 4, 8, 16, and 0 as a blank space). The board will
     begin with all 16 null tiles (associated with an int value of 0), and the
     game will end when every space has been filled up and there are no possible
     combines OR the player reaches 2048. 2D Arrays were also used for checking
     the board's game state (win or loss) and changing the state of the game by
     moving the tiles around.

  2. File I/O
     File I/O will be used to store game state and will help me save and load game
     progress. The saved game state will be in a text file, and to load said game
     state, the text file data will be read and parsed so that it can be displayed.
     Specifically, each int value of the tiles will be recorded into a text file
     with "," acting as a separator. Then to load the file, the int values will be
     read and placed back onto the 2D Array game board.

  3. Collections
     I revised this while coding because the idea of an "Undo" button didn't make
     much sense to me in 2048 - sort of takes the risk entirely out of the game, no?
     I also had a hard time debugging my undo. Therefore, I decided to use collections
     to store the coordinate locations of empty tiles on the 2D array game board.
     I created a LinkedList<Int[]> collection to store the coordinates of "null" tile
     values (which signify empty cells). Then using the LinkedList of int arrays I
     created, I checked the size to find how many empty tiles there were. Using a random
     int based on the size of LinkedList collection, I accessed a random empty tile
     coordinate, and spawned a randomly generated 2 or 4. The colors of the tiles are
     also stored in a TreeMap that helps associate specific tile int values with colors
     on the drawn game state.

  4. JUnit testable component
     JUnit testing will help me test if my gameplay is working without having to physically
     run the game. For example, some functionalities of 2048 that I test are: how the
     game ends when there are no more possible moves, how someone "wins" when they reach 2048,
     and edge cases like when someone makes the same move twice expecting something to happen.
     My game ran just fine, so I wasn't concerned with testing movements and combines. I was
     more concerned with testing issues pertaining to difficult-to-achieve boards that could
     potentially have edge cases for me to test. I tried testing a lot of "non-moving" edge
     cases that technically couldn't move in a specific direction given the positioning of
     the board, but wouldn't end the game. Some other general test cases are written though
     for a grand total of 16 cases.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  GameBoard initializes the board, helps listen for keyboard presses on the up/down/left/right
  buttons, helps create interface between RunTwentyFortyEight and GameLogic for buttons and
  other functions written in GameLogic, and draws the board.

  GameLogic is the bulk of the functionality for the game. It has my File I/O saving and loading
  logic, checking game state logic, spawning random tile logic, movement logic, and some testing
  helper methods for the JUnit testing. If you want a more in-depth look, I comment throughout
  the class and have very clear sections and explanations for each one.

  RunTwentyFortyEight runs the actual window to play the game by putting everything together
  between the graphics in GameBoard and the logic/game functionality in GameLogic.

  savedBoards.txt stores the File I/O saved state of the board. Look at GameLogic File I/O
  section for more in-depth details.

  Tile stores a class for my tiles. The Tile class helps the tile store the integer values
  associated to them and has a getter to access it, and sets up the combining feature. What
  that means is that it has a setter for the combine status, determines if two tiles can
  combine, and dictates what happens when they combine.

  GameTest just has JUnit tests. Explained above.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  I had a difficult time implementing "undo", but opted out of coding it because it didn't feel
  like it made too much sense in the context of 2048. I also had a hard time with File I/O concepts
  but a simple review of HW08 helped a lot. Having movement work was also a giant pain, I had to
  consult Rosetta Code for help there.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  I feel like my design is pretty good. I spent a solid amount of time on this. However, if
  I could change something, it'd be having the int values be more easily accessible from the
  Tile class. I'm not sure if they necessarily need to be gatekept behind a getter. It's not
  a huge deal though, I created getBoardVals to get around that, but I feel like not having
  "null" tiles and having to work between "null" and 0 interchangeably would be nice.



========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  https://rosettacode.org/wiki/2048#Java
  https://play2048.co/
  https://imagecolorpicker.com/en
