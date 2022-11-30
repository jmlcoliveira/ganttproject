package es.group.work.features.event;

import biz.ganttproject.core.calendar.GPCalendarListener;
import net.sourceforge.ganttproject.task.event.TaskListener;
public interface ChangeAdapter extends TaskListener, GPCalendarListener {
    void setListener(ChangeListener listener);
    void reset();
}
