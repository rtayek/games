package games.match3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public final class GardenMatchApp {
    private GardenMatchApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GardenMatchApp::show);
    }

    private static void show() {
        GardenMatchGame game = new GardenMatchGame(new Random(7));
        JFrame frame = new JFrame("GardenMatch");
        JPanel grid = new JPanel(new GridLayout(BoardController.SIZE, BoardController.SIZE, 2, 2));
        JButton[][] buttons = new JButton[BoardController.SIZE][BoardController.SIZE];
        JLabel status = new JLabel("Select adjacent tiles. Tokens: 0");
        Position[] selected = new Position[1];

        for (int row = 0; row < BoardController.SIZE; row++) {
            for (int column = 0; column < BoardController.SIZE; column++) {
                Position position = new Position(row, column);
                JButton button = new JButton();
                button.addActionListener(event -> {
                    if (selected[0] == null) {
                        selected[0] = position;
                        status.setText("Selected " + position + ". Tokens: " + game.tokens());
                        return;
                    }
                    boolean moved = game.swap(selected[0], position);
                    selected[0] = null;
                    refresh(buttons, game.board());
                    status.setText((moved ? "Matched" : "No match") + ". Tokens: " + game.tokens());
                });
                buttons[row][column] = button;
                grid.add(button);
            }
        }

        refresh(buttons, game.board());
        frame.add(grid, BorderLayout.CENTER);
        frame.add(status, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 560);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void refresh(JButton[][] buttons, BoardController board) {
        for (int row = 0; row < BoardController.SIZE; row++) {
            for (int column = 0; column < BoardController.SIZE; column++) {
                Tile tile = board.tileAt(new Position(row, column));
                buttons[row][column].setText(tile.name().substring(0, 1));
                buttons[row][column].setToolTipText(tile.name());
            }
        }
    }
}
