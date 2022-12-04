package es.group.work.features.tests;

import biz.ganttproject.core.calendar.WeekendCalendarImpl;
import junit.framework.TestCase;
import net.sourceforge.ganttproject.task.*;
import net.sourceforge.ganttproject.task.algorithm.ExtendUncompletedTaskAlgorithm;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Feature1Tests extends TestCase {


    private final static long THIRD_DECEMBER_AT_TWELVE = 1670068800000L;


    private TaskManagerImpl manager;
    private WeekendCalendarImpl weekendCalendar;

    private ExtendUncompletedTaskAlgorithm alg;
    private Random rand;


    ArrayList<Task> randomTasks;
    private final static int NUMBER_OF_TASKS = 20;


    public Feature1Tests(){

        rand = new Random();
        setup();
    }

    private void setup(){

        manager = (TaskManagerImpl) TaskManager.Access.newInstance(null, null, null, null, null);
        weekendCalendar = new WeekendCalendarImpl();
        alg = new ExtendUncompletedTaskAlgorithm(null, null, null, null);
        alg.defineEndPreviousWorkDay(weekendCalendar, THIRD_DECEMBER_AT_TWELVE);
        alg.defineEndNextWorkDay(weekendCalendar, THIRD_DECEMBER_AT_TWELVE);


        createTasks();


    }

    private void createTasks(){

        randomTasks = new ArrayList<Task>(NUMBER_OF_TASKS);
        for(int i = 0; i < NUMBER_OF_TASKS; i++){

            randomTasks.add(createTask(i));

        }

    }

    private void randomlyDistributeTasks(){


        TaskMutator mutator;
        Task task;
        for(int i = 0; i < NUMBER_OF_TASKS; i++){

            task = randomTasks.get(i);
            mutator = task.createMutator();
            mutator.setStart();

        }


    }

    public Task createTask(int id){

        return new TaskImpl(manager, id);

    }

    @Test
    void onlyExtendsTasks(){




    }

    private void onlyExtends(Task task){

        Date startDate = task.getStart().getTime();
        Date endDate = task.getEnd().getTime();
        alg.extendUncompletedTask(task);

        assert(task.getEnd().getTime().after(endDate) || task.getEnd().getTime().equals(endDate));
        assert(task.getStart().getTime().equals(endDate) || task.getEnd().getTime().equals(endDate));


    }



}
