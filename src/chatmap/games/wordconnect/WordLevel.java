package chatmap.games.wordconnect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public final class WordLevel {
    private final List<Character> letters;
    private final Set<String> answers;

    public WordLevel(String letters, Collection<String> answers) {
        if (letters == null || letters.isBlank()) {
            throw new IllegalArgumentException("letters must not be blank");
        }
        this.letters = normalizeLetters(letters);
        this.answers = normalizeAnswers(answers);
    }

    public List<Character> letters() {
        return List.copyOf(letters);
    }

    public Set<String> answers() {
        return Set.copyOf(answers);
    }

    public boolean accepts(String word) {
        return answers.contains(normalizeWord(word));
    }

    private static List<Character> normalizeLetters(String value) {
        ArrayList<Character> normalized = new ArrayList<>();
        for (char c : value.toUpperCase(Locale.ROOT).toCharArray()) {
            if (Character.isLetter(c)) {
                normalized.add(c);
            }
        }
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("letters must contain letters");
        }
        return List.copyOf(normalized);
    }

    private static Set<String> normalizeAnswers(Collection<String> values) {
        Objects.requireNonNull(values);
        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        for (String value : values) {
            String word = normalizeWord(value);
            if (!word.isBlank()) {
                normalized.add(word);
            }
        }
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("answers must not be empty");
        }
        return Set.copyOf(normalized);
    }

    static String normalizeWord(String word) {
        return word == null ? "" : word.trim().toUpperCase(Locale.ROOT);
    }
}
