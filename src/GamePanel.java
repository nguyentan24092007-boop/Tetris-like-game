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
    private GridOutline playArea;
    private int score = 0;
    private int topScore = 0;
    private boolean gameOver = false;


    public GamePanel(){
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setLayout(null);

        //keyboard input
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(gameOver) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        board = new Board();
                        tetromino = Tetromino.randomShape();
                        nextTetromino = Tetromino.randomShape();
                        score = 0;
                        fallCount = 0;
                        gameOver = false;
                        repaint();
                    }
                }
                if(tetromino == null || board == null) {
                    return;
                }
                int x = tetromino.getX();
                int y = tetromino.getY();

                switch (e.getKeyCode()) {
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
                        int[][] nextShape = tetromino.rotateShape();
                        int[][] currentShape = tetromino.getShape();

                        tetromino.setShape(nextShape);
                        if(!board.valid(tetromino, x, y)) {
                            tetromino.setShape(currentShape);
                        }
                        break;
                    case KeyEvent.VK_SPACE:  //instant drop
                        while(board.valid(tetromino, tetromino.getX(), tetromino.getY() + 1)) {
                            tetromino.move(0, 1);
                        }
                        fallCount = 30; 
                        break;
                }
                repaint();
            }
        });
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

                        if(score > topScore) {
                            topScore = score;
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

            //press enter to retry
            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.drawString("Press ENTER to retry", 1020/4+5, GridOutline.top_y -20);
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
