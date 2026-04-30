import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH=1000;
    public static final int HEIGHT=720;
    final int FPS = 60;
    Thread gameThread;
    private Board board;
    public static int blockSize = 30;
    private Tetromino tetromino;
    private Tetromino nextTetromino;
    private int fallCount = 0;
    private GridOutline playArea;
    private int score = 0;
    private int topScore = 0;
    private boolean gameOver = false;

    //add game menu
    public enum GameState { MENU, PLAYING }
    private GameState currentState = GameState.MENU;
    private int menuOption = 0;
    private int fallSpeed = 30;
    private String difficulty = "NORMAL";
    //game mode
    private int potholeScore = 1000;
    private int gameModeNumber = 0;
    private String modeName = "CLASSIC";
    Random random = new Random();

    public GamePanel(){
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setLayout(null);

        //keyboard input
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int input = e.getKeyCode();

                if (gameOver) {
                    gameOverInput(input);
                }
                else if(currentState == GameState.MENU) {
                    menuInput(input);
                }
                else if(currentState == GameState.PLAYING) {
                    gamePlayInput(input);
                }
                repaint();
            }
        });
    }

    //game over
    private void gameOverInput(int input) {
        if( input == KeyEvent.VK_ENTER) {
            resetGame();
        }
        else if( input == KeyEvent.VK_ESCAPE) {
            currentState = GameState.MENU;
            gameOver = false;
        }
    }
    //game menu
    private void menuInput(int key) {
        if (key == KeyEvent.VK_UP) {
            menuOption--;
            if (menuOption < 0) menuOption = 3;
        }
        else if (key == KeyEvent.VK_DOWN) {
            menuOption++;
            if (menuOption > 3) menuOption = 0;
        } 
        else if (key == KeyEvent.VK_ENTER) {
            if (menuOption == 0) resetGame();
            else if (menuOption == 1) {
                if (fallSpeed == 30) { fallSpeed = 15; difficulty = "HARD"; }
                else if (fallSpeed == 15) { fallSpeed = 45; difficulty = "EASY"; }
                else { fallSpeed = 30; difficulty = "NORMAL"; }
            } 
            else if (menuOption == 2) { //pick mode
                if (gameModeNumber == 0) {
                    gameModeNumber = 1;
                    modeName = "POTHOLE";
                }
                else if(gameModeNumber == 1) {
                    gameModeNumber = 2;
                    modeName = "RANDOM SHAPE";
                }
                else {
                    gameModeNumber = 0;
                    modeName = "CLASSIC";
                }
            }
            else if (menuOption == 3) System.exit(0);
        }
    }

    //modes (extra feature)
    private void randomShape() {
        Tetromino newShape = RandomTetromino.randomShape();
        int randomRotateTime = random.nextInt(4);
        
        for (int i = 0; i < randomRotateTime; i++) {
            newShape.setShape(newShape.rotateShape());
        }
        int moveX = tetromino.getX() - newShape.getX();
        int moveY = tetromino.getY() - newShape.getY();
        newShape.move(moveX, moveY);

        if (board.valid(newShape, newShape.getX(), newShape.getY())) {
            this.tetromino = newShape;
        }
    }
    
    //gameplay
    private void gamePlayInput(int input) {
        if(tetromino == null || board == null) {
                return;
            }
            int x = tetromino.getX();
            int y = tetromino.getY();

            switch (input) {
                case KeyEvent.VK_LEFT:
                    if(board.valid(tetromino, x-1, y)) {
                        tetromino.move(-1, 0);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(board.valid(tetromino, x+1, y)) {
                        tetromino.move(1, 0);
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(board.valid(tetromino, x, y+1)) {
                        tetromino.move(0, 1);
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (gameModeNumber == 2) {
                        randomShape();
                    } 
                    else {
                        rotateLogic();
                    }
                    rotateLogic();
                    break;
                case KeyEvent.VK_SPACE:  //instant drop
                    while(board.valid(tetromino, tetromino.getX(), tetromino.getY() + 1)) {
                        tetromino.move(0, 1);
                    }
                    fallCount = 30; 
                    break;
        }
    }

    private void rotateLogic() {
        int x = tetromino.getX();
        int y = tetromino.getY();

        int[][] nextShape = tetromino.rotateShape();
        int[][] currentShape = tetromino.getShape();

        tetromino.setShape(nextShape);
        if(board.valid(tetromino, x, y)) {
            return;
        }
        if(board.valid(tetromino, x-1, y)) {
            tetromino.move(-1, 0);
            return;
        }
        if(board.valid(tetromino, x+1, y)) {
            tetromino.move(1, 0);
            return;
        }
        if(board.valid(tetromino, x, y-1)) {
            tetromino.move(0, -1);
            return;
        }
        if(board.valid(tetromino, x, y+1)) {
            tetromino.move(0, 1);
            return;
        }
        if (board.valid(tetromino, x - 2, y)) { 
            tetromino.move(-2, 0);
            return;
        }
        if (board.valid(tetromino, x + 2, y)) { 
            tetromino.move(2, 0);
            return;
        }
        tetromino.setShape(currentShape);
    }

    public void launchGame(){
        gameThread = new Thread(this);
        gameThread.start();

        this.board = new Board();
        this.tetromino = RandomTetromino.randomShape();
        this.nextTetromino = RandomTetromino.randomShape();
        this.playArea = new GridOutline();
    }

    public void resetGame() {
        this.board = new Board();
        this.tetromino = RandomTetromino.randomShape();
        this.nextTetromino = RandomTetromino.randomShape();
        this.score = 0;
        this.fallCount = 0;
        this.gameOver = false;
        this.currentState = GameState.PLAYING;
        this.potholeScore = 1000;
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta=0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime-lastTime)/drawInterval;
            lastTime=currentTime;

            if (delta >=1 ){
                update();
                repaint();
                delta--;
            }
        }
    }
    
    public void update(){
        if(currentState == GameState.PLAYING && !gameOver) {
            gameLogic();
        }
    }

    private void gameLogic () {
        if(tetromino != null && board != null) {
            fallCount++;  //time counting to fall 

            if(fallCount >= fallSpeed) {
                if(board.valid(tetromino, tetromino.getX(), tetromino.getY() + 1)) { //asking if the next move valid
                    tetromino.move(0, 1);
                }
                else {
                    board.pieceLock(tetromino);
                    this.tetromino = this.nextTetromino;
                    this.nextTetromino = RandomTetromino.randomShape();

                    //score counting
                    int line = board.clearLine();
                    if(line > 0) {
                        score += (line*100);

                        if(score > topScore) {
                            topScore = score;
                        }
                        if (gameModeNumber == 1) {
                            while (score >= potholeScore) {
                                board.potHoleLogic();
                                potholeScore += 1000;
                            }
                        }
                    }
                    if(!board.valid(tetromino, tetromino.getX(), tetromino.getY())) {
                        gameOver = true;
                    }
                }
                fallCount = 0;
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        //game menu
        if (currentState == GameState.MENU) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 60));
            g2.drawString("TETRIS-LIKE GAME", WIDTH / 2 - 270, 200);

            g2.setFont(new Font("Arial", Font.BOLD, 30));
            g2.drawString(">" , WIDTH / 2 - 200, 350 + (menuOption * 70));
            g2.drawString("START GAME", WIDTH / 2 - 150, 350);
            g2.drawString("DIFFICULTY:  " + difficulty, WIDTH / 2 - 150, 420);
            g2.drawString("QUIT", WIDTH / 2 - 150, 560);
            g2.drawString("MODE: "+ modeName, WIDTH/2 -150, 490);
            return; // Skip drawing the rest of the game board!
        }

        if(playArea != null) {
            playArea.draw(g2);
        }

        g2.setColor(new Color(50, 50, 50));
        for(int i = 0; i < Board.column; i++) {
            for(int j = 0; j < Board.row; j++) {
                int x = GridOutline.left_x + (i * blockSize);
                int y = GridOutline.top_y + (j * blockSize);
                g2.drawRect(x, y, blockSize, blockSize);
            }
        }
        if(board != null) {
            Block[][] current = board.getGrid();
            
            for(int i = 0; i < Board.column; i++) {
                for(int j = 0; j< Board.row; j++) {
                    if(current[i][j].hasPiece()) {
                        g2.setColor(current[i][j].getColor());
                        int x = GridOutline.left_x + (i * blockSize);
                        int y = GridOutline.top_y + (j * blockSize);
                        g2.fill3DRect(x, y, blockSize, blockSize, true);
                    }
                }
            }
        }
        if(tetromino != null) {
            int[][] shape = tetromino.getShape();
            g2.setColor(tetromino.getColor());

            for(int i = 0; i < shape.length; i++) {
                for(int j = 0; j < shape[0].length; j++) {
                    if(shape[i][j] == 1) {
                        int x = GridOutline.left_x + ((tetromino.getX() + j) * blockSize);
                        int y = GridOutline.top_y + ((tetromino.getY() + i) * blockSize);
                        g2.fill3DRect(x, y, blockSize, blockSize, true);
                    }
                }
            }
        }
        //score display
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("SCORE: " + score, 600, 200);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Ariel", Font.BOLD, 30));
        g2.drawString("TOP SCORE: "+ topScore, 600, 300);

        //game over display
        if(gameOver) {
            g2.setColor(new Color(0,0, 0,180));
            g2.fillRect(GridOutline.left_x, GridOutline.top_y, 300, 600);

            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 45));
            g2.drawString("GAME OVER", GridOutline.left_x + 12, GridOutline.top_y +300);

            //press enter to retry and esc for menu
            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.drawString("Press ENTER to retry or ESC for menu", 45, GridOutline.top_y -20);

        }
        
        //display next tetromino
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("NEXT SHAPE", 600, 400);
        if(nextTetromino != null) {
            int[][] nextShape = nextTetromino.getShape();
            g2.setColor(Color.GRAY);

            for(int i = 0; i < nextShape.length; i++) {
                for(int j = 0; j < nextShape[0].length; j++) {
                    if(nextShape[i][j] == 1) {
                        int x = 650 + (j*blockSize);
                        int y = 450 + (i*blockSize);
                        g2.fill3DRect(x, y, blockSize, blockSize, true);
                    }
                }
            }
        }
    }
}