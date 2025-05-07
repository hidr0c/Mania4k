public class SoundManagerAccessor {
    private SoundManager soundManager;

    public SoundManagerAccessor() {
        this.soundManager = new SoundManager();
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
}