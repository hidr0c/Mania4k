import java.util.Objects;

public class Note
{
    private static final double SCROLL_SPEED = 0.5; // Adjust this based on your game's scroll rate
    private long timestamp;
    private int x; // x position (column)
    private int y; // y position (always 192 for mania, but we'll store it)
    private int type; // Type of the note (1 = normal, 128 = hold)
    private long endTime; // For hold notes
    private boolean hit;     // Flag to indicate if the note has been hit
    private Game game;
    private int trackIndex;

    public Note(long timestamp, int x, int type, long endTime, Game game, int trackIndex) {
        this.game = game;
        if (game == null) {
            System.out.println("Game instance cannot be null");
        }
        System.out.println("Note created");
        this.timestamp = timestamp;
        this.x = x;
        this.y = 192; // Fixed for osu!mania
        this.type = type;
        this.endTime = endTime; // 0 if not a hold note
        this.hit = false;

        this.trackIndex = trackIndex;
    }

    /**
     * Access the track this note belongs to.
     */
    public Track getTrack(Game game) {
        System.out.println("Game instance:" + game);
        if (trackIndex >= 0 && trackIndex < 4) {
            return game.getTracks()[trackIndex];
        } else {
            throw new IndexOutOfBoundsException("Invalid track index: " + trackIndex);
        }
    }
    public void checkHit(long currentTime) {
        if (!hit) {
            this.hit = true; // Mark the note as hit
        }
    }
    public boolean isHit(Game game){
        long currentTime = game.getCurrentTime();
        checkHit(currentTime);
        return hit;
    }
    public void setJudgment(String judgement) {
        System.out.println(judgement);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getX() {
        return x;
    }

    public int getType() {
        return type;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean isHoldNote() {
        return type == 128;
    }
    /**
     * Dynamic Y position for the note based on current time.
     *
     * @param currentTime The current time in the game (ms).
     * @return The Y position of the note for rendering.
     */
    public int getYPosition(long currentTime) {
        long timeDifference = timestamp - currentTime;

        // Positive means the note is above the hit line and scrolling towards it
        return (int) (192 - (SCROLL_SPEED * timeDifference));
    }
    public void update(long currentTime) {
        // Dynamically recalculate the Y position
        this.y = getYPosition(currentTime);
    }

    /**
     * For hold notes: Calculate the current end position for rendering.
     *
     * @param currentTime The current time in the game (ms).
     * @return The Y position of the end of a hold note.
     */
    public int getHoldEndPosition(long currentTime) {
        if (isHoldNote()) {
            long timeDifference = endTime - currentTime;

            return (int) (192 - (SCROLL_SPEED * timeDifference));
        }
        return 192;
    }

    public Note getClosestNote() {
        if (game != null) {
            return getTrack(game).getClosestNote();
        } else {
            throw new IllegalStateException("Game instance is null in Note");
        }
    }
}
