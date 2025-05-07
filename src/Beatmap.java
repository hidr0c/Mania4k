import java.util.ArrayList;
import java.util.List;

public class Beatmap {
    private String title;
    private String artist;
    private String creator;
    private String audioFilePath;
    private double bpm;
    private List<Note> notes;

    public Beatmap() {
        this.notes = new ArrayList<>();
    }

    // Title getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Artist getters and setters
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    // Creator getters and setters
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    // Audio file path getters and setters
    public String getAudioFilePath() {
        return audioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }

    // BPM getters and setters
    public double getBpm() {
        return bpm;
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    // Notes management
    public List<Note> getNotes() {
        return notes;
    }

    // Method for adding a note with timestamp and track index
    public void addNote(long timestamp, int trackIndex) {
        Note note = new Note(timestamp);
        notes.add(note);
    }

    // Original method for adding a note object directly
    public void addNote(Note note) {
        notes.add(note);
    }
}