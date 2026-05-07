import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Simple Tetris");

        //Add GamePanel to Window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
        SFX.playBGM("sfx/bgm.wav"); //play BGM
    }
}