public class Board {

    public static final int WIDTH  = 10;
    public static final int HEIGHT = 20;

    private int[][] grid;

    public Board() {
        grid = new int[HEIGHT][WIDTH];
    }

    public boolean isValidMove(Tetromino piece) {
        int[][] shape = piece.getShape();
        int px = piece.getX();
        int py = piece.getY();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int newX = px + col;
                    int newY = py + row;

                    if (newX < 0 || newX >= WIDTH) return false;

                    if (newY >= HEIGHT) return false;
                    if (newY >= 0 && grid[newY][newX] != 0) return false;
                }
            }

        }
        return true;
    }

    public void place(Tetromino piece) {
        int[][] shape = piece.getShape();
        int px = piece.getX();
        int py = piece.getY();

        int colorCode = piece.getType().ordinal() + 1;

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int gx = px + col;
                    int gy = py + row;
                    if (gy >= 0) {
                        grid[gy][gx] = colorCode;
                    }
                }
            }
        }
    }

    public int clearLines() {
        int linesCleared = 0;

        for (int row = HEIGHT - 1; row >= 0; row--) {
            if (isRowFull(row)) {
                removeRow(row);
                linesCleared++;
                row++;
            }
        }
        return linesCleared;
    }

    private boolean isRowFull(int row) {
        for (int col = 0; col < WIDTH; col++) {
            if (grid[row][col] == 0) return false;
        }
        return true;
    }

    private void removeRow(int rowToRemove) {

        for (int row = rowToRemove; row > 0; row--) {
            grid[row] = grid[row - 1].clone();
        }

        grid[0] = new int[WIDTH];
    }

    public int getGhostY(Tetromino piece) {
        int originalY = piece.getY();
        int ghostY = originalY;

        while (true) {
            piece.setY(ghostY + 1);
            if (!isValidMove(piece)) {
                piece.setY(originalY);
                return ghostY;
            }
            ghostY++;
        }
    }

    public void reset() {
        grid = new int[HEIGHT][WIDTH];
    }

    public int[][] getGrid() {
        return grid;
    }
}