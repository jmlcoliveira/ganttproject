package es.group.work.features;

import biz.ganttproject.core.calendar.GPCalendarCalc;
import es.group.work.features.event.ChangeAdapter;
import es.group.work.features.event.ChangeListener;
import es.group.work.features.event.TaskChange;
import es.group.work.features.sliders.MyManager;
import es.group.work.features.sliders.SliderManager;
import net.sourceforge.ganttproject.ChartPanel;
import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.*;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class Statistics{

    private static final int MAX_COMPLETION = 100;

    private static final String COMPLETED = "completed";
    private static final String DELAYED = "delayed";
    private static final String UNCOMPLETED = "uncompleted";
    private TaskManager manager;
    private GPCalendarCalc calendar;

    private SliderManager sliderManager;

    public Statistics(GanttProject project, ChartPanel mainPanel) {
        this.manager = project.getTaskManager();
        this.calendar = project.getActiveCalendar();
        this.sliderManager = new MyManager();
        this.setupGui(mainPanel);
        this.setupEvents();
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
        JLabel title = new JLabel("Statistics");
        title.setFont(new Font("Courier", Font.BOLD,15));
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

        JButton button = new JButton();

        return button;

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

        System.out.println("----------------------------------");
        System.out.println("uncompleted = " + uncompleted);
        System.out.println("completed = " + completed);
        System.out.println("delayed = " + delayed);
        System.out.println("total = " + total);

        System.out.println("calcPercentage(completed, total) = " + calcPercentage(completed, total));
        System.out.println("calcPercentage(uncompleted, total) = " + calcPercentage(uncompleted, total));
        System.out.println("calcPercentage(delayed, total) = " + calcPercentage(delayed, total));

        sliderManager.getSlider(COMPLETED).setProgress(calcPercentage(completed, total));
        sliderManager.getSlider(UNCOMPLETED).setProgress(calcPercentage(uncompleted, total));
        sliderManager.getSlider(DELAYED).setProgress(calcPercentage(delayed, total));
    }

    private  float calcPercentage(int portion, int total){
        if(total == 0) return 0f;
        return (float) portion / total * 100.0f;
    }



}
