package es.group.work.features;

import es.group.work.features.event.ChangeAdapter;
import es.group.work.features.event.ChangeListener;
import es.group.work.features.event.TaskChange;
import es.group.work.features.event.TaskProgressListener;
import es.group.work.features.sliders.MyManager;
import es.group.work.features.sliders.Slider;
import es.group.work.features.sliders.SliderManager;
import es.group.work.features.statistics.Statistics;
import net.sourceforge.ganttproject.ChartPanel;
import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.algorithm.AlgorithmBase;
import net.sourceforge.ganttproject.task.algorithm.ExtendUncompletedTaskAlgorithm;
import net.sourceforge.ganttproject.task.event.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FeaturesSetup {

    // some gui related constants
    private static final String COMPLETED = "completed";
    private static final String DELAYED = "delayed";
    private static final String UNCOMPLETED = "uncompleted";
    private static final String EXTEND_MESSAGE = "Extend late tasks";
    private static  final String STATS_TITLE = "Statistics";
    private static  final Font TITLE_FONT = new Font("Courier", Font.BOLD,15);

    private static final String TASK_ENDED_EARLY_MESSAGE = "Do you want to end the task completed today?";
    private static final String TASK_ENDED_EARLY_TITLE = "Task completed early";

    // feature 2 variables
    private SliderManager sliderManager;
    private Statistics stats;

    // feature 1 variables

    //an algorithm that extends the duration of unfinished tasks that should have ended in the past, with implementation in the task's algorithms package
    private TaskManager taskManager;



    public FeaturesSetup(GanttProject project, ChartPanel mainPanel) {
        this.stats = new Statistics(project.getTaskManager());
        this.sliderManager = new MyManager();

        this.taskManager = project.getTaskManager();

        // set's up the gui and the events
        this.setupGui(mainPanel);
        this.setupEvents(project);
    }

    private  void setupEvents(GanttProject project){
        ChangeAdapter adapter = new TaskChange();

        adapter.setListener(new ChangeListener() {
            @Override
            public void changed() {
                updateSliders();
            }
        });

        // set's some events listeners :)
        project.getActiveCalendar().addListener(adapter);
        project.getTaskManager().addTaskListener(adapter);

        taskManager.addTaskListener(new TaskProgressListener() {

            @Override
            public void taskProgressChanged(TaskPropertyEvent e) {

                Task task = e.getTask();
                ExtendUncompletedTaskAlgorithm extendAlg = taskManager.getAlgorithmCollection().getExtendUncompletedTaskAlgorithm();
                AlgorithmBase squedImpl = taskManager.getAlgorithmCollection().getScheduler();
                if(task.getCompletionPercentage() == 100 && taskManager.getAlgorithmCollection().getExtendUncompletedTaskAlgorithm().taskAfterNextWorkingEnd(task)){

                    taskManager.taskCommitYesNo(extendAlg.modifyTaskEndToNextWorkingEnd(task), TASK_ENDED_EARLY_MESSAGE, TASK_ENDED_EARLY_TITLE);
                    squedImpl.run();
                }

            }
        });


    }
    private  Component setupTitle(){
        JLabel title = new JLabel(STATS_TITLE);
        title.setFont(TITLE_FONT);
        title.setHorizontalAlignment(JLabel.CENTER);
        JPanel title_panel = new JPanel();
        title_panel.add(title);
        return title_panel;
    }
    private Component setupSliders(){
        // think about this later :)
        sliderManager.newSlider(COMPLETED);
        sliderManager.newSlider(UNCOMPLETED);
        sliderManager.newSlider(DELAYED);
        this.syncSliders();
        return sliderManager.getComponent();
    }

    private Component setupButton(){
        JPanel buttonPanel = new JPanel();
        JButton button = new JButton(EXTEND_MESSAGE);

        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                taskManager.getAlgorithmCollection().getExtendUncompletedTaskAlgorithm().run();
            }
        });

        // set's the panel layout
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(new JSeparator());
        buttonPanel.add(button);

        // set the button and buttonPanel position to center
        for(JComponent cmp : new JComponent[]{button, buttonPanel}){
            cmp.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        return buttonPanel;
    }

    private void setupGui(ChartPanel mainPanel){
        JPanel statsPanel =  new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        mainPanel.getLeftPanel().add(statsPanel, BorderLayout.SOUTH);

        statsPanel.add(this.setupTitle());
        statsPanel.add(this.setupSliders());
        statsPanel.add(this.setupButton());
    }

    private void updateSliders(){
        stats.calcParameters();
        syncSliders();
    }

    private void syncSliders(){
        String[] keys = {COMPLETED, UNCOMPLETED, DELAYED};
        int[] values = {
                stats.getCompletedTasks(),
                stats.getUncompletedTasks(),
                stats.getDelayedTasks()
        };

        int total = stats.getTotalTask();
        for(int i = 0; i < keys.length; i++){
            float progress = total == 0 ? 0f : (float) values[i] / total * 100.0f;
            Slider slider  = sliderManager.getSlider(keys[i]);
            slider.setProgress(progress);
        }
    }
}
