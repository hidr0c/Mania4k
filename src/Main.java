import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mania 4K");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            Game game = new Game();
            System.out.println("Game created");
            frame.add(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            // Load the beatmap
            BeatmapLoader beatmapLoader = new BeatmapLoader();
            System.out.println("Loading beatmap");

            try {
                // Ensure game is correctly passed when selecting a beatmap
                Beatmap beatmap = beatmapLoader.loadBeatmapWithFileChooser(game);
                if (beatmap != null) {
                    System.out.println("Beatmap loaded successfully: " + beatmap.getTitle());
                } else {
                    System.out.println("Failed to load beatmap!");
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e.getMessage());
            }
            new Thread(game::gameLoop).start();

        });
    }
}