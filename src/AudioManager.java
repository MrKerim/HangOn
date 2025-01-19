import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AudioManager {
    private static Clip clip;
    private static final String filePath = "audio/theme.wav";
    private static boolean playing = false;

    public static void playMusic() {
        try {
            // Load the audio file as a resource
            InputStream audioStream = AudioManager.class.getResourceAsStream("audio/theme.wav");
            if (audioStream == null) {
                throw new IOException("Audio file not found");
            }

            // Convert the InputStream to an AudioInputStream
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(audioStream));

            // Get a clip and open it with the audio stream
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();

            // Loop the audio if desired
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            playing = true;

        } catch (LineUnavailableException |
                 UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopMusic() {
        clip.stop();
        playing = false;
    }

    public static void togglePlayStopMusic() {
        if (playing) stopMusic();
        else playMusic();
    }
}
