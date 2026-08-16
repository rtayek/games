package games.merge;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public final class MergeMansionApp {
    private static final int ROWS = 4;
    private static final int COLUMNS = 4;
    private static final Border NORMAL_BORDER = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
    private static final Border SELECTED_BORDER = BorderFactory.createLineBorder(Color.BLACK, 4);

    private MergeMansionApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MergeMansionApp::show);
    }

    private static void show() {
        MergeMansionGame[] game = { newGame() };

        JFrame frame = new JFrame("MergeMansion");
        JPanel grid = new JPanel(new GridLayout(ROWS, COLUMNS, 2, 2));
        JButton[][] buttons = new JButton[ROWS][COLUMNS];
        JLabel message = new JLabel("Select an item, then a target cell.");
        JLabel moves = new JLabel();
        JLabel score = new JLabel();
        JLabel spawned = new JLabel();
        MergePosition[] selected = new MergePosition[1];

        for (int row = 0; row < buttons.length; row++) {
            for (int column = 0; column < buttons[row].length; column++) {
                MergePosition position = new MergePosition(row, column);
                JButton button = new JButton();
                button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                button.setFocusPainted(false);
                button.setOpaque(true);
                button.setBorder(NORMAL_BORDER);
                button.addActionListener(event -> {
                    if (selected[0] == null) {
                        selected[0] = position;
                        refresh(buttons, game[0].board(), selected[0]);
                        message.setText("Selected " + position);
                        return;
                    }
                    boolean moved = game[0].drag(selected[0], position);
                    selected[0] = null;
                    refresh(buttons, game[0].board(), null);
                    refreshScore(moves, score, spawned, game[0]);
                    message.setText(moved ? "Moved" : "Cannot merge");
                });
                buttons[row][column] = button;
                grid.add(button);
            }
        }

        JButton spawn = new JButton("Spawn");
        spawn.addActionListener(event -> {
            boolean placed = game[0].spawn();
            refresh(buttons, game[0].board(), selected[0]);
            refreshScore(moves, score, spawned, game[0]);
            message.setText(placed ? "Spawned seed." : "Board is full.");
        });

        JButton reset = new JButton("New Game");
        reset.addActionListener(event -> {
            game[0] = newGame();
            selected[0] = null;
            refresh(buttons, game[0].board(), null);
            refreshScore(moves, score, spawned, game[0]);
            message.setText("New mansion.");
        });

        JPanel actions = new JPanel(new GridLayout(1, 2, 6, 0));
        actions.add(spawn);
        actions.add(reset);

        JPanel scorePanel = new JPanel(new GridLayout(1, 3, 12, 0));
        scorePanel.add(moves);
        scorePanel.add(score);
        scorePanel.add(spawned);

        JPanel top = new JPanel(new BorderLayout());
        top.add(message, BorderLayout.NORTH);
        top.add(scorePanel, BorderLayout.CENTER);
        top.add(actions, BorderLayout.EAST);

        refresh(buttons, game[0].board(), null);
        refreshScore(moves, score, spawned, game[0]);
        frame.add(top, BorderLayout.NORTH);
        frame.add(grid, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 520);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static MergeMansionGame newGame() {
        MergeMansionGame game = new MergeMansionGame(ROWS, COLUMNS);
        game.spawn();
        game.spawn();
        return game;
    }

    private static void refresh(JButton[][] buttons, MergeBoard board, MergePosition selected) {
        for (int row = 0; row < buttons.length; row++) {
            for (int column = 0; column < buttons[row].length; column++) {
                MergePosition position = new MergePosition(row, column);
                ItemNode item = board.itemAt(position);
                buttons[row][column].setText(label(item));
                buttons[row][column].setToolTipText(item == null ? "Empty" : item.family() + " level " + item.level());
                buttons[row][column].setBackground(color(item));
                buttons[row][column].setForeground(Color.BLACK);
                buttons[row][column].setBorder(position.equals(selected) ? SELECTED_BORDER : NORMAL_BORDER);
            }
        }
    }

    private static String label(ItemNode item) {
        return item == null ? "" : item.family().substring(0, 1).toUpperCase() + item.level();
    }

    private static Color color(ItemNode item) {
        if (item == null) {
            return new Color(238, 238, 238);
        }
        return switch (Math.min(item.level(), 5)) {
            case 1 -> new Color(184, 224, 150);
            case 2 -> new Color(124, 203, 139);
            case 3 -> new Color(110, 188, 196);
            case 4 -> new Color(136, 161, 222);
            default -> new Color(201, 151, 221);
        };
    }

    private static void refreshScore(JLabel moves, JLabel score, JLabel spawned, MergeMansionGame game) {
        moves.setText("Moves: " + game.moves());
        score.setText("Score: " + game.score());
        spawned.setText("Spawned: " + game.spawned());
    }
}
