import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH=1020;
    public static final int HEIGHT=720;
    final int FPS = 60;
    Thread gameThread;
    private Board board;
    public static int blockSize = 30;
    private Tetromino tetromino;
    private Tetromino nextTetromino;
    private int fallCount = 0;
    private gridOutline playArea;
    private int score = 0;
    private boolean gameOver = false;

    public enum GameState { MENU, PLAYING, GAME_OVER }
    private GameState currentState = GameState.MENU;
    private int menuOption = 0;
    private int fallSpeed = 30;
    private String difficulty = "EASY";
    private int highScore = 0;


    public GamePanel(){
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setLayout(null);

        //keyboard input
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();

                //menu state
                if(currentState == GameState.MENU){
                    if(code == KeyEvent.VK_UP) {
                        menuOption--;
                        if (menuOption < 0) menuOption = 2;
                    }
                    else if(code == KeyEvent.VK_DOWN) {
                        menuOption++;
                        if (menuOption > 2) menuOption = 0;
                    }
                    else if (code == KeyEvent.VK_ENTER) {
                        if (menuOption == 0) {
                            resetGame();
                        } else if (menuOption == 1) {
                            if (fallSpeed == 30) { fallSpeed = 15; difficulty = "HARD"; }
                            else if (fallSpeed == 15) { fallSpeed = 45; difficulty = "EASY"; }
                            else { fallSpeed = 30; difficulty = "NORMAL"; }
                        } else if (menuOption == 2) {
                            System.exit(0);
                        }
                    }
                }
                //playing state
                else if(currentState == GameState.PLAYING){
                    if(gameOver || tetromino == null || board == null) {
                      return;
                    }
                    int x = tetromino.getX();
                    int y = tetromino.getY();

                switch (code) {
                    case KeyEvent.VK_LEFT:
                        if(board.valid(tetromino, x-1, y)) 
                            tetromino.move(-1, 0); {
                        break;
                    }
                    case KeyEvent.VK_RIGHT:
                        if(board.valid(tetromino, x+1, y))
                            tetromino.move(1, 0); {
                        break;
                    }
                    case KeyEvent.VK_DOWN:
                        if(board.valid(tetromino, x, y+1))
                            tetromino.move(0, 1); {
                        break; 
                    }
                    case KeyEvent.VK_UP:
                    int[][] nextShape = tetromino.rotateShape();
                    int[][] currentShape = tetromino.getShape();

                    tetromino.setShape(nextShape);
                    if(!board.valid(tetromino, x, y)) {
                        tetromino.setShape(currentShape); 
                    }
                    break;
                }
            }
            else if (currentState = GameState.GAME_OVER){
                if (code == KeyEvent.VK_ENTER){
                    resetGame(); //replay
                else if(code == KeyEvent.VK_ESCAPE){
                    currentState = GameState.MENU; //Comeback to menu
                }
            }
                repaint();
            }
        });
    }
    
    public void resetGame() {
        this.board = new Board();
        this.tetromino = Tetromino.randomShape();
        this.nextTetromino = Tetromino.randomShape();
        this.playArea = new gridOutline();
        this.score = 0;
        this.fallCount = 0;
        this.gameOver = false;
        this.currentState = GameState.PLAYING;
    }
    public void launchGame(){
        gameThread = new Thread(this);
        gameThread.start();

        this.board = new Board();
        this.tetromino = Tetromino.randomShape();
        this.nextTetromino = Tetromino.randomShape();
        this.playArea = new GridOutline();
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
        if(gameOver) {
            return;
        }
        if(tetromino != null && board != null) {
            fallCount++;  //time counting to fall 

            if(fallCount >= 30) { //reduce to move faster and vice versa 
                if(board.valid(tetromino, tetromino.getX(), tetromino.getY() + 1)) { //asking if the next move valid
                    tetromino.move(0, 1);
                }
                else {
                    board.pieceLock(tetromino);
                    this.tetromino = this.nextTetromino;
                    this.nextTetromino = Tetromino.randomShape();

                    //score counting
                    int line = board.clearLine();
                    if(line > 0) {
                        score += (line*100);
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

        if (currentState == GameState.MENU) {
            drawMenu(g2);
        }
        else if (currentState == GameState.PLAYING) {
            drawPlayScreen(g2);
        }
        else if (currentState == GameState.GAME_OVER) {
            drawPlayScreen(g2);
            drawGameOver(g2);
        }
    }    
    private void drawMenu(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 60));
        g2.drawString("SIMPLE TETRIS", WIDTH/2 -180, 200);

        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString(">" , WIDTH/2 -150, 350 + (menuOption * 70));
        g2.drawString("START GAME", WIDTH/2 -100, 350);
        g2.drawString("DIFFICULTY:  " + difficulty, WIDTH/2 -100, 420);
        g2.drawString("QUIT", WIDTH/2 -100, 490);

        g2.setColor(Color.YELLOW);
        g2.drawString("HIGH SCORE: " + highScore, WIDTH/2 -100, 600);
    }
     private void drawGameOver(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150)); // Màn hình tối đi
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 60));
        g2.drawString("GAME OVER", WIDTH/2 -180, HEIGHT/2 -50);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("SCORE: " + score, WIDTH/2 -100, HEIGHT/2 +30);

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString( "Press ENTER to replay", WIDTH/2 -120, HEIGHT/2 +100);
        g2.drawString( "Press ESC for Menu", WIDTH/2 -120, HEIGHT/2 +140);
    }
     private void drawPlayScreen(Graphics2D g2) {
        if(playArea != null) {
            playArea.draw(g2);
        }
        g2.setColor(new Color(50, 50, 50));
        for(int i = 0; i < Board.column; i++) {
            for(int j = 0; j < Board.row; j++) {
                int x = gridOutline.left_x + (i * blockSize);
                int y = gridOutline.top_y + (j * blockSize);
                g2.drawRect(x, y, blockSize, blockSize);
            }
        }
        if(board != null) {
            Block[][] current = board.getGrid();
            
            for(int i = 0; i < Board.column; i++) {
                for(int j = 0; j< Board.row; j++) {
                    if(current[i][j].hasPiece()) {
                        g2.setColor(current[i][j].getColor());
                        int x = gridOutline.left_x + (i * blockSize);
                        int y = gridOutline.top_y + (j * blockSize);
                        g2.fillRect(x, y, blockSize, blockSize);
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
                        int x = gridOutline.left_x + ((tetromino.getX() + j) * blockSize);
                        int y = gridOutline.top_y + ((tetromino.getY() + i) * blockSize);
                        g2.fillRect (x, y, blockSize, blockSize);
                    }
                }
            }
        }
        //score display
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("SCORE: " + score, 650, 200);

        //game over display
        if(gameOver) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.drawString("GAME OVER", gridOutline.left_x - 2, gridOutline.top_y - 15);
        }

        //display next tetromino
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("NEXT SHAPE", 650, 400);
        if(nextTetromino != null) {
            int[][] nextShape = nextTetromino.getShape();
            g2.setColor(Color.GRAY);

            for(int i = 0; i < nextShape.length; i++) {
                for(int j = 0; j < nextShape[0].length; j++) {
                    if(nextShape[i][j] == 1) {
                        int x = 700 + (j*blockSize);
                        int y = 450 + (i*blockSize);
                        g2.fillRect(x, y, blockSize, blockSize);
                    }
                }
            }
        }
    }
}
