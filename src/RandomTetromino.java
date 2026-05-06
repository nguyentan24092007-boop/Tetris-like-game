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
                {{0,0,1},{1,1,1},{0,0,0}}
        };
        Color[] allColor = {
                new Color(255, 230, 0),
                new Color(0, 255, 255),
                new Color(191, 0, 255),
                new Color(255, 20, 147),
                new Color(50, 255, 18),
                new Color(0, 102, 255),
                new Color(255, 140, 0)
        };
        Random random = new Random();
        int roller = random.nextInt(7);
        return new Tetromino(allShape[roller], allColor[roller]);
    }
}
