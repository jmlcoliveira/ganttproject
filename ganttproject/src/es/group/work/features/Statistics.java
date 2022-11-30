package es.group.work.features;

import biz.ganttproject.core.calendar.GPCalendarCalc;
import es.group.work.features.event.ChangeAdapter;
import es.group.work.features.event.ChangeListener;
import es.group.work.features.event.TaskChange;
import es.group.work.features.sliders.MyManager;
import es.group.work.features.sliders.Slider;
import es.group.work.features.sliders.SliderManager;
import net.sourceforge.ganttproject.ChartPanel;
import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.*;
import net.sourceforge.ganttproject.task.algorithm.ExtendUncompletedTaskAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class Statistics{

    private static final int MAX_COMPLETION = 100;
    private static final String COMPLETED = "completed";
    private static final String DELAYED = "delayed";
    private static final String UNCOMPLETED = "uncompleted";
    private static final String EXTEND_MESSAGE = "Extend late tasks";
    private static  final String STATS_TITLE = "Statistics";
    private static  final Font TITLE_FONT = new Font("Courier", Font.BOLD,15);

    private TaskManager manager;
    private GPCalendarCalc calendar;

    private SliderManager sliderManager;

    private ExtendUncompletedTaskAlgorithm extendTasks;

    public Statistics(GanttProject project, ChartPanel mainPanel) {
        this.manager = project.getTaskManager();
        this.calendar = project.getActiveCalendar();
        this.sliderManager = new MyManager();
        this.setupGui(mainPanel);
        this.setupEvents();
        this.extendTasks = project.getTaskManager().getAlgorithmCollection().getExtendUncompletedTaskAlgorithm();
    }

    private  void setupEvents(){
        ChangeAdapter adapter = new TaskChange();
        adapter.setListener(new ChangeListener() {
            @Override
            public void changed() {
                printStats();
            }
        });

        calendar.addListener(adapter);
        manager.addTaskListener(adapter);
    }
    private  Component setupTitle(){
        JLabel title = new JLabel(STATS_TITLE);
        title.setFont(TITLE_FONT);
        title.setHorizontalAlignment(JLabel.CENTER);
        JPanel title_panel = new JPanel();
        title_panel.add(title);
        return title_panel;
    }
    private Component setupSliders(){
        sliderManager.newSlider(COMPLETED);
        sliderManager.newSlider(UNCOMPLETED);
        sliderManager.newSlider(DELAYED);
        return sliderManager.getComponent();
    }

    private Component setupButton(){
        JPanel buttonPanel = new JPanel();
        JButton button = new JButton(EXTEND_MESSAGE);

        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                extendTasks.run();
            }
        });

        // set's the panel layout
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(new JSeparator());
        buttonPanel.add(button);

        // ask to be placed in the center
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return buttonPanel;
    }

    private void setupGui(ChartPanel mainPanel){
        JPanel statsPanel =  new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        mainPanel.getLeftPanel().add(statsPanel, BorderLayout.SOUTH);

        statsPanel.add(this.setupTitle());
        statsPanel.add(this.setupSliders());
        statsPanel.add(this.setupButton());
    }

    private void printStats(){

        final Task[] tasks = manager.getTasks();
        Date today = new Date();
        int completed = 0;
        int delayed = 0;

        for(Task t : tasks){
            if(t.getCompletionPercentage() == MAX_COMPLETION) completed++;
            else if(t.getEnd().getTime().before(today)) delayed++;
        }
        int uncompleted = tasks.length - completed;
        int total = tasks.length;

        String[] keys = {COMPLETED, UNCOMPLETED, DELAYED};
        int[] values = {completed, uncompleted, delayed};

        for(int i = 0; i < keys.length; i++){
            float progress = total == 0 ? 0f : (float) values[i] / total * 100.0f;
            Slider slider  = sliderManager.getSlider(keys[i]);
            slider.setProgress(progress);
        }
    }

}
