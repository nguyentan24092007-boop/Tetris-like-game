import java.awt.*;

public class GameUserInterface {
    public static void draw(Graphics2D g2, GamePanel gamePanel) {
        //game menu
        if (gamePanel.getCurrentState() == GamePanel.GameState.MENU) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 60));
            g2.drawString("TETRIS-LIKE GAME", GamePanel.WIDTH / 2 - 270, 200);

            g2.setFont(new Font("Arial", Font.BOLD, 30));
            g2.drawString(">" , GamePanel.WIDTH / 2 - 200, 350 + (gamePanel.getMenuOption() * 70));
            g2.drawString("START GAME", GamePanel.WIDTH / 2 - 150, 350);
            g2.drawString("DIFFICULTY:  " + gamePanel.getDifficulty(), GamePanel.WIDTH / 2 - 150, 420);
            g2.drawString("QUIT", GamePanel.WIDTH / 2 - 150, 560);
            g2.drawString("MODE: "+ gamePanel.getModeName(), GamePanel.WIDTH/2 -150, 490);
            return; // Skip drawing the rest of the game board!
        }

        if(gamePanel.getPlayArea() != null) {
            gamePanel.getPlayArea().draw(g2);
        }

        g2.setColor(new Color(50, 50, 50));
        for(int i = 0; i < Board.column; i++) {
            for(int j = 0; j < Board.row; j++) {
                int x = GridOutline.left_x + (i * GamePanel.blockSize);
                int y = GridOutline.top_y + (j * GamePanel.blockSize);
                g2.drawRect(x, y, GamePanel.blockSize, GamePanel.blockSize);
            }
        }
        if(gamePanel.getBoard() != null) {
            Block[][] current = gamePanel.getBoard().getGrid();
            
            for(int i = 0; i < Board.column; i++) {
                for(int j = 0; j< Board.row; j++) {
                    if(current[i][j].hasPiece()) {
                        g2.setColor(current[i][j].getColor());
                        int x = GridOutline.left_x + (i * GamePanel.blockSize);
                        int y = GridOutline.top_y + (j * GamePanel.blockSize);
                        g2.fill3DRect(x, y, GamePanel.blockSize, GamePanel.blockSize, true);
                    }
                }
            }
        }
        if(gamePanel.getTetromino() != null) {
            int[][] shape = gamePanel.getTetromino().getShape();
            g2.setColor(gamePanel.getTetromino().getColor());

            for(int i = 0; i < shape.length; i++) {
                for(int j = 0; j < shape[0].length; j++) {
                    if(shape[i][j] == 1) {
                        int x = GridOutline.left_x + ((gamePanel.getTetromino().getX() + j) * GamePanel.blockSize);
                        int y = GridOutline.top_y + ((gamePanel.getTetromino().getY() + i) * GamePanel.blockSize);
                        g2.fill3DRect(x, y, GamePanel.blockSize, GamePanel.blockSize, true);
                    }
                }
            }
        }
        //score display
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("SCORE: " + gamePanel.getScore(), 600, 200);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Ariel", Font.BOLD, 30));
        g2.drawString("TOP SCORE: "+ gamePanel.getTopScore(), 600, 300);

        //game over display
        if(gamePanel.getGameOver()) {
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
        if(gamePanel.getNextTetromino() != null) {
            int[][] nextShape = gamePanel.getNextTetromino().getShape();
            g2.setColor(Color.GRAY);

            for(int i = 0; i < nextShape.length; i++) {
                for(int j = 0; j < nextShape[0].length; j++) {
                    if(nextShape[i][j] == 1) {
                        int x = 650 + (j*GamePanel.blockSize);
                        int y = 450 + (i*GamePanel.blockSize);
                        g2.fill3DRect(x, y, GamePanel.blockSize, GamePanel.blockSize, true);
                    }
                }
            }
        }
    }
}