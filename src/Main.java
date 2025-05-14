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
            Beatmap beatmap = null;
            try {
                beatmap = BeatmapLoader.loadBeatmap("D:/Code Projects/Mania 4k/assets/beatmaps/sample.txt", game);
                System.out.println("Beatmap loaded successfully: " + beatmap.getTitle());
                try {
                    Thread.sleep(3000); // 3 seconds delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.err.println("Failed to load beatmap!");
                e.printStackTrace();
            }
            new Thread(game::gameLoop).start();

            game.setFocusable(true);
            game.requestFocusInWindow();
            game.addKeyListener(game);

        });
    }
}