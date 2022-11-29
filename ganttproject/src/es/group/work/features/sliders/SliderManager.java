package es.group.work.features.sliders;

import java.awt.*;

public interface SliderManager {

    Slider newSlider(String name, float progress);
    Slider newSlider(String name);
    Slider getSlider(String name);
    void removeSlider(String name);

    Component getComponent();
}
