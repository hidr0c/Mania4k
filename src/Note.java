public class Note {
    private double yPosition;
    private double speed;
    private long timestamp; // Time when the note should be hit (in ms)
    private boolean hit;
    private String judgment; // "perfect", "great", "good", "miss" or null if not judged yet

    public Note(long timestamp) {
        this.timestamp = timestamp;
        this.yPosition = 0;
        this.speed = 300; // pixels per second
        this.hit = false;
        this.judgment = null;
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

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public String getJudgment() {
        return judgment;
    }

    public void setJudgment(String judgment) {
        this.judgment = judgment;
        this.hit = !judgment.equals("miss");
    }
}