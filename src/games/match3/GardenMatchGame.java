package games.match3;

import java.util.Random;

public final class GardenMatchGame {
    private final BoardController board;
    private int tokens;

    public GardenMatchGame(Random random) {
        board = new BoardController(random);
    }

    public boolean swap(Position first, Position second) {
        if (!board.swap(first, second)) {
            return false;
        }
        tokens += board.clearMatchesAndRefill();
        return true;
    }

    public int tokens() {
        return tokens;
    }

    public BoardController board() {
        return board;
    }
}
