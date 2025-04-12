import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private Map<String, Clip> soundEffects;
    private Clip musicClip;

    public SoundManager() {
        soundEffects = new HashMap<>();
        loadSoundEffects();
    }

    private void loadSoundEffects() {
        // Load hit sound effects
        try {
            loadSound("hit", "assets/note audio/answer.wav");
        } catch (Exception e) {
            System.out.println("Error loading sound effects: " + e.getMessage());
        }
    }

    private void loadSound(String name, String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File file = new File(path);
        if (file.exists()) {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            soundEffects.put(name, clip);
        }
    }

    public void playSound(String name) {
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void playMusic(String path) {
        try {
            if (musicClip != null) {
                musicClip.stop();
                musicClip.close();
            }

            File musicFile = new File(path);
            if (musicFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
                musicClip = AudioSystem.getClip();
                musicClip.open(audioIn);
                musicClip.start();
            }
        } catch (Exception e) {
            System.out.println("Error playing music: " + e.getMessage());
        }
    }

    public void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
        }
    }
}