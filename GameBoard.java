package org.cis1200.TwentyFortyEight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

/**
 * This class instantiates a TwentyFortyEight object, which is the logic model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private GameLogic logic; // model for the game
    private JLabel status; // current status text

    // Game constants (400 is perfect for the 4by4 grid)
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        logic = new GameLogic(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                repaint(); // repaints the game board
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                //Movement Key Inputs
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        logic.moveUp();
                        logic.spawnRandomTile();
                        break;
                    case KeyEvent.VK_DOWN:
                        logic.moveDown();
                        logic.spawnRandomTile();
                        break;
                    case KeyEvent.VK_LEFT:
                        logic.moveLeft();
                        logic.spawnRandomTile();
                        break;
                    case KeyEvent.VK_RIGHT:
                        logic.moveRight();
                        logic.spawnRandomTile();
                        break;
                    default:
                        break;
                }

                //Game Status Updates on South Status Panel
                String status = null;
                if (logic.canMove()) {
                    status = "Reach 2048 to win! (Game Status: Playing)";
                }

                if (logic.losingGameState()) {
                    status = "You lose! (Game Status: Loss)";
                }

                if (logic.winningGameState()) {
                    status = "You win! (Game Status: Won)";
                }
                updateStatus(status);
                repaint();
            }
        });


    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus(String text) {
        status.setText(text);
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        logic.reset();
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     *  Creates window of instructions on how to play game
     */
    public void instructions() {
        JFrame frame = new JFrame();
        JDialog dialog = new JDialog(frame);
        dialog.setLayout(new FlowLayout());
        dialog.setBounds(0, 0, 500, 350);
        String instructions =
            "<html>" + "<br> 2048 made with love by Michael Li <br><br>" +
                "1. Use the arrow keys to move the numbered tiles on the grid. <br>" +
                "2. The tiles will slide as far as possible in the chosen direction <br>" +
                "&nbsp&nbsp&nbsp until they reach the edge of the " +
                    "grid or collide with another tile. <br>" +
                "3. If two tiles with the same number collide, they will combine into <br>" +
                "&nbsp&nbsp&nbsp a single tile! <br>" +
                "4. The game ends when the grid is full and no more moves are possible, <br>" +
                "&nbsp&nbsp&nbsp or when a tile with the value of 2048 is created. " +
                    "The goal is to create <br>" +
                "&nbsp&nbsp&nbsp a tile with the value of 2048 before the game ends. <br><br>" +
                "Buttons Added: <br>" +
                "Reset: Reset Game from the very beginning <br>" +
                "Instructions: Self-explanatory, you're looking at it right now :) <br>" +
                "Save: Save current game state <br>" +
                "Load: Load saved game state (will only work if there " +
                    "is an existing save state)<br>" +
                "<html>";

        JLabel jl = new JLabel(String.format(instructions));
        dialog.add(jl);
        dialog.setVisible(true);
        repaint();
        requestFocusInWindow();
    }

    /**
     * Saves the game to the savedBoards.txt file
     */
    public void save() {
        logic.saveGameToFile();
        String status = "(Current Game Board Successfully Saved)";
        updateStatus(status);
        repaint();
        requestFocusInWindow();
    }


    /**
     * Loads the game from the savedBoards.txt file
     */
    public void load() {
        logic.loadGameFromFile();
        String status = "(Saved Game Board Successfully Loaded)";
        updateStatus(status);
        repaint();
        requestFocusInWindow();
    }

    /**
     * Making the GameBoard below~
     */

    /**
     * Using TreeMap collection to map colors to tile integer values (Collections)
     */
    public static final TreeMap<Integer, Color> TILECOLORS = new TreeMap<>();
    static {
        TILECOLORS.put(0, new Color(203,194,179));
        TILECOLORS.put(2, new Color(238,230,219));
        TILECOLORS.put(4, new Color(237,224,200));
        TILECOLORS.put(8, new Color(237,178,127));
        TILECOLORS.put(16, new Color(242,151,101));
        TILECOLORS.put(32, new Color(242,124,107));
        TILECOLORS.put(64, new Color(240,99,64));
        TILECOLORS.put(128, new Color(236,207,115));
        TILECOLORS.put(256, new Color(239,203,98));
        TILECOLORS.put(512, new Color(237,200,86));
        TILECOLORS.put(1024, new Color(233,191,93));
        TILECOLORS.put(2048, new Color(231,190,79));
    }

    public static Color getTileColor(int tileVal) {
        Color color = TILECOLORS.get(tileVal);

        if (color == null) {
            throw new IllegalArgumentException();
        } else {
            return color;
        }
    }

    /**
     * Painting the actual board (2d Arrays)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw Tiles
        int[][] tileValBoard = logic.getBoardVals();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int currTileVal = tileValBoard[i][j];

                g.setColor(getTileColor(currTileVal));
                g.fillRect(i * 100, j * 100, 100, 100);

                if (currTileVal != 0) {
                    Font font = new Font("Arial", Font.BOLD, 48);
                    g.setFont(font);
                    g.setColor(Color.BLACK);
                    FontMetrics fontMetrics = g.getFontMetrics();
                    int w = fontMetrics.stringWidth("" + currTileVal);
                    int h = fontMetrics.getHeight();
                    int x = ((100 - w) / 2);
                    int y = ((100 - h) / 2) + fontMetrics.getAscent();

                    g.drawString("" + currTileVal, i * 100 + x, j * 100 + y);
                }
            }
        }

        //Draw Gridlines
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 0, 400);

        g.drawLine(100, 400, 100, 0);
        g.drawLine(200, 400, 200, 0);
        g.drawLine(300, 400, 300, 0);
        g.drawLine(400, 400, 400, 0);

        g.drawLine(0, 100, 400, 100);
        g.drawLine(0, 200, 400, 200);
        g.drawLine(0, 300, 400, 300);
        g.drawLine(0, 400, 400, 400);
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
