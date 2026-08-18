package games.wordconnect;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JPanel;

public final class LetterWheelPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int RADIUS = 80;
    private static final int LETTER_RADIUS = 20;

    private final List<Character> letters;
    private final LetterWheelController controller;
    private final Consumer<String> onSubmit;

    private final List<Point> letterPositions = new ArrayList<>();
    private final List<Integer> currentPath = new ArrayList<>();
    private Point currentMousePosition;

    public LetterWheelPanel(List<Character> letters, LetterWheelController controller, Consumer<String> onSubmit) {
        this.letters = List.copyOf(letters);
        this.controller = controller;
        this.onSubmit = onSubmit;
        setPreferredSize(new Dimension(250, 250));

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = getHoveredIndex(e.getPoint());
                if (index != -1) {
                    LetterWheelPanel.this.controller.beginAt(index);
                    currentPath.clear();
                    currentPath.add(index);
                    currentMousePosition = e.getPoint();
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!currentPath.isEmpty()) {
                    currentMousePosition = e.getPoint();
                    int index = getHoveredIndex(e.getPoint());
                    if (index != -1 && !currentPath.contains(index)) {
                        LetterWheelPanel.this.controller.dragTo(index);
                        currentPath.add(index);
                    }
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!currentPath.isEmpty()) {
                    String word = LetterWheelPanel.this.controller.selectedWord();
                    LetterWheelPanel.this.onSubmit.accept(word);
                    LetterWheelPanel.this.controller.clear();
                    currentPath.clear();
                    currentMousePosition = null;
                    repaint();
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private int getHoveredIndex(Point p) {
        for (int i = 0; i < letterPositions.size(); i++) {
            if (letterPositions.get(i).distance(p) <= LETTER_RADIUS) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        letterPositions.clear();
        double angleStep = 2 * Math.PI / letters.size();
        for (int i = 0; i < letters.size(); i++) {
            double angle = i * angleStep - Math.PI / 2;
            int x = centerX + (int) (RADIUS * Math.cos(angle));
            int y = centerY + (int) (RADIUS * Math.sin(angle));
            letterPositions.add(new Point(x, y));
        }

        if (!currentPath.isEmpty()) {
            g2.setColor(new Color(100, 200, 100));
            g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < currentPath.size() - 1; i++) {
                Point p1 = letterPositions.get(currentPath.get(i));
                Point p2 = letterPositions.get(currentPath.get(i + 1));
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            if (currentMousePosition != null) {
                Point lastP = letterPositions.get(currentPath.get(currentPath.size() - 1));
                g2.drawLine(lastP.x, lastP.y, currentMousePosition.x, currentMousePosition.y);
            }
        }

        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();
        for (int i = 0; i < letters.size(); i++) {
            Point p = letterPositions.get(i);

            if (currentPath.contains(i)) {
                g2.setColor(new Color(150, 220, 150));
            } else {
                g2.setColor(Color.LIGHT_GRAY);
            }
            g2.fillOval(p.x - LETTER_RADIUS, p.y - LETTER_RADIUS, LETTER_RADIUS * 2, LETTER_RADIUS * 2);

            g2.setColor(Color.BLACK);
            String letter = letters.get(i).toString();
            int strX = p.x - fm.stringWidth(letter) / 2;
            int strY = p.y + fm.getAscent() / 2 - 2;
            g2.drawString(letter, strX, strY);
        }
    }
}
