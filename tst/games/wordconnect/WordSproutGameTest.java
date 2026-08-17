package games.wordconnect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public final class WordSproutGameTest {
    @Test
    void selectsAndSubmitsWords() {
        WordLevel level = new WordLevel("sprout", List.of("SOP", "SORT"));
        WordSproutGame game = new WordSproutGame(level);
        LetterWheelController wheel = new LetterWheelController(level.letters());

        wheel.beginAt(0);
        wheel.dragTo(3);
        wheel.dragTo(1);

        assertEquals("SOP", wheel.selectedWord(), "wheel selection");
        assertTrue(game.submit("sop"), "valid word accepted");
        assertFalse(game.submit("soup"), "invalid word rejected");
        assertFalse(game.complete(), "level incomplete");
        assertTrue(game.submit("sort"), "second valid word accepted");
        assertTrue(game.complete(), "level complete");
    }

    @Test
    void rejectsAnswerWithUnavailableLetter() {
        assertThrows(IllegalArgumentException.class, () -> new WordLevel("sprout", List.of("SODA")));
    }

    @Test
    void rejectsAnswerWithTooManyCopiesOfLetter() {
        assertThrows(IllegalArgumentException.class, () -> new WordLevel("sprout", List.of("SOOT")));
    }
}
