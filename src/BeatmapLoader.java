import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatmapLoader {
    public void loadBeatmap(String filename, Game game) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Format: timestamp,track
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    long timestamp = Long.parseLong(parts[0]);
                    int trackIndex = Integer.parseInt(parts[1]);

                    if (trackIndex >= 0 && trackIndex < 4) {
                        Note note = new Note(timestamp);
                        game.getTracks()[trackIndex].addNote(note);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // A simple method to generate a test beatmap
    public void generateTestBeatmap(Game game) {
        // Add some test notes to each track
        for (int i = 0; i < 20; i++) {
            for (int track = 0; track < 4; track++) {
                if (Math.random() > 0.5) { // 50% chance to add a note
                    Note note = new Note(1000 + i * 500);
                    game.getTracks()[track].addNote(note);
                }
            }
        }
    }
}