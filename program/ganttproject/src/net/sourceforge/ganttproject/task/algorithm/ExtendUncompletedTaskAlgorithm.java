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

    private final DependencyGraph myGraph;
    private boolean isRunning;
    private final SchedulerImpl scheduler; //this will be called everytime we make a duration change
    private Date nextWorkingDayEnd; // this is the end of the next working day, at 0:00:00

    public ExtendUncompletedTaskAlgorithm(DependencyGraph graph, WeekendCalendarImpl weekendCalendar, SchedulerImpl scheduler) {
        myGraph = graph;
        this.scheduler = scheduler;

        defineEndOfNextWorkingDay(weekendCalendar);

    }

    private void defineEndOfNextWorkingDay(WeekendCalendarImpl weekendCalendar){

        long currentMilliSeconds = System.currentTimeMillis();
        Date currentDate = new Date(currentMilliSeconds);
        Date closestWorkingDate = weekendCalendar.findClosestWorkingTime(currentDate);

        while(!closestWorkingDate.equals(currentDate)){


            currentMilliSeconds = currentMilliSeconds + 24*60*60*1000;
            currentDate = new Date(currentMilliSeconds);
            closestWorkingDate = weekendCalendar.findClosestWorkingTime(currentDate);

        }

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

                        if(task.getCompletionPercentage() < 100 && taskBeforeNextWorkingEnd(task)){

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


        if(task.getCompletionPercentage() < 100 && task.getEnd().getTime().before(nextWorkingDayEnd)){

            modifyTaskEndToNextWorkingEnd(task).commit();

        }



    }


    public TaskMutator modifyTaskEndToNextWorkingEnd(Task task) {

        TaskMutator mutator = task.createMutator();
        if(task.getEnd().getTime().equals(nextWorkingDayEnd)) {
            return mutator;
        }else {

            if(task.getStart().getTime().after(nextWorkingDayEnd) || task.getStart().getTime().equals(nextWorkingDayEnd)){

                GanttCalendar newStartCalendar = CalendarFactory.createGanttCalendar(new Date (nextWorkingDayEnd.getTime() - 24*60*60*1000));
                
                mutator.setStart(newStartCalendar);

            }

            GanttCalendar newEndCalendar = CalendarFactory.createGanttCalendar(nextWorkingDayEnd);
            if (getDiagnostic() != null) {
                getDiagnostic().addModifiedTask(task, null, nextWorkingDayEnd);
            }
            mutator.setEnd(newEndCalendar);
            return mutator;

        }
    }

    public boolean taskBeforeNextWorkingEnd(Task task){

        return task.getEnd().getTime().before(nextWorkingDayEnd);

    }

    public boolean taskAfterNextWorkingEnd(Task task){

        return task.getEnd().getTime().after(nextWorkingDayEnd);

    }



}
