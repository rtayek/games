package games.match3;

import static games.match3.Tile.DAISY;
import static games.match3.Tile.IRIS;
import static games.match3.Tile.LILY;
import static games.match3.Tile.ROSE;
import static games.match3.Tile.TULIP;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertTrue(board.hasLegalMove(), "new board has at least one legal move");
        assertFalse(board.swap(new Position(0, 0), new Position(2, 0)), "non-adjacent swap rejected");
        assertNotNull(board.tileAt(new Position(7, 7)), "last tile populated");
    }

    @Test
    void adjacentSwapThatCreatesMatchStaysSwapped() {
        BoardController board = new BoardController(swapFixture(), new Random(1));

        assertTrue(board.swap(new Position(0, 0), new Position(0, 1)), "swap creates row match");

        assertEquals(TULIP, board.tileAt(new Position(0, 0)));
        assertEquals(ROSE, board.tileAt(new Position(0, 1)));
        assertTrue(board.matches().contains(new Position(0, 2)), "match remains visible before clearing");
    }

    @Test
    void adjacentSwapWithoutMatchReverts() {
        BoardController board = new BoardController(swapFixture(), new Random(1));

        assertFalse(board.swap(new Position(0, 0), new Position(1, 0)), "swap without match rejected");

        assertEquals(ROSE, board.tileAt(new Position(0, 0)));
        assertEquals(TULIP, board.tileAt(new Position(1, 0)));
    }

    @Test
    void clearMatchesClearsOnlyMatchedTiles() {
        BoardController board = new BoardController(matchedFixture(), new Random(1));

        assertEquals(3, board.clearMatches());

        assertEquals(null, board.tileAt(new Position(0, 0)));
        assertEquals(null, board.tileAt(new Position(0, 1)));
        assertEquals(null, board.tileAt(new Position(0, 2)));
        assertEquals(IRIS, board.tileAt(new Position(0, 3)));
    }

    @Test
    void refillLeavesNoEmptyCellsOrImmediateMatches() {
        BoardController board = new BoardController(matchedFixture(), new Random(2));

        assertEquals(3, board.clearMatchesAndRefill());

        for (int row = 0; row < BoardController.SIZE; row++) {
            for (int column = 0; column < BoardController.SIZE; column++) {
                assertNotNull(board.tileAt(new Position(row, column)), "cell should be refilled");
            }
        }
        assertTrue(board.matches().isEmpty(), "refill resolves immediate matches");
        assertTrue(board.hasLegalMove(), "refill leaves a legal move");
    }

    @Test
    void detectsDeadlockedBoard() {
        BoardController board = new BoardController(deadlockedFixture(), new Random(1));

        assertTrue(board.matches().isEmpty(), "fixture has no immediate matches");
        assertFalse(board.hasLegalMove(), "fixture has no legal swap");
    }

    @Test
    void recoversDeadlockedBoard() {
        BoardController board = new BoardController(deadlockedFixture(), new Random(1));

        board.ensurePlayable();

        assertTrue(board.matches().isEmpty(), "recovered board has no immediate matches");
        assertTrue(board.hasLegalMove(), "recovered board has a legal swap");
    }

    @Test
    void gameAwardsTokensAndGardenLevels() {
        GardenMatchGame game = new GardenMatchGame(new BoardController(swapFixture(), new Random(1)));

        assertEquals(0, game.moves());
        assertEquals(0, game.score());
        assertEquals(0, game.tokens());
        assertEquals(1, game.gardenLevel());
        assertEquals(10, game.tokensToNextLevel());
        assertTrue(game.swap(new Position(0, 0), new Position(0, 1)));
        assertEquals(1, game.moves());
        assertEquals(300, game.score());
        assertEquals(3, game.tokens());
        assertEquals(1, game.gardenLevel());
        assertEquals(7, game.tokensToNextLevel());
    }

    @Test
    void rejectedSwapDoesNotCountAsMove() {
        GardenMatchGame game = new GardenMatchGame(new BoardController(swapFixture(), new Random(1)));

        assertFalse(game.swap(new Position(0, 0), new Position(1, 0)));

        assertEquals(0, game.moves());
        assertEquals(0, game.score());
        assertEquals(0, game.tokens());
    }

    private static Tile[][] matchedFixture() {
        Tile[][] tiles = swapFixture();
        tiles[0][0] = ROSE;
        tiles[0][1] = ROSE;
        tiles[0][2] = ROSE;
        tiles[0][3] = IRIS;
        return tiles;
    }

    private static Tile[][] swapFixture() {
        return new Tile[][] {
                { ROSE, TULIP, ROSE, ROSE, LILY, IRIS, DAISY, TULIP },
                { TULIP, LILY, IRIS, DAISY, ROSE, TULIP, LILY, IRIS },
                { LILY, IRIS, DAISY, TULIP, LILY, IRIS, DAISY, ROSE },
                { IRIS, DAISY, TULIP, LILY, IRIS, DAISY, ROSE, TULIP },
                { DAISY, ROSE, LILY, IRIS, DAISY, ROSE, TULIP, LILY },
                { ROSE, TULIP, IRIS, DAISY, ROSE, TULIP, LILY, IRIS },
                { TULIP, LILY, DAISY, ROSE, TULIP, LILY, IRIS, DAISY },
                { LILY, IRIS, ROSE, TULIP, LILY, IRIS, DAISY, ROSE },
        };
    }

    private static Tile[][] deadlockedFixture() {
        Tile[] values = { ROSE, TULIP, DAISY, LILY, IRIS };
        Tile[][] tiles = new Tile[BoardController.SIZE][BoardController.SIZE];
        for (int row = 0; row < BoardController.SIZE; row++) {
            for (int column = 0; column < BoardController.SIZE; column++) {
                tiles[row][column] = values[(row + column) % values.length];
            }
        }
        return tiles;
    }
}
