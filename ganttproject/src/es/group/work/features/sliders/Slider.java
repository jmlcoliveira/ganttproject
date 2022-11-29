package es.group.work.features.sliders;

import java.awt.*;

public interface Slider {
    String text();
    float progress();
    void setProgress(float value);

    Component getComponent();
}
