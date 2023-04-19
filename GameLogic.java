package org.cis1200.TwentyFortyEight;

import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 * This class is the Logic behind TwentyFortyEight
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 * 
 * Run this file to see the main method play a game of TwentyFortyEight,
 * visualized with Strings printed to the console.
 */
public class GameLogic {

    //Initializing the board and its width and height (permanently at 4)
    private Tile[][] board;
    private final int width = 4;
    private final int height = 4;

    /**==========================================================================
     * Game States (2d Arrays)
     * Includes:
     *      1. [START] GameLogic starts the game by invoking reset
     *          -> reset game and construct board
     *          (reset game state, make/remake 2d array board, & spawn 2 tiles)
     *      2. [WIN] Uses getBoardVals to convert Tile[][] into int[][]
     *          and checking if 2048 exists to determine win
     *      3. [LOSS] Checks if game is won and if you can move. If none of those
     *          statements are true, a loss is determined
     *===========================================================================
     */

    public GameLogic() {
        resetSaveState(); //reset txt file
        reset(); //my reset method sets up the whole game
    }

    public void reset() {
        board = new Tile[width][height]; //reset board

        spawnRandomTile(); //spawn 2 random tiles at the start/reset of game
        spawnRandomTile();
    }

    public int[][] getBoardVals() {
        int[][] tileValBoard = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[i][j] == null) {
                    tileValBoard[i][j] = 0; //if null input 0 int
                } else { //else get the tile val as int
                    tileValBoard[i][j] = board[i][j].getTileVal();
                }
            }
        }
        return tileValBoard;
    }

    public boolean winningGameState() {
        int[][] tileValBoard = getBoardVals();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (tileValBoard[i][j] == 2048) { //if 2048 exists, you win!
                    return true;
                }
            }
        }
        return false;
    }

    public boolean losingGameState() {
        if (winningGameState()) { //already won
            return false;
        }
        if (canMove()) { //if you can move, you ain't winnin
            return false;
        }
        return true; //nothing else checks out? u lost.
    }

    /**===================================================================================
     * Save, Load, and Reset Game States (File I/O)
     * Includes:
     *      1. [SAVE] Save your current game state into a file by writing in every integer
     *          on the 2d array into a text file
     *      2. [LOAD] Load in your saved game state from a text file reading every integer
     *          in the file and parsing it into its proper location on the 2d Array
     *      3. [RESET SAVE] resetSaveState resets the save state every time you open
     *          start the game. Not to be confused with the reset button that resets
     *          the game board. The save state will only be reset upon starting the game
     *===================================================================================
     */

    public void saveGameToFile() {
        File savedBoardsFile = Paths.get("savedBoards.txt").toFile();
        BufferedWriter bwBoard;

        try {
            bwBoard = new BufferedWriter(new FileWriter(savedBoardsFile, false));
            int[][] tileValboard = getBoardVals(); //use int vals not Tile vals

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bwBoard.write(tileValboard[i][j] + ","); //write the values separated by ","
                }
                bwBoard.newLine(); //new line once row is done
            }
            bwBoard.close(); //DONE!

        } catch (IOException e) {
            System.out.println(
                    "Error: something went wrong with resetting file data");
        }
    }

    public void loadGameFromFile() {
        BufferedReader brBoard;

        try {
            brBoard = new BufferedReader(new FileReader("savedBoards.txt"));


            for (int i = 0; i < width; i++) { //load board
                String line = brBoard.readLine(); //read the lines
                String[] tileVals = line.split(","); //split line by the separator
                for (int j = 0; j < height; j++) {
                    //parse values into new array
                    board[i][j] = new Tile(Integer.parseInt(tileVals[j]));
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Error: savedBoard is NULL, you must save a game state before loading!");
        }
    }

    public void resetSaveState() {
        File savedBoardsFile = Paths.get("savedBoards.txt").toFile();
        BufferedWriter bwBoards;

        try {
            bwBoards = new BufferedWriter(new FileWriter(savedBoardsFile, false));
            bwBoards.write(""); //empty ""
            bwBoards.close(); //DONE!
        } catch (IOException ex) {
            System.out.println(
                    "Error: something went wrong with resetting file data");
        }
    }

    /**========================================================================================
     * Random Tile Generation Logic (Collections)
     * Includes:
     *      1. Helper function to pick random tile value
     *      2. Method that uses collections to create a list of empty tile positions
     *      3. Method that spawns random tile value from randomly picked empty tile position
     * ========================================================================================
     */
    //25% chance to generate a 4 | 75% chance to generate a 2
    public Tile generateRandomTile() {
        if (Math.random() < 0.25) {
            return new Tile(4);
        } else {
            return new Tile(2);
        }
    }

    //Create a LinkedList of the positions on the board that are empty
    public LinkedList<int[]> getEmptyTilePosList() {
        LinkedList<int[]> emptyTileList = new LinkedList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j] == null) {
                    int[] emptyTiles = {i, j};
                    emptyTileList.add(emptyTiles); //adding empty positions into LinkedList
                }
            }
        }
        return emptyTileList;
    }

    //Spawning a random tile on one of those empty positions
    public void spawnRandomTile() {
        //get empty tile position LinkedList
        LinkedList<int[]> emptyTilePosList = getEmptyTilePosList();

        if (emptyTilePosList.size() != 0) { //there exists empty tiles
            //choosing a random empty tile based off the size of the empty tile list
            int randomEmptyTile = (int) (emptyTilePosList.size() * Math.random());
            //accessing the random empty tile position in the LinkedList
            int[] randomEmptyTilePos = emptyTilePosList.get(randomEmptyTile);
            //generating random tile on accessed position
            board[randomEmptyTilePos[0]][randomEmptyTilePos[1]] = generateRandomTile();
        }
    }

    /**=============================================================================
     * Tile Moving Logic (2d Arrays)
     * Includes:
     *      1. Boolean to check if the board's current state can move
     *      2. Move method that is heavily influenced by Rosetta Stone's movement
     *         (Note: since most of the code is from Rosetta Stone,
     *          I will not count this towards my "4 concepts" if not possible)
     *      3. Using move method to create directional movement
     *==============================================================================
     */
    private boolean checkMoves;

    //iterate through 2d array and set status to false for new combines
    private void resetCanCombine() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[i][j] != null) {
                    board[i][j].setCombineStatus(false);
                }
            }
        }
    }

    boolean canMove() {
        checkMoves = true;
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkMoves = false;
        return hasMoves;
    }

    public boolean move(int bounds, int x, int y) {
        boolean hasMoved = false;

        for (int i = 0; i < width * height; i++) {
            int j = Math.abs(bounds - i);
            int row = j / width;
            int col = j % height;

            if (board[row][col] == null) { //move past null spaces
                continue;
            }
            int movedRow = row + y; //incremented row motion
            int movedCol = col + x; //incremented col motion

            //while within bounds do:
            while (movedRow >= 0 && movedRow < width && movedCol >= 0 && movedCol < height) {
                Tile newPos = board[movedRow][movedCol]; //board after moving
                Tile currPos = board[row][col]; //board before moving

                if (newPos == null) { //if new possible pos is null/empty, move through it
                    if (checkMoves) {
                        return true;
                    }
                    board[movedRow][movedCol] = currPos; //fill new position
                    board[row][col] = null; //last position becomes null
                    row = movedRow; //shift to new row pos
                    col = movedCol; //shift to new col pos
                    movedRow = movedRow + y; //increment row movement again
                    movedCol = movedCol + x; //increment col movement again
                    hasMoved = true;
                } else if (newPos.canCombineWith(currPos)) { //check if tiles can combine
                    if (checkMoves) {
                        return true;
                    }
                    //combine tiles and update board accordingly
                    newPos.combineWith(currPos);
                    board[row][col] = null;
                    hasMoved = true;
                    break;
                } else {
                    //if no more spaces to move through, break
                    break;
                }
            }
        }
        if (hasMoved) { //checking movement
            if (!winningGameState()) {
                resetCanCombine(); //reset combine state
//spawnRandomTile(); //spawn in random tile, removed for testing, will move to GameBoard
            }
        }
        return hasMoved;
    }

    //actual movements in respect to direction
    boolean moveUp() {
        return move(0, -1, 0);
    }
    boolean moveDown() {
        return move(15, 1, 0);
    }
    boolean moveLeft() {
        return move(0, 0, -1);
    }
    boolean moveRight() {
        return move(15, 0, 1);
    }


    /**==========================================================================
     * Testing Functions (JUnit Testable Component)
     * Includes:
     *      1. setTilePos helps me with "creating" board scenarios to test "hard
     *         to replicate in game" test cases
     *      2. printBoard is being used to view the positioning of the board
     *         after inputting commands
     *===========================================================================
     */

    public void setTilePos(int row, int col, Tile tile) {
        board[row][col] = tile;
    }

    public void printBoard() {
        int[][] tileValBoard = getBoardVals();

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                System.out.print(tileValBoard[row][col] + ", ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
