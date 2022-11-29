package es.group.work.feature;

import biz.ganttproject.core.calendar.GPCalendar;
import biz.ganttproject.core.calendar.GPCalendarCalc;
import biz.ganttproject.core.calendar.GPCalendarListener;
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

    public Statistics(GanttProject project, ChartPanel mainPanel) {
        this.manager = project.getTaskManager();
        this.calendar = project.getActiveCalendar();

        // keep changing things from here :)
        JPanel featurePanel = new JPanel();
        mainPanel.getLeftPanel().add(featurePanel, BorderLayout.SOUTH);
        featurePanel.add(new JLabel("Hello, world"));

        calendar.addListener(new GPCalendarListener() {
            @Override
            public void onCalendarChange() {
                printStats();
            }
        });
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
        //
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
