import java.awt.event.KeyEvent;

public class InputHandler {
    private Game game;
    private int[] keyBindings = {
            KeyEvent.VK_D,   // Track 0
            KeyEvent.VK_F,   // Track 1
            KeyEvent.VK_J,   // Track 2
            KeyEvent.VK_K    // Track 3
    };

    public InputHandler(Game game) {
        this.game = game;
    }

    public void handleKeyPress(int e) {
        int keyCode = e;
        char keyChar = (char) e;
        System.out.println("Key pressed: keyCode=" + keyCode + ", keyChar=" + keyChar);
        // Vòng lặp xử lý như cũ
        for (int i = 0; i < keyBindings.length; i++) {
            if (keyCode == keyBindings[i]) {
                game.getTracks()[i].setKeyPressed(true);
                checkNoteHit(i);
                break;
            }
        }
    }

    public void handleKeyRelease(int keyCode) {
        for (int i = 0; i < keyBindings.length; i++) {
            if (keyCode == keyBindings[i]) {
                game.getTracks()[i].setKeyPressed(false);
                break;
            }
        }
    }

    private void checkNoteHit(int trackIndex) {
        Track track = game.getTracks()[trackIndex];
        Note closestNote = track.getClosestNote();

        if (closestNote != null) {
            double distance = Math.abs(closestNote.getYPosition() - 500); // 500 is hit position

            // Different scoring windows
            if (distance < 20) {
                // Perfect hit
                game.addScore(100, true);
                track.removeNote(closestNote);
            } else if (distance < 50) {
                // Good hit
                game.addScore(50, true);
                track.removeNote(closestNote);
            } else if (distance < 100) {
                // OK hit
                game.addScore(25, true);
                track.removeNote(closestNote);
            }
        }
    }
}