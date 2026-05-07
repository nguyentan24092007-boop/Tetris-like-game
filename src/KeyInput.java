import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
    private GamePanel gamePanel;

    public KeyInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int input = e.getKeyCode();

        if (gamePanel.getGameOver()) {
            gameOverInput(input);
        } else if (gamePanel.getCurrentState() == GamePanel.GameState.MENU) {
            menuInput(input);
        } else if (gamePanel.getCurrentState() == GamePanel.GameState.PLAYING) {
            gamePlayInput(input);
        } else if (gamePanel.getCurrentState() == GamePanel.GameState.PAUSED) {
            pausedInput(input);
        }
        gamePanel.repaint();
    }

    //game over
    private void gameOverInput(int input) {
        if( input == KeyEvent.VK_ENTER) {
            SFX.playSound("sfx/menu_SFX.wav");
            gamePanel.resetGame();
        }
        else if( input == KeyEvent.VK_ESCAPE) {
            SFX.playSound("sfx/menu_SFX.wav");
            gamePanel.setCurrentState(GamePanel.GameState.MENU);
            gamePanel.setGameOver(false);
        }
    }

    //game menu
    private void menuInput(int key) {
        int menuOption = gamePanel.getMenuOption();
        if (key == KeyEvent.VK_UP) {
            SFX.playSound("sfx/menu_SFX.wav");
            menuOption--;
            if (menuOption < 0) menuOption = 3;
            gamePanel.setMenuOption(menuOption);
        }
        else if (key == KeyEvent.VK_DOWN) {
            SFX.playSound("sfx/menu_SFX.wav");
            menuOption++;
            if (menuOption > 3) menuOption = 0;
            gamePanel.setMenuOption(menuOption);
        }
        else if (key == KeyEvent.VK_ENTER) {
            SFX.playSound("sfx/menu_SFX.wav");
            if (menuOption == 0) gamePanel.resetGame();
            else if (menuOption == 1) {
                if (gamePanel.getFallSpeed() == 30) { gamePanel.setFallSpeed(15); gamePanel.setDifficulty("HARD"); }
                else if (gamePanel.getFallSpeed() == 15) { gamePanel.setFallSpeed(45); gamePanel.setDifficulty("EASY"); }
                else { gamePanel.setFallSpeed(30); gamePanel.setDifficulty("NORMAL"); }
            }
            else if (menuOption == 2) { //pick mode
                if (gamePanel.getGameModeNumber() == 0) {
                    gamePanel.setGameModeNumber(1);
                    gamePanel.setModeName("POTHOLE");
                } else if (gamePanel.getGameModeNumber() == 1) {
                    gamePanel.setGameModeNumber(2);
                    gamePanel.setModeName("RANDOM SHAPE");
                } else {
                    gamePanel.setGameModeNumber(0);
                    gamePanel.setModeName("CLASSIC");
                }
            } else if (menuOption == 3) System.exit(0);
        }
    }

    //gameplay
    private void gamePlayInput(int input) {
        if (gamePanel.getTetromino() == null || gamePanel.getBoard() == null) {
            return;
        }
        int x = gamePanel.getTetromino().getX();
        int y = gamePanel.getTetromino().getY();

        switch (input) {
            case KeyEvent.VK_LEFT:
                if (gamePanel.getBoard().valid(gamePanel.getTetromino(), x - 1, y)) {
                    gamePanel.getTetromino().move(-1, 0);
                }
                SFX.playSound("sfx/rotate_block.wav"); // rotating block sfx
                break;
            case KeyEvent.VK_RIGHT:
                if (gamePanel.getBoard().valid(gamePanel.getTetromino(), x + 1, y)) {
                    gamePanel.getTetromino().move(1, 0);
                }
                SFX.playSound("sfx/rotate_block.wav"); // rotating block sfx
                break;
            case KeyEvent.VK_DOWN:
                if (gamePanel.getBoard().valid(gamePanel.getTetromino(), x, y + 1)) {
                    gamePanel.getTetromino().move(0, 1);
                }
                SFX.playSound("sfx/rotate_block.wav"); // rotating block sfx
                break;
            case KeyEvent.VK_UP:
                if (gamePanel.getGameModeNumber() == 2) {
                    gamePanel.randomShape();
                }
                else {
                    gamePanel.rotateLogic();
                }
                SFX.playSound("sfx/rotate_block.wav"); // rotating block sfx
                break;
            case KeyEvent.VK_SPACE:  //instant drop
                while (gamePanel.getBoard().valid(gamePanel.getTetromino(), gamePanel.getTetromino().getX(), gamePanel.getTetromino().getY() + 1)) {
                    gamePanel.getTetromino().move(0, 1);
                }
                gamePanel.setFallCount(30);
                break;
        }
        if (input == KeyEvent.VK_ESCAPE) {
            gamePanel.setCurrentState(GamePanel.GameState.PAUSED);
        }
    }

    private void pausedInput(int input) {
        if (input == KeyEvent.VK_ENTER || input == KeyEvent.VK_BACK_SPACE) {
            gamePanel.setCurrentState(GamePanel.GameState.PLAYING);
        } else if (input == KeyEvent.VK_ESCAPE) {
            gamePanel.setCurrentState(GamePanel.GameState.MENU);
        }
    }
}