package es.group.work.features;

import biz.ganttproject.core.calendar.GPCalendarCalc;
import biz.ganttproject.core.calendar.GPCalendarListener;
import es.group.work.features.sliders.MyManager;
import es.group.work.features.sliders.Slider;
import es.group.work.features.sliders.SliderManager;
import net.sourceforge.ganttproject.ChartPanel;
import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.event.*;
import net.sourceforge.ganttproject.task.*;

import javax.swing.*;
import java.awt.*;

public class Statistics implements TaskListener {

    private static final int MAX_COMPLETATION = 100;
    private TaskManager manager;
    private GPCalendarCalc calendar;

    // TODO: a new dispatcher (probably)
    // TODO: have this thing working and look for potentials problems§
    public Statistics(GanttProject project, ChartPanel mainPanel) {
        this.manager = project.getTaskManager();
        this.calendar = project.getActiveCalendar();
        SliderManager sliders = new MyManager();

        this.setupSliders(mainPanel);

        calendar.addListener(new GPCalendarListener() {
            @Override
            public void onCalendarChange() {
                printStats();
            }
        });
    }

    private void setupSliders(ChartPanel mainPanel){
        SliderManager manager = new MyManager();
        mainPanel.getLeftPanel().add(manager.getComponent(), BorderLayout.SOUTH);
        Slider aux = manager.newSlider("completed", 99.8f);
        manager.newSlider("uncompleted", 50);
        manager.newSlider("delayed", 30);
        aux.setProgress(30.88f);
    }
    @Override
    public void taskScheduleChanged(TaskScheduleEvent e) {}

    @Override
    public void dependencyAdded(TaskDependencyEvent e) {}

    @Override
    public void dependencyRemoved(TaskDependencyEvent e) {}


    @Override
    public void dependencyChanged(TaskDependencyEvent e) {}

    @Override
    public void taskMoved(TaskHierarchyEvent e) {
        printStats();
    }

    @Override
    public void taskPropertiesChanged(TaskPropertyEvent e) {
        printStats();
    }

    @Override
    public void taskProgressChanged(TaskPropertyEvent e) {
        printStats();
    }

    @Override
    public void taskModelReset() {}

    private void printStats(){
        // completed, uncompleted, delayed
        // VBox() => [<slide0, label0>,
        //            <slide1, label1>,
        //            <slide2, label2> ]
        final Task[] tasks = manager.getTasks();
        int completed = 0;
        for(Task t : tasks){
            if(t.getCompletionPercentage() == MAX_COMPLETATION) completed++;
        }

        System.out.println("------------------------------------------");
        System.out.printf("total tasks     : %d\n", tasks.length);
        System.out.printf("completed tasks : %d\n", completed);
        System.out.println("------------------------------------------");
    }
    @Override
    public void taskAdded(TaskHierarchyEvent e) {
        printStats();
    }

    @Override
    public void taskRemoved(TaskHierarchyEvent e) {
        printStats();
    }

}
