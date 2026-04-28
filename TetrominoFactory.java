import java.util.Random;

public class TetrominoFactory{
    private Random random;

    public TetrominoFactory(){
        this.random = new.random();
    }

    //Create a random Tetromino piece
    public Tetromino createTetromino(){
        PieceType[] types = PiceType.values();
        PieceType randomType = types[random.nextInt(types.length)];
        return createSpecificTetromino(randomType);
    }
    //Create a Tetromino piece of a specific type.
    public Tetromino createSpecificTetromino(PieceType type){
        return new Tetromino(type);
    }
}