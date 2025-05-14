import java.awt.*;
import java.util.List;

public class Renderer
{
    private Game game;

    private static final int TRACK_WIDTH = 80;
    private static final int TRACK_SPACING = 10;
    private static final int HIT_POSITION_Y = 500;

    public Renderer(Game game) {
        this.game = game;
    }

    public void render(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw tracks
        Track[] tracks = game.getTracks();
        for (int i = 0; i < tracks.length; i++) {
            int x = 20 + i * (TRACK_WIDTH + TRACK_SPACING);

            // Draw track background
            //g.setColor(Color.DARK_GRAY);
            //g.fillRect(x, 0, TRACK_WIDTH, 600);

            g.setColor(Color.WHITE);
            int diameter = TRACK_WIDTH;
            g.drawOval(x, HIT_POSITION_Y - diameter / 2, diameter, diameter);

            if (tracks[i].isKeyPressed()) {
                g.setColor(Color.CYAN);
                diameter = TRACK_WIDTH;
                int centerX = x + TRACK_WIDTH / 2;
                int centerY = HIT_POSITION_Y;
                g.fillOval(centerX - diameter / 2, centerY - diameter / 2, diameter, diameter);
            }

            // Draw notes (make a copy to avoid ConcurrentModificationException)
            java.util.List<Note> notesCopy = new java.util.ArrayList<>(tracks[i].getNotes());
            for (Note note : notesCopy) {
                drawNote(g, note, x);
            }
        }

        // Draw score and combo
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + game.getScore(), 10, 30);
        g.drawString("Combo: " + game.getCombo(), 10, 60);
    }

    public Note findClosestNote(long currentTime, List<Note> notes) {
        // Use iterator to safely remove off-screen notes
        notes.removeIf(note -> note.getYPosition(currentTime) > 600);

        // If there are no remaining notes, return null
        if (notes.isEmpty()) {
            return null;
        }

        // Find the closest note to the "hit position" (Y = 500)
        Note closest = null;
        double minDistance = Double.MAX_VALUE;

        for (Note note : notes) {
            double distance = Math.abs(note.getYPosition(currentTime) - 500); // Y = 500 is the hit position
            if (distance < minDistance) {
                minDistance = distance;
                closest = note;
            }
        }

        return closest; // Return the closest note
    }

    private void drawNote(Graphics g, Note note, int trackX)
    {
        long currentTime = game.getCurrentTime(); // Get the current game time
        int noteY = (int) note.getYPosition(currentTime);
        // Set color based on whether it's a normal note or a hold note
        if (note.isHoldNote()) {
            int holdEndY = note.getHoldEndPosition(currentTime); // Get the end Y position for the hold note

            // Render hold note as a vertical bar
            g.setColor(new Color(255, 200, 0)); // Orange for hold notes
            g.fillRect(trackX, Math.min(noteY, holdEndY), TRACK_WIDTH, Math.abs(holdEndY - noteY));

            // Outline for the hold note
            g.setColor(Color.BLACK);
            g.drawRect(trackX, Math.min(noteY, holdEndY), TRACK_WIDTH, Math.abs(holdEndY - noteY));
        } else {
            // Render normal note as a white circle
            g.setColor(Color.WHITE);
            int diameter = TRACK_WIDTH - 10;
            int centerX = trackX + TRACK_WIDTH / 2;
            int centerY = noteY;
            g.fillOval(centerX - diameter / 2, centerY - diameter / 2, diameter, diameter);

            // Outline for the normal note
            g.setColor(Color.BLACK);
            g.drawOval(centerX - diameter / 2, centerY - diameter / 2, diameter, diameter);
        }
    }
}
