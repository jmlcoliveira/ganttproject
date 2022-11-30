package es.group.work.features.sliders;

import javax.swing.*;
import java.awt.*;

public class SimpleSlider implements Slider{

    private static final int N_DECIMAL_CASE = 2;
    private static final float MAX_PROGRESS = 100;
    private static final float MIN_PROGRESS = 0;
    private static final int M_FACTOR = (int) Math.pow(10.0,  N_DECIMAL_CASE);
    private static final int MAX_SLIDE_PROGRESS = (int) MAX_PROGRESS * M_FACTOR;
    private static final int MIN_SLIDE_PROGRESS = (int) MIN_PROGRESS * M_FACTOR;

    private static  final String FORMAT = String.format(" %%6.%df%%%%", N_DECIMAL_CASE);

    private float progress;
    private JProgressBar bar;
    private JLabel label;
    private JPanel container;

    public SimpleSlider() {
        this(0);
    }

    public SimpleSlider(float progress){
        container = new JPanel();
        bar = new JProgressBar();
        label = new JLabel();
        bar.setMaximum(MAX_SLIDE_PROGRESS);
        bar.setMinimum(MIN_SLIDE_PROGRESS);

        BoxLayout layout = new BoxLayout(container, BoxLayout.X_AXIS);
        container.setLayout(layout);

        container.add(bar);
       // container.add(Box.createHorizontalGlue());
        container.add(label);

        // think about the sizes later :)
        setProgress(progress); // this??
    }

    @Override
    public String text() {
        return label.getText();
    }


    @Override
    public float progress() {
        return progress;
    }

    @Override
    public void setProgress(float value) {
        float rounded_progress = round(value);
        float aux = Math.min(rounded_progress, MAX_PROGRESS);
        this.progress = Math.max(aux, MIN_PROGRESS);

        // %3.2f%%
        label.setText(this.formatProgress());
        this.set_slide_progress();
    }

    @Override
    public Component getComponent() {
        return container;
    }

    private String formatProgress(){
        return String.format(FORMAT, this.progress);
    }

    private void set_slide_progress(){
        int bar_value = (int) (progress * M_FACTOR);
        this.bar.setValue(bar_value);
    }

    private float round(float number){
        float factor = M_FACTOR;
        return Math.round(number * factor) / factor;
    }
}
