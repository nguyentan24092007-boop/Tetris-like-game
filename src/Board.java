public class Board {
    public static final int column = 10;
    public static final int row = 20;
    public Block[][] grid;

    public Board() {
        grid = new Block[column][row];

        for(int i = 0; i < column; i++) {
            for(int j = 0; j < row; j++) {
                grid[i][j] = new Block();
            }
        }
    }
    public Block[][] getGrid() { return this.grid; }

    public boolean valid(Tetromino t, int x, int y) {
        int[][] shape = t.getShape();

        for(int i = 0; i < shape.length; i++) {
            for(int j = 0; j < shape[0].length; j++) {
                if(shape[i][j] != 0) {
                    int boardX = x + j;
                    int boardY = y + i;

                    if(boardX < 0 || boardX >= column) {
                        return false;
                    }
                    if(boardY >= row) {
                        return false;
                    }
                    if(boardY >= 0 && grid[boardX][boardY].hasPiece()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}