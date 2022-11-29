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

import biz.ganttproject.core.time.CalendarFactory;
import biz.ganttproject.core.time.GanttCalendar;
import com.google.common.base.Supplier;
import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskContainmentHierarchyFacade;
import net.sourceforge.ganttproject.task.TaskMutator;
import net.sourceforge.ganttproject.task.algorithm.DependencyGraph.Node;
import java.util.Collection;
import java.util.Date;


/**
 * For now this is equal to the schedulerImpl but should implement the first feature
 *
 * @author r.goncalo
 *
 */
public class ExtendUncompletedTaskAlgorithm extends AlgorithmBase {

    private final DependencyGraph myGraph;
    private boolean isRunning;
    private final Supplier<TaskContainmentHierarchyFacade> myTaskHierarchy;
    private final SchedulerImpl scheduler; //this will be called everytime we make a duration change
    private Date tomorrowDate;

    public ExtendUncompletedTaskAlgorithm(DependencyGraph graph, Supplier<TaskContainmentHierarchyFacade> taskHierarchy, SchedulerImpl scheduler) {
        myGraph = graph;
        myTaskHierarchy = taskHierarchy;
        this.scheduler = scheduler;

        long currentMilliSeconds = System.currentTimeMillis();
        this.tomorrowDate = new Date(currentMilliSeconds + ( 23*60*60*1000 - (currentMilliSeconds % (24*60*60*1000)))); //create day tomorrow at 0h:00m
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

                        if(task.getCompletionPercentage() < 100 && task.getEnd().getTime().before(tomorrowDate)){

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
        scheduler.run();
    }

    private void extendUncompletedTasks(Node node) {

        Task task = node.getTask();

        if(task.getCompletionPercentage() < 100 && task.getEnd().getTime().before(tomorrowDate)){

            modifyTaskEnd(task, tomorrowDate);

        }

    }

    private void modifyTaskEnd(Task task, Date newEnd) {

        if (task.getEnd().getTime().equals(newEnd)) {
            return;
        }
        GanttCalendar newEndCalendar = CalendarFactory.createGanttCalendar(newEnd);
        if (getDiagnostic() != null) {
            getDiagnostic().addModifiedTask(task, null, newEnd);
        }
        TaskMutator mutator = task.createMutator();
        mutator.setEnd(newEndCalendar);
        mutator.commit();
    }


}
