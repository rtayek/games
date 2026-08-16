package games.wordconnect;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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

        JFrame frame = new JFrame("WordSprout");
        JLabel grid = new JLabel(htmlBoard(board.render(game.found())));
        grid.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));

        JLabel letters = new JLabel("Letters: " + level.letters());
        JTextField entry = new JTextField(12);
        JButton submit = new JButton("Submit");
        JLabel status = new JLabel("Find all words.");

        submit.addActionListener(event -> {
            boolean accepted = game.submit(entry.getText());
            entry.setText("");
            grid.setText(htmlBoard(board.render(game.found())));
            status.setText(accepted ? "Accepted" : "Not in this puzzle");
            if (game.complete()) {
                status.setText("Level complete");
            }
        });

        JPanel controls = new JPanel();
        controls.add(letters);
        controls.add(entry);
        controls.add(submit);

        frame.add(grid, BorderLayout.CENTER);
        frame.add(controls, BorderLayout.NORTH);
        frame.add(status, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 360);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static String htmlBoard(String text) {
        return "<html><pre>" + text + "</pre></html>";
    }
}
