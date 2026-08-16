package chatmap.games.wordconnect;

import java.util.ArrayList;
import java.util.List;

public final class LetterWheelController {
    private final List<Character> letters;
    private final ArrayList<Integer> path = new ArrayList<>();

    public LetterWheelController(List<Character> letters) {
        if (letters == null || letters.isEmpty()) {
            throw new IllegalArgumentException("letters must not be empty");
        }
        this.letters = List.copyOf(letters);
    }

    public void beginAt(int index) {
        path.clear();
        append(index);
    }

    public void dragTo(int index) {
        append(index);
    }

    public String selectedWord() {
        StringBuilder word = new StringBuilder(path.size());
        for (int index : path) {
            word.append(letters.get(index));
        }
        return word.toString();
    }

    public void clear() {
        path.clear();
    }

    private void append(int index) {
        if (index < 0 || index >= letters.size()) {
            throw new IndexOutOfBoundsException(index);
        }
        if (!path.contains(index)) {
            path.add(index);
        }
    }
}
