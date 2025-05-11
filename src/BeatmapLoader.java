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
        if (game == null) {
            throw new IllegalArgumentException("Game instance cannot be null when loading beatmaps.");
        }

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
        if (game == null) {
            throw new IllegalArgumentException("Game instance cannot be null when loading beatmaps.");
        }
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
                    // Format: x,y,time,type,hitSound,[endTime or extras]
                    String[] parts = line.split(",");

                    if (parts.length >= 5) { // Ensure the minimum expected fields are present
                        try {
                            int x = Integer.parseInt(parts[0]); // x-coordinate (for osu!mania column)
                            int y = Integer.parseInt(parts[1]); // y-coordinate (not used, always 192)
                            long timestamp = Long.parseLong(parts[2]); // Time the note appears
                            int type = Integer.parseInt(parts[3]); // Note type: 1 = single, 128 = hold
                            long endTime = 0; // Default endTime for single notes

                            // If it's a hold note, extract the endTime field (6th parameter)
                            if (type == 128 && parts.length >= 6) {
                                endTime = Long.parseLong(parts[5].split(":")[0]); // Parse endTime
                            }
                            int trackIndex = Math.min(x / 128, 3);  // Assume 4 tracks (0-3 for 4K)


                            // Add note to beatmap
                            Note note = new Note(timestamp, x, type, endTime, game, trackIndex);  // Pass the Game instance
                            beatmap.addNote(note);


                            // Also add to game's tracks if applicable
                            if (x >= 0 && x < 512) {
                                game.getTracks()[trackIndex].addNote(note);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid note format: " + line);
                        }
                    }
                }
            }

            // Load audio file if exists
            if (beatmap.getAudioFilePath() != null) {
                game.getSoundManager().playMusic(beatmap.getAudioFilePath());
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
            e.printStackTrace();
        }

        return beatmap;
    }
}