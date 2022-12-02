package es.group.work.features.tests;

import es.group.work.features.statistics.Statistics;
import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Feature2Tests extends TestCase{

   private MockingManager manager;

   private final Date today;
   private final Date fiveDaysAgo;

   private final Random rand;

   public Feature2Tests(){
      manager = new MockingManager();
      today = new Date();
      fiveDaysAgo = calcFiveDaysAgo();
      rand = new Random();
   }

   private Date calcFiveDaysAgo(){
      Calendar cal = Calendar.getInstance();
      cal.setTime(today);
      cal.add(Calendar.DATE, -5); // delays day by 5 days
      return cal.getTime();
   }

   public void testCompleted(){
      manager.clear(); // deletes all the task from the task manager
      Statistics st = new Statistics(manager);
      int completed = 0;

      // start adding tasks
      for(int i = 0; i < this.genTaskNumber(); i++){
         // decides if the task will be delayed
         boolean isCompleted = rand.nextInt(2) == 0;
         if(isCompleted){
           manager.createCompletedTask(today);
           ++completed;
         }else
            manager.createTask(today);

         st.calcParameters();
         assertEquals(completed, st.getCompletedTasks());
      }

   }

   public void testUncompleted(){
      manager.clear();
      Statistics st = new Statistics(manager);
      int uncompleted = 0;

      for(int i = 0; i < this.genTaskNumber(); i++){
         boolean isUncompleted = rand.nextInt(2)  == 0;
         if(isUncompleted){
            manager.createTask(today);
            ++uncompleted;
         } else
            manager.createCompletedTask(today);
      }
      st.calcParameters();
      assertEquals(uncompleted, st.getUncompletedTasks());
   }

   public void testDelayed(){
      manager.clear();

      Statistics st = new Statistics(manager);
      int delayed = 0; // tasks that are no completed and it's end time has passed

      for(int i = 0; i < this.genTaskNumber(); i++){
         boolean isDelayed = rand.nextInt(2)  == 0;
         if(isDelayed){
            manager.createTask(fiveDaysAgo);
            ++delayed;
         } else
            manager.createCompletedTask(today);
      }
      st.calcParameters();
      assertEquals(delayed, st.getDelayedTasks());
   }

   /**
    * @return  the number of tasks should be created by the test
    */
   private int genTaskNumber(){
      return rand.nextInt(40) + 10; // number of tasks we wil add
   }

}
