package pro1.swingComponents;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import pro1.utils.ColorUtils;

public class OptionsPanel extends JPanel {

    private final MainFrame parent;
    private JSlider rSlider;
    private JSlider gSlider;
    private JSlider bSlider;

    public OptionsPanel(MainFrame parent) {
        this.parent = parent;
        this.setPreferredSize(new Dimension(200, 0));
        JButton newColorBtn = new JButton("Náhodná barva");
        this.add(newColorBtn);
        newColorBtn.addActionListener(
                (e) -> {
                    this.parent.setColor(ColorUtils.randomColor());
                    this.parent.showCircle();
                }
        );
        this.rSlider = new JSlider(0, 255, 0);
        this.gSlider = new JSlider(0, 255, 0);
        this.bSlider = new JSlider(0, 255, 0);
        this.add(this.rSlider);
        this.add(this.gSlider);
        this.add(this.bSlider);
        this.rSlider.addChangeListener((e) -> this.sliderChanged());
        this.gSlider.addChangeListener((e) -> this.sliderChanged());
        this.bSlider.addChangeListener((e) -> this.sliderChanged());

        // Součást úkolu 2
        JSpinner circleRadius = new JSpinner(new SpinnerNumberModel(25, 10, 100, 1));
        this.add(circleRadius);
        circleRadius.addChangeListener((e) -> {
            this.parent.setCircleRadius((Integer) circleRadius.getValue());
        });

        JCheckBox showCircles = new JCheckBox("Zobrazit kolečka");
        this.add(showCircles);
        showCircles.addActionListener((e) -> {
            this.parent.setShowCircles(showCircles.isSelected());
        });

        JButton resetBtn = new JButton("Reset");
        this.add(resetBtn);
        resetBtn.addActionListener((e) -> {
            this.parent.deleteCircles();
        });
        // Konec úkolu 2
    }

    private void sliderChanged() {
        this.parent.setColor(ColorUtils.color(
                this.rSlider.getValue(),
                this.gSlider.getValue(),
                this.bSlider.getValue()
        ));
        this.parent.showCircle();
    }
}
