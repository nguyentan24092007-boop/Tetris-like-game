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

                    if(boardX < 0 || boardX >= column || boardY >= row) return false;
                    if(boardY >= 0 && grid[boardX][boardY].hasPiece()) return false;
                }
            }
        }
        return true;
    }

    public void addTetrominoToGrid(Tetromino t) {
        int[][] shape = t.getShape();
        for(int i = 0; i < shape.length; i++) {
            for(int j = 0; j < shape[0].length; j++) {
                if(shape[i][j] != 0) {
                    int bx = t.getX() + j;
                    int by = t.getY() + i;
                    if(by >= 0) {
                        grid[bx][by].have(t.getColor());
                    }
                }
            }
        }
    }

    //check từ dưới lên trên xem coi có dòng nào đầy chưa để tính điểm
    public void clearLines() {
        for (int y = row - 1; y >= 0; y--) {
            boolean full = true;
            for (int x = 0; x < column; x++) {
                if (!grid[x][y].hasPiece()) {
                    full = false;
                    break;
                }
            }
            if (full) {
                shiftRowsDown(y);
                y++;
            }
        }
    }

    //copy dòng ở trên dòng bị xoá xuống dòng vừa bị xoá
    private void shiftRowsDown(int clearedRow) {
        for (int y = clearedRow; y > 0; y--) {
            for (int x = 0; x < column; x++) {
                if (grid[x][y-1].hasPiece()) {
                    grid[x][y].have(grid[x][y-1].getColor());
                } else {
                    grid[x][y].dontHave();
                }
            }
        }
        for (int x = 0; x < column; x++) grid[x][0].dontHave();
    }
}