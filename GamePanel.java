import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class GamePanel extends JPanel implements IScoreObserver, KeyListener {
    private static final int CELL_SIZE = 30;
    private static final int PANEL_HEIGHT = Board.HEIGHT * CELL_SIZE;
    private static final Color[] PIECE_COLORS = {
        new Color(0, 240, 240), // I - cyan
        new Color(240, 240, 0), // O - yellow
        new Color(160, 0, 240), // T - purple
        new Color(0, 240, 0), // S - green
        new Color(240, 0, 0), // Z - red
        new Color(0, 0, 240), // J - blue
        new Color(240, 160, 0), // L - orange
    };
    private static final Color BG_COLOR = new Color(20, 20, 30);
    private static final Color GRID_COLOR = new Color(40, 40, 55);
    private static final Color GHOST_COLOR = new Color(255, 255, 255, 50);
    private Game game;
    private Timer gameTimer;
    private int displayScore = 0;
    private int displayLines = 0;
    private int displayLevel = 1;
    
    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH + 160, PANEL_HEIGHT));
        setBackground(BG_COLOR);
        setFocusable(true);
        addKeyListener(this);
        game = new Game();
        game.getScoreManager().addObserver(this);
        gameTimer = new Timer(500, e -> {
            game.update();
            repaint();
            gameTimer.setDelay(game.getScoreManager().getFallSpeed());
            if (game.isGameOver()) {
                gameTimer.stop();
                showGameOverDialog();
            }
        });
    }
    
    @Override
    public void onScoreChanged(int newScore) {
        displayScore = newScore;
        displayLevel = game.getScoreManager().getLevel();
        repaint();
    }
    @Override
    public void onLinesCleared(int linesCleared, int totalScore) {
        displayLines = game.getScoreManager().getLines();
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_O);
        drawBackground(g2);
        drawGrid(g2);
        if (game.isRunning() || game.isGameOver()) {
            drawBoard(g2);
            if (!game.isGameOver() && game.getCurrentPiece() != null) {
                drawGhostPiece(g2);
                drawCurrentPiece(g2);
            }
        }
        drawSidePanel(g2);
        if (game.isPaused()) {
            drawPauseOverlay(g2);
        }
        if (!game.isRunning() && !game.isGameOver()) {
            drawStartScreen(g2);
        }
    }
    private void drawBackground(Graphics2D g) {
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    private void drawGrid(Graphics2D g) {
        g.setColor(GRID_COLOR);
        for (int row = 0; row <= Board.HEIGHT; row++) {
            g.drawLine(0, row * CELL_SIZE, PANEL_WIDTH, row * CELL_SIZE);
        }
        for (int col = 0; col <= Board.WIDTH; col++) {
            g.drawLine(col * CELL_SIZE, 0, col * CELL_SIZE, PANEL_HEIGHT);
        }
    }
    private void drawBoard(Graphics2D g) {
        int[][] grid = game.getBoard().getGrid();
        for (int row = 0; row < Board.HEIGHT; row++) {
            for (int col = 0; col < Board.WIDTH; col++) {
                if (grid[row][col] != 0) {
                    drawCell(g, col, row, PIECE_COLORS[grid[row][col] - 1]);
                }
            }
        }
    }
    private void drawCurrentPiece(Graphics2D g) {Tetromino piece = game.getCurrentPiece();
        int[][] shape = piece.getShape();
        Color color = PIECE_COLORS[piece.getType().ordinal()];
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    drawCell(g, piece.getX() + col, piece.getY() + row, color);
                }
            }
        }
    }
    
    private void drawGhostPiece(Graphics2D g) {
        Tetromino piece = game.getCurrentPiece();
        int ghostY = game.getBoard().getGhostY(piece);
        int[][] shape = piece.getShape();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int px = (piece.getX() + col) * CELL_SIZE;
                    int py = (ghostY + row) * CELL_SIZE;
                    g.setColor(GHOST_COLOR);
                    g.fillRect(px + 1, py + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                }
            }
        }
    }
    private void drawCell(Graphics2D g, int col, int row, Color color) {
        int x = col * CELL_SIZE;
        int y = row * CELL_SIZE;

        g.setColor(color);
        g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
        g.setColor(color.brighter());
        g.drawLine(x + 1, y + 1, x + CELL_SIZE - 2, y + 1);
        g.drawLine(x + 1, y + 1, x + 1, y + CELL_SIZE - 2);
        g.setColor(color.darker());
        g.drawLine(x + CELL_SIZE - 2, y + 1, x + CELL_SIZE - 2, y + CELL_SIZE - 2);g.drawLine(x + 1, y + CELL_SIZE - 2, x + CELL_SIZE - 2, y + CELL_SIZE - 2);
    }
    private void drawSidePanel(Graphics2D g) {
        int sx = PANEL_WIDTH + 10;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.setColor(new Color(200, 200, 255));
        g.drawString("TETRIS", sx + 20, 30);
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g.drawString("SCORE", sx, 70);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString(String.valueOf(displayScore), sx, 90);
        // Level
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g.drawString("LEVEL", sx, 120);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString(String.valueOf(displayLevel), sx, 140);
        // Lines
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g.drawString("LINES", sx, 170);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString(String.valueOf(displayLines), sx, 190);
        // Next piece
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g.drawString("NEXT", sx, 230);
        drawNextPiece(g, sx, 245);
        // Keyboard
        g.setColor(new Color(150, 150, 150));g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.drawString("← → : Move", sx, 420);
        g.drawString("↑ : Rotate", sx, 435);
        g.drawString("↓ : Soft drop", sx, 450);
        g.drawString("Space: Hard drop", sx, 465);
        g.drawString("P : Pause", sx, 480);
        g.drawString("R : Restart", sx, 495);
    }
    private void drawNextPiece(Graphics2D g, int sx, int sy) {
        if (game.getNextPiece() == null) return;
        Tetromino next = game.getNextPiece();
        int[][] shape = next.getShape();
        Color color = PIECE_COLORS[next.getType().ordinal()];
        int mini = 20;
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    g.setColor(color);
                    g.fillRect(sx + col * mini, sy + row * mini, mini - 2, mini - 2);
                }
            }
        }
    }
    private void drawPauseOverlay(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 24));
        g.drawString("PAUSED", PANEL_WIDTH / 2 - 55, PANEL_HEIGHT / 2);
    }
    private void drawStartScreen(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        g.setColor(new Color(200, 200, 255));
        g.setFont(new Font("Monospaced", Font.BOLD, 28));
        g.drawString("TETRIS", PANEL_WIDTH / 2 - 50, 200);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g.drawString("Press ENTER to start", PANEL_WIDTH / 2 - 80, 260);
    }
    private void showGameOverDialog() {
        int choice = JOptionPane.showOptionDialog(
            this,
            "Game Over!\nScore: " + displayScore + "\nLevel: " + displayLevel,
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Play Again", "Quit"},
            "Play Again"
        );
        if (choice == 0) {
            startGame();
        } else {
            System.exit(0);
        }
    }
    private void startGame() {
        game = new Game();
        game.getScoreManager().addObserver(this);
        displayScore = 0;
        displayLines = 0;
        displayLevel = 1;
        game.start();
        gameTimer.setDelay(game.getScoreManager().getFallSpeed());
        gameTimer.restart();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                if (!game.isRunning()) startGame();
                break;
            case KeyEvent.VK_LEFT:
                game.moveLeft(); repaint(); break;
            case KeyEvent.VK_RIGHT:
                game.moveRight(); repaint(); break;
            case KeyEvent.VK_DOWN:
                game.moveDown(); repaint(); break;
            case KeyEvent.VK_UP:
                game.rotate(); repaint(); break;
            case KeyEvent.VK_SPACE:
                game.hardDrop(); repaint(); break;
            case KeyEvent.VK_P:
                game.pause(); repaint(); break;
            case KeyEvent.VK_R:
                if (game.isRunning()) startGame(); break;
        }
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}