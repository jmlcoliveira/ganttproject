package es.group.work.features.event;

import net.sourceforge.ganttproject.task.event.*;

public abstract class TaskProgressListener implements TaskListener {


    @Override
    public void taskScheduleChanged(TaskScheduleEvent e) {}

    @Override
    public void dependencyAdded(TaskDependencyEvent e) {}

    @Override
    public void dependencyRemoved(TaskDependencyEvent e) {}

    @Override
    public void dependencyChanged(TaskDependencyEvent e) {}

    @Override
    public void taskAdded(TaskHierarchyEvent e) {}

    @Override
    public void taskRemoved(TaskHierarchyEvent e) {}

    @Override
    public void taskMoved(TaskHierarchyEvent e) {}

    @Override
    public void taskPropertiesChanged(TaskPropertyEvent e) {}

    @Override
    public void taskModelReset() {}
}
