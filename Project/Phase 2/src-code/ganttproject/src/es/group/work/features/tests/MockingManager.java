package es.group.work.features.tests;

import biz.ganttproject.core.calendar.GPCalendarCalc;
import biz.ganttproject.core.calendar.GPCalendarListener;
import biz.ganttproject.core.option.ColorOption;
import biz.ganttproject.core.option.EnumerationOption;
import biz.ganttproject.core.option.StringOption;
import biz.ganttproject.core.time.CalendarFactory;
import biz.ganttproject.core.time.GanttCalendar;
import biz.ganttproject.core.time.TimeDuration;
import biz.ganttproject.core.time.TimeUnit;
import net.sourceforge.ganttproject.CustomPropertyDefinition;
import net.sourceforge.ganttproject.CustomPropertyManager;
import net.sourceforge.ganttproject.GanttTask;
import net.sourceforge.ganttproject.ProjectEventListener;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskContainmentHierarchyFacade;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.TaskMutator;
import net.sourceforge.ganttproject.task.algorithm.AlgorithmCollection;
import net.sourceforge.ganttproject.task.algorithm.DependencyGraph;
import net.sourceforge.ganttproject.task.dependency.TaskDependencyCollection;
import net.sourceforge.ganttproject.task.dependency.TaskDependencyConstraint;
import net.sourceforge.ganttproject.task.event.TaskListener;

import java.text.DateFormat;
import java.util.*;

public class MockingManager implements TaskManager {

    private List<Task> tasks;

    public MockingManager(){
        this.tasks = new ArrayList<>();
    }


    public Task createTask(Date startTime){
        GanttCalendar start = CalendarFactory.createGanttCalendar(startTime);
        GanttCalendar end = CalendarFactory.createGanttCalendar(addOneDay(startTime));
        Task t = new MockingTask(start, end);
        this.tasks.add(t);
        return t;
    }

    private Date addOneDay(Date date){
        Calendar cal  = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    public Task createCompletedTask(Date start){
        Task t = this.createTask(start);
        t.setCompletionPercentage(100); // 100 means completed
        return t;
    }


    /**
     * Deletes all the tasks of the manager
     */
    public void clear(){
        tasks.clear();
    }


    @Override
    public Task[] getTasks() {
        return this.tasks.toArray(new Task[0]);
    }

    // from here on we don't actually care :)
    @Override
    public void taskCommitYesNo(TaskMutator mutatorToCommit, String message, String title) {

    }

    @Override
    public TaskBuilder newTaskBuilder() {
        return null;
    }


    @Override
    public Task getRootTask() {
        return null;
    }

    @Override
    public GanttTask getTask(int taskId) {
        return null;
    }

    @Override
    public void registerTask(Task task) {

    }

    @Override
    public GanttTask createTask() {
        return null;
    }

    @Override
    public GanttTask createTask(int taskId) {
        return null;
    }

    @Override
    public String encode(TimeDuration duration) {
        return null;
    }

    @Override
    public TimeDuration createLength(String lengthAsString) {
        return null;
    }

    @Override
    public TimeDuration createLength(long length) {
        return null;
    }

    @Override
    public TimeDuration createLength(TimeUnit unit, float length) {
        return null;
    }

    @Override
    public TimeDuration createLength(TimeUnit timeUnit, Date startDate, Date endDate) {
        return null;
    }

    @Override
    public Date shift(Date original, TimeDuration duration) {
        return null;
    }

    @Override
    public TaskDependencyCollection getDependencyCollection() {
        return null;
    }

    @Override
    public AlgorithmCollection getAlgorithmCollection() {
        return null;
    }

    @Override
    public TaskDependencyConstraint createConstraint(TaskDependencyConstraint.Type constraintType) {
        return null;
    }

    @Override
    public GPCalendarCalc getCalendar() {
        return null;
    }

    @Override
    public TaskContainmentHierarchyFacade getTaskHierarchy() {
        return null;
    }

    @Override
    public void addTaskListener(TaskListener listener) {

    }

    @Override
    public TimeDuration getProjectLength() {
        return null;
    }

    @Override
    public int getTaskCount() {
        return 0;
    }

    @Override
    public Date getProjectStart() {
        return null;
    }

    @Override
    public Date getProjectEnd() {
        return null;
    }

    @Override
    public int getProjectCompletion() {
        return 0;
    }

    @Override
    public TaskManager emptyClone() {
        return null;
    }

    @Override
    public Map<Task, Task> importData(TaskManager taskManager, Map<CustomPropertyDefinition, CustomPropertyDefinition> customPropertyMapping) {
        return null;
    }

    @Override
    public void importAssignments(TaskManager importedTaskManager, HumanResourceManager hrManager, Map<Task, Task> original2importedTask, Map<HumanResource, HumanResource> original2importedResource) {

    }

    @Override
    public void processCriticalPath(Task root) {

    }

    @Override
    public void deleteTask(Task tasktoRemove) {

    }

    @Override
    public CustomPropertyManager getCustomPropertyManager() {
        return null;
    }

    @Override
    public StringOption getTaskNamePrefixOption() {
        return null;
    }

    @Override
    public StringOption getTaskCopyNamePrefixOption() {
        return null;
    }

    @Override
    public ColorOption getTaskDefaultColorOption() {
        return null;
    }

    @Override
    public EnumerationOption getDependencyHardnessOption() {
        return null;
    }

    @Override
    public void setZeroMilestones(Boolean b) {

    }

    @Override
    public Boolean isZeroMilestones() {
        return null;
    }

    @Override
    public DependencyGraph getDependencyGraph() {
        return null;
    }

    @Override
    public ProjectEventListener getProjectListener() {
        return null;
    }

    @Override
    public GPCalendarListener getCalendarListener() {
        return null;
    }
}
