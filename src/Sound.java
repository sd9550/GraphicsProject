import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
public class Sound {

    Clip clip;
    public Sound() {
        playPeacefulMusic();
    }

    public void playPeacefulMusic() {

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sound/peaceful.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(3);
        }
        catch(Exception ex) {
            System.out.println("Error" + ex.fillInStackTrace());
        }
    }

    private void stopPlay() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }

}
