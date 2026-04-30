import java.awt.*;
import java.util.*;

public class RandomTetromino {
    public static Tetromino randomShape() {
        int[][][] allShape = {
            {{1,1},{1,1}}, //O
            {{0,0,0,0},{1,1,1,1},{0,0,0,0},{0,0,0,0}}, //I
            {{0,1,0},{1,1,1},{0,0,0}}, //T
            {{1,1,0},{0,1,1},{0,0,0}}, //Z
            {{0,1,1},{1,1,0},{0,0,0}}, //S
            {{1,0,0},{1,1,1},{0,0,0}}, //J
            {{0,0,1},{1,1,1},{0,0,0}}  //L
        };
        Color[] allColor = {
            Color.CYAN,
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.BLUE,
            Color.PINK,
            Color.GREEN
        };
        Random random = new Random();
        int roller = random.nextInt(7);
        return new Tetromino(allShape[roller], allColor[roller]);
    }
}
