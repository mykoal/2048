package org.cis1200.TwentyFortyEight;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {

    /**
     * Tests (JUnit Tests)
     * Since my game runs well, I want to do JUnit tests on edge cases on "rare" boards
     * I don't necessarily want to test simple movement or combines.
     * These can easily be seen working while actually playing the game.
     * What I'm looking for is situations that I can't easily replicate while playing.
     * This is predominately going to be focused on vertical and horizontal move-lock cases
     */
    GameLogic logic = new GameLogic();

    //Test File I/O
    @Test
    public void writeToAndReadFrom() {
        logic.resetSaveState();
        logic.spawnRandomTile();
        logic.saveGameToFile(); //save the random spawned tile (1 tile present)
        int[][] savedBoard = logic.getBoardVals();
        logic.spawnRandomTile(); //spawn another tile (2 tiles present)
        logic.loadGameFromFile(); //load to save state (1 tile present)
        int[][] expectedBoard = logic.getBoardVals();
        assertTrue(Arrays.deepEquals(savedBoard, expectedBoard)); //same :)
    }

    //Test Tile Spawning
    @Test
    public void spawnRandomTileOnEmptyPosTest() {
        logic.spawnRandomTile(); //2 originally spawned tiles + 1 more new randomly spawned tile
        int emptyTilesOnBoard = logic.getEmptyTilePosList().size(); //should be 16 - 3
        assertEquals(13, emptyTilesOnBoard);
        logic.printBoard(); //new board every time
    }

    //Deceptive test
    @Test
    public void testMoveRight() {
        logic.setTilePos(0, 0, new Tile(16));
        logic.moveRight();
        int[][] expectedBoard = { { 0, 0, 0, 16 },
                                  { 0, 0, 0, 0 },
                                  { 0, 0, 0, 0 },
                                  { 0, 0, 0, 0 } };
        //you'd think it's true, but there will be a randomly spawned tile after movement
        //so any of those "expected" 0s could be a 2 or 4
        //not to mention the 2 random tiles that are spawned on game start
        assertFalse(Arrays.deepEquals(logic.getBoardVals(), expectedBoard));

        //printBoard() helps a lot, I can really visualize the random nature of the actual board
        logic.printBoard();
    }

    //actually testing movement will be hard because
    //whenever movement is invoked, a random tile is spawned
    //must personally insert every value to test
    //still concerned about the random spawned tile after movement though,
    //how will I locate it in expected?
    @Test
    public void testPersonalGetAroundForInitialRandomSpawnedTiles() {
        logic.setTilePos(0, 0, new Tile(4));
        logic.setTilePos(1, 0, new Tile(4));
        logic.setTilePos(2, 0, new Tile(4));
        logic.setTilePos(3, 0, new Tile(4));

        logic.setTilePos(0, 1, new Tile(4));
        logic.setTilePos(1, 1, new Tile(4));
        logic.setTilePos(2, 1, new Tile(4));
        logic.setTilePos(3, 1, new Tile(4));

        logic.setTilePos(0, 2, new Tile(4));
        logic.setTilePos(1, 2, new Tile(4));
        logic.setTilePos(2, 2, new Tile(4));
        logic.setTilePos(3, 2, new Tile(4));

        logic.setTilePos(0, 3, new Tile(4));
        logic.setTilePos(1, 3, new Tile(4));
        logic.setTilePos(2, 3, new Tile(4));
        logic.setTilePos(3, 3, new Tile(4));

        int[][] expectedBoard = { { 4, 4, 4, 4 },
                                  { 4, 4, 4, 4 },
                                  { 4, 4, 4, 4 },
                                  { 4, 4, 4, 4 } };
        assertTrue(Arrays.deepEquals(logic.getBoardVals(), expectedBoard));
    } //PERFECT, by blocking out every spot I can get a workaround for the initial random spawn
    //The workaround is to blot the whole board in 0s so no random tile spawns occur

    @Test
    public void testRandomSpawningWorkaround() {
        logic.moveLeft();
        //2 originally spawned tiles + 0 spawned tile from move strictly from logic end (HOPEFULLY)
        int emptyTilesOnBoard = logic.getEmptyTilePosList().size(); //should be 16 - 2
        assertEquals(14, emptyTilesOnBoard);
    }
    //PERFECT, I found a way to stop the random spawns on movement
    //by moving the spawnRandomTile() to GameBoard


    //Tests on locked movements
    //i.e. no moving occurs because it shouldn't be possible given the board
    @Test
    public void testEdgeCaseUnableToMoveAtVerticalEdgeOfBoard() {
        logic.setTilePos(0, 0, new Tile(0));
        logic.setTilePos(1, 0, new Tile(0));
        logic.setTilePos(2, 0, new Tile(0));
        logic.setTilePos(3, 0, new Tile(0));

        logic.setTilePos(0, 1, new Tile(0));
        logic.setTilePos(1, 1, new Tile(0));
        logic.setTilePos(2, 1, new Tile(0));
        logic.setTilePos(3, 1, new Tile(0));

        logic.setTilePos(0, 2, new Tile(0));
        logic.setTilePos(1, 2, new Tile(0));
        logic.setTilePos(2, 2, new Tile(0));
        logic.setTilePos(3, 2, new Tile(0));

        logic.setTilePos(0, 3, new Tile(0));
        logic.setTilePos(1, 3, new Tile(0));
        logic.setTilePos(2, 3, new Tile(0));
        logic.setTilePos(3, 3, new Tile(4));

        logic.moveDown();

        int[][] expectedBoard = { { 0, 0, 0, 0 },
                                  { 0, 0, 0, 0 },
                                  { 0, 0, 0, 0 },
                                  { 0, 0, 0, 4 } };

        assertTrue(Arrays.deepEquals(logic.getBoardVals(), expectedBoard));
    }

    @Test
    public void testEdgeCaseUnableToMoveAndMergeAtVerticalEdgeOfBoard() {
        logic.setTilePos(0, 0, new Tile(0));
        logic.setTilePos(1, 0, new Tile(0));
        logic.setTilePos(2, 0, new Tile(0));
        logic.setTilePos(3, 0, new Tile(0));

        logic.setTilePos(0, 1, new Tile(0));
        logic.setTilePos(1, 1, new Tile(0));
        logic.setTilePos(2, 1, new Tile(0));
        logic.setTilePos(3, 1, new Tile(0));

        logic.setTilePos(0, 2, new Tile(0));
        logic.setTilePos(1, 2, new Tile(0));
        logic.setTilePos(2, 2, new Tile(0));
        logic.setTilePos(3, 2, new Tile(0));

        logic.setTilePos(0, 3, new Tile(0));
        logic.setTilePos(1, 3, new Tile(0));
        logic.setTilePos(2, 3, new Tile(2));
        logic.setTilePos(3, 3, new Tile(4));

        logic.moveDown();

        int[][] expectedBoard = { { 0, 0, 0, 0 },
                                  { 0, 0, 0, 0 },
                                  { 0, 0, 0, 2 },
                                  { 0, 0, 0, 4 } };

        assertTrue(Arrays.deepEquals(logic.getBoardVals(), expectedBoard));
    }

    @Test
    public void testEdgeCaseUnableToMoveAtHorizontalEdgeOfBoard() {
        logic.setTilePos(0, 0, new Tile(0));
        logic.setTilePos(1, 0, new Tile(2));
        logic.setTilePos(2, 0, new Tile(0));
        logic.setTilePos(3, 0, new Tile(0));

        logic.setTilePos(0, 1, new Tile(0));
        logic.setTilePos(1, 1, new Tile(0));
        logic.setTilePos(2, 1, new Tile(0));
        logic.setTilePos(3, 1, new Tile(0));

        logic.setTilePos(0, 2, new Tile(0));
        logic.setTilePos(1, 2, new Tile(0));
        logic.setTilePos(2, 2, new Tile(0));
        logic.setTilePos(3, 2, new Tile(0));

        logic.setTilePos(0, 3, new Tile(0));
        logic.setTilePos(1, 3, new Tile(0));
        logic.setTilePos(2, 3, new Tile(0));
        logic.setTilePos(3, 3, new Tile(0));

        logic.moveLeft();

        int[][] expectedBoard = { { 0, 0, 0, 0 },
                                  { 2, 0, 0, 0 },
                                  { 0, 0, 0, 0 },
                                  { 0, 0, 0, 0 } };

        assertTrue(Arrays.deepEquals(logic.getBoardVals(), expectedBoard));
    }

    @Test
    public void testEdgeCaseUnableToMoveAndMergeAtHorizontalEdgeOfBoard() {
        logic.setTilePos(0, 0, new Tile(0));
        logic.setTilePos(1, 0, new Tile(2));
        logic.setTilePos(2, 0, new Tile(0));
        logic.setTilePos(3, 0, new Tile(0));

        logic.setTilePos(0, 1, new Tile(0));
        logic.setTilePos(1, 1, new Tile(4));
        logic.setTilePos(2, 1, new Tile(0));
        logic.setTilePos(3, 1, new Tile(0));

        logic.setTilePos(0, 2, new Tile(0));
        logic.setTilePos(1, 2, new Tile(0));
        logic.setTilePos(2, 2, new Tile(0));
        logic.setTilePos(3, 2, new Tile(0));

        logic.setTilePos(0, 3, new Tile(0));
        logic.setTilePos(1, 3, new Tile(0));
        logic.setTilePos(2, 3, new Tile(0));
        logic.setTilePos(3, 3, new Tile(0));

        logic.moveLeft();

        int[][] expectedBoard = { { 0, 0, 0, 0 },
                                  { 2, 4, 0, 0 },
                                  { 0, 0, 0, 0 },
                                  { 0, 0, 0, 0 } };

        assertTrue(Arrays.deepEquals(logic.getBoardVals(), expectedBoard));
    }

    @Test
    public void testEdgeCaseUnableToMoveInFullColumn() {
        logic.setTilePos(0, 0, new Tile(0));
        logic.setTilePos(1, 0, new Tile(0));
        logic.setTilePos(2, 0, new Tile(0));
        logic.setTilePos(3, 0, new Tile(0));

        logic.setTilePos(0, 1, new Tile(0));
        logic.setTilePos(1, 1, new Tile(0));
        logic.setTilePos(2, 1, new Tile(0));
        logic.setTilePos(3, 1, new Tile(0));

        logic.setTilePos(0, 2, new Tile(0));
        logic.setTilePos(1, 2, new Tile(0));
        logic.setTilePos(2, 2, new Tile(0));
        logic.setTilePos(3, 2, new Tile(0));

        logic.setTilePos(0, 3, new Tile(16));
        logic.setTilePos(1, 3, new Tile(8));
        logic.setTilePos(2, 3, new Tile(2));
        logic.setTilePos(3, 3, new Tile(4));

        logic.moveDown();
        logic.moveUp();

        int[][] expectedBoard = { { 0, 16, 0, 0 },
                                  { 0, 8, 0, 0 },
                                  { 0, 2, 0, 0 },
                                  { 0, 4, 0, 0 } };

        assertTrue(Arrays.deepEquals(logic.getBoardVals(), expectedBoard));
    }

    @Test
    public void testEdgeCaseUnableToMoveInFullRow() {
        logic.setTilePos(0, 0, new Tile(0));
        logic.setTilePos(1, 0, new Tile(2));
        logic.setTilePos(2, 0, new Tile(0));
        logic.setTilePos(3, 0, new Tile(0));

        logic.setTilePos(0, 1, new Tile(0));
        logic.setTilePos(1, 1, new Tile(4));
        logic.setTilePos(2, 1, new Tile(0));
        logic.setTilePos(3, 1, new Tile(0));

        logic.setTilePos(0, 2, new Tile(0));
        logic.setTilePos(1, 2, new Tile(8));
        logic.setTilePos(2, 2, new Tile(0));
        logic.setTilePos(3, 2, new Tile(0));

        logic.setTilePos(0, 3, new Tile(0));
        logic.setTilePos(1, 3, new Tile(16));
        logic.setTilePos(2, 3, new Tile(0));
        logic.setTilePos(3, 3, new Tile(0));

        logic.moveLeft();
        logic.moveRight();

        int[][] expectedBoard = { { 0, 0, 0, 0 },
                                  { 0, 0, 0, 0 },
                                  { 2, 4, 8, 16 },
                                  { 0, 0, 0, 0 } };

        assertTrue(Arrays.deepEquals(logic.getBoardVals(), expectedBoard));
    }

    @Test
    public void testEdgeCaseSameMovementTwice() {
        logic.setTilePos(0, 0, new Tile(0));
        logic.setTilePos(1, 0, new Tile(0));
        logic.setTilePos(2, 0, new Tile(0));
        logic.setTilePos(3, 0, new Tile(0));

        logic.setTilePos(0, 1, new Tile(0));
        logic.setTilePos(1, 1, new Tile(0));
        logic.setTilePos(2, 1, new Tile(0));
        logic.setTilePos(3, 1, new Tile(0));

        logic.setTilePos(0, 2, new Tile(0));
        logic.setTilePos(1, 2, new Tile(0));
        logic.setTilePos(2, 2, new Tile(0));
        logic.setTilePos(3, 2, new Tile(0));

        logic.setTilePos(0, 3, new Tile(0));
        logic.setTilePos(1, 3, new Tile(0));
        logic.setTilePos(2, 3, new Tile(2));
        logic.setTilePos(3, 3, new Tile(4));

        logic.moveRight();
        logic.moveRight();

        int[][] expectedBoard = { { 0, 0, 0, 0 },
                                  { 0, 0, 0, 0 },
                                  { 0, 0, 0, 2 },
                                  { 0, 0, 0, 4 } };

        assertTrue(Arrays.deepEquals(logic.getBoardVals(), expectedBoard));
    }

    //Checking game states
    //(more relevant for winning; make sure it ends when a 2048 tile is created)
    @Test
    public void testWinningGameState() { //cuz we know I'm never gonna actually win 2048 :(
        logic.setTilePos(0, 0, new Tile(2048));
        assertTrue(logic.winningGameState());
    }

    @Test
    public void testNonWinningGameState() {
        logic.setTilePos(0, 0, new Tile(1024));
        assertFalse(logic.winningGameState());
    }

    @Test
    public void testLosingGameState() { //no movement possible
        logic.setTilePos(0, 0, new Tile(4));
        logic.setTilePos(1, 0, new Tile(8));
        logic.setTilePos(2, 0, new Tile(16));
        logic.setTilePos(3, 0, new Tile(32));

        logic.setTilePos(0, 1, new Tile(64));
        logic.setTilePos(1, 1, new Tile(128));
        logic.setTilePos(2, 1, new Tile(256));
        logic.setTilePos(3, 1, new Tile(512));

        logic.setTilePos(0, 2, new Tile(4));
        logic.setTilePos(1, 2, new Tile(8));
        logic.setTilePos(2, 2, new Tile(16));
        logic.setTilePos(3, 2, new Tile(32));

        logic.setTilePos(0, 3, new Tile(64));
        logic.setTilePos(1, 3, new Tile(128));
        logic.setTilePos(2, 3, new Tile(256));
        logic.setTilePos(3, 3, new Tile(512));

        assertTrue(logic.losingGameState());
    }

    @Test
    public void testNonLosingGameState() {
        logic.setTilePos(0, 0, new Tile(4));
        logic.setTilePos(1, 0, new Tile(8));
        logic.setTilePos(2, 0, new Tile(16));
        logic.setTilePos(3, 0, new Tile(32));

        logic.setTilePos(0, 1, new Tile(64));
        logic.setTilePos(1, 1, new Tile(128));
        logic.setTilePos(2, 1, new Tile(256));
        logic.setTilePos(3, 1, new Tile(512));

        logic.setTilePos(0, 2, new Tile(4));
        logic.setTilePos(1, 2, new Tile(8));
        logic.setTilePos(2, 2, new Tile(16));
        logic.setTilePos(3, 2, new Tile(32));

        logic.setTilePos(0, 3, new Tile(64));
        logic.setTilePos(1, 3, new Tile(128));
        logic.setTilePos(2, 3, new Tile(256)); //these 256s can still combine
        logic.setTilePos(3, 3, new Tile(256)); //hope is not lost!

        assertFalse(logic.losingGameState());
    }





}
