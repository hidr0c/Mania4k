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

            // Đảm bảo game nhận focus để bắt phím
            game.setFocusable(true);
            game.requestFocusInWindow();
            game.addKeyListener(game);

            // Sau 3 giây mới load beatmap và start game loop
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // 3 seconds delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Load beatmap và phát nhạc
                Beatmap beatmap = null;
                try {
                    beatmap = BeatmapLoader.loadBeatmap("D:/Code Projects/Mania 4k/assets/beatmaps/sample.txt", game);
                    System.out.println("Beatmap loaded successfully: " + beatmap.getTitle());
                } catch (Exception e) {
                    System.err.println("Failed to load beatmap!");
                    e.printStackTrace();
                }
                // Start game loop
                game.gameLoop();
            }).start();
        });
    }
}