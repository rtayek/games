package games.wordconnect;

import java.util.List;

public final class WordSproutApp {
    private WordSproutApp() {
    }

    public static void main(String[] args) {
        WordLevel level = new WordLevel("sprout", List.of("SOP", "SORT", "SPROUT", "ROUT"));
        WordSproutGame game = new WordSproutGame(level);
        game.submit(args.length == 0 ? "sop" : args[0]);
        System.out.println(new CrosswordBoardPane(level.answers()).render(game.found()));
    }
}
