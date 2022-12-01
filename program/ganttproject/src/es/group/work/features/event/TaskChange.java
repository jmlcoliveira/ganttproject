package es.group.work.features.event;

import net.sourceforge.ganttproject.task.event.*;

public class TaskChange implements ChangeAdapter{
    private ChangeListener listener;

    public TaskChange(){
        listener = null;
    }

    @Override
    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void reset() {
        listener = null;
    }

    private void fire(){
        if(listener != null) listener.changed();
    }


    @Override
    public void taskScheduleChanged(TaskScheduleEvent e) {
        this.fire();
    }


    @Override
    public void taskAdded(TaskHierarchyEvent e) {
        this.fire();
    }

    @Override
    public void taskRemoved(TaskHierarchyEvent e) {
        this.fire();
    }

    @Override
    public void taskMoved(TaskHierarchyEvent e) {
        this.fire();
    }

    @Override
    public void taskPropertiesChanged(TaskPropertyEvent e) {
        this.fire();
    }

    @Override
    public void taskProgressChanged(TaskPropertyEvent e) {
        this.fire();
    }
     @Override
    public void onCalendarChange() {
        this.fire();
    }

    @Override
    public void projectOpened() {
        this.fire();
    }

    @Override
    public void projectCreated() {
        this.fire();
    }
    @Override
    public void dependencyAdded(TaskDependencyEvent e) {}

    @Override
    public void dependencyRemoved(TaskDependencyEvent e) {}

    @Override
    public void dependencyChanged(TaskDependencyEvent e) {}
    @Override
    public void taskModelReset() {}

    @Override
    public void projectModified() {}

    @Override
    public void projectSaved() {}

    @Override
    public void projectClosed() {}


}
