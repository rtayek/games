package games.merge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public final class MergeBoardTest {
    @Test
    void movesItemToEmptyCell() {
        MergeBoard board = new MergeBoard(2, 2);
        board.place(new MergePosition(0, 0), new ItemNode("seed", 1));

        assertTrue(board.drag(new MergePosition(0, 0), new MergePosition(1, 1)));

        assertNull(board.itemAt(new MergePosition(0, 0)));
        assertEquals(new ItemNode("seed", 1), board.itemAt(new MergePosition(1, 1)));
    }

    @Test
    void mergesMatchingItems() {
        MergeBoard board = new MergeBoard(2, 2);
        board.place(new MergePosition(0, 0), new ItemNode("seed", 1));
        board.place(new MergePosition(0, 1), new ItemNode("seed", 1));

        assertTrue(board.drag(new MergePosition(0, 0), new MergePosition(0, 1)), "matching items merge");
        assertEquals(new ItemNode("seed", 2), board.itemAt(new MergePosition(0, 1)), "target level increments");
        assertNull(board.itemAt(new MergePosition(0, 0)), "source cell empties");
    }

    @Test
    void rejectsMismatchedMerge() {
        MergeBoard board = new MergeBoard(2, 2);
        board.place(new MergePosition(0, 0), new ItemNode("seed", 1));
        board.place(new MergePosition(0, 1), new ItemNode("seed", 2));

        assertFalse(board.drag(new MergePosition(0, 0), new MergePosition(0, 1)));

        assertEquals(new ItemNode("seed", 1), board.itemAt(new MergePosition(0, 0)));
        assertEquals(new ItemNode("seed", 2), board.itemAt(new MergePosition(0, 1)));
    }

    @Test
    void rejectsEmptySource() {
        MergeBoard board = new MergeBoard(2, 2);
        board.place(new MergePosition(0, 1), new ItemNode("seed", 1));

        assertFalse(board.drag(new MergePosition(0, 0), new MergePosition(0, 1)));

        assertEquals(new ItemNode("seed", 1), board.itemAt(new MergePosition(0, 1)));
    }

    @Test
    void gameSpawnsIntoFirstEmptyCell() {
        MergeMansionGame game = new MergeMansionGame(2, 2);

        assertTrue(game.spawn());

        assertEquals(new ItemNode("seed", 1), game.board().itemAt(new MergePosition(0, 0)));
        assertEquals(1, game.spawned());
        assertEquals(0, game.moves());
        assertEquals(0, game.score());
    }

    @Test
    void gameTracksMoveAndMergeScore() {
        MergeBoard board = new MergeBoard(2, 2);
        board.place(new MergePosition(0, 0), new ItemNode("seed", 1));
        board.place(new MergePosition(0, 1), new ItemNode("seed", 1));
        MergeMansionGame game = new MergeMansionGame(board);

        assertTrue(game.drag(new MergePosition(0, 0), new MergePosition(0, 1)));

        assertEquals(1, game.moves());
        assertEquals(200, game.score());
        assertEquals(new ItemNode("seed", 2), game.board().itemAt(new MergePosition(0, 1)));
    }

    @Test
    void rejectedGameMoveDoesNotChangeCounters() {
        MergeBoard board = new MergeBoard(2, 2);
        board.place(new MergePosition(0, 0), new ItemNode("seed", 1));
        board.place(new MergePosition(0, 1), new ItemNode("seed", 2));
        MergeMansionGame game = new MergeMansionGame(board);

        assertFalse(game.drag(new MergePosition(0, 0), new MergePosition(0, 1)));

        assertEquals(0, game.moves());
        assertEquals(0, game.score());
    }

    @Test
    void sameCellGameDragIsRejectedWithoutMutation() {
        MergeBoard board = new MergeBoard(2, 2);
        board.place(new MergePosition(0, 0), new ItemNode("seed", 1));
        MergeMansionGame game = new MergeMansionGame(board);

        assertFalse(game.drag(new MergePosition(0, 0), new MergePosition(0, 0)));

        assertEquals(new ItemNode("seed", 1), game.board().itemAt(new MergePosition(0, 0)));
        assertEquals(0, game.moves());
        assertEquals(0, game.score());
    }
}
