package games.wordconnect;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public final class WordSproutApp {
    private WordSproutApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WordSproutApp::show);
    }

    private static void show() {
        WordLevel level = new WordLevel("sprout", List.of("SOP", "SORT", "SPROUT", "ROUT"));
        WordSproutGame game = new WordSproutGame(level);
        CrosswordBoardPane board = new CrosswordBoardPane(level.answers());
        LetterWheelController controller = new LetterWheelController(level.letters());

        JFrame frame = new JFrame("WordSprout");
        JLabel grid = new JLabel(htmlBoard(board.render(game.found())));
        grid.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));

        JLabel status = new JLabel("Find all words.");

        LetterWheelPanel wheel = new LetterWheelPanel(level.letters(), controller, word -> {
            boolean accepted = game.submit(word);
            grid.setText(htmlBoard(board.render(game.found())));
            status.setText(accepted ? "Accepted" : "Not in this puzzle");
            if (game.complete()) {
                status.setText("Level complete");
            }
        });

        frame.add(grid, BorderLayout.NORTH);
        frame.add(wheel, BorderLayout.CENTER);
        frame.add(status, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 520);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static String htmlBoard(String text) {
        return "<html><pre>" + text + "</pre></html>";
    }
}
