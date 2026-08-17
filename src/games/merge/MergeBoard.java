package games.merge;

import java.util.Objects;

public final class MergeBoard {
    private final ItemNode[][] items;

    public MergeBoard(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new IllegalArgumentException("board must not be empty");
        }
        items = new ItemNode[rows][columns];
    }

    public void place(MergePosition position, ItemNode item) {
        check(position);
        if (items[position.row()][position.column()] != null) {
            throw new IllegalStateException("cell is occupied");
        }
        items[position.row()][position.column()] = Objects.requireNonNull(item);
    }

    public boolean drag(MergePosition from, MergePosition to) {
        check(from);
        check(to);
        if (from.equals(to)) {
            return false;
        }
        ItemNode source = items[from.row()][from.column()];
        ItemNode target = items[to.row()][to.column()];
        if (source == null) {
            return false;
        }
        if (target == null) {
            items[to.row()][to.column()] = source;
            items[from.row()][from.column()] = null;
            return true;
        }
        if (!source.canMerge(target)) {
            return false;
        }
        items[to.row()][to.column()] = target.merge(source);
        items[from.row()][from.column()] = null;
        return true;
    }

    public ItemNode itemAt(MergePosition position) {
        check(position);
        return items[position.row()][position.column()];
    }

    public MergePosition firstEmptyCell() {
        for (int row = 0; row < items.length; row++) {
            for (int column = 0; column < items[row].length; column++) {
                if (items[row][column] == null) {
                    return new MergePosition(row, column);
                }
            }
        }
        return null;
    }

    public int rows() {
        return items.length;
    }

    public int columns() {
        return items[0].length;
    }

    public String render() {
        StringBuilder out = new StringBuilder();
        for (int row = 0; row < items.length; row++) {
            if (row > 0) {
                out.append(System.lineSeparator());
            }
            for (int column = 0; column < items[row].length; column++) {
                ItemNode item = items[row][column];
                out.append(item == null ? "." : item.family().charAt(0) + Integer.toString(item.level()));
                if (column + 1 < items[row].length) {
                    out.append(' ');
                }
            }
        }
        return out.toString();
    }

    private void check(MergePosition position) {
        if (position.row() < 0 || position.row() >= items.length || position.column() < 0
                || position.column() >= items[0].length) {
            throw new IndexOutOfBoundsException(position.toString());
        }
    }
}
