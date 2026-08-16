package chatmap.games.match3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

public final class BoardControllerTest {
    @Test
    void createsPlayableBoardWithoutImmediateMatches() {
        BoardController board = new BoardController(new Random(3));

        assertTrue(board.matches().isEmpty(), "new board has no immediate matches");
        assertFalse(board.swap(new Position(0, 0), new Position(2, 0)), "non-adjacent swap rejected");
        assertNotNull(board.tileAt(new Position(7, 7)), "last tile populated");
    }
}
