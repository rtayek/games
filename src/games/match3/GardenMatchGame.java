package games.match3;

import java.util.Random;

public final class GardenMatchGame {
    private static final int TOKENS_PER_LEVEL = 10;

    private final BoardController board;
    private int tokens;

    public GardenMatchGame(Random random) {
        board = new BoardController(random);
    }

    GardenMatchGame(BoardController board) {
        this.board = board;
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

    public int gardenLevel() {
        return tokens / TOKENS_PER_LEVEL + 1;
    }

    public int tokensToNextLevel() {
        return TOKENS_PER_LEVEL - tokens % TOKENS_PER_LEVEL;
    }

    public BoardController board() {
        return board;
    }
}
