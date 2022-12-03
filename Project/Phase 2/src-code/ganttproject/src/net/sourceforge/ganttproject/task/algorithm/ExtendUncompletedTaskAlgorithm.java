/*
Copyright 2003-2012 Dmitry Barashev, GanttProject Team

This file is part of GanttProject, an opensource project management tool.

GanttProject is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

GanttProject is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with GanttProject.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sourceforge.ganttproject.task.algorithm;

import biz.ganttproject.core.calendar.WeekendCalendarImpl;
import biz.ganttproject.core.time.CalendarFactory;
import biz.ganttproject.core.time.GanttCalendar;
import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskMutator;
import net.sourceforge.ganttproject.task.algorithm.DependencyGraph.Node;

import java.util.Collection;
import java.util.Date;


/**
 * This algorithm extends tasks that are not complete but ended in the past, with some extra functionalities public to the outside
 *
 * @author r.goncalo
 *
 */
public class ExtendUncompletedTaskAlgorithm extends AlgorithmBase {

    private final DependencyGraph myGraph; //used to go through the tasks
    private boolean isRunning;
    private Date workingDayEnd; //this is the end of the previous working day, at 0:00:00 of the next day

    public ExtendUncompletedTaskAlgorithm(DependencyGraph graph, WeekendCalendarImpl weekendCalendar, SchedulerImpl scheduler) {
        myGraph = graph;

        defineEndWorkDay(weekendCalendar, System.currentTimeMillis());

    }

    /**
     *
     * defines the end of the current or previous working day
     *this is public for testing purposes
     *
     * @param weekendCalendar
     */
    public void defineEndWorkDay(WeekendCalendarImpl weekendCalendar, long milliSecondsNow){

        //current date
        long currentMilliSeconds = milliSecondsNow;
        Date currentDate = new Date(currentMilliSeconds);

        //is it a work date?
        Date closestWorkingDate = weekendCalendar.findClosestWorkingTime(currentDate);

        //while it's not, we go backwards in time
        while(!closestWorkingDate.equals(currentDate)){


            currentMilliSeconds = currentMilliSeconds - 24*60*60*1000; //we move to yesterday
            currentDate = new Date(currentMilliSeconds);
            closestWorkingDate = weekendCalendar.findClosestWorkingTime(currentDate);

        }

        //we want the end of the day
        this.workingDayEnd = new Date(currentMilliSeconds + ( 23*60*60*1000 - (currentMilliSeconds % (24*60*60*1000))));

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (isEnabled()) {
            run();
        }
    }

    @Override
    public void run() {
        if (!isEnabled() || isRunning) {
            return;
        }
        isRunning = true;
        try {
            doRun();
        } finally {
            isRunning = false;
        }
    }

    public boolean couldRun() {
        if (!isEnabled() || isRunning) {

            return false;

        }else {

            int layers = myGraph.checkLayerValidity();
            for (int i = 0; i < layers; i++) {
                Collection<Node> layer = myGraph.getLayer(i);
                for (Node node : layer) {
                    try {

                        Task task = node.getTask();

                        if(task.getCompletionPercentage() < 100 && taskBeforeWorkEnd(task)){

                            return true;

                        }


                    } catch (IllegalArgumentException e) {
                        GPLogger.log(e);
                    }
                }
            }


        }

        return false;

    }

    private void doRun() {

        int layers = myGraph.checkLayerValidity();

        for (int i = 0; i < layers; i++) {
            Collection<Node> layer = myGraph.getLayer(i);
            for (Node node : layer) {
                try {
                    extendUncompletedTasks(node);
                } catch (IllegalArgumentException e) {
                    GPLogger.log(e);
                }
            }
        }

    }


    private void extendUncompletedTasks(Node node) {

        Task task = node.getTask();

        if(task.getCompletionPercentage() < 100 && task.getEnd().getTime().before(workingDayEnd)){

            modifyTaskEnd(task).commit();

        }



    }


    /**
     *
     * modifies the task end to be in the end of this or previous working day
     *
     * @param task
     *
     * @return an uncommited mutator with the modification
     *
     */
    public TaskMutator modifyTaskEnd(Task task) {

        TaskMutator mutator = task.createMutator();
        if(task.getEnd().getTime().equals(workingDayEnd)) {
            return mutator;
        }else {

            GanttCalendar newEndCalendar = CalendarFactory.createGanttCalendar(workingDayEnd);
            if (getDiagnostic() != null) {
                getDiagnostic().addModifiedTask(task, null, workingDayEnd);
            }
            mutator.setEnd(newEndCalendar);
            return mutator;

        }
    }

    /**
     *
     * @param task
     *
     * @return true if the task ends before this or previous working day
     */
    public boolean taskBeforeWorkEnd(Task task){

        return task.getEnd().getTime().before(workingDayEnd);

    }
    /**
     *
     * @param task
     *
     * @return true if the task ends before this or after working day
     */
    public boolean taskAfterWorkEnd(Task task){

        return task.getEnd().getTime().after(workingDayEnd);

    }

    /**
     *
     * @param task
     *
     * @return true if the task starts before this or previous working day
     */
    public boolean taskStartsBefWorkEnd(Task task){

        return task.getStart().getTime().before(workingDayEnd);

    }
}
