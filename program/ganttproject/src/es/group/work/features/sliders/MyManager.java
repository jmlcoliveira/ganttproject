package es.group.work.features.sliders;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public class MyManager implements  SliderManager{

    // <title> <barra>
    // <title> <anything>
    // <title> /// slider
    // total:  numero
    private  static final Font LABEL_FONT = new Font("Courier", Font.BOLD,12);
    private JPanel board;
    private Map<String, Map.Entry<Component, Slider>> sliders;

    private static class MyEntry implements Map.Entry<Component, Slider>{

        private final Component comp;
        private final Slider slider;
        public MyEntry(Component comp, Slider slider){
            this.slider = slider;
            this.comp = comp;
        }

        @Override
        public Component getKey() {
            return comp;
        }

        @Override
        public Slider getValue() {
            return slider;
        }

        @Override
        public Slider setValue(Slider value) {
            return null; // does nothing :)
        }
    }
    public MyManager(){
        board = new JPanel();
        //board.setLayout(new BoxLayout(board, BoxLayout.Y_AXIS));
        // problems to solve : (
        //GridLayout layout = new GridLayout(0, 2, 1, 1);
        GridLayout layout = new GridLayout(0, 2, 1, 1);
        board.setLayout(layout);
        sliders = new TreeMap<>();
    }

    @Override
    public Slider newSlider(String name, float progress) {
        return newSlider(name, new SimpleSlider(progress));
    }

    @Override
    public Slider newSlider(String name) {
        return newSlider(name, new SimpleSlider());
    }

    @Override
    public Slider getSlider(String name) {
        Map.Entry<Component, Slider> tmp = sliders.get(name);;
        if(tmp != null) return tmp.getValue();
        return null;
    }

    @Override
    public void removeSlider(String name) {
        Map.Entry<Component, Slider>  tmp = sliders.remove(name);;
        if(tmp != null) board.remove(tmp.getKey());
    }

    private Slider newSlider(String name, Slider slider){
        this.removeSlider(name); // if there's a slider with the same name remove it first

        // setting up the title
        JLabel wrapper = new JLabel(name); // do something :)
        wrapper.setFont(LABEL_FONT);
       // wrapper.setMaximumSize(new Dimension(10, 10));

        wrapper.setHorizontalAlignment(JLabel.CENTER);
        board.add(wrapper);
        board.add(slider.getComponent());

        Map.Entry<Component, Slider>  tmp = new MyEntry(wrapper, slider);
        sliders.put(name, tmp);
        return slider;
    }

    @Override
    public Component getComponent() {
        return board;
    }
}
