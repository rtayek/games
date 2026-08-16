package chatmap.games.wordconnect;

import java.util.LinkedHashSet;
import java.util.Set;

public final class WordSproutGame {
    private final WordLevel level;
    private final LinkedHashSet<String> found = new LinkedHashSet<>();

    public WordSproutGame(WordLevel level) {
        this.level = level;
    }

    public boolean submit(String word) {
        String normalized = WordLevel.normalizeWord(word);
        if (!level.accepts(normalized)) {
            return false;
        }
        found.add(normalized);
        return true;
    }

    public boolean complete() {
        return found.containsAll(level.answers());
    }

    public Set<String> found() {
        return Set.copyOf(found);
    }

    public WordLevel level() {
        return level;
    }
}
