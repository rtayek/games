package chatmap.games.merge;

public final class MergeMansionApp {
    private MergeMansionApp() {
    }

    public static void main(String[] args) {
        MergeBoard board = new MergeBoard(4, 4);
        board.place(new MergePosition(0, 0), new ItemNode("seed", 1));
        board.place(new MergePosition(0, 1), new ItemNode("seed", 1));
        board.drag(new MergePosition(0, 0), new MergePosition(0, 1));
        System.out.println(board.render());
    }
}
