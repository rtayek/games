package chatmap.games.wordconnect;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public final class CrosswordBoardPane {
    private final TreeSet<String> answers;

    public CrosswordBoardPane(Collection<String> answers) {
        this.answers = new TreeSet<>();
        for (String answer : answers) {
            this.answers.add(WordLevel.normalizeWord(answer));
        }
    }

    public String render(Set<String> found) {
        TreeSet<String> normalizedFound = new TreeSet<>();
        for (String word : found) {
            normalizedFound.add(WordLevel.normalizeWord(word));
        }

        StringBuilder out = new StringBuilder();
        for (String answer : answers) {
            if (!out.isEmpty()) {
                out.append(System.lineSeparator());
            }
            out.append(normalizedFound.contains(answer) ? answer : "_".repeat(answer.length()));
        }
        return out.toString();
    }
}
