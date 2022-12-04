package es.group.work.features.tests;

import biz.ganttproject.core.calendar.WeekendCalendarImpl;
import junit.framework.TestCase;
import net.sourceforge.ganttproject.task.*;
import net.sourceforge.ganttproject.task.algorithm.ExtendUncompletedTaskAlgorithm;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class Feature1Tests extends TestCase {


    private final static long THIRD_DECEMBER_AT_TWELVE = 1670068800000L;


    private TaskManager manager;
    private WeekendCalendarImpl weekendCalendar;

    private ExtendUncompletedTaskAlgorithm alg;


    private final static int NUMBER_OF_TASKS = 20;


    public Feature1Tests(){

        setup();
    }

    private void setup(){

        manager = new MockingManager();
        weekendCalendar = new WeekendCalendarImpl();
        alg = new ExtendUncompletedTaskAlgorithm(null, null, null, null);
        alg.defineEndPreviousWorkDay(weekendCalendar, THIRD_DECEMBER_AT_TWELVE);
        alg.defineEndNextWorkDay(weekendCalendar, THIRD_DECEMBER_AT_TWELVE);


        createTasks();


    }

    public void tearDown(){

        manager = null;
        weekendCalendar = null;
        alg = null;


    }

    private void createTasks(){

        for(int i = 0; i < NUMBER_OF_TASKS; i++){

            manager.createTask();

        }

    }


    public void testOnlyExtendsTasks(){

        setup();

        Task[] tasks = manager.getTasks();
        for(int i = 0; i < NUMBER_OF_TASKS; i++){

            onlyExtends(tasks[i]);

        }

        tearDown();


    }

    private void onlyExtends(Task task){

        Date startDate = task.getStart().getTime();
        Date endDate = task.getEnd().getTime();
        alg.extendUncompletedTask(task);

        assert(task.getEnd().getTime().after(endDate) || task.getEnd().getTime().equals(endDate));
        assert(task.getStart().getTime().equals(startDate));


    }

    @Test
    void onlyEndsTasksEarly(){

        Task[] tasks = manager.getTasks();
        for(int i = 0; i < NUMBER_OF_TASKS; i++){

            onlyEndsEarly(tasks[i]);

        }


    }

    private void onlyEndsEarly(Task task){

        Date startDate = task.getStart().getTime();
        Date endDate = task.getEnd().getTime();
        alg.endEarlyCompletedTask(task);

        assert(task.getEnd().getTime().before(endDate) || task.getEnd().getTime().equals(endDate));
        assert(task.getStart().getTime().equals(startDate));


    }



}
