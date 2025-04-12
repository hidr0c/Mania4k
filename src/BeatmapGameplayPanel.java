import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class BeatmapGameplayPanel extends JPanel implements KeyListener {
    // List to store beat timings (in ms) loaded from beatmap file
    private ArrayList<Integer> noteTimings;
    // Game start time in ms (set when game starts)
    private long startTime;
    // Speed factor (pixels per millisecond) for falling notes
    private double speed = 0.3;
    // Y position of the hit area circle (fixed)
    private final int hitAreaY = 500;
    // Hit area circle settings
    private final int hitAreaX = 200;  // horizontal center
    private final int hitRadius = 50;
    // Timing accuracy window (ms)
    private final int hitWindow = 150;

    public BeatmapGameplayPanel() {
        noteTimings = new ArrayList<>();
        loadBeatmap("assets/beatmaps/sample.txt");

        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        // Initialize game start time when the panel is shown
        startTime = System.currentTimeMillis();

        // Timer to update game state and repaint (simulate a game loop)
        Timer timer = new Timer(16, e -> {
            // Remove notes that have passed far beyond the hit area (missed)
            long currentTime = System.currentTimeMillis() - startTime;
            Iterator<Integer> it = noteTimings.iterator();
            while (it.hasNext()) {
                int noteTime = it.next();
                // If the note passed the hit area by more than hitWindow without a hit, remove it
                if (currentTime - noteTime > hitWindow) {
                    it.remove();
                }
            }
            repaint();
        });
        timer.start();
    }

    // Load beatmap notes from a file. Each line should contain a number (time in ms)
    private void loadBeatmap(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    int noteTime = Integer.parseInt(line.trim());
                    noteTimings.add(noteTime);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid note time in beatmap: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading beatmap: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the hit area circle (input circle)
        g2d.setColor(Color.BLUE);
        g2d.drawOval(hitAreaX - hitRadius, hitAreaY - hitRadius, hitRadius * 2, hitRadius * 2);
        g2d.drawString("Hit Area", hitAreaX - 25, hitAreaY - hitRadius - 10);

        // Calculate the current game time (in ms)
        long currentTime = System.currentTimeMillis() - startTime;

        // Draw falling notes (red filled circles)
        g2d.setColor(Color.RED);
        for (int noteTime : noteTimings) {
            // Calculate vertical position: when currentTime equals noteTime, note is at hitAreaY
            int yPos = hitAreaY - (int) ((noteTime - currentTime) * speed);
            // Fixed horizontal position for simplicity
            int xPos = hitAreaX - 10;
            g2d.fillOval(xPos, yPos, 20, 20);
        }

        // Display debug text: current time and remaining notes
        g2d.setColor(Color.BLACK);
        g2d.drawString("Time: " + currentTime + " ms", 10, 20);
        g2d.drawString("Notes left: " + noteTimings.size(), 10, 40);
    }

    // When the user presses SPACE, check if a note is in the hit window
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            long currentTime = System.currentTimeMillis() - startTime;
            boolean hitRegistered = false;
            Iterator<Integer> iterator = noteTimings.iterator();
            while (iterator.hasNext()) {
                int noteTime = iterator.next();
                if (Math.abs(noteTime - currentTime) <= hitWindow) {
                    hitRegistered = true;
                    iterator.remove();
                    System.out.println("Hit! Note time: " + noteTime + " at " + currentTime + " ms");
                    break;
                }
            }
            if (!hitRegistered) {
                System.out.println("Miss! Current time: " + currentTime + " ms");
            }
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    // Main method to run the gameplay panel
    public static void main(String[] args) {
        JFrame frame = new JFrame("Beatmap Gameplay Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        BeatmapGameplayPanel panel = new BeatmapGameplayPanel();
        frame.add(panel);
        frame.setVisible(true);
    }
}