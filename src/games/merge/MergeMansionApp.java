package games.merge;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public final class MergeMansionApp {
    private MergeMansionApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MergeMansionApp::show);
    }

    private static void show() {
        MergeBoard board = new MergeBoard(4, 4);
        board.place(new MergePosition(0, 0), new ItemNode("seed", 1));
        board.place(new MergePosition(0, 1), new ItemNode("seed", 1));

        JFrame frame = new JFrame("MergeMansion");
        JPanel grid = new JPanel(new GridLayout(4, 4, 2, 2));
        JButton[][] buttons = new JButton[4][4];
        JLabel status = new JLabel("Select an item, then a target cell.");
        MergePosition[] selected = new MergePosition[1];

        for (int row = 0; row < buttons.length; row++) {
            for (int column = 0; column < buttons[row].length; column++) {
                MergePosition position = new MergePosition(row, column);
                JButton button = new JButton();
                button.addActionListener(event -> {
                    if (selected[0] == null) {
                        selected[0] = position;
                        status.setText("Selected " + position);
                        return;
                    }
                    boolean moved = board.drag(selected[0], position);
                    selected[0] = null;
                    refresh(buttons, board);
                    status.setText(moved ? "Moved" : "Cannot merge");
                });
                buttons[row][column] = button;
                grid.add(button);
            }
        }

        refresh(buttons, board);
        frame.add(grid, BorderLayout.CENTER);
        frame.add(status, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 440);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void refresh(JButton[][] buttons, MergeBoard board) {
        for (int row = 0; row < buttons.length; row++) {
            for (int column = 0; column < buttons[row].length; column++) {
                ItemNode item = board.itemAt(new MergePosition(row, column));
                buttons[row][column].setText(item == null ? "" : item.family().substring(0, 1) + item.level());
            }
        }
    }
}
