import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Track {
    private int index;
    private List<Note> notes;
    private boolean keyPressed;

    public Track(int index) {
        this.index = index;
        this.notes = new ArrayList<>();
        this.keyPressed = false;
    }

    public void update(double delta) {
        // Update all notes
        Iterator<Note> iterator = notes.iterator();
        while (iterator.hasNext()) {
            Note note = iterator.next();
            note.update(delta);

            // Remove notes that have gone off screen
            if (note.getYPosition() > 600) {
                iterator.remove();
            }
        }
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public void removeNote(Note note) {
        notes.remove(note);
    }

    public Note getClosestNote() {
        if (notes.isEmpty()) {
            return null;
        }

        Note closest = null;
        double minDistance = Double.MAX_VALUE;

        for (Note note : notes) {
            double distance = Math.abs(note.getYPosition() - 500); // 500 is hit position
            if (distance < minDistance) {
                minDistance = distance;
                closest = note;
            }
        }

        return closest;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public boolean isKeyPressed() {
        return keyPressed;
    }

    public void setKeyPressed(boolean keyPressed) {
        this.keyPressed = keyPressed;
    }
}