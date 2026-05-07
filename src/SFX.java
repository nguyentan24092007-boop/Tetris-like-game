import javax.sound.sampled.*;
import java.io.File;

public class SFX {

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
    
}