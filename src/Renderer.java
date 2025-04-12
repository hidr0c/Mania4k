import java.awt.*;

public class Renderer {
    private Game game;

    private static final int TRACK_WIDTH = 80;
    private static final int TRACK_SPACING = 10;
    private static final int HIT_POSITION_Y = 500;

    public Renderer(Game game) {
        this.game = game;
    }

    public void render(Graphics g) {
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

            // Draw notes
            for (Note note : tracks[i].getNotes()) {
                drawNote(g, note, x);
            }
        }

        // Draw score and combo
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + game.getScore(), 10, 30);
        g.drawString("Combo: " + game.getCombo(), 10, 60);
    }

    private void drawNote(Graphics g, Note note, int trackX) {
        int noteY = (int) note.getYPosition();

        g.setColor(Color.YELLOW);
        g.fillRect(trackX, noteY - 10, TRACK_WIDTH, 20);

        g.setColor(Color.BLACK);
        g.drawRect(trackX, noteY - 10, TRACK_WIDTH, 20);
    }
}