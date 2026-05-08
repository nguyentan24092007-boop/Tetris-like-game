import java.awt.*;

public class GameUserInterface {
    public static void draw(Graphics2D g2, GamePanel gamePanel) {
        GameTheme theme = GameTheme.createCurrentTheme();

        GradientPaint background = new GradientPaint(0, 0, theme.backgroundTop, 0, GamePanel.HEIGHT, theme.backgroundTopBottom);
        g2.setPaint(background);
        g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        //game menu
        if (gamePanel.getCurrentState() == GamePanel.GameState.MENU) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 60));
            g2.drawString("TETRIS-LIKE GAME", GamePanel.WIDTH / 2 - 270, 200);

            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString("\u2192" , GamePanel.WIDTH / 2 - 200, 350 + (gamePanel.getMenuOption() * 70));
            
            g2.setFont(new Font("Arial", Font.BOLD, 30));
            g2.drawString("START GAME", GamePanel.WIDTH / 2 - 150, 350);
            g2.drawString("DIFFICULTY:  " + gamePanel.getDifficulty(), GamePanel.WIDTH / 2 - 150, 420);
            g2.drawString("QUIT", GamePanel.WIDTH / 2 - 150, 560);
            g2.drawString("MODE: "+ gamePanel.getModeName(), GamePanel.WIDTH/2 -150, 490);

            g2.setFont(new Font("Arial", Font.PLAIN, 22));
            g2.drawString("\u2191  \u2193 : Navigate    |    ENTER : Select", 300, 700);
            return; // Skip drawing the rest of the game board!
        }

        if(gamePanel.getPlayArea() != null) {
            gamePanel.getPlayArea().draw(g2);
            GradientPaint playArea = new GradientPaint(
                gamePanel.getPlayArea().getLeftX(), gamePanel.getPlayArea().getTopY(), theme.areaTop,
                gamePanel.getPlayArea().getLeftX(), gamePanel.getPlayArea().getBottomY(), theme.areaBottom
            );
            g2.setPaint(playArea);
            g2.fillRoundRect(gamePanel.getPlayArea().getLeftX(), gamePanel.getPlayArea().getTopY(), gamePanel.getPlayArea().getWidth(), gamePanel.getPlayArea().getHeight(), 15, 15);
        }

        g2.setColor(new Color(255, 255, 255,30));
        g2.setStroke(new BasicStroke(2f));
        for(int i = 0; i < Board.column; i++) {
            for(int j = 0; j < Board.row; j++) {
                int x = gamePanel.getPlayArea().getLeftX() + (i * GamePanel.blockSize);
                int y = gamePanel.getPlayArea().getTopY() + (j * GamePanel.blockSize);
                g2.drawRoundRect(x, y, GamePanel.blockSize, GamePanel.blockSize, 10, 10);
            }
        }
        if(gamePanel.getBoard() != null) {
            Block[][] current = gamePanel.getBoard().getGrid();

            for(int i = 0; i < Board.column; i++) {
                for(int j = 0; j< Board.row; j++) {
                    if(current[i][j].hasPiece()) {
                        g2.setColor(current[i][j].getColor());
                        int x = gamePanel.getPlayArea().getLeftX() + (i * GamePanel.blockSize);
                        int y = gamePanel.getPlayArea().getTopY() + (j * GamePanel.blockSize);
                        g2.fillRoundRect(x, y, GamePanel.blockSize-2, GamePanel.blockSize-2, 10,10);
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
                        int x = gamePanel.getPlayArea().getLeftX() + ((gamePanel.getTetromino().getX() + j) * GamePanel.blockSize);
                        int y = gamePanel.getPlayArea().getTopY() + ((gamePanel.getTetromino().getY() + i) * GamePanel.blockSize);
                        g2.fillRoundRect(x+1, y+1, GamePanel.blockSize-2, GamePanel.blockSize-2, 10,10);
                    }
                }
            }
        }
        
        //score display
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("SCORE: " + gamePanel.getScore(), 580, 140);
        g2.drawString("TOP SCORE: "+ gamePanel.getTopScore(), 560, 210);

        //game over display
        if(gamePanel.getGameOver()) {
            g2.setColor(new Color(0,0, 0,180));
            g2.fillRect(gamePanel.getPlayArea().getLeftX(), gamePanel.getPlayArea().getTopY(), 300, 600);

            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 45));
            g2.drawString("GAME OVER", gamePanel.getPlayArea().getLeftX() + 12, gamePanel.getPlayArea().getTopY() +300);

            //press enter to retry and esc for menu
            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.drawString("Press ENTER to retry or ESC for Menu", 45, gamePanel.getPlayArea().getTopY() -20);

        }

        //display next tetromino
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("NEXT SHAPE", 628, 320);
        if(gamePanel.getNextTetromino() != null) {
            int[][] nextShape = gamePanel.getNextTetromino().getShape();
            g2.setColor(Color.GRAY);

            for(int i = 0; i < nextShape.length; i++) {
                for(int j = 0; j < nextShape[0].length; j++) {
                    if(nextShape[i][j] == 1) {
                        int x = 675 + (j*GamePanel.blockSize);
                        int y = 370 + (i*GamePanel.blockSize);
                        g2.fillRoundRect(x+1, y+1, GamePanel.blockSize-2, GamePanel.blockSize-2, 10,10);
                    }
                }
            }
        }

        //button guide
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Ariel", Font.PLAIN, 25));
        if(gamePanel.getGameModeNumber() == 2) {
            g2.drawString("\u2191 : Change tetromino", 600, 510);
        }
        else {
            g2.drawString("\u2191 : Rotate tetromino", 600, 510);
        }
        g2.drawString("\u2190 : Move left", 600, 542);
        g2.drawString("\u2192 : Move right", 600, 574);
        g2.drawString("\u2193 : Fast drop", 600, 606);
        g2.drawString("Space : Instance drop", 600, 638);
        g2.drawString("Esc : Pause", 600, 670);

        //game pause
        if (gamePanel.getCurrentState() == GamePanel.GameState.PAUSED) {
            g2.setColor(new Color(0, 0, 0, 150)); //Darken the screen when pausing the game
            g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Ariel", Font.BOLD, 50));
            g2.drawString("PAUSED", GamePanel.WIDTH / 2 - 100, GamePanel.HEIGHT / 2 - 40);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Ariel", Font.PLAIN, 30));
            g2.drawString("Press ENTER or BACKSPACE to Resume", GamePanel.WIDTH / 2 - 250, GamePanel.HEIGHT / 2 + 30);
            g2.drawString("Press ESC for Menu", GamePanel.WIDTH / 2 - 125, GamePanel.HEIGHT / 2 + 80);
        }
    }
}