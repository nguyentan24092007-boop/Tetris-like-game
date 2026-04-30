import java.awt.Color;
import java.util.*;

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
    //keep the old block stay down
    public void pieceLock(Tetromino t) {
        int[][] shape = t.getShape();

        for(int i = 0; i < shape.length; i++) {
            for(int j = 0; j < shape[0].length; j++) {
                if(shape[i][j] == 1) {
                    int x = t.getX() + j;
                    int y = t.getY() + i;
                    grid[x][y].have(t.getColor());
                }
            }
        }
    }
    //line clear up
    public int clearLine() {
        int clearLine = 0;

        for(int y = row - 1; y >= 0; y--) {
            boolean full = true;
            for(int x = 0; x < column; x++) {
                if(!grid[x][y].hasPiece()) {
                    full = false;
                    break;
                }
            }
            if(full) {
                clearLine++;
            
                for(int dropY = y; dropY > 0; dropY--) {
                    for(int x = 0; x < column; x++) {
                        if(grid[x][dropY-1].hasPiece()) {
                            grid[x][dropY].have(grid[x][dropY-1].getColor());
                        }
                        else {
                            grid[x][dropY].dontHave();
                        }
                    }
                }
                for(int x = 0; x < column; x++) {
                    grid[x][0].dontHave();
                }
                y++;
            }
        }
        return clearLine;
    }
    //pothole game mode
    public void potHoleLogic() {
        for(int y = 1; y < row; y++) {
            for(int x = 0; x < column; x++) {
                grid[x][y-1] = grid[x][y];
            }
        }
        Random randomLocation = new Random();
        int missingBlock = randomLocation.nextInt(4);

        for(int x = 0; x <column; x++) {
            if(x == missingBlock) {
                grid[x][row-1] = new Block();
            }
            else {
                Block randomLine = new Block();
                randomLine.have(Color.GRAY);
                grid[x][row - 1] = randomLine;
            }
        }
    }
}