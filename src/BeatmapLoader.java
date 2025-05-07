import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BeatmapLoader {

    // Hàm để chọn và load một beatmap file
    public Beatmap loadBeatmapWithFileChooser(Game game) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Beatmap File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Beatmap Files", "txt", "json", "osu"));

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return loadBeatmap(selectedFile.getAbsolutePath(), game);
        }
        return null;
    }

    // Load beatmap from a file
    public Beatmap loadBeatmap(String filename, Game game) {
        Beatmap beatmap = new Beatmap();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean inMetadataSection = false;
            boolean inNotesSection = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("//")) {
                    continue;
                }

                // Check for section headers
                if (line.equals("[Metadata]")) {
                    inMetadataSection = true;
                    inNotesSection = false;
                    continue;
                } else if (line.equals("[Notes]")) {
                    inMetadataSection = false;
                    inNotesSection = true;
                    continue;
                }

                // Process metadata
                if (inMetadataSection) {
                    String[] parts = line.split(":");
                    if (parts.length >= 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();

                        switch (key.toLowerCase()) {
                            case "title":
                                beatmap.setTitle(value);
                                break;
                            case "artist":
                                beatmap.setArtist(value);
                                break;
                            case "creator":
                                beatmap.setCreator(value);
                                break;
                            case "audiofile":
                                beatmap.setAudioFilePath(value);
                                break;
                            case "bpm":
                                try {
                                    beatmap.setBpm(Double.parseDouble(value));
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid BPM value: " + value);
                                }
                                break;
                        }
                    }
                }

                // Process notes
                if (inNotesSection) {
                    // Format: timestamp,track
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        try {
                            long timestamp = Long.parseLong(parts[0]);
                            int trackIndex = Integer.parseInt(parts[1]);

                            beatmap.addNote(timestamp, trackIndex);

                            // Also add to the game's tracks if game is provided
                            if (game != null && trackIndex >= 0 && trackIndex < 4) {
                                Note note = new Note(timestamp);
                                game.getTracks()[trackIndex].addNote(note);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid note format: " + line);
                        }
                    }
                }
            }

            // Load audio file if exists
            if (beatmap.getAudioFilePath() != null && game != null) {
                game.getSoundManager().playMusic(beatmap.getAudioFilePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return beatmap;
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

    // Create a sample beatmap file
    public void createSampleBeatmapFile(String filename) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(filename)) {
            writer.println("[Metadata]");
            writer.println("Title: Sample Beatmap");
            writer.println("Artist: Unknown");
            writer.println("Creator: BeatmapLoader");
            writer.println("AudioFile: assets/music/sample.wav");
            writer.println("BPM: 120");
            writer.println("");
            writer.println("[Notes]");

            // Generate some sample notes
            for (int i = 0; i < 40; i++) {
                long timestamp = 1000 + i * 500;
                int track = (int) (Math.random() * 4);
                writer.println(timestamp + "," + track);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}