package games.merge;

public final class MergeMansionGame {
    private final MergeBoard board;
    private int moves;
    private int spawned;
    private int score;

    public MergeMansionGame(int rows, int columns) {
        board = new MergeBoard(rows, columns);
    }

    MergeMansionGame(MergeBoard board) {
        this.board = board;
    }

    public boolean spawn() {
        MergePosition empty = board.firstEmptyCell();
        if (empty == null) {
            return false;
        }
        board.place(empty, new ItemNode("seed", 1));
        spawned++;
        return true;
    }

    public boolean drag(MergePosition from, MergePosition to) {
        ItemNode source = board.itemAt(from);
        ItemNode target = board.itemAt(to);
        boolean merged = source != null && source.canMerge(target);
        if (!board.drag(from, to)) {
            return false;
        }
        moves++;
        if (merged) {
            score += board.itemAt(to).level() * 100;
        }
        return true;
    }

    public int moves() {
        return moves;
    }

    public int spawned() {
        return spawned;
    }

    public int score() {
        return score;
    }

    public MergeBoard board() {
        return board;
    }
}
