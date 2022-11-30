package es.group.work.features.statistics;

import biz.ganttproject.core.calendar.GPCalendarCalc;
import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskManager;

import java.util.Date;

public class Statistics {

    private static final int MAX_COMPLETION = 100;

    private TaskManager manager;

    private int completed, uncompleted, delayed, total;

    public Statistics(TaskManager manager){
        this.manager = manager;
        this.calcParameters();
    }

    public void calcParameters(){
        final Task[] tasks = manager.getTasks();
        Date today = new Date();
        int completed = 0;
        int delayed = 0;

        for(Task t : tasks){
            if(t.getCompletionPercentage() == MAX_COMPLETION) completed++;
            else if(t.getEnd().getTime().before(today)) delayed++;
        }

        this.uncompleted = tasks.length - completed;
        this.completed = completed;
        this.delayed = delayed;
        this.total = tasks.length;
    }

    public int getTotalTask(){
        return total;
    }

    public int getCompletedTasks(){
        return completed;
    }

    public int getUncompletedTasks(){
        return uncompleted;
    }

    public int getDelayedTasks(){
        return delayed;
    }
}
