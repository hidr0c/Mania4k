import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener {
    private static final int GAME_WIDTH = 400;
    private static final int GAME_HEIGHT = 600;

    private boolean running;
    private long startTime;
    private Track[] tracks;
    private Renderer renderer;
    private InputHandler inputHandler;
    private SoundManager soundManager;

    private int score;
    private int combo;

    public Game() {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);

        init();
    }

    private void init() {
        System.out.println( "Initializing game...");
        running = true;
        // Initialize components
        System.out.println( "Initializing components...");
        tracks = new Track[4];
        for (int i = 0; i < 4; i++) {
            tracks[i] = new Track(i);
        }
        renderer = new Renderer(this);
        inputHandler = new InputHandler(this);
        soundManager = new SoundManager();
        // Initialize start time
        startTime = System.currentTimeMillis();
        score = 0;
        combo = 0;

    }

    public void gameLoop() {
        final int FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / FPS;

        long lastLoopTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;

            long delta = updateLength / OPTIMAL_TIME;

            // Update game state
            update(delta);

            // Render game
            repaint();

            try {
                long sleepTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public long getCurrentTime() {
        return System.currentTimeMillis() - startTime;
    }

    private void update(long delta) {
        // Update notes and other game objects
        for (Track track : tracks) {
            track.update(delta);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.render(g);
    }

    // Getters
    public Track[] getTracks() {
        return tracks;
    }

    public int getScore() {
        return score;
    }

    public int getCombo() {
        return combo;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }


    // Method to add points and update combo
    public void addScore(int points, boolean hit) {
        score += points;
        if (hit) {
            combo++;
        } else {
            combo = 0;
        }
    }

    // KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        inputHandler.handleKeyPress(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        inputHandler.handleKeyRelease(e.getKeyCode());
    }
}