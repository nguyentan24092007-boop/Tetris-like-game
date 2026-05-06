import javax.swing.*;
import java.awt.*;
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
        this.addKeyListener(new KeyInput(this));
    }

    //modes (extra feature)
    public void randomShape() {
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

    public void rotateLogic() {
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
                    SFX.playSound("sfx/lock_block.wav"); // locking block sfx
                    board.pieceLock(tetromino);
                    this.tetromino = this.nextTetromino;
                    this.nextTetromino = RandomTetromino.randomShape();

                    //score counting
                    int line = board.clearLine();
                    if(line > 0) {

                        int scoreMultiply = 100;

                        if(fallSpeed == 45) {
                            scoreMultiply = 50;
                        }
                        else {
                            scoreMultiply = 200;
                        }

                        score += (line*scoreMultiply);

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
                        SFX.playSound("sfx/game_over.wav"); // game over sfx
                    }
                }
                fallCount = 0;
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        GameUserInterface.draw((Graphics2D) g, this);
    }

    public GameState getCurrentState() { return currentState; }
    public void setCurrentState(GameState currentGameState) { this.currentState = currentGameState; }

    public int getMenuOption() { return menuOption; }
    public void setMenuOption(int menuOption) { this.menuOption = menuOption; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getModeName() { return modeName; }
    public void setModeName(String modeName) { this.modeName = modeName; }

    public boolean getGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) {  this.gameOver = gameOver; }

    public GridOutline getPlayArea() { return playArea; }

    public Board getBoard() { return board; }

    public Tetromino getTetromino() { return tetromino; }

    public Tetromino getNextTetromino() { return nextTetromino; }

    public int getScore() { return score; }

    public int getTopScore() { return topScore; }

    public void setFallCount(int fallCount) { this.fallCount = fallCount; }

    public int getGameModeNumber() { return gameModeNumber; }
    public void setGameModeNumber(int gameModeNumber) { this.gameModeNumber = gameModeNumber; }

    public int getFallSpeed() { return fallSpeed; }
    public void setFallSpeed(int fallSpeed) { this.fallSpeed = fallSpeed; }
}
