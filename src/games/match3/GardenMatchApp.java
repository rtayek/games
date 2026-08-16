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
        JLabel message = new JLabel("Select adjacent tiles.");
        JLabel moves = new JLabel();
        JLabel score = new JLabel();
        JLabel tokens = new JLabel();
        JLabel garden = new JLabel();
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
                        message.setText("Selected " + position + ".");
                        return;
                    }
                    boolean moved = game[0].swap(selected[0], position);
                    selected[0] = null;
                    refresh(buttons, game[0].board(), null);
                    refreshScore(moves, score, tokens, garden, game[0]);
                    message.setText(moved ? "Matched." : "No match.");
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
            refreshScore(moves, score, tokens, garden, game[0]);
            message.setText("New garden.");
        });

        JPanel scorePanel = new JPanel(new GridLayout(1, 4, 12, 0));
        scorePanel.add(moves);
        scorePanel.add(score);
        scorePanel.add(tokens);
        scorePanel.add(garden);

        JPanel top = new JPanel(new BorderLayout());
        top.add(message, BorderLayout.NORTH);
        top.add(scorePanel, BorderLayout.CENTER);
        top.add(newGame, BorderLayout.EAST);

        refresh(buttons, game[0].board(), null);
        refreshScore(moves, score, tokens, garden, game[0]);
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

    private static void refreshScore(JLabel moves, JLabel score, JLabel tokens, JLabel garden, GardenMatchGame game) {
        moves.setText("Moves: " + game.moves());
        score.setText("Score: " + game.score());
        tokens.setText("Tokens: " + game.tokens());
        garden.setText("Garden: " + game.gardenLevel() + " Next: " + game.tokensToNextLevel());
    }
}
