package es.group.work.features.tests;

import biz.ganttproject.core.chart.render.ShapePaint;
import biz.ganttproject.core.time.GanttCalendar;
import biz.ganttproject.core.time.TimeDuration;
import net.sourceforge.ganttproject.GanttTask;
import net.sourceforge.ganttproject.document.Document;
import net.sourceforge.ganttproject.task.*;
import net.sourceforge.ganttproject.task.dependency.TaskDependencySlice;

import java.awt.*;
import java.util.Calendar;
import java.util.List;

public class MockingTask implements Task {

    private  GanttCalendar start;
    private GanttCalendar end;
    private int completation;

    public MockingTask(GanttCalendar start, GanttCalendar end) {
        this.start = start;
        this.end = end;
        this.completation = 0;
    }

    @Override
    public void setStart(GanttCalendar start) {
        this.start = start;
    }

    @Override
    public void setEnd(GanttCalendar end) {
        this.end = end;
    }

    @Override
    public GanttCalendar getStart() {
        return start;
    }

    @Override
    public GanttCalendar getEnd() {
        return end;
    }

    @Override
    public int getCompletionPercentage() {
        return completation;
    }

    @Override
    public void setCompletionPercentage(int percentage) {
        int tmp = Math.min(percentage,  100); // max percentage 100
        this.completation = Math.max(tmp, 0); // min percentage 0
    }

    // from here on we don't actually care :)
    @Override
    public void setName(String name) {}

    @Override
    public void setMilestone(boolean isMilestone) {}

    @Override
    public void setPriority(Priority priority) {}

    @Override
    public void setDuration(TimeDuration length) {}

    @Override
    public void shift(TimeDuration shift) {}


    @Override
    public void setShape(ShapePaint shape) {}

    @Override
    public void setColor(Color color) {}

    @Override
    public void setWebLink(String webLink) {}

    @Override
    public void setNotes(String notes) {}

    @Override
    public void addNotes(String notes) {}

    @Override
    public void setExpand(boolean expand) {}

    @Override
    public void setCritical(boolean critical) {}

    @Override
    public void setTaskInfo(TaskInfo taskInfo) {}

    @Override
    public void setProjectTask(boolean projectTask) {}

    @Override
    public Cost getCost() {
        return null;
    }

    @Override
    public TaskMutator createMutator() {
        return null;
    }

    @Override
    public TaskMutator createMutatorFixingDuration() {
        return null;
    }

    @Override
    public int getTaskID() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isMilestone() {
        return false;
    }

    @Override
    public Priority getPriority() {
        return null;
    }

    @Override
    public List<TaskActivity> getActivities() {
        return null;
    }

    @Override
    public GanttCalendar getDisplayEnd() {
        return null;
    }

    @Override
    public TimeDuration getDuration() {
        return null;
    }

    @Override
    public TimeDuration translateDuration(TimeDuration duration) {
        return null;
    }


    @Override
    public ShapePaint getShape() {
        return null;
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public String getNotes() {
        return null;
    }

    @Override
    public boolean getExpand() {
        return false;
    }

    @Override
    public ResourceAssignment[] getAssignments() {
        return new ResourceAssignment[0];
    }

    @Override
    public TaskDependencySlice getDependencies() {
        return null;
    }

    @Override
    public TaskDependencySlice getDependenciesAsDependant() {
        return null;
    }

    @Override
    public TaskDependencySlice getDependenciesAsDependee() {
        return null;
    }

    @Override
    public ResourceAssignmentCollection getAssignmentCollection() {
        return null;
    }

    @Override
    public Task getSupertask() {
        return null;
    }

    @Override
    public Task[] getNestedTasks() {
        return new Task[0];
    }

    @Override
    public void move(Task targetSupertask) {}

    @Override
    public void move(Task targetSupertask, int position) {}

    @Override
    public void delete() {}

    @Override
    public TaskManager getManager() {
        return null;
    }

    @Override
    public Task unpluggedClone() {
        return null;
    }

    @Override
    public CustomColumnsValues getCustomValues() {
        return null;
    }

    @Override
    public boolean isCritical() {
        return false;
    }

    @Override
    public GanttCalendar getThird() {
        return null;
    }

    @Override
    public void applyThirdDateConstraint() {}

    @Override
    public int getThirdDateConstraint() {
        return 0;
    }

    @Override
    public void setThirdDate(GanttCalendar thirdDate) {}

    @Override
    public void setThirdDateConstraint(int dateConstraint) {}

    @Override
    public TaskInfo getTaskInfo() {
        return null;
    }

    @Override
    public boolean isProjectTask() {
        return false;
    }

    @Override
    public boolean isSupertask() {
        return false;
    }

    @Override
    public List<Document> getAttachments() {
        return null;
    }
}
