package games.match3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public final class GardenMatchApp {
    private static final Border NORMAL_BORDER = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
    private static final Border SELECTED_BORDER = BorderFactory.createLineBorder(Color.BLACK, 4);

    private GardenMatchApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GardenMatchApp::show);
    }

    private static void show() {
        GardenMatchGame[] game = { new GardenMatchGame(new Random()) };
        JFrame frame = new JFrame("GardenMatch");
        JPanel grid = new JPanel(new GridLayout(BoardController.SIZE, BoardController.SIZE, 2, 2));
        JButton[][] buttons = new JButton[BoardController.SIZE][BoardController.SIZE];
        JLabel status = new JLabel(statusText("Select adjacent tiles.", game[0]));
        Position[] selected = new Position[1];

        for (int row = 0; row < BoardController.SIZE; row++) {
            for (int column = 0; column < BoardController.SIZE; column++) {
                Position position = new Position(row, column);
                JButton button = new JButton();
                button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                button.setFocusPainted(false);
                button.setOpaque(true);
                button.setBorder(NORMAL_BORDER);
                button.addActionListener(event -> {
                    if (selected[0] == null) {
                        selected[0] = position;
                        refresh(buttons, game[0].board(), selected[0]);
                        status.setText(statusText("Selected " + position + ".", game[0]));
                        return;
                    }
                    boolean moved = game[0].swap(selected[0], position);
                    selected[0] = null;
                    refresh(buttons, game[0].board(), null);
                    status.setText(statusText(moved ? "Matched." : "No match.", game[0]));
                });
                buttons[row][column] = button;
                grid.add(button);
            }
        }

        JButton newGame = new JButton("New Game");
        newGame.addActionListener(event -> {
            game[0] = new GardenMatchGame(new Random());
            selected[0] = null;
            refresh(buttons, game[0].board(), null);
            status.setText(statusText("New garden.", game[0]));
        });

        JPanel top = new JPanel(new BorderLayout());
        top.add(status, BorderLayout.CENTER);
        top.add(newGame, BorderLayout.EAST);

        refresh(buttons, game[0].board(), null);
        frame.add(top, BorderLayout.NORTH);
        frame.add(grid, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 560);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void refresh(JButton[][] buttons, BoardController board, Position selected) {
        for (int row = 0; row < BoardController.SIZE; row++) {
            for (int column = 0; column < BoardController.SIZE; column++) {
                Tile tile = board.tileAt(new Position(row, column));
                buttons[row][column].setText(tile.name().substring(0, 1));
                buttons[row][column].setToolTipText(tile.name());
                buttons[row][column].setBackground(color(tile));
                buttons[row][column].setForeground(Color.BLACK);
                buttons[row][column].setBorder(new Position(row, column).equals(selected) ? SELECTED_BORDER : NORMAL_BORDER);
            }
        }
    }

    private static Color color(Tile tile) {
        return switch (tile) {
            case ROSE -> new Color(244, 142, 159);
            case TULIP -> new Color(255, 198, 98);
            case DAISY -> new Color(255, 242, 138);
            case LILY -> new Color(147, 209, 136);
            case IRIS -> new Color(156, 176, 238);
        };
    }

    private static String statusText(String message, GardenMatchGame game) {
        return message + " Tokens: " + game.tokens() + "  Garden: " + game.gardenLevel()
                + "  Next: " + game.tokensToNextLevel();
    }
}
