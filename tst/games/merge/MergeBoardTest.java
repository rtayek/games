package games.merge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public final class MergeBoardTest {
    @Test
    void mergesMatchingItems() {
        MergeBoard board = new MergeBoard(2, 2);
        board.place(new MergePosition(0, 0), new ItemNode("seed", 1));
        board.place(new MergePosition(0, 1), new ItemNode("seed", 1));

        assertTrue(board.drag(new MergePosition(0, 0), new MergePosition(0, 1)), "matching items merge");
        assertEquals(new ItemNode("seed", 2), board.itemAt(new MergePosition(0, 1)), "target level increments");
        assertNull(board.itemAt(new MergePosition(0, 0)), "source cell empties");
    }
}
