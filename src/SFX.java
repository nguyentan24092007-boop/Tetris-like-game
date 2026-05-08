import javax.sound.sampled.*;
import java.io.File;

public class SFX {
    public static String THEME_BGM = "sfx/theme.wav"; 
    public static String GAMEOVER_BGM = "sfx/game_over.wav";
    public static String MOVE_SFX = "sfx/move.wav"; // when move block and change option in menu
    public static String ROTATE_SFX = "sfx/rotate.wav"; // press up
    public static String CLEAR_SFX = "sfx/clear.wav"; // line clear
    public static String DROP_SFX = "sfx/drop.wav"; //for instance drop (press space)
    public static String SELECT_SFX = "sfx/select.wav"; // press enter, esc, backspace

    public static void playSound(String filePath) {
        try {
            File soundPath = new File(filePath);
            if(soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);

                clip.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //BGM looping
    public static Clip playBGM(String filePath) {
        try {
            File soundPath = new File(filePath);
            if(soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                
                clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the BGM forever
                clip.start();
                return clip;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void pauseSound(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    public static void resumeSound(Clip clip) {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }
    public static void stopSound(Clip clip) {
        if (clip != null) {
            clip.stop();
            clip.flush();
            clip.setFramePosition(0);
        }
    }
}
