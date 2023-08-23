import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
public class Sound {

    Clip clip;
    public Sound() {

    }

    public void playPeacefulMusic() {

        try {
            stopPlay();
            AudioInputStream inputStream = AudioSystem
                    .getAudioInputStream(getClass().getResourceAsStream("/sound/doom.wav"));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            stopPlay();
            System.err.println(e.getMessage());
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
