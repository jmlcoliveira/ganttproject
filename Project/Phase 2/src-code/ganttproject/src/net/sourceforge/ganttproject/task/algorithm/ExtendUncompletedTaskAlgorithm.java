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
import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskMutator;
import net.sourceforge.ganttproject.task.algorithm.DependencyGraph.Node;

import java.util.Collection;
import java.util.Date;


/**
 * This algorithm extends tasks that are not complete but ended in the past, with some extra functionalities public to the outside
 * Sometimes we refer to "end of current or previous work day", this refers to the end of the current day if it is an work day
 * Or the end of the previous work day if today is not an work day
 *
 * @author r.goncalo
 *
 */
public class ExtendUncompletedTaskAlgorithm extends AlgorithmBase {

    private final DependencyGraph myGraph; //used to go through the tasks
    private boolean isRunning;
    private Date nextWorkingDayEnd; //this is the end of the previous working day, at 0:00:00 of the next day
    private Date previousWorkingDayEnd; //this is the end of the next working day, at 0:00:00 of the day
    private  GanttProject project;

    public ExtendUncompletedTaskAlgorithm(DependencyGraph graph, WeekendCalendarImpl weekendCalendar, SchedulerImpl scheduler, GanttProject project) {
        myGraph = graph;

        long currentTimeMillis = System.currentTimeMillis();

        defineEndNextWorkDay(weekendCalendar, currentTimeMillis);
        defineEndPreviousWorkDay(weekendCalendar, currentTimeMillis);

        this.project = project;


    }

        /**
         *
         * defines the end of the current or previous working day
         *this is public for testing purposes
         *
         * @param weekendCalendar
         */
    public void defineEndPreviousWorkDay(WeekendCalendarImpl weekendCalendar, long milliSecondsNow){

        //current date
        long currentMilliSeconds = milliSecondsNow;
        Date currentDate = new Date(currentMilliSeconds);

        //is it a work date?
        Date closestWorkingDate = weekendCalendar.findClosestWorkingTime(currentDate);

        if(!closestWorkingDate.equals(currentDate)) {

            //while it's not, we go backwards in time until we find one
            while (!closestWorkingDate.equals(currentDate)) {


                currentMilliSeconds -= 24 * 60 * 60 * 1000; //we move to yesterday
                currentDate = new Date(currentMilliSeconds);
                closestWorkingDate = weekendCalendar.findClosestWorkingTime(currentDate);

            }

        }

        //we want the end of the day
        this.previousWorkingDayEnd = new Date(currentMilliSeconds + ( 23*60*60*1000 - (currentMilliSeconds % (24*60*60*1000))));

    }

    public void defineEndNextWorkDay(WeekendCalendarImpl weekendCalendar, long milliSecondsNow){

        //current date
        long currentMilliSeconds = milliSecondsNow;
        Date currentDate = new Date(currentMilliSeconds);

        //is it a work date?
        Date closestWorkingDate = weekendCalendar.findClosestWorkingTime(currentDate);


            //while it's not, we go backwards in time until we find one
        while (!closestWorkingDate.equals(currentDate)) {

            currentMilliSeconds += 24 * 60 * 60 * 1000; //we move to tommoroow
            currentDate = new Date(currentMilliSeconds);
            closestWorkingDate = weekendCalendar.findClosestWorkingTime(currentDate);

        }


        //we want the end of the day
        this.nextWorkingDayEnd = new Date(currentMilliSeconds + ( 23*60*60*1000 - (currentMilliSeconds % (24*60*60*1000))));

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
            project.refresh();//because we need to re-render
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

                        if(task.getCompletionPercentage() < 100 && taskBeforePrevWorkEnd(task)){

                            //yes the algorithm would make changes
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

    //in a similar fashion to the SchedulerImpl
    private void doRun() {

        int layers = myGraph.checkLayerValidity();
        Task task;

        for (int i = 0; i < layers; i++) {
            Collection<Node> layer = myGraph.getLayer(i);
            for (Node node : layer) {
                try {

                    task = node.getTask();
                    if(task.getCompletionPercentage() < 100 && task.getEnd().getTime().before(nextWorkingDayEnd)) {

                        extendUncompletedTask(node.getTask()).commit();

                    }


                } catch (IllegalArgumentException e) {
                    GPLogger.log(e);
                }
            }
        }

    }


    /**
     *
     * extends the time of an uncompleted task to end of the current or next work day
     *
     * @param task
     */
    public TaskMutator extendUncompletedTask(Task task) {



            return modifyTaskEnd(task, nextWorkingDayEnd);


    }

    /**
     *
     * @param task
     * @return
     */
    public TaskMutator endEarlyCompletedTask(Task task) {



        return modifyTaskEnd(task, previousWorkingDayEnd);


    }


    /**
     *
     * @param task
     * @param newDate
     * @return an uncommited mutatator with the change
     */
    private TaskMutator modifyTaskEnd(Task task, Date newDate) {

        TaskMutator mutator = task.createMutator();
        if(task.getEnd().getTime().equals(newDate)) {
            return mutator;
        }else {

            GanttCalendar newEndCalendar = CalendarFactory.createGanttCalendar(newDate);
            if (getDiagnostic() != null) {
                getDiagnostic().addModifiedTask(task, null, newDate);
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
    public boolean taskBeforePrevWorkEnd(Task task){

        return task.getEnd().getTime().before(previousWorkingDayEnd);

    }
    /**
     *
     * @param task
     *
     * @return true if the task ends before this or after working day
     */
    public boolean taskAfterPrevWorkEnd(Task task){

        return task.getEnd().getTime().after(previousWorkingDayEnd);

    }

    /**
     *
     * @param task
     *
     * @return true if the task starts before this or previous working day
     */
    public boolean taskStartsBefPrevWorkEnd(Task task){

        return task.getStart().getTime().before(previousWorkingDayEnd);

    }
}
