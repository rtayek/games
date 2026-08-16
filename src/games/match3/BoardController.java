package games.match3;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public final class BoardController {
    public static final int SIZE = 8;

    private final Tile[][] tiles = new Tile[SIZE][SIZE];
    private final Random random;

    public BoardController(Random random) {
        this.random = Objects.requireNonNull(random);
        Tile[] values = Tile.values();
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                tiles[row][column] = nextNonMatchingTile(values, this.random, row, column);
            }
        }
    }

    BoardController(Tile[][] tiles, Random random) {
        this.random = Objects.requireNonNull(random);
        if (tiles.length != SIZE) {
            throw new IllegalArgumentException("board must have " + SIZE + " rows");
        }
        for (int row = 0; row < SIZE; row++) {
            if (tiles[row].length != SIZE) {
                throw new IllegalArgumentException("board must have " + SIZE + " columns");
            }
            for (int column = 0; column < SIZE; column++) {
                this.tiles[row][column] = tiles[row][column];
            }
        }
    }

    public Tile tileAt(Position position) {
        check(position);
        return tiles[position.row()][position.column()];
    }

    public boolean swap(Position first, Position second) {
        check(first);
        check(second);
        if (!adjacent(first, second)) {
            return false;
        }
        exchange(first, second);
        if (matches().isEmpty()) {
            exchange(first, second);
            return false;
        }
        return true;
    }

    public Set<Position> matches() {
        LinkedHashSet<Position> found = new LinkedHashSet<>();
        for (int row = 0; row < SIZE; row++) {
            int runStart = 0;
            for (int column = 1; column <= SIZE; column++) {
                if (column == SIZE || tiles[row][column] != tiles[row][runStart]) {
                    if (tiles[row][runStart] != null && column - runStart >= 3) {
                        addRow(found, row, runStart, column);
                    }
                    runStart = column;
                }
            }
        }
        for (int column = 0; column < SIZE; column++) {
            int runStart = 0;
            for (int row = 1; row <= SIZE; row++) {
                if (row == SIZE || tiles[row][column] != tiles[runStart][column]) {
                    if (tiles[runStart][column] != null && row - runStart >= 3) {
                        addColumn(found, column, runStart, row);
                    }
                    runStart = row;
                }
            }
        }
        return Set.copyOf(found);
    }

    public int clearMatches() {
        Set<Position> matched = matches();
        for (Position position : matched) {
            tiles[position.row()][position.column()] = null;
        }
        return matched.size();
    }

    public int clearMatchesAndRefill() {
        int total = 0;
        int cleared = clearMatches();
        while (cleared > 0) {
            total += cleared;
            collapseColumns();
            refill();
            cleared = clearMatches();
        }
        collapseColumns();
        refill();
        return total;
    }

    public String render() {
        StringBuilder out = new StringBuilder();
        for (int row = 0; row < SIZE; row++) {
            if (row > 0) {
                out.append(System.lineSeparator());
            }
            for (int column = 0; column < SIZE; column++) {
                out.append(tiles[row][column] == null ? '.' : tiles[row][column].name().charAt(0));
            }
        }
        return out.toString();
    }

    private Tile nextNonMatchingTile(Tile[] values, Random random, int row, int column) {
        ArrayDeque<Tile> options = new ArrayDeque<>();
        for (Tile value : values) {
            if (!createsImmediateMatch(value, row, column)) {
                options.add(value);
            }
        }
        int pick = random.nextInt(options.size());
        for (int i = 0; i < pick; i++) {
            options.addLast(options.removeFirst());
        }
        return options.removeFirst();
    }

    private boolean createsImmediateMatch(Tile tile, int row, int column) {
        boolean horizontal = column >= 2 && tiles[row][column - 1] == tile && tiles[row][column - 2] == tile;
        boolean vertical = row >= 2 && tiles[row - 1][column] == tile && tiles[row - 2][column] == tile;
        return horizontal || vertical;
    }

    private void collapseColumns() {
        for (int column = 0; column < SIZE; column++) {
            int writeRow = SIZE - 1;
            for (int row = SIZE - 1; row >= 0; row--) {
                if (tiles[row][column] != null) {
                    tiles[writeRow][column] = tiles[row][column];
                    if (writeRow != row) {
                        tiles[row][column] = null;
                    }
                    writeRow--;
                }
            }
        }
    }

    private void refill() {
        Tile[] values = Tile.values();
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (tiles[row][column] == null) {
                    tiles[row][column] = nextNonMatchingTile(values, random, row, column);
                }
            }
        }
    }

    private static boolean adjacent(Position first, Position second) {
        int distance = Math.abs(first.row() - second.row()) + Math.abs(first.column() - second.column());
        return distance == 1;
    }

    private void exchange(Position first, Position second) {
        Tile temp = tiles[first.row()][first.column()];
        tiles[first.row()][first.column()] = tiles[second.row()][second.column()];
        tiles[second.row()][second.column()] = temp;
    }

    private static void addRow(Set<Position> found, int row, int start, int end) {
        for (int column = start; column < end; column++) {
            found.add(new Position(row, column));
        }
    }

    private static void addColumn(Set<Position> found, int column, int start, int end) {
        for (int row = start; row < end; row++) {
            found.add(new Position(row, column));
        }
    }

    private static void check(Position position) {
        if (position.row() < 0 || position.row() >= SIZE || position.column() < 0 || position.column() >= SIZE) {
            throw new IndexOutOfBoundsException(position.toString());
        }
    }
}
