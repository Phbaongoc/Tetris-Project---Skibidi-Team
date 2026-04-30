public class Tetromino {
    protected PieceType type;
    protected int[][] shape;
    protected int x;
    protected int y;
    protected int[][] color;

    private static final int[][][] SHAPES ={
        //I
        {{1,1,1,1}},
        //O
        {{1,1}, {1,1}},
        //T
        {{0,1,0}, {1,1,1}},
        //S
        {{0,1,1}, {1,1,0}},
        //Z
        {{1,1,0}, {0,1,1}},
        //J
        {{1,0,0},{1,1,1}},
        //L
        {{0,0,1}, {1,1,1}},
};

public Tetromino(PieceType type){
    this.type = type;
    this.shape = SHAPES [type.ordinal()];
    this.x = 3;
    this.y = 0;
}

public void rotate(){
    int rows = shape.length;
    int cols = shape[0].length;
    int[][] rotated = new int[cols][rows];
    
    for (int r = 0; r< rows; r++) {
        for (int c = 0; c< cols; c++) {
            rotated[c][rows -1 - r] = shape[r][c];
        }
    }
    shape = rotated;
}

public void rotateBack(){
    int rows = shape.length;
    int cols = shape[0].length;
    int[][] rotated = new int[cols][rows];

    for(int r = 0; r< rows; r++){
        for(int c = 0; c<cols; c++){
            rotated[cols - 1 - c][r] = shape[r][c];
        }
    }
    shape =rotated;
}

public boolean moveDown() {
    y++;
    return true;
}

public void moveLeft(){
    x--;
}

public void moveRight() {
    x++;
}

public void moveUp() {
    y--;
}

public void moveBackLeft() {
    x++;
}

public void moveBackRight() {
    x--;
}

//getters

public PieceType getType() {
    return type;
}

public int[] [] getShape() {
    return shape;
}

public int getX () {
    return x;
}

public int getY (){
    return y;
}

public void setX(int x){
    this.x = x;
}

public void setY(int y){
    this.y = y;
}
}