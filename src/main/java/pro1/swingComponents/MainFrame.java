package pro1.swingComponents;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pro1.drawingModel.Drawable;
import pro1.drawingModel.Ellipse;
import pro1.drawingModel.Group;
import pro1.drawingModel.Line;

public class MainFrame extends JFrame {

    private final DisplayPanel displayPanel;
    private int x;
    private int y;
    private String color = "#000000";
    private ArrayList<Drawable> drawables = new ArrayList<>();
    private ArrayList<int[]> circleCenters = new ArrayList<>();
    private static int CIRCLE_RADIUS = 25;
    private static int LINE_THICKNESS = 2;
    private boolean showCircles = true;

    // Tohle zůstává 
    public void setColor(String color) {
        this.color = color;
    }

    public MainFrame() {
        this.setTitle("PRO1 Drawing");
        this.setVisible(true);
        this.setSize(800, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.displayPanel = new DisplayPanel();
        this.add(this.displayPanel, BorderLayout.CENTER);

        JPanel leftPanel = new OptionsPanel(this);
        this.add(leftPanel, BorderLayout.WEST);

        this.displayPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MainFrame.this.x = e.getX();
                MainFrame.this.y = e.getY();
                MainFrame.this.showCircle();
            }
        });
    }

    // Kolečko
    private Drawable circle() {
        circleCenters.add(new int[]{this.x, this.y});
        return buildDisplay();
    }

    private Drawable buildDisplay() {
        this.drawables.clear();

        // Jestli se mají zobrazovat kolečka, přidáme je do drawables
        if (showCircles) {
            for (int[] center : circleCenters) {
                Ellipse circle = new Ellipse(center[0] - CIRCLE_RADIUS, center[1] - CIRCLE_RADIUS,
                        CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2, this.color);
                drawables.add(circle);
            }
        }

        // Jestli se mají zobrazovat úsečky, přidáme je do drawables
        if (showCircles && circleCenters.size() > 1) {
            for (int i = 1; i < circleCenters.size(); i++) {
                int[] currentCenter = circleCenters.get(i);
                int closestIndex = findClosestCircle(currentCenter, i);
                int[] closestCenter = circleCenters.get(closestIndex);

                Line connectingLine = new Line(currentCenter[0], currentCenter[1],
                        closestCenter[0], closestCenter[1],
                        LINE_THICKNESS, this.color);
                drawables.add(connectingLine);
            }
        }

        // Pokud není nic k zobrazení, vrátíme prázdnou skupinu
        if (drawables.isEmpty()) {
            return new Group(new Drawable[0], 0, 0, 0, 1, 1);
        }

        Drawable[] drawablesArray = drawables.toArray(new Drawable[0]);
        return new Group(drawablesArray, 0, 0, 0, 1, 1);
    }

    // Stejný jako originál showExample
    public void showCircle() {
        MainFrame.this.displayPanel.setDrawable(this.circle());
    }

    // Vymažu uložiště koleček a aktualizuji zobrazení
    public void deleteCircles() {
        this.circleCenters.clear();
        this.displayPanel.setDrawable(null);
    }

    // Změna poloměru a překreslení
    public void setCircleRadius(int radius) {
        this.CIRCLE_RADIUS = radius;
        this.refreshDisplay();
    }

    // Změna zobrazení a překreslení
    public void setShowCircles(boolean showCircles) {
        this.showCircles = showCircles;
        this.refreshDisplay();
    }

    // Překreslení
    private void refreshDisplay() {
        if (circleCenters.isEmpty()) {
            this.displayPanel.setDrawable(null);
        } else {
            this.displayPanel.setDrawable(this.buildDisplay());
        }
    }

    // Najde index nejbližšího kolečka k novému kolečku, vyloučí kolečka s indexem >= excludeIndex
    private int findClosestCircle(int[] newCenter, int excludeIndex) {
        int closestIndex = -1;
        double closestDistance = Double.MAX_VALUE;

        // Matika kterou jsem našel na internetu
        for (int i = 0; i < excludeIndex; i++) {
            int[] center = circleCenters.get(i);
            double distance = Math.sqrt(Math.pow(newCenter[0] - center[0], 2)
                    + Math.pow(newCenter[1] - center[1], 2));

            if (distance < closestDistance) {
                closestDistance = distance;
                closestIndex = i;
            }
        }

        return closestIndex;
    }
}
