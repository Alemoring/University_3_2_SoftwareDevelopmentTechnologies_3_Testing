package GUI;

import figures.Figure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends Window {
    public MainWindow(String title) {
        super(title);

        int paneWidth = 1000;
        int paneHeight = 900;
        int scaleFactor = 20;
        int mainWidth = 1200;
        int mainHeight = 1000;

        setPreferredSize(new Dimension(mainWidth, mainHeight));
        setLayout(new BorderLayout());
        setLocation(765 - (mainWidth / 2), 0);

        Figure figure = new Figure(scaleFactor, paneWidth);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JPanel  panelDraw = new JPanel();
        panelDraw.setBorder(BorderFactory.createTitledBorder("Рисование"));
        panelDraw.setLayout(new BoxLayout(panelDraw, BoxLayout.PAGE_AXIS));

        JPanel  panelCalculation = new JPanel();
        panelCalculation.setBorder(BorderFactory.createTitledBorder("Расчёт"));
        panelCalculation.setLayout(new BoxLayout(panelCalculation, BoxLayout.PAGE_AXIS));

        JPanel  panelScale = new JPanel();
        panelScale.setBorder(BorderFactory.createTitledBorder("Масштабирование"));
        panelScale.setLayout(new BoxLayout(panelScale, BoxLayout.PAGE_AXIS));

        JButton drawButton = new JButton("Нарисовать фигуру");
        drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton calculateButton = new JButton("Посчитать площадь");
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton sizeIncreaseButton = new JButton("Увеличить фигуру");
        sizeIncreaseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton sizeDecreaseButton = new JButton("Уменьшить фигуру");
        sizeDecreaseButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField radiusTextField = new JTextField();
        radiusTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        radiusTextField.setMaximumSize(new Dimension(150, 50));

        JLabel radiusLabel = new JLabel("Радиус в см");
        radiusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelDraw.add(radiusLabel);
        panelDraw.add(radiusTextField);
        panelDraw.add(drawButton);

        panelCalculation.add(calculateButton);

        panelScale.add(sizeIncreaseButton);
        panelScale.add(sizeDecreaseButton);

        inputPanel.add(panelDraw);
        inputPanel.add(panelCalculation);
        inputPanel.add(panelScale);

        JPanel drawingPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                int width = this.getWidth();
                int height = this.getHeight();

                int centerX = width / 2;
                int centerY = height / 2;

                g.setColor(Color.WHITE);

                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.BLUE);

                g.fillOval(centerX - figure.getRadius() / 2, centerY - figure.getRadius() / 2, figure.getRadius(), figure.getRadius());

                g.setColor(Color.BLACK);
                g.drawOval(centerX - figure.getRadius() / 2, centerY - figure.getRadius() / 2, figure.getRadius(), figure.getRadius());

                g.setColor(Color.WHITE);
                int sides = 5;
                double angleOffset = Math.PI / 2;  // Поворачиваем на 90° вверх

                int[] xPoints = new int[sides];
                int[] yPoints = new int[sides];

                for (int i = 0; i < sides; i++) {
                    double angle = angleOffset + (2 * Math.PI * i) / sides;
                    xPoints[i] = (int) (centerX + ((double) figure.getRadius() / 2) * Math.cos(angle));
                    yPoints[i] = (int) (centerY + ((double) figure.getRadius() / 2) * Math.sin(angle));
                }

                g.fillPolygon(xPoints, yPoints, sides);

                g.setColor(Color.BLACK);

                g.drawPolygon(xPoints, yPoints, sides);

                int innerRadius = figure.getRadius() / 2; // Радиус внутренних вершин (~38.2%)
                int outerRadius =  (int) (innerRadius / 0.382); // Радиус внешних вершин
                angleOffset = -Math.PI / 2;
                xPoints = new int[10];
                yPoints = new int[10];

                for (int i = 0; i < 5; i++) {
                    // Внешняя вершина
                    double outerAngle = angleOffset + (2 * Math.PI * i) / 5;
                    xPoints[2 * i] = (int) (centerX + outerRadius * Math.cos(outerAngle));
                    yPoints[2 * i] = (int) (centerY + outerRadius * Math.sin(outerAngle));

                    // Внутренняя вершина (смещение на 36°)
                    double innerAngle = angleOffset + (2 * Math.PI * i) / 5 + Math.PI / 5;
                    xPoints[2 * i + 1] = (int) (centerX + innerRadius * Math.cos(innerAngle));
                    yPoints[2 * i + 1] = (int) (centerY + innerRadius * Math.sin(innerAngle));
                }

                g.drawPolygon(xPoints, yPoints, 10);
            }
        };

        drawingPanel.setPreferredSize(new Dimension(paneWidth, paneHeight));
        drawingPanel.setMaximumSize(new Dimension(paneWidth, paneHeight));
        drawingPanel.setMinimumSize(new Dimension(50, 50));

        drawButton.addActionListener(_ -> {
            try {
                figure.setNewRates(radiusTextField.getText());

                drawingPanel.repaint();
            } catch (NullPointerException exception) {
                JOptionPane.showMessageDialog(drawingPanel, """
                        Неверный формат радиуса!
                        Он должен быть ненулевым положительным целым числом!""", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(drawingPanel, """
                        Слишком большое значение радиуса!
                        Пожалуйста, уменьшите радиус, чтобы фигура вошла в окно.
                        Максимальный радиус\s""" + (int)((double) paneWidth / scaleFactor / 2.5) + "см", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        calculateButton.addActionListener(_ -> {
            try {
                double area = figure.calculateShadedArea();

                JOptionPane.showMessageDialog(drawingPanel, "Площадь закрашенных участков равна " + area + "см²", "Площадь", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(drawingPanel, """
                        Фигура ещё не построена!
                        Для начала постройте фигуру!""", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        sizeIncreaseButton.addActionListener(_ -> {
            try {
                figure.changeSize(1.2);

                drawingPanel.repaint();

                radiusTextField.setText(String.valueOf(figure.getSmRadius()));
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(drawingPanel, """
                        Фигура ещё не построена!
                        Для начала постройте фигуру!""", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(drawingPanel, """
                        Больше нельзя увеличить!""", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        sizeDecreaseButton.addActionListener(_ -> {
            try {
                figure.changeSize(0.8);

                drawingPanel.repaint();

                radiusTextField.setText(String.valueOf(figure.getSmRadius()));
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(drawingPanel, """
                        Фигура ещё не построена!
                        Для начала постройте фигуру!""", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(drawingPanel, """
                        Больше нельзя уменьшить!""", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }

            drawingPanel.repaint();
        });

        MouseAdapter mouseAdapter = new MouseAdapter() {
            private Point initialPoint = null;
            private Dimension initialSize = null;

            @Override
            public void mousePressed(MouseEvent e) {
                initialPoint = e.getPoint();
                initialSize = drawingPanel.getSize();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (initialPoint != null) {
                    int newWidth = initialSize.width + (e.getX() - initialPoint.x);
                    int newHeight = initialSize.height + (e.getY() - initialPoint.y);

                    drawingPanel.setPreferredSize(new Dimension(Math.min(Math.max(newWidth, 50), paneWidth), Math.min(Math.max(newHeight, 50), paneHeight)));

                    figure.changeScale(
                            (double) drawingPanel.getWidth() / paneWidth
                    );

                    drawingPanel.revalidate();
                    drawingPanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                initialPoint = null;
                initialSize = null;
            }
        };

        drawingPanel.addMouseListener(mouseAdapter);
        drawingPanel.addMouseMotionListener(mouseAdapter);

        JPanel  panelForDrawingPanel = new JPanel();
        panelForDrawingPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        panelForDrawingPanel.add(drawingPanel);

        add(panelForDrawingPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.WEST);

        pack();
    }
}
