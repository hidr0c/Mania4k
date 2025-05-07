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

    public void handleKeyPress(int keyCode) {
        for (int i = 0; i < keyBindings.length; i++) {
            if (keyCode == keyBindings[i]) {
                game.getTracks()[i].setKeyPressed(true);
                checkNoteHit(i);
                break;
            }
        }
    }

    private void checkNoteHit(int trackIndex) {
        Track track = game.getTracks()[trackIndex];
        Note closestNote = track.getClosestNote();

        if (closestNote != null && !closestNote.isHit()) {
            double distance = Math.abs(closestNote.getYPosition() - 500); // 500 is hit position

            // Different scoring windows
            if (distance < 20) {
                // Perfect hit
                closestNote.setJudgment("perfect");
                game.addScore(100, true);
                track.removeNote(closestNote);
                game.getSoundManager().playSound("answer");

            } else if (distance < 50) {
                // Great hit
                closestNote.setJudgment("great");
                game.addScore(75, true);
                track.removeNote(closestNote);
                game.getSoundManager().playSound("answer");
            } else if (distance < 100) {
                // Good hit
                closestNote.setJudgment("good");
                game.addScore(50, true);
                track.removeNote(closestNote);
                game.getSoundManager().playSound("answer");
            } else if (distance < 150) {
                // OK hit
                closestNote.setJudgment("ok");
                game.addScore(25, true);
                track.removeNote(closestNote);
                game.getSoundManager().playSound("answer");
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
}