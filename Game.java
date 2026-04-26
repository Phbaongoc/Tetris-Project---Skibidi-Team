public class Game {

    private Board board;
    private Tetromino currentPiece;
    private Tetromino nextPiece;
    private ScoreManager scoreManager;
    private TetrominoFactory factory;

    private boolean isRunning;
    private boolean isPaused;
    private boolean isGameOver;

    public Game(){
        this.board = new Board();
        this.scoreManager = new ScoreManager();
        this.factory = new TetrominoFactory();
        this.isRunning = false;
        this.isPaused = false;
        this.isGameOver = false;
    }

    public void start(){
        board.reset();
        scoreManager.reset();
        isGameOver = false;
        isPaused = false;
        isRunning = false;

        currentPiece = factory.createTetromino();
        nextPiece = factory.createTetromino();
    }

    public void pause() {
        if (isRunning && !isGameOver) {
            isPaused = !isPaused;
        }
    }

    public void update(){
        if (!isRunning || isPaused || isGameOver) return;
        
        currentPiece.moveDown();
        
        if (!board.isValidMove(currentPiece)) {
            currentPiece.moveUp();
            board.place(currentPiece);
            
            int linesCleared = board.clearLines();
            if (linesCleared > 0) {
                scoreManager.addScore(linesCleared);
            }
            if (checkGameOver()) {
                isGameOver = true;
                isRunning = false;
                return;
            }
            spawnNextPiece();
            }
        }
        public void moveLeft(){
            if (!isRunning || isPaused || isGameOver) return;
            currentPiece.moveLeft();
            if (!board.isValidMove(currentPiece)) {
                currentPiece.moveBackLeft();
            }
        }
        public void moveRight(){
            if (!isRunning || isPaused || isGameOver) return;
            currentPiece.moveRight();
            if (!board.isValidMove(currentPiece)) {
                currentPiece.moveBackRight();
            }
        }
        public void moveDown(){
            if(!isRunning || isPaused || isGameOver) return;
            update();
        }
        public void rotate() {
            if (!isRunning || isPaused || isGameOver) return;
            currentPiece.rotate();
            if (!board.isValidMove(currentPiece)) {
                currentPiece.rotateBack();
            }
        }
        public void hardDrop() {
            if (!isRunning || isPaused || isGameOver) return;
            while (board.isValidMove(currentPiece)) {
                currentPiece.moveDown();
            }
            currentPiece.moveUp();
            update();
        }
        public boolean checkGameOver() {
            Tetromino testPiece = factory.createTetromino();
            return !board.isValidMove(testPiece);
        }
        private void spawnNextPiece() {
            currentPiece = nextPiece;
            nextPiece = factory.createTetromino();
        }
        //============Getters============
        public Board getBoard() {
            return board;
        }
        public Tetromino getCurrentPiece() {
            return currentPiece;
        }
        public Tetromino getNextPiece() {
            return nextPiece;
        }
        public ScoreManager getScoreManager() {
            return scoreManager;
        }
        public boolean isRunning() {
            return isRunning;
        }
        public boolean isPaused() {
            return isPaused;
        }
        public boolean isGameOver() {
            return isGameOver;
        }
}
