import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mania 4K");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            Game game = new Game();

            frame.add(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            // Load the beatmap
            BeatmapLoader beatmapLoader = new BeatmapLoader();
            
            Beatmap beatmap = beatmapLoader.loadBeatmapWithFileChooser(game);
            if (beatmap != null) {
                System.out.println("Beatmap loaded successfully: " + beatmap.getTitle());
            } else {
                System.err.println("Failed to load beatmap!");
            }


            new Thread(game::gameLoop).start();
        });
    }
}