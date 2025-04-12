public class Note {
    private double yPosition;
    private double speed;
    private long timestamp; // Time when the note should be hit (in ms)

    public Note(long timestamp) {
        this.timestamp = timestamp;
        this.yPosition = 0;
        this.speed = 300; // pixels per second
    }

    public void update(double delta) {
        yPosition += speed * delta;
    }

    public double getYPosition() {
        return yPosition;
    }

    public long getTimestamp() {
        return timestamp;
    }
}