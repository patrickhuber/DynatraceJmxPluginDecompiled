 package com.dynatrace.diagnostics.plugin.jmx;

 import com.dynatrace.diagnostics.pdk.Status;
 import com.dynatrace.diagnostics.pdk.Task;
 import com.dynatrace.diagnostics.pdk.TaskEnvironment;
 import java.util.logging.Logger;





 public class JMXTask
   extends JMXExecutor
   implements Task
 {
   public static final Logger log = Logger.getLogger(JMXTask.class.getName());
























   public Status setup(TaskEnvironment env)
     throws Exception
   {
     return super.setup(env);
   }




















   public Status execute(TaskEnvironment env)
     throws Exception
   {
     return super.execute(env);
   }






































   public void teardown(TaskEnvironment env)
     throws Exception
   {
     super.teardown(env);
   }
 }


/* Location:              C:\Users\PRH7261\Downloads\com.dynatrace.diagnostics.plugin.jmx_1.0.9.jar!\com\dynatrace\diagnostics\plugin\jmx\JMXTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */