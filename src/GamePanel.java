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
    private int fallCount = 0;

    public GamePanel(){
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setLayout(null);

        //keyboard input
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(tetromino == null || board == null) {
                    return;
                }
                int x = tetromino.getX();
                int y = tetromino.getY();

                switch (e.getKeyCode()) {
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
                }
                repaint();
            }
        });
    }
    public void launchGame(){
        gameThread = new Thread(this);
        gameThread.start();

        //cai nay de thu, se xoa sau
        this.board = new Board();
        int blueSquare[][] = { {1,1},{1,1}} ;
        this.tetromino = new Tetromino(blueSquare, Color.BLUE);

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
        if(tetromino != null && board != null) {
            fallCount++;  //time counting to fall 

            if(fallCount >= 30) { //reduce to move faster and vice versa 
                if(board.valid(tetromino, tetromino.getX(), tetromino.getY() + 1)) { //asking if the next move valid
                    tetromino.move(0, 1);
                }
                else {
                    board.pieceLock(tetromino);
                    this.tetromino = Tetromino.randomShape();
                }
                fallCount = 0;
            }
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if(board != null) {
            Block[][] current = board.getGrid();
            
            for(int i = 0; i < Board.column; i++) {
                for(int j = 0; j< Board.row; j++) {
                    if(current[i][j].hasPiece()) {
                        g.setColor(current[i][j].getColor());
                        g.fillRect(i*blockSize, j*blockSize, blockSize, blockSize);
                    }
                }
            }
        }
        if(tetromino != null) {
            int[][] shape = tetromino.getShape();
            g.setColor(tetromino.getColor());

            for(int i = 0; i < shape.length; i++) {
                for(int j = 0; j < shape[0].length; j++) {
                    if(shape[i][j] == 1) {
                        int x = (tetromino.getX() + j)*blockSize;
                        int y = (tetromino.getY() + i)*blockSize;
                        g.fillRect (x, y, blockSize, blockSize);
                    }
                }
            }
        }
    }
}